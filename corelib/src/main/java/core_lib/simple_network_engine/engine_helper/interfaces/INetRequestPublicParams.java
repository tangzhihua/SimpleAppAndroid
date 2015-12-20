package core_lib.simple_network_engine.engine_helper.interfaces;

import com.google.common.collect.ImmutableMap;

/**
 * 业务Bean请求时,需要传递到服务器的公共参数
 * 
 * @author zhihua.tang
 * 
 */
public interface INetRequestPublicParams {
  public ImmutableMap<String, String> publicParams();
}
