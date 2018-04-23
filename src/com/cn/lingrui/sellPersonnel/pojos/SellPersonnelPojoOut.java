package com.cn.lingrui.sellPersonnel.pojos;

import java.util.List;
import java.util.Map;

import com.cn.lingrui.common.pojos.BasePojoOut;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;

public class SellPersonnelPojoOut extends BasePojoOut{

	private String loginDeptId; // 登录人员所在部门
	
	private NBPT_VIEW_CURRENTPERSON person; // 登录人员信息
	
	private Map<String, List<NBPT_SP_PERSON>> resultMap; // 页面显示列表
	
	private String maxId; // 当前人员表中最大编号(新)

	public String getLoginDeptId() {
		return loginDeptId;
	}

	public void setLoginDeptId(String loginDeptId) {
		this.loginDeptId = loginDeptId;
	}

	public Map<String, List<NBPT_SP_PERSON>> getResultMap() {
		return resultMap;
	}

	public void setResultMap(Map<String, List<NBPT_SP_PERSON>> resultMap) {
		this.resultMap = resultMap;
	}

	public String getMaxId() {
		return maxId;
	}

	public void setMaxId(String maxId) {
		this.maxId = maxId;
	}

	public NBPT_VIEW_CURRENTPERSON getPerson() {
		return person;
	}

	public void setPerson(NBPT_VIEW_CURRENTPERSON person) {
		this.person = person;
	}

}
