package core_lib.engine_helper.project;

import org.json.JSONObject;

import core_lib.simple_network_engine.engine_helper.interfaces.IServerResponseDataValidityTest;
import core_lib.simple_network_engine.error_bean.ErrorCodeEnum;
import core_lib.simple_network_engine.error_bean.SimpleException;

/**
 * 测试从服务器端返回的数据是否是有效的(数据要先解包, 然后再根据服务器返回的错误码做判断)
 *
 * @author zhihua.tang
 */
public final class ServerResponseDataValidityTest implements IServerResponseDataValidityTest {

    @Override
    public void serverResponseDataValidityTest(final Object netUnpackedData) throws SimpleException {
        if (!(netUnpackedData instanceof JSONObject)) {
            throw new SimpleException(
                    ErrorCodeEnum.Client_NetResponseDataTypeDifferent);
        }
        if (!((JSONObject) netUnpackedData).has("status")) {
            throw new SimpleException(ErrorCodeEnum.Server_LostErrorCodeField);
        }
        int errorCode = ((JSONObject) netUnpackedData).optInt("status");
        String errorMessage = ((JSONObject) netUnpackedData).optString("msg");
        if (errorCode != 1) {
            // 服务器端返回了错误码
            throw new SimpleException(errorCode, errorMessage);
        }

    }

}
