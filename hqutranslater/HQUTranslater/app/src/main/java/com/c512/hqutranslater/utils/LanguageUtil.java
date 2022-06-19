package com.c512.hqutranslater.utils;

import java.util.HashMap;
import java.util.Map;

public class LanguageUtil {

    private static Map<String, String> textMap = new HashMap<>();
    private static Map<String, String> imgMap = new HashMap<>();

    static{
        // 设置 文本翻译 对应的接口值
        textMap.put("自动识别", "auto");
        textMap.put("中文", "zh");
        textMap.put("英文", "en");
        textMap.put("日语", "jp");
        textMap.put("德语", "dan");
        textMap.put("俄语", "ru");
        textMap.put("西班牙语", "spa");

        // 设置 图片翻译 对应的接口值
        imgMap.put("自动识别", "auto");
        imgMap.put("中文", "zh-CHS");
        imgMap.put("英文", "en");
        imgMap.put("日语", "ja");
        imgMap.put("德语", "de");
        imgMap.put("俄语", "ru");
        imgMap.put("西班牙语", "es");
    }

    /**
     * 获取文本翻译对应的语种
     * @param var 中文语种
     * @return 英文语种
     */
    public static String getToInText(String var){
        return textMap.get(var);
    }

    /**
     * 获取图片翻译对应的语种
     * @param var 中文语种
     * @return 英文语种
     */
    public static String getToInImg(String var){
        return imgMap.get(var);
    }

}
