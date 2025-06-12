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
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.os.Build;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.graphics.Rect;
import android.os.Handler;
import androidx.core.content.ContextCompat;

public class RecordAndPlay extends AppCompatActivity {
    // Constants
    private static final float DRAG_SCALE = 1.1f;
    private static final int SCROLL_SPEED = 10;
    private static final int SCROLL_EDGE_THRESHOLD = 100; // pixels from edge to start scrolling
    private static final int LONG_PRESS_DURATION = 500; // milliseconds

    // State flags
    boolean New_Action_Created = false;
    boolean Previous_Frame_Recorded = true;
    Robot RoboticArm = new Robot();
    int SliderMax = 285;
    int SliderMin = 15;
    List<Frame> action = new ArrayList<>();
    Stack<List<Frame>> undoStack = new Stack<>();
    Stack<List<Frame>> redoStack = new Stack<>();
    String action_name = "";
    int current_frame = 0;
    String ip;
    int p1 = 150;
    int p2 = 150;
    int p3 = 150;
    int p4 = 150;
    int p5 = 150;
    int p6 = 150;
    boolean playing = false;
    boolean looping = false;
    boolean[] pressed = {false, false, false, false, false, false};
    int resolution = 1;
    RobotCom robot = new RobotCom();
    int selected_button = 1;
    int stepSize = 1;

    // View declarations
    private Button playAction;
    private Button loopButton;
    private ImageButton undoButton;
    private ImageButton redoButton;
    private ImageButton sb_Dec;
    private ImageButton sb_Inc;
    private Button motor1;
    private Button motor2;
    private Button motor3;
    private Button motor4;
    private Button motor5;
    private Button gripper;
    private Button neutral;
    private Button newAction;
    private Button newFrame;
    private Button recordFrame;
    private Button saveAction;
    private Button loadAction;
    private SeekBar motorAngle;
    private TextView tv_motorNum;
    private TextView tv_title;
    private HorizontalScrollView scrollView;
    private LinearLayout linearLayout;

    // State variables
    private boolean isPlayingAction = false;
    private int currentPlayingFrame = 0;
    private Handler playHandler = new Handler();
    private Handler scrollHandler = new Handler();
    private boolean isDragging = false;
    private View draggedView = null;
    private int draggedPosition = -1;
    private float dragStartX;
    private float lastTouchX;

    private Runnable playRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isPlayingAction) {
                return;
            }

            if (currentPlayingFrame < action.size()) {
                Frame frame = action.get(currentPlayingFrame);
                double[] positions = frame.getPositions();
                robot.writeDegreesSyncAxMx(
                    RoboticArm.IDs,
                    RoboticArm.MotorTypes,
                    positions,
                    RoboticArm.Neutral_RPMs,
                    RoboticArm.Baudrate
                );
                
                highlightCurrentFrame(currentPlayingFrame);
                currentPlayingFrame++;
                
                // Schedule next frame using frame duration
                playHandler.postDelayed(this, (long)frame.getDuration());
            } else {
                if (looping) {
                    currentPlayingFrame = 0;
                    playHandler.post(this);
                } else {
                    stopPlayback();
                }
            }
        }
    };

    /* access modifiers changed from: protected */
    @SuppressLint({"ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_and_play);

        // Initialize views and controls
        initializeViews();
        setupMotorControls();
        setupFrameList();
        
        // Get IP from intent and connect
        ip = getIntent().getStringExtra("ip_add");
        if (ip != null && robot != null) {
            robot.openTcp(ip);
            robot.writeDegreesSyncAxMx(
                RoboticArm.IDs,
                RoboticArm.MotorTypes,
                RoboticArm.Neutral_Positions,
                RoboticArm.Neutral_RPMs,
                RoboticArm.Baudrate
            );
        }
    }

    private void initializeViews() {
        // Initialize all buttons and views
        playAction = findViewById(R.id.bt_playAction);
        playAction.setText(R.string.play_stop);
        loopButton = findViewById(R.id.bt_loop);
        undoButton = findViewById(R.id.bt_undo);
        redoButton = findViewById(R.id.bt_redo);
        sb_Dec = findViewById(R.id.bt_sliderDec);
        sb_Inc = findViewById(R.id.bt_sliderInc);
        motor1 = findViewById(R.id.bt_motor1);
        motor2 = findViewById(R.id.bt_motor2);
        motor3 = findViewById(R.id.bt_motor3);
        motor4 = findViewById(R.id.bt_motor4);
        motor5 = findViewById(R.id.bt_motor5);
        gripper = findViewById(R.id.bt_gripper);
        neutral = findViewById(R.id.bt_neutral);
        newAction = findViewById(R.id.bt_newAction);
        newFrame = findViewById(R.id.bt_newFrame);
        recordFrame = findViewById(R.id.bt_recordFrame);
        saveAction = findViewById(R.id.bt_saveAction);
        loadAction = findViewById(R.id.bt_loadAction);
        motorAngle = findViewById(R.id.sb_motorAngle);
        tv_motorNum = findViewById(R.id.tv_motorNumber);
        tv_title = findViewById(R.id.tv_title);
        scrollView = findViewById(R.id.sv_frameList);
        linearLayout = findViewById(R.id.ll_frames);

        // Set initial text
        tv_motorNum.setText(R.string.motor1);
    }

    private void setupFrameList() {
        playAction.setOnClickListener(v -> play_on_click(v));
        loopButton.setOnClickListener(v -> {
            looping = !looping;
            loopButton.setSelected(looping);
        });
        undoButton.setOnClickListener(v -> undo_on_click(v));
        redoButton.setOnClickListener(v -> redo_on_click(v));
        scrollView.setOnTouchListener((v, event) -> {
            if (isDragging) {
                handleDrag(event);
                return true;
            }
            return false;
        });
    }

    private void setupMotorControls() {
        motorAngle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    switch (selected_button) {
                        case 1:
                            p1 = progress;
                            break;
                        case 2:
                            p2 = progress;
                            break;
                        case 3:
                            p3 = progress;
                            break;
                        case 4:
                            p4 = progress;
                            break;
                        case 5:
                            p5 = progress;
                            break;
                        case 6:
                            p6 = progress;
                            break;
                    }
                    updateRobotPosition();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Initialize motor buttons with click listeners
        motor1.setOnClickListener(v -> {
            selected_button = 1;
            motorAngle.setProgress(p1);
            tv_motorNum.setText(getString(R.string.motor1));
        });

        motor2.setOnClickListener(v -> {
            selected_button = 2;
            motorAngle.setProgress(p2);
            tv_motorNum.setText(getString(R.string.motor2));
        });

        motor3.setOnClickListener(v -> {
            selected_button = 3;
            motorAngle.setProgress(p3);
            tv_motorNum.setText(getString(R.string.motor3));
        });

        motor4.setOnClickListener(v -> {
            selected_button = 4;
            motorAngle.setProgress(p4);
            tv_motorNum.setText(getString(R.string.motor4));
        });

        motor5.setOnClickListener(v -> {
            selected_button = 5;
            motorAngle.setProgress(p5);
            tv_motorNum.setText(getString(R.string.motor5));
        });

        gripper.setOnClickListener(v -> {
            selected_button = 6;
            motorAngle.setProgress(p6);
            tv_motorNum.setText(getString(R.string.gripper));
        });

        neutral.setOnClickListener(v -> {
            p1 = 150;
            p2 = 150;
            p3 = 150;
            p4 = 150;
            p5 = 150;
            p6 = 150;
            motorAngle.setProgress(150);
            updateRobotPosition();
        });
    }

    private void updateRobotPosition() {
        if (robot != null) {
            robot.writeDegreesSyncAxMx(
                RoboticArm.IDs,
                RoboticArm.MotorTypes,
                new double[]{p1, p2, p3, p4, p5, p6},
                RoboticArm.Neutral_RPMs,
                RoboticArm.Baudrate
            );
        }
    }

    public void newFrame_on_click(View view) {
        // Check for duplicate frame
        if (!action.isEmpty()) {
            Frame lastFrame = action.get(action.size() - 1);
            double[] lastPositions = lastFrame.getPositions();
            if (lastPositions[0] == p1 && lastPositions[1] == p2 && 
                lastPositions[2] == p3 && lastPositions[3] == p4 && 
                lastPositions[4] == p5 && lastPositions[5] == p6) {
                Toast.makeText(this, "Frame with identical positions already exists", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        
        Frame frame = new Frame(p1, p2, p3, p4, p5, p6);
        action.add(frame);
        saveToUndoStack();
        updateFrameList();
        current_frame = action.size() - 1;
        updateMotorPositions();
    }

    private void updateFrameList() {
        linearLayout.removeAllViews();
        
        for (int i = 0; i < action.size(); i++) {
            TextView item = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 4, 8, 4);
            item.setLayoutParams(params);
            item.setText("Frame " + (i + 1));
            item.setTextSize(14);
            item.setPadding(16, 8, 16, 8);
            item.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, android.R.color.white));
            item.setElevation(0f);
            
            final int position = i;
            
            // Add long click listener for deletion
            item.setOnLongClickListener(v -> {
                if (!isDragging) {
                    new AlertDialog.Builder(this)
                        .setTitle("Delete Frame")
                        .setMessage("Are you sure you want to delete this frame?")
                        .setPositiveButton("Delete", (dialog, which) -> deleteFrame(position))
                        .setNegativeButton("Cancel", null)
                        .show();
                }
                return true;
            });
            
            item.setOnTouchListener(createFrameTouchListener(position));
            linearLayout.addView(item);
        }
        
        // Highlight current frame if exists
        if (current_frame >= 0 && current_frame < action.size()) {
            highlightCurrentFrame(current_frame);
        }
    }

    private View.OnTouchListener createFrameTouchListener(final int position) {
        return new View.OnTouchListener() {
            private long pressStartTime;
            private boolean isLongPress = false;
            private float initialTouchX;
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pressStartTime = System.currentTimeMillis();
                        initialTouchX = event.getRawX();
                        v.setPressed(true);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        if (!isLongPress && 
                            System.currentTimeMillis() - pressStartTime > LONG_PRESS_DURATION) {
                            if (position != 0) {
                                startDrag(v, position);
                            }
                        }
                        
                        if (isDragging && draggedView == v) {
                            handleDrag(event);
                        }
                        return true;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (isDragging && draggedView == v) {
                            endDrag();
                        } else if (!isLongPress && 
                                 Math.abs(event.getRawX() - initialTouchX) < 10) {
                            handleFrameClick(position);
                        }
                        
                        v.setPressed(false);
                        isLongPress = false;
                        return true;
                }
                return false;
            }
        };
    }

    private void startDrag(View view, int position) {
        isDragging = true;
        draggedView = view;
        draggedPosition = position;
        
        view.setElevation(8f);
        view.setScaleX(DRAG_SCALE);
        view.setScaleY(DRAG_SCALE);
        view.setAlpha(0.7f);
        view.setBackgroundResource(R.color.light_grey);
        
        scrollHandler.post(autoScrollRunnable);
    }

    private void handleDrag(MotionEvent event) {
        float deltaX = event.getRawX() - lastTouchX;
        draggedView.setTranslationX(draggedView.getTranslationX() + deltaX);
        lastTouchX = event.getRawX();

        // Check for position swap
        float centerX = draggedView.getX() + draggedView.getWidth() / 2;
        int newPosition = -1;

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (i != draggedPosition) {
                View child = linearLayout.getChildAt(i);
                float childCenterX = child.getX() + child.getWidth() / 2;
                if (Math.abs(centerX - childCenterX) < child.getWidth() / 2) {
                    newPosition = i;
                    break;
                }
            }
        }

        if (newPosition != -1 && newPosition != draggedPosition && newPosition != 0) {
            saveToUndoStack();
            Frame draggedFrame = action.remove(draggedPosition);
            action.add(newPosition, draggedFrame);
            draggedPosition = newPosition;
            updateFrameList();
            
            // Maintain drag visual state
            View v = linearLayout.getChildAt(newPosition);
            v.setElevation(8f);
            v.setScaleX(DRAG_SCALE);
            v.setScaleY(DRAG_SCALE);
            v.setAlpha(0.7f);
            v.setBackgroundResource(R.color.light_grey);
            
            Toast.makeText(this, 
                "Frame moved to position " + (newPosition + 1), 
                Toast.LENGTH_SHORT).show();
        }
    }

    private void endDrag() {
        if (draggedView != null) {
            draggedView.setElevation(0f);
            draggedView.setScaleX(1f);
            draggedView.setScaleY(1f);
            draggedView.setAlpha(1f);
            draggedView.setTranslationX(0);
            draggedView.setBackgroundResource(android.R.color.white);
        }
        
        scrollHandler.removeCallbacks(autoScrollRunnable);
        isDragging = false;
        draggedView = null;
        draggedPosition = -1;
    }

    private void handleFrameClick(int position) {
        current_frame = position;
        updateMotorPositions();
        highlightSelectedFrame(position);
    }

    private void highlightSelectedFrame(int position) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            if (i == position) {
                child.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, R.color.dark_pink));
            } else {
                child.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, android.R.color.white));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (robot != null) {
            robot.closeTcp();
        }
        if (playHandler != null) {
            playHandler.removeCallbacks(playRunnable);
            playHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    private void updateMotorPositions() {
        if (current_frame >= 0 && current_frame < action.size()) {
            Frame frame = action.get(current_frame);
            double[] positions = frame.getPositions();
            p1 = (int)positions[0];
            p2 = (int)positions[1];
            p3 = (int)positions[2];
            p4 = (int)positions[3];
            p5 = (int)positions[4];
            p6 = (int)positions[5];

            motorAngle.setProgress((int)positions[selected_button - 1]);
            updateRobotPosition();
        }
    }

    public void play_on_click(View view) {
        if (!isPlayingAction) {
            startPlayback();
        } else {
            stopPlayback();
        }
    }

    private void saveToUndoStack() {
        List<Frame> currentState = new ArrayList<>();
        for (Frame frame : action) {
            Frame copy = new Frame(
                frame.getName(),
                frame.getDuration(),
                frame.getPause(),
                frame.getPositions().clone()
            );
            currentState.add(copy);
        }
        undoStack.push(currentState);
        redoStack.clear();
    }

    public void undo_on_click(View view) {
        if (!undoStack.isEmpty()) {
            // Save current state to redo stack
            List<Frame> currentState = new ArrayList<>();
            for (Frame frame : action) {
                currentState.add(new Frame(
                    frame.name,
                    frame.duration,
                    frame.pause,
                    frame.positions.clone()
                ));
            }
            redoStack.push(currentState);
            
            // Restore previous state
            action = undoStack.pop();
            updateFrameList();
            Toast.makeText(this, "Undo successful", Toast.LENGTH_SHORT).show();
        }
    }

    public void redo_on_click(View view) {
        if (!redoStack.isEmpty()) {
            // Save current state to undo stack
            List<Frame> currentState = new ArrayList<>();
            for (Frame frame : action) {
                currentState.add(new Frame(
                    frame.name,
                    frame.duration,
                    frame.pause,
                    frame.positions.clone()
                ));
            }
            undoStack.push(currentState);
            
            // Restore next state
            action = redoStack.pop();
            updateFrameList();
            Toast.makeText(this, "Redo successful", Toast.LENGTH_SHORT).show();
        }
    }

    private Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (isDragging) {
                int scrollX = scrollView.getScrollX();
                int width = scrollView.getWidth();
                float touchX = lastTouchX - scrollView.getLeft();

                if (touchX < SCROLL_EDGE_THRESHOLD) {
                    scrollView.smoothScrollBy(-SCROLL_SPEED, 0);
                } else if (touchX > width - SCROLL_EDGE_THRESHOLD) {
                    scrollView.smoothScrollBy(SCROLL_SPEED, 0);
                }
                scrollHandler.postDelayed(this, 16); // ~60fps
            }
        }
    };

    private void highlightCurrentFrame(int frameIndex) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            if (i == frameIndex && isPlayingAction) {
                child.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, R.color.very_light_grey));
            } else if (i == current_frame && !isPlayingAction) {
                child.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, R.color.dark_pink));
            } else {
                child.setBackgroundColor(androidx.core.content.ContextCompat.getColor(this, android.R.color.white));
            }
        }
    }

    private void startPlayback() {
        if (!isPlayingAction && !action.isEmpty()) {
            isPlayingAction = true;
            currentPlayingFrame = 0;
            playHandler.post(playRunnable);
            playAction.setText(R.string.stop);
        }
    }

    private void stopPlayback() {
        isPlayingAction = false;
        playHandler.removeCallbacks(playRunnable);
        playAction.setText(R.string.play);
        currentPlayingFrame = 0;
        updateMotorPositions();
    }

    private void deleteFrame(int position) {
        if (position >= 0 && position < action.size()) {
            saveToUndoStack();
            action.remove(position);
            
            // Update current frame if needed
            if (current_frame >= action.size()) {
                current_frame = action.size() - 1;
            }
            
            updateFrameList();
            if (current_frame >= 0) {
                updateMotorPositions();
            }
            
            Toast.makeText(this, "Frame deleted", Toast.LENGTH_SHORT).show();
        }
    }

    public void recordFrame_on_click(View view) {
        if (current_frame >= 0 && current_frame < action.size()) {
            saveToUndoStack();
            Frame frame = action.get(current_frame);
            frame.setPositions(new double[]{p1, p2, p3, p4, p5, p6});
            updateFrameList();
            Toast.makeText(this, "Frame updated", Toast.LENGTH_SHORT).show();
        } else {
            // If no frame is selected, create a new one
            Frame frame = new Frame(p1, p2, p3, p4, p5, p6);
            action.add(frame);
            saveToUndoStack();
            current_frame = action.size() - 1;
            updateFrameList();
            Toast.makeText(this, "New frame recorded", Toast.LENGTH_SHORT).show();
        }
        updateMotorPositions();
    }
}
