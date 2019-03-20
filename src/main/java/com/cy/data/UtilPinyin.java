package com.cy.data;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class UtilPinyin {

    public static String hanziTopinyin(String tobeConvert){
        try {
            return PinyinHelper.convertToPinyinString(tobeConvert,"", PinyinFormat.WITHOUT_TONE);
        } catch (PinyinException e) {
            e.printStackTrace();
        }
        return "";
    }
}
