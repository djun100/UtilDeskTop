package com.cy.util;


import com.cy.data.UtilCollection;
import com.cy.data.UtilString;
import org.joor.Reflect;
import ru.lanwen.verbalregex.VerbalExpression;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cy on 2016/5/8.
 */
public class UtilRegex {

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
                        keyInfo=m.group(0);
                    }
                    //若提供反射处理函数，则处理关键信息
                    if (!UtilString.isEmpty(regexExtract.get(i).clazz)
                            && !UtilString.isEmpty(regexExtract.get(i).method)) {
                        keyInfo= Reflect
                                .on(regexExtract.get(i).clazz)
                                .call(regexExtract.get(i).method,keyInfo)
                                .get();
                    }
//                    logger.info("关键信息："+keyInfo);
                    keys.add(keyInfo);
                }else {
//                    System.out.println("跳过一个关键行，因未在该行匹配到关键信息:"+originLine);
                    return null;
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
    public static List<String> dealLinesListComplex(String contentLines, String lineFeature, List<BeanRegex> regexExtract, String formatOut){
        ArrayList<String> results=new ArrayList<>();
        String[] ss = contentLines.split("\n");

        for (int i = 0; i < ss.length; i++) {
            String temp= dealLineComplex(ss[i],lineFeature,regexExtract,formatOut);
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
        String fileContent= UtilFile.read_UTF8(file);
        String result=dealLines(fileContent,lineFeature,regexes,formatOut);
        return result;
    }

    /**逐行分析文件，按正则获取格式化特征值
     * @param regexes position 0:特征行正则  position 1：特征内容
     * */
    public static String dealFileComplex(String pathName,String lineFeature,ArrayList<BeanRegex> regexes, String formatOut){
        File file = new File(pathName);
        String fileContent= UtilFile.read_UTF8(file);
        String result=dealLinesComplex(fileContent,lineFeature,regexes,formatOut);
        return result;
    }



    private static void testDealFile() {
        ArrayList<String> regex=new ArrayList<String>();
//        regex.add("");
        regex.add("\\s*[\\u4e00-\\u9fa5\\w]*\\s*\\n\\s*http://[a-zA-Z0-9_/.]+.aspx\\s*");
        String formatOut="public static String URL_=\"%s\"";
        String result= dealFile("test.txt","\\s*[\\u4e00-\\u9fa5\\w]*\\s*",regex,formatOut);
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
        beanRegex1.clazz="com.cy.data.UtilPinyin";
        beanRegex1.method="hanziTopinyin";
        beanRegexes.add(beanRegex1);
        BeanRegex beanRegex2=new BeanRegex("http://[a-zA-Z0-9_/.]+.aspx");
        beanRegexes.add(beanRegex2);

        String formatOut="public static String URL_%s=\n\"%s\"";
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

    public static void testMultiLine(){
        String multiLine="abc\n" +
                "d\n" +
                "e\n" +
                "fg";
        String content="a\n" +
                "http://demo.abc.com/APP/app_fundAccountManage.aspx\n" +
                "//\n" +
                "b\n" +
                "http://demo.abc.com/APP/app_fundManage.aspx\n";

        Matcher matcher=Pattern.compile("\\w+\\n\\w+").matcher(multiLine);
//        Matcher matcher=Pattern.compile("\\w*\\nhttp://[a-zA-Z0-9_/.]+.aspx").matcher(content);

        while (matcher.find()){
            System.out.println(matcher.group()+"\n");
        }

//        System.out.println(matcher.matches());
//        System.out.println(matcher.group());

    }


    public static void main(String[] args){
//        testUrlExtract();
//        testAttrToStyle();
//            testMultiLine();
//        testDealFile();
//        testExtractPkgName();
        regexTest();
    }

    /**正则批量提取
     key:bird value:鸟
     名词
     key:dolala value:
     人名
     杜拉拉
     key:dog value:狗
     名词
     */
    public static void regexTest() {
        String filetext =
                "1@bird@鸟\n" +
                "名词\n" +
                "2@dolala@\n" +
                "人名\n" +
                "杜拉拉\n" +
                "390@dog@狗\n" +
                "名词\n" +
                "0@";
        Map<String,String> wordsAndTrans=new LinkedHashMap<>();
        List<String> words=new ArrayList<>();
        List<String> trans=new ArrayList<>();
        Pattern p = Pattern.compile("\\d+@(.+)@");
        Matcher m = p.matcher(filetext);
        while (m.find()) {
            String word=m.group(1);
            System.out.println("find:"+word);
            words.add(word);
        }

        //使用？问号指定非贪婪匹配，尽可能少重复即匹配
        Pattern p2 = Pattern.compile("\\D@((.|\\n)+?)\\d+@");
        Matcher m2 = p2.matcher(filetext);
        while (m2.find()) {
            String tran=m2.group(1);
            System.out.println("find:"+tran);
            trans.add(tran);
        }

        for (int i = 0; i < words.size(); i++) {
            wordsAndTrans.put(words.get(i),trans.get(i));
        }

        System.out.println(UtilCollection.toString(wordsAndTrans));
    }
}
