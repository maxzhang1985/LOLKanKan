package com.maxzhang.lolkankan;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import android.widget.Toast;
import com.maxzhang.BindingSourceAdapter.BindingSourceAdapter;

public class FindVideoPlayTask extends AsyncTask<String, Integer, String>
{
    private Context mContext;
    public FindVideoPlayTask(Context context)
    {
        mContext = context;

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
        String playerUrl = null;
        try {
            strResult = HttpHelper.getHtmlCode(httpUrl,"Mozilla/5.0 (iPhone; CPU iPhone OS 5_0_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A405 Safari/7534.48.3");
            String match = "<embed.*src=\"(.*?)\"";
            Pattern pattern = Pattern.compile(match);
            Matcher matcher = pattern.matcher(strResult);
            if(matcher.find())
            {
                playerUrl = matcher.group(1);
            }
            else
            {
                String newUrl = httpUrl.replace("lol.178.com/", "178.v.playradio.cn/");
                strResult = HttpHelper.getHtmlCode(newUrl,null);
                matcher = pattern.matcher(strResult);
                if(matcher.find())
                {
                    playerUrl = matcher.group(1);
                }
            }


            Pattern pattern1 = Pattern.compile("sid/(.*)/");
            Matcher matcher1 = pattern1.matcher(playerUrl);
            if(matcher1.find())
            {
                playerUrl = matcher1.group(1);
                playerUrl = "http://v.youku.com/player/getRealM3U8/vid/" + playerUrl +"/type/video.m3u8";
            }
            else
            {
                playerUrl =null;
            }

        } catch (Exception e) {
            Log.e("error",e.getMessage());
        }


        return playerUrl;

    }


    @Override
    protected void onPostExecute(String playerUrl) {
        if(playerUrl != null)
        {
            Toast.makeText(mContext, "正在缓冲中，请稍后......", Toast.LENGTH_LONG).show();
            Intent i = new Intent(mContext,VideoPlayActivity.class);
            i.putExtra("player",playerUrl);
            mContext.startActivity(i);
        }
        else
        {
            Toast.makeText(mContext, "系统不支持此格式，请联系管理员！", Toast.LENGTH_LONG).show();
        }
        super.onPostExecute(playerUrl);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
