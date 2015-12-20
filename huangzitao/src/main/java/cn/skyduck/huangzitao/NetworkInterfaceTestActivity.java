package cn.skyduck.huangzitao;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import core_lib.domainbean_model.login.LoginNetRequestBean;
import core_lib.domainbean_model.login.LoginNetRespondBean;
import core_lib.simple_network_engine.domain_net_layer.SimpleNetworkEngineSingleton;
import core_lib.simple_network_engine.domain_net_layer.domainbean.IRespondBeanAsyncResponseListener;
import core_lib.simple_network_engine.error_bean.ErrorBean;
import core_lib.simple_network_engine.net_layer.INetRequestHandle;
import core_lib.simple_network_engine.net_layer.NetRequestHandleNilObject;
import core_lib.toolutils.SimpleProgressDialogTools;

public class NetworkInterfaceTestActivity extends Activity {
    private String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_interface_test);

        ButterKnife.bind(this);

    }


    // 登录接口的网络请求操作句柄
    private INetRequestHandle netRequestHandleForLogin = new NetRequestHandleNilObject();

    @OnClick(R.id.button0)
    void onButtonClick0(View view) {

        // 创建目标接口的 网络请求业务Bean
        LoginNetRequestBean netRequestBean = new LoginNetRequestBean("13681101304", "123456");


        netRequestHandleForLogin = SimpleNetworkEngineSingleton.getInstance.requestDomainBean(netRequestBean, new IRespondBeanAsyncResponseListener<LoginNetRespondBean>() {
            @Override
            public void onBegin() {
                // 激活一个 网络请求提示框
                SimpleProgressDialogTools.show(NetworkInterfaceTestActivity.this, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // 取消本次网络请求
                        netRequestHandleForLogin.cancel();
                    }
                });
            }

            @Override
            public void onSuccess(LoginNetRespondBean loginNetRespondBean) {
                Toast.makeText(NetworkInterfaceTestActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ErrorBean errorBean) {
                Toast.makeText(NetworkInterfaceTestActivity.this, errorBean.getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEnd(boolean isSuccess) {
                // 关闭 网络请求提示框
                SimpleProgressDialogTools.dismiss(NetworkInterfaceTestActivity.this);
            }
        });
    }
}
