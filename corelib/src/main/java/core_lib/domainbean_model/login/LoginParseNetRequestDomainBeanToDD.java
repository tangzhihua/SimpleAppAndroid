package core_lib.domainbean_model.login;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import core_lib.simple_network_engine.domain_layer.interfaces.IParseNetRequestDomainBeanToDataDictionary;
import core_lib.toolutils.SimpleMD5Tools;

final class LoginParseNetRequestDomainBeanToDD implements
        IParseNetRequestDomainBeanToDataDictionary<LoginNetRequestBean> {

    @Override
    public Map<String, String> parseNetRequestBeanToDataDictionary(
            final LoginNetRequestBean netRequestDomainBean) throws Exception {
        final String username = netRequestDomainBean.getUsername();
        final String password = netRequestDomainBean.getPassword();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            throw new Exception("用户名/密码不能为空!");
        }
        final Map<String, String> params = new HashMap<String, String>();

        StringBuilder strXml = new StringBuilder();
        strXml.append("<U_login_1_0>");
        strXml.append("<phone>");
        strXml.append(username);
        strXml.append("</phone>");
        strXml.append("<password>");
        strXml.append(password);
        strXml.append("</password>");
        strXml.append("</U_login_1_0>");

        //
        params.put("request", strXml.toString());

        return params;
    }
}
