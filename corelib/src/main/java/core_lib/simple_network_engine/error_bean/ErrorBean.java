package core_lib.simple_network_engine.error_bean;

import java.io.Serializable;

/**
 * 网络请求过程中, 发生错误时的数据Bean
 * 
 * @author zhihua.tang
 */
public final class ErrorBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4841567150604927632L;

	// 错误代码
	private final int code;
	// 错误描述信息
	private final String msg;

	public ErrorBean(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return "ErrorBean [code=" + code + ", msg=" + msg + "]";
	}

}
