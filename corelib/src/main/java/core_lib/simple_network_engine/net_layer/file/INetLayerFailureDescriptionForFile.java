package core_lib.simple_network_engine.net_layer.file;

/**
 * 当网络层访问失败时, 通过此接口可以获取本地化的失败原因描述
 * 
 * @author zhihua.tang
 * 
 */

public interface INetLayerFailureDescriptionForFile {
  public String getLocalizedFailureDescriptionForFile(int statusCode, Throwable e);
}
