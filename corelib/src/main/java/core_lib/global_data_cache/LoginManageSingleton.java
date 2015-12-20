package core_lib.global_data_cache;

import java.io.File;
import java.util.List;

import core_lib.domainbean_model.login.LoginNetRequestBean;
import core_lib.domainbean_model.login.LoginNetRespondBean;
import core_lib.simple_network_engine.domain_net_layer.SimpleNetworkEngineSingleton;
import core_lib.simple_network_engine.domain_net_layer.domainbean.IRespondBeanAsyncResponseListener;
import core_lib.simple_network_engine.error_bean.ErrorBean;
import core_lib.simple_network_engine.net_layer.INetRequestHandle;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by tangzhihua on 15/9/6.
 */
public enum LoginManageSingleton {
    getInstance;

    private final String TAG = this.getClass().getSimpleName();

    /**
     * 模块初始化方法, 使用模块前要先调用此方法(不能重复调用)
     */
    public void init() {
        latestLoginNetRespondBean = GlobalDataCacheForDiskTools.getLatestLoginNetRespondBean();
        if (latestLoginNetRespondBean != null) {
            /* 注意 : 防止用户删除了SD卡上的缓存目录, 所以每次启动时都重新创建一遍当前登录用户的本地数据目录 */
            // 创建用户相关的各种数据目录
            makeUserDirsWithUserID(latestLoginNetRespondBean.getUid());


        }
    }

    // 用户登录成功后, 服务器返回的 "用户信息", 可以通过判断这个属性是否为空来判断当前是否有用户处于登录状态
    private LoginNetRespondBean latestLoginNetRespondBean;

    public LoginNetRespondBean getLatestLoginNetRespondBean() {
        return latestLoginNetRespondBean;
    }

    /**
     * 登录请求
     *
     * @param loginNetRequestBean
     * @param domainBeanAsyncHttpResponseListener
     * @return
     */
    public INetRequestHandle login(final LoginNetRequestBean loginNetRequestBean, final IRespondBeanAsyncResponseListener<LoginNetRespondBean> domainBeanAsyncHttpResponseListener) {
        return SimpleNetworkEngineSingleton.getInstance.requestDomainBean(loginNetRequestBean, new IRespondBeanAsyncResponseListener<LoginNetRespondBean>() {
            @Override
            public void onBegin() {
                domainBeanAsyncHttpResponseListener.onBegin();
            }

            @Override
            public void onSuccessInBackground(LoginNetRespondBean loginNetRespondBean) {
                // 内存缓存
                latestLoginNetRespondBean = loginNetRespondBean;
                // 硬盘缓存
                GlobalDataCacheForDiskTools.setLatestLoginNetRespondBean(loginNetRespondBean);

                // 创建用户相关的各种数据目录
                makeUserDirsWithUserID(loginNetRespondBean.getUid());


                // 通知外部
                domainBeanAsyncHttpResponseListener.onSuccessInBackground(loginNetRespondBean);
            }

            @Override
            public void onSuccess(LoginNetRespondBean loginNetRespondBean) {
                domainBeanAsyncHttpResponseListener.onSuccess(loginNetRespondBean);
            }

            @Override
            public void onFailure(ErrorBean errorBean) {
                domainBeanAsyncHttpResponseListener.onFailure(errorBean);
            }

            @Override
            public void onEnd(boolean isSuccess) {
                domainBeanAsyncHttpResponseListener.onEnd(isSuccess);
            }
        });
    }

    /**
     * 登出请求
     */
    public void logout() {
        latestLoginNetRespondBean = null;
        GlobalDataCacheForDiskTools.setLatestLoginNetRespondBean(null);


    }

    /**
     * 创建当前用户相关的各种缓存目录
     */
    private void makeUserDirsWithUserID(final String userId) {
        List<File> directories = newArrayList();
        // "当前用户" 数据缓存目录的根目录
        directories.add(new File(LocalCacheDataPathConstantTools.localCacheUserDataRootPathInSDCard().getPath() + "/" + userId));

    }
}
