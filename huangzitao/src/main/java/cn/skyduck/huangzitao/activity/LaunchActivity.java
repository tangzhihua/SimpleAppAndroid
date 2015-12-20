package cn.skyduck.huangzitao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import cn.skyduck.huangzitao.R;
import core_lib.global_data_cache.LoginManageSingleton;

/**
 * 启动界面
 */
public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        if (LoginManageSingleton.getInstance.getLatestLoginNetRespondBean() == null) {
            // 还未登录
            gotoLoginActivity();
        } else {
            // 已经登录
            gotoMainActivity();
        }
    }

    private void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

        finish();
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }
}
