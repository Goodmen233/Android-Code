package com.example.l13_chengbincai;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    String CHANNEL_NORMAL = "com.example.l13_chengbincai.channel_normal";
    String CHANNEL_HEADSUP = "com.example.l13_chengbincai.channel_headsup";

    private void createNormalNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Normal Channel";
            String description = "This is a normal notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_NORMAL, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createHeadsUpNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Heads-up Channel";
            String description = "This is a Heads-up notification channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_HEADSUP, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("http://www.guodegang.org/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);

        button.setOnClickListener(v->{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_NORMAL)
                    .setSmallIcon(R.drawable.ic_baseline_adb_24)
                    .setContentTitle("A Common Invitation")
                    .setContentText("You have a common invitation from the Company!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon))
                    .setColor(Color.parseColor("#A52A2A"));
            notificationManager.notify(1, builder.build());
        });

        button2.setOnClickListener(v->{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_HEADSUP)
                    .setSmallIcon(R.drawable.ic_baseline_adb_24)
                    .setContentTitle("A Heads-up Invitation")
                    .setContentText("You have a Heads-up invitation from the Company!")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon))
                    .setColor(Color.parseColor("#A52A2A"))
                    .setDefaults(Notification.DEFAULT_VIBRATE);
            notificationManager.notify(2, builder.build());
        });

        button3.setOnClickListener(v->{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_HEADSUP)
                    .setSmallIcon(R.drawable.ic_baseline_adb_24)
                    .setContentTitle("Operation")
                    .setContentText("Operation in progress")
                    .setOnlyAlertOnce(true)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setProgress(100, 0, false);
            notificationManager.notify(3, builder.build());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int PROGRESS_CURRENT = 0;
                    try {
                        for(int i = 0; i<10; i++){
                            PROGRESS_CURRENT = PROGRESS_CURRENT + 10;
                            builder.setProgress(100,PROGRESS_CURRENT,false);
                            notificationManager.notify(3, builder.build());
                            Thread.sleep(1000);
                        }
                        builder.setProgress(0, 0, false)
                                .setContentText("Operation Completed");
                        notificationManager.notify(3, builder.build());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        });

        button4.setOnClickListener(v->{
            String GROUP_KEY = "com.example.l13_chengbincai.group_key";
            Notification notificationSummary = new NotificationCompat.Builder(this, CHANNEL_NORMAL)
                    .setSmallIcon(R.drawable.ic_baseline_adb_24)
                    .setGroup(GROUP_KEY)
                    .setGroupSummary(true)
                    .build();
            notificationManager.notify(4, notificationSummary);
            long time_current = System.currentTimeMillis();
            NotificationCompat.Builder builderChild = new NotificationCompat.Builder(MainActivity.this,CHANNEL_NORMAL)
                    .setSmallIcon(R.drawable.ic_baseline_adb_24)
                    .setContentTitle("First One")
                    .setContentText("First invitation in the Group")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setWhen(time_current - 2*60*60*1000)
                    .setGroup(GROUP_KEY);
            notificationManager.notify(5, builderChild.build());

            builderChild.setContentTitle("Second One")
                    .setContentText("Second invitation in the Group")
                    .setWhen(time_current - 1*60*60*1000);
            notificationManager.notify(6,builderChild.build());

            builderChild.setContentTitle("Third One")
                    .setContentText("Third invitation in the Group")
                    .setWhen(time_current);
            notificationManager.notify(7, builderChild.build());
        });

        button5.setOnClickListener(v->{
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_HEADSUP)
                    .setSmallIcon(R.drawable.ic_baseline_adb_24)
                    .setContentTitle("CCB")
                    .setContentText("Just do it!")
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon))
                    .setColor(Color.parseColor("#A52A2A"))
                    .setDefaults(Notification.DEFAULT_LIGHTS);
            notificationManager.notify(8, builder.build());
        });

        button6.setOnClickListener(v->{
            notificationManager.cancelAll();
        });

        createNormalNotificationChannel();
        createHeadsUpNotificationChannel();
    }
}