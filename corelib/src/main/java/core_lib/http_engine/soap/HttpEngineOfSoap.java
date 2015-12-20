package core_lib.http_engine.soap;


import android.os.Handler;

import java.io.File;
import java.util.Map;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


import core_lib.simple_network_engine.engine_helper.interfaces.IPostDataPackage;

import core_lib.simple_network_engine.error_bean.ErrorBean;
import core_lib.simple_network_engine.error_bean.ErrorCodeEnum;
import core_lib.simple_network_engine.error_bean.SimpleException;
import core_lib.simple_network_engine.net_layer.INetLayerInterface;
import core_lib.simple_network_engine.net_layer.INetRequestHandle;
import core_lib.simple_network_engine.net_layer.domainbean.IDomainBeanRequestAsyncHttpResponseListener;
import core_lib.simple_network_engine.net_layer.file.IFileRequestAsyncHttpResponseListener;
import core_lib.toolutils.DebugLog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


/**
 * Created by Cxy on 2015/11/24.
 */
public class HttpEngineOfSoap implements INetLayerInterface {
    private final String TAG = this.getClass().getSimpleName();

    // 固定大小的线程池
    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private Handler handler = new Handler();

    @Override
    public INetRequestHandle requestDomainBean(final String urlString,
                                               final String httpMethod,
                                               final Map<String, String> httpHeaders,
                                               final Map<String, String> httpParams,
                                               final IPostDataPackage customPostDataPackageHandler,
                                               final IDomainBeanRequestAsyncHttpResponseListener domainBeanRequestAsyncHttpResponseListener) {

        DebugLog.e(TAG, "--> requestDomainBean : 使用 Soap !");
        final HttpRequestHandleOfSoap requestHandle = new HttpRequestHandleOfSoap();
        final Future future = threadPool.submit(new Runnable() {
            @Override
            public void run() {
                DebugLog.e(TAG, "进入后台线程, 开始请求HTTP.");
                // 命名空间
                String nameSpace = "http://tempuri.org/";
                // 调用的方法名称
                String methodName = "XmlSubmit";
                // SOAP Action
                String soapAction = "http://tempuri.org/XmlSubmit";

                String responseData = null;
                try {
                    // 指定WebService的命名空间和调用的方法名
                    SoapObject soapObject = new SoapObject(nameSpace, methodName);
                    //处理soap12:Body数据部分
                    for (String key : httpParams.keySet()) {
                        soapObject.addProperty(key, httpParams.get(key));
                    }
                    DebugLog.e(TAG, "方法名 : " + methodName);
                    DebugLog.e(TAG, "发往服务器的数据 : " + httpParams.toString());
                    // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    envelope.dotNet = true; //指定webservice的类型的（java，PHP，dotNet）
                    envelope.bodyOut = soapObject;
                    envelope.setOutputSoapObject(soapObject);
                    // 指定HttpTransportSE
                    HttpTransportSE ht = new HttpTransportSE(urlString);
                    // 访问webservice服务器
                    ht.call(soapAction, envelope);
                    final Object bodyIn = envelope.bodyIn;
                    if (bodyIn == null) {
                        // 从服务器端获得的实体数据为空(EntityData), 这种情况有可能是正常的, 比如 退出登录 接口,
                        // 服务器就只是通知客户端访问成功, 而不发送任何实体数据.
                        throw new SimpleException(ErrorCodeEnum.Server_NoResponseData);
                    } else {
                        responseData = envelope.getResponse().toString();
                    }

                    domainBeanRequestAsyncHttpResponseListener.onSuccess(requestHandle, responseData);
                } catch (SimpleException e) {
                    domainBeanRequestAsyncHttpResponseListener.onFailure(requestHandle, e.getCode(), e);
                } catch (Exception e) {
                    DebugLog.e(TAG, "调用HttpEngine发起Http请求出现了异常-->" + e.toString());
                    domainBeanRequestAsyncHttpResponseListener.onFailure(requestHandle, ErrorCodeEnum.Client_NetLayerUnkonwError.getCode(), e);
                }
            }
        });

        requestHandle.setFuture(future);
        return requestHandle;
    }

    @Override
    public INetRequestHandle requestDownloadFile(String urlString, boolean isNeedContinuingly, String httpMethod, Map<String, String> params, File downLoadFile, IFileRequestAsyncHttpResponseListener fileRequestAsyncHttpResponseListener) {
        return null;
    }

    @Override
    public INetRequestHandle requestUploadFile(String urlString, Map<String, String> params, String uploadFileKey, File uploadFile, IFileRequestAsyncHttpResponseListener fileRequestAsyncHttpResponseListener) {
        return null;
    }

    @Override
    public String getLocalizedFailureDescriptionForDomainBean(int statusCode, Throwable e) {
        return null;
    }

    @Override
    public String getLocalizedFailureDescriptionForFile(int statusCode, Throwable e) {
        return null;
    }
}
