package com.cn.lingrui.common.pojos.login;

import com.cn.lingrui.common.pojos.BasePojoOut;

public class LoginPojoOut extends BasePojoOut{

	private Boolean flag = null;
	private String userName = "";
	private String userRole = "";
	private CurrentUser userInfo;
	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public CurrentUser getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(CurrentUser userInfo) {
		this.userInfo = userInfo;
	}
}
