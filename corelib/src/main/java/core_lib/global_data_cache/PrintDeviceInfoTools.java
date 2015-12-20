package core_lib.global_data_cache;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import core_lib.toolutils.SimpleStorageUtilTools;
import core_lib.toolutils.SimpleUniqueIdentifierSingleton;

/**
 * Created by tangzhihua on 15/10/23.
 */
public final class PrintDeviceInfoTools {
    private static final String TAG = PrintDeviceInfoTools.class.getSimpleName();

    private PrintDeviceInfoTools() {
        throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
    }

    public static void printDeviceInfo(Application application) {
        StringBuffer sb = new StringBuffer();
        //
        sb.append("\n\n\n当前开发代号 : Build.VERSION.CODENAME --> " + android.os.Build.VERSION.CODENAME);
        sb.append("\n当前开发代号 : Build.VERSION.CODENAME --> " + android.os.Build.VERSION.CODENAME);
        sb.append("\n源码控制版本号 : Build.VERSION.INCREMENTAL --> " + android.os.Build.VERSION.INCREMENTAL);
        sb.append("\n版本字符串 : Build.VERSION.RELEASE --> " + android.os.Build.VERSION.RELEASE);
        sb.append("\n版本号 : Build.VERSION.SDK_INT --> " + android.os.Build.VERSION.SDK_INT + "");
        //
        sb.append("\ndeviceId --> " + SimpleUniqueIdentifierSingleton.getInstance.getDeviceUniqueIdentifierString());
        sb.append("\ndeviceModel --> " + android.os.Build.MODEL);
        //
        TelephonyManager tm = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
        sb.append("\n手机号码 --> " + tm.getLine1Number());
        sb.append("\nDeviceId(IMEI) --> " + tm.getDeviceId());
        sb.append("\n运营商名称 --> " + tm.getNetworkOperatorName());
        sb.append("\nsim卡序列号 --> " + tm.getSimSerialNumber());
        sb.append("\nIMSI --> " + tm.getSubscriberId());
        sb.append("\nsim卡所在国家" + tm.getNetworkCountryIso());
        sb.append("\n运营商编号 --> " + tm.getNetworkOperator());
        sb.append("\nDeviceSoftwareVersion --> " + tm.getDeviceSoftwareVersion());
        sb.append("\nNetworkType --> " + tm.getNetworkType() + "");
        //
        sb.append("\nSD卡是否可写 --> " + Boolean.toString(SimpleStorageUtilTools.isExternalStoreWritable()));
        sb.append("\nSD卡是否读取 --> " + Boolean.toString(SimpleStorageUtilTools.isExternalStoreReadable()));
        sb.append("\nSD卡总容量 --> " + SimpleStorageUtilTools.getSDCardTotalSize() + "");
        sb.append("\nSD卡剩余容量 --> " + SimpleStorageUtilTools.getSDCardFreeSize() + "");
        sb.append("\n设备总容量 --> " + SimpleStorageUtilTools.getDeviceStorageTotalSize() + "");
        sb.append("\n设备剩余容量 --> " + SimpleStorageUtilTools.getDeviceStorageFreeSize() + "\n\n\n\n");

        sb.append("\nAndroid 数据目录 --> " + Environment.getDataDirectory().getAbsolutePath());
        sb.append("\nAndroid 下载/缓存内容目录 --> " + Environment.getDownloadCacheDirectory().getAbsolutePath());
        sb.append("\nAndroid 获取外部存储目录 --> " + Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append("\nAndroid Android 的根目录 --> " + Environment.getRootDirectory().getAbsolutePath());


        Log.e(TAG, "");
        Log.e(TAG, "");
        Log.e(TAG, "-----------------------------   设备信息   -----------------------------");
        Log.e("PrintDeviceInfoTools", sb.toString());
        Log.e(TAG, "-----------------------------   设备信息   -----------------------------");
        Log.e(TAG, "");
        Log.e(TAG, "");
        Log.e(TAG, "");
    }
}
