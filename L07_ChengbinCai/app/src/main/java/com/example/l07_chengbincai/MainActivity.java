package com.example.l07_chengbincai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    Button buttonCall, buttonVisit;
    TextView name, phone, website;
    ImageView gameImage;

    public void call(){
        try {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + phone.getText()));
            startActivity(intent);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    public void showCustomToast(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_layout,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        ImageView imageView = layout.findViewById(R.id.toast_image);
        imageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        TextView textView = layout.findViewById(R.id.toast_massage);
        textView.setText(name.getText());
        textView.setTextColor(getResources().getColor(R.color.teal_700));
        Toast toast = new Toast(MainActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonCall = findViewById(R.id.call_customer_services);
        buttonVisit = findViewById(R.id.visit_official_website);
        gameImage = findViewById(R.id.imageView);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        website = findViewById(R.id.website);
        gameImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomToast();
            }
        });
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, 1);
                }else{
                    call();
                }
            }
        });
        buttonVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Visit " + website.getText() + " ?", Snackbar.LENGTH_LONG)
                        .setAction("Go", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(website.getText().toString()));
                                startActivity(intent);
                            }
                        })
                        .setActionTextColor(Color.parseColor("#C29999"))
                        .show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    call();
                }else{
                    Toast.makeText(MainActivity.this, "You denited the Call Phone permission", Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }

}