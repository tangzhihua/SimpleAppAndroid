package core_lib.http_engine.volley;

import com.android.volley.Request;

import core_lib.simple_network_engine.net_layer.ANetRequestHandleBase;
import core_lib.simple_network_engine.net_layer.INetRequestHandle;
import core_lib.simple_network_engine.net_layer.INetRequestIsCancelled;

public class HttpRequestHandleOfVolley extends ANetRequestHandleBase {

    /**
     * Volley返回的Request, 本身没有提供isFinished()方法, 所以由外部回调进行调用
     */
    private boolean isFinished = false;

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    private Request<?> requestHandle;

    public void setRequestHandle(Request<?> requestHandle) {
        this.requestHandle = requestHandle;
    }

    public HttpRequestHandleOfVolley() {
    }

    @Override
    public boolean isCancelled() {
        return requestHandle.isCanceled();
    }

    @Override
    public boolean isIdle() {
        return isFinished || requestHandle.isCanceled();
    }

    @Override
    public void cancel() {
        super.cancel();

        requestHandle.cancel();
    }

}
