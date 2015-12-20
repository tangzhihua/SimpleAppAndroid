package core_lib.simple_network_engine.net_layer;

/**
 * 给 "业务网络层" 准备的, 用于在 "业务层" 取消了一个网络请求时, "业务网路层" 能够响应这个取消操作.
 * 
 * @author zhihua.tang
 * 
 */
public interface INetRequestIsCancelled {
  /**
   * 判断当前网络请求是否已经被取消
   * 
   * @return
   */
  public boolean isCancelled();
}
