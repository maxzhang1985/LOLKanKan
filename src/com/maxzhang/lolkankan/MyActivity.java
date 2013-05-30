package com.maxzhang.lolkankan;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
        actionBar.setHomeButtonEnabled(true);

    }


    @Override
    protected void onDestroy() {


        bindingSourceAdapter.clearCache();

        super.onDestroy();
    }

    private boolean isLoading = false;
    public boolean getIsLoading()
    {
        return isLoading;
    }

    public void setIsloading(boolean loading)
    {
        isLoading = loading;
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
                    if(lastItem >= count && task.getComplete())
                    {
                        int pageindex =  Integer.valueOf(bindingSourceAdapter.Tag.toString());
                        List<String> pageList = task.getPageList();

                        if(pageindex < pageList.size()){
                            task = new AsyncHtmlRequestTask(MyActivity.this);
                            task.setPageList(pageList);
                            task.setPageIndex(pageindex);
                            task.execute();
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

        VideoInfo info = this.bindingSourceAdapter.getItem(position);

        try {
            Toast.makeText(this, "正在读取网络文件，请稍后......", Toast.LENGTH_LONG).show();
            FindVideoPlayTask task = new FindVideoPlayTask(this);
            task.execute(info.Url);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String m = "";
        switch (item.getItemId())
        {
            case R.id.mainmenu1:
                m="menu1";
                break;
            case R.id.mainmenu2:
                m="menu2";
                break;
            case android.R.id.home:
                m="home";
                break;
        }
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}
