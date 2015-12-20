package core_lib.simple_network_engine.domain_layer.interfaces;

import java.util.Map;

/**
 * 把一个 "网络请求业务Bean" 解析成其对应网络业务接口的 "数据字典"
 * 注意 : 如果具体接口没有参数, 就不要实现IParseNetRequestDomainBeanToDataDictionary接口
 * @author zhihua.tang
 * 
 */
public interface IParseNetRequestDomainBeanToDataDictionary<NetRequestBean> {
  public Map<String, String> parseNetRequestBeanToDataDictionary(final NetRequestBean netRequestBean) throws Exception;
}
