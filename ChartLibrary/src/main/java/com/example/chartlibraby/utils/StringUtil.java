package com.example.chartlibraby.utils;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenhongfei on 2016/2/19.
 */
public class StringUtil {
    /**
     * 判断字符串是否包含html标签
     *
     * @param string
     * @return
     */
    public static boolean isContainHtml(String string) {
        Pattern pattern = Pattern.compile("<(\\S*?) [^>]*>.*?</\\1>|<[^>]*? />|<.*?>");
        Matcher matcher = pattern.matcher(string);
        boolean result = matcher.find();

        return result;
    }


    /**
     * 格式化小数点（如果小数位为0，则去除）保留一位小数
     *
     * @param number
     * @return
     */
    public static String formatZeroDecimalPoint(double number) {
        // 判断有没小数部分
        int intNumber = (int) number;
        double point = number - intNumber;
        if (point > 0) {
            return "" + keepOneDecimalPlaces(number);
        } else {
            return "" + intNumber;
        }
    }


    /**
     * 格式化小数点（如果小数位为0，则去除）保留一位小数
     * @param number
     * @return
     */
    public static String formatZeroDecimalPoint(float number) {
        // 判断有没小数部分
        int intNumber = (int) number;
        double point = number - intNumber;
        if (point > 0) {
            return "" + keepOneDecimalPlaces(number);
        } else {
            return "" + intNumber;
        }
    }



    /**
     * 保留1位小数
     *
     * @param number
     * @return
     */
    public static String keepOneDecimalPlaces(double number) {
        String result = new DecimalFormat("0.0").format(number);

        return result;
    }

}
