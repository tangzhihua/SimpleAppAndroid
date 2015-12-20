package core_lib.simple_network_engine.engine_helper.interfaces;

import java.util.Map;

/**
 * 根据httpRequestMethod和domainDataDictionary来打包RequestParams(可以在这里进行数据的加密工作)
 *
 * @author zhihua.tang
 */
public interface IPostDataPackage {
    /**
     * @param httpRequestMethod       http请求方法(GET/POST)
     * @param requestParamsDictionary 业务数据字典
     * @return
     */
    public <T> T packageNetRequestParams(final String httpRequestMethod, final Map<String, String> requestParamsDictionary);
}
