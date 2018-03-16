package com.cn.lingrui.common.pojos;

import java.util.ArrayList;
import java.util.List;

public class RightsPojo {

	private List<String> modulesRight = new ArrayList<String>();
	private List<String> functionsRight = new ArrayList<String>();
	public List<String> getModulesRight() {
		return modulesRight;
	}
	public void setModulesRight(List<String> modulesRight) {
		this.modulesRight = modulesRight;
	}
	public List<String> getFunctionsRight() {
		return functionsRight;
	}
	public void setFunctionsRight(List<String> functionsRight) {
		this.functionsRight = functionsRight;
	}
	
}
