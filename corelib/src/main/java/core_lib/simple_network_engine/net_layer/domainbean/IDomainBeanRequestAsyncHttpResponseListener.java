package core_lib.simple_network_engine.net_layer.domainbean;

import core_lib.simple_network_engine.net_layer.INetRequestIsCancelled;

/**
 * "网络层" 对外提供的接口, 用于 "业务Bean网络请求" 的异步响应
 *
 * @author zhihua.tang
 */
public interface IDomainBeanRequestAsyncHttpResponseListener {
    /**
     * 请求成功
     *
     * @param netRequestIsCancelled
     * @param response
     */
    public void onSuccess(final INetRequestIsCancelled netRequestIsCancelled, final Object response);

    /**
     * 请求失败
     *
     * @param netRequestIsCancelled
     * @param statusCode
     * @param e
     */
    public void onFailure(final INetRequestIsCancelled netRequestIsCancelled, int statusCode, Throwable e);
}
