package com.cn.lingrui.common.pojos.login;

public class LoginPojoIn {

	/**
	 * 登录用户名
	 */
	private String userId;
	
	/**
	 * 登录密码
	 */
	private String password;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
