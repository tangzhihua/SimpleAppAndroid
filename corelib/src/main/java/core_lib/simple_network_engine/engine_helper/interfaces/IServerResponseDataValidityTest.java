package core_lib.simple_network_engine.engine_helper.interfaces;

import core_lib.simple_network_engine.error_bean.SimpleException;

public interface IServerResponseDataValidityTest {
  /**
   * 服务器返回的数据有效性检测(这是业务层面有效性检测, 比如说, 调用后台一个搜索接口, 传入关键字, 在后台没有搜索到结果的情况下, 在业务层面,
   * 我们认为是失败的)
   * 
   * @param netUnpackedData
   *          数据交换协议对象
   * @throws SimpleException
   *           如果服务器返回的数据业务角度无效, 就要抛出KalendsException
   */
  public void serverResponseDataValidityTest(final Object netUnpackedData) throws SimpleException;
}
