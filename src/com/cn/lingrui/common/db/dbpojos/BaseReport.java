package com.cn.lingrui.common.db.dbpojos;

import java.util.ArrayList;
import java.util.List;

public class BaseReport {

	/**
	 * 定义title是为确定页面展示的标题顺序
	 */
	protected String[] title;
	
	protected List<List<String>> reportData = new ArrayList<List<String>>();
	
	public List<List<String>> getReportData() {
		return reportData;
	}
	public void setReportData(List<List<String>> reportData) {
		this.reportData = reportData;
	}
	public String[] getTitle() {
		return title;
	}
	public void setTitle(String[] title) {
		this.title = title;
	}
}
