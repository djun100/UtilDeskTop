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
    private static String dealLine(String originLine, String lineFeature, List<String>regexExtract, String formatOut){
        // TODO: 2016/5/8 按正则提取有用信息
        // TODO: 2016/5/8 判断该行是否为匹配行，是→继续
        ArrayList<String> keys=new ArrayList<>();
        Matcher m = Pattern.compile(lineFeature).matcher(originLine); // 获取 matcher 对象
        if (m.find()){//该行是关键行
            for (int i = 0; i < regexExtract.size(); i++) {
                boolean isContainsGroup=false;
                if (regexExtract.get(i).contains("(")){
                    isContainsGroup=true;//该正则需要在匹配出的内容中提取出有用信息
                }
                m=Pattern.compile(regexExtract.get(i)).matcher(originLine);
                String keyInfo;
                if (m.find()){
                    if (isContainsGroup){
                        keyInfo=m.group(1);//在匹配出的内容中提取出有用信息
                    }else {
                        keyInfo=m.group();
                    }
//                    logger.info("关键信息："+keyInfo);
                    keys.add(keyInfo);
                }else {
                    throw new RuntimeException("正则有误：匹配出该行为关键行，但未在该行匹配到关键信息");
                }
            }
        }else {
            return null;
        }

        String result=String.format(formatOut, keys.toArray());
        return result;
    }

    /**处理多行数据
     * @param contentLines
     * @param lineFeature
     * @param regexExtract
     * @param formatOut
     * @return
     */
    public static List<String> dealLinesList(String contentLines, String lineFeature, List<String> regexExtract, String formatOut){
        ArrayList<String> results=new ArrayList<>();
        String[] ss = contentLines.split("\n");

        for (int i = 0; i < ss.length; i++) {
            System.out.println("当前处理行："+ss[i]);
            String temp= dealLine(ss[i],lineFeature,regexExtract,formatOut);
            if (!UtilString.isEmpty(temp)) {
                results.add(temp);
            }
        }
        return results;
    }

    /**处理多行数据
     * @param contentLines
     * @param lineFeature
     * @param regexExtract
     * @param formatOut
     * @return
     */
    public static String dealLines(String contentLines, String lineFeature, List<String> regexExtract, String formatOut){
        StringBuilder sb=new StringBuilder();
        List<String> results=dealLinesList(contentLines,lineFeature,regexExtract,formatOut);
        int size=results.size();
        if (size==0) return "";

        for (int i = 0; i < size; i++) {
            sb.append(results.get(i)).append(i==size-1?"":"\n");
        }
        return sb.toString();
    }

    /**逐行分析文件，按正则获取格式化特征值
     * @param regexes position 0:特征行正则  position 1：特征内容
     * */
    public static String dealFile(String pathName,String lineFeature,ArrayList<String> regexes, String formatOut){
        File file = new File(pathName);
        String fileContent= UtilFile.read_UTF8_FileContent(file);
        String result=dealLines(fileContent,lineFeature,regexes,formatOut);
        return result;
    }

    public static void main(String[] args){
//        testUrlExtract();
//        testAttrToStyle();

        testDealFile();
    }

    private static void testDealFile() {
        ArrayList<String> regex=new ArrayList<String>();
//        regex.add("");
        regex.add("http://[a-zA-Z0-9_/.]+.aspx");
        String formatOut="public static String URL_=\"%s\"";
        String result= dealFile("test.txt","http://",regex,formatOut);
        System.out.println(result);
    }

    private static void testUrlExtract() {
        String orignLine="资金账户管理（5）http://demo.abc.com/APP/app_fundAccountManage.aspx 备注：xxxx\n" +
                "资金管理（6）http://demo.abc.com/APP/app_fundManage.aspx 备注：yyy";
        ArrayList<String> regex=new ArrayList<String>();
//        regex.add("");
        regex.add("http://[a-zA-Z0-9_/.]+.aspx");
        String formatOut="public static String URL_=\"%s\"";
//        String formatOut="public static String URL_%s=\"%s\"";
        String result= dealLines(orignLine,"http://",regex,formatOut);
        System.out.println(result);
    }

    public static void testAttrToStyle(){
        String contents="                <TextView\n" +
                "                    android:id=\"@+id/newMemor_StartTime\"\n" +
                "                    style=\"@style/NewMemorActivity_tv_value\"\n" +
                "                    android:layout_marginLeft=\"10dp\"\n" +
                "                    android:layout_toRightOf=\"@id/tvTimeBegin\"\n" +
                "\n" +
                "                     />";

        ArrayList<String> regex=new ArrayList<String>();
        regex.add("[a-zA-Z_0-9:]+");//
        regex.add("=\"(.*)\"");

        String formatOut="<item name=\"%s\">%s</item>";

        String results= dealLines(contents,"=",regex,formatOut);

        System.out.println("结果"+results);
    }
}
