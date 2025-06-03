package com.example.roboticarm;

import java.util.Arrays;

/* compiled from: RecordAndPlay */
class Frame {
    public double duration;
    public double pause;
    public double[] positions;

    public Frame(int x) {
        this.positions = new double[x];
        Arrays.fill(this.positions, 150.0d);
        this.duration = 1.0d;
        this.pause = 0.0d;
    }

    public Frame(double[] positionss) {
        this.positions = new double[positionss.length];
        for (int i = 0; i < positionss.length; i++) {
            this.positions[i] = positionss[i];
        }
        this.duration = 1.0d;
        this.pause = 0.0d;
    }
}
