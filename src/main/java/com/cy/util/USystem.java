package com.cy.util;

/**
 * Created by cy on 2017/6/16.
 */
public class USystem {

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
}
