package core_lib.engine_helper;

import java.util.Map;

import core_lib.domainbean_model.NetworkInterfaceMappingSingleton;
import core_lib.engine_helper.project.CustomHttpHeaders;
import core_lib.engine_helper.project.GetServerResponseDataValidityData;
import core_lib.engine_helper.project.NetRequestParamsPackage;
import core_lib.engine_helper.project.NetRequestPublicParams;
import core_lib.engine_helper.project.NetResponseRawEntityDataUnpackForAsyncHttpClient;
import core_lib.engine_helper.project.NetResponseRawEntityDataUnpackForVolley;
import core_lib.engine_helper.project.ParseNetResponseDataToNetRespondBean;
import core_lib.engine_helper.project.ServerResponseDataValidityTest;
import core_lib.engine_helper.project.SpliceFullUrlByDomainBeanSpecialPath;
import core_lib.simple_network_engine.domain_layer.IDomainBeanHelper;
import core_lib.simple_network_engine.engine_helper.IEngineHelper;
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
 * 引擎助手类
 *
 * @author zhihua.tang
 */
public enum EngineHelperSingleton implements IEngineHelper {
    getInstance;

    private final IPostDataPackage netRequestParamsPackageFunction = new NetRequestParamsPackage();

    @Override
    public IPostDataPackage postDataPackageFunction() {
        return netRequestParamsPackageFunction;
    }

    private final INetResponseRawEntityDataUnpack netResponseRawEntityDataUnpackFunctionForVolley = new NetResponseRawEntityDataUnpackForVolley();
    private final INetResponseRawEntityDataUnpack netResponseRawEntityDataUnpackFunctionForAsyncHttpClient = new NetResponseRawEntityDataUnpackForAsyncHttpClient();

    @Override
    public INetResponseRawEntityDataUnpack netResponseRawEntityDataUnpackFunction() {
        return netResponseRawEntityDataUnpackFunctionForVolley;
    }

    private final IServerResponseDataValidityTest serverResponseDataValidityTestFunction = new ServerResponseDataValidityTest();

    @Override
    public IServerResponseDataValidityTest serverResponseDataValidityTestFunction() {
        return serverResponseDataValidityTestFunction;
    }

    private final ISpliceFullUrlByDomainBeanSpecialPath spliceFullUrlByDomainBeanSpecialPathFunction = new SpliceFullUrlByDomainBeanSpecialPath();

    @Override
    public ISpliceFullUrlByDomainBeanSpecialPath spliceFullUrlByDomainBeanSpecialPathFunction() {
        return spliceFullUrlByDomainBeanSpecialPathFunction;
    }

    private final INetRequestPublicParams domainBeanRequestPublicParamsFunction = new NetRequestPublicParams();

    @Override
    public INetRequestPublicParams netRequestPublicParamsFunction() {
        return domainBeanRequestPublicParamsFunction;
    }

    @Override
    public INetLayerInterface netLayerInterfaceFunction() {
        return HttpEngineSingleton.getInstance;
    }

    private final ICustomHttpHeaders customHttpHeadersFunction = new CustomHttpHeaders();

    @Override
    public ICustomHttpHeaders customHttpHeadersFunction() {
        return customHttpHeadersFunction;
    }

    @Override
    public Map<Class<?>, IDomainBeanHelper<?, ?>> getNetworkInterfaceMapping() {
        return NetworkInterfaceMappingSingleton.getInstance.getNetworkInterfaceMapping();
    }

    private final IGetServerResponseDataValidityData getServerResponseDataValidityDataFunction = new GetServerResponseDataValidityData();

    @Override
    public IGetServerResponseDataValidityData getServerResponseDataValidityDataFunction() {
        return getServerResponseDataValidityDataFunction;
    }

    private final IParseNetResponseDataToNetRespondBean parseNetResponseDataToNetRespondBeanFunction = new ParseNetResponseDataToNetRespondBean();

    @Override
    public IParseNetResponseDataToNetRespondBean parseNetResponseDataToNetRespondBeanFunction() {
        return parseNetResponseDataToNetRespondBeanFunction;
    }

}
