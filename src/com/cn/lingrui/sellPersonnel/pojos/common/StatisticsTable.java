package com.cn.lingrui.sellPersonnel.pojos.common;

public class StatisticsTable {

	private String regionName; // 大区名称
	private String provinceName; // 省区名称
	private String areaName; // 地区名称
	private Integer need = 0; // 配额
	private Integer balance = 0; // 大区差额
	private Integer regionResper = 0; // 大区总
	private Integer areaResper = 0; // 地区总
	private Integer xzquResper = 0; // 区县总
	private Integer xzquResper_preparatory = 0; // 预备区县总
	private Integer promote = 0; // 推广经理
	private Integer dismission = 0; // 离职数量
	private Integer total = 0; // 合计数量
	private String dismissionRate = ""; // 离职率
	private String regionUid = ""; // 大区UID
	private String areaUid = ""; // 地区UID
	private String provinceId = ""; // 省份ID
	private String terminalPid = ""; // 人员PID
	private String type = "";
	
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Integer getNeed() {
		return need;
	}
	public void setNeed(Integer need) {
		this.need = need;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	public Integer getRegionResper() {
		return regionResper;
	}
	public void setRegionResper(Integer regionResper) {
		this.regionResper = regionResper;
	}
	public Integer getAreaResper() {
		return areaResper;
	}
	public void setAreaResper(Integer areaResper) {
		this.areaResper = areaResper;
	}
	public Integer getXzquResper() {
		return xzquResper;
	}
	public void setXzquResper(Integer xzquResper) {
		this.xzquResper = xzquResper;
	}
	public Integer getXzquResper_preparatory() {
		return xzquResper_preparatory;
	}
	public void setXzquResper_preparatory(Integer xzquResper_preparatory) {
		this.xzquResper_preparatory = xzquResper_preparatory;
	}
	public Integer getPromote() {
		return promote;
	}
	public void setPromote(Integer promote) {
		this.promote = promote;
	}
	public Integer getDismission() {
		return dismission;
	}
	public void setDismission(Integer dismission) {
		this.dismission = dismission;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public String getDismissionRate() {
		return dismissionRate;
	}
	public void setDismissionRate(String dismissionRate) {
		this.dismissionRate = dismissionRate;
	}
	public String getRegionUid() {
		return regionUid;
	}
	public void setRegionUid(String regionUid) {
		this.regionUid = regionUid;
	}
	public String getAreaUid() {
		return areaUid;
	}
	public void setAreaUid(String areaUid) {
		this.areaUid = areaUid;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getTerminalPid() {
		return terminalPid;
	}
	public void setTerminalPid(String terminalPid) {
		this.terminalPid = terminalPid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
