package com.maxzhang.lolkankan;

import android.os.AsyncTask;

/**
 * Created with IntelliJ IDEA.
 * User: Maxzhang8
 * Date: 13-6-26
 * Time: 下午4:43
 * To change this template use File | Settings | File Templates.
 */
public class AsyncGetHtmlTask extends AsyncTask<String, Integer, String> {

    private boolean isBusy = false;

    public boolean getIsBusy()
    {
        return isBusy;
    }

    @Override
    protected String doInBackground(String... params) {
        if(params.length <= 0)
            return null;
        isBusy =true;
        String httpUrl = params[0];
        String html = null;
        try {
            html = HttpHelper.getHtmlCode(httpUrl ,null);
            if(_getHtmlCallback != null)
                _getHtmlCallback.OnGetHtmlAsync(html);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return html;
    }

    private OnGetHtmlCallback _getHtmlCallback;

    public void setOnGetHtmlCallback(OnGetHtmlCallback callback)
    {
        _getHtmlCallback = callback;
    }


    @Override
    protected void onPostExecute(String s) {

        isBusy =false;
        if(_getHtmlCallback != null && s != null)
            _getHtmlCallback.OnGetHtml(s);

        super.onPostExecute(s);
    }
}
