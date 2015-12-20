package core_lib.simple_network_engine.net_layer;

import core_lib.simple_network_engine.domain_net_layer.domainbean.IRespondBeanAsyncResponseListener;
import core_lib.toolutils.DebugLog;

/**
 * Created by zhihuatang on 15/11/25.
 */
public abstract class ANetRequestHandleBase implements INetRequestHandle, INetRequestIsCancelled {
    private IRespondBeanAsyncResponseListener<?> respondBeanAsyncResponseListener;

    @Override
    public void cancel() {
        DebugLog.e("ANetRequestHandleBase", "外部调用者取消了本次网络请求");
        if (respondBeanAsyncResponseListener != null){
            respondBeanAsyncResponseListener.onEnd(false);
        }
    }

    public void setRespondBeanAsyncResponseListener(IRespondBeanAsyncResponseListener<?> respondBeanAsyncResponseListener) {
        this.respondBeanAsyncResponseListener = respondBeanAsyncResponseListener;
    }
}
