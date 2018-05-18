package com.jinhui.common.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，处理单个参数的json接收
 *
 * @autor wsc
 * @create 2018-04-10 9:42
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonParam {
    String value();
}
