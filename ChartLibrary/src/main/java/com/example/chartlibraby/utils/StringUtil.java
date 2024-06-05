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
     * 半角转全角
     *
     * @param string
     * @return
     */
    public static String toSBC(String string) {
        char[] c = string.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    //首字母大写
    public static String initcapName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    /**
     * 全角转换为半角
     *
     * @param string
     * @return
     */
    public static String toDBC(String string) {
        char[] c = string.toCharArray();
        for (int i = 0; i < c.length; i++) {
            // 全角空格为12288，半角空格为32
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            // 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 只转义指定的字符
     *
     * @param string
     * @return
     */
    public static String toSpecialSBC(String string) {
        char[] c = string.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] >= 48 && c[i] <= 57) // 只转义数字字符，会计的字符基本上都是数字字符（大题有换行符\n，被转义了会乱码）
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    /**
     * stream 转 String
     *
     * @param in
     * @param encoding
     * @return
     */
    public static String inputStreamToString(InputStream in, String encoding) {
        final int BUFFER_SIZE = 4096;
        String retString = null;
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] data = new byte[BUFFER_SIZE];
            int count = -1;
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);

            data = null;
            retString = new String(outStream.toByteArray(), encoding == null ? "UTF-8" : encoding);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retString;
    }

    /**
     * 转成标准的http url
     *
     * @param url
     * @return
     */
    public static String formatUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }
        if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file://")) {
            return url;
        }

        String formatUrl = "http://" + url;

        return formatUrl;
    }

    public static boolean isUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return true;
        }
        return false;
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
     * 格式化小数点（如果小数位为0，则去除）保留一位小数
     *
     * @param number
     * @return
     */
    public static String formatZeroDecimalPoint(double number, String pattern) {
        // 判断有没小数部分
        int intNumber = (int) number;
        double point = number - intNumber;
        if (point > 0) {
            return "" + keepOneDecimalPlaces(number, pattern);
        } else {
            return "" + intNumber;
        }
    }


    public static String formatZeroDecimalPoint(String number) {
        if (TextUtils.isEmpty(number)) {
            return "";
        }
        // 判断有没小数部分
        double newNumber = Double.valueOf(number);
        int intNumber = (int) newNumber;
        double point = newNumber - intNumber;
        if (point > 0) {
            return "" + keepOneDecimalPlaces(newNumber);
        } else {
            return "" + intNumber;
        }
    }

    /**
     * 判断手机格式是否正确，改为简单判断
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {

        // Pattern p = Pattern
        // .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        // Matcher m = p.matcher(mobiles);
        // return m.matches();
        //
        if (mobile.startsWith("1") && mobile.length() == 11) {
            return true;
        }
        return false;
    }

    /**
     * 把小数点格式化到length位
     *
     * @param number
     * @param length
     * @return
     */
    public static String formatDecimal(double number, int length) {
        // 判断有没小数部分
        int p = (int) Math.pow(10, length);
        double num = (double) (Math.round(number * p)) / p;
        String retString = formatZeroDecimalPoint(num);

        return retString;
    }

    public static String random() {
        String random = String.valueOf((int) (Math.random() * 1000)); // 产生随机数

        return random;
    }

    public static String buildUrl(String url, Map<String, String> paramMap) {
        if (TextUtils.isEmpty(url) || CollectionUtil.isEmpty(paramMap)) {
            return url;
        }
        StringBuilder urlBuilder = new StringBuilder(url);
        if (url.indexOf("?") == -1) {
            urlBuilder.append("?");
        } else {
            urlBuilder.append("&");
        }

        Iterator<Map.Entry<String, String>> it = paramMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (TextUtils.isEmpty(entry.getKey()) || TextUtils.isEmpty(entry.getValue())) {
                continue;
            }
            urlBuilder.append(entry.getKey() + "=" + entry.getValue());
        }

        return urlBuilder.toString();
    }

    /**
     * 转换成人类可读的格式
     *
     * @param size
     * @return
     */
    public static String convertFileSizeToHM(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            float f = (float) size / gb;
            return String.format("%.2f GB", f);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format("%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format("%.0f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    public static String getFileType(String suffix, boolean isSuffix) {
        String fileType = "unknown";
        if (!TextUtils.isEmpty(suffix)) {
            if (!isSuffix) {
                suffix = suffix.substring(suffix.lastIndexOf(".") + 1, suffix.length()).toLowerCase();
            }
            switch (suffix.toLowerCase()) {
                case "doc":// word
                case "docx":
                    fileType = "word";
                    break;
                case "xls":
                case "xlsx":
                    fileType = "excel";
                    break;
                case "ppt":
                case "pptx":
                    fileType = "ppt";
                    break;
                case "pdf":
                    fileType = "pdf";
                    break;
                case "txt":
                case "log":
                    fileType = "txt";
                    break;
                case "zip":
                case "rar":
                case "tar":
                case "gz":
                case "7z":
                    fileType = "zip";
                    break;
                case "pic":
                case "jpg":
                case "png":
                case "bmp":
                case "gif":
                case "jpeg":
                case "psd":
                    fileType = "pic";
                    break;
                case "mp4":
                case "mpg":
                case "mpeg":
                case "avi":
                case "rm":
                case "rmvb":
                case "mov":
                case "wmv":
                case "asf":
                case "flv":
                case "3gp":
                    fileType = "video";
                    break;
                default:
                    break;
            }
        }

        return fileType;
    }

    public static boolean isIpv4(String ip) {
        Pattern pattern = Pattern.compile("([0-9]+\\.){3}([0-9]+){1}");
        Matcher matcher = pattern.matcher(ip);
        boolean result = matcher.find();

        return result;
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

    public static String keepTwoDecimalPlaces(double number) {
        String result = new DecimalFormat("0.00").format(number);

        return result;
    }

    /**
     * 保留1位小数
     *
     * @param number
     * @return
     */
    public static String keepOneDecimalPlaces(double number, String pattern) {
        String result = new DecimalFormat(pattern).format(number);

        return result;
    }


    /**
     * 将字符串转成Int
     */
    public static int toInteger(String s) {
        if (TextUtils.isEmpty(s.trim())) {
            return -1;
        }
        try {
            Integer i = Integer.valueOf(s);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    /**
     * 将字符串转成long
     */
    public static long toLong(String s) {
        if (TextUtils.isEmpty(s.trim())) {
            return -1;
        }

        try {
            Long l = Long.valueOf(s);
            return l;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }


    /**
     * 是否满足密码要求：密码必须是包含大写字母、小写字母、数字、特殊符号（不是字母，数字，下划线，汉字的字符）组合
     *
     * @param psw
     * @return
     */
    public static boolean isSatisfyPassWord(String psw) {
        String reg = "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_!@#$%^&*`~()-+=]+$)(?![a-z0-9]+$)(?![a-z\\W_!@#$%^&*`~()-+=]+$)(?![0-9\\W_!@#$%^&*`~()-+=]+$)(?![a-zA-Z0-9]+$)(?![a-zA-Z\\W_!@#$%^&*`~()-+=]+$)(?![a-z0-9\\W_!@#$%^&*`~()-+=]+$)(?![0-9A-Z\\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\\W_!@#$%^&*`~()-+=]{8,16}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(psw);
        if (m.find()) {
            return true;
        }
        return false;
    }


}
