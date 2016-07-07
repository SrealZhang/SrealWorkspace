package com.app.sample.chatting.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yangbin on 2016/3/15.
 */
public class DateUtil {

    public static String long2Date(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(time);//获取当前时间
        String str = sdf.format(curDate);
        return isToday(str, time);
    }

    public static String isToday(String str, long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(str);
            Date now = new Date();
            now = sdf.parse(sdf.format(now));
            long sl = date.getTime();
            long el = now.getTime();
            long ei = sl - el;
            int value = (int) (ei / (1000 * 60 * 60 * 24));
            if (value == 0) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                Date curDate = new Date(time);//获取当前时间
                return sdf1.format(curDate);
            } else if (value == -1) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                Date curDate = new Date(time);//获取当前时间
                return "昨天 " + sdf1.format(curDate);
            } else if (value == 1) {
                return "明天";
            } else {
                return str;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取SimpleDateFormat
     *
     * @param parttern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String parttern) throws RuntimeException {
        return new SimpleDateFormat(parttern);
    }

    /**
     * 获取日期中的某数值。如获取月份
     *
     * @param date     日期
     * @param dateType 日期格式
     * @return 数值
     */
    private static int getInteger(Date date, int dateType) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(dateType);
    }
}
