package cn.itbat.microsoft.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * @version V1.0
 */
@Slf4j
public class DateUtil {

    public static String YYMMDD = "yyyy-MM-dd";
    public static String YYMMDD_HHmm = "yyyy-MM-dd HH:mm";
    public static String YYMMDD_HHmmSS = "yyyy-MM-dd HH:mm:ss";
    public static String HHmmSS = "HH:mm:ss";
    public static String YYMMDDHHmmssSSS = "yyyyMMddHHmmssSSS";
    public static String HHmmssSSS = "HHmmssSSS";
    public static String YYMM = "yyyy-MM";
    public static String YYMMDDHHmm = "yyyyMMddHHmm";
    public static String YYMMDD_SIMPLE = "yyyyMMdd";
    public static String MMDD_SIMPLE = "MMdd";
    public static String YYMMDDHHmmss = "yyyyMMddHHmmss";


    /**
     * 不可实例化
     */
    private DateUtil() {
    }

    /**
     * 日期参数类型
     */
    public enum ParamType {
        day, month, yeah
    }


    /**
     * 格式化时间
     *
     * @param date 时间,为空时 取当前时间
     *             格式
     */
    public static String format(Date date, boolean defail) {
        if (date == null) {
            if (defail) {
                date = new Date();
            } else {
                return "";
            }
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }


    /**
     * 格式化时间
     *
     * @param date   时间,为空时 取当前时间
     * @param format 格式
     */
    public static String format(Date date, String format) {
        if (date == null) {
            return "";
        }
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * String 字符串转时间类型
     *
     * @param dateStr 时间字符串
     * @param format  格式化
     */
    public static Date stringToDate(String dateStr, String format) {

        if (dateStr == null) {
            return null;
        }


        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            log.error("日期转换异常", e);
        }
        return date;
    }

    /**
     * String 时间戳字符串转时间
     *
     * @param dateStr 时间戳字符串
     * @return Date
     */
    public static Date stringStampToDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        if (dateStr.length() == 10) {
            dateStr = dateStr + "000";
        }
        Date date = null;
        try {
            date = new Date(Long.valueOf(dateStr));
        } catch (Exception e) {
            log.error("日期转换异常", e);
        }
        return date;
    }


    /**
     * 获得距指定时间多少小时前的日期
     */
    public static Date getBeforeHour(Date date, int hour) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.HOUR, now.get(Calendar.HOUR) - hour);
        return now.getTime();
    }


    /**
     * 解析日期
     *
     * @param strDate
     */
    public static Date parse(String strDate) {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        }
        SimpleDateFormat formater = new SimpleDateFormat(YYMMDD_HHmmSS);
        try {
            return formater.parse(strDate);
        } catch (ParseException e) {
            log.error("日期转换异常", e);
        }
        formater = new SimpleDateFormat(YYMMDD);
        try {
            return formater.parse(strDate);
        } catch (ParseException e) {
            log.error("日期转换异常", e);

        }
        return null;

    }


    /**
     * 获取指定日期所在月份结束的时间戳
     *
     * @param date 指定日期
     */
    public static Date getMonthEnd(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        //设置为当月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至23
        c.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        c.set(Calendar.MINUTE, 59);
        //将秒至59
        c.set(Calendar.SECOND, 59);
        //将毫秒至999
        c.set(Calendar.MILLISECOND, 998);
        // 获取本月最后一天的时间戳
        return c.getTime();
    }

    /**
     *  * 获取指定日期下个月的第一天
     *
     * @param date
     */
    public static Date getFirstDayOfNextMonth(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, 1);
            //将小时至0
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            //将分钟至0
            calendar.set(Calendar.MINUTE, 0);
            //将秒至0
            calendar.set(Calendar.SECOND, 0);
            //将毫秒至0
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (Exception e) {
            log.error("日期转换异常", e);
        }
        return null;
    }


    public static String getUtcDate(Date date) {
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df2.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df2.format(date);
    }

    public static String formatBetween(long betweenMs, BetweenFormater.Level level) {
        return (new BetweenFormater(betweenMs, level)).format();
    }


    public static void main(String[] args) {
        System.out.println("" + getUtcDate(new Date()));
        System.out.println(format(getFirstDayOfNextMonth(parse("2018-01-01 00:00:00")), "yyyy-MM-dd HH:mm:ss"));
        System.out.println(format(getMonthEnd(getFirstDayOfNextMonth(parse("2018-01-01 00:00:00"))), "yyyy-MM-dd HH:mm:ss"));
//        System.out.println(DateUtil.format(getMonthFirstDay(new Date(), 0),"yyyy-MM-dd HH:mm:ss"));
    }

}
