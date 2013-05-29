package com.maxzhang.lolkankan;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: Maxzhang8
 * Date: 13-5-23
 * Time: 下午3:43
 * To change this template use File | Settings | File Templates.
 */
public class HttpHelper {

    public static String getHtmlCode(String path , String userAgent) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if(userAgent!=null || userAgent!="")
            conn.setRequestProperty("User-Agent",userAgent);
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        InputStream inStream = conn.getInputStream();
        byte[] data = readFromInput(inStream);
        String html = new String(data, "utf-8");
        return html;
    }

    private static byte[] readFromInput(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }



}
