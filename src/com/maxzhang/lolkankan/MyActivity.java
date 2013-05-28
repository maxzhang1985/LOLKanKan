package com.maxzhang.lolkankan;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
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
        setupViews();

    }

    private List<VideoInfo> videos = new ArrayList<VideoInfo>();

    private void setupViews() {

        bindingSourceAdapter =  new BindingSourceAdapter<VideoInfo>(this,R.layout.listitem,videos);
        this.setListAdapter(bindingSourceAdapter);

        Button btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(Hander);

        ListView listView = this.getListView();
        listView.setOnScrollListener(mScrollListener);
    }


    @Override
    protected void onDestroy() {


        bindingSourceAdapter.clearCache();

        super.onDestroy();
    }


    AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    bindingSourceAdapter.setFlagBusy(true);
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    bindingSourceAdapter.setFlagBusy(false);
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    bindingSourceAdapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
            bindingSourceAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }
    };


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
        Intent i = new Intent(this,VideoPlayActivity.class);
        startActivity(i);
    }




}
