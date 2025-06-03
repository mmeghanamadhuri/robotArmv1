package com.example.roboticarm;

import android.util.Log;

public class MotorandLED extends i2c_write {
    public static final byte led_add = 96;
    public static final byte max_vel = 63;
    public static final byte min_vel = 6;
    public static final byte motor1 = 1;
    public static final byte motor1_add = 96;
    public static final byte motor2 = 2;
    public static final byte motor2_add = 98;
    public static final byte motor_bdir = 2;
    public static final byte motor_fdir = 1;
    public static final byte motor_stop = 0;
    byte motor1_direction;
    byte motor1_vel;
    byte motor1_vel_prev;
    byte motor2_direction;
    byte motor2_vel;
    byte motor2_vel_prev;

    public void set_vel(byte slider_value, byte motor) {
        switch (motor) {
            case 1:
                this.motor1_vel = (byte) ((int) (((((double) slider_value) / 100.0d) * 57.0d) + 6.0d));
                Log.d("Vel1", "" + this.motor1_vel);
                this.motor1_vel_prev = this.motor1_vel;
                return;
            case 2:
                this.motor2_vel = (byte) ((int) (((((double) slider_value) / 100.0d) * 57.0d) + 6.0d));
                Log.d("Vel2", "" + this.motor2_vel);
                this.motor2_vel_prev = this.motor2_vel_prev;
                return;
            default:
                return;
        }
    }

    public void set_dir(byte dir, byte motor) {
        switch (motor) {
            case 1:
                this.motor1_direction = dir;
                return;
            case 2:
                this.motor2_direction = dir;
                return;
            default:
                return;
        }
    }

    public byte[] send_data() {
        refresh();
        if (this.motor1_direction == 0 && this.motor2_direction == 0) {
            this.motor1_vel = this.motor1_vel_prev;
            this.motor2_vel = this.motor2_vel_prev;
        }
        byte[] arr = {0, (byte) ((this.motor1_direction & 3) | ((this.motor1_vel & 63) << 2))};
        Log.d("arr[1]=", "" + arr[1]);
        set_device_data((byte) 96, (byte) 2, arr);
        arr[0] = 0;
        arr[1] = (byte) ((this.motor2_direction & 3) | ((this.motor2_vel & 63) << 2));
        Log.d("arr[1]=", "" + arr[1]);
        return set_device_data((byte) 98, (byte) 2, arr);
    }

    public byte[] forward(byte slider_value) {
        set_dir((byte) 2, (byte) 1);
        set_dir((byte) 1, (byte) 2);
        set_vel(slider_value, (byte) 1);
        set_vel(slider_value, (byte) 2);
        return send_data();
    }

    public byte[] stop() {
        set_dir((byte) 0, (byte) 1);
        set_dir((byte) 0, (byte) 2);
        return send_data();
    }

    public byte[] shrap_left(byte slider_value) {
        set_dir((byte) 2, (byte) 1);
        set_dir((byte) 2, (byte) 2);
        set_vel(slider_value, (byte) 1);
        set_vel(slider_value, (byte) 2);
        return send_data();
    }

    public byte[] sharp_right(byte slider_value) {
        set_dir((byte) 1, (byte) 1);
        set_dir((byte) 1, (byte) 2);
        set_vel(slider_value, (byte) 1);
        set_vel(slider_value, (byte) 2);
        return send_data();
    }

    public byte[] left(byte slider_value) {
        set_dir((byte) 2, (byte) 1);
        set_dir((byte) 0, (byte) 2);
        set_vel(slider_value, (byte) 1);
        set_vel((byte) 0, (byte) 2);
        return send_data();
    }

    public byte[] right(byte slider_value) {
        set_dir((byte) 1, (byte) 2);
        set_dir((byte) 0, (byte) 1);
        set_vel(slider_value, (byte) 2);
        set_vel((byte) 0, (byte) 1);
        return send_data();
    }

    public byte[] led_red() {
        return set_device_data((byte) 96, (byte) 8, new byte[]{18, 0, -1, 0, -1, 69, 81, 20});
    }

    public byte[] led_green() {
        return set_device_data((byte) 96, (byte) 8, new byte[]{18, 0, -1, 0, -1, 81, 20, 69});
    }

    public byte[] led_blue() {
        return set_device_data((byte) 96, (byte) 8, new byte[]{18, 0, -1, 0, -1, 20, 69, 81});
    }
}
