package com.example.l04_chengbincai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TargetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
        TextView textView = findViewById(R.id.textView);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("key_name", "null");
        Integer student_number = bundle.getInt("key_student_number", 0);
        Boolean at_HQU = bundle.getBoolean("key_at_HQU", false);
        String email = intent.getStringExtra("key_email");
        textView.setText("Name: " + name + "\n ID: " + student_number + "\n At_HQU: " + at_HQU + "\n Email: " + email);
    }
}