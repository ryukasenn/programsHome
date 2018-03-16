package com.cn.lingrui.common.pojos.exoutCheckItems;

import java.util.List;

public class ItemsOut {

	private List<Items> checkItemsByAge = null;
	private List<Items> checkItemsByXl = null;
	private List<Items> checkItemsByWorkAge = null;
	private List<Items> checkItemsByWorkLevel = null;

	public List<Items> getCheckItemsByAge() {
		return checkItemsByAge;
	}

	public void setCheckItemsByAge(List<Items> checkItemsByAge) {
		this.checkItemsByAge = checkItemsByAge;
	}

	public List<Items> getCheckItemsByXl() {
		return checkItemsByXl;
	}

	public void setCheckItemsByXl(List<Items> checkItemsByXl) {
		this.checkItemsByXl = checkItemsByXl;
	}

	public List<Items> getCheckItemsByWorkAge() {
		return checkItemsByWorkAge;
	}

	public void setCheckItemsByWorkAge(List<Items> checkItemsByWorkAge) {
		this.checkItemsByWorkAge = checkItemsByWorkAge;
	}

	public List<Items> getCheckItemsByWorkLevel() {
		return checkItemsByWorkLevel;
	}

	public void setCheckItemsByWorkLevel(List<Items> checkItemsByWorkLevel) {
		this.checkItemsByWorkLevel = checkItemsByWorkLevel;
	}
}
