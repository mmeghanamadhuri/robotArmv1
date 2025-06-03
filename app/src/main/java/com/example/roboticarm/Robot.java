package com.example.roboticarm;

/* compiled from: RecordAndPlay */
class Robot {
    public byte AX12 = 0;
    public byte AX18 = 1;
    public byte[] AllDisabled = {0, 0, 0, 0, 0, 0};
    public byte[] AllEnabled = {1, 1, 1, 1, 1, 1};
    public int Baudrate = 57142;
    public byte[] IDs = {1, 2, 3, 4, 5, 6};
    public byte MX106 = 4;
    public byte MX28 = 2;
    public byte MX64 = 3;
    public byte[] MotorTypes = {this.AX18, this.AX18, this.AX18, this.AX18, this.AX18, this.AX18};
    public double[] Neutral_Positions = {150.0d, 150.0d, 150.0d, 150.0d, 150.0d, 150.0d};
    public double[] Neutral_RPMs = {10.0d, 10.0d, 10.0d, 10.0d, 10.0d, 10.0d};
    public int NumberOfMotors = 6;
    public byte XL320 = 0;

    Robot() {
    }
}
