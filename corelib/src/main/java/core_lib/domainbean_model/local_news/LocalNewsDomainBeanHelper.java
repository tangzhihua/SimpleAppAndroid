package core_lib.domainbean_model.local_news;

import com.google.gson.TypeAdapterFactory;

import core_lib.simple_network_engine.domain_layer.IDomainBeanHelper;
import core_lib.simple_network_engine.domain_layer.interfaces.IParseNetRequestDomainBeanToDataDictionary;
import core_lib.simple_network_engine.error_bean.ErrorCodeEnum;
import core_lib.simple_network_engine.error_bean.SimpleException;


public final class LocalNewsDomainBeanHelper implements
        IDomainBeanHelper<LocalNewsNetRequestBean, LocalNewsNetRespondBean> {

    @Override
    public IParseNetRequestDomainBeanToDataDictionary<LocalNewsNetRequestBean> parseNetRequestDomainBeanToDataDictionaryFunction() {
        return new LocalNewsParseNetRequestDomainBeanToDD();
    }

    @Override
    public String specialUrlPath(LocalNewsNetRequestBean netRequestBean) {
        return "";
    }

    @Override
    public String httpMethod() {
        return "POST";
    }

    @Override
    public Class<LocalNewsNetRespondBean> netRespondBeanClass() {
        return LocalNewsNetRespondBean.class;
    }

    @Override
    public void netRespondBeanValidityTest(LocalNewsNetRespondBean netRespondBean) throws SimpleException {
        if (netRespondBean.getList().size() <= 0) {
            // 服务器无结果返回
            throw new SimpleException(ErrorCodeEnum.kErrorCodeEnum_Server_Custom_Error_NoResult);
        }
    }

    @Override
    public TypeAdapterFactory typeAdapterFactoryForGSON() {
        return null;
    }
}
