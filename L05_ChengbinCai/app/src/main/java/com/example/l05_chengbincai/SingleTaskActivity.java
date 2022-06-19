package com.example.l05_chengbincai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SingleTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);

        Log.d("ChengbinCai", toString());
        Log.d("ChengbinCai", "Task ID: " + getTaskId());

        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleTaskActivity.this, SingleTaskActivity.class);
                startActivity(intent);
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleTaskActivity.this, StandardActivity.class);
                startActivity(intent);
            }
        });
    }
}