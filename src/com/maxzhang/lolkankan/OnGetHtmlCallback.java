package com.maxzhang.lolkankan;

/**
 * Created with IntelliJ IDEA.
 * User: Maxzhang8
 * Date: 13-6-26
 * Time: 下午4:51
 * 获取Html回调接口
 */
public interface OnGetHtmlCallback {
    /**
     * 通过获取网络页面Html内容后，回调函数（同步）
     * @param html 网络页面Html内容
     */
    public void OnGetHtml(String html);
    /**
     * 通过获取网络页面Html内容后，回调函数（异步）
     * @param html 网络页面Html内容
     */
    public void OnGetHtmlAsync(String html);
}
