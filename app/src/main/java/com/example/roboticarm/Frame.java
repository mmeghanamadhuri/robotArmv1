package com.example.roboticarm;

import com.google.gson.annotations.SerializedName;

/* compiled from: RecordAndPlay */
public class Frame {
    @SerializedName("name")
    public String name;
    
    @SerializedName("duration")
    public double duration;
    
    @SerializedName("pause")
    public double pause;
    
    @SerializedName("positions")
    public double[] positions;

    // Backward compatibility fields
    public double p1;
    public double p2;
    public double p3;
    public double p4;
    public double p5;
    public double p6;

    // Default constructor for Gson
    public Frame() {
        this.name = "";
        this.duration = 1000;
        this.pause = 0;
        this.positions = new double[6];
        initializePositions();
    }

    public Frame(String name, double duration, double pause, double[] positions) {
        this.name = name;
        this.duration = duration;
        this.pause = pause;
        this.positions = positions != null ? positions : new double[6];
        initializePositions();
    }

    // Constructor for motor positions
    public Frame(double p1, double p2, double p3, double p4, double p5, double p6) {
        this.name = "Frame";
        this.duration = 1000;
        this.pause = 0;
        this.positions = new double[]{p1, p2, p3, p4, p5, p6};
    }

    private void initializePositions() {
        if (positions == null) {
            positions = new double[6];
        }
        if (positions.length < 6) {
            double[] newPositions = new double[6];
            System.arraycopy(positions, 0, newPositions, 0, positions.length);
            for (int i = positions.length; i < 6; i++) {
                newPositions[i] = 150.0; // Default neutral position
            }
            positions = newPositions;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getPause() {
        return pause;
    }

    public void setPause(double pause) {
        this.pause = pause;
    }

    public double[] getPositions() {
        return positions;
    }

    public void setPositions(double[] positions) {
        this.positions = positions;
        initializePositions();
    }

    public double getPosition(int index) {
        if (index >= 0 && index < positions.length) {
            return positions[index];
        }
        return 150.0; // Default neutral position
    }

    public void setPosition(int index, double value) {
        if (index >= 0 && index < positions.length) {
            positions[index] = value;
        }
    }
}
