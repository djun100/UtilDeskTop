package com.cy.util;

import com.cy.data.UtilString;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Stack;

/**
 * @author djun100
 */
public class UtilFile {

    public static void main(String[] args) {
        //test 遍历文件夹
        String path="/Users/cy/cy";
        ArrayList<File> files=getFiles(new File(path),"");
        for (File f:files){
            System.out.println(f.getAbsolutePath());
        }
    }

    public static String read_UTF8(File file) {
        return read(file,"UTF-8");
    }

    public static String read(File file,String charset){
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
    } catch (Exception e) {
        e.printStackTrace();
    }
    return str;
}


    public static boolean write_UTF8(File file, String content) {
        return write(file,content,"UTF-8",false);
    }

    public static boolean write_UTF8(File file, String content,boolean append) {
        return write(file,content,"UTF-8",append);
    }

    public static boolean write(File file,String content,String charset,boolean append){
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file,append), charset);
            out.write(content.toCharArray());
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) { // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
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
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        boolean flag;
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 覆盖方式复制一个目录及其子目录、文件到另外一个目录
     * @param src
     * @param dest
     * @throws IOException
     * PS： apache commons-io包，FileUtils有相关的方法，IOUtils一般是拷贝文件。

    删除目录结构                    FileUtils.deleteDirectory(dest);
    递归复制目录及文件        FileUtils.copyDirectory(src, dest);
     */
    public static void copyFolder(File src, File dest) throws IOException {

        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            String files[] = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // 递归复制
                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }
    /**
     * 智能添加路径开头分隔符
     *
     * @param path
     */
    public static String addStartPathSeparator(String path) {
        String pathNew = path;
        if (!path.startsWith("/")) {
            // path="/"+path;不能在字符串自身进行修改操作，String是不可变的
            pathNew = "/" + path;
        }
        return pathNew;
    }

    /**
     * 智能添加路径末尾分隔符
     *
     * @param path
     */
    public static String addEndPathSeparator(String path) {
        String pathNew = path;
        if (!path.endsWith("/")) {
            pathNew = path + "/";
        }
        return pathNew;
    }

    /**
     * 写在/mnt/sdcard/目录下面的文件
     *
     * @param fileName
     * @param content
     */
    public void writeFileSdcard(String fileName, String content) {
        try {
            // FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);
            FileOutputStream fout = new FileOutputStream(fileName);
            byte[] bytes = content.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读在/mnt/sdcard/目录下面的文件
     *
     * @param fileName
     * @return
     */
    public static String readFileSdcard(String fileName) {

        String res = "";

        try {

            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte[] buffer = new byte[length];

            fin.read(buffer);

            res = new String(buffer, "UTF-8");

            fin.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return res;

    }

    /**
     * 根据地址url获取文件名<br>
     * String url="http://192.168.1.15:8080/tvportal/images/c11.jpg";<br>
     * File file=new File(url);<br>
     * System.out.println(file.getName());<br>
     *
     * @param url
     * @return
     */
    public static String getFileName(String url) {
        File file = new File(url);
        return file.getName();
    }

    public static final String ENCODER_UTF_8="UTF-8";
    public static final String ENCODER_UTF_8_NO_BOM="UTF-8-NO-BOM";
    public static final String ENCODER_UTF_8_BOM="UTF-8-BOM";
    public static final String ENCODER_GBK="GBK";
    public static final String ENCODER_UNICODE="Unicode";
    public static final String ENCODER_UNF_16BE="UTF-16BE";

    /**
     * @Comments ：获取文件编码格式
     * @param fileName
     * @return
     */
    private static String getCharset(File fileName) {
        String code = null;
        BufferedInputStream bin;
        int bom = 0;
        String str = " ";
        String str2 = "";
        try {
            bin = new BufferedInputStream(new FileInputStream(fileName));
            bom = (bin.read() << 8) + bin.read();

            // 获取两个字节内容，如果文件无BOM信息，则通过判断字的字节长度区分编码格式
            byte bs[] = new byte[10];
            while(str.matches("\\s+\\w*")){
                bin.read(bs);
                str = new String(bs, "UTF-8");
            }
            str2 = new String(bs, "GBK");
        } catch (Exception e) {
        }

        // 有BOM
        switch (bom) {
            case 0xefbb:
                code = ENCODER_UTF_8_BOM;
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            case 0x5c75:
                code = "ANSI|ASCII" ;
                break ;
            default:
                // 无BOM
                if (str.length() <=str2.length()) {
                    code = ENCODER_UTF_8_NO_BOM;
                } else {
                    code = "GBK";
                }
        }


        return code;
    }

    /**
     * 获取文件后缀
     */
    public static String getFileExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }

    public static String getUrlFileName(String url) {
        int slashIndex = url.lastIndexOf('/');
        int dotIndex = url.lastIndexOf('.');
        String filenameWithoutExtension;
        if (dotIndex == -1) {
            filenameWithoutExtension = url.substring(slashIndex + 1);
        } else {
            filenameWithoutExtension = url.substring(slashIndex + 1, dotIndex);
        }
        return filenameWithoutExtension;
    }

    public static String getUrlExtension(String url) {
        if (!UtilString.isEmpty(url)) {
            int i = url.lastIndexOf('.');
            if (i > 0 && i < url.length() - 1) {
                return url.substring(i + 1).toLowerCase();
            }
        }
        return "";
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }
    /**@param format %.20f 小数点后20位*/
    public static String showFileSize(long size,String format) {
        if (UtilString.isEmpty(format)) format="%.1f";
        final double KB = 1024.0;
        final double MB = KB * KB;
        final double GB = KB * KB * KB;
        String fileSize;
        if (size < KB)
            fileSize = size + "B";
        else if (size < MB)
            fileSize = String.format(format, size / KB) + "K";
        else if (size < GB)
            fileSize = String.format(format, size / MB) + "M";
        else
            fileSize = String.format(format, size / GB) + "G";

        return fileSize;
    }

    /**
     * 判断文件是否存在，并且存在的不是一个目录
     *
     * @param path
     * @return
     */
    public static boolean ifExist(String path) {

        return (new File(path).exists()) && !(new File(path).isDirectory());
    }

    /**
     * 使用Java.nio ByteBuffer字节将一个文件输出至另一文件
     * tips
     * 拷贝文件夹的时候不要把目标文件夹设在原文件夹的子文件夹内，会造成复制文件为空内容
     * */
    public static void copyFileByByteBuffer(String sourcePathName, String desPathName, int bufferSize) {
        FileInputStream in = null;
        FileOutputStream out = null;
        new File(new File(desPathName).getParent()).mkdirs();
        try {
            // 获取源文件和目标文件的输入输出流
            in = new FileInputStream(sourcePathName);
            out = new FileOutputStream(desPathName);
            // 获取输入输出通道
            FileChannel fcIn = in.getChannel();
            FileChannel fcOut = out.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            while (true) {
                // clear方法重设缓冲区，使它可以接受读入的数据
                buffer.clear();
                // 从输入通道中将数据读到缓冲区
                int r = fcIn.read(buffer);
                if (r == -1) {
                    break;
                }
                // flip方法让缓冲区可以将新读入的数据写入另一个通道
                buffer.flip();
                fcOut.write(buffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null && out != null) {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 请确保目标文件夹存在，不存在请先新建
     * 使用Java.nio ByteBuffer字节将一个文件输出至另一文件<br>
     * bufferSize=1024;<br>
     * public static void main(String args[]) {<br>
     * long time1 = getTime() ;<br>
     * // readFileByByte(FILE_PATH); //2338,2286<br>
     * // readFileByCharacter(FILE_PATH);//160,162,158<br>
     * // readFileByLine(FILE_PATH); //46,51,57<br>
     * // readFileByBybeBuffer(FILE_PATH);//19,18,17<br>
     * // readFileByBybeBuffer(FILE_PATH);//2048: 11,13<br>
     * // readFileByBybeBuffer(FILE_PATH);//1024*100 100k,711k: 6,6<br>
     * // readFileByBybeBuffer(FILE_PATH);//1024*100 100k,1422k: 7<br>
     * // readFileByBybeBuffer(FILE_PATH);//1024*100 100k,9951k: 49,48<br>
     * // readFileByBybeBuffer(FILE_PATH);//1024*1000 1M,711k: 7,7<br>
     * // readFileByBybeBuffer(FILE_PATH);//1024*1000 1M,1422k: 7,8<br>
     * // readFileByBybeBuffer(FILE_PATH);//1024*1000 1M,9951k: 48,49<br>
     * // readFileByBybeBuffer(FILE_PATH);//1024*10000 10M,711k: 21,13,17<br>
     * // readFileByBybeBuffer(FILE_PATH);//1024*10000 10M,1422k: 16,17,14,15<br>
     * // readFileByBybeBuffer(FILE_PATH);//1024*10000 10M,9951k:64,60<br>
     * <p/>
     * long time2 = getTime() ;<br>
     * System.out.println(time2-time1); }
     *      * tips
     *      * 拷贝文件夹的时候不要把目标文件夹设在原文件夹的子文件夹内，会造成复制文件为空内容
     */
    public static void copyFileByByteBuffer(String sourcePathName, String desPathName) {
        FileInputStream in = null;
        FileOutputStream out = null;
        int bufferSize = 1024;
        // TODO: 2015/11/27 如a/b/file file是文件，b文件夹不存在，先新建b.makedirs()
        new File(new File(desPathName).getParent()).mkdirs();
        try {
            // 获取源文件和目标文件的输入输出流
            in = new FileInputStream(sourcePathName);
            out = new FileOutputStream(desPathName);
            // 获取输入输出通道
            FileChannel fcIn = in.getChannel();
            FileChannel fcOut = out.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
            while (true) {
                // clear方法重设缓冲区，使它可以接受读入的数据
                buffer.clear();
                // 从输入通道中将数据读到缓冲区
                int r = fcIn.read(buffer);
                if (r == -1) {
                    break;
                }
                // flip方法让缓冲区可以将新读入的数据写入另一个通道
                buffer.flip();
                fcOut.write(buffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null && out != null) {
                try {
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**移动文件*/
    public static boolean move(File srcFile, String destPath)
    {
        // Destination directory
        File dir = new File(destPath);

        // Move file to new directory
        boolean success = srcFile.renameTo(new File(dir, srcFile.getName()));

        return success;
    }
    /**移动文件*/
    public static boolean move(String srcFile, String destPath)
    {
        // File (or directory) to be moved
        File file = new File(srcFile);

        // Destination directory
        File dir = new File(destPath);

        // Move file to new directory
        boolean success = file.renameTo(new File(dir, file.getName()));

        return success;
    }

    /**     * tips
     * 拷贝文件夹的时候不要把目标文件夹设在原文件夹的子文件夹内，会造成复制文件为空内容
     * @param oriPathName
     * @param destPathName
     */
    public static void copy(String oriPathName, String destPathName) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oriPathName);
            if (oldfile.exists()) {
                new File(new File(destPathName).getParent()).mkdirs();
                InputStream inStream = new FileInputStream(oriPathName);
                FileOutputStream fs = new FileOutputStream(destPathName);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("error  ");
            e.printStackTrace();
        }
    }

    /**     * tips
     * 拷贝文件夹的时候不要把目标文件夹设在原文件夹的子文件夹内，会造成复制文件为空内容
     * @param orifile
     * @param newPathName
     */
    public static void copy(File orifile, String newPathName) {
        try {
            int bytesum = 0;
            int byteread = 0;
            //File     oldfile     =     new     File(oldPath);
            if (orifile.exists()) {
                new File(new File(newPathName).getParent()).mkdirs();
                InputStream inStream = new FileInputStream(orifile);
                FileOutputStream fs = new FileOutputStream(newPathName);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("error  ");
            e.printStackTrace();
        }
    }

    /**
     * 获取文件夹大小
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file) {

        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    /**非递归遍历文件夹，有的用linkedlist，在此用stack，由二叉树非递归遍历衍生而来
     * @param file  文件夹
     * @param suffix    后缀名
     */
    public static ArrayList<File> getFiles(File file,String suffix){
        if (UtilString.notEmpty(suffix)) {
            if (!suffix.startsWith(".")){
                suffix="."+suffix;
            }
        }
        ArrayList<File> filesRlt=new ArrayList<>();
        Stack<File> stack=new Stack<>();
        stack.push(file);
        File curr;
        while (stack.size()>0){
            curr=stack.pop();
            if (curr.isFile()){
                if (curr.getName().endsWith(suffix)){
                    filesRlt.add(curr);
//                    System.out.println(curr.getAbsolutePath());
                }
            }else {
                File[] files=curr.listFiles();
                if (files!=null){
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isFile()){
                            if (files[i].getName().endsWith(suffix)){
                                filesRlt.add(files[i]);
//                                System.out.println(files[i].getAbsolutePath());
                            }
                        }else {
                            stack.push(files[i]);
                        }
                    }
                }
            }

        }
        return filesRlt;
    }

    public static String readResourcesFileContent(String resourcePathName) {
        //resource路径必须/开头
        resourcePathName = resourcePathName.startsWith("/") ? resourcePathName : "/" + resourcePathName;
        StringBuilder stringBuilder = new StringBuilder();
        InputStream is = UtilFile.class.getResourceAsStream(resourcePathName);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = "";
        try {
            while ((s = br.readLine()) != null) {
                stringBuilder.append(s).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = stringBuilder.toString();
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
