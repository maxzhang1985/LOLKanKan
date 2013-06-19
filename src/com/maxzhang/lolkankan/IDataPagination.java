package com.maxzhang.lolkankan;

/**
 * Created with IntelliJ IDEA.
 * User: maxzhang
 * Date: 13-6-19
 * Time: 下午9:27
 *分页接口，描述数据分页的基本元数据和协议
 */
public interface IDataPagination {

    /**
     *判断当前分页索引是否是分页组件中的第一页
     * @return 是否是第一页
     */
    public boolean isFirst();

    /**
     * 判断当前分页索引是否是分页组件中的最后一页
     * @return 是否是最后一页
     */
    public boolean isLast();

    /**
     * 取得分页组件中的当前分页索引值
     * @return 当前分页索引值
     */
    public int getCurrentPageIndex();

    /**
     * 执行分页组件下一页调用
     * @param pagination 分页回调接口函数，用于自定义下一页调用定制需求。
     */
    public void Next(OnPaginationNextListener pagination);
    public void Refresh(Object refeshData);

}
