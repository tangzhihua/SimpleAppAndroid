package core_lib.simple_network_engine.net_layer.file;

import java.io.File;
import java.util.Map;

import core_lib.simple_network_engine.net_layer.INetRequestHandle;

/**
 * Created by tangzhihua on 15/9/7.
 * 上传多文件
 */
public interface IHttpRequestForUploadFile {
    /**
     * 上传文件
     *
     * @param urlString
     * @param params                               要发给服务器的字典数据
     * @param uploadFileKey                        上传的文件数据对应的key
     * @param uploadFile                           要上传的文件数据
     * @param fileRequestAsyncHttpResponseListener
     * @return
     */
    public INetRequestHandle requestUploadFile(final String urlString,
                                               final Map<String, String> params,
                                               final String uploadFileKey,
                                               final File uploadFile,
                                               final IFileRequestAsyncHttpResponseListener fileRequestAsyncHttpResponseListener);
}
