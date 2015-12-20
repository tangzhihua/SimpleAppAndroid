package cn.skyduck.huangzitao.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.skyduck.huangzitao.R;
import cn.skyduck.huangzitao.controls.TitleBar;
import core_lib.domainbean_model.login.LoginNetRequestBean;
import core_lib.domainbean_model.login.LoginNetRespondBean;
import core_lib.global_data_cache.LoginManageSingleton;
import core_lib.simple_network_engine.domain_net_layer.domainbean.IRespondBeanAsyncResponseListener;
import core_lib.simple_network_engine.error_bean.ErrorBean;
import core_lib.simple_network_engine.net_layer.INetRequestHandle;
import core_lib.simple_network_engine.net_layer.NetRequestHandleNilObject;
import core_lib.toolutils.SimpleProgressDialogTools;

/**
 * 登录界面
 */
public class LoginActivity extends Activity {

    @Bind(R.id.userName_editText)
    EditText userNameEditText;
    @Bind(R.id.passWord_editText)
    EditText passWordEditText;
    @Bind(R.id.remember_password_checkBox)
    CheckBox rememberPasswordCheckBox;
    @Bind(R.id.forget_password_textView)
    TextView forgetPasswordTextView;
    @Bind(R.id.login_button)
    Button loginButton;
    @Bind(R.id.register_button)
    Button registerButton;
    @Bind(R.id.try_button)
    TextView tryButton;


    private INetRequestHandle netRequestHandleForLogin = new NetRequestHandleNilObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.try_button)
    void tryButtonOnClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.register_button)
    void registerButtonOnClick(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_button)
    void loginButtonOnClick(View v) {

        final String username = userNameEditText.getText().toString();
        final String password = passWordEditText.getText().toString();
        // 先判断用户输入的用户名和密码格式是否合法
        if (isUsernameAndPasswordFormatValid(username, password)) {
            // 如果合法, 才发起网络请求
            requestLogin(username, password);
        }
    }

    /**
     * 判断用户输入的用户名和密码格式是否合法
     *
     * @param username
     * @param password
     * @return
     */
    private boolean isUsernameAndPasswordFormatValid(String username, String password) {
        String errorMessage;

        do {
            if (TextUtils.isEmpty(username)) {
                errorMessage = "用户名不能为空";
                break;
            }

            if (TextUtils.isEmpty(password)) {
                errorMessage = "密码不能为空";
                break;
            }

            // 判断用户输入的用户名是否合法(合法的用户名是手机号码)
            // 判断用户输入的密码长度范围是否合法
            if (username.length() < 6 || password.length() > 8) {
                errorMessage = "密码长度范围在 6 ~ 8 位之间.";
                break;
            }

            // 一切OK
            return true;
        } while (false);

        // 用户输入非法
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 发起登录接口的网络访问
     *
     * @param username
     * @param password
     */
    private void requestLogin(String username, String password) {

        netRequestHandleForLogin = LoginManageSingleton.getInstance.login(new LoginNetRequestBean(username, password), new IRespondBeanAsyncResponseListener<LoginNetRespondBean>() {
            @Override
            public void onBegin() {
                SimpleProgressDialogTools.show(LoginActivity.this, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        netRequestHandleForLogin.cancel();
                    }
                });
            }

            @Override
            public void onSuccess(LoginNetRespondBean loginNetRespondBean) {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ErrorBean errorBean) {
                Toast.makeText(LoginActivity.this, errorBean.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEnd(boolean isSuccess) {
                SimpleProgressDialogTools.dismiss(LoginActivity.this);
            }
        });
    }
}
