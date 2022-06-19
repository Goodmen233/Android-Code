package com.c512.hqutranslater;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.c512.hqutranslater.dao.HistoryRecordDao;
import com.c512.hqutranslater.entity.HistoryRecord;
import com.c512.hqutranslater.utils.ClipboardUtil;
import com.c512.hqutranslater.utils.DbUtil;
import com.c512.hqutranslater.utils.LanguageUtil;
import com.c512.hqutranslater.utils.Translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class TextTranslate extends AppCompatActivity {

    HistoryRecordDao recordDao;

    Button translateBtn;
    Button clearBtn;
    Button clipbordBtn;
    TextView srcTextView;
    TextView destTextView;
    Spinner fromSpinner;
    Spinner toSpinner;
    LinearLayout linearLayout;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_translate);

        translateBtn = findViewById(R.id.translateBtn);
        clearBtn = findViewById(R.id.clearBtn);
        clipbordBtn = findViewById(R.id.clipbordBtn);
        srcTextView = findViewById(R.id.srcTextView);
        destTextView = findViewById(R.id.destTextView);
        fromSpinner = findViewById(R.id.fromSpin);
        toSpinner = findViewById(R.id.toSpin);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        scrollView = findViewById(R.id.scrollView);


        recordDao = DbUtil.getDao();

        /**
         * 隐藏结果框
         */
        destTextView.setVisibility(View.GONE);


        // 设置spinner默认值
        String[] vals = getResources().getStringArray(R.array.lan);
        toSpinner.setSelection(getPosSpinner(vals, "中文"), true);

        /**
         * 翻译按钮点击事件
         */
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate();
                initHistory();
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
            }
        });

        /**
         * 清除按钮点击事件
         */
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                srcTextView.setText("");
                destTextView.setText("");
                initHistory();
            }
        });

        /**
         * 剪贴板翻译按钮点击事件
         */
        clipbordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = ClipboardUtil.getClipboard(getApplicationContext());
                if("".equals(content)){
                    Toast toast = Toast.makeText(getApplicationContext(), "剪切板中没有内容！", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    srcTextView.setText(content);
                    translate();
                    initHistory();
                }
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
            }
        });

        /**
         * 动态添加history
         */
        initHistory();
    }

    /**
     * 获取内容，直接翻译，并回显和存储
     */
    private synchronized void translate(){
        destTextView.setVisibility(View.VISIBLE);
        FutureTask ft = new FutureTask<>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 获取/设置翻译内容
                String src = String.valueOf(srcTextView.getText());
                // 过滤
                if("".equals(src)){
                    Toast.makeText(getApplicationContext(), "输入有误！", Toast.LENGTH_LONG).show();
                }
                // 从spinner获取翻译类型
                String fromZh = fromSpinner.getSelectedItem().toString();
                String from = LanguageUtil.getToInText(fromZh);
                String toZh = toSpinner.getSelectedItem().toString();
                String to = LanguageUtil.getToInText(toZh);
                // 获取翻译结果
                String res = Translation.textTranslation(src, from, to);
                String dest = null;
                try {
                    JSONObject json = new JSONObject(res);
                    JSONObject json0 = (JSONObject) json.get("result");
                    JSONArray transResult = (JSONArray) json0.get("trans_result");
                    dest = (String) transResult.getJSONObject(0).get("dst");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 翻译失败
                /*
                。。。。。。。。。。。。。。。。。。。。。。
                 */
                destTextView.setText(dest);
                // 存储到历史记录
                recordDao.insertOneRecord(new HistoryRecord(src, dest, fromZh, toZh, System.currentTimeMillis()));
                return dest;
            }
        });
        new Thread(ft).start();
        // 设置到剪切板中
        try {
            ClipboardUtil.setClipboard(getApplicationContext(), String.valueOf(ft.get()));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化历史记录
     */
    public synchronized void initHistory(){
        int count = linearLayout.getChildCount();
        for(int i = 1; i < count; i++) {
            linearLayout.removeViewAt(1);
        }
        List<HistoryRecord> list = recordDao.selectTopTenRecord();
        for (int i = 0; i < list.size(); i++) {
            setHistoryRecord(linearLayout, list.get(i));
        }

    }
    

    /**
     * 将Record转换为history_item组件
     * @param linearLayout
     * @param record
     */
    public void setHistoryRecord(LinearLayout linearLayout, HistoryRecord record){

        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(linearLayout.getContext()).inflate(R.layout.history_item,linearLayout,false);
        TextView src = (TextView) layout.getChildAt(1);
        src.setText(record.getSrc());
        TextView dest = (TextView) layout.getChildAt(2);
        dest.setText(record.getDest());
        TextView recordId = (TextView) layout.getChildAt(3);
        recordId.setText(String.valueOf(record.getRecordId()));
        linearLayout.addView(layout);

        // 设置历史记录点击恢复
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("record" + record);
                // 设置输入与输出
                srcTextView.setText(record.getSrc());
                destTextView.setText(record.getDest());

                String[] vals = getResources().getStringArray(R.array.lan);

                // 设置源语种
                String fromZh = record.getFrom();
                fromSpinner.setSelection(getPosSpinner(vals, fromZh), true);

                // 设置目标语种
                String toZh = record.getTo();
                toSpinner.setSelection(getPosSpinner(vals, toZh), true);

                // 设置可见
                destTextView.setVisibility(View.VISIBLE);

                // 回到顶部
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
            }
        });

        // 设置历史记录点击删除
        ImageButton delete = (ImageButton) layout.getChildAt(0);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordDao.deleteOneRecord(record);
                linearLayout.removeView(layout);
            }
        });
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