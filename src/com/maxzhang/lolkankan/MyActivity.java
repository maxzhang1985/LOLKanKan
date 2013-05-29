package com.maxzhang.lolkankan;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.maxzhang.BindingSourceAdapter.BindingSourceAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends ListActivity {

    private BindingSourceAdapter<VideoInfo> bindingSourceAdapter = null;
    AsyncHtmlRequestTask task = new AsyncHtmlRequestTask(MyActivity.this);
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupViews();
        //first request
        task.execute("http://lol.178.com/list/video.html");
    }

    private List<VideoInfo> videos = new ArrayList<VideoInfo>();

    private void setupViews() {

        bindingSourceAdapter =  new BindingSourceAdapter<VideoInfo>(this,R.layout.listitem,videos);
        this.setListAdapter(bindingSourceAdapter);
        ListView listView = this.getListView();
        listView.setOnScrollListener(mScrollListener);
        ActionBar actionBar = getActionBar();

    }


    @Override
    protected void onDestroy() {


        bindingSourceAdapter.clearCache();

        super.onDestroy();
    }

    private int lastItem;//listview当前显示页面的最后一条数据
    private int firstItem;//listview当前显示页面的第一条数据
    AbsListView.OnScrollListener mScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    bindingSourceAdapter.setFlagBusy(true);
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    bindingSourceAdapter.setFlagBusy(false);
                    int count = bindingSourceAdapter.getCount();
                    if(lastItem >= count)
                    {
                        int pageindex =  Integer.valueOf(bindingSourceAdapter.Tag.toString());
                        List<String> pageList = task.getPageList();

                        if(pageindex < pageList.size()){
                            AsyncHtmlRequestTask newtask = new AsyncHtmlRequestTask(MyActivity.this);
                            newtask.setPageList(pageList);
                            newtask.setPageIndex(pageindex);
                            newtask.execute();
                            Toast.makeText(MyActivity.this, "正在加载数据......", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(MyActivity.this, "已经是最后一页了!", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    bindingSourceAdapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
            //bindingSourceAdapter.notifyDataSetChanged();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            lastItem = firstVisibleItem + visibleItemCount;//计算出lastitem的值
            firstItem = firstVisibleItem;//同样拿出lastitem的值
        }
    };


    @Override
    protected void onListItemClick(android.widget.ListView l, android.view.View v, int position, long id) {
        //Log.i("msg",this.items[position]);
        VideoInfo info = this.bindingSourceAdapter.getItem(position);

        try {

            //AsyncHtmlRequestTask.saveFile(html);
            //Log.v("saveFile",html);

            String newUrl = info.Url.replace("lol.178.com/", "178.v.playradio.cn/");

            FindVideoPlayTask task = new FindVideoPlayTask();
            task.execute(newUrl);

        } catch (Exception e) {
//			 TODO Auto-generated catch block
            e.printStackTrace();
        }

        Intent i = new Intent(this,VideoPlayActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        //添加菜单项
        MenuItem add=menu.add(0,0,0,"？");

        //绑定到ActionBar
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }


}
