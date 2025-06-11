package com.example.roboticarm;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

public class ArmControl extends AppCompatActivity {

    int SliderMax = 285;
    int SliderMin = 15;
    String ip;
    SeekBar m1;
    SeekBar m2;
    SeekBar m3;
    SeekBar m4;
    SeekBar m5;
    SeekBar m6;
    ImageButton motor1Dec;
    ImageButton motor1Inc;
    ImageButton motor2Dec;
    ImageButton motor2Inc;
    ImageButton motor3Dec;
    ImageButton motor3Inc;
    ImageButton motor4Dec;
    ImageButton motor4Inc;
    ImageButton motor5Dec;
    ImageButton motor5Inc;
    ImageButton motor6Dec;
    ImageButton motor6Inc;
    Button neutral1;
    Button neutral2;
    Button neutral3;
    Button neutral4;
    Button neutral5;
    Button neutral6;
    Button disconnectButton;
    int p1 = 150;
    int p2 = 150;
    int p3 = 150;
    int p4 = 150;
    int p5 = 150;
    int p6 = 150;
    int resolution = 5;
    RobotCom robot = new RobotCom();
    Robot robotArm = new Robot();
    int stepSize = 5;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_arm_control);
        this.m1 = (SeekBar) findViewById(R.id.motor1);
        this.m2 = (SeekBar) findViewById(R.id.motor2);
        this.m3 = (SeekBar) findViewById(R.id.motor3);
        this.m4 = (SeekBar) findViewById(R.id.motor4);
        this.m5 = (SeekBar) findViewById(R.id.motor5);
        this.m6 = (SeekBar) findViewById(R.id.motor6);
        this.neutral1 = (Button) findViewById(R.id.bt_motor1Neutral);
        this.neutral2 = (Button) findViewById(R.id.bt_motor2Neutral);
        this.neutral3 = (Button) findViewById(R.id.bt_motor3Neutral);
        this.neutral4 = (Button) findViewById(R.id.bt_motor4Neutral);
        this.neutral5 = (Button) findViewById(R.id.bt_motor5Neutral);
        this.neutral6 = (Button) findViewById(R.id.bt_motor6Neutral);
        this.motor1Dec = (ImageButton) findViewById(R.id.bt_motor1Dec);
        this.motor2Dec = (ImageButton) findViewById(R.id.bt_motor2Dec);
        this.motor3Dec = (ImageButton) findViewById(R.id.bt_motor3Dec);
        this.motor4Dec = (ImageButton) findViewById(R.id.bt_motor4Dec);
        this.motor5Dec = (ImageButton) findViewById(R.id.bt_motor5Dec);
        this.motor6Dec = (ImageButton) findViewById(R.id.bt_motor6Dec);
        this.motor1Inc = (ImageButton) findViewById(R.id.bt_motor1Inc);
        this.motor2Inc = (ImageButton) findViewById(R.id.bt_motor2Inc);
        this.motor3Inc = (ImageButton) findViewById(R.id.bt_motor3Inc);
        this.motor4Inc = (ImageButton) findViewById(R.id.bt_motor4Inc);
        this.motor5Inc = (ImageButton) findViewById(R.id.bt_motor5Inc);
        this.motor6Inc = (ImageButton) findViewById(R.id.bt_motor6Inc);
        this.disconnectButton = (Button) findViewById(R.id.bt_disconnect);
        this.ip = getIntent().getStringExtra("ip_add");
        this.robot.openTcp(this.ip);
        this.m1.setProgress(150);
        this.m2.setProgress(150);
        this.m3.setProgress(150);
        this.m4.setProgress(150);
        this.m5.setProgress(150);
        this.m6.setProgress(150);
        this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{150.0d, 150.0d, 150.0d, 150.0d, 150.0d, 150.0d}, new double[]{10.0d, 10.0d, 10.0d, 10.0d, 10.0d, 10.0d}, 57142);
        this.m1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progress2 = Math.round((float) (progress / ArmControl.this.stepSize)) * ArmControl.this.stepSize;
                Log.d("slider 1:", Integer.toString(progress2));
                seekBar.setProgress(progress2);
                ArmControl.this.p1 = progress2;
                ArmControl.this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) ArmControl.this.p1, (double) ArmControl.this.p2, (double) ArmControl.this.p3, (double) ArmControl.this.p4, (double) ArmControl.this.p5, (double) ArmControl.this.p6}, new double[]{10.0d, 10.0d, 10.0d, 10.0d, 10.0d, 10.0d}, 57142);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.m2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progress2 = Math.round((float) (progress / ArmControl.this.stepSize)) * ArmControl.this.stepSize;
                Log.d("slider 2:", Integer.toString(progress2));
                seekBar.setProgress(progress2);
                ArmControl.this.p2 = progress2;
                ArmControl.this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) ArmControl.this.p1, (double) ArmControl.this.p2, (double) ArmControl.this.p3, (double) ArmControl.this.p4, (double) ArmControl.this.p5, (double) ArmControl.this.p6}, new double[]{10.0d, 10.0d, 10.0d, 10.0d, 10.0d, 10.0d}, 57142);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.m3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progress2 = Math.round((float) (progress / ArmControl.this.stepSize)) * ArmControl.this.stepSize;
                Log.d("slider 3:", Integer.toString(progress2));
                seekBar.setProgress(progress2);
                ArmControl.this.p3 = progress2;
                ArmControl.this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) ArmControl.this.p1, (double) ArmControl.this.p2, (double) ArmControl.this.p3, (double) ArmControl.this.p4, (double) ArmControl.this.p5, (double) ArmControl.this.p6}, new double[]{10.0d, 10.0d, 10.0d, 10.0d, 10.0d, 10.0d}, 57142);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.m4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progress2 = Math.round((float) (progress / ArmControl.this.stepSize)) * ArmControl.this.stepSize;
                Log.d("slider 3:", Integer.toString(progress2));
                seekBar.setProgress(progress2);
                ArmControl.this.p4 = progress2;
                ArmControl.this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) ArmControl.this.p1, (double) ArmControl.this.p2, (double) ArmControl.this.p3, (double) ArmControl.this.p4, (double) ArmControl.this.p5, (double) ArmControl.this.p6}, new double[]{10.0d, 10.0d, 10.0d, 10.0d, 10.0d, 10.0d}, 57142);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.m5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progress2 = Math.round((float) (progress / ArmControl.this.stepSize)) * ArmControl.this.stepSize;
                Log.d("slider 5:", Integer.toString(progress2));
                seekBar.setProgress(progress2);
                ArmControl.this.p5 = progress2;
                ArmControl.this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) ArmControl.this.p1, (double) ArmControl.this.p2, (double) ArmControl.this.p3, (double) ArmControl.this.p4, (double) ArmControl.this.p5, (double) ArmControl.this.p6}, new double[]{10.0d, 10.0d, 10.0d, 10.0d, 10.0d, 10.0d}, 57142);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.m6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int progress2 = Math.round((float) (progress / ArmControl.this.stepSize)) * ArmControl.this.stepSize;
                seekBar.setProgress(progress2);
                ArmControl.this.p6 = progress2;
                if (progress2 > 150) {
                    ArmControl.this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) ArmControl.this.p1, (double) ArmControl.this.p2, (double) ArmControl.this.p3, (double) ArmControl.this.p4, (double) ArmControl.this.p5, (double) ArmControl.this.p6}, new double[]{10.0d, 10.0d, 10.0d, 10.0d, 10.0d, 10.0d}, 57142);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        this.neutral1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArmControl.this.p1 = 150;
                ArmControl.this.m1.setProgress(150);
            }
        });
        this.neutral2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArmControl.this.p2 = 150;
                ArmControl.this.m2.setProgress(150);
            }
        });
        this.neutral3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArmControl.this.p3 = 150;
                ArmControl.this.m3.setProgress(150);
            }
        });
        this.neutral4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArmControl.this.p4 = 150;
                ArmControl.this.m4.setProgress(150);
            }
        });
        this.neutral5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArmControl.this.p5 = 150;
                ArmControl.this.m5.setProgress(150);
            }
        });
        this.neutral6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArmControl.this.p6 = 150;
                ArmControl.this.m6.setProgress(150);
            }
        });
        this.motor1Inc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m1.getProgress() < ArmControl.this.SliderMax) {
                    ArmControl.this.m1.setProgress(ArmControl.this.m1.getProgress() + ArmControl.this.resolution);
                }
            }
        });
        this.motor2Inc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m2.getProgress() < ArmControl.this.SliderMax) {
                    ArmControl.this.m2.setProgress(ArmControl.this.m2.getProgress() + ArmControl.this.resolution);
                }
            }
        });
        this.motor3Inc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m3.getProgress() < ArmControl.this.SliderMax) {
                    ArmControl.this.m3.setProgress(ArmControl.this.m3.getProgress() + ArmControl.this.resolution);
                }
            }
        });
        this.motor4Inc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m4.getProgress() < ArmControl.this.SliderMax) {
                    ArmControl.this.m4.setProgress(ArmControl.this.m4.getProgress() + ArmControl.this.resolution);
                }
            }
        });
        this.motor5Inc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m5.getProgress() < ArmControl.this.SliderMax) {
                    ArmControl.this.m5.setProgress(ArmControl.this.m5.getProgress() + ArmControl.this.resolution);
                }
            }
        });
        this.motor6Inc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m6.getProgress() < ArmControl.this.SliderMax) {
                    ArmControl.this.m6.setProgress(ArmControl.this.m6.getProgress() + ArmControl.this.resolution);
                }
            }
        });
        this.motor1Dec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m1.getProgress() > ArmControl.this.SliderMin) {
                    ArmControl.this.m1.setProgress(ArmControl.this.m1.getProgress() - ArmControl.this.resolution);
                }
            }
        });
        this.motor2Dec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m2.getProgress() > ArmControl.this.SliderMin) {
                    ArmControl.this.m2.setProgress(ArmControl.this.m2.getProgress() - ArmControl.this.resolution);
                }
            }
        });
        this.motor3Dec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m3.getProgress() > ArmControl.this.SliderMin) {
                    ArmControl.this.m3.setProgress(ArmControl.this.m3.getProgress() - ArmControl.this.resolution);
                }
            }
        });
        this.motor4Dec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m4.getProgress() > ArmControl.this.SliderMin) {
                    ArmControl.this.m4.setProgress(ArmControl.this.m4.getProgress() - ArmControl.this.resolution);
                }
            }
        });
        this.motor5Dec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m1.getProgress() > ArmControl.this.SliderMin) {
                    ArmControl.this.m5.setProgress(ArmControl.this.m5.getProgress() - ArmControl.this.resolution);
                }
            }
        });
        this.motor6Dec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ArmControl.this.m6.getProgress() > ArmControl.this.SliderMin) {
                    ArmControl.this.m6.setProgress(ArmControl.this.m6.getProgress() - ArmControl.this.resolution);
                }
            }
        });
        this.disconnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                p1 = 150;
                p2 = 216;
                p3 = 236;
                p4 = 150;
                p5 = 150;
                p6 = 150;
                
                m1.setProgress(150);
                m2.setProgress(216);
                m3.setProgress(236);
                m4.setProgress(150);
                m5.setProgress(150);
                m6.setProgress(150);
                
                robot.writeDegreesSyncAxMx(robotArm.IDs, robotArm.MotorTypes, 
                    new double[]{150.0d, 216.0d, 236.0d, 150.0d, 150.0d, 150.0d}, 
                    new double[]{5.0d, 5.0d, 5.0d, 5.0d, 5.0d, 5.0d},
                    57142);
                
                try {
                    Thread.sleep(3000);
                    
                    if (robot.mTcpClient != null) {
                        robot.mTcpClient.stopClient();
                    }
                    
                    Toast.makeText(ArmControl.this, "Robot safely moved to rest position and disconnected", Toast.LENGTH_SHORT).show();
                    
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(ArmControl.this, "Error during disconnect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
