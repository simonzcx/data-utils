package com.mingcloud.data.common;

public class ServiceResponse<T> {
	private int status;
	private String message;
	private T data;
	
	public ServiceResponse() {
	}
	
	public ServiceResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public ServiceResponse(int status, String message, T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}
	// 成功status=200 message=ok
	public static <T> ServiceResponse<T> ok() {
		return new ServiceResponse<T>(200, "ok");
	}
	// 成功status=200 message=ok
	public static <T> ServiceResponse<T> ok(T data) {
		return new ServiceResponse<T>(200, "ok", data);
	}
	// 失败status=200 message=ERROR
	public static <T> ServiceResponse<T> error() {
		return new ServiceResponse<T>(500, "ERROR");
	}
	public static <T> ServiceResponse<T> error(T data) {
		return new ServiceResponse<T>(500, "ERROR", data);
	}
	//set get
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
