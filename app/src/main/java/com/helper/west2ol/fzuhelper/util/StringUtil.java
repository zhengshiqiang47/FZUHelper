package com.helper.west2ol.fzuhelper.util;

/**
 * Created by CoderQiang on 2017/10/3.
 */

public class StringUtil {

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }else {
            if (str.length() >= 1) {
                return false;
            }else {
                return true;
            }
        }
    }
}
