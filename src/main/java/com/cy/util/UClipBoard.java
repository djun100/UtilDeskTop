package com.cy.util;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;

/**
 * Created by cy on 2017/6/16.
 */
public class UClipBoard {

    public static void write(String content) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trandata = new StringSelection(content);
        clipboard.setContents(trandata, null);
    }

    public static String read() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable clipT = clipboard.getContents(null); //获取文本中的Transferable对象
        String result = null;
        if (clipT != null) {
            if (clipT.isDataFlavorSupported(DataFlavor.stringFlavor)) //判断内容是否为文本类型stringFlavor
                try {
                    result = (String) clipT.getTransferData(DataFlavor.stringFlavor); //返回指定flavor类型的数据
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return result;
        }
        return null;
    }
}
