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
import android.view.MotionEvent;
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
import java.util.Stack;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

public class RecordAndPlay extends AppCompatActivity {
    boolean New_Action_Created = false;
    boolean Previous_Frame_Recorded = true;
    Robot RoboticArm = new Robot();
    int SliderMax = 285;
    int SliderMin = 15;
    List<Frame> action = new ArrayList();
    Stack<List<Frame>> undoStack = new Stack<>();
    Stack<List<Frame>> redoStack = new Stack<>();
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
    Button stopAction;
    ImageButton undoButton;
    ImageButton redoButton;
    ImageButton loopButton;
    boolean playing = false;
    boolean looping = false;
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

        Button disconnectButton = (Button) findViewById(R.id.bt_disconnect);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Set specific positions for safe disconnect
                p1 = 150;
                p2 = 216;
                p3 = 236;
                p4 = 150;
                p5 = 150;
                p6 = 150;
                
                // Update motor angle if it's currently selected
                motorAngle.setProgress(getSelectedMotorPosition());
                
                // Send rest position command to robot
                robot.writeDegreesSyncAxMx(RoboticArm.IDs, RoboticArm.MotorTypes, 
                    new double[]{150.0d, 216.0d, 236.0d, 150.0d, 150.0d, 150.0d}, 
                    new double[]{5.0d, 5.0d, 5.0d, 5.0d, 5.0d, 5.0d},
                    57142);
                
                try {
                    Thread.sleep(3000);
                    
                    if (robot.mTcpClient != null) {
                        robot.mTcpClient.stopClient();
                    }
                    
                    Toast.makeText(RecordAndPlay.this, "Robot safely moved to rest position and disconnected", Toast.LENGTH_SHORT).show();
                    
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Toast.makeText(RecordAndPlay.this, "Error during disconnect", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Initialize new buttons
        undoButton = findViewById(R.id.bt_undo);
        redoButton = findViewById(R.id.bt_redo);
        loopButton = findViewById(R.id.bt_loop);
        stopAction = findViewById(R.id.bt_stop);
        
        // Set click listeners for new buttons
        undoButton.setOnClickListener(v -> undo_on_click(v));
        redoButton.setOnClickListener(v -> redo_on_click(v));
        loopButton.setOnClickListener(v -> loop_on_click(v));
        stopAction.setOnClickListener(v -> stop_on_click(v));

        // Initialize drag and drop functionality
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                
                if (fromPosition == 0 || toPosition == 0) {
                    // Don't allow moving the neutral frame
                    return false;
                }

                // Save state before moving
                saveToUndoStack();

                // Swap frames in the action list
                Frame movedFrame = action.get(fromPosition);
                action.remove(fromPosition);
                action.add(toPosition, movedFrame);

                // Update UI
                update_scrollView_frames();
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Not used
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(new RecyclerView(this));

        // Add touch listeners for drag and drop
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            private float startX;
            private float startY;
            private int draggedButtonIndex = -1;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        // Find which button was touched
                        for (int i = 0; i < linearLayout.getChildCount(); i++) {
                            View child = linearLayout.getChildAt(i);
                            if (child instanceof Button) {
                                float left = child.getX();
                                float right = left + child.getWidth();
                                float top = child.getY();
                                float bottom = top + child.getHeight();
                                if (startX >= left && startX <= right && startY >= top && startY <= bottom) {
                                    if (i != 0) { // Don't allow dragging neutral frame
                                        draggedButtonIndex = i;
                                        child.setAlpha(0.5f);
                                    }
                                    break;
                                }
                            }
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:
                        if (draggedButtonIndex != -1) {
                            float deltaX = event.getX() - startX;
                            View draggedButton = linearLayout.getChildAt(draggedButtonIndex);
                            draggedButton.setTranslationX(deltaX);

                            // Check if we should swap with adjacent buttons
                            for (int i = 1; i < linearLayout.getChildCount(); i++) {
                                if (i != draggedButtonIndex) {
                                    View otherButton = linearLayout.getChildAt(i);
                                    float otherCenter = otherButton.getX() + otherButton.getWidth() / 2;
                                    float draggedCenter = draggedButton.getX() + deltaX + draggedButton.getWidth() / 2;

                                    if (Math.abs(draggedCenter - otherCenter) < draggedButton.getWidth() / 2) {
                                        // Save state before swapping
                                        saveToUndoStack();

                                        // Swap frames in the action list
                                        Frame temp = action.get(draggedButtonIndex);
                                        action.set(draggedButtonIndex, action.get(i));
                                        action.set(i, temp);

                                        // Update UI
                                        draggedButton.setTranslationX(0);
                                        draggedButton.setAlpha(1.0f);
                                        draggedButtonIndex = -1;
                                        update_scrollView_frames();
                                        break;
                                    }
                                }
                            }
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (draggedButtonIndex != -1) {
                            View draggedButton = linearLayout.getChildAt(draggedButtonIndex);
                            draggedButton.setTranslationX(0);
                            draggedButton.setAlpha(1.0f);
                            draggedButtonIndex = -1;
                        }
                        break;
                }
                return true;
            }
        });
    }

    private int getSelectedMotorPosition() {
        switch (selected_button) {
            case 1: return p1;
            case 2: return p2;
            case 3: return p3;
            case 4: return p4;
            case 5: return p5;
            case 6: return p6;
            default: return 150;
        }
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
        saveToUndoStack(); // Save state before recording new frame
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
        if (!playing) {
            playing = true;
            new PlayActionTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            // Highlight current frame being played
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View child = linearLayout.getChildAt(i);
                if (child instanceof Button) {
                    child.setBackgroundResource(R.drawable.framelist_button_background_playing);
                }
            }
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

    private class PlayActionTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int currentFrame = values[0];
            // Highlight current frame
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View child = linearLayout.getChildAt(i);
                if (child instanceof Button) {
                    if (i == currentFrame) {
                        child.setBackgroundResource(R.drawable.framelist_button_background_playing);
                    } else {
                        child.setBackgroundResource(R.drawable.framelist_button_background);
                    }
                }
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            do {
                // Check if playback should continue
                if (!playing) break;

                // Initialize motor speeds
                double[] rpms = new double[RoboticArm.NumberOfMotors];
                boolean not_neutral = false;

                // Check if last frame is not in neutral position
                for (int j = 0; j < RoboticArm.NumberOfMotors; j++) {
                    if (action.get(action.size() - 1).positions[j] != 150.0d) {
                        not_neutral = true;
                        break;
                    }
                }

                // Return to neutral position if needed
                if (not_neutral) {
                    double[] positions = action.get(action.size() - 1).positions;
                    for (int j = 0; j < RoboticArm.NumberOfMotors; j++) {
                        double diff = Math.abs(RoboticArm.Neutral_Positions[j] - positions[j]);
                        if (RoboticArm.MotorTypes[j] == RoboticArm.AX12 || RoboticArm.MotorTypes[j] == RoboticArm.AX18) {
                            rpms[j] = (diff / 180.0d) / 0.03333333333333333d;
                        } else {
                            rpms[j] = (diff / 360.0d) / 0.03333333333333333d;
                        }
                    }
                    robot.writeDegreesSyncAxMx(RoboticArm.IDs, RoboticArm.MotorTypes, RoboticArm.Neutral_Positions, rpms, (long) RoboticArm.Baudrate);
                    try {
                        Thread.sleep((long) ((int) ((0.03333333333333333d + 0.03333333333333333d) * 60.0d * 1000.0d)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Play through all frames
                if (action.size() > 1) {
                    for (int i = 1; i < action.size() && playing; i++) {
                        // Update UI to show current frame
                        publishProgress(i);

                        double dur = action.get(i).duration / 60.0d;
                        double pau = action.get(i).pause / 60.0d;

                        // Calculate motor speeds
                        for (int j = 0; j < RoboticArm.NumberOfMotors; j++) {
                            double diff = Math.abs(action.get(i).positions[j] - action.get(i - 1).positions[j]);
                            if (RoboticArm.MotorTypes[j] == RoboticArm.AX12 || RoboticArm.MotorTypes[j] == RoboticArm.AX18) {
                                rpms[j] = (diff / 180.0d) / dur;
                            } else {
                                rpms[j] = (diff / 360.0d) / dur;
                            }
                        }

                        // Move motors
                        robot.writeDegreesSyncAxMx(RoboticArm.IDs, RoboticArm.MotorTypes, action.get(i).positions, rpms, (long) RoboticArm.Baudrate);

                        // Wait for duration and pause
                        try {
                            Thread.sleep((long) ((int) ((dur + pau) * 60.0d * 1000.0d)));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } while (looping && playing);

            playing = false;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Reset frame highlighting
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                View child = linearLayout.getChildAt(i);
                if (child instanceof Button) {
                    child.setBackgroundResource(R.drawable.framelist_button_background);
                }
            }
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
        saveToUndoStack(); // Save state before deleting frame
        if (frameIndex >= 0 && frameIndex < action.size()) {
            action.remove(frameIndex);
            current_frame--;
            update_scrollView_frames();
            Toast.makeText(this, "Frame deleted", Toast.LENGTH_SHORT).show();
        }
    }

    // Add new methods for undo/redo functionality
    private void saveToUndoStack() {
        List<Frame> currentState = new ArrayList<>();
        for (Frame frame : action) {
            currentState.add(new Frame(frame.positions));
            currentState.get(currentState.size() - 1).duration = frame.duration;
            currentState.get(currentState.size() - 1).pause = frame.pause;
        }
        undoStack.push(currentState);
        redoStack.clear(); // Clear redo stack when new action is performed
    }

    public void undo_on_click(View view) {
        if (!undoStack.isEmpty()) {
            // Save current state to redo stack
            List<Frame> currentState = new ArrayList<>();
            for (Frame frame : action) {
                currentState.add(new Frame(frame.positions));
                currentState.get(currentState.size() - 1).duration = frame.duration;
                currentState.get(currentState.size() - 1).pause = frame.pause;
            }
            redoStack.push(currentState);

            // Restore previous state
            action = undoStack.pop();
            current_frame = action.size() - 1;
            update_scrollView_frames();
            Toast.makeText(this, "Undo successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nothing to undo", Toast.LENGTH_SHORT).show();
        }
    }

    public void redo_on_click(View view) {
        if (!redoStack.isEmpty()) {
            // Save current state to undo stack
            List<Frame> currentState = new ArrayList<>();
            for (Frame frame : action) {
                currentState.add(new Frame(frame.positions));
                currentState.get(currentState.size() - 1).duration = frame.duration;
                currentState.get(currentState.size() - 1).pause = frame.pause;
            }
            undoStack.push(currentState);

            // Restore next state
            action = redoStack.pop();
            current_frame = action.size() - 1;
            update_scrollView_frames();
            Toast.makeText(this, "Redo successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Nothing to redo", Toast.LENGTH_SHORT).show();
        }
    }

    public void loop_on_click(View view) {
        looping = !looping;
        if (looping) {
            loopButton.setImageResource(R.drawable.loop_active);
            Toast.makeText(this, "Loop mode enabled", Toast.LENGTH_SHORT).show();
        } else {
            loopButton.setImageResource(R.drawable.loop);
            Toast.makeText(this, "Loop mode disabled", Toast.LENGTH_SHORT).show();
        }
    }

    public void stop_on_click(View view) {
        if (playing) {
            playing = false;
            looping = false;
            loopButton.setImageResource(R.drawable.loop);
            Toast.makeText(this, "Playback stopped", Toast.LENGTH_SHORT).show();
        }
    }
}
