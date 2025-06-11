package com.example.roboticarm;

import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpClient {
    public String SERVER_IP;
    public int SERVER_PORT = 7777;
    private BufferedReader mBufferIn;
    private PrintWriter mBufferOut;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    private String mServerMessage;
    private DataOutputStream sWriter;
    Socket socket;

    public interface OnMessageReceived {
        void messageReceived(String str);
    }

    public TcpClient(OnMessageReceived listener) {
        this.mMessageListener = listener;
    }

    public void sendMessage(String message) {
        if (this.mBufferOut != null && !this.mBufferOut.checkError()) {
            this.mBufferOut.println(message);
            this.mBufferOut.flush();
        }
    }

    public void sendMessage(byte[] msg) {
        if (this.sWriter != null) {
            try {
                this.sWriter.write(msg);
            } catch (IOException e) {
                Log.e("Socket", "exception", e);
            }
        }
    }

    public byte[] readVideo() {
        byte[] bufarr = new byte[1024];
        try {
            if (this.socket.getInputStream().available() > 1024) {
                this.socket.getInputStream().read(bufarr);
                return bufarr;
            }
        } catch (Exception e) {
        }
        return new byte[]{0};
    }

    public byte[] readMessage() {
        byte[] bufarr = new byte[1];
        List<Byte> buflist = new ArrayList<>();
        int count = 0;
        while (count < 8192) {
            try {
                if (this.socket.getInputStream().available() <= 0) {
                    break;
                }
                this.socket.getInputStream().read(bufarr);
                buflist.add(Byte.valueOf(bufarr[0]));
                count++;
            } catch (Exception e) {
            }
        }
        return toByteArray(buflist);
    }

    public void stopClient() {
        Log.e("TCP Client", "Stopping client...");
        this.mRun = false;
        
        if (this.mBufferOut != null) {
            this.mBufferOut.flush();
            this.mBufferOut.close();
        }
        
        if (this.sWriter != null) {
            try {
                this.sWriter.close();
            } catch (IOException e) {
                Log.e("TCP", "Error closing sWriter", e);
            }
        }
        
        if (this.mBufferIn != null) {
            try {
                this.mBufferIn.close();
            } catch (IOException e) {
                Log.e("TCP", "Error closing mBufferIn", e);
            }
        }
        
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                Log.e("TCP", "Error closing socket", e);
            }
        }
        
        Log.e("TCP Client", "Client stopped.");
    }

    public static byte[] toByteArray(List<Byte> in) {
        int n = in.size();
        byte[] ret = new byte[n];
        for (int i = 0; i < n; i++) {
            ret[i] = in.get(i).byteValue();
        }
        return ret;
    }

    public void run() {
        this.mRun = true;
        try {
            InetAddress serverAddr = InetAddress.getByName(this.SERVER_IP);
            Log.e("TCP Client", "C: Connecting...");
            this.socket = new Socket(serverAddr, this.SERVER_PORT);
            try {
                this.mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
                this.sWriter = new DataOutputStream(this.socket.getOutputStream());
                this.mBufferIn = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                byte[] registerAsyncPacket = {0, 0, 2, 3, 0, 0, 0, 0, 0, 0};
                if (this.SERVER_PORT == 7777) {
                    sendMessage(new String(registerAsyncPacket, "UTF-8"));
                    Thread.sleep(500);
                    readMessage();
                }
            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            }
        } catch (Exception e2) {
            Log.e("TCP", "C: Error", e2);
        }
    }
}
