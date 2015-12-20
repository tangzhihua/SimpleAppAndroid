package core_lib.simple_network_engine.engine_helper.interfaces;

import core_lib.simple_network_engine.domain_layer.IDomainBeanHelper;
import core_lib.simple_network_engine.error_bean.SimpleException;

public interface IParseNetResponseDataToNetRespondBean {
  /**
   * 将网络返回的数据, 解析成 "网络响应业务Bean"
   * 
   * @param netResponseData
   *          网络返回的数据(一般是JSONObject)
   * @param netRespondBeanClass
   *          网络响应业务Bean Class
   * @return
   */
  public <NetRespondBean> NetRespondBean parseNetResponseDataToNetRespondBean(
      final Object netResponseData, final IDomainBeanHelper<?, ?> domainBeanHelper)
      throws SimpleException;
}
