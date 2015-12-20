package core_lib.simple_network_engine.domain_layer;

import com.google.gson.TypeAdapterFactory;

import core_lib.simple_network_engine.domain_layer.interfaces.IParseNetRequestDomainBeanToDataDictionary;
import core_lib.simple_network_engine.error_bean.SimpleException;

/**
 * 业务Bean相关的助手方法群(这里使用的是抽象工厂模式)
 * 
 * 这里罗列的接口是每个业务Bean都需要实现的.
 * 
 * @author zhihua.tang
 */
public interface IDomainBeanHelper<NetRequestBean, NetRespondBean> {
  /**
   * 将NetRequestDomainBean(网络请求业务Bean), 解析成发往服务器的数据字典(key要跟服务器定义的接口协议对应,
   * value可以在这里进行二次处理, 比如密码的md5加密)
   * 
   * @return
   */
  public IParseNetRequestDomainBeanToDataDictionary<NetRequestBean> parseNetRequestDomainBeanToDataDictionaryFunction();

  /**
   * 当前网络接口对应的NetRespondBean 我们使用KVC的方式直接从字典和Class映射成具体的模型对象, 这里设置的就是要转换的
   * [NetRespondBean Class]
   * 
   * @return
   */
  public Class<NetRespondBean> netRespondBeanClass();

  /**
   * 当前业务接口, 对应的path.
   * 
   * @return
   */
  public String specialUrlPath(final NetRequestBean netRequestBean);

  /**
   * 检查当前NetRespondBean是否有效 这里的设计含义是 : 我们因为直接使用KVC,
   * 将网络返回的数据字典直接解析成NetRespondBean, 但是这里有个隐患,
   * 就是服务器返回的数据字典可能和本地的NetRespondBean字段不匹配, 所以每个NetRespondBean都应该设计有核心字段的概念,
   * 只要服务器返回的数据字典包含有核心字典, 就认为本次数据有效,比如说登录接口,当登录成功后, 服务器会返回username和uid和其他一些字段,
   * 那么uid和username就是核心字段, 只要这两个字段有效就可以认为本次网络请求有效
   * 
   * @return
   */
  public void netRespondBeanValidityTest(final NetRespondBean netRespondBean)
      throws SimpleException;

  /**
   * 当前接口的http request method
   * 
   * @return
   */
  public String httpMethod();

  /**
   * 这是对gson库的支撑, 需要分步解析的复杂业务Bean, 就需要自定义实现 TypeAdapterFactory
   * 
   * @return
   */
  public TypeAdapterFactory typeAdapterFactoryForGSON();
}
