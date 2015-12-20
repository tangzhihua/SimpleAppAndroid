package core_lib.domainbean_model.get_verify_code;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import core_lib.simple_network_engine.domain_layer.interfaces.IParseNetRequestDomainBeanToDataDictionary;

final class GetVerifyCodeParseNetRequestDomainBeanToDD implements
        IParseNetRequestDomainBeanToDataDictionary<GetVerifyCodeNetRequestBean> {

    @Override
    public Map<String, String> parseNetRequestBeanToDataDictionary(
            final GetVerifyCodeNetRequestBean netRequestDomainBean) throws Exception {
        final String phoneNumber = netRequestDomainBean.getPhoneNumber();
        if (netRequestDomainBean.getVerifyTypeEnum() == null) {
            throw new Exception("验证码类型 不能为空!");
        }
        final String verifyType = netRequestDomainBean.getVerifyTypeEnum().getCode() + "";
        if (TextUtils.isEmpty(phoneNumber)) {
            throw new Exception("手机号码 不能为空!");
        }
        final Map<String, String> params = new HashMap<String, String>();

        StringBuilder strXml = new StringBuilder();
        strXml.append("<U_VerifyCode_1_0>");
        strXml.append("<Mobile>");
        strXml.append(phoneNumber);
        strXml.append("</Mobile>");
        strXml.append("<VerifyType>");
        strXml.append(verifyType);
        strXml.append("</VerifyType>");
        strXml.append("</U_VerifyCode_1_0>");

        //
        params.put("request", strXml.toString());

        return params;
    }
}
