package com.cn.lingrui.common.pojos.pageManage;

public class PmFuncPojoIn {

	private String funcId; // 功能ID
	private String funcBz; // 功能描述
	private String moduleId; // 所属模块ID
	private String funcName; // 功能名称
	public String getFuncId() {
		return funcId;
	}
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	public String getFuncBz() {
		return funcBz;
	}
	public void setFuncBz(String funcBz) {
		this.funcBz = funcBz;
	}
	public String getFuncType() {
		return "1";
	}
	public String getFuncPid() {
		return this.getModuleId();
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getFuncName() {
		return funcName;
	}
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	
	
}
