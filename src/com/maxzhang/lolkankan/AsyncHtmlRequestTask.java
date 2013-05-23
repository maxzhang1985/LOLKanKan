package com.maxzhang.lolkankan;

import android.*;
import android.R;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Maxzhang8
 * Date: 13-5-23
 * Time: 下午3:46
 * To change this template use File | Settings | File Templates.
 */
public class AsyncHtmlRequestTask extends AsyncTask<String, Integer, String>
{
    private ArrayList<String> list = new ArrayList<String>();

    private void saveFile(String result){
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
        if (!root.exists())
        {
            root.mkdirs();
        }
        try
        {
            File gpxfile = new File(root, "log.txt");
            FileWriter writer = new FileWriter(gpxfile,false);
            writer.write(result);
            writer.flush();
            writer.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }

    }

    @Override
    protected String doInBackground(String... params) {
        String strResult = "";
        String httpUrl = params[0];
        try {
            strResult = HttpHelper.getHtmlCode(httpUrl);
            saveFile(strResult);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //Log.e("error",e.getMessage());
            System.out.print(e.getMessage());
        }
        int postion = httpUrl.indexOf(".html");
        String match = httpUrl.substring(0,postion) + "_\\d*" + httpUrl.substring(postion);
        Log.v(match, "log");
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(strResult);
        while(matcher.find())
        {

            String s= matcher.group();
            Log.v(s,"log");
            list.add(s);
        }

        String match1 = "<dt><a href=\"(.*?)\".*title=\"(.*?)\".*background-image:[\\s]*url\\(\\'(.*?)\\'\\);\"><span>(.*)</span>[\\s]*(<strong>(.*)</strong>)?";
        Pattern pattern1 = Pattern.compile(match1);
        Matcher matcher1 = pattern1.matcher(strResult);

        while(matcher1.find())
        {
            int count = matcher1.groupCount();
            for(int i =1 ; i< count ; i++){
                String s= matcher1.group(i);
                Log.v(s,"log");
            }
        }

        return strResult;

    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
