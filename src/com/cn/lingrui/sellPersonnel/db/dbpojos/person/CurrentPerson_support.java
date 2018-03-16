package com.cn.lingrui.sellPersonnel.db.dbpojos.person;

import java.util.List;

public class CurrentPerson_support {

	private List<CurrentPerson_statistics> currentPersons_region;
	private List<CurrentPerson_statistics> CurrentPersons_total;
	public List<CurrentPerson_statistics> getCurrentPersons_region() {
		return currentPersons_region;
	}
	public void setCurrentPersons_region(List<CurrentPerson_statistics> currentPersons_region) {
		this.currentPersons_region = currentPersons_region;
	}
	public List<CurrentPerson_statistics> getCurrentPersons_total() {
		return CurrentPersons_total;
	}
	public void setCurrentPersons_total(List<CurrentPerson_statistics> currentPersons_total) {
		CurrentPersons_total = currentPersons_total;
	}
	
	
}
