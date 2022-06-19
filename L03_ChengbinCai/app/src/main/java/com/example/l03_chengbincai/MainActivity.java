package com.example.l03_chengbincai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    Integer i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Log.d("ChengbinCai","MainActivity calls onCreate()");
        TextView record = findViewById(R.id.display_game_record);
        Button button_click = findViewById(R.id.button_click);
        Button button_quit = findViewById(R.id.button_quit);
        if(savedInstanceState != null){
            i = savedInstanceState.getInt("user_record");
            record.setText("Click record:" + i);
        }else{
            record.setText("Please Click!");
        }
        button_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = i + 1;
                record.setText("Click record:" + i);
                if(i == 10){
                    Intent intent = new Intent(MainActivity.this, DialogActivity.class);
                    startActivity(intent);
                    record.setText("Please Click!");
                    i = 0;
                }
            }
        });
        button_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("user_record", i);
        Log.d("ChengbinCai","MainActivity onSaveinstanceState user_record" + i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ChengbinCai","MainActivity calls onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ChengbinCai","MainActivity calls onResume()");
        Log.d("ChengbinCai","MainActivity Running, it is fully visible");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("ChengbinCai","MainActivity calls onPause()");
        Log.d("ChengbinCai","MainActivity is partially visible");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("ChengbinCai","MainActivity calls onStop()");
        Log.d("ChengbinCai","MainActivity is completely invisible");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("ChengbinCai","MainActivity calls onRestart()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ChengbinCai","MainActivity calls onDestroy()");
    }
}