package core_lib.http_engine.volley;

import java.util.Map;

import com.loopj.android.http.RequestParams;

public class MyRequestParams extends RequestParams {

  public MyRequestParams() {
    super();
    // TODO Auto-generated constructor stub
  }

  public MyRequestParams(Map<String, String> arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

  public MyRequestParams(Object... arg0) {
    super(arg0);
    // TODO Auto-generated constructor stub
  }

  public MyRequestParams(String key, String value) {
    super(key, value);
    // TODO Auto-generated constructor stub
  }

  @Override
  public String getParamString() {
    return super.getParamString();
  }

}
