package core_lib.simple_network_engine.domain_net_layer.file;

import java.io.File;

import core_lib.simple_network_engine.error_bean.ErrorBean;

/**
 * "业务网络层" 对外提供的接口, 用于发起文件操作(上传/下载)的异步响应
 *
 * @author zhihua.tang
 */
public interface IFileAsyncHttpResponseListener {
    /**
     * 文件下载完成
     *
     * @param file
     */
    public void onSuccess(final File file, final String responseBody);

    /**
     * 文件下载失败
     *
     * @param error
     */
    public void onFailure(final ErrorBean error);

    /**
     * 取消
     */
    // public void onCancel();
}
