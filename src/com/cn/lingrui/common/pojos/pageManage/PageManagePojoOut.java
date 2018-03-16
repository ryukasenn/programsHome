package com.cn.lingrui.common.pojos.pageManage;

import java.util.ArrayList;
import java.util.List;

import com.cn.lingrui.common.pojos.BasePojoOut;

public class PageManagePojoOut extends BasePojoOut{


	private List<Module> modules = new ArrayList<>();

	public List<Module> getModules() {
		return modules;
	}

	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
	
}
