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

import java.util.*;

public class MyActivity extends ListActivity {

    private BindingSourceAdapter<VideoInfo> bindingSourceAdapter = null;
    AsyncHtmlRequestTask task = new AsyncHtmlRequestTask(MyActivity.this);
    private MenuDrawer mDrawer;
    private LinkedHashMap<String,String> menuMap = new LinkedHashMap<String, String>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        mDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
        mDrawer.setMenuView(R.layout.mainleftmenu);
        mDrawer.setMenuSize(250);
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
        menuList.setAdapter(new ArrayAdapter<String>(this, R.layout.mainleftmenuitem,getMenuData()));
        menuList.setOnItemClickListener(onMenulistitemClick);


    }

    private List<String> getMenuData(){
        menuMap.put("精彩视频专辑","http://lol.178.com/list/video.html");
        menuMap.put("解说视频专辑","http://lol.178.com/list/guofuvideo/index.html");
        menuMap.put("赛事专辑","http://lol.178.com/list/bisaishipin/index.html");
        menuMap.put("国服高手解说","http://lol.178.com/list/116743038027.html");
        menuMap.put("高端OB局解说","http://lol.178.com/list/ob.html");
        menuMap.put("7M教学大全专辑","http://lol.178.com/list/146255006330.html");
        menuMap.put("JY解说专辑","http://lol.178.com/list/jy/index.html");
        menuMap.put("JD解说专辑","http://lol.178.com/list/120419608247.html");
        menuMap.put("天天解说专辑","http://lol.178.com/list/124596016347.html");
        menuMap.put("SMZ24解说专辑","http://lol.178.com/list/119657498491.html");
        menuMap.put("戴尔解说专辑","http://lol.178.com/list/116477983166.html");
        menuMap.put("JoKer解说专辑","http://lol.178.com/list/127966158778.html");
        menuMap.put("小苍解说专辑","http://lol.178.com/list/xiaocang/index.html");
        menuMap.put("小智解说专辑","http://lol.178.com/list/142589587871.html");
        menuMap.put("小漠解说专辑","http://lol.178.com/list/xiaomo/index.html");
        menuMap.put("天天解说专辑","http://lol.178.com/list/124596016347.html");
        menuMap.put("WE专辑","http://lol.178.com/list/116743035365.html");
        menuMap.put("CLG.EU专辑","http://lol.178.com/list/133669237409.html");
        menuMap.put("M5专辑","http://lol.178.com/list/120167121601.html");
        menuMap.put("IG专辑","http://lol.178.com/list/117082659809.html");
        menuMap.put("打野专辑","http://lol.178.com/list/dayeshipin/index.html");
        menuMap.put("娱乐专辑","http://lol.178.com/list/recommend.html");

        Object[] cs = menuMap.keySet().toArray();

        ArrayList<String> menunamelist = new ArrayList<String>();
        for(Object key : cs)
        {
            menunamelist.add(key.toString());
        }
        return menunamelist;
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

    private AdapterView.OnItemClickListener onMenulistitemClick =  new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

            Object[] vas = menuMap.values().toArray();
            String url = vas[pos].toString();
            if(task.getComplete())
            {
                bindingSourceAdapter.clear();
                bindingSourceAdapter.notifyDataSetChanged();
                task = new AsyncHtmlRequestTask(MyActivity.this);
                task.execute(url);
            }

        }
    };


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

        return super.onOptionsItemSelected(item);
    }
}
