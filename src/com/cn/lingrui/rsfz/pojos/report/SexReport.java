package com.cn.lingrui.rsfz.pojos.report;

import java.util.List;

public class SexReport extends BaseReport{

	@Override
	public List<String> getTitle() {
		if(0 == title.size()) {

			title.add("人事一级部门");
			title.add("男");
			title.add("女");
		}
		return title;
	}
	
}
