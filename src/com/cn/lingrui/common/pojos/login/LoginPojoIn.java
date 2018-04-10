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
	
	/**
	 * 登录模式 1:OA连接登录;无:普通登录
	 */
	private String loginModel;
	
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
	public String getLoginModel() {
		return loginModel;
	}
	public void setLoginModel(String loginModel) {
		this.loginModel = loginModel;
	}
}
