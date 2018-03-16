package com.cn.lingrui.rsfz.pojos.report;

import java.util.ArrayList;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.PMBASE;
import com.cn.lingrui.common.pojos.BasePojoOut;

public class ReportPojoOut extends BasePojoOut{

	private List<PMBASE> ListPMBASE = new ArrayList<PMBASE>(); // 查询到的员工信息
	
	private ReportData reportData = new ReportData();
	
	private List<PMBASE> needCheckList = new ArrayList<PMBASE>(); // 需要修改检查的员工信息
	
	public List<PMBASE> getListPMBASE() {
		return ListPMBASE;
	}

	public void setListPMBASE(List<PMBASE> listPMBASE) {
		ListPMBASE = listPMBASE;
	}

	public List<PMBASE> getNeedCheckList() {
		return needCheckList;
	}

	public void setNeedCheckList(List<PMBASE> needCheckList) {
		this.needCheckList = needCheckList;
	}

	public ReportData getReportData() {
		return reportData;
	}

	public void setReportData(ReportData reportData) {
		this.reportData = reportData;
	}

}
