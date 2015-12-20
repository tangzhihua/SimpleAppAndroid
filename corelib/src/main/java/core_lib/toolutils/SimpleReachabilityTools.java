package core_lib.toolutils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import core_lib.global_data_cache.ApplicationSingleton;
import core_lib.global_data_cache.GlobalDataCacheForMemorySingleton;

/**
 * 网络连通性检测
 *
 * @author zhihua.tang
 */
public final class SimpleReachabilityTools {
    private SimpleReachabilityTools() {
        throw new AssertionError("这个是一个工具类, 不能创建实例对象.");
    }

    public static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager = ((ConnectivityManager) ApplicationSingleton.getInstance.getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo();
    }

    public static boolean isReachable() {
        NetworkInfo currentNetworkInfo = getActiveNetworkInfo();
        return currentNetworkInfo != null && currentNetworkInfo.isAvailable();
    }

    /**
     * 3G
     *
     * @return
     */
    public static boolean isReachableViaWWAN() {
        NetworkInfo currentNetworkInfo = getActiveNetworkInfo();
        return currentNetworkInfo != null
                && currentNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * wifi
     *
     * @return
     */
    public static boolean isReachableViaWiFi() {
        NetworkInfo currentNetworkInfo = getActiveNetworkInfo();
        return currentNetworkInfo != null
                && currentNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean is2GMobileNetwork() {
        NetworkInfo currentNetworkInfo = getActiveNetworkInfo();
        return currentNetworkInfo != null
                && currentNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE
                && ((currentNetworkInfo.getSubtype() == 1) || (currentNetworkInfo.getSubtype() == 4) || (currentNetworkInfo
                .getSubtype() == 2));
    }

}
