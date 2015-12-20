package core_lib.simple_network_engine.net_layer.domainbean;

import java.util.Map;

import core_lib.simple_network_engine.engine_helper.interfaces.IPostDataPackage;
import core_lib.simple_network_engine.net_layer.INetRequestHandle;

public interface IHttpRequestForDomainBean {
    /**
     * 发起一个业务Bean的http请求
     *
     * @param urlString                                  完整的URL
     * @param httpMethod                                 http method
     * @param httpHeaders                                http headers
     * @param httpParams                                 数据字典(需要传递到服务器的参数列表)
     * @param customPostDataPackageHandler               自定义的POST数据打包函数
     * @param domainBeanRequestAsyncHttpResponseListener 异步响应回调
     * @return 控制本次网络请求的操作句柄
     */
    public INetRequestHandle requestDomainBean(final String urlString,
                                               final String httpMethod,
                                               final Map<String, String> httpHeaders,
                                               final Map<String, String> httpParams,
                                               final IPostDataPackage customPostDataPackageHandler,
                                               final IDomainBeanRequestAsyncHttpResponseListener domainBeanRequestAsyncHttpResponseListener);
}
