package com.cn.lingrui.sellPersonnel.pojos.check;

import java.util.List;

import com.cn.lingrui.sellPersonnel.db.dbpojos.infoCommissioner.Region;

public class CurrentAreasPojo {

	private List<Region> regions;
	private List<CurrentAreas_provinces> provinces;
	private List<CurrentAreas_areas> areas;
	public List<Region> getRegions() {
		return regions;
	}
	public void setRegions(List<Region> regions) {
		this.regions = regions;
	}
	public List<CurrentAreas_provinces> getProvinces() {
		return provinces;
	}
	public void setProvinces(List<CurrentAreas_provinces> provinces) {
		this.provinces = provinces;
	}
	public List<CurrentAreas_areas> getAreas() {
		return areas;
	}
	public void setAreas(List<CurrentAreas_areas> areas) {
		this.areas = areas;
	}
	
	
}
