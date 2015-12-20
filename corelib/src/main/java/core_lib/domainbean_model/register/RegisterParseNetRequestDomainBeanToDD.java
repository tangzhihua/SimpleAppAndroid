package core_lib.domainbean_model.register;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import core_lib.global_data_cache.GlobalConstantTools;
import core_lib.simple_network_engine.domain_layer.interfaces.IParseNetRequestDomainBeanToDataDictionary;

final class RegisterParseNetRequestDomainBeanToDD implements
        IParseNetRequestDomainBeanToDataDictionary<RegisterNetRequestBean> {

    @Override
    public Map<String, String> parseNetRequestBeanToDataDictionary(
            final RegisterNetRequestBean netRequestDomainBean) throws Exception {
        final String username = netRequestDomainBean.getUsername();
        final String password = netRequestDomainBean.getPassword();
        final String verificationCode = netRequestDomainBean.getVerificationCode();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(verificationCode)) {
            throw new Exception("用户名/密码/验证码 不能为空!");
        }
        final Map<String, String> params = new HashMap<String, String>();

        StringBuilder strXml = new StringBuilder();
        strXml.append("<U_Register_1_0>");
        strXml.append("<phone>");
        strXml.append(username);
        strXml.append("</phone>");
        strXml.append("<password>");
        strXml.append(password);
        strXml.append("</password>");
        strXml.append("<usertype>");
        strXml.append(GlobalConstantTools.UserTypeEnum.PuTong.getCode() + "");
        strXml.append("</usertype>");
        strXml.append("<vcode>");
        strXml.append(verificationCode);
        strXml.append("</vcode>");
        strXml.append("</U_Register_1_0>");

        //
        params.put("request", strXml.toString());

        return params;
    }
}
