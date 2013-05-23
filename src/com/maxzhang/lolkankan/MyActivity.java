package com.maxzhang.lolkankan;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MyActivity extends ListActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(Hander);

    }


    View.OnClickListener Hander = new View.OnClickListener() {

        public void onClick(View v) {


            AsyncHtmlRequestTask task = new AsyncHtmlRequestTask();
            task.execute("http://lol.178.com/list/video.html");


            // TODO Auto-generated method stub
            //UserInfo u3 = new UserInfo();
            //u3.setName1("1");
            //u3.setName2("2");
            //users.add(u3);
            //users.add(u4);
            //Random random = new Random();
            //String name = String.valueOf(random.nextLong());
            //users.get(1).setName1(name);
            //adapter.add(u4);
            //adapter.notifyDataSetInvalidated();
        }
    };


    @Override
    protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
        //Log.i("msg",this.items[position]);

    }




}
