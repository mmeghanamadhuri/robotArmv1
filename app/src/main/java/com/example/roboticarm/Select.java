package com.example.roboticarm;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class Select extends AppCompatActivity {
    Button Control_click;
    Button Record_click;
    String ip;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_select);
        this.Control_click = (Button) findViewById(R.id.bt_control);
        this.Record_click = (Button) findViewById(R.id.bt_record);
        this.ip = getIntent().getStringExtra("ip_add");
    }


    public void control_on_click(View view) {
        Intent intent = new Intent(this, ArmControl.class);
        intent.putExtra("ip_add", this.ip);
        startActivity(intent);
    }

    public void record_on_click(View view) {
        Intent intent = new Intent(this, RecordAndPlay.class);
        intent.putExtra("ip_add", this.ip);
        startActivity(intent);
    }


}
