package core_lib.simple_network_engine.domain_net_layer;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.common.collect.Maps;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import core_lib.domainbean_model.DomainBeanStub;
import core_lib.domainbean_model.NetRequestHandleStub;
import core_lib.engine_helper.EngineHelperSingleton;
import core_lib.http_engine.volley.MyRequestParams;
import core_lib.simple_network_engine.domain_layer.AMoreUrlNetRequestBean;
import core_lib.simple_network_engine.domain_layer.IDomainBeanHelper;
import core_lib.simple_network_engine.domain_net_layer.domainbean.IRespondBeanAsyncResponseListener;
import core_lib.simple_network_engine.domain_net_layer.file.IFileAsyncHttpResponseListener;
import core_lib.simple_network_engine.domain_net_layer.file.IFileAsyncHttpResponseListenerOnProgressDoInBackground;
import core_lib.simple_network_engine.domain_net_layer.file.IFileAsyncHttpResponseListenerOnProgressDoInUIThread;
import core_lib.simple_network_engine.error_bean.ErrorBean;
import core_lib.simple_network_engine.error_bean.ErrorCodeEnum;
import core_lib.simple_network_engine.error_bean.SimpleException;
import core_lib.simple_network_engine.net_layer.ANetRequestHandleBase;
import core_lib.simple_network_engine.net_layer.INetRequestHandle;
import core_lib.simple_network_engine.net_layer.INetRequestIsCancelled;
import core_lib.simple_network_engine.net_layer.NetRequestHandleNilObject;
import core_lib.simple_network_engine.net_layer.domainbean.IDomainBeanRequestAsyncHttpResponseListener;
import core_lib.simple_network_engine.net_layer.file.IFileRequestAsyncHttpResponseListener;
import core_lib.toolutils.DebugLog;
import core_lib.toolutils.SimpleReachabilityTools;

/*
 * ------------ 引擎设计说明 ------------
 * 
 * zhihua.tang update latest date : 20140629
 * 
 * 引擎的设计初衷是, 通过分层架构, 让各层的职责单一化(关注点隔离思想), 只有保证了各层职责单一化,
 * 才能充分发挥团队中, 高中初级工程师的技术水准.
 * 引擎分为三层, 业务层/业务网络层/网络层.
 * 
 * 1) 业务层
 * 就是控制层(Controller/Activity), 这层的定义是完成app相关的业务逻辑,
 * 所以这层也是一个app中工作量最大和改动量最大的地方;
 * 一个在线App, 其实就是客户端和服务器之间传递 "业务Bean", 客户端主要负责显示服务器端传递回来的 "业务Bean".
 * 在我的引擎中, 最大程度的保证了业务层开发的单一性, 在业务层通过 "外观模式" 获取一个统一的API接口 SimpleNetworkEngineSingleton,
 * 通过它将复杂的网络访问隔离开去, 使用两种业务Bean来体现网络接口的交互,
 * 这就是 NetRequestBean(网络请求业务Bean, 发起一个网络请求时, 需要构建目标接口的 NetRequestBean),
 * 和 NetRespondBean(网络响应业务Bean, 服务器返回的数据, 都被封装在这个Bean中.)
 * 客户端和服务器的交互, 在业务层, 就被抽象成了这两个业务Bean(这点上和http协议的 请求报文 和 响应报文 的抽象相似).
 * 调用 SimpleNetworkEngineSingleton 提供的方法, 发起一个网络接口的访问后, 会直接返回 id<INetRequestHandle>,
 * 这个就是给业务层提供的, 本次网络请求的操作句柄, 通过这个句柄可以查询本次网络请求的运行情况, 并且可以取消本次网络请求.
 * SimpleNetworkEngineSingleton 提供的方法, 永远不会返回一个nil对象, 当发生错误时, 会返回一个 NetRequestHandleNilObject 对象,
 * 这样就最大程度保证了业务层编码住够简单安全.
 * 业务层不应该在发起网络请求时, 看见 "多线程" "数据交换协议" "数据加密" "具体的网络协议, 或者具体的网络引擎", 这些应该被封装, 被隔离.
 * 业务层只跟业务打交道, 数据的代表就是两个业务Bean.
 * 这样做的好处是不言而喻的, 也就是说, 当那些和业务无关的细节(上面提到的 "多线程" "数据交换协议" 等等...)需要改变时, 业务层代码不需要修改, 这样就遵循了OO的OCP原则.
 * 
 * 3) 网络层
 * 网络层的定位, 就是一个通用的http请求, 在网络层不应该有具体业务的识别, 而只应该遵循http协议的抽象定义, "请求报文" 和 "响应报文"
 * 网络层的具体设计细节就是, "网络层" 和 "业务网络层" 之间定义一个接口 INetLayerInterface,
 * 这个接口只传递最少的数据, 就是URL和DataDictionary(数据字典, 要发往服务器的数据),
 * 网络层可以采用目前流行的http引擎, 如MKNetworkKit, AFNetworking, 或者自己完全实现一个http引擎都可以.
 * 
 * 2) 业务网络层
 * 业务网络层的定位就是完成 "业务层" 和 "网络层" 的协调/沟通/翻译, 反正就是中间人的角色.
 * 业务网络层和业务层之间的数据交互, 就是通过 "NetRequestBean" "NetRespondBean".
 * 但是和网络层之间的数据交互是要按照它们之间的约定接口 INetLayerInterface,
 * 这个接口需要的是URL和DataDictionary, 所以业务网络层要完成
 * NetRequestBean ----> URL和DataDictionary 以及
 * NetResponseData ----> NetRespondBean 之间的转换工作.
 * 
 * 这里的设计细节是这样的:
 * 1.首先定义一个所有业务Bean都需要实现的抽象工厂接口 IDomainBeanHelper,
 * 这个接口主要提供具体业务接口的 URL和将业务Bean解析成数据字典的策略算法, 还有用于反射的Class, 这些接口每个具体的业务Bean都不一样.
 * 2.在DomainBeanHelperClassNameMapping中完成一个具体的业务接口请求Bean和其抽象工厂的映射,
 * 完成映射后, 业务网络层, 只要编写多态的代码, 就可以应对所有业务接口的访问了.
 * 以前见过很多非面向对象的设计是这样的, 会为每个业务接口提供一个助手函数, 入参是具体的参数, 返回值可能是数据交换协议或者一个响应业务Bean,
 * 这种设计, 都是 "重复代码" 这种坏味道的体现, 虽然乍一看没有什么代码是重复, 但是都忽略了一点, 就是抽象,
 * 这里的抽象是什么呢, 就是, "请求" 和 "响应", 所有助手函数的代码逻辑都是一样的, 一旦要增加变化时, 就需要修改全部的函数, 这个简直是噩梦.
 */
/*
 * 泛型参数的命名规则 : 
 * 类型参数T
 泛型的类型参数T可以看作是一个占位符，它不是一种类型，而仅代表某种可能的类型。
 在定义泛型时，T出现的位置可以在使用时用任何类型来代替。类型参数T的命名规则如下：

 使用描述性名称命名泛型类型参数，除非单个字母名称完全可以让人了解它表示的含义，
 而描述性名称不会有更多的意义。
 */
public enum SimpleNetworkEngineSingleton {
    getInstance;

    private final String TAG = this.getClass().getSimpleName();

    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * 发起一个业务接口的网络请求(其实就是从服务器同步数据Bean到客户端, 数据Bean将采用常用的数据交换协议进行承载, 如JSON/XML)
     *
     * @param <NetRequestBean>                    网络请求业务Bean
     * @param <NetRespondBean>                    网络响应业务Bean
     * @param netRequestBean                      网络请求业务Bean
     * @param domainBeanAsyncHttpResponseListener 异步网络响应监听(监听返回时, 调用者根据自己需求实现相关回调方法)
     * @return INetRequestHandle (可以通过这个handle来查看本次网络请求的状态, 或者取消本次网络请求)
     */
    public <NetRequestBean, NetRespondBean> INetRequestHandle requestDomainBean(
            final NetRequestBean netRequestBean,
            final IRespondBeanAsyncResponseListener<NetRespondBean> domainBeanAsyncHttpResponseListener) {

        DebugLog.i(TAG, " ");
        DebugLog.i(TAG, " ");
        DebugLog.i(TAG, " ");
        DebugLog.i(TAG, "<<<<<<<<<<     发起一个DomainBean网络请求, 参数检验中....     >>>>>>>>>>");
        DebugLog.i(TAG, " ");

        // TODO:20150215, 我感觉应该保证完整的流程, 也就是说, 即使发起一个网络请求在参数检验阶段就失败了, 也应该给调用者完整的生命周期
        if (domainBeanAsyncHttpResponseListener != null) {
            domainBeanAsyncHttpResponseListener.onBegin();
        }

        INetRequestHandle netRequestHandle = new NetRequestHandleNilObject();

        try {
            if (!SimpleReachabilityTools.isReachable()) {
                // 客户端没有激活的网络.
                throw new SimpleException(ErrorCodeEnum.Client_isNoAvailableNetwork);
            }
            // effective for java 38 检查参数有效性, 对于共有的方法,
            // 要使用异常机制来通知调用方发生了入参错误.
            if (netRequestBean == null) {
                throw new SimpleException(ErrorCodeEnum.Client_InputParamsError);
            }

            /************************************************************************************/
            // 下面使用的是本地桩数据
            if (DomainBeanStub.isUseStubDomainBean) {
                DebugLog.e(TAG, "使用的是本地桩数据.");
                final NetRequestHandleStub netRequestHandleStub = new NetRequestHandleStub();
                final ExecutorService executorStub = Executors.newSingleThreadExecutor();
                executorStub.execute(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (!netRequestHandleStub.isIdle()) {
                            // 调用方没有取消本次请求
                            final NetRespondBean netRespondBean = (NetRespondBean) DomainBeanStub
                                    .getNetRespondBeanStubByNetRequestBean(netRequestBean);
                            if (netRespondBean != null) {
                                if (domainBeanAsyncHttpResponseListener != null) {
                                    domainBeanAsyncHttpResponseListener.onSuccessInBackground(netRespondBean);
                                }
                            }

                            // 一切OK
                            handler.post(new Runnable() {
                                @SuppressWarnings("unchecked")
                                public void run() {
                                    // ------------------------------------- >>>
                                    if (!netRequestHandleStub.isIdle()) {

                                        netRequestHandleStub.setIdle(true);


                                        if (netRespondBean == null) {
                                            if (domainBeanAsyncHttpResponseListener != null) {
                                                domainBeanAsyncHttpResponseListener
                                                        .onFailure(new ErrorBean(-1, "操作错误"));
                                            }
                                        } else {
                                            if (domainBeanAsyncHttpResponseListener != null) {
                                                domainBeanAsyncHttpResponseListener.onSuccess(netRespondBean);
                                            }
                                        }

                                        // 通知控制层, 本次网络请求彻底完成
                                        if (domainBeanAsyncHttpResponseListener != null) {
                                            domainBeanAsyncHttpResponseListener.onEnd(true);
                                        }
                                    }
                                    // ------------------------------------- >>>
                                }
                            });
                        }


                    }
                });

                return netRequestHandleStub;
            }
            /************************************************************************************/
            // 下面是真正的网络访问
            else {

                // 将 "网络请求业务Bean" 的 getClass().getName() 作为何其绑定的DomainBeanHelper对象匹配的key
                DebugLog.i(TAG, "请求接口 : " + netRequestBean.getClass().getSimpleName());

                // domainBeanHelper 中包含了跟当前网络接口相关的一组助手方法(这里使用了 "抽象工厂" 设计模式)
                @SuppressWarnings("unchecked")
                final IDomainBeanHelper<NetRequestBean, NetRespondBean> domainBeanHelper
                        = (IDomainBeanHelper<NetRequestBean, NetRespondBean>) EngineHelperSingleton.getInstance.getNetworkInterfaceMapping().get(netRequestBean.getClass());
                if (domainBeanHelper == null) {
                    throw new SimpleException(
                            ErrorCodeEnum.Client_ProgrammingError.getCode(), "接口 ["
                            + netRequestBean.getClass().getSimpleName()
                            + "] 找不到与其对应的 domainBeanHelper, 客户端编程错误.");
                }

                // 发往服务器的完整的 "数据字典", 包括 "公共参数" 和 "私有参数"
                final Map<String, String> fullParams = Maps.newHashMap();
                // 公共参数
                final Map<String, String> publicParams = EngineHelperSingleton.getInstance
                        .netRequestPublicParamsFunction().publicParams();
                fullParams.putAll(publicParams);
                DebugLog.i(TAG, "publicParams--> " + publicParams.toString());

                // 请求URL
                String urlString = "";
                Map<String, String> privateParams = null;
                if (netRequestBean instanceof AMoreUrlNetRequestBean
                        && !TextUtils.isEmpty(((AMoreUrlNetRequestBean) netRequestBean).getMoreUrlString())) {
                    // 需要使用 more_url 来请求下一页数据
                    urlString = ((AMoreUrlNetRequestBean) netRequestBean).getMoreUrlString() + "&";
                } else {
                    // 普通情况, 需要添加目标接口的私有参数列表, 另外url也是目标接口设定好的

                    // 具体接口的参数
                    // 注意 : 一定要保证先填充 "公共参数", 然后再填充 "具体接口的参数", 这是因为有时候具体接口的参数需要覆盖公共参数.
                    if (domainBeanHelper.parseNetRequestDomainBeanToDataDictionaryFunction() != null) {
                        privateParams = domainBeanHelper.parseNetRequestDomainBeanToDataDictionaryFunction()
                                .parseNetRequestBeanToDataDictionary(netRequestBean);
                        if (privateParams == null || privateParams.size() <= 0) {
                            throw new SimpleException(
                                    ErrorCodeEnum.Client_ProgrammingError.getCode(),
                                    "接口["
                                            + netRequestBean.getClass().getSimpleName()
                                            + "]"
                                            + " parseNetRequestBeanToDataDictionary 方法返回的参数字典为空(如果当前接口没有参数, 请不要实现IParseNetRequestDomainBeanToDataDictionary接口).");
                        }
                        fullParams.putAll(privateParams);
                    }

                    final String specialPath = domainBeanHelper.specialUrlPath(netRequestBean);
                    urlString = EngineHelperSingleton.getInstance.spliceFullUrlByDomainBeanSpecialPathFunction().fullUrlByDomainBeanSpecialPath(specialPath);

                    /* 注意 : 额外处理的逻辑, 现在后台要求, 公共参数, 以GET方式传递 */
                    //final MyRequestParams paramsForGet = new MyRequestParams(publicParams);
                    //urlString = urlString + paramsForGet.getParamString();
                    /* ----------------------------------------------------- */
                    //
                    DebugLog.i(TAG, "specialPath--> " + specialPath);
                }

                DebugLog.i(TAG, "fullUrlString--> " + urlString);

                // 调用http引擎, 发送请求
                netRequestHandle = EngineHelperSingleton.getInstance.netLayerInterfaceFunction()
                        .requestDomainBean(urlString, domainBeanHelper.httpMethod(),
                                EngineHelperSingleton.getInstance.customHttpHeadersFunction().customHttpHeaders(),
                                fullParams, EngineHelperSingleton.getInstance.postDataPackageFunction(),

                                new IDomainBeanRequestAsyncHttpResponseListener() {

                                    @Override
                                    public void onSuccess(final INetRequestIsCancelled netRequestIsCancelled, final Object response) {


                                        try {

                                            DebugLog.e(TAG, " ");
                                            DebugLog.e(TAG, "<<<<<<<<<<     接口 [" + netRequestBean.getClass().getSimpleName() + "] 网络层(http)请求成功    >>>>>>>>>>");
                                            DebugLog.e(TAG, " ");

                                            // ------------------------------------- >>>
                                            if (netRequestIsCancelled.isCancelled()) {
                                                // 外部调用者取消了本次网络请求
                                                return;
                                            }
                                            // ------------------------------------- >>>

                                            // ------------------------------------- >>>
                                            // 将具体网络引擎层返回的 "原始未加工数据byte[]" ,先解包成 "可识别数据字符串(一般是utf-8)",
                                            // 然后解析成数据交换协议(如JSONObject).
                                            // 这里要考虑网络传回的原生数据有加密的情况(如AES加密, base64)
                                            final Object dataExchangeProtocolObject = EngineHelperSingleton.getInstance
                                                    .netResponseRawEntityDataUnpackFunction()
                                                    .unpackNetResponseRawEntityDataToDataExchangeProtocolObject(response);
                                            DebugLog.e(TAG, "服务器返回的数据(已解包) -------------->\n" + dataExchangeProtocolObject.toString() + "\n<--------------------------------------");
                                            DebugLog.e(TAG, " ");
                                            // ------------------------------------- >>>

                                            // ------------------------------------- >>>
                                            // 检查服务器返回的数据是否有效, 如果无效, 要获取服务器返回的错误码和错误描述信息
                                            // (比如说某次网络请求成功了,
                                            // 但是服务器那边没有有效的数据给客户端,所以服务器会返回错误码和描述信息告知客户端访问结果)
                                            EngineHelperSingleton.getInstance.serverResponseDataValidityTestFunction()
                                                    .serverResponseDataValidityTest(dataExchangeProtocolObject);
                                            DebugLog.e(TAG, "!!!服务器返回的数据, 逻辑上有效(不包含错误码).");
                                            DebugLog.e(TAG, " ");
                                            // ------------------------------------- >>>

                                            // ------------------------------------- >>>
                                            // 得到真正的有效数据(一般是 data 部分的数据)
                                            final Object validityData = EngineHelperSingleton.getInstance
                                                    .getServerResponseDataValidityDataFunction()
                                                    .serverResponseDataValidityData(dataExchangeProtocolObject);
                                            // ------------------------------------- >>>

                                            // ------------------------------------- >>>
                                            // 将 "数据交换协议对象" 解析成 "具体的网络响应业务Bean"
                                            final NetRespondBean respondDomainBean = EngineHelperSingleton.getInstance
                                                    .parseNetResponseDataToNetRespondBeanFunction()
                                                    .<NetRespondBean>parseNetResponseDataToNetRespondBean(validityData,
                                                            domainBeanHelper);
                                            DebugLog.e(TAG, "生成 netRespondBean 成功 -> " + respondDomainBean.toString());
                                            DebugLog.e(TAG, " ");
                                            // ------------------------------------- >>>

                                            // ------------------------------------- >>>
                                            // 检查 netRespondBean 有效性, 在这里要检查服务器返回的数据中, 是否丢失了核心字段.
                                            domainBeanHelper.netRespondBeanValidityTest(respondDomainBean);
                                            // ------------------------------------- >>>

                                            // 将 netRespondBean 回调给控制层(Activity)
                                            if (domainBeanAsyncHttpResponseListener != null) {
                                                domainBeanAsyncHttpResponseListener.onSuccessInBackground(respondDomainBean);
                                            }

                                            // 一切OK
                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 将 netRespondBean 回调给控制层(Activity)
                                                    if (domainBeanAsyncHttpResponseListener != null) {
                                                        domainBeanAsyncHttpResponseListener.onSuccess(respondDomainBean);
                                                    }

                                                    // 通知控制层(Activity), 本次网络请求彻底完成
                                                    if (domainBeanAsyncHttpResponseListener != null) {
                                                        domainBeanAsyncHttpResponseListener.onEnd(true);
                                                    }

                                                    DebugLog.e(TAG, "<<<<<<<<<<     接口 [" + netRequestBean.getClass().getSimpleName() + "] 本次网络请求成功, 并且圆满结束.    >>>>>>>>>>");
                                                    DebugLog.e(TAG, " ");
                                                    DebugLog.e(TAG, " ");
                                                    DebugLog.e(TAG, " ");
                                                }
                                            });

                                            // 一切OK
                                            return;

                                        } catch (final SimpleException e) {
                                            DebugLog.e(TAG,
                                                    "<<<<<<<<<<     接口 [" + netRequestBean.getClass().getSimpleName()
                                                            + "] \n业务层解析失败, 失败原因是 = " + e.getLocalizedMessage() + "\n>>>>>>>>>>");
                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 将KalendsErrorBean回调给控制层(Activity), 告知本次网络请求失败的原因
                                                    if (domainBeanAsyncHttpResponseListener != null) {
                                                        domainBeanAsyncHttpResponseListener.onFailure(new ErrorBean(e
                                                                .getCode(), e.getLocalizedMessage()));
                                                    }

                                                    // 通知控制层(Activity), 本次网络请求彻底完成
                                                    if (domainBeanAsyncHttpResponseListener != null) {
                                                        domainBeanAsyncHttpResponseListener.onEnd(false);
                                                    }

                                                    DebugLog.i(TAG, " ");
                                                    DebugLog.e(TAG, "<<<<<<<<<<     接口 ["
                                                            + netRequestBean.getClass().getSimpleName()
                                                            + "] 本次网络请求失败, 并且遗憾结束.    >>>>>>>>>>");
                                                    DebugLog.i(TAG, " ");
                                                    DebugLog.i(TAG, " ");
                                                    DebugLog.i(TAG, " ");
                                                }
                                            });

                                            // 一切OK
                                            return;
                                        } catch (final Exception e) {
                                            // TODO: 不应该走到这里, 走到这里证明是没有按照规定编码
                                            DebugLog.i(TAG, " ");
                                            DebugLog.e(TAG, "<<<<<<<<<<     接口 ["
                                                    + netRequestBean.getClass().getSimpleName()
                                                    + "] 不应该走到这里, 走到这里证明是没有按照规定编码, 错误原因=" + e.getLocalizedMessage()
                                                    + "    >>>>>>>>>>");
                                            handler.post(new Runnable() {

                                                @Override
                                                public void run() {
                                                    // 将KalendsErrorBean回调给控制层(Activity), 告知本次网络请求失败的原因
                                                    if (domainBeanAsyncHttpResponseListener != null) {
                                                        domainBeanAsyncHttpResponseListener.onFailure(new ErrorBean(ErrorCodeEnum.Client_DomainNetLayerHandleHttpRespondDataUnkonwError.getCode(), e.getLocalizedMessage()));
                                                    }

                                                    // 通知控制层(Activity), 本次网络请求彻底完成
                                                    if (domainBeanAsyncHttpResponseListener != null) {
                                                        domainBeanAsyncHttpResponseListener.onEnd(false);
                                                    }

                                                    DebugLog.i(TAG, " ");
                                                    DebugLog.e(TAG, "<<<<<<<<<<     接口 ["
                                                            + netRequestBean.getClass().getSimpleName()
                                                            + "] 本次网络请求失败, 并且遗憾结束.    >>>>>>>>>>");
                                                    DebugLog.i(TAG, " ");
                                                    DebugLog.i(TAG, " ");
                                                    DebugLog.i(TAG, " ");
                                                }
                                            });
                                        }

                                    }

                                    @Override
                                    public void onFailure(final INetRequestIsCancelled netRequestIsCancelled,
                                                          final int statusCode, final Throwable e) {
                                        DebugLog.i(TAG, " ");
                                        // TODO:要将网络层错误原因解析成, 能够对用户显示的本地化信息
                                        final String localizedFailureDescription = EngineHelperSingleton.getInstance
                                                .netLayerInterfaceFunction().getLocalizedFailureDescriptionForDomainBean(
                                                        statusCode, e);
                                        DebugLog.e(TAG, "<<<<<<<<<<     接口 ["
                                                + netRequestBean.getClass().getSimpleName()
                                                + "] \n网络层(http)访问失败 , \n原因-->statusCode=" + statusCode + ", Throwable="
                                                + e.toString() + ", localizedFailureDescription="
                                                + localizedFailureDescription + "\n>>>>>>>>>");

                                        // ------------------------------------- >>>
                                        if (netRequestIsCancelled.isCancelled()) {
                                            // 外部调用者取消了本次网络请求
                                            return;
                                        }
                                        // ------------------------------------- >>>

                                        handler.post(new Runnable() {

                                            @Override
                                            public void run() {
                                                // 将KalendsErrorBean回调给控制层(Activity), 告知本次网络请求失败的原因
                                                DebugLog.i(TAG, "domainBean");
                                                if (domainBeanAsyncHttpResponseListener != null) {
                                                    DebugLog.i(TAG, "onFailure=" + statusCode + "=" + e.getLocalizedMessage());
                                                    domainBeanAsyncHttpResponseListener.onFailure(new ErrorBean(
                                                            statusCode, localizedFailureDescription));
                                                }

                                                // 通知控制层(Activity), 本次网络请求彻底完成
                                                if (domainBeanAsyncHttpResponseListener != null) {
                                                    domainBeanAsyncHttpResponseListener.onEnd(false);
                                                }
                                                DebugLog.i(TAG, " end");
                                                DebugLog.i(TAG, " ");
                                                DebugLog.e(TAG, "<<<<<<<<<<     接口 ["
                                                        + netRequestBean.getClass().getSimpleName()
                                                        + "] 本次网络请求失败, 并且遗憾结束.    >>>>>>>>>>");
                                                DebugLog.i(TAG, " ");
                                                DebugLog.i(TAG, " ");
                                                DebugLog.i(TAG, " ");
                                                DebugLog.i(TAG, " ");
                                            }
                                        });
                                        return;
                                    }

                                    //
                                });

                DebugLog.i(TAG, " ");
                DebugLog.i(TAG, "<<<<<<<<<<     参数检验正确, 启动子线程进行异步访问.");

            }
        } catch (final SimpleException e) {
            // TODO:一定要保证始终的异步特性, 所以这里要进行异步回调, 不要直接同步返回给外部调用者
            handler.post(new Runnable() {

                @Override
                public void run() {
                    DebugLog.i(TAG, "");
                    DebugLog.e(TAG, "<<<<<<<<<<     接口 [" + netRequestBean.getClass().getSimpleName()
                            + "] \n发起一个DomainBean网络请求参数检查失败(可能是没有网络连接或者入参错误), \n错误原因=" + e.getLocalizedMessage()
                            + "   \n>>>>>>>>>>");
                    DebugLog.i(TAG, "");

                    // 发起网络请求失败, 在参数检测阶段就失败了
                    if (domainBeanAsyncHttpResponseListener != null) {
                        domainBeanAsyncHttpResponseListener.onFailure(e.toErrorBean());
                    }

                    // 通知控制层(Activity), 本次网络请求彻底完成(即使在参数检测阶段就失败了, 也要给外部调用者完整的生命周期)
                    if (domainBeanAsyncHttpResponseListener != null) {
                        domainBeanAsyncHttpResponseListener.onEnd(false);
                    }

                    DebugLog.e(TAG, "<<<<<<<<<<     接口 [" + netRequestBean.getClass().getSimpleName()
                            + "] 本次网络请求失败, 并且遗憾结束.    >>>>>>>>>>");
                    DebugLog.i(TAG, " ");
                    DebugLog.i(TAG, " ");
                    DebugLog.i(TAG, " ");
                    DebugLog.i(TAG, " ");
                }
            });

        } catch (final Exception e) {
            // TODO:不应该走到这里, 因为项目中抛出的异常要求都使用我们自定义的KalendsException
            // TODO:IParseNetRequestDomainBeanToDataDictionary
            // 如果不使用KalendsException也无伤大雅
            handler.post(new Runnable() {

                @Override
                public void run() {
                    DebugLog.i(TAG, "");
                    DebugLog.e(TAG, "<<<<<<<<<<     接口 [" + netRequestBean.getClass().getSimpleName()
                            + "] \n发起一个DomainBean网络请求参数检查失败(可能是没有网络连接或者入参错误), \n错误原因=" + e.getLocalizedMessage()
                            + "   \n>>>>>>>>>>");
                    DebugLog.i(TAG, "");

                    // 发起网络请求失败, 在参数检测阶段就失败了
                    if (domainBeanAsyncHttpResponseListener != null) {
                        domainBeanAsyncHttpResponseListener.onFailure(new ErrorBean(
                                ErrorCodeEnum.Client_Error.getCode(), e.getLocalizedMessage()));
                    }

                    // 通知控制层(Activity), 本次网络请求彻底完成(即使在参数检测阶段就失败了, 也要给外部调用者完整的生命周期)
                    if (domainBeanAsyncHttpResponseListener != null) {
                        domainBeanAsyncHttpResponseListener.onEnd(false);
                    }

                    DebugLog.e(TAG, "<<<<<<<<<<     接口 [" + netRequestBean.getClass().getSimpleName()
                            + "] 本次网络请求失败, 并且遗憾结束.    >>>>>>>>>>");
                    DebugLog.i(TAG, " ");
                    DebugLog.i(TAG, " ");
                    DebugLog.i(TAG, " ");
                    DebugLog.i(TAG, " ");
                }
            });

        } finally {

        }

        //
        DebugLog.i(TAG, " ");
        DebugLog.i(TAG, " ");
        DebugLog.i(TAG, " ");
        DebugLog.i(TAG, " ");
        ((ANetRequestHandleBase)netRequestHandle).setRespondBeanAsyncResponseListener(domainBeanAsyncHttpResponseListener);
        return netRequestHandle;

    }

    /**
     * 请求一个文件下载
     *
     * @param url                           文件下载url
     * @param params                        请求参数字典
     * @param filePathString                文件完整的保存路径(包括文件名)
     * @param isNeedContinuingly            是否需要断点续传
     * @param fileAsyncHttpResponseListener 文件下载异步监听
     * @return
     */
    public INetRequestHandle requestFileDownlaod(final String url,
                                                 final Map<String, String> params,
                                                 final String filePathString,
                                                 final boolean isNeedContinuingly,
                                                 final IFileAsyncHttpResponseListener fileAsyncHttpResponseListener) {

        INetRequestHandle netRequestHandle = new NetRequestHandleNilObject();

        try {

            DebugLog.i(TAG, "发起文件下载请求---->:requestFileDownlaod url = " + url);
            if (!SimpleReachabilityTools.isReachable()) {
                // 请检查网络
                throw new Exception("请检查网络");
            }
            if (TextUtils.isEmpty(url)) {
                throw new IllegalArgumentException("入参 url 为空.");
            }
            if (TextUtils.isEmpty(filePathString)) {
                throw new IllegalArgumentException("入参 filePathString 为空.");
            }
            final File file = new File(filePathString);

            // TODO: 这里客户端开发人员要和服务器开发人员约定好, 如果有请求参数时, 一律使用POST方式, 如果没有请求参数时,
            // 一律使用GET方式.
            String httpRequestMethod = "GET";
            if (params != null && params.size() > 0) {
                httpRequestMethod = "POST";
            }

            // 发往服务器的完整的 "数据字典", 包括 "公共参数" 和 "私有参数"
            final Map<String, String> fullParams = new HashMap<>(params);
            // 公共参数
            final Map<String, String> publicParams = EngineHelperSingleton.getInstance
                    .netRequestPublicParamsFunction().publicParams();
            fullParams.putAll(publicParams);
            // TODO:额外处理的逻辑, 现在后台要求, 公共参数, 以GET方式传递
            final MyRequestParams paramsForGet = new MyRequestParams(publicParams);
            final String fullUrlString = url + paramsForGet.getParamString();

            netRequestHandle = EngineHelperSingleton.getInstance.netLayerInterfaceFunction().requestDownloadFile(
                    fullUrlString, isNeedContinuingly, httpRequestMethod, fullParams, file,
                    new IFileRequestAsyncHttpResponseListener() {

                        @Override
                        public void onSuccess(byte[] responseBody) {
                            fileAsyncHttpResponseListener.onSuccess(file, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Throwable e) {
                            fileAsyncHttpResponseListener.onFailure(new ErrorBean(statusCode, e.getLocalizedMessage()));
                        }

                        @Override
                        public void onProgress(final long bytesWritten, final long totalSize) {
                            DebugLog.i(TAG, "文件下载进度-->bytesWritten=" + bytesWritten + ", totalSize=" + totalSize);
                            // 通知外部, 下载进度
                            if (fileAsyncHttpResponseListener != null) {
                                final long tempBytesWritten = bytesWritten;// 当前进度

                                // 如果需要在后台线程中通知下载进度
                                if (fileAsyncHttpResponseListener instanceof IFileAsyncHttpResponseListenerOnProgressDoInBackground) {
                                    ((IFileAsyncHttpResponseListenerOnProgressDoInBackground) fileAsyncHttpResponseListener)
                                            .onProgressDoInBackground(tempBytesWritten, totalSize);
                                }

                                // 如果需要在主线程中通知下载进度
                                if (fileAsyncHttpResponseListener instanceof IFileAsyncHttpResponseListenerOnProgressDoInUIThread) {
                                    handler.post(new Runnable() {

                                        @Override
                                        public void run() {
                                            ((IFileAsyncHttpResponseListenerOnProgressDoInUIThread) fileAsyncHttpResponseListener)
                                                    .onProgressDoInUIThread(tempBytesWritten, totalSize);
                                        }
                                    });
                                }

                            }
                        }
                    });
        } catch (Exception e) {
            DebugLog.e(TAG, "发起网络请求失败, 错误原因-->" + e.getLocalizedMessage());
            e.printStackTrace();
            if (fileAsyncHttpResponseListener != null) {
                fileAsyncHttpResponseListener.onFailure(new ErrorBean(
                        ErrorCodeEnum.Client_Error.getCode(), e.getLocalizedMessage()));
            }
        }

        return netRequestHandle;
    }

    /**
     * @param url
     * @param downloadFileSavePathFile
     * @param fileAsyncHttpResponseHandler
     * @return
     */
    public INetRequestHandle requestFileDownlaod(final String url,
                                                 final File downloadFileSavePathFile,
                                                 final IFileAsyncHttpResponseListener fileAsyncHttpResponseHandler) {
        return requestFileDownlaod(url, null, downloadFileSavePathFile.getPath(), false, fileAsyncHttpResponseHandler);
    }

    /**
     * @param url
     * @param downloadFileSavePathString
     * @param fileAsyncHttpResponseHandler
     * @return
     */
    public INetRequestHandle requestFileDownlaod(final String url,
                                                 final String downloadFileSavePathString,
                                                 final IFileAsyncHttpResponseListener fileAsyncHttpResponseHandler) {
        return requestFileDownlaod(url, null, downloadFileSavePathString, false, fileAsyncHttpResponseHandler);
    }

    public INetRequestHandle requestFileUpload(final String url,
                                               final Map<String, String> params,
                                               final String uploadFileKey,
                                               final String filePathString,
                                               final IFileAsyncHttpResponseListener fileAsyncHttpResponseListener) {
        INetRequestHandle netRequestHandle = new NetRequestHandleNilObject();

        try {

            DebugLog.i(TAG, "发起文件上传请求---->:requestFileUpload url = " + url);
            if (!SimpleReachabilityTools.isReachable()) {
                // 请检查网络
                throw new Exception("请检查网络");
            }
            if (TextUtils.isEmpty(url)) {
                throw new IllegalArgumentException("入参 url 为空.");
            }
            if (TextUtils.isEmpty(filePathString)) {
                throw new IllegalArgumentException("入参 filePathString 为空.");
            }
            if (TextUtils.isEmpty(uploadFileKey)) {
                throw new IllegalArgumentException("入参 uploadFileKey 为空.");
            }
            final File file = new File(filePathString);
            if (!file.exists()) {
                throw new IllegalArgumentException("要上传的文件不存在.");
            }

            // 发往服务器的完整的 "数据字典", 包括 "公共参数" 和 "私有参数"
            final Map<String, String> fullParams = new HashMap<>(params);
            // 公共参数
            final Map<String, String> publicParams = EngineHelperSingleton.getInstance
                    .netRequestPublicParamsFunction().publicParams();
            fullParams.putAll(publicParams);

            // TODO:额外处理的逻辑, 现在后台要求, 公共参数, 以GET方式传递
            final MyRequestParams paramsForGet = new MyRequestParams(publicParams);
            final String fullUrlString = url + paramsForGet.getParamString();

            netRequestHandle = EngineHelperSingleton.getInstance.netLayerInterfaceFunction().requestUploadFile(fullUrlString, fullParams, uploadFileKey, file, new IFileRequestAsyncHttpResponseListener() {
                @Override
                public void onSuccess(final byte[] responseBody) {
                    String responseBodyString = new String(responseBody);
                    DebugLog.e(TAG, "上传文件 (" + file.getPath() + ") 成功, 服务器返回的 responseBody = " + responseBodyString);
                    if (fileAsyncHttpResponseListener != null) {
                        fileAsyncHttpResponseListener.onSuccess(file, responseBodyString);
                    }
                }

                @Override
                public void onFailure(final int statusCode, final Throwable e) {
                    DebugLog.e(TAG, "上传文件 (" + file.getPath() + ") 失败, 服务器返回的 statusCode = " + statusCode + ", Throwable=" + e.getLocalizedMessage());
                    fileAsyncHttpResponseListener.onFailure(new ErrorBean(statusCode, e.getLocalizedMessage()));
                }

                @Override
                public void onProgress(final long bytesWritten, final long totalSize) {
                    DebugLog.i(TAG, "文件上传进度(" + file.getPath() + ")-->bytesWritten=" + bytesWritten + ", totalSize=" + totalSize);
                    // 通知外部, 下载进度
                    if (fileAsyncHttpResponseListener != null) {
                        final long tempBytesWritten = bytesWritten;// 当前进度

                        // 如果需要在后台线程中通知下载进度
                        if (fileAsyncHttpResponseListener instanceof IFileAsyncHttpResponseListenerOnProgressDoInBackground) {
                            ((IFileAsyncHttpResponseListenerOnProgressDoInBackground) fileAsyncHttpResponseListener)
                                    .onProgressDoInBackground(tempBytesWritten, totalSize);
                        }

                        // 如果需要在主线程中通知下载进度
                        if (fileAsyncHttpResponseListener instanceof IFileAsyncHttpResponseListenerOnProgressDoInUIThread) {
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    ((IFileAsyncHttpResponseListenerOnProgressDoInUIThread) fileAsyncHttpResponseListener)
                                            .onProgressDoInUIThread(tempBytesWritten, totalSize);
                                }
                            });
                        }

                    }
                }
            });
        } catch (Exception e) {
            DebugLog.e(TAG, "发起网络请求失败, 错误原因-->" + e.getLocalizedMessage());
            e.printStackTrace();
            if (fileAsyncHttpResponseListener != null) {
                fileAsyncHttpResponseListener.onFailure(new ErrorBean(
                        ErrorCodeEnum.Client_Error.getCode(), e.getLocalizedMessage()));
            }
        }

        return netRequestHandle;
    }

}
