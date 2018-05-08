package com.cn.lingrui.pm.pojos;

public class PmInfo {

	private String RQ;//日期月份
	private String XH;//序号
	private String DQH;//地区号
	private String RYXM;//人员姓名
	private String PM;//排名
	private String DW;//段位
	private String QS;//趋势
	private String LRB;//羚锐币
	public String getRQ() {
		return RQ;
	}
	public void setRQ(String rQ) {
		RQ = rQ;
	}
	public String getXH() {
		return XH;
	}
	public void setXH(String xH) {
		XH = xH;
	}
	public String getDQH() {
		return DQH;
	}
	public void setDQH(String dQH) {
		DQH = dQH;
	}
	public String getRYXM() {
		return RYXM;
	}
	public void setRYXM(String rYXM) {
		RYXM = rYXM;
	}
	public String getPM() {
		return PM;
	}
	public void setPM(String pM) {
		PM = pM;
	}
	public String getDW() {
		return DW;
	}
	public void setDW(String dW) {
		DW = dW;
	}
	public String getQS() {
		return QS;
	}
	public void setQS(String qS) {
		QS = qS;
	}
	public String getLRB() {
		return LRB;
	}
	public void setLRB(String lRB) {
		LRB = lRB;
	}
	public PmInfo(){}
	public PmInfo(String rQ, String xH, String dQH, String rYXM, String pM, String dW, String qS, String lRB) {
		super();
		RQ = rQ;
		XH = xH;
		DQH = dQH;
		RYXM = rYXM;
		PM = pM;
		DW = dW;
		QS = qS;
		LRB = lRB;
	}
	@Override
	public String toString() {
		return "PmInfo [RQ=" + RQ + ", XH=" + XH + ", DQH=" + DQH + ", RYXM=" + RYXM + ", PM=" + PM + ", DW=" + DW
				+ ", QS=" + QS + ", LRB=" + LRB + "]";
	}
	
	
	
}
