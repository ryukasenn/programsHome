package com.cn.lingrui.common.pojos.roleManage;

public class RmRolePojoIn {

	private String NBPT_RSFZ_ROLE_ID;
	private String NBPT_RSFZ_ROLE_NAME;
	private String NBPT_RSFZ_ROLE_BZ;
	private String[] modules;
	private String[] funcs;
	private String[] pages;
	
	public String getRSFZROLE_LEVEL() {
		return "1";
	}
	public String[] getModules() {
		return modules;
	}
	public void setModules(String[] modules) {
		this.modules = modules;
	}
	public String[] getFuncs() {
		return funcs;
	}
	public void setFuncs(String[] funcs) {
		this.funcs = funcs;
	}
	public String[] getPages() {
		return pages;
	}
	public void setPages(String[] pages) {
		this.pages = pages;
	}
	public String getNBPT_RSFZ_ROLE_ID() {
		return NBPT_RSFZ_ROLE_ID;
	}
	public void setNBPT_RSFZ_ROLE_ID(String nBPT_RSFZ_ROLE_ID) {
		NBPT_RSFZ_ROLE_ID = nBPT_RSFZ_ROLE_ID;
	}
	public String getNBPT_RSFZ_ROLE_NAME() {
		return NBPT_RSFZ_ROLE_NAME;
	}
	public void setNBPT_RSFZ_ROLE_NAME(String nBPT_RSFZ_ROLE_NAME) {
		NBPT_RSFZ_ROLE_NAME = nBPT_RSFZ_ROLE_NAME;
	}
	public String getNBPT_RSFZ_ROLE_BZ() {
		return NBPT_RSFZ_ROLE_BZ;
	}
	public void setNBPT_RSFZ_ROLE_BZ(String nBPT_RSFZ_ROLE_BZ) {
		NBPT_RSFZ_ROLE_BZ = nBPT_RSFZ_ROLE_BZ;
	}
	
}
