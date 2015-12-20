package core_lib.http_engine.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.Map;

import core_lib.global_data_cache.ApplicationSingleton;
import core_lib.simple_network_engine.engine_helper.interfaces.IPostDataPackage;
import core_lib.simple_network_engine.error_bean.ErrorCodeEnum;
import core_lib.simple_network_engine.net_layer.INetLayerInterface;
import core_lib.simple_network_engine.net_layer.INetRequestHandle;
import core_lib.simple_network_engine.net_layer.domainbean.IDomainBeanRequestAsyncHttpResponseListener;
import core_lib.simple_network_engine.net_layer.file.IFileRequestAsyncHttpResponseListener;
import core_lib.toolutils.DebugLog;

public class HttpEngineOfVolley implements INetLayerInterface {
    private final String TAG = this.getClass().getSimpleName();
    private final RequestQueue responseQueue;

    public HttpEngineOfVolley() {
        responseQueue = Volley.newRequestQueue(ApplicationSingleton.getInstance.getApplication());
        responseQueue.start();
    }

    @Override
    public INetRequestHandle requestDomainBean(String urlString, final String httpMethod,
                                               final Map<String, String> httpHeaders, final Map<String, String> httpParams,
                                               final IPostDataPackage customPostDataPackageHandler,
                                               final IDomainBeanRequestAsyncHttpResponseListener domainBeanRequestAsyncHttpResponseListener) {
        int methodForVolley = 0;
        if ("GET".equals(httpMethod)) {
            methodForVolley = Request.Method.GET;

            // TODO:volley对于传递中文参数时, get请求需要自己来完成URLEncode,
            // 目前盗用AsyncHttpClient的RequestParams, POST请求volley会进行encode
            final MyRequestParams paramsForGet = new MyRequestParams(httpParams);
            urlString = urlString + paramsForGet.getParamString();

            DebugLog.i(TAG, "网络层发起一个GET请求 --> \n" + urlString);
        } else {
            methodForVolley = Request.Method.POST;

            DebugLog.i(TAG, "网络层发起一个POST请求 --> \n" + urlString + httpParams);
        }
        final String urlStringForVolley = urlString;
        final HttpRequestHandleOfVolley requestHandleOfVolley = new HttpRequestHandleOfVolley();

        final StringRequest volleyRequest = new StringRequest(methodForVolley, urlString,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DebugLog.d(TAG, response.toString());
                        domainBeanRequestAsyncHttpResponseListener.onSuccess(requestHandleOfVolley, response);
                        requestHandleOfVolley.setFinished(true);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DebugLog.e(TAG, "HTTP引擎(Volley)访问错误------------------------->");
                DebugLog.e(TAG, "出错时的请求参数 : HTTP_METHOD =" + httpMethod + ", URL = "
                        + urlStringForVolley + ", Params = " + httpParams.toString());
                DebugLog.e(TAG, "VolleyError=" + error.getLocalizedMessage());
                int errorCode = ErrorCodeEnum.HTTP_Error.getCode();
                if (error.networkResponse != null) {
                    errorCode = error.networkResponse.statusCode;

                    DebugLog.e(TAG, "networkResponse.statusCode=" + error.networkResponse.statusCode);
                    DebugLog.e(TAG, "networkResponse.headers=" + error.networkResponse.headers);
                    DebugLog.e(TAG, "networkResponse.notModified=" + error.networkResponse.notModified);
                }
                DebugLog.e(TAG, "HTTP引擎(Volley)访问错误------------------------->");

                domainBeanRequestAsyncHttpResponseListener.onFailure(requestHandleOfVolley, errorCode,
                        error);
                requestHandleOfVolley.setFinished(true);
                DebugLog.e(TAG, "setFinished");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO:不过如果需要加密时, 怎么办呢?
                // DebugLog.i(TAG, "getParams() = " +
                // requestParamsDictionary.toString());
                return httpParams;
            }

            @Override
            public RetryPolicy getRetryPolicy() {// 网络超时时间
                // RetryPolicy retryPolicy = new
                // DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                // DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                // DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                // add by szk 超时的设置
                RetryPolicy retryPolicy = new DefaultRetryPolicy(10000, 0, 0f);
                return retryPolicy;
            }
        };

        requestHandleOfVolley.setRequestHandle(volleyRequest);
        responseQueue.add(volleyRequest);
        return requestHandleOfVolley;
    }



    @Override
    public String getLocalizedFailureDescriptionForDomainBean(int statusCode, Throwable e) {
        if (e instanceof ServerError) {
            return "服务器没有响应.";
        } else if (e instanceof TimeoutError) {
            return "网络请求超时.";
        } else {
            return "网络错误.";
        }
    }

    @Override
    public String getLocalizedFailureDescriptionForFile(int statusCode, Throwable e) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public INetRequestHandle requestDownloadFile(String urlString, boolean isNeedContinuingly, String httpMethod, Map<String, String> params, File downLoadFile, IFileRequestAsyncHttpResponseListener fileRequestAsyncHttpResponseListener) {
        return null;
    }

    @Override
    public INetRequestHandle requestUploadFile(String urlString, Map<String, String> params, String uploadFileKey, File uploadFile, IFileRequestAsyncHttpResponseListener fileRequestAsyncHttpResponseListener) {
        return null;
    }
}
