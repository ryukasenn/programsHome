package com.cn.lingrui.pm.pojos;

public class DwInfo {

	private String ID;//段位id
	private String DWMC;//段位名称
	private String DWBH;//段位编号
	private String DWWC;//段位位次
	private String DW;//段位
	private String WCBH;//位次编号
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getDWMC() {
		return DWMC;
	}
	public void setDWMC(String dWMC) {
		DWMC = dWMC;
	}
	public String getDWBH() {
		return DWBH;
	}
	public void setDWBH(String dWBH) {
		DWBH = dWBH;
	}
	public String getDWWC() {
		return DWWC;
	}
	public void setDWWC(String dWWC) {
		DWWC = dWWC;
	}
	public String getDW() {
		return DW;
	}
	public void setDW(String dW) {
		DW = dW;
	}
	public String getWCBH() {
		return WCBH;
	}
	public void setWCBH(String wCBH) {
		WCBH = wCBH;
	}
	public DwInfo() {
		
	}
	public DwInfo(String iD, String dWMC, String dWBH, String dWWC, String dW, String wCBH) {
		super();
		ID = iD;
		DWMC = dWMC;
		DWBH = dWBH;
		DWWC = dWWC;
		DW = dW;
		WCBH = wCBH;
	}
	
	@Override
	public String toString() {
		return "DwInfo [ID=" + ID + ", DWMC=" + DWMC + ", DWBH=" + DWBH + ", DWWC=" + DWWC + ", DW=" + DW + ", WCBH="
				+ WCBH + "]";
	}
	
	
	
	
	
	
	
	
	
}

