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
     */
    public void Next();

    /**
     * 设置分页下一页回调函数。
     * @param pagination 分页回调接口函数，用于自定义下一页调用定制需求。
     */
    public void setOnPaginationNextListener(OnPaginationNextListener pagination);

    /**
     * 刷分页对象，从而重新获取分页地址
     * @param refeshData 刷新数据，比如Html内容等。
     */
    public void Refresh(Object refeshData);

    /**
     * 重新初始化分页对象
     */
    public void Rest();

    /**
     * 根据新的初始对象重新初始化分页对象
     * @param startupObj 初始对象，一般为与第一页相关的对象
     */
    public void Rest(Object startupObj);

}
