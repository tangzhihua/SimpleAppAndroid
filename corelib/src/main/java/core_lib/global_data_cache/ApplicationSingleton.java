package core_lib.global_data_cache;

import android.app.Application;

/**
 * Created by zhihuatang on 15/8/26.
 */
public enum ApplicationSingleton {
    getInstance;

    private Application application;

    public Application getApplication() {
        return application;
    }

    public void init(Application application) {
        assert this.application == null : "不能重复调用初始化方法.";
        this.application = application;

    }
}
