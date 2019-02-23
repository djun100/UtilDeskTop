package com.cy.util;

import java.util.Properties;

/**
 * Created by cy on 2017/6/16.
 */
public class UtilEnv {

    private static String os = System.getProperty("os.name").toLowerCase();

    public static String getOsNameVer(){
        return System.getProperty("os.name")+System.getProperty("os.version");
    }

    public static boolean isWindows(){
        return os.indexOf("windows")>=0;
    }

    public static boolean isMac(){
        return os.indexOf("mac")>=0;
    }

    public static boolean isLinux(){
        return os.indexOf("linux")>=0;
    }

    public static Properties getSystemProperties(){
        Properties props=System.getProperties(); //系统属性
        return props;
    }

    /**用户的主目录、用户的账户名称
     * @return C:\Users\xuechao.wang
     */
    public static String getUserHome(){
        String userHome=getSystemProperties().getProperty("user.name");
        return userHome;
    }

    /**用户的当前工作目录，等同于执行
     * new File("").getAbsolutePath();
     * @return C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2018.3.4\bin
     */
    public static String getWorkDir(){
        String userHome=getSystemProperties().getProperty("user.dir");
        return userHome;
    }
}
