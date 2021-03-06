package com.cn.lingrui.rsfz.pojos.report;

import java.util.ArrayList;
import java.util.List;

public class BaseReport {
	
	/**
	 * 定义title是为确定页面展示的标题顺序
	 */
	protected List<String> title = new ArrayList<String>();
	
	protected List<List<String>> reportData = new ArrayList<List<String>>();
	public List<String> getTitle() {
		return title;
	}
	public void setTitle(List<String> title) {
		this.title = title;
	}
	public List<List<String>> getReportData() {
		return reportData;
	}
	public void setReportData(List<List<String>> reportData) {
		this.reportData = reportData;
	}
}
