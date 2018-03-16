package com.cn.lingrui.rsfz.pojos.report;

import java.util.List;

public class WorkLevelReport extends BaseReport{

	@Override
	public List<String> getTitle() {
		if(0 == title.size()) {

			title.add("人事一级部门");
			title.add("高管");
			title.add("总监");
			title.add("中层正职");
			title.add("中层副职");
			title.add("主管");
			title.add("员工");
		}
		return title;
	}
}
