package com.maxzhang.lolkankan;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import com.maxzhang.BindingSourceAdapter.BindingSourceAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends ListActivity {

    private BindingSourceAdapter<VideoInfo> bindingSourceAdapter = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        List<VideoInfo> videos = new ArrayList<VideoInfo>();
        bindingSourceAdapter =  new BindingSourceAdapter<VideoInfo>(this,R.layout.listitem,videos);
        this.setListAdapter(bindingSourceAdapter);

        Button btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(Hander);
    }


   private View.OnClickListener Hander = new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           AsyncHtmlRequestTask task = new AsyncHtmlRequestTask(MyActivity.this);
           task.execute("http://lol.178.com/list/video.html");
       }
   };




    @Override
    protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
        //Log.i("msg",this.items[position]);

    }




}
