package com.cn.lingrui.rsfz.pojos.report;


public class ReportData {

	private AgeReport ageReport = null; // 年龄报表数据

	private XlReport xlReport = null; // 学历报表数据
	
	private SexReport sexReport = null; // 性别报表数据
	
	private WorkLevelReport workLevelReport = null; // 性别报表数据
	
	private WorkAgeReport workAgeReport = null; // 性别报表数据

	public AgeReport getAgeReport() {
		return ageReport;
	}

	public void setAgeReport(AgeReport ageReport) {
		this.ageReport = ageReport;
	}

	public XlReport getXlReport() {
		return xlReport;
	}

	public void setXlReport(XlReport xlReport) {
		this.xlReport = xlReport;
	}

	public SexReport getSexReport() {
		return sexReport;
	}

	public void setSexReport(SexReport sexReport) {
		this.sexReport = sexReport;
	}

	public WorkLevelReport getWorkLevelReport() {
		return workLevelReport;
	}

	public void setWorkLevelReport(WorkLevelReport workLevelReport) {
		this.workLevelReport = workLevelReport;
	}

	public WorkAgeReport getWorkAgeReport() {
		return workAgeReport;
	}

	public void setWorkAgeReport(WorkAgeReport workAgeReport) {
		this.workAgeReport = workAgeReport;
	}


}
