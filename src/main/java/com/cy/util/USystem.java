package com.cy.util;

/**
 * Created by cy on 2017/6/16.
 */
public class USystem {
    public static String getOsNameVer(){
        return System.getProperty("os.name")+System.getProperty("os.version");
    }
}
