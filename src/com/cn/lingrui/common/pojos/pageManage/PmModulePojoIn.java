package com.cn.lingrui.common.pojos.pageManage;

public class PmModulePojoIn {

	private String moduleId; // 模块ID
	private String moduleBz; // 模块描述
	private String moduleName; // 模块名称
	
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleBz() {
		return moduleBz;
	}
	public void setModuleBz(String moduleBz) {
		this.moduleBz = moduleBz;
	}
	public String getModuleType() {
		return "0";
	}
	public String getModulePid() {
		return this.moduleId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	
}
