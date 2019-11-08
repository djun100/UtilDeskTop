import com.cy.data.UtilList;
import com.cy.util.UtilCmd;
import com.sun.javafx.binding.StringFormatter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExtractFromDoc {
    @Test
    public void extractFromDetailDoc() throws IOException {
        // get current dir
        File file = new File(System.getProperty("user.dir"));
        // get parent dir
        String basePath = file.getAbsolutePath();
		System.out.println(basePath);

//        String wdiffPath=basePath+ File.separatorChar+"wdiff.exe";
        String wdiffPath="wdiff";
        String sourcePath=basePath+ File.separatorChar+"test2.txt";
        String templetePath=basePath+ File.separatorChar+"test2_diff.txt";
        String resultPath=basePath+ File.separatorChar+"test2_result.txt";
//        String cmd= String.format("dir");
//        String cmd= String.format(wdiffPath);
//        String cmd= String.format("wdiff");
//        String cmd= String.format("%s -23 %s %s",wdiffPath,sourcePath,templetePath);
        String cmd= String.format("%s -23 %s %s > %s",wdiffPath,sourcePath,templetePath,resultPath);
        //        String cmd="wdiff -23 /Users/cy/cy/projects/android/UtilDeskTop/test2.txt /Users/cy/cy/projects/android/UtilDeskTop/test2_diff.txt > /Users/cy/cy/projects/android/UtilDeskTop/test2_extract.txt";
//        String cmd="wdiff -23 /Users/cy/cy/projects/android/UtilDeskTop/test2.txt /Users/cy/cy/projects/android/UtilDeskTop/test2_diff.txt";
        System.out.println(cmd);
        String result= UtilCmd.exec(cmd);
        System.out.println(result);

        String[] params=result.split("======================================================================");
        List<String> list= UtilList.arrayToList(params);
        UtilList.removeEmptyAndTrim(list);
//        System.out.println(list);
        String format="/**%s*/\n" +
                "private static final String %s = \"%s\";";
        System.out.println(String.format(format,
                list.get(1),
                list.get(2).split("/")[list.get(2).split("/").length-1].toUpperCase(),
                list.get(2)));
    }
}
