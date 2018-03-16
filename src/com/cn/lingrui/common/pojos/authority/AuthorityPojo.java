package com.cn.lingrui.common.pojos.authority;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AuthorityPojo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String roleId;
	private List<String> moduleRight = new ArrayList<String>();
	private List<String> functionRight = new ArrayList<String>();
	private List<String> pageRight = new ArrayList<String>();
	public List<String> getModuleRight() {
		return moduleRight;
	}
	public void setModuleRight(List<String> moduleRight) {
		this.moduleRight = moduleRight;
	}
	public List<String> getFunctionRight() {
		return functionRight;
	}
	public void setFunctionRight(List<String> functionRight) {
		this.functionRight = functionRight;
	}
	public List<String> getPageRight() {
		return pageRight;
	}
	public void setPageRight(List<String> pageRight) {
		this.pageRight = pageRight;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
}
