package com.cy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cy on 2016/5/8.
 */
public class UtilStringFactory {
    /**<pre>按正则格式提取原文中的有用信息并按正则格式返回
     * eg:资金账户管理（6）http://demo.abc.com/APP/app_fundAccountManage.aspx
     * 账户相关与系统设置（13）http://demo.shbgz.com/APP/app_accountSystemSettings.aspx
     * @param originLine 原文任意行信息
     * @param regexExtract 按该正则从关键行提取关键信息
     *                     positon 0:关键行，position 1：关键信息1，position 2：关键信息2...
     * @param  formatOut 按照关键信息提取顺序，将提取的行信息按格式化输出
     *                   eg:public static String URL_%s="%s";
     *
     *
     *
     * <pre>todo 给出示例关键信息，反推正则表达式，列出相似性匹配结果
     *
     * */
    public static String processFactory(String originLine, List<String>regexExtract, String formatOut){
        // TODO: 2016/5/8 按正则提取有用信息
        // TODO: 2016/5/8 判断该行是否为匹配行，是→继续
        ArrayList<String> keys=new ArrayList<>();
        Pattern p = Pattern.compile(regexExtract.get(0));
        Matcher m = p.matcher(originLine); // 获取 matcher 对象
        if (m.find()){
            for (int i = 1; i < regexExtract.size(); i++) {
                boolean isContainsGroup=false;
                if (regexExtract.get(i).contains("(")){
                    isContainsGroup=true;
                }
                p=Pattern.compile(regexExtract.get(i));
                m=p.matcher(originLine);
                String keyInfo;
                if (m.find()){
                    if (isContainsGroup){
                        keyInfo=m.group(1);
                    }else {
                        keyInfo=m.group();
                    }
                    System.out.println("关键信息："+keyInfo);
                    keys.add(keyInfo);
                }else {
                    throw new RuntimeException("未在关键行匹配到关键信息");
                }
            }
        }else {
            return null;
        }

        String result=String.format(formatOut, keys.toArray());
        return result;
    }

    public static void main(String[] args){
//        String orignLine="资金账户管理（6）http://demo.abc.com/APP/app_fundAccountManage.aspx dd";
//        ArrayList<String> regex=new ArrayList<String>();
//        regex.add("http://");
//        regex.add("");
//        regex.add("http://[a-zA-Z0-9_/.]+.aspx");
//        String formatOut="public static String URL_=\"%s\"";
////        String formatOut="public static String URL_%s=\"%s\"";
//        String result=processFactory(orignLine,regex,formatOut);
//        System.out.println(result);
//        testAttrLineToItem();
        testAttrToStyle();
    }

    public static String testAttrLineToItem(String orignLine){

//        orignLine="android:id=\"@+id/newMemor_StartTime\"" ;
        ArrayList<String> regex=new ArrayList<String>();
        regex.add("=");
        regex.add("[a-zA-Z_0-9:]+");//
        regex.add("=\"(.*)\"");
        String formatOut="<item name=\"%s\">%s</item>";
        String result=processFactory(orignLine,regex,formatOut);
        System.out.println(result);
        return result;
    }

    public static void testAttrToStyle(){
        String attr="                <TextView\n" +
                "                    android:id=\"@+id/newMemor_StartTime\"\n" +
                "                    style=\"@style/NewMemorActivity_tv_value\"\n" +
                "                    android:layout_marginLeft=\"10dp\"\n" +
                "                    android:layout_toRightOf=\"@id/tvTimeBegin\"\n" +
                "\n" +
                "                     />";
        ArrayList<String> results=new ArrayList<>();
        String[] ss = attr.split("\n");
        for (int i = 0; i < ss.length; i++) {
            System.out.println("当前处理行："+ss[i]);
            results.add(testAttrLineToItem(ss[i]));
        }
        System.out.println("结果");
        for (String temp:results){
            System.out.println(temp);
        }
    }
}
