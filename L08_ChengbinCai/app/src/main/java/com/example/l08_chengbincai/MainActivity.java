package com.example.l08_chengbincai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver br;
    Boolean airplaneModeState, wifiState;
    Boolean observeAirPlaneMode = false, observeWifi = false, isRegistered = false;
    ImageView airplaneModeStateImage, wifiStateImage;

    class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()){
                case Intent.ACTION_AIRPLANE_MODE_CHANGED:
                    airplaneModeState = intent.getBooleanExtra("state", false);
                    if (observeAirPlaneMode) {
                        if (airplaneModeState) {
                            airplaneModeStateImage.setImageResource(R.drawable.ic_baseline_airplanemode_active_24);
                        }else{
                            airplaneModeStateImage.setImageResource(R.drawable.ic_baseline_airplanemode_inactive_24);
                        }
                    }
                    break;
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    if(state == WifiManager.WIFI_STATE_ENABLED){
                        wifiState = true;
                    }else if(state == WifiManager.WIFI_STATE_DISABLED){
                        wifiState = false;
                    }
                    if (observeWifi) {
                        if (wifiState) {
                            wifiStateImage.setImageResource(R.drawable.ic_baseline_wifi_24);
                        }else{
                            wifiStateImage.setImageResource(R.drawable.ic_baseline_wifi_off_24);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void onControllerChange(View view){
        boolean isChecked = ((CheckBox) view).isChecked();
        switch (view.getId()){
            case R.id.checkBox:
                if(isChecked){
                    observeAirPlaneMode = true;
                }else{
                    observeAirPlaneMode = false;
                    airplaneModeStateImage.setImageResource(R.drawable.ic_baseline_help_24);
                }
                break;
            case R.id.checkBox2:
                if(isChecked){
                    observeWifi = true;
                }else{
                    observeWifi = false;
                    wifiStateImage.setImageResource(R.drawable.ic_baseline_help_24);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        airplaneModeStateImage = findViewById(R.id.imageView);
        wifiStateImage = findViewById(R.id.imageView2);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        Switch broadcastSwitch = findViewById(R.id.switch1);

        br = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

        broadcastSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isRegistered = true;
                    registerReceiver(br, intentFilter);
                    Toast.makeText(getApplicationContext(), "The Broadcast Receiver is registered!", Toast.LENGTH_LONG).show();
                } else {
                    isRegistered = false;
                    unregisterReceiver(br);
                    airplaneModeStateImage.setImageResource(R.drawable.ic_baseline_help_24);
                    wifiStateImage.setImageResource(R.drawable.ic_baseline_help_24);
                    Toast.makeText(getApplicationContext(), "The Broadcast Receiver is unregistered!", Toast.LENGTH_LONG).show();
                }
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            floatingActionButton.setTooltipText("Send my device's state");
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = "This is Chengbin's device " +
                        "\n Airplane Mode: " + airplaneModeState +
                        "\n Wifi: " + wifiState;
                Uri sms_to = Uri.parse("smsto:10010");
                Intent intent = new Intent(Intent.ACTION_SENDTO, sms_to);
                intent.putExtra("sms_body", message);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isRegistered){
            unregisterReceiver(br);
        }
    }

}