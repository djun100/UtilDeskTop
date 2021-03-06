package com.cy.util;


import com.cy.data.UtilArray;
import com.cy.data.UtilList;
import com.cy.data.UtilString;
import org.joor.Reflect;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cy on 2017/8/29.
 */
public class UtilRegexV2 {

    public static ArrayList<ArrayList<String>> extractListByRegex(String regex,int[] groups,String inputContent){
        ArrayList<String> contents=new ArrayList<>();
        ArrayList<ArrayList<String>> results=new ArrayList<>();
        if (UtilString.isEmpty(regex)){
            results.add(contents);
            return results;
        }

        Matcher matcher=Pattern.compile(regex).matcher(inputContent);
        while (matcher.find()){
            ArrayList<String> oneFindResults=new ArrayList<>();

            if (UtilArray.isEmpty(groups)){
                oneFindResults.add(matcher.group());
            }else {

                for (int i = 0; i < groups.length; i++) {
                    String temp=matcher.group(groups[i]);
                    oneFindResults.add(temp);
                }
            }

            results.add(oneFindResults);
        }
        return results;
    }


    public static ArrayList<String> extractLinesByRegex(String regex,int[] groups,String inputContent,String outputFormat){

        ArrayList<ArrayList<String>> listList=extractListByRegex(regex,groups,inputContent);
        ArrayList<String> resultLines=new ArrayList<>();

        for (int i = 0; i < listList.size(); i++) {
            ArrayList<String> itemList=listList.get(i);

            String[] params=new String[itemList.size()];
            for (int j = 0; j < itemList.size(); j++) {
                 params[j]=itemList.get(j);
            }
            String line=String.format(outputFormat,params);
            resultLines.add(line);
        }
        return resultLines;
    }

    public static String extractContentByRegex(String regex,int[] groups,String inputContent,String outputFormat){
        ArrayList<String> lines=extractLinesByRegex(regex,groups,inputContent,outputFormat);
        StringBuilder resultBuilder=new StringBuilder();
        if (UtilList.notEmpty(lines)) {
            resultBuilder.append(lines.get(0));
        }
        for (int i = 1; i < lines.size(); i++) {
            resultBuilder/*.append("\n")*/.append(lines.get(i));
        }
        return resultBuilder.toString();
    }

    /**eg:        String content="calla我bdda你bbb他d";//将所有ab间的中文替换为全拼
     * @param group
     * @param inputContent
     * @param beanRegex regex class method  要求：单参数且返回string的静态函数
     * @return
     */
    public static ReplaceResult replaceByRegex(int group,String inputContent,BeanRegex beanRegex,String formatOut){

        ReplaceResult replaceResult=new ReplaceResult();
        replaceResult.content=inputContent;

        if (beanRegex==null || UtilString.isEmpty(beanRegex.regex)){
            return replaceResult;
        }
        if (UtilString.isEmpty(formatOut)) formatOut="%s";

        //replaceAll a我b → awob   a你b → anib
        int[] groups;
        if (group!=0) {
            groups=new int[]{0,group};
        }else {
            groups=new int[]{0};
        }

        //a我b   a你b
        ArrayList<ArrayList<String>> extracted=extractListByRegex(beanRegex.regex,groups,inputContent);
        ArrayList<String> wordsOri=new ArrayList<>();

        for (int i = 0; i < extracted.size(); i++) {
            String extract0=extracted.get(i).get(0);
            String extract1= null;
            if (extracted.get(i).size()>1) {
                extract1 = extracted.get(i).get(1);
            }else {
                extract1=extract0;
            }
            wordsOri.add(extract1);
            //deal list:a我b 我
            String deal = "";
            if (UtilString.notEmpty(beanRegex.clazz) && UtilString.notEmpty(beanRegex.method)) {
                deal= Reflect
                        .on(beanRegex.clazz)
                        .call(beanRegex.method,extract1)
                        .get();
            }
            if (group!=0){
                replaceResult.wordsFinal.add(deal);
            }
            deal=String.format(formatOut,deal);
            String after=extract0.replace(extract1,deal);
            if (group==0) {
                replaceResult.wordsFinal.add(after);
            }
            inputContent = inputContent.replaceAll(extract0, after);

        }
        replaceResult.content=inputContent;
        replaceResult.wordsOri=wordsOri;
        return replaceResult;
    }

    public static ReplaceResult replaceByRegex(int group,File file,BeanRegex beanRegex,String formatOut){
        String content = UtilFile.read_UTF8(file);
        ReplaceResult result = replaceByRegex(group,content,beanRegex,formatOut);
        UtilFile.write_UTF8(file,result.content);
        return result;
    }

    public static class ReplaceResult{
        /*** 替换后的全文内容*/
        public String content;
        /*** 被替换后*/
        public ArrayList<String> wordsFinal = new ArrayList<>();
        /*** 替换前*/
        public ArrayList<String> wordsOri = new ArrayList<>();

        public void print() {
            System.out.println(wordsFinal);
            System.out.println(wordsOri);
        }
    }

}
