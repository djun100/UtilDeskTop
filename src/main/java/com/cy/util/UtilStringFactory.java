package com.cy.util;


import com.cy.DataStructure.UtilString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cy on 2016/5/8.
 */
public class UtilStringFactory {

    /**按正则格式提取原文中的有用信息并按正则格式返回
     * @param originLine 当前分析行内容
     * @param lineFeature  特征行正则
     * @param regexExtract 与输出%s数量对应的若干特征内容正则
     * @param formatOut 特征内容格式化输出
     *                  按照关键信息提取顺序，将提取的行信息按格式化输出
     *                  eg:public static String URL_%s="%s";
     *
     * <pre>todo 给出示例关键信息，反推正则表达式，列出相似性匹配结果
     * @return*/
    public static String processFactory(String originLine,String lineFeature, List<String>regexExtract, String formatOut){
        // TODO: 2016/5/8 按正则提取有用信息
        // TODO: 2016/5/8 判断该行是否为匹配行，是→继续
        ArrayList<String> keys=new ArrayList<>();
        Pattern p = Pattern.compile(lineFeature);
        Matcher m = p.matcher(originLine); // 获取 matcher 对象
        if (m.find()){
            for (int i = 0; i < regexExtract.size(); i++) {
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
//                    logger.info("关键信息："+keyInfo);
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

    /**逐行分析文件，按正则获取格式化特征值
     * @param regexes position 0:特征行正则  position 1：特征内容
     * */
    public static String extractFeatureFromFile(String pathName,String lineFeature,ArrayList<String> regexes){

        File file = new File(pathName);
        BufferedReader reader = null;
        int lengthHasRead=0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {

                String temp=processFactory(tempString,lineFeature,regexes,"%s");
                if (temp!=null){
                    // 显示行号
//                    logger.info("在line " + line + "提取出特征结果:" + temp+" 于"+pathName);
                    return temp;
                }

                lengthHasRead+=tempString.length();


                line++;
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return null;
    }

    public static void main(String[] args){
        urlExtract();
//        testAttrToStyle();
    }

    private static void urlExtract() {
        String orignLine="资金账户管理（6）http://demo.abc.com/APP/app_fundAccountManage.aspx 备注：xxxx";
        ArrayList<String> regex=new ArrayList<String>();
//        regex.add("");
        regex.add("http://[a-zA-Z0-9_/.]+.aspx");
        String formatOut="public static String URL_=\"%s\"";
//        String formatOut="public static String URL_%s=\"%s\"";
        String result=processFactory(orignLine,"http://",regex,formatOut);
        System.out.println(result);
    }

    public static String testAttrLineToItem(String orignLine){

//        orignLine="android:id=\"@+id/newMemor_StartTime\"" ;
        ArrayList<String> regex=new ArrayList<String>();
        regex.add("[a-zA-Z_0-9:]+");//
        regex.add("=\"(.*)\"");
        String formatOut="<item name=\"%s\">%s</item>";
        String result=processFactory(orignLine,"=",regex,formatOut);
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
            String temp=testAttrLineToItem(ss[i]);
            if (!UtilString.isEmpty(temp)) {
                results.add(temp);
            }
        }

        System.out.println("结果");
        for (String temp:results){
            System.out.println(temp);
        }
    }
}
