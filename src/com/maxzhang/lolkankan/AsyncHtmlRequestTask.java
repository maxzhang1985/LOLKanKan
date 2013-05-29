package com.maxzhang.lolkankan;

import android.*;
import android.R;
import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.maxzhang.BindingSourceAdapter.BindingSourceAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private ListActivity listActivity = null;
    public AsyncHtmlRequestTask(ListActivity context)
    {
        listActivity = context;
        isComplete = false;
    }



    private List<String> pageList = new ArrayList<String>();
    private ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();
    private boolean isComplete = false;

    private void addPageList(String pageString)
    {
        if(!pageList.contains(pageString))
            pageList.add(pageString);
    }

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


    public List<String> getPageList()
    {
        return pageList;
    }

    public void setPageList(List<String> pagelist)
    {
        pageList = pagelist;
    }

    public int getPageIndex()
    {
        return index;

    }

    public void setPageIndex(int i)
    {
        index = i;
    }


    private int index = 1;

    @Override
    protected String doInBackground(String... params) {
        String httpUrl ="";
        if(params.length <= 0 )
        {
            int pageCount = pageList.size();
            Log.v("page", String.valueOf(pageCount) + ", " + String.valueOf(index));
            if(pageCount > index){
                httpUrl = pageList.get(index);
                Log.v("page",String.valueOf(index));
                index = index + 1;

            }
            else
            {
                return "";
            }

        }
        else{
            httpUrl = params[0];
        }
        isComplete = false;
        String strResult = "";

        this.addPageList(httpUrl);
        try {
            strResult = HttpHelper.getHtmlCode(httpUrl,null);
            Log.v("log",strResult);
//            saveFile(strResult);
        } catch (Exception e) {
            Log.e("error",e.getMessage());
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
            this.addPageList(s);
        }

        String match1 = "<dt><a href=\"(.*?)\".*title=\"(.*?)\".*background-image:[\\s]*url\\(\\'(.*?)\\'\\);\"><span>(.*)</span>[\\s]*(<strong>(.*)</strong>)?";
        Pattern pattern1 = Pattern.compile(match1);
        Matcher matcher1 = pattern1.matcher(strResult);

        while(matcher1.find())
        {
            VideoInfo info = new VideoInfo();
            info.setUrl(matcher1.group(1));
            info.setTitle(matcher1.group(2));
            info.setImageUrl(matcher1.group(3));
            info.setTimeSpan(matcher1.group(4));
            videoList.add(info);
        }

        return strResult;

    }


    @Override
    protected void onPostExecute(String s) {
        if(s.equals(""))
            return;

        BindingSourceAdapter<VideoInfo> adapter = (BindingSourceAdapter<VideoInfo>)listActivity.getListAdapter();
        adapter.Tag= this.index;
        adapter.addAll(this.videoList);
        adapter.notifyDataSetChanged();
        this.videoList.clear();
        isComplete = true;
        super.onPostExecute(s);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
