package com.maxzhang.lolkankan.Pagination;

/**
 * Created with IntelliJ IDEA.
 * User: maxzhang
 * Date: 13-6-19
 * Time: 下午10:24
 *分页组件下一页事件监听器
 */
public interface OnPaginationNextListener {
    /**
     * 分页组件接口回调函数
     * @param state 执行状态
     */
    public void OnNext(Object state);
}
