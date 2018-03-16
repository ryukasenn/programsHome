package com.cn.lingrui.common.pojos.pageManage;

public class PmPagePojoIn {

	private String pageId; // 页面ID
	private String pageBz; // 页面描述
	private String funcId; // 所属功能ID
	private String pageName; // 页面名称
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public String getPageBz() {
		return pageBz;
	}
	public void setPageBz(String pageBz) {
		this.pageBz = pageBz;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPageType() {
		
		return "2";
	}
	public String getPagePid() {
		return this.getFuncId();
	}
	private String getFuncId() {
		return funcId;
	}
	public void setFuncId(String funcId) {
		this.funcId = funcId;
	}
	
}
