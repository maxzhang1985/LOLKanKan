package com.maxzhang.lolkankan;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.maxzhang.BindingSourceAdapter.BindingSourceAdapter;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends ListActivity {

    private BindingSourceAdapter<VideoInfo> bindingSourceAdapter = null;
    AsyncHtmlRequestTask task = new AsyncHtmlRequestTask(MyActivity.this);
    private MenuDrawer mDrawer;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        mDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
        mDrawer.setMenuView(R.layout.mainleftmenu);
        mDrawer.setMenuSize(450);
        setupViews();
        //first request
        task.execute("http://lol.178.com/list/video.html");
        Log.v("exec","start");
    }

    private List<VideoInfo> videos = new ArrayList<VideoInfo>();

    private void setupViews() {

        bindingSourceAdapter =  new BindingSourceAdapter<VideoInfo>(this,R.layout.listitem,videos);
        this.setListAdapter(bindingSourceAdapter);
        ListView listView = this.getListView();
        listView.setOnScrollListener(mScrollListener);
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);

        //Menu List
        ListView menuList = (ListView)this.findViewById(R.id.menulist);
        menuList.setAdapter(new ArrayAdapter<String>(this, R.layout.mainleftmenuitem,getData()));


        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        emptyView.setText("This appears when the list is empty");
        emptyView.setVisibility(View.GONE);
        ((ViewGroup)listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);

    }

    private List<String> getData(){

        List<String> data = new ArrayList<String>();
        data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");
        data.add("测试数据4");

        return data;
    }

    @Override
    public void setContentView(int layoutResID) {
        // This override is only needed when using MENU_DRAG_CONTENT.
        mDrawer.setContentView(layoutResID);
        mDrawer.setBackground(getResources().getDrawable(R.drawable.backgroundr));
        onContentChanged();
    }


    @Override
    public void onBackPressed() {
        final int drawerState = mDrawer.getDrawerState();
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
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
                mDrawer.toggleMenu(true);
                break;
        }
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
        return super.onOptionsItemSelected(item);
    }
}
