package com.helper.west2ol.fzuhelper.test;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CoderQiang on 2017/10/2.
 */

public class RegexTest {

    public static void main(String[] args){
        String roomName="东1-202 105(105) 机房";
        String res=roomName.replaceAll("\\(([0-9]*)\\)"," $1 ");
        System.out.println("res:" + res);

        Pattern pattern=Pattern.compile("(.*)\\(([0-9]*)\\)(.*)");
        Matcher matcher = pattern.matcher(roomName);
        while (matcher.find()) {
            for (int i = 0; i < matcher.groupCount(); i++) {
                System.out.println("onBindViewHolder: matchFind "+i+" "+matcher.group(i+1));
            }
        }
    }
}
