package com.maxzhang.lolkankan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created with IntelliJ IDEA.
 * User: Maxzhang8
 * Date: 13-6-5
 * Time: 下午4:12
 * To change this template use File | Settings | File Templates.
 */
public class StartActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startpage);
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 2000);
    }

    class splashhandler implements Runnable{

        public void run() {
            startActivity(new Intent(getApplication(),MyActivity.class));
            StartActivity.this.finish();
        }

    }


}