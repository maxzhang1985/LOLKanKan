package com.maxzhang.lolkankan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Maxzhang8
 * Date: 13-6-26
 * Time: 下午4:28
 * To change this template use File | Settings | File Templates.
 */
public class VideoListPagination  implements IDataPagination , OnGetHtmlCallback {
    //当前页面索引
    private int currentIndex = 0;
    //当前页面集合
    private List<String> currentPageList = new ArrayList<String>();

    //取页面Html异步任务
    private AsyncGetHtmlTask asyncGetTask = null;

    private String startupPageUrl = null;


    public void setStartupPageUrl(String url)
    {
        startupPageUrl = url;
    }

    /**
     * 判断当前分页索引是否是分页组件中的第一页
     *
     * @return 是否是第一页
     */
    @Override
    public boolean isFirst() {
        return currentIndex == 0;
    }

    /**
     * 判断当前分页索引是否是分页组件中的最后一页
     *
     * @return 是否是最后一页
     */
    @Override
    public boolean isLast() {
        return currentIndex == currentPageList.size() - 1;
    }

    /**
     * 取得分页组件中的当前分页索引值
     *
     * @return 当前分页索引值
     */
    @Override
    public int getCurrentPageIndex() {
        return this.currentIndex;
    }

    /**
     * 执行分页组件下一页调用
     */
    @Override
    public void Next() {
        asyncGetTask = new AsyncGetHtmlTask();
        asyncGetTask.setOnGetHtmlCallback(this);
        if(this.isFirst())
        {


            asyncGetTask.execute(currentPageList.get(0));
        }
    }

    /**
     * 设置分页下一页回调函数。
     *
     * @param pagination 分页回调接口函数，用于自定义下一页调用定制需求。
     */
    @Override
    public void setOnPaginationNextListener(OnPaginationNextListener pagination) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void Refresh(Object refeshData) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void OnGetHtml(String html) {

    }
}
