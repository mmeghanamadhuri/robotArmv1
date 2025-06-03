package com.example.roboticarm;

import android.os.AsyncTask;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RobotCom {
    public Long[] BAUDRATES = {2000000L, 1000000L, 500000L, 222222L, 117647L, 100000L, 57142L, 9615L};
    public byte[] LatestReceivedBytes = new byte[512];
    public MotorTypes MOTORTYPES = new MotorTypes();
    public boolean checkLatest;
    public TcpClient mTcpClient;
    public String serverip;

    public class MotorTypes {
        public byte AX12 = 0;
        public byte AX18 = 1;
        public byte MX106 = 4;
        public byte MX28 = 2;
        public byte MX64 = 3;
        public byte XL320 = 5;

        public MotorTypes() {
        }
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {
        public ConnectTask() {
        }

        /* access modifiers changed from: protected */
        public TcpClient doInBackground(String... message) {
            RobotCom.this.mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                public void messageReceived(String message) {
                    ConnectTask.this.publishProgress(new String[]{message});
                }
            });
            RobotCom.this.mTcpClient.SERVER_IP = RobotCom.this.serverip;
            RobotCom.this.mTcpClient.SERVER_PORT = 7777;
            RobotCom.this.mTcpClient.run();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.d("test", "response " + values[0]);
        }
    }

    public void openTcp(String ip) {
        this.serverip = ip;
        new ConnectTask().execute(new String[]{""});
    }

    public class sendMessageBytes extends AsyncTask<byte[], Void, String> {
        public sendMessageBytes() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(byte[]... params) {
            try {
                RobotCom.this.mTcpClient.sendMessage(params[0]);
                Log.d("LUCI_Tx", "" + Arrays.toString(params[0]));
                return "Executed";
            } catch (Exception e) {
                Log.e("SOCKET", "exception", e);
                return "Executed";
            }
        }
    }

    public class sendAndReceiveMessageBytes extends AsyncTask<byte[], Void, String> {
        public sendAndReceiveMessageBytes() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(byte[]... params) {
            try {
                RobotCom.this.LatestReceivedBytes = new byte[512];
                RobotCom.this.mTcpClient.readMessage();
                Thread.sleep(10);
                RobotCom.this.mTcpClient.sendMessage(params[0]);
                Log.d("LUCI_Tx", "" + Arrays.toString(params[0]));
                long tStart = System.currentTimeMillis();
                while (true) {
                    if (System.currentTimeMillis() - tStart >= 2000) {
                        break;
                    }
                    byte[] reply = RobotCom.this.mTcpClient.readMessage();
                    if (reply.length > 1) {
                        RobotCom.this.LatestReceivedBytes = reply;
                        RobotCom.this.checkLatest = true;
                        break;
                    }
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                Log.e("SOCKET", "exception", e);
            }
            RobotCom.this.checkLatest = true;
            return "Executed";
        }
    }

    public byte[] getLHbytes(int x) {
        return new byte[]{(byte) (x & 255), (byte) ((x >> 8) & 255)};
    }

    public void setWheelModeSyncAxMx(byte[] ids, byte[] motortypes, long baudRate) {
        byte[][] datas = (byte[][]) Array.newInstance(byte.class, new int[]{ids.length, 4});
        for (int i = 0; i < ids.length; i++) {
            if (motortypes[i] == this.MOTORTYPES.AX12 || motortypes[i] == this.MOTORTYPES.AX18) {
                datas[i][0] = 0;
                datas[i][1] = 0;
                datas[i][2] = 0;
                datas[i][3] = 0;
            } else if (motortypes[i] == this.MOTORTYPES.MX28 || motortypes[i] == this.MOTORTYPES.MX64 || motortypes[i] == this.MOTORTYPES.MX106) {
                datas[i][0] = 0;
                datas[i][1] = 0;
                datas[i][2] = 0;
                datas[i][3] = 0;
            }
        }
        new sendMessageBytes().execute(new byte[][]{LUCI_createPacket(254, 0, LUCI_WriteUARTPacket(baudRate, syncWriteAxMxPacket(4, 6, ids, datas)), (byte[]) null)});
    }

    /* access modifiers changed from: protected */
    public byte[] LuciSingleReadUartPacket(int baudRate, byte bytes2Read, byte[] uartPacket) {
        byte[] RUPacket = new byte[(uartPacket.length + 6)];
        int baudrate = Arrays.asList(this.BAUDRATES).indexOf(Long.valueOf((long) baudRate));
        RUPacket[0] = 0;
        RUPacket[1] = (byte) baudrate;
        RUPacket[2] = 1;
        RUPacket[3] = 0;
        RUPacket[4] = bytes2Read;
        RUPacket[5] = (byte) uartPacket.length;
        System.arraycopy(uartPacket, 0, RUPacket, 6, uartPacket.length);
        return RUPacket;
    }

    /* access modifiers changed from: protected */
    public byte[] LuciSyncReadUartPacket(int baudRate, byte numMotors, byte ComType, byte[] bytes2Read, List<byte[]> uartPackets) {
        int RUPacketLength = 0;
        for (int k = 0; k < numMotors; k++) {
            RUPacketLength = RUPacketLength + 2 + uartPackets.get(k).length;
        }
        byte[] RUPacket = new byte[(RUPacketLength + 4)];
        int baudrate = Arrays.asList(this.BAUDRATES).indexOf(Long.valueOf((long) baudRate));
        RUPacket[0] = 1;
        RUPacket[1] = (byte) baudrate;
        RUPacket[2] = numMotors;
        RUPacket[3] = ComType;
        int j = 4;
        for (int i = 0; i < numMotors; i++) {
            int j2 = j + 1;
            RUPacket[j] = bytes2Read[i];
            int j3 = j2 + 1;
            RUPacket[j2] = (byte) uartPackets.get(i).length;
            System.arraycopy(uartPackets.get(i), 0, RUPacket, j3, uartPackets.get(i).length);
            j = j3 + uartPackets.get(i).length;
        }
        return RUPacket;
    }

    public double[] readSyncAxMx(byte[] ids, byte[] motorTypes, int baudRate) {
        byte[] bArr = ids;
        List<byte[]> uartPackets = new ArrayList<>();
        byte[] bytes2Read = new byte[bArr.length];
        int i = 0;
        for (int k = 0; k < bArr.length; k++) {
            uartPackets.add(readPacketAxMx(bArr[k]));
            bytes2Read[k] = 26;
        }
        byte[] luciuartpacket = LuciSyncReadUartPacket(baudRate, (byte) bArr.length, (byte) 0, bytes2Read, uartPackets);
        Log.d("luciuartpacket", Arrays.toString(luciuartpacket));
        byte[] lucipacket = LUCI_createPacket(254, 1, luciuartpacket, (byte[]) null);
        this.checkLatest = false;
        new sendAndReceiveMessageBytes().execute(new byte[][]{lucipacket});
        while (!this.checkLatest) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("Latest Bytes Received", Arrays.toString(this.LatestReceivedBytes));
        List<byte[]> receivedPackets = new ArrayList<>();
        int k2 = -1;
        try {
            k2 = new String(this.LatestReceivedBytes, "US-ASCII").indexOf(new String(new byte[]{0, 0, 2, 0, -2, 0}, "US-ASCII")) + 10;
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        if (k2 > 9) {
            int k3 = k2;
            for (int i2 = 0; i2 < bArr.length; i2++) {
                if (this.LatestReceivedBytes[k3] == 0) {
                    int k4 = k3 + 1;
                    int k5 = k4 + 1;
                    byte[] tempbytes = new byte[this.LatestReceivedBytes[k4]];
                    System.arraycopy(this.LatestReceivedBytes, k5, tempbytes, 0, tempbytes.length);
                    receivedPackets.add(tempbytes);
                    k3 = tempbytes.length + k5;
                }
            }
            double[] positions = new double[bArr.length];
            if (receivedPackets.size() == bArr.length) {
                while (true) {
                    int i3 = i;
                    if (i3 < receivedPackets.size()) {
                        double val = (double) ((receivedPackets.get(i3)[17] & 255) + ((receivedPackets.get(i3)[18] & 255) << 8));
                        if (motorTypes[i3] == this.MOTORTYPES.AX12 || motorTypes[i3] == this.MOTORTYPES.AX18) {
                            positions[i3] = (300.0d * val) / 1023.0d;
                        } else {
                            positions[i3] = (360.0d * val) / 4095.0d;
                        }
                        i = i3 + 1;
                    } else {
                        Log.d("Postions", Arrays.toString(positions));
                        return positions;
                    }
                }
            } else {
                int i4 = k3;
            }
        }
        return new double[bArr.length];
    }

    /* access modifiers changed from: protected */
    public byte[] readPacketAxMx(byte id) {
        return new byte[]{-1, -1, id, 4, 2, 24, 20, (byte) (255 - (((byte) ((((id + 4) + 2) + 24) + 20)) & 255))};
    }

    public void readSingleAxMx(byte id, int motorType, int baudRate) {
        byte[] lucipacket = LUCI_createPacket(254, 1, LuciSingleReadUartPacket(baudRate, (byte) 26, readPacketAxMx(id)), (byte[]) null);
        new sendAndReceiveMessageBytes().execute(new byte[][]{lucipacket});
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("Latest Bytes Received", Arrays.toString(this.LatestReceivedBytes));
        try {
            new String(this.LatestReceivedBytes, "US-ASCII").indexOf(new String(new byte[]{0, 0, 2, 0, -2, 0}, "US-ASCII"));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
    }

    public void writeEnableSyncAxMx(byte[] ids, byte[] enables, long baudRate) {
        byte[][] datas = (byte[][]) Array.newInstance(byte.class, new int[]{ids.length, 1});
        for (int i = 0; i < ids.length; i++) {
            datas[i][0] = enables[i];
        }
        new sendMessageBytes().execute(new byte[][]{LUCI_createPacket(254, 0, LUCI_WriteUARTPacket(baudRate, syncWriteAxMxPacket(1, 24, ids, datas)), (byte[]) null)});
    }

    public void writeDegreesSyncAxMx(byte[] ids, byte[] motortypes, double[] degrees, double[] rpms, long baudRate) {
        byte[] bArr = ids;
        byte[][] datas = (byte[][]) Array.newInstance(byte.class, new int[]{bArr.length, 4});
        for (int i = 0; i < bArr.length; i++) {
            if (motortypes[i] == this.MOTORTYPES.AX12 || motortypes[i] == this.MOTORTYPES.AX18) {
                byte[] goalPosBytes = getLHbytes((int) ((degrees[i] / 300.0d) * 1023.0d));
                byte[] goalVelBytes = getLHbytes((int) ((rpms[i] / 114.0d) * 1023.0d));
                datas[i][0] = goalPosBytes[0];
                datas[i][1] = goalPosBytes[1];
                datas[i][2] = goalVelBytes[0];
                datas[i][3] = goalVelBytes[1];
            } else if (motortypes[i] == this.MOTORTYPES.MX28 || motortypes[i] == this.MOTORTYPES.MX64 || motortypes[i] == this.MOTORTYPES.MX106) {
                byte[] goalPosBytes2 = getLHbytes((int) ((degrees[i] / 360.0d) * 4095.0d));
                byte[] goalVelBytes2 = getLHbytes((int) ((rpms[i] / 117.0d) * 1023.0d));
                datas[i][0] = goalPosBytes2[0];
                datas[i][1] = goalPosBytes2[1];
                datas[i][2] = goalVelBytes2[0];
                datas[i][3] = goalVelBytes2[1];
            }
        }
        new sendMessageBytes().execute(new byte[][]{LUCI_createPacket(254, 0, LUCI_WriteUARTPacket(baudRate, syncWriteAxMxPacket(4, 30, bArr, datas)), (byte[]) null)});
    }

    /* access modifiers changed from: package-private */
    public void request_video_connection() {
        byte[] lucipacket = LUCI_createPacket(259, 0, new byte[]{0}, (byte[]) null);
        new sendMessageBytes().execute(new byte[][]{lucipacket});
    }

    /* access modifiers changed from: package-private */
    public double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (((x - in_min) * (out_max - out_min)) / (in_max - in_min)) + out_min;
    }

    /* access modifiers changed from: package-private */
    public void setMotorPWM(MotorandLED motorandLED, byte direction, int velocity) {
        MotorandLED motorandLED2 = motorandLED;
        byte b = direction;
        int i = velocity;
        motorandLED2.set_dir(b, (byte) 1);
        motorandLED2.set_vel((byte) ((int) map((double) i, 0.0d, 100.0d, 30.0d, 100.0d)), (byte) 1);
        motorandLED2.set_dir(b, (byte) 2);
        motorandLED2.set_vel((byte) ((int) map((double) i, 0.0d, 100.0d, 30.0d, 100.0d)), (byte) 2);
        byte[] dataTosend = motorandLED.send_data();
        byte[] luciPacket = new byte[(dataTosend.length + 10)];
        System.arraycopy(new byte[]{0, 0, 2, -2, 0, 0, 0, 0, (byte) dataTosend.length, 0}, 0, luciPacket, 0, 10);
        System.arraycopy(dataTosend, 0, luciPacket, 10, dataTosend.length);
        new sendMessageBytes().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new byte[][]{luciPacket});
    }

    public void setOutput(int gpio) {
        new sendMessageBytes().execute(new byte[][]{new byte[]{0, 0, 2, -2, 0, 0, 0, 0, 9, 0, 3, 6, 0, 0, 0, 0, 1, (byte) gpio, 1}});
    }

    public void setGPIO(int gpio, int state) {
        new sendMessageBytes().execute(new byte[][]{new byte[]{0, 0, 2, -2, 0, 0, 0, 0, 9, 0, 3, 4, 0, 0, 0, 1, 1, (byte) gpio, (byte) state}});
    }

    public byte[] LUCI_createPacket(int mbnum, int mode, byte[] packet0, byte[] packet1) {
        int i = mode;
        byte[] bArr = packet0;
        if (i == 4 || i == 5) {
            return new byte[]{0, 0, 2};
        }
        byte[] packet0lenbytes = getLHbytes(bArr.length);
        byte[] packet1lenbytes = getLHbytes(0);
        byte[] mbnumbytes = getLHbytes(mbnum);
        int LuciLength = bArr.length + 0 + 5;
        byte[] lucilengthbytes = getLHbytes(LuciLength);
        byte[] LUCI_packet = new byte[(LuciLength + 10)];
        System.arraycopy(new byte[]{0, 0, 2, mbnumbytes[0], mbnumbytes[1], 0, 0, 0, lucilengthbytes[0], lucilengthbytes[1], (byte) i, packet0lenbytes[0], packet0lenbytes[1], packet1lenbytes[0], packet1lenbytes[1]}, 0, LUCI_packet, 0, 15);
        System.arraycopy(bArr, 0, LUCI_packet, 15, bArr.length);
        return LUCI_packet;
    }

    public byte[] LUCI_WriteUARTPacket(long baudRate, byte[] uartPacket) {
        int bIndex = Arrays.asList(this.BAUDRATES).indexOf(Long.valueOf(baudRate));
        byte[] luciuartpacket = new byte[(uartPacket.length + 2)];
        System.arraycopy(uartPacket, 0, luciuartpacket, 2, uartPacket.length);
        luciuartpacket[0] = 0;
        luciuartpacket[1] = (byte) bIndex;
        return luciuartpacket;
    }

    public byte[] syncWriteAxMxPacket(int lenofdata, int startaddress, byte[] ids, byte[][] datas) {
        int len = ((lenofdata + 1) * ids.length) + 4;
        byte[] sync_packet = new byte[(len + 4)];
        System.arraycopy(new byte[]{-1, -1, -2, (byte) len, -125, (byte) startaddress, (byte) lenofdata}, 0, sync_packet, 0, 7);
        int crc = len + 254 + 131 + startaddress + lenofdata;
        int j = 0;
        while (j < ids.length) {
            sync_packet[((lenofdata + 1) * j) + 7] = ids[j];
            int crc2 = crc + ids[j];
            for (int k = 0; k < lenofdata; k++) {
                sync_packet[((lenofdata + 1) * j) + 7 + k + 1] = datas[j][k];
                crc2 += datas[j][k];
            }
            j++;
            crc = crc2;
        }
        sync_packet[(((lenofdata + 1) * ids.length) + 8) - 1] = (byte) (255 - (crc & 255));
        return sync_packet;
    }
}
