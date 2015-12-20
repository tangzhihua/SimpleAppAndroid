package core_lib.simple_network_engine.net_layer;

import core_lib.simple_network_engine.net_layer.domainbean.IHttpRequestForDomainBean;
import core_lib.simple_network_engine.net_layer.domainbean.INetLayerFailureDescriptionForDomainBean;
import core_lib.simple_network_engine.net_layer.file.IHttpRequestForDownloadFile;
import core_lib.simple_network_engine.net_layer.file.IHttpRequestForUploadFile;
import core_lib.simple_network_engine.net_layer.file.INetLayerFailureDescriptionForFile;

/**
 * 网络层接口
 *
 * @author zhihua.tang
 */
public interface INetLayerInterface extends
        IHttpRequestForDomainBean,
        IHttpRequestForDownloadFile,
        INetLayerFailureDescriptionForDomainBean,
        INetLayerFailureDescriptionForFile,
        IHttpRequestForUploadFile {

}
