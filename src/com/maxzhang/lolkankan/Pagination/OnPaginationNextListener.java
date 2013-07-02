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
     * 分页组件接口下一页异步处理回调函数
     * @param state 执行状态
     * @return 返回处理后的结果数据
     */
    public Object OnNextAsync(Object state);
    /**
     * 分页组件接口用于界面显示的回调函数
     * @param data 异步处理后的结果数据
     */
    public void OnDataBind(Object data);

}
