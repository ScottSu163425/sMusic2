package com.scott.su.common.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Calendar;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/5/17
 */

public class TimeUtil {
    public static final long MILLISECONDS_OF_SECOND = 1000;
    public static final long SECONDS_OF_MINUTE = 60;
    public static final long MINUTES_OF_HOUR = 60;
    public static final long SECONDS_OF_HOUR = SECONDS_OF_MINUTE * MINUTES_OF_HOUR;
    public static final long MILLISECONDS_OF_MINUTE = MILLISECONDS_OF_SECOND * SECONDS_OF_MINUTE;
    public static final long MILLISECONDS_OF_HOUR = MILLISECONDS_OF_SECOND * SECONDS_OF_HOUR;

    private static final String DIVIDER_HHMMSS = ":";


    private TimeUtil() {
    }

    /**
     * 获取当前系统时间：小时
     *
     * @return 0~24
     */
    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前系统时间：分钟
     *
     * @return
     */
    public static int getCurrentMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取当前系统时间：秒
     *
     * @return
     */
    public static int getCurrentSecond() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 分转小时
     *
     * @param minutes
     * @return
     */
    public static long getHoursFromMinutes(long minutes) {
        return minutes / MINUTES_OF_HOUR;
    }

    /**
     * 秒转小时
     *
     * @param seconds
     * @return
     */
    public static long getHoursFromSeconds(long seconds) {
        return seconds / SECONDS_OF_HOUR;
    }

    /**
     * 毫秒转小时
     *
     * @param mills
     * @return
     */
    public static long getHoursFromMills(long mills) {
        return mills / MILLISECONDS_OF_HOUR;
    }

    /**
     * 毫秒转分钟
     *
     * @param mills
     * @return
     */
    public static long getMinutesFromMills(long mills) {
        return mills / MILLISECONDS_OF_MINUTE;
    }

    /**
     * 毫秒转分钟
     *
     * @param seconds
     * @return
     */
    public static long getMinutesFromSeconds(long seconds) {
        return seconds / SECONDS_OF_MINUTE;
    }

    /**
     * 毫秒转秒
     *
     * @param mills
     * @return
     */
    public static long getSecondsFromMills(long mills) {
        return mills / MILLISECONDS_OF_SECOND;
    }

    /**
     * 毫秒转时、分、秒
     *
     * @param mills
     * @return
     */
    public static long[] getHmsFromMills(long mills) {
        final long[] hms = new long[]{0, 0, 0};

        long h = mills / MILLISECONDS_OF_HOUR;
        mills = mills % MILLISECONDS_OF_HOUR;

        long m = mills / MILLISECONDS_OF_MINUTE;
        mills = mills % MILLISECONDS_OF_MINUTE;

        long s = mills / MILLISECONDS_OF_SECOND;

        hms[0] = h;
        hms[1] = m;
        hms[2] = s;

        return hms;
    }

    /**
     * 秒转时、分、秒
     *
     * @param seconds
     * @return
     */
    public static long[] getHmsFromSeconds(long seconds) {
        return getHmsFromMills(seconds * MILLISECONDS_OF_SECOND);
    }

    /**
     * 毫秒转HH:MM:SS字符串
     *
     * @param mills
     * @param divider 自定义分隔符
     * @return
     */
    public static String getHhmmssFromMills(long mills, @Nullable String divider) {
        long[] hms = getHmsFromMills(mills);
        String strH = toDoubleDigits(hms[0]);
        String strM = toDoubleDigits(hms[1]);
        String strS = toDoubleDigits(hms[2]);

        String div = TextUtils.isEmpty(divider) ? DIVIDER_HHMMSS : divider;

        return strH + div + strM + div + strS;
    }

    /**
     * 秒转HH:MM:SS字符串
     *
     * @param seconds
     * @param divider 自定义分隔符
     * @return
     */
    public static String getHhmmssFromSeconds(long seconds, @Nullable String divider) {
        return getHhmmssFromMills(seconds * MILLISECONDS_OF_SECOND,divider);
    }

    private static String toDoubleDigits(long num) {
        String str = String.valueOf(num);

        if (str.length() < 2) {
            str = "0" + str;
        }

        if (str.length() > 2) {
            str = "99";
        }


        return str;
    }
}
