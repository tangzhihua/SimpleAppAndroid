package core_lib.simple_network_engine.net_layer.file;

import java.io.File;
import java.util.Map;

import core_lib.simple_network_engine.net_layer.INetRequestHandle;

public interface IHttpRequestForDownloadFile {

    /**
     * 发起一个http文件请求(上传/下载)(这是网络层对外提供的接口)
     *
     * @param urlString
     * @param isNeedContinuingly                   是否需要断点续传
     * @param httpMethod
     * @param params                               传递给服务器的参数
     * @param downLoadFile                         要下载的文件, 会保存在这里(如果需要断点续传, 会从这里读取已经下载的数据长度)
     * @param fileRequestAsyncHttpResponseListener
     * @return
     */
    public INetRequestHandle requestDownloadFile(final String urlString,
                                                 final boolean isNeedContinuingly,
                                                 final String httpMethod,
                                                 final Map<String, String> params,
                                                 final File downLoadFile,
                                                 final IFileRequestAsyncHttpResponseListener fileRequestAsyncHttpResponseListener);
}
