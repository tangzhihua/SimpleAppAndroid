package core_lib.simple_network_engine.engine_helper.interfaces;

import core_lib.simple_network_engine.error_bean.SimpleException;

public interface INetResponseRawEntityDataUnpack {
  /**
   * 将网络返回的 "生数据", 解密/解码, 转成和服务器约定好的 "数据交换协议对象" 如 JSONObject,
   * 这里要考虑网络传回的原生数据有加密的情况(如AES加密, base64)
   * 
   * @param rawData
   *          网络返回的 "生数据", 可能是二进制流, 如果使用soap协议, 可能是个soap对象
   * @return "数据交换协议对象"
   * @throws SimpleException
   *           具体策略类保证, 内部异常的完整捕获, 并且打包成KalendsException向外抛出
   */
  public Object unpackNetResponseRawEntityDataToDataExchangeProtocolObject(final Object rawData)
      throws SimpleException;
}
