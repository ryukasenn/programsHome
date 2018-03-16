package com.cn.lingrui.common.pojos.pageManage;

import java.util.ArrayList;
import java.util.List;

public class Module {

	/**
	 * 模块ID
	 */
	private String moduleId;
	
	/**
	 * 模块名称
	 */
	private String moduleName;

	/**
	 * 模块描述
	 */
	private String moduleBz;
	/**
	 * 该模块所包含的功能
	 */
	private List<Func> funcList = new ArrayList<Func>();
	
	public List<Func> getFuncList() {
		return funcList;
	}
	public void setFuncList(List<Func> funcList) {
		this.funcList = funcList;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getModuleBz() {
		return moduleBz;
	}
	public void setModuleBz(String moduleBz) {
		this.moduleBz = moduleBz;
	}
}
