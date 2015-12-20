package core_lib.engine_helper.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import core_lib.simple_network_engine.engine_helper.interfaces.IGetServerResponseDataValidityData;
import core_lib.simple_network_engine.error_bean.ErrorCodeEnum;
import core_lib.simple_network_engine.error_bean.SimpleException;

public class GetServerResponseDataValidityData implements IGetServerResponseDataValidityData {

    @Override
    public Object serverResponseDataValidityData(Object serverResponseData) throws SimpleException {
        if (!(serverResponseData instanceof JSONObject)) {
            throw new SimpleException(ErrorCodeEnum.Client_NetResponseDataTypeDifferent);
        }
        Object validityData = serverResponseData;

//        try {
//            validityData = ((JSONObject) serverResponseData).get("data");
//            /* 注意 :
//               这里是在兼容后台返回的数据类型, 因为有的接口, 后台认为访问成功时,
//               返回的data是一个JSONArray类型, 并且是空的.
//               */
//            if (validityData instanceof JSONArray) {
//                validityData = new JSONObject();
//            }
//        } catch (JSONException e) {
//            throw new SimpleException(ErrorCodeEnum.Server_LostDataField);
//        }

        return validityData;
    }

}
