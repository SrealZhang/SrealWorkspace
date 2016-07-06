package com.app.sample.chatting.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import com.app.sample.chatting.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by androidDEV2 on 2016/7/6.
 */
public class TimeUtil {

    private static final int SHORT_DATE_FLAGS = DateUtils.FORMAT_SHOW_DATE
            | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_ABBREV_ALL;
    private static final int FULL_DATE_FLAGS = DateUtils.FORMAT_SHOW_TIME
            | DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_SHOW_DATE;

    public static String readableTimeDifference(Context context, long time) {
        return readableTimeDifference(context, time, false);
    }

    public static String readableTimeDifferenceFull(Context context, long time) {
        return readableTimeDifference(context, time, true);
    }

    private static String readableTimeDifference(Context context, long time,
                                                 boolean fullDate) {
        if (time == 0) {
            return context.getString(R.string.just_now);
        }
        Date date = new Date(time);
        long difference = (System.currentTimeMillis() - time) / 1000;
        if (difference < 60) {
            return context.getString(R.string.just_now);
        } else if (difference < 60 * 2) {
            return context.getString(R.string.minute_ago);
        } else if (difference < 60 * 15) {
            return context.getString(R.string.minutes_ago,
                    Math.round(difference / 60.0));
        } else if (today(date)) {
            java.text.DateFormat df = DateFormat.getTimeFormat(context);
            return df.format(date);
        } else {
            if (fullDate) {
                return DateUtils.formatDateTime(context, date.getTime(),
                        FULL_DATE_FLAGS);
            } else {
                return DateUtils.formatDateTime(context, date.getTime(),
                        SHORT_DATE_FLAGS);
            }
        }
    }

    private static boolean today(Date date) {
        return sameDay(date, new Date(System.currentTimeMillis()));
    }

    public static boolean sameDay(long timestamp1, long timestamp2) {
        return sameDay(new Date(timestamp1),new Date(timestamp2));
    }

    private static boolean sameDay(Date a, Date b) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(a);
        cal2.setTime(b);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2
                .get(Calendar.DAY_OF_YEAR);
    }

}
