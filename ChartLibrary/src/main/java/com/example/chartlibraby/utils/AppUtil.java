package com.example.chartlibraby.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * app 相关的工具类
 * <p/>
 * Created by chenhongfei on 2016/2/18.
 */
public class AppUtil {
    private static final String TAG = "AppUtil";
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int px = (int) (dipValue * scale + 0.5f);
//        Log.v("dip2px", "scale:" + scale + " dip:" + dipValue + " px:" + px);
        return px;
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int dp = (int) (pxValue / scale + 0.5f);
//        Log.v("px2dip", "scale:" + scale + " dip:" + dp + " px:" + pxValue);
        return dp;
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }



    //判断当前设备是否是模拟器。如果返回TRUE，则当前是模拟器，不是返回FALSE
    public static boolean isEmulator(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000")) {
                return true;
            }
            return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
        } catch (Exception ioe) {

        }
        return false;
    }

    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     * @return
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
     * 19及以上
     *
     * @param context
     * @return
     */
    public static boolean isEnableV19(Context context) {
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass;
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            LogUtil.e("异常信息" + e.getMessage());
        } catch (NoSuchMethodException e) {
            LogUtil.e("异常信息" + e.getMessage());
        } catch (NoSuchFieldException e) {
            LogUtil.e("异常信息" + e.getMessage());
        } catch (InvocationTargetException e) {
            LogUtil.e("异常信息" + e.getMessage());
        } catch (IllegalAccessException e) {
            LogUtil.e("异常信息" + e.getMessage());
        }
        return false;
    }

    /**
     * Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
     * 针对8.0及以上设备
     *
     * @param context
     * @return
     */
    public static boolean isEnableV26(Context context) {
        try {
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Method sServiceField = notificationManager.getClass().getDeclaredMethod("getService");
            sServiceField.setAccessible(true);
            Object sService = sServiceField.invoke(notificationManager);

            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            Method method = sService.getClass().getDeclaredMethod("areNotificationsEnabledForPackage"
                    , String.class, Integer.TYPE);
            method.setAccessible(true);
            return (boolean) method.invoke(sService, pkg, uid);
        } catch (Exception e) {
            LogUtil.e("异常：" + e);
        }
        return false;
    }


    /**
     * 获取包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        String packageName = null;
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), 0);
            packageName = pInfo.packageName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageName;
    }

    /**
     * 获取清单中的值
     *
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String metaValue = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                metaValue = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e("获取清单" + metaKey + "的值错误");
        }
        return metaValue;// xxx
    }


    /**
     * 获取底部导航条的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationHeight(Context context) {
        try {
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            int navigationHeight = context.getResources().getDimensionPixelSize(resourceId);
            return navigationHeight;
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int version = 0;
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    public static int getIconId(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            return info.icon;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }

    /**
     *      * 判断自己是否为默认桌面
     *      
     */
    public static boolean isDefaultHome(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);//Intent.ACTION_VIEW
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isDefault = getPackageName(context).equals(info.activityInfo.packageName);
        return isDefault;
    }

    /**
     * 获取Manifest定义的属性值
     *
     * @param context
     * @param name
     * @return
     */
    public static String getMetaDataValue(Context context, String name) {
        String value = null;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null && appInfo.metaData != null) {
                value = appInfo.metaData.getString(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static int getMetaDataResource(Context context, String name) {
        int value = -1;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null && appInfo.metaData != null) {
                value = appInfo.metaData.getInt(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    /**
     * 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception e) {
            LogUtil.e("错误信息" + TAG + " getMacAddress :" + e.getMessage());
            return null;
        }
    }

    /**
     * 获取设备的厂商名称
     *
     * @return
     */
    public static String manuFacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取imei号
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return getAndroidId(context);
        }
        return tm.getDeviceId();
    }

    /**
     * 获取imsi
     *
     * @param context
     * @return
     */
    public static String getImsi(Context context) {
        int checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String ret = tm.getSubscriberId();
        return ret;
    }

    /**
     * 获取sn号
     *
     * @param context
     * @return
     */
    public static String getSn(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "";
        }
        return tm.getSimSerialNumber();
    }

    /**
     * 获取android id
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        return androidId;
    }

    public static String getDeviceName() {
        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        return myDevice.getName();
    }

    /**
     * 生成 uuid
     */
    public static String getUuid() {
        String uuid = UUID.randomUUID().toString();

        return uuid;
    }

    public static String getPushClientId() {
        try {
            String uuid = UUID.randomUUID().toString();
            uuid = uuid.replace("-", "");
            uuid += uuid;

            return uuid;
        } catch (Exception e) {

        }

        return "";
    }


    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        String status = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (gps || network || status.contains("gps")) {
            return true;
        }
        return false;
    }


    public static boolean isBlueEnable() {
        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        if (blueadapter.isEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
//			Log.e(TAG, "flags:" + info.flags + " flag debug:" + ApplicationInfo.FLAG_DEBUGGABLE);
//			Log.e(TAG, "flag:" + (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) );
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 没有网络
     */
    public static final int NETWORK_TYPE_INVALID = 0;
    /**
     * wap网络
     */
    public static final int NETWORK_TYPE_WAP = 1;
    /**
     * 2G网络
     */
    public static final int NETWORK_TYPE_2G = 2;
    /**
     * 3G网络
     */
    public static final int NETWORK_TYPE_3G = 3;
    /**
     * wifi网络
     */
    public static final int NETWORK_TYPE_WIFI = 4;

    public static int getNetworkType(Context context) {
        int networkType = NETWORK_TYPE_INVALID;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            int netType = networkInfo.getType();
            if (netType == ConnectivityManager.TYPE_WIFI) {
                networkType = NETWORK_TYPE_WIFI;
            } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                int subtype = networkInfo.getSubtype();
                TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                if (subtype == TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming()) {
                    networkType = NETWORK_TYPE_3G;// 3G
                } else {
                    networkType = NETWORK_TYPE_2G;// 2G
                }
            }
        } else {
            networkType = NETWORK_TYPE_INVALID;
        }

        return networkType;
    }

    public static boolean isWifiNetwork(Context context) {
        if (NETWORK_TYPE_WIFI == getNetworkType(context)) {
            return true;
        }

        return false;
    }

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
   /*     try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;*/
//以下方法才能兼容android:label="${APP_NAME}"并取得应用名称，其中APP_NAME在打包过程中会直接赋值为应用名称
        if (context == null) {
            return null;
        }
        try {
            PackageManager packageManager = context.getPackageManager();
            return String.valueOf(packageManager.getApplicationLabel(context.getApplicationInfo()));
        } catch (Throwable e) {
            Log.i(TAG, "getAppName >> e:" + e.toString());
        }
        return null;
    }

    /**
     * 状态栏的高
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 添加联系人到本机
     *
     * @param context
     * @param contactMap
     * @return
     */
    public static boolean addContact(Context context, Map<String, String> contactMap) {
        try {
            ContentValues values = new ContentValues();

            // 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
            Uri rawContactUri = context.getContentResolver().insert(RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);

            // 向data表插入姓名数据
            String name = contactMap.get("name");
            if (!TextUtils.isEmpty(name)) {
                values.clear();
                values.put(RawContacts.Data.RAW_CONTACT_ID, rawContactId);
                values.put(RawContacts.Data.MIMETYPE, CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
                values.put(CommonDataKinds.StructuredName.GIVEN_NAME, name);
                context.getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入电话数据
            String mobile_number = contactMap.get("phone");
            if (!TextUtils.isEmpty(mobile_number)) {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                values.put(Phone.NUMBER, mobile_number);
                values.put(Phone.TYPE, Phone.TYPE_MOBILE);
                context.getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入Email数据
            String email = contactMap.get("email");
            if (!TextUtils.isEmpty(email)) {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
                values.put(Email.DATA, email);
                values.put(Email.TYPE, Email.TYPE_WORK);
                context.getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入备注信息
            String describe = contactMap.get("remark");
            if (!TextUtils.isEmpty(describe)) {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Note.CONTENT_ITEM_TYPE);
                values.put(Note.NOTE, describe);
                context.getContentResolver().insert(Data.CONTENT_URI, values);
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 复制到剪切板
     *
     * @param text
     */
    public static void copyClipboard(String text) {
//        ClipboardManager cbManager = (ClipboardManager)
//                MyApplication.getContextObject().getSystemService(Context.CLIPBOARD_SERVICE);
//        cbManager.setPrimaryClip(ClipData.newPlainText(null, text.trim()));
    }

    /**
     * intent是否存在
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context context, final Intent intent) {
        final PackageManager packageManager = context.getPackageManager();

        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 判断某个APP是否已安装
     *
     * @param context
     * @param appPackageName
     * @return
     */
    public static boolean isClientInstalled(Context context, String appPackageName) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setPackage(appPackageName);
        i.setType("image/*");
        PackageManager pm = context.getPackageManager();
        @SuppressLint("WrongConstant")
        List<?> ris = pm.queryIntentActivities(i, PackageManager.GET_ACTIVITIES);
        return ris != null && ris.size() > 0;
    }

    /**
     * 检查系统中是否安装了某个应用
     *
     * @param context     你懂的
     * @param packageName 应用的包名
     * @return true表示已安装，否则返回false
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> installedList = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        Iterator<PackageInfo> iterator = installedList.iterator();

        PackageInfo info;
        String name;
        while (iterator.hasNext()) {
            info = iterator.next();
            name = info.packageName;
            if (name.contains(packageName))  //改为contains，而不用equals
            {
                return true;
            }
        }
        return false;
    }


    /**
     * 根据进程pid获取进程名
     *
     * @param context
     * @param pid
     * @return
     */
    public static String getProcessName(Context context, int pid) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcessInfoList = am.getRunningAppProcesses();
        if (null != runningProcessInfoList && !runningProcessInfoList.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcessInfoList) {
                if (pid == processInfo.pid) {
                    processName = processInfo.processName;
                    break;
                }
            }
        }

        return processName;
    }

    public static boolean isMainProcess(Context context) {
        String packageName = getPackageName(context);
        String processName = getProcessName(context, android.os.Process.myPid());
//        AspLog.d("jsonjson", "packageName:" + packageName + "   processName:" + processName + " " + packageName.equals(processName));
        if (packageName.equals(processName)) {
            return true;
        }

        return false;
    }

    /**
     * 键盘是否显示
     *
     * @param rootView
     * @return
     */
    public boolean isSoftInputShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }


    public static String getUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        int checkPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
        } else {
            tmDevice = "";
            tmSerial = "";
        }

        androidId = "" + getAndroidId(context);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uuid = deviceUuid.toString();
        int index = uuid.lastIndexOf('-') + 1;
        if (index > 1) {
            return uuid.substring(index);
        }
        return uuid.substring(uuid.length() - 12);
    }

    /**
     * 目录的总空间
     *
     * @param path
     * @return
     */
    private static long getStatFsTotalSize(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            path = file.getPath();
        }

        StatFs statFs = new StatFs(path);
        long blockSize = 0l;
        long blockCount = 0l;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            blockCount = statFs.getBlockCountLong();
        } else {
            blockSize = statFs.getBlockSize();
            blockCount = statFs.getBlockCount();
        }
        long totalSize = blockSize * blockCount;
        Log.d(TAG, "getStatFsTotalSize path:" + path + " totalSize:" + totalSize);

        return totalSize;
    }

    /**
     * 目录的可用空间
     *
     * @param path
     * @return
     */
    private static long getStatFsAvailableSize(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            path = file.getPath();
        }

        StatFs statFs = new StatFs(path);
        long blockSize = 0l;
        long blockCount = 0l;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = statFs.getBlockSizeLong();
            blockCount = statFs.getAvailableBlocksLong();
        } else {
            blockSize = statFs.getBlockSize();
            blockCount = statFs.getAvailableBlocks();
        }
        long totalSize = blockSize * blockCount;
        Log.d(TAG, "getStatFsAvailableSize path:" + path + " availableSize:" + totalSize);

        return totalSize;
    }

    /**
     * 系统可用空间
     *
     * @return
     */
    public static long getSystemAvailableSize() {
        return getStatFsAvailableSize(Environment.getDataDirectory().getPath());
    }

    /**
     * SDCard 可用空间
     *
     * @return
     */
    public static long getSDCardAvailableSize() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return getStatFsAvailableSize(Environment.getExternalStorageDirectory().getPath());
        }

        return 0;
    }

    public static Intent getTextFileIntent(String filePath, boolean localFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (localFile) {
            Uri uriLocal = Uri.fromFile(new File(filePath));
            intent.setDataAndType(uriLocal, "text/plain");
        } else {
            Uri uri = Uri.parse(filePath);
            intent.setDataAndType(uri, "text/plain");
        }

        return intent;
    }

    /**
     * intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
     * intent.setDataAndType(uri, "application/vnd.ms-excel");
     * intent.setDataAndType(uri, "application/msword");
     *
     * @param context
     * @param filePath
     * @param type
     */
    public static Intent getFileIntent(Context context, String filePath, String type) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, type);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            return intent;
        }

        return null;
    }

    public static Intent getFileIntent(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        String fileName = file.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase();
        return getFileIntent(context, filePath, ext);
    }

    public static void openFileIntent(Context context, String filePath) {
        Intent intent = getFileIntent(context, filePath);
        if (null != intent) {
            context.startActivity(intent);
        }
    }

    /**
     * 屏幕的高度
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口高度
        int screenHeight = dm.heightPixels;

        return screenHeight;
    }

    /**
     * 屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        dm = context.getResources().getDisplayMetrics();

        //窗口高度
        int screenHeight = dm.heightPixels;

        return screenHeight;
    }

    /**
     * 屏幕密度
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        dm = context.getResources().getDisplayMetrics();

        //窗口高度
        float screendensity = dm.density;

        return screendensity;
    }

    /**
     * 屏幕的宽度
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口高度
        int screenWidth = dm.widthPixels;

        return screenWidth;
    }

    /**
     * 屏幕的宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        dm = context.getResources().getDisplayMetrics();

        //窗口高度
        int screenWidth = dm.widthPixels;

        return screenWidth;
    }

    /**
     * 应用区域的高度
     *
     * @param activity
     * @return
     */
    public static int getAppDisplayHeight(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);

        return outRect.height();
    }

    /**
     * 应用区域的宽度
     *
     * @param activity
     * @return
     */
    public static int getAppDisplayWidth(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);

        return outRect.width();
    }


    public static void hideSoftInput(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static void hideSoftInput(Context context, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != imm) {
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0); //强制隐藏键盘
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        int size = serviceList.size();

        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 是否运行在前台
     *
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        AspLog.d(TAG, " package:" + currentPackageName + " my packname:" + getPackageName(context));
        if (currentPackageName != null && currentPackageName.equals(getPackageName(context))) {
            return true;
        }
        return false;
    }



    /**
     * 判断是否开启通知，兼容各个版本
     * @param context
     * @return
     */
    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return isEnableV26(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return isEnableV19(context);
        } else {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            Class appOpsClass = null;
            /* Context.APP_OPS_MANAGER */
            try {

                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE,
                        String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static void killApplication(Context context, String packgerName) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(am, packgerName);//packageName是需要强制停止的应用程序包名
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取默认的输入法包名
     *
     * @param context
     * @return
     */
    public static String getDefaultInputMethodPkgName(Context context) {
        String mDefaultInputMethodPkg = null;

        String mDefaultInputMethodCls = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.DEFAULT_INPUT_METHOD);
        //输入法类名信息
        Log.d(TAG, "mDefaultInputMethodCls=" + mDefaultInputMethodCls);
        if (!TextUtils.isEmpty(mDefaultInputMethodCls)) {
            //输入法包名
            mDefaultInputMethodPkg = mDefaultInputMethodCls.split("/")[0];
            Log.d(TAG, "mDefaultInputMethodPkg=" + mDefaultInputMethodPkg);
        }
        return mDefaultInputMethodPkg;
    }


    /**
     * 若触宝输入法已安装，则设其为系统默认输入法
     * (写入Android系统数据库)
     * inputType 为输入法的类型：如搜狗为：sogou
     */
    public static void setDefaultInputMethod(Context context, String inputType) {
        //获取系统已安装的输入法ID
        ArrayList<String> methodlist = getInputMethodIdList(context);
        if (methodlist == null || methodlist.size() == 0) {
            LogUtil.e("输入法不存在");
            return;
        }

        //检查是否安装触宝输入法
        //触宝输入法ID "com.cootek.smartinputv5/com.cootek.smartinput5.TouchPalIME";
        String targetKeyword = inputType;  //这里以搜狗为例
        String value = "";
        for (String m : methodlist) {
            if (m.toLowerCase().contains(targetKeyword.toLowerCase())) {
                value = m;//找到触宝输入法
            }
        }
        if (value == "") {
            return;
        }

        //设置默认输入法
        String key = Settings.Secure.DEFAULT_INPUT_METHOD;
        boolean success = Settings.Secure.putString(context.getContentResolver(), key, value);
        LogUtil.e("：" + String.format("输入法设置,result: %s", value, success));

    }

    /**
     * 获取系统已安装的输入法ID
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getInputMethodIdList(Context context) {
        ArrayList<String> methodlist = new ArrayList<>();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.getInputMethodList() != null) {
            for (int i = 0; i < imm.getInputMethodList().size(); i++) {
                methodlist.add(imm.getInputMethodList().get(i).getId());

            }
            return methodlist;
        }
        return null;
    }

}
