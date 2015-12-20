package core_lib.toolutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.skyduck.corelib.R;
import core_lib.global_data_cache.ApplicationSingleton;

/**
 * 这里只放置, 在当前项目中会被用到的方法
 *
 * @author zhihua.tang
 */
public final class ToolsFunctionForThisProgect {
    public static final String TAG = ToolsFunctionForThisProgect.class.getSimpleName();

    private ToolsFunctionForThisProgect() {
        throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
    }

    /**
     * 在 onKeyDown() 方法中,调用此方法, 就完成对退出app的逻辑处理
     *
     * @param activity
     * @param keyCode
     * @param event
     */
    public static synchronized boolean onKeyDownForFinishApp(final Activity activity, final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(activity).setTitle("提示")
                    .setMessage("是否要退出应用程序?")
                    .setNegativeButton("取消", new
                            DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setPositiveButton("确定", new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            quitApp(activity);
                        }
                    }).show();
            return true;
        } else {
            return false;
        }
    }

    private static synchronized void stopServiceWithThisApp() {
        // Intent intent = new Intent(MyApplication.getApplication(),
        // StartServiceForPreLoadedData.class);
        // MyApplication.getApplication().stopService(intent);
    }

    /**
     * 退出app的方法
     *
     * @param activity
     */
    public static synchronized void quitApp(final Activity activity) {
        activity.finish();

        // 停止当前app相关的service
        stopServiceWithThisApp();
        // 在这里保存数据


        // 完整退出应用
        Intent startMain = new Intent(Intent.ACTION_MAIN);// 跳转到系统桌面
        // Intent中的Category属性是一个执行动作Action的附加信息。比如：CATEGORY_HOME则表示回到Home界面,这里的home界面应该是系统的Launcher界面
        startMain.addCategory(Intent.CATEGORY_HOME);// 启动Home应用程序
        startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 清除缓存的Activity
        activity.startActivity(startMain);
        System.exit(0);
    }

    // 获取当前设备的UA信息
    public static synchronized String getUserAgent() {
        // app名称 : Hanmi
        String bundleName = "KalendsCheBao";
        // app当前版本号 : 1.1.2
        String version = getVersionName();
        // 当前设备型号
        String platFormHardware = Build.MODEL + Build.VERSION.RELEASE;
        String platFormOSversion = "Android" + Build.VERSION.RELEASE;
        // HanmiBook_1.1.0-SNAPSHOT_MI 2S4.1.1_Android4.1.1
        String userAgent = bundleName + "_" + version + "_" + platFormHardware + "_" + platFormOSversion;
        return userAgent;
    }

    /**
     * 获取版本的android:versionName
     *
     * @return
     * @throws Exception
     */
    public static String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = ApplicationSingleton.getInstance.getApplication().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ApplicationSingleton.getInstance.getApplication().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String versionName = packInfo.versionName;
        return versionName;
    }

    public static int getVersionCode() {
        // 获取packagemanager的实例
        PackageManager packageManager = ApplicationSingleton.getInstance.getApplication().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ApplicationSingleton.getInstance.getApplication().getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int versionCode = packInfo.versionCode;
        return versionCode;
    }

    /**
     * 获取友盟渠道标识
     *
     * @return
     */
    public static String getUmengChannel() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = ApplicationSingleton.getInstance.getApplication().getPackageManager().getApplicationInfo(ApplicationSingleton.getInstance.getApplication().getPackageName(), PackageManager.GET_META_DATA);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String msg = appInfo.metaData.getString("UMENG_CHANNEL");
        return msg;
    }

    // 统计目录中全部文件总大小的方法
    public static long getDirectorySize(final File file) {
        long size = 0;
        if (file.isFile()) {
            // 如果是文件，获取文件大小累加
            size += file.length();
        } else if (file.isDirectory()) {
            // 获取目录中的文件及子目录信息
            File[] listFiles = file.listFiles();
            for (File subFile : listFiles) {
                // 调用递归遍历listFiles数组中的每一个对象
                size += getDirectorySize(subFile);
            }
        }
        return size;
    }

    /**
     * 获取系统 Configuration 类
     *
     * @return
     */
    public static Configuration getConfiguration() {
        return ApplicationSingleton.getInstance.getApplication().getResources().getConfiguration();
    }

    /**
     * 开关软键盘
     */
    public static void swithSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) ApplicationSingleton.getInstance.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    public static int getWindowWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getWindowHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static float getWindowDensity(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * 检查是否是 "手机号码"
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(14[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 检查是否是 "邮箱"
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * 检查是否是 "车牌号码"
     *
     * @param platNumber
     * @return
     */
    public static boolean isPlateNumber(String platNumber) {
        Pattern p = Pattern.compile("^[A-Za-z]{1}[a-zA-Z_0-9]{5}$");
        Matcher m = p.matcher(platNumber);
        return m.matches();
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfWithSecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdfWithMinute = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String formatTime(Date date) {
        String dateString = "";

        try {
            dateString = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String formatTimeWithSecond(Date date) {
        String dateString = "";

        try {
            dateString = sdfWithSecond.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public static String formatTimeWithMinute(Date date) {
        String dateString = "";

        try {
            dateString = sdfWithMinute.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateString;
    }

    public final static String NETWORK_STATE_NOTHING = "network_state_nothing";
    public final static String NETWORK_STATE_MOBILE = "network_state_mobile";
    public final static String NETWORK_STATE_WIFI = "network_state_wifi";
    public final static String NETWORK_STATE_OTHER = "network_state_other";


    public static File getCameraDir() {

        File cameraDir = new File(SimpleStorageUtilTools.getSDCardDir(), "/" + Environment.DIRECTORY_DCIM + "/Camera");
        if (!cameraDir.exists()) {

            cameraDir.mkdirs();
        }

        return cameraDir;
    }


    /**
     * 此方法返回的最大内存量，虚拟机将尝试使用，以字节为单位
     *
     * @return
     */
    public static long getRuntimeMaxMemory() {

        return Runtime.getRuntime().maxMemory();
    }

    public static String getIMEI() {

        String imei = "";
        try {

            Context ctx = ApplicationSingleton.getInstance.getApplication().getApplicationContext();
            TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {

                imei = telephonyManager.getDeviceId();
                if (TextUtils.isEmpty(imei))
                    imei = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);

                if (imei == null)
                    imei = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return imei;
    }

    /**
     * @param packageName
     * @return
     */
    public static boolean hasApp(String packageName) {

        if (TextUtils.isEmpty(packageName))
            return false;

        PackageInfo packageInfo = null;
        try {

            packageInfo = ApplicationSingleton.getInstance.getApplication().getApplicationContext().getPackageManager().getPackageInfo(packageName, 0);

        } catch (NameNotFoundException e) {


            e.printStackTrace();
        } catch (Exception e) {


            e.printStackTrace();
        }

        return packageInfo == null ? false : true;
    }

    /**
     * @return
     */
    public static boolean hasGoogleMapApp() {

        return hasApp("com.google.android.apps.maps");
    }

    /**
     * 判断网络是否可用
     *
     * @return
     */
    public static boolean isNetworkEnable() {

        ConnectivityManager conManager = (ConnectivityManager) ApplicationSingleton.getInstance.getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    /**
     * 判断网络是否不可用
     *
     * @return
     */
    public static boolean isNetworkDisable() {

        ConnectivityManager conManager = (ConnectivityManager) ApplicationSingleton.getInstance.getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo == null || !networkInfo.isAvailable();
    }

    /**
     * 获取当前网络状态
     *
     * @return
     */
    public static String getNetworkState() {

        ConnectivityManager cm = (ConnectivityManager) ApplicationSingleton.getInstance.getApplication().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            return NETWORK_STATE_NOTHING;
        } else {

            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NETWORK_STATE_MOBILE;
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_STATE_WIFI;
            } else {
                return NETWORK_STATE_OTHER;
            }
        }
    }


    public static int getScreenWidth() {

        return ApplicationSingleton.getInstance.getApplication().getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {

        return ApplicationSingleton.getInstance.getApplication().getApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * add by Daisw
     *
     * @return 状态栏的高度
     */
    public static int getStatusBarHeight() {

        int height = 0;

        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int id = Integer.parseInt(field.get(obj).toString());
            height = ApplicationSingleton.getInstance.getApplication().getApplicationContext().getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
        }

        return height;
    }

    public static boolean hasSinaWeiboClient() {
        try {

            PackageInfo packageInfo = ApplicationSingleton.getInstance.getApplication().getApplicationContext().getPackageManager().getPackageInfo("com.sina.weibo", 0);
            if (packageInfo == null)
                return false;

            int highBit = packageInfo.versionName.charAt(0);
            return highBit > 50 ? true : false;// 50 = 2

        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断是否有电话功能
     *
     * @return true:有电话功能
     * false:没有电话功能
     */
    public static boolean hasPhone() {

        TelephonyManager telephony = (TelephonyManager) ApplicationSingleton.getInstance.getApplication().getApplicationContext().
                getSystemService(Context.TELEPHONY_SERVICE);
        return telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE ? false : true;
    }
}
