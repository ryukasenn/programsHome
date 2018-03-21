package com.cn.lingrui.sellPersonnel.db.dbpojos.person;

public class CurrentPerson_statistics {

	private String name = ""; // 大区名称
	private Integer need = 0; // 大区配额
	private Integer regionResper = 0; // 大区总
	private Integer areaResper = 0; // 地区总
	private Integer xzquResper = 0; // 区县总
	private Integer xzquResper_preparatory = 0; // 预备区县总
	private Integer promote = 0; // 推广经理
	private Integer dismission = 0; // 离职数量
	private Integer total = 0; // 离职数量
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
	


}
