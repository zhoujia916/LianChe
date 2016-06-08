package puzzle.lianche.utils;

import puzzle.lianche.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertUtil {
    public static int toInt(String value){
        if(StringUtil.isNullOrEmpty(value)){
            throw new IllegalArgumentException("Value is null or empty");
        }
        return Integer.valueOf(value);
    }

    public static String toString(int value){
        return String.valueOf(value);
    }

    public static Long toLong(String value){
        if(StringUtil.isNullOrEmpty(value)){
            throw new IllegalArgumentException("Value is null or empty");
        }
        return Long.valueOf(value);
    }

    public static String toString(long value){
        return String.valueOf(value);
    }

    public static double toDouble(String value){
        if(StringUtil.isNullOrEmpty(value)){
            throw new IllegalArgumentException("Value is null or empty");
        }
        return Double.valueOf(value);
    }

    public static String toString(double value){
        return String.valueOf(value);
    }

    public static Date toDateTime(String value) throws ParseException {
        return toDate(value, Constants.DATETIME_FORMAT);
    }

    public static Date toTime(String value) throws ParseException {
        return toDate(value, Constants.TIME_FORMAT);
    }

    public static Date toDate(String value) throws ParseException {
        return toDate(value, Constants.DATE_FORMAT);
    }


    public static Date toDate(String value, String format) throws ParseException {
        if(StringUtil.isNullOrEmpty(value) || StringUtil.isNullOrEmpty(format)){
            throw new IllegalArgumentException("Value or format is null or empty");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.parse(value);
    }

    public static String toString(Date date){
        return toString(date, Constants.DATETIME_FORMAT);
    }

    public static String toString(Date date, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date toDate(long ticket){
        return new Date(ticket);
    }

    public static Long toLong(Date date){
        return date.getTime();
    }

    public static Date toDate(int ticket, long base){
        return new Date(ticket + base);
    }

    public static int toInt(Date date, long base){
        return (int)(date.getTime() - base);
    }
}
