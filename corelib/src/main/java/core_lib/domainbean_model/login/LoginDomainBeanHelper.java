package core_lib.domainbean_model.login;

import android.text.TextUtils;

import com.google.gson.TypeAdapterFactory;

import core_lib.domainbean_model.UrlConstantForThisProject;
import core_lib.simple_network_engine.domain_layer.IDomainBeanHelper;
import core_lib.simple_network_engine.domain_layer.interfaces.IParseNetRequestDomainBeanToDataDictionary;
import core_lib.simple_network_engine.error_bean.ErrorCodeEnum;
import core_lib.simple_network_engine.error_bean.SimpleException;

public final class LoginDomainBeanHelper implements
        IDomainBeanHelper<LoginNetRequestBean, LoginNetRespondBean> {

    @Override
    public IParseNetRequestDomainBeanToDataDictionary<LoginNetRequestBean> parseNetRequestDomainBeanToDataDictionaryFunction() {
        return new LoginParseNetRequestDomainBeanToDD();
    }

    @Override
    public String specialUrlPath(LoginNetRequestBean netRequestBean) {
        return "";
    }

    @Override
    public String httpMethod() {
        return "POST";
    }

    @Override
    public Class<LoginNetRespondBean> netRespondBeanClass() {
        return LoginNetRespondBean.class;
    }

    @Override
    public void netRespondBeanValidityTest(LoginNetRespondBean netRespondBean) throws SimpleException {
        String errorMessage = "";
        do {
            if (TextUtils.isEmpty(netRespondBean.getPhone())) {
                errorMessage = "服务器返回的数据中, 丢失关键字段 phone.";
                break;
            }

            if (TextUtils.isEmpty(netRespondBean.getUid())) {
                errorMessage = "服务器返回的数据中, 丢失关键字段 uid.";
                break;
            }

            // 一切OK
            return;
        } while (false);

        // 服务器返回的数据中, 丢失关键字段
        throw new SimpleException(ErrorCodeEnum.Server_LostCoreField.getCode(), errorMessage);
    }

    @Override
    public TypeAdapterFactory typeAdapterFactoryForGSON() {
        return null;
    }
}
