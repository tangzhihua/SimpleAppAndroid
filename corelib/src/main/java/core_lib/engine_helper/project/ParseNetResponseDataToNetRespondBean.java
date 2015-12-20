package core_lib.engine_helper.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import core_lib.simple_network_engine.domain_layer.IDomainBeanHelper;
import core_lib.simple_network_engine.engine_helper.interfaces.IParseNetResponseDataToNetRespondBean;
import core_lib.simple_network_engine.error_bean.ErrorCodeEnum;
import core_lib.simple_network_engine.error_bean.SimpleException;
import core_lib.toolutils.DebugLog;

public class ParseNetResponseDataToNetRespondBean implements IParseNetResponseDataToNetRespondBean {

    @Override
    public <NetRespondBean> NetRespondBean parseNetResponseDataToNetRespondBean(
            final Object netResponseData, final IDomainBeanHelper<?, ?> domainBeanHelper)
            throws SimpleException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (domainBeanHelper.typeAdapterFactoryForGSON() != null) {
            gsonBuilder.registerTypeAdapterFactory(domainBeanHelper.typeAdapterFactoryForGSON());
        }
        Gson gson = gsonBuilder.create();

        try {
            // String jsonString = netResponseData.toString().replace("\\", "");
            // jsonString = jsonString.substring(1, jsonString.length()-1); //去掉头尾引号
            String jsonString = netResponseData.toString();
            @SuppressWarnings("unchecked")
            NetRespondBean respondBean = (NetRespondBean) gson.fromJson(jsonString, domainBeanHelper.netRespondBeanClass());
            return respondBean;
        } catch (Exception e) {
            DebugLog.e("ParseNetResponseDataToNetRespondBean", e.getLocalizedMessage());
            throw new SimpleException(ErrorCodeEnum.Client_CreateNetRespondBeanFailed);
        }

    }

}
