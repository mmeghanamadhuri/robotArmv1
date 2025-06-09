package com.example.roboticarm;

import android.annotation.SuppressLint;
//import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class RecordAndPlay extends AppCompatActivity {
    boolean New_Action_Created = false;
    boolean Previous_Frame_Recorded = true;
    Robot RoboticArm = new Robot();
    int SliderMax = 285;
    int SliderMin = 15;
    List<Frame> action = new ArrayList();
    String action_name = "";
    int current_frame = 0;
    Button gripper;
    String ip;
    LinearLayout linearLayout;
    Button loadAction;
    Button motor1;
    Button motor2;
    Button motor3;
    Button motor4;
    Button motor5;
    SeekBar motorAngle;
    Button neutral;
    Button newAction;
    Button newFrame;
    int p1 = 150;
    int p2 = 150;
    int p3 = 150;
    int p4 = 150;
    int p5 = 150;
    int p6 = 150;
    Button playAction;
    boolean playing = false;
    boolean[] pressed = {false, false, false, false, false, false};
    Button recordFrame;
    int resolution = 1;
    RobotCom robot = new RobotCom();
    Button saveAction;
    ImageButton sb_Dec;
    ImageButton sb_Inc;
    HorizontalScrollView scrollView;
    int selected_button = 1;
    int stepSize = 1;
    TextView tv_motorNum;
    TextView tv_title;

    /* access modifiers changed from: protected */
    @SuppressLint({"ClickableViewAccessibility"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_record_and_play);
        this.sb_Dec = (ImageButton) findViewById(R.id.bt_motor1Dec);
        this.sb_Inc = (ImageButton) findViewById(R.id.bt_motor1Inc);
        this.motor1 = (Button) findViewById(R.id.bt_motor1);
        this.motor2 = (Button) findViewById(R.id.bt_motor2);
        this.motor3 = (Button) findViewById(R.id.bt_motor3);
        this.motor4 = (Button) findViewById(R.id.bt_motor4);
        this.motor5 = (Button) findViewById(R.id.bt_motor5);
        this.gripper = (Button) findViewById(R.id.bt_gripper);
        this.neutral = (Button) findViewById(R.id.bt_neutral);
        this.motorAngle = (SeekBar) findViewById(R.id.sb_motorAngle);
        this.scrollView = (HorizontalScrollView) findViewById(R.id.sv_frameList);
        this.linearLayout = (LinearLayout) findViewById(R.id.ll_frames);
        this.tv_title = (TextView) findViewById(R.id.tv_title);
        this.linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        this.tv_motorNum = (TextView) findViewById(R.id.tv_motorNumber);
        this.ip = getIntent().getStringExtra("ip_add");
        this.robot.openTcp(this.ip);
        this.robot.writeDegreesSyncAxMx(this.RoboticArm.IDs, this.RoboticArm.MotorTypes, this.RoboticArm.Neutral_Positions, this.RoboticArm.Neutral_RPMs, (long) this.RoboticArm.Baudrate);
        this.motorAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                SeekBar seekBar2 = seekBar;
                switch (RecordAndPlay.this.selected_button) {
                    case 1:
                        int progress2 = Math.round((float) (progress / RecordAndPlay.this.stepSize)) * RecordAndPlay.this.stepSize;
                        seekBar2.setProgress(progress2);
                        RecordAndPlay.this.p1 = progress2;
                        RecordAndPlay.this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) RecordAndPlay.this.p1, (double) RecordAndPlay.this.p2, (double) RecordAndPlay.this.p3, (double) RecordAndPlay.this.p4, (double) RecordAndPlay.this.p5, (double) RecordAndPlay.this.p6}, new double[]{20.0d, 20.0d, 20.0d, 20.0d, 20.0d, 20.0d}, 57142);
                        return;
                    case 2:
                        int progress3 = Math.round((float) (progress / RecordAndPlay.this.stepSize)) * RecordAndPlay.this.stepSize;
                        seekBar2.setProgress(progress3);
                        RecordAndPlay.this.p2 = progress3;
                        RecordAndPlay.this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) RecordAndPlay.this.p1, (double) RecordAndPlay.this.p2, (double) RecordAndPlay.this.p3, (double) RecordAndPlay.this.p4, (double) RecordAndPlay.this.p5, (double) RecordAndPlay.this.p6}, new double[]{20.0d, 20.0d, 20.0d, 20.0d, 20.0d, 20.0d}, 57142);
                        return;
                    case 3:
                        int progress4 = Math.round((float) (progress / RecordAndPlay.this.stepSize)) * RecordAndPlay.this.stepSize;
                        seekBar2.setProgress(progress4);
                        RecordAndPlay.this.p3 = progress4;
                        RecordAndPlay.this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) RecordAndPlay.this.p1, (double) RecordAndPlay.this.p2, (double) RecordAndPlay.this.p3, (double) RecordAndPlay.this.p4, (double) RecordAndPlay.this.p5, (double) RecordAndPlay.this.p6}, new double[]{20.0d, 20.0d, 20.0d, 20.0d, 20.0d, 20.0d}, 57142);
                        return;
                    case 4:
                        int progress5 = Math.round((float) (progress / RecordAndPlay.this.stepSize)) * RecordAndPlay.this.stepSize;
                        seekBar2.setProgress(progress5);
                        RecordAndPlay.this.p4 = progress5;
                        RecordAndPlay.this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) RecordAndPlay.this.p1, (double) RecordAndPlay.this.p2, (double) RecordAndPlay.this.p3, (double) RecordAndPlay.this.p4, (double) RecordAndPlay.this.p5, (double) RecordAndPlay.this.p6}, new double[]{20.0d, 20.0d, 20.0d, 20.0d, 20.0d, 20.0d}, 57142);
                        return;
                    case 5:
                        int progress6 = Math.round((float) (progress / RecordAndPlay.this.stepSize)) * RecordAndPlay.this.stepSize;
                        seekBar2.setProgress(progress6);
                        RecordAndPlay.this.p5 = progress6;
                        RecordAndPlay.this.robot.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) RecordAndPlay.this.p1, (double) RecordAndPlay.this.p2, (double) RecordAndPlay.this.p3, (double) RecordAndPlay.this.p4, (double) RecordAndPlay.this.p5, (double) RecordAndPlay.this.p6}, new double[]{20.0d, 20.0d, 20.0d, 20.0d, 20.0d, 20.0d}, 57142);
                        return;
                    case 6:
                        int progress7 = Math.round((float) (progress / RecordAndPlay.this.stepSize)) * RecordAndPlay.this.stepSize;
                        seekBar2.setProgress(progress7);
                        RecordAndPlay.this.p6 = progress7;
                        if (RecordAndPlay.this.p6 > 150) {
                            RobotCom robotCom = RecordAndPlay.this.robot;
                            RobotCom robotCom2 = robotCom;
                            robotCom2.writeDegreesSyncAxMx(new byte[]{1, 2, 3, 4, 5, 6}, new byte[]{0, 0, 0, 0, 0, 0}, new double[]{(double) RecordAndPlay.this.p1, (double) RecordAndPlay.this.p2, (double) RecordAndPlay.this.p3, (double) RecordAndPlay.this.p4, (double) RecordAndPlay.this.p5, (double) RecordAndPlay.this.p6}, new double[]{20.0d, 20.0d, 20.0d, 20.0d, 20.0d, 20.0d}, 57142);
                            return;
                        }
                        return;
                    default:
                        int i = progress;
                        return;
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        findViewById(R.id.bt_motor1).setOnClickListener(this::onMotor1Click);
        findViewById(R.id.bt_motor2).setOnClickListener(this::onMotor2Click);
        findViewById(R.id.bt_motor3).setOnClickListener(this::onMotor3Click);
        findViewById(R.id.bt_motor4).setOnClickListener(this::onMotor4Click);
        findViewById(R.id.bt_motor5).setOnClickListener(this::onMotor5Click);
        findViewById(R.id.bt_gripper).setOnClickListener(this::onGripperClick);
    }

    // Separate click methods
    public void onMotor1Click(View view) {
        this.selected_button = 1;
        if (this.pressed[0]) {
            this.motorAngle.setProgress(this.p1);
        } else {
            this.motorAngle.setProgress(150);
        }
        this.pressed[0] = true;
        this.tv_motorNum.setText(R.string.motor1);
        Toast.makeText(this, "Motor 1 clicked", Toast.LENGTH_SHORT).show();
    }

    public void onMotor2Click(View view) {
        Toast.makeText(this, "Motor 2 clicked", Toast.LENGTH_SHORT).show();
        this.selected_button = 2;
        if (this.pressed[1]) {
            this.motorAngle.setProgress(this.p2);
        } else {
            this.motorAngle.setProgress(150);
        }
        this.pressed[1] = true;
        this.tv_motorNum.setText(R.string.motor2);
    }

    public void onMotor3Click(View view) {
        this.selected_button = 3;
        if (this.pressed[2]) {
            this.motorAngle.setProgress(this.p3);
        } else {
            this.motorAngle.setProgress(150);
        }
        this.pressed[2] = true;
        this.tv_motorNum.setText(R.string.motor3);
        Toast.makeText(this, "Motor 3 clicked", Toast.LENGTH_SHORT).show();
    }

    public void onMotor4Click(View view) {
        this.selected_button = 4;
        if (this.pressed[3]) {
            this.motorAngle.setProgress(this.p4);
        } else {
            this.motorAngle.setProgress(150);
        }
        this.pressed[3] = true;
        this.tv_motorNum.setText(R.string.motor4);
        Toast.makeText(this, "Motor 4 clicked", Toast.LENGTH_SHORT).show();
    }

    public void onMotor5Click(View view) {
        this.selected_button = 5;
        if (this.pressed[4]) {
            this.motorAngle.setProgress(this.p5);
        } else {
            this.motorAngle.setProgress(150);
        }
        this.pressed[4] = true;
        this.tv_motorNum.setText(R.string.motor5);
        Toast.makeText(this, "Motor 5 clicked", Toast.LENGTH_SHORT).show();
    }

    public void onGripperClick(View view) {

        Toast.makeText(this, "Gripper clicked", Toast.LENGTH_SHORT).show();
        this.selected_button = 6;
        if (this.pressed[5]) {
            this.motorAngle.setProgress(this.p6);
        } else {
            this.motorAngle.setProgress(150);
        }
        this.pressed[5] = true;
        this.tv_motorNum.setText(R.string.gripper);
    }

    public void inc_slider(View view) {
        switch (this.selected_button) {
            case 1:
                if (this.motorAngle.getProgress() < this.SliderMax) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() + this.resolution);
                    return;
                }
                return;
            case 2:
                if (this.motorAngle.getProgress() < this.SliderMax) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() + this.resolution);
                    return;
                }
                return;
            case 3:
                if (this.motorAngle.getProgress() < this.SliderMax) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() + this.resolution);
                    return;
                }
                return;
            case 4:
                if (this.motorAngle.getProgress() < this.SliderMax) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() + this.resolution);
                    return;
                }
                return;
            case 5:
                if (this.motorAngle.getProgress() < this.SliderMax) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() + this.resolution);
                    return;
                }
                return;
            case 6:
                if (this.motorAngle.getProgress() < this.SliderMax) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() + this.resolution);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void dec_slider(View view) {
        switch (this.selected_button) {
            case 1:
                if (this.motorAngle.getProgress() > this.SliderMin) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() - this.resolution);
                    return;
                }
                return;
            case 2:
                if (this.motorAngle.getProgress() > this.SliderMin) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() - this.resolution);
                    return;
                }
                return;
            case 3:
                if (this.motorAngle.getProgress() > this.SliderMin) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() - this.resolution);
                    return;
                }
                return;
            case 4:
                if (this.motorAngle.getProgress() > this.SliderMin) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() - this.resolution);
                    return;
                }
                return;
            case 5:
                if (this.motorAngle.getProgress() > this.SliderMin) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() - this.resolution);
                    return;
                }
                return;
            case 6:
                if (this.motorAngle.getProgress() > this.SliderMin) {
                    this.motorAngle.setProgress(this.motorAngle.getProgress() - this.resolution);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void motor_on_click(View view) {
        int id = view.getId(); // Get the clicked button ID

        switch (id) {
            case 2131230761:
                this.selected_button = 6;
                if (this.pressed[5]) {
                    this.motorAngle.setProgress(this.p6);
                } else {
                    this.motorAngle.setProgress(150);
                }
                this.pressed[5] = true;
                this.tv_motorNum.setText(R.string.gripper);
                return;
            case 2131230760:
                this.selected_button = 1;
                if (this.pressed[0]) {
                    this.motorAngle.setProgress(this.p1);
                } else {
                    this.motorAngle.setProgress(150);
                }
                this.pressed[0] = true;
                this.tv_motorNum.setText(R.string.motor1);
                return;
            case 2131230764:
                this.selected_button = 2;
                if (this.pressed[1]) {
                    this.motorAngle.setProgress(this.p2);
                } else {
                    this.motorAngle.setProgress(150);
                }
                this.pressed[1] = true;
                this.tv_motorNum.setText(R.string.motor2);
                return;
            case 2131230768:
                this.selected_button = 3;
                if (this.pressed[2]) {
                    this.motorAngle.setProgress(this.p3);
                } else {
                    this.motorAngle.setProgress(150);
                }
                this.pressed[2] = true;
                this.tv_motorNum.setText(R.string.motor3);
                return;
            case 2131230772:
                this.selected_button = 4;
                if (this.pressed[3]) {
                    this.motorAngle.setProgress(this.p4);
                } else {
                    this.motorAngle.setProgress(150);
                }
                this.pressed[3] = true;
                this.tv_motorNum.setText(R.string.motor4);
                return;
            case 2131230776:
                this.selected_button = 5;
                if (this.pressed[4]) {
                    this.motorAngle.setProgress(this.p5);
                } else {
                    this.motorAngle.setProgress(150);
                }
                this.pressed[4] = true;
                this.tv_motorNum.setText(R.string.motor5);
                return;
            default:
                return;
        }
    }

    public void neutral_on_click(View view) {
        this.p1 = 150;
        this.p2 = 150;
        this.p3 = 150;
        this.p4 = 150;
        this.p5 = 150;
        this.p6 = 150;
        this.motorAngle.setProgress(150);
        this.robot.writeDegreesSyncAxMx(this.RoboticArm.IDs, this.RoboticArm.MotorTypes, this.RoboticArm.Neutral_Positions, this.RoboticArm.Neutral_RPMs, (long) this.RoboticArm.Baudrate);
    }

    public void newAction_on_click(View view) {
        if (!this.New_Action_Created) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle((CharSequence) "Action Name");
            final EditText input = new EditText(this);
            input.setInputType(1);
            builder.setView((View) input);
            builder.setPositiveButton((CharSequence) "Create", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    RecordAndPlay.this.action_name = input.getText().toString();
                    TextView textView = RecordAndPlay.this.tv_title;
                    textView.setText("title:" + RecordAndPlay.this.action_name);
                    RecordAndPlay.this.action.clear();
                    RecordAndPlay.this.action.add(new Frame(RecordAndPlay.this.RoboticArm.Neutral_Positions));
                    RecordAndPlay.this.action.get(0).duration = 1.0d;
                    RecordAndPlay.this.action.get(0).pause = 1.0d;
                    RecordAndPlay.this.add_neutral_frame_to_scroll_view();
                    RecordAndPlay.this.New_Action_Created = true;
                    Toast.makeText(RecordAndPlay.this, "New Action created", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
            return;
        }
        Toast.makeText(this, "New Action already created", Toast.LENGTH_SHORT).show();
    }

    public void newFrame_on_click(View view) {
        if (!this.New_Action_Created) {
            Toast.makeText(this, "Create a new Action!", Toast.LENGTH_SHORT).show();
        } else if (this.Previous_Frame_Recorded) {
            this.p1 = 150;
            this.p2 = 150;
            this.p3 = 150;
            this.p4 = 150;
            this.p5 = 150;
            this.p6 = 150;
            this.motorAngle.setProgress(150);
            this.current_frame++;
            Button button = new Button(this);
            button.setText("N");
            button.setId(this.current_frame);
            button.setBackground(getDrawable(R.drawable.framelist_button_background));
            button.setTextColor(getColor(R.color.buttonTextColor));
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    RecordAndPlay.this.edit_frame(view);
                }
            });
            this.linearLayout.addView(button);
            this.scrollView.scrollTo(this.linearLayout.getRight(), 0);
            this.Previous_Frame_Recorded = false;
        } else {
            Toast.makeText(this, "Record the frame!", Toast.LENGTH_SHORT).show();
        }
        this.action.add(new Frame(new double[]{(double) this.p1, (double) this.p2, (double) this.p3, (double) this.p4, (double) this.p5, (double) this.p6}));
        this.action.get(this.current_frame).duration = 1.0d;
        this.action.get(this.current_frame).pause = 1.0d;
        this.Previous_Frame_Recorded = true;
    }

    public void add_neutral_frame_to_scroll_view() {
        Button button = new Button(this);
        button.setBackground(getDrawable(R.drawable.framelist_button_background));
        button.setTextColor(getColor(R.color.buttonTextColor));
        button.setText("N");
        button.setId(this.current_frame);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RecordAndPlay.this.edit_frame(view);
            }
        });
        this.linearLayout.addView(button);
        Toast.makeText(this, "Neutral frame added", Toast.LENGTH_SHORT).show();
    }

    public void edit_frame(View view) {
        try {
            final int selected_frame = view.getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle((CharSequence) "Frame");
            LinearLayout layout = new LinearLayout(this);
            LinearLayout linearLayout2 = this.linearLayout;
            layout.setOrientation(LinearLayout.VERTICAL);
            TextView tv_duration = new TextView(this);
            tv_duration.setText("duration");
            final EditText et_duration = new EditText(this);
            et_duration.setText(String.valueOf(this.action.get(selected_frame).duration));
            TextView tv_delay = new TextView(this);
            tv_delay.setText("delay");
            final EditText et_delay = new EditText(this);
            et_delay.setText(String.valueOf(this.action.get(selected_frame).pause));
            et_duration.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            et_delay.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            layout.addView(tv_duration);
            layout.addView(et_duration);
            layout.addView(tv_delay);
            layout.addView(et_delay);
            builder.setView((View) layout);
            builder.setPositiveButton((CharSequence) "Save", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    RecordAndPlay.this.action.get(selected_frame).duration = Double.valueOf(et_duration.getText().toString()).doubleValue();
                    RecordAndPlay.this.action.get(selected_frame).pause = Double.valueOf(et_delay.getText().toString()).doubleValue();
                }
            });
            
            // Add Delete button if it's not the neutral frame (frame 0)
            if (selected_frame != 0) {
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteFrame(selected_frame);
                    }
                });
            }
            
            builder.show();
        } catch (Exception e) {
            Toast.makeText(this, "Record frame first", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadAction_on_click(View view) {
        List<File> files = getListFiles(getFilesDir());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Action Name");
        final Spinner input = new Spinner(this);
        String[] action_names = new String[files.size()];
        for (int i = 0; i < files.size(); i++) {
            action_names[i] = files.get(i).getName();
        }
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, action_names);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        input.setAdapter(dataAdapter);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    RecordAndPlay.this.action_name = input.getSelectedItem().toString().replace(".txt", "");
                    TextView textView = RecordAndPlay.this.tv_title;
                    textView.setText("title:" + RecordAndPlay.this.action_name);
                    RecordAndPlay.this.action.clear();
                    String readFile = RecordAndPlay.this.readFromFile(RecordAndPlay.this, RecordAndPlay.this.action_name.concat(".txt"));
                    RecordAndPlay.this.action = RecordAndPlay.this.get_action_from_string(readFile);
                    RecordAndPlay.this.update_scrollView_frames();
                    Toast.makeText(RecordAndPlay.this, "Action loaded", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(RecordAndPlay.this, "No Actions", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<>();
        for (File file : parentDir.listFiles()) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else if (file.getName().endsWith(".txt")) {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

    public void update_scrollView_frames() {
        if (this.linearLayout.getChildCount() > 0) {
            this.linearLayout.removeAllViews();
        }
        this.current_frame = this.action.size() - 1;
        for (int i = 0; i < this.action.size(); i++) {
            if (i == 0) {
                Button button = new Button(this);
                button.setText("N");
                button.setId(i);
                button.setBackground(getDrawable(R.drawable.framelist_button_background));
                button.setTextColor(getColor(R.color.buttonTextColor));
                this.linearLayout.addView(button);
            } else {
                Button frameButton = new Button(this);
                frameButton.setText("frame " + Integer.toString(i));
                frameButton.setId(i);
                frameButton.setBackground(getDrawable(R.drawable.framelist_button_background));
                frameButton.setTextColor(getColor(R.color.buttonTextColor));
                frameButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        RecordAndPlay.this.edit_frame(view);
                    }
                });
                this.linearLayout.addView(frameButton);
            }
        }
        this.New_Action_Created = true;
    }

    public List<Frame> get_action_from_string(String str) {
        String[] lines = str.split("E");
        List<Frame> act = new ArrayList<>();
        for (int i = 0; i < lines.length; i++) {
            double dur = Double.valueOf(lines[i].split("D")[0]).doubleValue();
            lines[i] = lines[i].split("D")[1];
            double pau = Double.valueOf(lines[i].split("P")[0]).doubleValue();
            lines[i] = lines[i].split("P")[1];
            String[] motorvals = lines[i].split("A");
            double[] positions = new double[motorvals.length];
            for (int j = 0; j < motorvals.length; j++) {
                positions[j] = Double.valueOf(motorvals[j]).doubleValue();
            }
            Frame f = new Frame(positions);
            f.duration = dur;
            f.pause = pau;
            act.add(f);
        }
        return act;
    }

    /* access modifiers changed from: private */
    public String readFromFile(Context context, String action_name2) {
        try {
            InputStream inputStream = context.openFileInput(action_name2);
            if (inputStream == null) {
                return "";
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                String receiveString = readLine;
                if (readLine != null) {
                    stringBuilder.append(receiveString);
                } else {
                    inputStream.close();
                    return stringBuilder.toString();
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            return "";
        } catch (IOException e2) {
            Log.e("login activity", "Can not read file: " + e2.toString());
            return "";
        }
    }

    public void recordFrame_on_click(View view) {
        if (!this.New_Action_Created) {
            Toast.makeText(this, "Create a new Action!", Toast.LENGTH_SHORT).show();
        } else if (this.Previous_Frame_Recorded) {
            this.current_frame++;
            
            Button frameButton = new Button(this);
            frameButton.setText("frame " + Integer.toString(this.current_frame));
            frameButton.setId(this.current_frame);
            frameButton.setBackground(getDrawable(R.drawable.framelist_button_background));
            frameButton.setTextColor(getColor(R.color.buttonTextColor));
            frameButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    RecordAndPlay.this.edit_frame(view);
                }
            });
            
            this.linearLayout.addView(frameButton);
            this.scrollView.scrollTo(this.linearLayout.getRight(), 0);
            this.Previous_Frame_Recorded = false;
            Toast.makeText(this, "frame recorded", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Record the frame!", Toast.LENGTH_SHORT).show();
        }
        this.action.add(new Frame(new double[]{(double) this.p1, (double) this.p2, (double) this.p3, (double) this.p4, (double) this.p5, (double) this.p6}));
        this.action.get(this.current_frame).duration = 1.0d;
        this.action.get(this.current_frame).pause = 1.0d;
        this.Previous_Frame_Recorded = true;
    }

    public void playAction_on_click(View view) {
        Toast.makeText(this, "Playing Action", Toast.LENGTH_SHORT).show();
        if (!this.playing) {
            this.playing = true;
            new PlayActionTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }
    }

    public void saveAction_on_click(View view) {
        if (this.action_name.compareTo("") != 0) {
            String data = "";
            for (int i = 0; i < this.action.size(); i++) {
                String data2 = data.concat(Double.toString(this.action.get(i).duration)).concat("D").concat(Double.toString(this.action.get(i).pause)).concat("P");
                for (int j = 0; j < this.action.get(i).positions.length; j++) {
                    data2 = data2.concat(Double.toString(this.action.get(i).positions[j]));
                    if (j < this.action.get(i).positions.length - 1) {
                        data2 = data2.concat("A");
                    }
                }
                data = data2.concat("E");
            }
            writeToFile(data, this);
            Toast.makeText(this, "Action Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(this.action_name.replace(".txt", "").concat(".txt"), 0));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private class PlayActionTask extends AsyncTask<Void, Void, Void> {
        private PlayActionTask() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voids) {
            int i;
            double[] rpms = new double[RecordAndPlay.this.RoboticArm.NumberOfMotors];
            boolean not_nuetral = false;
            int j = 0;
            while (true) {
                i = 1;
                if (j >= RecordAndPlay.this.RoboticArm.NumberOfMotors) {
                    break;
                }
                if (RecordAndPlay.this.action.get(RecordAndPlay.this.action.size() - 1).positions[j] != 150.0d) {
                    not_nuetral = true;
                }
                j++;
            }
            if (not_nuetral) {
                double[] positions = RecordAndPlay.this.action.get(RecordAndPlay.this.action.size() - 1).positions;
                for (int j2 = 0; j2 < RecordAndPlay.this.RoboticArm.NumberOfMotors; j2++) {
                    double diff = Math.abs(RecordAndPlay.this.RoboticArm.Neutral_Positions[j2] - positions[j2]);
                    if (RecordAndPlay.this.RoboticArm.MotorTypes[j2] == RecordAndPlay.this.RoboticArm.AX12 || RecordAndPlay.this.RoboticArm.MotorTypes[j2] == RecordAndPlay.this.RoboticArm.AX18) {
                        rpms[j2] = (diff / 180.0d) / 0.03333333333333333d;
                    } else {
                        rpms[j2] = (diff / 360.0d) / 0.03333333333333333d;
                    }
                }
                boolean z = not_nuetral;
                double[] dArr = positions;
                RecordAndPlay.this.robot.writeDegreesSyncAxMx(RecordAndPlay.this.RoboticArm.IDs, RecordAndPlay.this.RoboticArm.MotorTypes, RecordAndPlay.this.RoboticArm.Neutral_Positions, rpms, (long) RecordAndPlay.this.RoboticArm.Baudrate);
                try {
                    Thread.sleep((long) ((int) ((0.03333333333333333d + 0.03333333333333333d) * 60.0d * 1000.0d)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (RecordAndPlay.this.action.size() > 1) {
                while (true) {
                    int i2 = i;
                    if (i2 >= RecordAndPlay.this.action.size()) {
                        break;
                    }
                    double dur = RecordAndPlay.this.action.get(i2).duration / 60.0d;
                    double pau = RecordAndPlay.this.action.get(i2).pause / 60.0d;
                    for (int j3 = 0; j3 < RecordAndPlay.this.RoboticArm.NumberOfMotors; j3++) {
                        double diff2 = Math.abs(RecordAndPlay.this.action.get(i2).positions[j3] - RecordAndPlay.this.action.get(i2 - 1).positions[j3]);
                        if (RecordAndPlay.this.RoboticArm.MotorTypes[j3] == RecordAndPlay.this.RoboticArm.AX12 || RecordAndPlay.this.RoboticArm.MotorTypes[j3] == RecordAndPlay.this.RoboticArm.AX18) {
                            rpms[j3] = (diff2 / 180.0d) / dur;
                        } else {
                            rpms[j3] = (diff2 / 360.0d) / dur;
                        }
                    }
                    RecordAndPlay.this.robot.writeDegreesSyncAxMx(RecordAndPlay.this.RoboticArm.IDs, RecordAndPlay.this.RoboticArm.MotorTypes, RecordAndPlay.this.action.get(i2).positions, rpms, (long) RecordAndPlay.this.RoboticArm.Baudrate);
                    try {
                        Thread.sleep((long) ((int) ((dur + pau) * 60.0d * 1000.0d)));
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    i = i2 + 1;
                }
            }
            RecordAndPlay.this.playing = false;
            return null;
        }
    }

    public void delete_on_click(View view) {
        if (this.linearLayout.getChildCount() > 0) {
            this.linearLayout.removeAllViews();
        }
        this.action.clear();
        this.current_frame = 0;
        this.tv_title.setText("title:");
        this.New_Action_Created = false;
        Toast.makeText(this, "Frame list cleared", Toast.LENGTH_SHORT).show();
    }

    public void deleteFrame(int frameIndex) {
        if (frameIndex >= 0 && frameIndex < action.size()) {
            action.remove(frameIndex);
            current_frame--;
            update_scrollView_frames();
            Toast.makeText(this, "Frame deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
