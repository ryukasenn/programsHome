package com.cn.lingrui.rsfz.pojos.report;

import java.util.ArrayList;
import java.util.List;

public class Report {
	private String reportName;
	private String reportId;
	private List<String> items = null;
	public List<String> getItems() {
		return items;
	}
	public void setItems(Integer length) {
		
		
		this.items = new ArrayList<String>();
		
		for(int i = 0; i < length; i++) {
			this.items.add("0");
		}
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}
}
