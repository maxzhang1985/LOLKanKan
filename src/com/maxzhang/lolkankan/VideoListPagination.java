package com.maxzhang.lolkankan;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    //
    private OnPaginationNextListener onPaginationNextListener = null;
    private String startupPageUrl = null;


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
        if(currentPageList.size() > currentIndex)
        {
            asyncGetTask = new AsyncGetHtmlTask();
            asyncGetTask.setOnGetHtmlCallback(this);
            asyncGetTask.execute(currentPageList.get(currentIndex));
            currentIndex = currentIndex + 1;
        }
    }
    /**
     * 设置分页下一页回调函数。
     *
     * @param pagination 分页回调接口函数，用于自定义下一页调用定制需求。
     */
    @Override
    public void setOnPaginationNextListener(OnPaginationNextListener pagination) {
        onPaginationNextListener = pagination;
    }

    /**
     * 重新初始化分页对象
     */
    @Override
    public void Rest() {
        if(startupPageUrl == null)
            throw new UnsupportedOperationException("没有初始页面地址！");
        this.currentPageList.clear();
        this.currentPageList.add(startupPageUrl);
        this.currentIndex = 0;
    }

    /**
     * 根据新的初始对象重新初始化分页对象
     *
     * @param startupObj 初始对象，一般为与第一页相关的对象
     */
    @Override
    public void Rest(Object startupObj) {
        startupPageUrl = startupObj.toString();
        this.Rest();
    }


    /**
     * 刷分页对象，从而重新获取分页地址
     * @param refeshData 刷新数据，比如Html内容等。
     */
    @Override
    public void Refresh(Object refeshData) {

        String match = this.getPaginationMatchString();
        Log.v(match, "log");
        Pattern pattern = Pattern.compile(match);
        Matcher matcher = pattern.matcher(refeshData.toString());

        while(matcher.find())
        {
            String s= matcher.group();
            if(!this.currentPageList.contains(s))
                this.currentPageList.add(s);
        }

    }

    /**
     * 取得分页地址的正则匹配字符串
     */
    protected String getPaginationMatchString()
    {
        String ms =  "_\\d*";
        int postion = this.startupPageUrl.indexOf(".html");
        String match = this.startupPageUrl.substring(0,postion) + ms + this.startupPageUrl.substring(postion);
        return match;
    }





    @Override
    public void OnGetHtml(String html) {
        if(onPaginationNextListener != null){

            onPaginationNextListener.OnNext(html);
        }
    }

    @Override
    public void OnGetHtmlAsync(String html) {
        //刷新分页地址列表
        this.Refresh(html);
    }
}
