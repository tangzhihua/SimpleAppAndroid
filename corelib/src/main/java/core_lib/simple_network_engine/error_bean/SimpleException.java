package core_lib.simple_network_engine.error_bean;

/**
 * 
 * @author zhihua.tang
 * 
 */
public class SimpleException extends Exception {
  /**
   * 
   */
  private static final long serialVersionUID = -827295247430801129L;

  private final int code;
  private final String msg;

  public SimpleException(ErrorCodeEnum errorCodeEnum) {
    super(errorCodeEnum.getMessage());
    this.code = errorCodeEnum.getCode();
    this.msg = errorCodeEnum.getMessage();
  }

  public SimpleException(int code, String msg) {
    super(msg);
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public String getMsg() {
    return this.msg;
  }

  /**
   * 自动转换成自定义ErrorBean
   * 
   * @return
   */
  public ErrorBean toErrorBean() {
    return new ErrorBean(code, getMessage());
  }

  @Override
  public String toString() {
    return "SimpleException [code=" + code + ", msg=" + this.getMessage() + "]";
  }

}
