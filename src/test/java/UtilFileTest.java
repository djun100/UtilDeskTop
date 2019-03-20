import com.cy.util.UtilFile;
import org.junit.Test;

import java.io.File;

public class UtilFileTest {
    @Test
    public void testCopyFile() {
        String ori = "D:\\android\\projects\\Dialer-p-preview-2\\app\\src\\main\\java\\com\\android\\dialer\\blockreportspam\\block_report_spam_dialog_info.proto";

        String destDir = "D:\\test1\\test2";
//        UtilFile.copyFileByByteBuffer(ori, destDir+File.separator+new File(ori).getName());

//        String destPath="D:\\android\\projects\\Dialer-p-preview-2\\app\\src\\main\\proto";
        copyFile(ori,destDir+File.separator+new File(ori).getName());

    }

    public static void copyFile(String pathName, String destPath){
        UtilFile.copyFileByByteBuffer(pathName,destPath);
    }
}
