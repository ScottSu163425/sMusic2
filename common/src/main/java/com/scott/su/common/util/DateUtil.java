package com.scott.su.common.util;

import java.util.Calendar;

/**
 * 描述:
 * 作者: su
 * 日期: 2018/5/16
 */

public class DateUtil {

    /**
     * 获取当前系统年
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year;
    }

    /**
     * 获取当前系统月
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;//Java月份从0开始算
        return month;
    }

    /**
     * 获取当前系统日
     *
     * @return
     */
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    /**
     * 获取指定日期是一星期中的星期几
     *
     * @param year
     * @param month 1~12
     * @param day
     * @return 1~7 ,星期日~星期六
     */
    public static int getDayOfWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取指定月份的天数
     *
     * @param year
     * @param month 1~12
     * @return
     */
    public static int getDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);//先指定年份
        calendar.set(Calendar.MONTH, month - 1);//再指定月份 Java月份从0开始算
        int daysCountOfMonth = calendar.getActualMaximum(Calendar.DATE);//获取指定年份中指定月份有几天
        return daysCountOfMonth;
    }

}
