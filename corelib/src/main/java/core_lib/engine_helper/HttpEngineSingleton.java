package core_lib.engine_helper;

import java.io.File;
import java.util.Map;

import core_lib.http_engine.soap.HttpEngineOfSoap;
import core_lib.http_engine.volley.HttpEngineOfVolley;
import core_lib.simple_network_engine.engine_helper.interfaces.IPostDataPackage;
import core_lib.simple_network_engine.net_layer.INetLayerInterface;
import core_lib.simple_network_engine.net_layer.INetRequestHandle;
import core_lib.simple_network_engine.net_layer.domainbean.IDomainBeanRequestAsyncHttpResponseListener;
import core_lib.simple_network_engine.net_layer.file.IFileRequestAsyncHttpResponseListener;
import core_lib.toolutils.DebugLog;

/*
 * Every method of java will have a stack, and every invokation on that method will have it's own 'stack frame'. 
 * 每一个java方法都有一个堆栈, 而且对那个方法每次调用时, 都会有属于它自己的 'stack frame'.
 * So the local data of one method invokation will not affect others. 
 * 所以一个方法被调用时的本地数据, 不会影响到别人.
 * Please do not confuse 'synchronization' with 'atomic'. 
 * 请不要混淆 '同步' 和 '原子性'
 * If one static method is synchronized, JVM will use the Class as the lock. If not, it acts as an instance method.
 * 如果一个静态方法是同步的, JVM会使用类锁. 如果不, 它会被当成一个实力方法.
 * */

/**
 * http 引擎层
 *
 * @author zhihua.tang
 */
public enum HttpEngineSingleton implements INetLayerInterface {
    getInstance;

    private final String TAG = this.getClass().getSimpleName();


    @Override
    public INetRequestHandle requestDownloadFile(final String url, final boolean isNeedContinuingly,
                                                 final String requestMethod, final Map<String, String> params,
                                                 final File downLoadFile,
                                                 final IFileRequestAsyncHttpResponseListener fileRequestAsyncHttpResponseListener) {
        DebugLog.e(TAG, "当前使用的HTTP网络引擎是 --> " + "AsyncHttpClient");
        return null;
    }

    private final static HttpEngineOfVolley httpEngineOfVolley = new HttpEngineOfVolley();
    private final static HttpEngineOfSoap httpEngineOfSoap = new HttpEngineOfSoap();

    @Override
    public INetRequestHandle requestDomainBean(final String urlString, final String httpMethod,
                                               final Map<String, String> httpHeaders, final Map<String, String> httpParams,
                                               final IPostDataPackage customPostDataPackageHandler,
                                               final IDomainBeanRequestAsyncHttpResponseListener domainBeanRequestAsyncHttpResponseListener) {

        DebugLog.e(TAG, "当前使用的HTTP网络引擎是 --> " + "Soap");
        return httpEngineOfSoap.requestDomainBean(urlString, httpMethod, httpHeaders, httpParams,
                customPostDataPackageHandler, domainBeanRequestAsyncHttpResponseListener);
    }

    @Override
    public String getLocalizedFailureDescriptionForDomainBean(int statusCode, Throwable e) {
        return httpEngineOfSoap.getLocalizedFailureDescriptionForDomainBean(statusCode, e);
    }

    @Override
    public String getLocalizedFailureDescriptionForFile(int statusCode, Throwable e) {
        return "";
    }

    @Override
    public INetRequestHandle requestUploadFile(final String urlString,
                                               final Map<String, String> params,
                                               final String uploadFileKey,
                                               final File uploadFile,
                                               final IFileRequestAsyncHttpResponseListener fileRequestAsyncHttpResponseListener) {
        return null;
    }
}
