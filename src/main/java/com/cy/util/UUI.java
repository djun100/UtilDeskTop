package com.cy.util;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Enumeration;

/**
 * Created by cy on 2016/1/26.
 */
public class UUI {

    public static FontUIResource preferFont(){
        Dimension curr=getScreenSize();
        FontUIResource fontUIResource = null;
        if (curr.getWidth()==1920){
            fontUIResource=new FontUIResource("微软雅黑", Font.PLAIN,17);
        }else
            fontUIResource=new FontUIResource("微软雅黑", Font.PLAIN,32);
        return fontUIResource;
    }

    public static void setDefaultFont(JComponent component){
        component.setFont(new Font("微软雅黑",Font.PLAIN,30));
    }
    public static Dimension getScreenSize(){
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
    public static void initFrame2_3Screen(JFrame frame){
        Dimension screenHalf=new Dimension(getScreenSize().width*2/3,getScreenSize().height*2/3);
        frame.setMinimumSize(screenHalf);
        frame.setBounds((getScreenSize().width-screenHalf.width)/2,(getScreenSize().height-screenHalf.height)/2,0,0);

    }
    public static void initFrame(JFrame frame,Container container){
        initFrame2_3Screen(frame);
        frame.setContentPane(container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    public static void setUIFont(FontUIResource fui,Frame frame){
        Enumeration keys=UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key=keys.nextElement();
            Object value=UIManager.get(key);
            if (value != null && value instanceof FontUIResource) {
                UIManager.put(key, fui);
            }
        }
        SwingUtilities.updateComponentTreeUI(frame);
    }
    public static void appendTextNewLine(JTextComponent component,String content){
        component.setText(component.getText()+"\n"+content);
    }
}
