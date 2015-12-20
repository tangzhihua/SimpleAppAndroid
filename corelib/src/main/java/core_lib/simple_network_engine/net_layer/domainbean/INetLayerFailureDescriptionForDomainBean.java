package core_lib.simple_network_engine.net_layer.domainbean;

/**
 * 当网络层访问失败时, 通过此接口可以获取本地化的失败原因描述
 * 
 * @author zhihua.tang
 * 
 */
public interface INetLayerFailureDescriptionForDomainBean {
  public String getLocalizedFailureDescriptionForDomainBean(int statusCode, Throwable e);
}
