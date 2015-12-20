package core_lib.domainbean_model.local_news;

import java.util.HashMap;
import java.util.Map;

import core_lib.simple_network_engine.domain_layer.interfaces.IParseNetRequestDomainBeanToDataDictionary;

final class LocalNewsParseNetRequestDomainBeanToDD implements
        IParseNetRequestDomainBeanToDataDictionary<LocalNewsNetRequestBean> {

    @Override
    public Map<String, String> parseNetRequestBeanToDataDictionary(
            final LocalNewsNetRequestBean netRequestDomainBean) throws Exception {

        if (netRequestDomainBean.getTypeEnum() == null) {
            throw new Exception("接口类型 不能为空!");
        }

        if (netRequestDomainBean.getSortEnum() == null) {
            throw new Exception("排序方式 不能为空!");
        }
        final Map<String, String> params = new HashMap<String, String>();

        StringBuilder strXml = new StringBuilder();
        strXml.append("<U_LocalNews_1_0>");
        strXml.append("<type>");
        strXml.append(netRequestDomainBean.getTypeEnum().getCode());
        strXml.append("</type>");
        strXml.append("<sort>");
        strXml.append(netRequestDomainBean.getTypeEnum().name());
        strXml.append("</sort>");
        strXml.append("<p>");
        strXml.append(netRequestDomainBean.getPage() + "");
        strXml.append("</p>");
        strXml.append("<per>");
        strXml.append(netRequestDomainBean.getTotal() + "");
        strXml.append("</per>");
        strXml.append("</U_LocalNews_1_0>");

        //
        params.put("request", strXml.toString());

        return params;
    }
}
