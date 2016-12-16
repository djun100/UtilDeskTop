package com.cy.util;

/**
 * Created by Administrator on 2016/12/16.
 */
public class BeanRegex {

    public BeanRegex(String regex) {
        this.regex = regex;
    }

    /**
     * 正则
     */
    public String regex;
    /**
     * 处理关键信息的反射函数所在的类
     */
    public String clazz;
    /**
     * 处理关键信息的反射函数，所定义的函数返回值为关键信息处理结果，参数必须为一个，为处理前的关键信息
     */
    public String method;
}
