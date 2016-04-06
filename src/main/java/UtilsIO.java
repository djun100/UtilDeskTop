import java.io.*;
import java.util.List;


public class UtilsIO {
	public static String read_UTF8_FileContent(File file){
		String str = null;
        try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
			StringBuffer sbread = new StringBuffer();
			while (isr.ready()) {
				sbread.append((char) isr.read());
			}
			isr.close();
			// 从构造器中生成字符串，并替换搜索文本
			str = sbread.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return str;
	}
	public static void write_UTF8_FileContent(File file,String content){
        try {
			OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			out.write(content.toCharArray());
			out.flush();
			out.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
    /**
     * 删除目录（文件夹）以及目录下的文件
     * @param   sPath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
    	boolean flag;
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
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
    }
    public static <E> void printList(List<E> objects){
        for (E object:objects) {
            System.out.println(object);
        }
    }
    public static String pathNameToLinux(String pathName){
        return pathName.replaceAll("\\\\","/");
    }
    public static String deleteLastLineIfNeeded(String lines){
        StringBuilder stringBuilder=new StringBuilder();
        String[] s=lines.split("\\n");
        //已经去掉过override行，代表客户端本次启动后不是第一次处理该activity文件
        if (!s[s.length-1].contains("@Override")){
            return lines;
        }
        for (int i=0;i<s.length-1;i++){
            stringBuilder.append(s[i]).append("\n");
        }
        String result=stringBuilder.toString();
        return result;
    }
}
