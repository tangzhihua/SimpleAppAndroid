package core_lib.simple_network_engine.engine_helper;

import java.util.Map;

import core_lib.simple_network_engine.domain_layer.IDomainBeanHelper;
import core_lib.simple_network_engine.engine_helper.interfaces.ICustomHttpHeaders;
import core_lib.simple_network_engine.engine_helper.interfaces.IGetServerResponseDataValidityData;
import core_lib.simple_network_engine.engine_helper.interfaces.INetRequestPublicParams;
import core_lib.simple_network_engine.engine_helper.interfaces.INetResponseRawEntityDataUnpack;
import core_lib.simple_network_engine.engine_helper.interfaces.IParseNetResponseDataToNetRespondBean;
import core_lib.simple_network_engine.engine_helper.interfaces.IPostDataPackage;
import core_lib.simple_network_engine.engine_helper.interfaces.IServerResponseDataValidityTest;
import core_lib.simple_network_engine.engine_helper.interfaces.ISpliceFullUrlByDomainBeanSpecialPath;
import core_lib.simple_network_engine.net_layer.INetLayerInterface;

/**
 * 引擎助手接口
 * 
 * @author zhihua.tang
 */
public interface IEngineHelper {
  // 打包post数据(可在这里进行数据的加密工作)
  public IPostDataPackage postDataPackageFunction();

  // 将网络返回的原生数据, 解压成可识别的UTF8字符串(在这里完成数据的解密)
  public INetResponseRawEntityDataUnpack netResponseRawEntityDataUnpackFunction();

  // 服务器返回的数据有效性检测(这是业务层面有效性检测, 比如说, 调用后台一个搜索接口, 传入关键字,
  // 在后台没有搜索到结果的情况下, 在业务层面, 我们认为是失败的)
  // 在这里主要是检查跟服务器约定好的 errorCode 和 errorMessage
  public IServerResponseDataValidityTest serverResponseDataValidityTestFunction();

  // 从服务器返回的数据中, 获取 data 部分(真正的有效数据)
  public IGetServerResponseDataValidityData getServerResponseDataValidityDataFunction();

  // 拼接一个网络接口的完整请求URL
  public ISpliceFullUrlByDomainBeanSpecialPath spliceFullUrlByDomainBeanSpecialPathFunction();

  // 业务Bean请求时,需要传递到服务器的公共参数
  public INetRequestPublicParams netRequestPublicParamsFunction();

  // 自定义的http headers
  public ICustomHttpHeaders customHttpHeadersFunction();

  // 网络层接口(提供http相关服务)
  public INetLayerInterface netLayerInterfaceFunction();

  // 将网络返回的数据, 解析成NetRespondBean
  public IParseNetResponseDataToNetRespondBean parseNetResponseDataToNetRespondBeanFunction();

  // 网络接口的映射
  // requestBean ---> domainBeanHelper
  public Map<Class<?>, IDomainBeanHelper<?, ?>> getNetworkInterfaceMapping();
}
