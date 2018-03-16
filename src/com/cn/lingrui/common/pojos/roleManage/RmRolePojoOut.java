package com.cn.lingrui.common.pojos.roleManage;

import java.util.ArrayList;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_PAGE;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_ROLE;
import com.cn.lingrui.common.pojos.BasePojoOut;

public class RmRolePojoOut extends BasePojoOut{

	private String modules;
	private String funcs;
	private String pages;
	private NBPT_RSFZ_ROLE rsfzrole;
	private List<NBPT_RSFZ_PAGE> moduleList = new ArrayList<NBPT_RSFZ_PAGE>();
	private List<NBPT_RSFZ_PAGE> funcList = new ArrayList<NBPT_RSFZ_PAGE>();
	private List<NBPT_RSFZ_PAGE> pageList = new ArrayList<NBPT_RSFZ_PAGE>();
	public List<NBPT_RSFZ_PAGE> getModuleList() {
		return moduleList;
	}
	public void setModuleList(List<NBPT_RSFZ_PAGE> moduleList) {
		this.moduleList = moduleList;
	}
	public List<NBPT_RSFZ_PAGE> getPageList() {
		return pageList;
	}
	public void setPageList(List<NBPT_RSFZ_PAGE> pageList) {
		this.pageList = pageList;
	}
	public NBPT_RSFZ_ROLE getRsfzrole() {
		return rsfzrole;
	}
	public void setRsfzrole(NBPT_RSFZ_ROLE rsfzrole) {
		this.rsfzrole = rsfzrole;
	}
	public String getModules() {
		return modules;
	}
	public void setModules(String modules) {
		this.modules = modules;
	}
	public String getFuncs() {
		return funcs;
	}
	public void setFuncs(String funcs) {
		this.funcs = funcs;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	public List<NBPT_RSFZ_PAGE> getFuncList() {
		return funcList;
	}
	public void setFuncList(List<NBPT_RSFZ_PAGE> funcList) {
		this.funcList = funcList;
	}
	
	
}
