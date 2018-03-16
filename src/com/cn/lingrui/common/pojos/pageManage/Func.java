package com.cn.lingrui.common.pojos.pageManage;

import java.util.ArrayList;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_PAGE;

public class Func {

	private String funcId;
	private String funcName;
	private String funcBz;
	private List<NBPT_RSFZ_PAGE> pageList = new ArrayList<NBPT_RSFZ_PAGE>();

	public String getFuncId() {
		return funcId;
	}

	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}

	public List<NBPT_RSFZ_PAGE> getPageList() {
		return pageList;
	}

	public void setPageList(List<NBPT_RSFZ_PAGE> pageList) {
		this.pageList = pageList;
	}


	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getFuncBz() {
		return funcBz;
	}

	public void setFuncBz(String funcBz) {
		this.funcBz = funcBz;
	}
}
