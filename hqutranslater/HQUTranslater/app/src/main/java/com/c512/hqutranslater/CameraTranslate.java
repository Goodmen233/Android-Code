package com.c512.hqutranslater;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.c512.hqutranslater.utils.ClipboardUtil;
import com.c512.hqutranslater.utils.ImgUtil;
import com.c512.hqutranslater.utils.LanguageUtil;
import com.c512.hqutranslater.utils.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CameraTranslate extends AppCompatActivity {

    String originImgBase64;
    String resImgBase64;
    boolean isOrigin = false;

//    private ImageButton imageTranslateBtn;
    private Spinner fromSpin;
    private Spinner toSpin;
    private ImageButton back;
    private ImageView resImgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_translate);

//        imageTranslateBtn = findViewById(R.id.imageTranslateBtn);
        fromSpin = findViewById(R.id.fromSpin);
        toSpin = findViewById(R.id.toSpin);
        back = findViewById(R.id.back_btn);
        resImgView = findViewById(R.id.resImage);

        init();

        /**
         * 翻译按钮的点击事件
         */
        toSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                translate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        fromSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                translate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        /**
         * 返回按钮的点击事件
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /**
         * 点击图片事件
         */
        resImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOrigin){
                    resImgView.setImageBitmap(ImgUtil.base64ToBitmap(resImgBase64));
                }else{
                    resImgView.setImageBitmap(ImgUtil.base64ToBitmap(originImgBase64));
                }
                isOrigin = ! isOrigin;
            }
        });
    }

    /**
     * 初始化事件
     */
    private void init(){
        // 设置spinner默认值
        String[] vals = getResources().getStringArray(R.array.lan);
        toSpin.setSelection(getPosSpinner(vals, "中文"), true);
        // 获取传入的图片,进行翻译
        Uri imgUri = getIntent().getData();
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
            // 压缩图片
//            bm = Bitmap.createScaledBitmap(bm, 600, 900, true);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.PNG,100,baos);
//            InputStream is = new ByteArrayInputStream(baos.toByteArray());
//
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            bm = BitmapFactory.decodeStream(is,null,options);
//            System.out.println(bm);
            originImgBase64 = ImgUtil.bitmapToBase64(bm);
            translate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 翻译图片，回显结果
     */
    private void translate(){
        FutureTask ft = new FutureTask<>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 从spinner获取翻译类型
                String fromZh = fromSpin.getSelectedItem().toString();
                String from = LanguageUtil.getToInImg(fromZh);
                String toZh = toSpin.getSelectedItem().toString();
                String to = LanguageUtil.getToInImg(toZh);
                // 获取翻译结果
                String res = Translation.imageTranslation(originImgBase64, from, to);
                return res;
            }
        });
        new Thread(ft).start();

        isOrigin = false;
        // 获取base64，回显图片
        try {
            String jsonStr = (String) ft.get();
            try {
                JSONObject json = new JSONObject(jsonStr);
                resImgBase64 = (String) json.get("render_image");
                // 获取翻译结果到剪切板
                String res = getTranslateRes(json);
                ClipboardUtil.setClipboard(getApplicationContext(), res);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(jsonStr != null){
                resImgView.setImageBitmap(ImgUtil.base64ToBitmap(resImgBase64));
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将翻译结果的文字提取为文本
     * @param json json对象
     * @return 结果字符串
     */
    private String getTranslateRes(JSONObject json){
        StringBuilder sb = new StringBuilder();
        String tem = null;
        try {
            JSONArray lines = json.getJSONArray("resRegions");
            for (int i = 0; i < lines.length(); i++) {
                tem = (String) lines.getJSONObject(i).get("tranContent");
                sb.append(tem + " ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private int getPosSpinner(String[] vals, String val){
        int pos = 1;
        for (int i = 0; i < vals.length; i++) {
            if(val.equals(vals[i])){
                pos = i;
                break;
            }
        }
        return pos;
    }

}