package com.c512.hqutranslater.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

public class ClipboardUtil {

    /**
     * 获取剪切板内容
     * @param context
     * @return
     */
    public static String getClipboard(Context context){
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                ClipData clipData = manager.getPrimaryClip();
                String addedText = clipData.getItemAt(0).getText().toString();
                if (!addedText.isEmpty()) {
                    return addedText;
                }
            }
        }
        return "";
    }

    /**
     * 设置剪切板内容
     * @param context 内容
     */
    public static void setClipboard(Context context, String content){
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if(manager != null){
            ClipData data = ClipData.newPlainText("OcrText", content);
            manager.setPrimaryClip(data);
            Toast.makeText(context, "已经粘贴到剪切板中！", Toast.LENGTH_LONG).show();
        }
    }
}
