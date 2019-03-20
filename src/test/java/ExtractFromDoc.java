import com.cy.data.UtilList;
import com.cy.util.UtilCmd;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ExtractFromDoc {
    @Test
    public void extractFromDetailDoc() throws IOException {
//        String cmd="wdiff -23 /Users/cy/cy/projects/android/UtilDeskTop/test2.txt /Users/cy/cy/projects/android/UtilDeskTop/test2_diff.txt > /Users/cy/cy/projects/android/UtilDeskTop/test2_extract.txt";
        String cmd="wdiff -23 /Users/cy/cy/projects/android/UtilDeskTop/test2.txt /Users/cy/cy/projects/android/UtilDeskTop/test2_diff.txt";
        String result= UtilCmd.exec(cmd);
//        System.out.println(result);

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
