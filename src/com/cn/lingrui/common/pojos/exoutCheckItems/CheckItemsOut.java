package com.cn.lingrui.common.pojos.exoutCheckItems;

import java.util.ArrayList;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.PMBASE;
import com.cn.lingrui.common.pojos.BasePojoOut;

public class CheckItemsOut extends BasePojoOut{

	
	private List<PMBASE> itemsByAge = new ArrayList<PMBASE>();// 年龄相关错误数据
	private List<PMBASE> itemsByXl = new ArrayList<PMBASE>();// 学历相关错误数据
	private List<PMBASE> itemsByWorkAge = new ArrayList<PMBASE>();// 学历相关错误数据
	private List<PMBASE> itemsByWorkLevel = new ArrayList<PMBASE>();// 职务级别相关错误数据

	private ItemsOut itemsOut = new ItemsOut(); // 输出数据

	public List<PMBASE> getItemsByAge() {
		return itemsByAge;
	}

	public void setItemsByAge(List<PMBASE> itemsByAge) {
		this.itemsByAge = itemsByAge;
	}
	
	public ItemsOut getItemsOut() {
		return itemsOut;
	}

	public void setItemsOut(ItemsOut itemsOut) {
		this.itemsOut = itemsOut;
	}

	public List<PMBASE> getItemsByXl() {
		return itemsByXl;
	}

	public void setItemsByXl(List<PMBASE> itemsByXl) {
		this.itemsByXl = itemsByXl;
	}

	public List<PMBASE> getItemsByWorkAge() {
		return itemsByWorkAge;
	}

	public void setItemsByWorkAge(List<PMBASE> itemsByWorkAge) {
		this.itemsByWorkAge = itemsByWorkAge;
	}

	public List<PMBASE> getItemsByWorkLevel() {
		return itemsByWorkLevel;
	}

	public void setItemsByWorkLevel(List<PMBASE> itemsByWorkLevel) {
		this.itemsByWorkLevel = itemsByWorkLevel;
	}
}
