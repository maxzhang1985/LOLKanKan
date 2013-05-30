package com.maxzhang.lolkankan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.maxzhang.BindingSourceAdapter.BindingSourceAdapter;

public class FindVideoPlayTask extends AsyncTask<String, Integer, String>
{
   
    public FindVideoPlayTask()
    {
    

    }


    public static void saveFile(String result){
        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
        Log.v("file",root.toString());
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
    	String strResult;
        String httpUrl = params[0];
       
       
        try {
            strResult = HttpHelper.getHtmlCode(httpUrl,"Mozilla/5.0 (iPhone; CPU iPhone OS 5_0_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A405 Safari/7534.48.3");
            Log.v("log",strResult);
            saveFile(strResult);
        } catch (Exception e) {
            Log.e("error",e.getMessage());
        }
        /*int postion = httpUrl.indexOf(".html");
        String match = httpUrl.substring(0,postion) + "_\\d*" + httpUrl.substring(postion);
        Log.v(match, "log");
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(strResult);

        while(matcher.find())
        {
            String s= matcher.group();
            Log.v(s,"log");
        }

 		*/

        return null;

    }


    @Override
    protected void onPostExecute(String s) {
     
      
        super.onPostExecute(s);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
