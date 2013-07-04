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
import com.maxzhang.lolkankan.Pagination.IDataPagination;
import com.maxzhang.lolkankan.Pagination.OnPaginationNextListener;
import com.maxzhang.lolkankan.Pagination.VideoListPagination;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.Position;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyActivity extends Activity implements OnPaginationNextListener {

    //视频列表绑定Adapter组件
    private BindingSourceAdapter<VideoInfo> bindingSourceAdapter = null;
    //数据分页组件
    private IDataPagination dataPagination = null;
    //滑动菜单控件
    private MenuDrawer mDrawer;
    //用于存储视频栏目菜单列表
    private LinkedHashMap<String,String> menuMap = new LinkedHashMap<String, String>();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();

        //初始化分页组件
        dataPagination = new VideoListPagination();
        dataPagination.setOnPaginationNextListener(this);
        dataPagination.Rest("http://lol.178.com/list/video.html");
        dataPagination.Next();
    }

    //初始化视图
    private void setupViews() {
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        //滑动菜单
        mDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT);
        mDrawer.setContentView(R.layout.main);
        mDrawer.setMenuView(R.layout.mainleftmenu);
        mDrawer.setMenuSize(400);

        //视频列表
        List<VideoInfo> videos = new ArrayList<VideoInfo>();
        ListView listview = (ListView)findViewById(R.id.listview);
        GridView gridview = (GridView)findViewById(R.id.gridview);
        AbsListView view = null;
        int layoutListItemID;
        if(gridview.getVisibility() == View.VISIBLE)
        {
            view = gridview;
            layoutListItemID = R.layout.gridviewitem;
        }
        else
        {
            view = listview;
            layoutListItemID = R.layout.listitem;
        }
        bindingSourceAdapter =  new BindingSourceAdapter<VideoInfo>(this,layoutListItemID,videos);

        view.setAdapter(bindingSourceAdapter);
        view.setOnScrollListener(mVideoListViewScrollListener);
        view.setOnItemClickListener(mVideoListViewItemClickListener);

        //滑动菜单 Menu List
        ListView menuList = (ListView)this.findViewById(R.id.menulist);
        menuList.setAdapter(new ArrayAdapter<String>(this, R.layout.mainleftmenuitem,getMenuData()));
        menuList.setOnItemClickListener(onMenulistitemClick);
    }

    //滑动菜单列表数据（视频专辑集合）
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


    //视频列表项单击事件
    AdapterView.OnItemClickListener mVideoListViewItemClickListener =  new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            VideoInfo info = bindingSourceAdapter.getItem(position);
            try {
                Toast.makeText(MyActivity.this, "正在读取网络文件，请稍后......", Toast.LENGTH_LONG).show();
                FindVideoPlayTask task = new FindVideoPlayTask(MyActivity.this);
                task.execute(info.Url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //滑动菜单列表项单击事件
    private AdapterView.OnItemClickListener onMenulistitemClick =  new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {

            Object[] vas = menuMap.values().toArray();
            String url = vas[pos].toString();
            bindingSourceAdapter.clear();
            bindingSourceAdapter.notifyDataSetChanged();
            dataPagination.Rest(url);
            dataPagination.Next();
            mDrawer.toggleMenu(true);
        }
    };



    private int lastItem;//l视频列表当前显示页面的最后一条数据
    //private int firstItem;//视频列表当前显示页面的第一条数据
    //视频列表滑动事件
    AbsListView.OnScrollListener mVideoListViewScrollListener = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                    bindingSourceAdapter.setFlagBusy(true);
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    bindingSourceAdapter.setFlagBusy(false);
                    int count = bindingSourceAdapter.getCount();
                    if(lastItem >= count )
                    {
                        dataPagination.Next();
                    }
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    bindingSourceAdapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            lastItem = firstVisibleItem + visibleItemCount;//计算出lastitem的值
        }
    };



//    ListActivty
//    @Override
//    public void setContentView(int layoutResID) {
//        // This override is only needed when using MENU_DRAG_CONTENT.
//        mDrawer.setContentView(layoutResID);
//        mDrawer.setBackground(getResources().getDrawable(R.drawable.backgroundr));
//        onContentChanged();
//    }


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

//    private boolean isLoading = false;
//    public boolean getIsLoading()
//    {
//        return isLoading;
//    }
//
//    public void setIsloading(boolean loading)
//    {
//        isLoading = loading;
//    }




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

    /**OnPaginationNextListener
     * 分页组件接口下一页异步处理回调函数
     * @param state 执行状态
     * @return 返回处理后的结果数据
     */
    @Override
    public Object OnNextAsync(Object state) {
        String html = state.toString();
        ArrayList<VideoInfo> videoList = new ArrayList<VideoInfo>();
        String match1 = "<dt><a href=\"(.*?)\".*title=\"(.*?)\".*background-image:[\\s]*url\\(\\'(.*?)\\'\\);\"><span>(.*)</span>[\\s]*(<strong>(.*)</strong>)?";
        Pattern pattern1 = Pattern.compile(match1);
        Matcher matcher1 = pattern1.matcher(html);
        while(matcher1.find())
        {
            VideoInfo info = new VideoInfo();
            info.setUrl(matcher1.group(1));
            info.setTitle(matcher1.group(2));
            info.setImageUrl(matcher1.group(3));
            info.setTimeSpan(matcher1.group(4));
            videoList.add(info);
        }
        return videoList;
    }

    /**OnPaginationNextListener
     * 分页组件接口用于界面显示的回调函数
     * @param data 异步处理后的结果数据
     */
    @Override
    public void OnDataBind(Object data) {
        ArrayList<VideoInfo> videoList =(ArrayList<VideoInfo>)data;
        bindingSourceAdapter.addAll(videoList);
        bindingSourceAdapter.notifyDataSetChanged();
        videoList.clear();
        data = videoList = null;

    }
}
