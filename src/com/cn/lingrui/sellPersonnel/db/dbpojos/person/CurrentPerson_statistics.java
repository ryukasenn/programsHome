package com.cn.lingrui.sellPersonnel.db.dbpojos.person;

public class CurrentPerson_statistics {

	// 统计OTC全部信息
	private String name = ""; // 大区名称
	private String uid = ""; // 大区UID或者地区UID
	private Integer need = 0; // 大区配额
	private String balance = ""; // 大区差额
	private Integer regionResper = 0; // 大区总
	private Integer areaResper = 0; // 地区总
	private Integer xzquResper = 0; // 区县总
	private Integer xzquResper_preparatory = 0; // 预备区县总
	private Integer promote = 0; // 推广经理
	private Integer dismission = 0; // 离职数量
	private Integer total = 0; // 离职数量
	private String dismissionRate = ""; // 离职数量
	private String regionUid = ""; // uid为地区UID时,regionUid为大区UID
	
	// 统计OTC大区下省份人员信息
	private String provinceId = ""; // 省份ID
	private String provinceName = ""; // 省份名称
	
	
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Integer getNeed() {
		return need;
	}
	public void setNeed(Integer need) {
		this.need = need;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
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
	


}
