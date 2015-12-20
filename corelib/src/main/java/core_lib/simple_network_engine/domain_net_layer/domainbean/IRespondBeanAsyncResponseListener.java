package core_lib.simple_network_engine.domain_net_layer.domainbean;

import core_lib.simple_network_engine.error_bean.ErrorBean;

/**
 * "业务网络层" 对外提供的接口, 用于 "业务Bean网络请求" 的异步响应
 *
 * @author zhihua.tang
 */
public abstract class IRespondBeanAsyncResponseListener<NetRespondBean> {

    /**
     * 网络请求激活成功(可以在这个回调中, 显示 ProgressDialog 之类的UI)
     */
    public void onBegin() {

    }


    /**
     * 成功 (运行在后台线程)
     *
     * @param respondBean 一个符合要求的业务Bean
     */
    public void onSuccessInBackground(final NetRespondBean respondBean) {

    }

    /**
     * 成功 (运行在UI线程)
     *
     * @param respondBean 一个符合要求的业务Bean
     */
    public void onSuccess(final NetRespondBean respondBean) {

    }

    /**
     * 失败
     *
     * @param errorBean 包含错误信息的Bean
     */
    public void onFailure(final ErrorBean errorBean) {

    }

    /**
     * 取消
     */
    // public void onCancel();

    /**
     * 网络请求彻底完成(可以在这个回调中, 关闭 ProgressDialog 之类的UI)
     */
    public void onEnd(boolean isSuccess) {

    }
}
