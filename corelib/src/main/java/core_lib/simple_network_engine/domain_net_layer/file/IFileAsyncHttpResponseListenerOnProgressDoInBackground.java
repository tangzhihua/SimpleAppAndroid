package core_lib.simple_network_engine.domain_net_layer.file;

public interface IFileAsyncHttpResponseListenerOnProgressDoInBackground extends IFileAsyncHttpResponseListener {
  /**
   * @note 文件下载进度(在子线程中)
   * 
   * @param bytesWritten
   *          已完成的数据长度
   * @param totalSize
   *          要下载的数据总长度
   */
  public void onProgressDoInBackground(final long bytesWritten, final long totalSize);
}
