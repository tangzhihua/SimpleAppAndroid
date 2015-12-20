package cn.skyduck.huangzitao;

import android.app.Application;
import android.content.res.Configuration;

import core_lib.global_data_cache.ApplicationSingleton;
import core_lib.global_data_cache.GlobalDataCacheForMemorySingleton;
import core_lib.global_data_cache.LocalCacheDataPathConstantTools;
import core_lib.global_data_cache.LoginManageSingleton;
import core_lib.global_data_cache.PrintDeviceInfoTools;
import core_lib.toolutils.DebugLog;
import core_lib.toolutils.area_query.SimpleAreaQueryTools;

/**
 * Created by zhihuatang on 15/8/25.
 */

public class MyApplication extends Application {
    private final String TAG = this.getClass().getSimpleName();

    // Application 类对外的接口
    private static MyApplication self;
    //
    public static Application getApplication() {
        assert false : "MyApplication 还未初始化完成, 不能调用Application.getApplication()方法.";
        return self;
    }

    @Override
    public void onCreate() {

        DebugLog.i(TAG, "onCreate");

        super.onCreate();

        /* 注意 : 设置全局的Application(必须在第1位) */
        ApplicationSingleton.getInstance.init(this);

        // 打印设备信息
        PrintDeviceInfoTools.printDeviceInfo(this);

        /* 注意 : 创建当前项目本地缓存目录(必须在第2位) */
        LocalCacheDataPathConstantTools.createLocalCacheDirectories();

        /* 注意 : 全局数据缓存模块初始化(必须在第3位) */
        GlobalDataCacheForMemorySingleton.getInstance.init();


        // Area查询工具类模块初始化
        SimpleAreaQueryTools.getInstance.init(this);

        // 用户登录管理模块初始化
        LoginManageSingleton.getInstance.init();
    }

    @Override
    public void onTerminate() {
        DebugLog.d(TAG, "onTerminate");
        // 父类方法, 必须调用
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        DebugLog.i(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        DebugLog.i(TAG, "onLowMemory");
        super.onLowMemory();
    }

}
