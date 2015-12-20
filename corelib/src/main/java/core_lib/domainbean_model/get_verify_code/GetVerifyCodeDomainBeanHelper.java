package core_lib.domainbean_model.get_verify_code;

import com.google.gson.TypeAdapterFactory;

import core_lib.simple_network_engine.domain_layer.IDomainBeanHelper;
import core_lib.simple_network_engine.domain_layer.interfaces.IParseNetRequestDomainBeanToDataDictionary;
import core_lib.simple_network_engine.error_bean.SimpleException;

/**
 * 智车账号登录
 *
 * @author zhihua.tang
 */
public final class GetVerifyCodeDomainBeanHelper implements
        IDomainBeanHelper<GetVerifyCodeNetRequestBean, GetVerifyCodeNetRespondBean> {

    @Override
    public IParseNetRequestDomainBeanToDataDictionary<GetVerifyCodeNetRequestBean> parseNetRequestDomainBeanToDataDictionaryFunction() {
        return new GetVerifyCodeParseNetRequestDomainBeanToDD();
    }

    @Override
    public String specialUrlPath(GetVerifyCodeNetRequestBean netRequestBean) {
        return "";
    }

    @Override
    public String httpMethod() {
        return "POST";
    }

    @Override
    public Class<GetVerifyCodeNetRespondBean> netRespondBeanClass() {
        return GetVerifyCodeNetRespondBean.class;
    }

    @Override
    public void netRespondBeanValidityTest(GetVerifyCodeNetRespondBean netRespondBean) throws SimpleException {

    }

    @Override
    public TypeAdapterFactory typeAdapterFactoryForGSON() {
        return null;
    }
}
