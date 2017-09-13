package com.helper.west2ol.fzuhelper.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by CoderQiang on 2017/8/11.
 */

public class DateUtil {

    public static List<String> getDate(){
        GregorianCalendar gc = new GregorianCalendar();
        gc.set(2017,4-1,21);
        System.out.println(new SimpleDateFormat("M/dd").format(gc.getTime()));
        Calendar cal = Calendar.getInstance();
        cal.setTime(gc.getTime());
        int w = cal.get(Calendar.DAY_OF_WEEK)-1;
        System.out.println("星期:"+w);
        gc.add(5,-w+1);
        cal.setTime(gc.getTime());
        int w2= cal.get(Calendar.DAY_OF_WEEK)-1;
        System.out.println("星期:"+w2+" 日期:"+new SimpleDateFormat("M/dd").format(gc.getTime()));
        return new ArrayList<String>();
    }
}
