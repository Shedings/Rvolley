package com.volley.procotol;

public class ResponseProtocol {
	 // success section 2xxxxx 成功
    public static int CODE_COMMON_SUCCESS = 200000;
    String MSG_COMMON_SUCCESS = "ok";

    // error section 4xxxxx 各种失败
    /**
     * 表示一个默认提示信息（error）、无业务数据信息的失败回执。通常用来表示无需特别错误提示信息的失败情况。
     */
    int CODE_COMMON_ERROR = 400000;
    String MSG_COMMON_ERROR = "error";
    /**
     * 表示一个有自定义提示信息、无业务数据信息的失败回执。通常用在直接展示自定义提示信息的场景。
     */
    int CODE_ERROR_WITH_MSG = 400001;
    /**
     * 表示一个有自定义提示信息、有具体业务数据信息的失败回执。通常用在告知客户端失败，并且提供一些可用的业务数据。
     */
    int CODE_ERROR_WITH_MSG_AND_DATA = 400002;
    /**
     * 认证失败，需要登录
     */
    int CODE_AUTHORIZATION_ERROR = 401000;
    String MSG_AUTHORIZATION_ERROR = "authorization error";
    /**
     * 授权失败
     */
    int CODE_FORBIDDEN_ERROR = 403000;
    String MSG_FORBIDDEN_ERROR = "forbidden error";

    // exception section 5xxxxx 服务端内部异常，通常客户端无法处理
    int CODE_COMMON_EXCEPTION = 500000;
    String MSG_COMMON_EXCEPTION = "common exception";
    
    private long code;
    private String data;
    private String msg;
    private String complete;
    
	public ResponseProtocol(long code, String data, String msg, String complete) {
		super();
		this.code = code;
		this.data = data;
		this.msg = msg;
		this.complete = complete;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getComplete() {
		return complete;
	}

	public void setComplete(String complete) {
		this.complete = complete;
	}
}