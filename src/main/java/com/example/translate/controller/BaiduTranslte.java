package com.example.translate.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.translate.util.HttpClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/7/18 10:51
 */
@Controller
public class BaiduTranslte {
    String url = "https://fanyi-api.baidu.com/api/trans/vip/translate";
    String appId = "20210420000790954";
    String appSecret = "dcBXksrzYX26CqQFjrrf";
    Random random = new Random(System.currentTimeMillis());

    public String translate(String query, int mode) {
        String resultAnswer;
        try {
            query = toEnglishName(query);
            System.out.println(query);
            String result = null;
            Long salt = random.nextLong();
            String a = new StringBuilder(appId)
                    .append(query == null ? "" : query)
                    .append(salt == null ? "" : salt)
                    .append(appSecret == null ? "" : appSecret)
                    .toString();
            System.out.println(a);
            String sign = getMD5(a);
            System.out.println(sign);

            Map<String, String> params = new HashMap<>();
            params.put("q", query);
            params.put("from", "auto");
            if (mode == 1)
                params.put("to", "zh");
            else
                params.put("to", "en");
            params.put("appid", appId);
            params.put("salt", salt.toString());
            params.put("sign", sign);
            String paramsJson = JSON.toJSONString(params);

            System.out.println(paramsJson);

            result = HttpClientUtil.doGet(url, params);
            System.out.println(result);

            JSONObject resultJson = JSON.parseObject(result);
            resultAnswer = resultJson.getJSONArray("trans_result").getJSONObject(0).getString("dst");
            resultAnswer = unicodeToUtf8(resultAnswer);
            System.out.println(resultAnswer);
        }catch (Exception e){
            System.out.println(e);
            return "翻译错误";
        }

        return resultAnswer;
    }

    public static String getMD5(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] result = md.digest(string.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                int sign = b & 0xff;
                String str = Integer.toHexString(sign);
                if (str.length() == 1) {
                    sb.append("0");
                }
                sb.append(str);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String unicodeToUtf8(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't'){
                        aChar = '\t';
                    }
                    if (aChar == 'r') {
                        aChar = '\r';
                    }
                    if (aChar == 'n') {
                        aChar = '\n';
                    }
                    if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    public static String toEnglishName(String s) {
        if (s == null) {//如果为空返回null
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);//返回这个字符串的指定索引处的char值
            boolean nextUpperCase = true;
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));//判断指定字符是否为大写字母
            }
            if ((i >= 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    if (i > 0) sb.append(" ");//相当于"+" (加下划线，DownLine标识下划线)
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();//转字符串
    }

}
