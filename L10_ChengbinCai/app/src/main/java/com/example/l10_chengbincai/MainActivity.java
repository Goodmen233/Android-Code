package com.example.l10_chengbincai;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    String fileName, imageURL;
    File filePath;
    ImageView imageView;
    Boolean isInternalStorage, isPersistentFiles;

    private boolean isExternalStorageWriteAble(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private void downloadImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    File writeFile = new File(filePath, fileName);
                    if(!writeFile.exists()){
                        URL url = new URL(imageURL);
                        URLConnection connection = url.openConnection();
                        InputStream inputStream = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        FileOutputStream outputStream = new FileOutputStream(writeFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void readImage(){
        try{
            File readFile = new File(filePath, fileName);
            if(readFile.exists()){
                FileInputStream inputStream = new FileInputStream(readFile);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                imageView.setImageBitmap(bitmap);
            }else{
                Toast.makeText(MainActivity.this, "File doesn't exits", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void specifyFilePathAndName(){
        if (isInternalStorage != null && isPersistentFiles != null) {
            if (isInternalStorage && isPersistentFiles) {
                imageURL = "http://pic-bucket.ws.126.net/photo/0001/2022-03-18/H2P09KSQ00AN0001NOS.jpg";
                fileName = "H2P09KSQ00AN0001NOS.jpg";
                filePath = getFilesDir();
            }
            if (isInternalStorage && !isPersistentFiles) {
                imageURL = "http://pic-bucket.ws.126.net/photo/0001/2022-02-25/H11VCOTR00AO0001NOS.png";
                fileName = "H11VCOTR00AO0001NOS.png";
                filePath = getCacheDir();
            }
            if (!isInternalStorage && isPersistentFiles) {
                imageURL = "http://pic-bucket.ws.126.net/photo/0001/2022-02-25/H11S6MMN00AO0001NOS.png";
                fileName = "H11S6MMN00AO0001NOS.png";
                filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            }
            if (!isInternalStorage && !isPersistentFiles) {
                imageURL = "http://pic-bucket.ws.126.net/photo/0001/2022-02-20/H0KVFML200AN0001NOS.jpg";
                fileName = "H0KVFML200AN0001NOS.jpg";
                filePath = getExternalCacheDir();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        RadioButton internal = findViewById(R.id.InternalStorage);
        RadioButton external = findViewById(R.id.ExternalStorage);
        RadioButton persistent = findViewById(R.id.PersistentFiles);
        RadioButton temporary = findViewById(R.id.TemporaryCaches);
        TextView absolutePath = findViewById(R.id.AbsolutePath);
        Button download = findViewById(R.id.Download);
        Button display = findViewById(R.id.Display);

        if(!isExternalStorageWriteAble()){
            external.setEnabled(false);
            Toast.makeText(MainActivity.this, "External Storage is not available!", Toast.LENGTH_LONG).show();
        }
        download.setOnClickListener(view -> {
            specifyFilePathAndName();
            if(fileName != null && filePath != null){
                absolutePath.setText("Absolute Path: " + filePath);
                downloadImage();
            }
        });
        display.setOnClickListener(view -> {
            specifyFilePathAndName();
            if(fileName != null && filePath != null){
                absolutePath.setText("Absolute Path: " + filePath);
                readImage();
            }
        });
        internal.setOnClickListener(view -> {
            if(internal.isChecked()){
                isInternalStorage = true;
            }
        });
        external.setOnClickListener(view -> {
            if(external.isChecked()){
                isInternalStorage = false;
            }
        });
        persistent.setOnClickListener(view -> {
            if(persistent.isChecked()){
                isPersistentFiles = true;
            }
        });
        temporary.setOnClickListener(view -> {
            if(temporary.isChecked()){
                isPersistentFiles = false;
            }
        });
    }
}