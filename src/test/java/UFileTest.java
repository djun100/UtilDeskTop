import com.cy.util.UFile;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class UFileTest {
    @Test
    public void testCopyFile() {
        String ori = "D:\\android\\projects\\Dialer-p-preview-2\\app\\src\\main\\java\\com\\android\\dialer\\blockreportspam\\block_report_spam_dialog_info.proto";

        String destDir = "D:\\test1\\test2";
//        UFile.copyFileByByteBuffer(ori, destDir+File.separator+new File(ori).getName());

//        String destPath="D:\\android\\projects\\Dialer-p-preview-2\\app\\src\\main\\proto";
        copyFile(ori,destDir+File.separator+new File(ori).getName());

    }

    public static void copyFile(String pathName, String destPath){
        UFile.copyFileByByteBuffer(pathName,destPath);
    }
}
