package com.cn.lingrui.pm.pojos;

public class DwInfo {

	private String id;//段位id
	private String dwmc;//段位名称
	private String dwbh;//段位编号
	private String dwwc;//段位位次
	private String dw;//段位
	private String wcbh;//位次编号
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDwmc() {
		return dwmc;
	}
	public void setDwmc(String dwmc) {
		this.dwmc = dwmc;
	}
	public String getDwbh() {
		return dwbh;
	}
	public void setDwbh(String dwbh) {
		this.dwbh = dwbh;
	}
	public String getDwwc() {
		return dwwc;
	}
	public void setDwwc(String dwwc) {
		this.dwwc = dwwc;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public String getWcbh() {
		return wcbh;
	}
	public void setWcbh(String wcbh) {
		this.wcbh = wcbh;
	}
	
	
	public DwInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DwInfo(String id, String dwmc, String dwbh, String dwwc, String dw, String wcbh) {
		super();
		this.id = id;
		this.dwmc = dwmc;
		this.dwbh = dwbh;
		this.dwwc = dwwc;
		this.dw = dw;
		this.wcbh = wcbh;
	}
	@Override
	public String toString() {
		return "DwInfo [id=" + id + ", dwmc=" + dwmc + ", dwbh=" + dwbh + ", dwwc=" + dwwc + ", dw=" + dw + ", wcbh="
				+ wcbh + "]";
	}
	
	
	
	
	
	
}

