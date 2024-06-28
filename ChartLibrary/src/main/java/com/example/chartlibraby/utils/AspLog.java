package com.example.chartlibraby.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志打印类，提供了一个开关来决定是否打印日志
 */
public class AspLog {
    public enum LogLevel {
        OFF,
        ERROR,
        WARN,
        INFO,
        DEBUG
    }

    public final static String STARTUP_TAG = "startup_log";
    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "Acc369userHead" + File.separator + "debug.txt";
    private static boolean isFirst = true;

    // 是否强制打开log
    private static boolean mIsForceLog = false;
    public static boolean isPrintLog = true;
    public static boolean isPrintSDcardLog = false;
    public static boolean isWriteToFile = false;
    private static final boolean isShowLoginSuc = false;
    private final static String LOG_FILEPATH = "Acc369userHead" + File.separator + "log" + File.separator;
    // private final static String LOG_CFGFILE = "mmlog.cfg";
    private final static String LOG_FILENAME = "log";
    private final static String LOG_FILEEXT = ".txt";
    private static File mLogFile;

    private final static long LOGFILE_LIMIT = 1000000L;
    private final static SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //    private final static SimpleDateFormat DATEFORMAT1 = new SimpleDateFormat("yyyyMMddHHmmss");
    private static String mSdcardRootPath = "/sdcard/";
    private static final String SHOW_CFGFILE = "mmloginsuccess.cfg";

     private static LogLevel mLevel = LogLevel.DEBUG;

    public static LogLevel getLogLevel() {
        return mLevel;
    }

    public static void setIsPrintLog() {
        try {
            if (new File(path).exists())
                isPrintLog = true;
            isWriteToFile = true;
            isFirst = false;
        } catch (Exception ex) {
        }
    }

    public static void setIsPrintLog(Activity acti) {
        try {
            Intent intent = acti.getIntent();
            // 文件是否存在
            File file = new File(path);
            if (file.exists()) {
                isPrintLog = true;
                isWriteToFile = true;
            }
            // intent内是否有数据
            if (isPrintLog == false) {
                isPrintLog = intent.getBooleanExtra("com.loogme.account.debug", false);

                if (isPrintLog == true) {
                    isWriteToFile = true;
                    file.createNewFile();
                }
            }

            isFirst = false;

        } catch (Exception ex) {

        }
    }

    private static void checkLog() {
        if (isFirst == true) {
            File file = new File(path);
            if (file.exists()) {
                isPrintLog = true;
                isWriteToFile = true;
                isPrintSDcardLog = true;
            }
            isFirst = false;
        }
        createLogFile();
    }

    public static void print(String msg) {
        checkLog();
        if (isPrintLog) {
            System.out.print(msg == null ? "" : msg);
        }
        writeLogFile("", "", msg);
    }

    private static void createLogFile() {
        if (isWriteToFile) {
            synchronized (LOG_FILENAME) {
                if (mLogFile == null) {
                    try {
                        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            return;
                        }

                        // File sdcardRootPath =
                        // Environment.getExternalStorageDirectory();
                        mSdcardRootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;

                        // File cfgfile = new File(mSdcardRootPath +
                        // LOG_CFGFILE);
                        // if(!cfgfile.exists()){//没用开启配置文件则不写日志文件
                        // return;
                        // }

                        File logpath = new File(mSdcardRootPath + LOG_FILEPATH);
                        if (!logpath.exists()) {
                            logpath.mkdir();
                        }

                        mLogFile = new File(mSdcardRootPath + LOG_FILEPATH + LOG_FILENAME + LOG_FILEEXT);
                        if (!mLogFile.exists()) {
                            AspLog.d("TestFile", "Create the file:" + LOG_FILENAME);
                            mLogFile.createNewFile();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mLogFile.isFile()) {
                        if (mLogFile.length() > LOGFILE_LIMIT) {
                            StringBuffer sb = new StringBuffer(mSdcardRootPath);
                            sb.append(LOG_FILEPATH);
                            sb.append(LOG_FILENAME);
                            sb.append(DATEFORMAT.format(new Date()));
                            sb.append(LOG_FILEEXT);
                            mLogFile.renameTo(new File(sb.toString()));
                            sb = null;
                            sb = new StringBuffer(mSdcardRootPath);
                            sb.append(LOG_FILEPATH);
                            sb.append(LOG_FILENAME);
                            sb.append(LOG_FILEEXT);
                            mLogFile = new File(sb.toString());
                            sb = null;
                            if (!mLogFile.exists()) {
                                AspLog.d("TestFile", "Create the file:" + LOG_FILENAME + LOG_FILEEXT);
                                try {
                                    mLogFile.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void writeLogFile(String level, String tag, String msg) {
        if (isWriteToFile) {
            synchronized (LOG_FILENAME) {
                if (mLogFile != null) {

                    StringBuffer sb = new StringBuffer();
                    sb.append(DATEFORMAT.format(new Date()));
                    sb.append(": ");
                    sb.append(level);
                    sb.append(": ");
                    sb.append(tag);
                    sb.append(": ");
                    sb.append(msg);
                    sb.append("\n");
                    RandomAccessFile raf = null;
                    try {
                        raf = new RandomAccessFile(mLogFile, "rw");
                        raf.seek(mLogFile.length());
                        raf.write(sb.toString().getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();

                    } finally {
                        sb = null;
                        if (raf != null) {
                            try {
                                raf.close();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        }
    }

    public static void println(String msg) {
        checkLog();
        if (isPrintLog) {
            System.out.println(msg == null ? "" : msg);
        }
        writeLogFile("", "", msg);
    }

    public static void v(String tag, String msg) {
        if (! isLoggable(Log.VERBOSE)) {
            return;
        }
        checkLog();
        if (isPrintLog) {
            Log.v(tag, msg == null ? "" : msg);
        }
        writeLogFile("VERBOSE", tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (! isLoggable(Log.VERBOSE)) {
            return;
        }
        checkLog();
        if (isPrintLog) {
            Log.v(tag, msg == null ? "" : msg, tr);
        }
        writeLogFile("VERBOSE", tag, msg);
    }

    public static void d(String tag, String msg) {
        if (! isLoggable(Log.DEBUG)) {
            return;
        }
        checkLog();
        if (isPrintLog) {
            Log.d(tag, msg == null ? "" : msg);
        }
        writeLogFile("DEBUG", tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (! isLoggable(Log.DEBUG)) {
            return;
        }
        checkLog();
        if (isPrintLog) {
            Log.d(tag, msg == null ? "" : msg, tr);
        }
        writeLogFile("DEBUG", tag, msg);
    }

    public static void i(String tag, String msg) {
        if (! isLoggable(Log.INFO)) {
            return;
        }
        checkLog();
        if (isPrintLog) {
            Log.i(tag, msg == null ? "" : msg);
        }

        writeLogFile("INFO", tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (! isLoggable(Log.INFO)) {
            return;
        }
        checkLog();
        if (isPrintLog) {
            Log.i(tag, msg == null ? "" : msg, tr);
        }
        writeLogFile("INFO", tag, msg);
    }

    public static void w(String tag, String msg) {
        if (! isLoggable(Log.WARN)) {
            return;
        }
        checkLog();
        if (isPrintLog) {
            Log.w(tag, msg == null ? "" : msg);
        }
        writeLogFile("WARN", tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (! isLoggable(Log.WARN)) {
            return;
        }
        checkLog();
        if (isPrintLog) {
            Log.w(tag, msg == null ? "" : msg, tr);
        }
        writeLogFile("WARN", tag, msg);
    }

    public static void e(String tag, String msg) {
        if (! isLoggable(Log.ERROR)) {
            return;
        }
        checkLog();
        if (isPrintLog) {
            Log.e(tag, msg == null ? "" : msg);
        }
        writeLogFile("ERROR", tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (! isLoggable(Log.ERROR)) {
            return;
        }
        checkLog();
        if (isPrintLog) {
            Log.e(tag, msg == null ? "" : msg, tr);
        }
        writeLogFile("ERROR", tag, msg);
    }


    public static void save2sd(String filename, String data) {

        File file = new File("/sdcard/Acc369userHead");
        if (!file.isDirectory()) {
            if (file.exists())
                file.delete();
            file.mkdir();
        }
        file = new File("/sdcard/Acc369userHead/" + filename);
        int index = -1;
        index = filename.lastIndexOf('/');
        if (index > 0) {
            filename = filename.substring(index + 1);
        }
        index = filename.lastIndexOf('.');
        String basename, extname;
        if (index > 0) {
            basename = filename.substring(0, index);
            extname = filename.substring(index);
        } else {
            basename = filename;
            extname = "";
        }
        index = 0;
        while (file.exists()) {
            file = null;
            file = new File("/sdcard/Acc369userHead/" + basename + (index++) + extname);
        }
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file, true);
            fos.write(data.getBytes());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
        }
    }


    /**
     * 是否一直显示登录成功提示toast,只用于调试,测试验证,拨测
     *
     * @return
     */
    public static boolean isShowLoginSuccess() {
        if (!isShowLoginSuc) {
            return false;
        }
        File cfgfile = new File(mSdcardRootPath + SHOW_CFGFILE);
        if (!cfgfile.exists()) {// 没有开启配置文件则不显示
            return false;
        }
        return true;
    }

    /**
     * 是否打印log
     *
     * 1、mIsForceLog 强制打开log，记录所有的log
     * 2、mIsForceLog 为false， （1）debug状态下，记录所有的log（2）在release下，只记录 >= warn 的log
     * @return
     */
    private static boolean isLoggable(int logLevel) {
       return true;
    }

    public static void other(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.e(tag, msg);
    }

}
