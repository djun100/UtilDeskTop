import com.cy.util.BeanRegex;
import com.cy.util.UFile;
import com.cy.util.URegexV2;

import java.io.File;

public class URegexTest {
    private static void testDocComplex() {
        String content= UFile.read_UTF8(new File("test2.txt"));
        String regex= UFile.read_UTF8(new File("test_result_reg.txt"));

        String result=URegexV2.extractContentByRegex(
                regex, new int[]{1,2},content,"\n%s\n%s");
        System.out.println(result);
    }

    public static void testReplaceStringToRes(){
        BeanRegex beanRegex=new BeanRegex("=\"([\\u4e00-\\u9fa5|（|）]+)\"");
        beanRegex.clazz="com.cy.data.UString";
        beanRegex.method="hanziTopinyin";
//        ReplaceResult replaceResult = replaceByRegex(1,new File(("test.xml")),beanRegex,"@string/%s");
        URegexV2.ReplaceResult replaceResult = URegexV2.replaceByRegex(1,new File(("/Users/cy/cy/projects/android/Guider/guider/src/main/res/layout/tgou_order_detail_receipt_item.xml")),beanRegex,"@string/%s");
        replaceResult.print();
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < replaceResult.wordsFinal.size(); i++) {
            sb.append(String.format("   <string name=\"modulename_%s\">%s</string>",replaceResult.wordsFinal.get(i),replaceResult.wordsOri.get(i))).append("\n");
        }
        System.out.println(sb.toString());
    }

    private static void testUrlDoc() {
        String content= UFile.read_UTF8(new File("test.txt"));

        String result=URegexV2.extractContentByRegex(
                "\\s*([\\u4e00-\\u9fa5]+)\\s*\\n" +
                        "(\\s*http://[\\w/.]+.aspx)",
                new int[]{1,2},content,"\n接口名称：%s\n接口地址：%s");
        System.out.println(result);
    }

    private static void testManifest(){
        String content= UFile.read_UTF8(new File("AndroidManifest.xml"));
        String result=URegexV2.extractContentByRegex("package=\"([\\w.]+)\"",
                new int[]{1},content,"包名：%s");
        System.out.println(result);
    }

    private static void testReplace(){
        String content="calla我bdda你bbb他d";//将所有ab间的中文替换为全拼

        BeanRegex beanRegex1=new BeanRegex("a([\\u4e00-\\u9fa5]+)b");
//        beanRegex1.clazz="com.cy.data.UString";
//        beanRegex1.method="hanziTopinyin";
        beanRegex1.clazz=URegexV2.class.getName();
        beanRegex1.method="test";

        System.out.println(URegexV2.replaceByRegex(1,content,beanRegex1,null));
    }
}
