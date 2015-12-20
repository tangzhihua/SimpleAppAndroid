package core_lib.http_engine.soap;

import java.util.concurrent.Future;

import core_lib.simple_network_engine.net_layer.ANetRequestHandleBase;
import core_lib.simple_network_engine.net_layer.INetRequestHandle;
import core_lib.simple_network_engine.net_layer.INetRequestIsCancelled;

/**
 * Created by Cxy on 2015/11/24.
 */
public class HttpRequestHandleOfSoap extends ANetRequestHandleBase {
    private Future future;

    public void setFuture(Future future) {
        this.future = future;
    }

    @Override
    public boolean isIdle() {
        return future.isCancelled() || future.isDone();
    }

    @Override
    public void cancel() {
        super.cancel();

        future.cancel(true);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }
}
