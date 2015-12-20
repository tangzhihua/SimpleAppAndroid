package core_lib.domainbean_model;

import core_lib.simple_network_engine.net_layer.INetRequestHandle;

public class NetRequestHandleStub implements INetRequestHandle {
  public NetRequestHandleStub() {
  }

  private boolean idle;

  public void setIdle(boolean idle) {
    this.idle = idle;
  }

  @Override
  public boolean isIdle() {
    return idle;
  }

  @Override
  public void cancel() {
    idle = true;
  }
}
