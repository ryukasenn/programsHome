package com.cn.lingrui.sellPersonnel.pojos.check;

import java.util.ArrayList;
import java.util.List;

import com.cn.lingrui.sellPersonnel.db.dbpojos.check.UncheckPerson;

public class CurrentAreas_areas {

	private String PROVINCE_ID;
	private List<UncheckPerson> areas = new ArrayList<>();
	public String getPROVINCE_ID() {
		return PROVINCE_ID;
	}
	public void setPROVINCE_ID(String pROVINCE_ID) {
		PROVINCE_ID = pROVINCE_ID;
	}
	public List<UncheckPerson> getAreas() {
		return areas;
	}
	public void setAreas(List<UncheckPerson> areas) {
		this.areas = areas;
	}
	
}
