package core_lib.global_data_cache;

import org.apache.http.client.CookieStore;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 需要全局缓存的数据(这里的属性还没做好多线程保护)
 */
public enum GlobalDataCacheForMemorySingleton {
    getInstance;

    // 加载最重要的缓存数据, 不加载完是不能进入app的
    // 这是个空方法, 实际调用的是实例块, 这样防止重复初始化
    public void init() {

    }

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();


    // 是否是第一次启动App的标志位
    private boolean isFirstStartApp;

    public boolean isFirstStartApp() {
        return isFirstStartApp;
    }

    public void setFirstStartApp(boolean isFirstStartApp) {
        this.isFirstStartApp = isFirstStartApp;

        GlobalDataCacheForDiskTools.setFirstStartAppMark(isFirstStartApp);
    }


    // TODO:实例块, 一定要放置在类的最后, 否则这里调用的时机有可能会先于属性定义
    // 使用实例块来加载最重要的缓存数据, 不加载完是不能进入app的
    { // 这些缓存数据是通过SharedPreferences来实现
        //
        this.isFirstStartApp = GlobalDataCacheForDiskTools.isFirstStartApp();
        //

    }

}
