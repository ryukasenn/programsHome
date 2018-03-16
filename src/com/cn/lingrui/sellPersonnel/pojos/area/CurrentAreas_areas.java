package com.cn.lingrui.sellPersonnel.pojos.area;

import java.util.ArrayList;
import java.util.List;

import com.cn.lingrui.sellPersonnel.db.dbpojos.area.Area;

public class CurrentAreas_areas {

	private String PROVINCE_ID;
	private List<Area> areas = new ArrayList<>();
	public String getPROVINCE_ID() {
		return PROVINCE_ID;
	}
	public void setPROVINCE_ID(String pROVINCE_ID) {
		PROVINCE_ID = pROVINCE_ID;
	}
	public List<Area> getAreas() {
		return areas;
	}
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	
}
