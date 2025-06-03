package com.example.roboticarm;

import android.util.Log;

public class i2c_write {
    public static final int buffer_size = 512;
    public static final byte data_pointer = 7;
    public static final byte i2c_internal_mode = 5;
    public static final byte i2c_nu_of_dev = 6;
    public static final byte led_add = 96;
    public static final byte length0_highbyte = 2;
    public static final byte length0_lowbyte = 1;
    public static final byte length1_highbyte = 4;
    public static final byte length1_lowbyte = 3;
    public static final byte luci_mode = 0;
    public static final byte max_vel = 63;
    public static final byte min_vel = 6;
    public static final byte motor1 = 1;
    public static final byte motor1_add = 96;
    public static final byte motor2 = 2;
    public static final byte motor2_add = 98;
    public static final byte motor_bdir = 1;
    public static final byte motor_fdir = 2;
    public static final byte motor_stop = 0;
    int length0;
    int length1;
    public byte[] message = new byte[512];
    int present_data_pointer = 7;

    /* access modifiers changed from: package-private */
    public void set_change_internal_mode(byte mode) {
        this.message[5] = mode;
    }

    /* access modifiers changed from: package-private */
    public byte get_change_internal_mode() {
        return this.message[5];
    }

    /* access modifiers changed from: package-private */
    public byte get_nu_of_device() {
        return this.message[6];
    }

    /* access modifiers changed from: package-private */
    public void set_nu_of_dev(byte number) {
        this.message[6] = number;
    }

    /* access modifiers changed from: package-private */
    public void set_data_length(int length, byte whichone) {
        switch (whichone) {
            case 0:
                this.message[1] = (byte) length;
                this.message[2] = (byte) (length >> 8);
                this.length0 = length;
                return;
            case 1:
                this.message[3] = (byte) length;
                this.message[4] = (byte) (length >> 8);
                this.length1 = length;
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public int get_data_length(byte whichone) {
        switch (whichone) {
            case 0:
                return this.message[1] + (this.message[2] << 8);
            case 1:
                return this.message[3] + (this.message[4] << 8);
            default:
                return 0;
        }
    }

    /* access modifiers changed from: package-private */
    public void generate_luci_packet() {
        this.message[0] = 2;
        for (int i = 0; i < get_data_length((byte) 0) + 5; i++) {
        }
    }

    /* access modifiers changed from: package-private */
    public int add_data_to_buffer(byte len, byte[] data) {
        if (len < 1) {
            return 0;
        }
        for (int i = 0; i < len; i++) {
            this.message[this.present_data_pointer + i] = data[i];
        }
        this.present_data_pointer += len;
        return len;
    }

    public void refresh() {
        this.length0 = 0;
        this.length1 = 0;
        this.present_data_pointer = 7;
        this.message = new byte[512];
    }

    public byte[] set_device_data(byte dev_add, byte data_length, byte[] data) {
        byte[] bArr = new byte[2];
        byte imode = get_change_internal_mode();
        byte nu_of_dev = get_nu_of_device();
        if (imode == 0 && nu_of_dev == 1) {
            set_change_internal_mode((byte) 1);
        }
        byte nu_of_dev2 = (byte) (nu_of_dev + 1);
        if (nu_of_dev2 == 1) {
            set_data_length(2, (byte) 0);
        }
        set_nu_of_dev(nu_of_dev2);
        if (add_data_to_buffer((byte) 2, new byte[]{dev_add, data_length}) != 2) {
            Log.d("error", "error");
        } else {
            int length = get_data_length((byte) 0) + 2;
            Log.d("i2c_write", "length1= " + length);
            set_data_length(length, (byte) 0);
        }
        if (add_data_to_buffer(data_length, data) != data_length) {
            Log.d("error", "error in data copy");
        } else {
            int length2 = get_data_length((byte) 0) + data_length;
            Log.d("i2c_write", "length2= " + length2);
            set_data_length(length2, (byte) 0);
        }
        byte[] retmessage = new byte[(this.length0 + 5 + this.length1)];
        for (int k = 0; k < retmessage.length; k++) {
            retmessage[k] = this.message[k];
        }
        retmessage[0] = 2;
        return retmessage;
    }
}
