package com.example.l05_chengbincai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SingleInstanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_instance);

        Log.d("ChengbinCai", toString());
        Log.d("ChengbinCai", "Task ID: " + getTaskId());

        Button button9 = findViewById(R.id.button9);
        Button button10 = findViewById(R.id.button10);
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleInstanceActivity.this, SingleInstanceActivity.class);
                startActivity(intent);
            }
        });
        button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SingleInstanceActivity.this, StandardActivity.class);
                startActivity(intent);
            }
        });
    }
}