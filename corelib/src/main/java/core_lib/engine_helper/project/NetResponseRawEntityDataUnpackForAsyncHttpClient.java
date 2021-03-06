package core_lib.engine_helper.project;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import core_lib.simple_network_engine.engine_helper.interfaces.INetResponseRawEntityDataUnpack;
import core_lib.simple_network_engine.error_bean.ErrorCodeEnum;
import core_lib.simple_network_engine.error_bean.SimpleException;

/**
 * 
 * @author zhihua.tang
 * 
 */
public final class NetResponseRawEntityDataUnpackForAsyncHttpClient implements
    INetResponseRawEntityDataUnpack {

  @Override
  public Object unpackNetResponseRawEntityDataToDataExchangeProtocolObject(Object rawData)
      throws SimpleException {
    byte[] rawDataOfByteArray = (byte[]) rawData;
    JSONObject netUnpackedData = null;
    try {
      // 将网络返回的二进制数据流, 转换成UTF-8字符串
      String netUnpackedDataOfUTF8String = new String(rawDataOfByteArray, "utf-8");
      // 将UTF-8字符串转换成 "数据交换协议对象" , 本项目使用的是json
      netUnpackedData = new JSONObject(netUnpackedDataOfUTF8String);
    } catch (UnsupportedEncodingException e) {
      throw new SimpleException(
          ErrorCodeEnum.Server_UnpackedResponseDataFailed.getCode(),
          ErrorCodeEnum.Server_UnpackedResponseDataFailed.getMessage());
    } catch (JSONException e) {
      throw new SimpleException(
          ErrorCodeEnum.Server_DataExchangeProtocolMismatch.getCode(),
          ErrorCodeEnum.Server_DataExchangeProtocolMismatch.getMessage());
    }

    return netUnpackedData;
  }

}
