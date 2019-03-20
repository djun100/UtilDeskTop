package com.cy.util;


import com.cy.util.KeyListener.ShortcutManager;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by cy on 2017/6/16.
 */
public class UtilKeyBoard {

    /**按键映射，执行前延时100ms
     * @param keyPresseds
     * @param keyMappeds
     */
    public static void mapKey(int[] keyPresseds, final int [] keyMappeds){
        mapKey(keyPresseds, keyMappeds,100);
    }

    /**按键映射
     * @param keyPresseds
     * @param keyMappeds
     * @param delay
     */
    public static void mapKey(final int[] keyPresseds, final int [] keyMappeds, final int delay){
        ShortcutManager.getInstance().addShortcutListener(new ShortcutManager.ShortcutListener() {
            public void handle() {
                try {
                    Robot r=new Robot();//创建自动化工具对象
                    r.delay(delay);
                    for (int keyMapped:keyMappeds){
                        r.keyPress(keyMapped);
                    }
                    for (int keyMapped:keyMappeds){
                        r.keyRelease(keyMapped);
                    }
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }
        }, keyPresseds);
    }
}
