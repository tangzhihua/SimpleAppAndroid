package core_lib.simple_network_engine.net_layer.file;

/**
 * "网络层" 对外提供的接口, 用于发起文件操作(上传/下载)的异步响应
 *
 * @author zhihua.tang
 */
public interface IFileRequestAsyncHttpResponseListener {
    /**
     * 请求文件成功
     *
     * @param responseBody 服务器返回的信息
     */
    public void onSuccess(byte[] responseBody);

    /**
     * 请求失败
     *
     * @param statusCode
     * @param e
     */
    public void onFailure(int statusCode, Throwable e);

    /**
     * 文件上传/下载进度
     *
     * @param bytesWritten
     * @param totalSize
     */
    public void onProgress(long bytesWritten, long totalSize);
}
