package core_lib.engine_helper.project;

import java.util.Map;


import core_lib.http_engine.volley.MyRequestParams;
import core_lib.simple_network_engine.engine_helper.interfaces.IPostDataPackage;

public class NetRequestParamsPackage implements IPostDataPackage {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T packageNetRequestParams(String httpRequestMethod,
                                         Map<String, String> requestParamsDictionary) {
        do {
            if (requestParamsDictionary == null || requestParamsDictionary.size() <= 0) {
                break;
            }

            MyRequestParams requestParams = new MyRequestParams(requestParamsDictionary);
            return (T) requestParams;

        } while (false);

        // 不要对外返回空指针
        return (T) new MyRequestParams();
    }

}
