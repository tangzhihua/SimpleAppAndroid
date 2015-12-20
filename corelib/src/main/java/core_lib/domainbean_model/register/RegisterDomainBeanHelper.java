package core_lib.domainbean_model.register;

import android.text.TextUtils;

import com.google.gson.TypeAdapterFactory;

import core_lib.domainbean_model.login.LoginNetRespondBean;
import core_lib.simple_network_engine.domain_layer.IDomainBeanHelper;
import core_lib.simple_network_engine.domain_layer.interfaces.IParseNetRequestDomainBeanToDataDictionary;
import core_lib.simple_network_engine.error_bean.ErrorCodeEnum;
import core_lib.simple_network_engine.error_bean.SimpleException;

/**
 * 智车账号登录
 *
 * @author zhihua.tang
 */
public final class RegisterDomainBeanHelper implements
        IDomainBeanHelper<RegisterNetRequestBean, LoginNetRespondBean> {

    @Override
    public IParseNetRequestDomainBeanToDataDictionary<RegisterNetRequestBean> parseNetRequestDomainBeanToDataDictionaryFunction() {
        return new RegisterParseNetRequestDomainBeanToDD();
    }

    @Override
    public String specialUrlPath(RegisterNetRequestBean netRequestBean) {
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
