package com.c512.hqutranslater;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.c512.hqutranslater.utils.DbUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int TAKE_PHOTO = 11;//拍照
    private static final int LOCAL_CROP = 13;//图库

    private Uri imageUri;

    //要申请的权限（可以一次申请多个权限），存放到数组里
    private static final String permission[] = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取拍照、文件权限
        for (int i = 0; i < permission.length; i++) {
            //判断是否获得权限
            if(ContextCompat.checkSelfPermission(MainActivity.this, permission[i]) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission[i]}, 1);
            }
        }

        // 取消严格模式  FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy( builder.build() );
        }

        // 初始化数据库
        DbUtil.init(getApplicationContext());

        Button textBtn = findViewById(R.id.text_btn);
        Button cameraBtn = findViewById(R.id.camera_btn);
        Intent intent = new Intent();

        // 跳转文本翻译页面
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.setClass(MainActivity.this,TextTranslate.class);
                startActivity(intent);
            }
        });

        // 跳转拍照翻译页面
        cameraBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                takePhotoOrSelectPic();
            }
        });



    }

    /**
     * 拍照、选择图片方法
     */
    private void takePhotoOrSelectPic() {
        CharSequence[] items = {"拍照","图库"};

        // 弹窗选择拍照还是图库
        new AlertDialog.Builder(MainActivity.this).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {

                Intent intent;

                switch (choice){
                    // 拍照
                    case 0:
                        // 创建文件保存拍照的图片
                        File dir = new File(getApplicationContext().getExternalFilesDir(null).getPath()+"QTranslator");
                        File takePhotoImage= null;
                        try {
                            if (!dir.exists()){
                                dir.mkdir();
                            }
                            // 根据路径名自动创建一个新的空文件

                            takePhotoImage = new File(dir+"/"+"take_Photo_image.jpg");
                            if(takePhotoImage.exists()){
                                takePhotoImage.delete();
                            }
                            takePhotoImage.createNewFile();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 获取文件图片文件的uri对象
                        imageUri = Uri.fromFile(takePhotoImage);
                        //创建Intent，启用手机的相机功能
                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 指定输出到文件Uri中
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                        // 启动intent开始拍照
                        startActivityForResult(intent,TAKE_PHOTO);
                        break;
                    case 1:
                        // 创建Intent，用于打开手机本地图库
                        intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // 启动intent
                        startActivityForResult(intent,LOCAL_CROP);
                        break;

                }
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO://拍照
                if (resultCode == RESULT_OK) {
                    // 创建intent
                    Intent intent = new Intent(MainActivity.this, CameraTranslate.class);
                    // 设置文件为uri，类型为图片格式
                    intent.setDataAndType(imageUri, "image/*");
                    // 指定输出到文件uri中
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    // 启动intent
                    startActivity(intent);
                }
                break;
            case LOCAL_CROP://调用图库
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(MainActivity.this, CameraTranslate.class);
                    //获图库所选图片的uri
                    Uri uri = data.getData();
                    intent.setDataAndType(uri,"image/*");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    //启动intent
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


}