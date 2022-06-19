package com.c512.hqutranslater.utils;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Translation {

    /**
     * 文本翻译
     * 百度文本翻译
     * @param text 需要翻译的文本
     * @param from 源语言语种
     * @param to 译语言语种
     * @return json的字符串格式
     */
    public static String textTranslation(String text, String from, String to) {
        // 请求token
        String token = new AuthService().getAuth();
        // 请求url
        String url = "https://aip.baidubce.com/rpc/2.0/mt/texttrans/v1";

        try {
            // 设置参数
            JSONObject object = new JSONObject();
            object.put("from",from);
            object.put("to",to);
            object.put("q",text);
            String param = object.toString();

            System.out.println("param+++++++++++++++++++++++++++++++++++++++：" + param);

            // 设置请求头
            String contentType = "application/json;charset=utf-8";

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String result = HttpUtil.post(url, token, contentType, param);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 翻译图片
     * 有道云图片翻译 API
     * @param q 图片的base64编码字符串
     * @param from 源语言语种
     * @param to 译语言语种
     * @return json的字符串格式
     */
    public static String imageTranslation(String q, String from, String to) {
        // 请求url
        String url = "https://openapi.youdao.com/ocrtransapi";

        String APP_KEY = "79c9e99ce5001be5";

        String APP_SECRET = "xlRL5cxWUO6cTPTOvEJsPvuiWz0PT9oZ";

        try {
            String salt = String.valueOf(System.currentTimeMillis());
            String type = "1";
            String signStr = APP_KEY + q + salt + APP_SECRET;
            String sign = getDigest(signStr);
            String render = "1";
            // 设置参数
            StringBuilder param0 = new StringBuilder();
            from = URLEncoder.encode(from, "UTF-8");
            param0.append("from=" + from + "&");
            to = URLEncoder.encode(to, "UTF-8");
            param0.append("to=" + to + "&");
            type = URLEncoder.encode(type, "UTF-8");
            param0.append("type=" + type + "&");
            q = URLEncoder.encode(q, "UTF-8");
            param0.append("q=" + q + "&");
            APP_KEY = URLEncoder.encode(APP_KEY, "UTF-8");
            param0.append("appKey=" + APP_KEY + "&");
            salt = URLEncoder.encode(salt, "UTF-8");
            param0.append("salt=" + salt + "&");
            sign = URLEncoder.encode(sign, "UTF-8");
            param0.append("sign=" + sign + "&");
            render = URLEncoder.encode(render, "UTF-8");
            param0.append("render=" + render + "");


            String param = param0.toString();
            System.out.println("param+++++++++++++++++++++++++++++++++++++++：" + param);

            // 设置请求头
            String contentType = "application/x-www-form-urlencoded";

            String result = HttpUtil.postGeneralUrl(url, contentType, param, "UTF-8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成加密字段
     */
    private static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

}
