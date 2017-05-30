package com.cy.util;


import com.cy.data.UString;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.joor.Reflect;
import ru.lanwen.verbalregex.VerbalExpression;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cy on 2016/5/8.
 */
public class URegex {

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
        List<BeanRegex> beanRegexes=new ArrayList<>();
        for (String regex:regexExtract){
            BeanRegex beanRegex=new BeanRegex(regex);
            beanRegexes.add(beanRegex);
        }
        return dealLineComplex(originLine,lineFeature,beanRegexes,formatOut);
    }

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
    private static String dealLineComplex(String originLine, String lineFeature,
                                          List<BeanRegex> regexExtract, String formatOut){

        ArrayList<String> keys=new ArrayList<>();
        Matcher m = Pattern.compile(lineFeature).matcher(originLine); // 获取 matcher 对象
        if (m.find()){//该行是关键行
            for (int i = 0; i < regexExtract.size(); i++) {
                boolean isContainsGroup=false;
                if (regexExtract.get(i).regex.contains("(")){
                    isContainsGroup=true;//该正则需要在匹配出的内容中提取出有用信息
                }
                m=Pattern.compile(regexExtract.get(i).regex).matcher(originLine);
                String keyInfo;
                if (m.find()){
                    if (isContainsGroup){
                        keyInfo=m.group(1);//在匹配出的内容中提取出有用信息
                    }else {
                        keyInfo=m.group();
                    }
                    //若提供反射处理函数，则处理关键信息
                    if (!UString.isEmpty(regexExtract.get(i).clazz)
                            && !UString.isEmpty(regexExtract.get(i).method)) {
                        keyInfo= Reflect
                                .on(regexExtract.get(i).clazz)
                                .call(regexExtract.get(i).method,keyInfo)
                                .get();
                    }
//                    logger.info("关键信息："+keyInfo);
                    keys.add(keyInfo);
                }else {
                    System.out.println("跳过一个关键行，因未在该行匹配到关键信息:"+originLine);
                    return null;
//                    throw new RuntimeException("正则有误：匹配出该行为关键行，但未在该行匹配到关键信息");
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
            if (!UString.isEmpty(temp)) {
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
    public static List<String> dealLinesListComplex(String contentLines, String lineFeature, List<BeanRegex> regexExtract, String formatOut){
        ArrayList<String> results=new ArrayList<>();
        String[] ss = contentLines.split("\n");

        for (int i = 0; i < ss.length; i++) {
            System.out.println("当前处理行："+ss[i]);
            String temp= dealLineComplex(ss[i],lineFeature,regexExtract,formatOut);
            if (!UString.isEmpty(temp)) {
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

    /**处理多行数据
     * @param contentLines
     * @param lineFeature
     * @param regexExtract
     * @param formatOut
     * @return
     */
    public static String dealLinesComplex(String contentLines, String lineFeature, List<BeanRegex> regexExtract, String formatOut){
        StringBuilder sb=new StringBuilder();
        List<String> results=dealLinesListComplex(contentLines,lineFeature,regexExtract,formatOut);
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
        String fileContent= UFile.read_UTF8_FileContent(file);
        String result=dealLines(fileContent,lineFeature,regexes,formatOut);
        return result;
    }

    /**逐行分析文件，按正则获取格式化特征值
     * @param regexes position 0:特征行正则  position 1：特征内容
     * */
    public static String dealFileComplex(String pathName,String lineFeature,ArrayList<BeanRegex> regexes, String formatOut){
        File file = new File(pathName);
        String fileContent= UFile.read_UTF8_FileContent(file);
        String result=dealLinesComplex(fileContent,lineFeature,regexes,formatOut);
        return result;
    }

    public static void main(String[] args){
//        testUrlExtract();
//        testAttrToStyle();

//        testDealFile();
        testExtractPkgName();
    }

    private static void testDealFile() {
        ArrayList<String> regex=new ArrayList<String>();
//        regex.add("");
        regex.add("http://[a-zA-Z0-9_/.]+.aspx");
        String formatOut="public static String URL_=\"%s\"";
        String result= dealFile("test.txt","http://",regex,formatOut);
        System.out.println(result);
    }

    private static void testExtractPkgName() {
        ArrayList<String> regex=new ArrayList<String>();
//        regex.add("");
        regex.add("package=\"([0-9a-zA-Z.]+)\"");
        String formatOut="%s";
        String result= dealFile("AndroidManifest.xml","package",regex,formatOut);
        System.out.println(result);
    }

    private static void testUrlExtract() {
        String orignLine="资金账户管理（5）http://demo.abc.com/APP/app_fundAccountManage.aspx 备注：xxxx\n" +
                "资金管理（6）http://demo.abc.com/APP/app_fundManage.aspx 备注：yyy";

        ArrayList<BeanRegex> beanRegexes =new ArrayList<BeanRegex>();
        BeanRegex beanRegex1=new BeanRegex("[\\u4e00-\\u9fa5]+");
        beanRegex1.clazz="com.cy.util.URegex";
        beanRegex1.method="hanziTopinyin";
        beanRegexes.add(beanRegex1);
        BeanRegex beanRegex2=new BeanRegex("http://[a-zA-Z0-9_/.]+.aspx");
        beanRegexes.add(beanRegex2);

        String formatOut="public static String URL_%s=\"%s\"";
        String result= dealLinesComplex(orignLine,"http://",beanRegexes,formatOut);
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

    public static boolean isMatchDeclare(String content){
        String declare="private Button  mBtn_Login_;";
        VerbalExpression testRegex=VerbalExpression.regex()
                .startOfLine()
                .then("private")
                .space()
                .oneOrMore()
                .range("A","Z")
                .count(1)

                .capt()
                .range("0","9","a","z","A","Z")
                .maybe("_")
                .endCapt()
                .zeroOrMore()

                .space()
                .oneOrMore()
                .then("m")

                .capt()
                .range("0","9","a","z","A","Z","_")
                .maybe("_")
                .endCapt()

                .zeroOrMore()
                .space()
                .zeroOrMore()
                .then(";")
                .build();

        return testRegex.testExact(content);
    }
}
