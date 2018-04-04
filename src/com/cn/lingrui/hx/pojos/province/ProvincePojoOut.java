package com.cn.lingrui.hx.pojos.province;

import java.util.ArrayList;
import java.util.List;

public class ProvincePojoOut {

	private List<ClassifyByshdkh_rybh> results = new ArrayList<ClassifyByshdkh_rybh>();
	
	private List<String> checkList = new ArrayList<String>();

	public List<ClassifyByshdkh_rybh> getResults() {
		return results;
	}

	public void setResults(List<ClassifyByshdkh_rybh> results) {
		this.results = results;
	}

	public List<String> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<String> checkList) {
		this.checkList = checkList;
	}
	
}
