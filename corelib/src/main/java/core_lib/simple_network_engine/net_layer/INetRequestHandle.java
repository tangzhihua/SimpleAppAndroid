package core_lib.simple_network_engine.net_layer;

/**
 * 给业务层使用的, 用于控制一个网络请求
 * 
 * @author zhihua.tang
 * 
 */
public interface INetRequestHandle {
  /**
   * 判断当前网络请求, 是否处于空闲状态(只有处于空闲状态时, 才应该发起一个新的网络请求)
   * 
   * @return
   */
  public boolean isIdle();

  /**
   * 取消当前请求
   */
  public void cancel();
}
