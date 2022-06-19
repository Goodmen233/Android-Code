package com.example.l02_chengbincai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button button;
    TextView textView;
    Integer message_switch = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.message);
        button = findViewById((R.id.button_next));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message_switch++;
                if(message_switch == 1){
                    textView.setText(R.string.message_1);
                }else if(message_switch == 2){
                    textView.setText(R.string.message_2);
                }else if(message_switch == 3){
                    textView.setText(R.string.message_3);
                    message_switch = 0;
                }
            }
        });
    }
}