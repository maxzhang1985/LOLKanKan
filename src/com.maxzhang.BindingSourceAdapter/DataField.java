package com.maxzhang.BindingSourceAdapter;

import com.maxzhang.BindingSourceAdapter.util.IValueConverter;
import com.maxzhang.BindingSourceAdapter.util.ViewDefined;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataField {
    ViewDefined View() default ViewDefined.Text;
    String Converter() default "";
}


