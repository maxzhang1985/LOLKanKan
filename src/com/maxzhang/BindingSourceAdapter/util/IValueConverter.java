package com.maxzhang.BindingSourceAdapter.util;

/**
 * Created with IntelliJ IDEA.
 * User: zlhxd_000
 * Date: 5/26/13
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IValueConverter {
    Object Convert(Object value,Class<?> targetType,Object parameter);
    Object ConvertBack(Object value,Class<?> targetType,Object parameter);
}
