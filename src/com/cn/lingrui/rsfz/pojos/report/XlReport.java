package com.cn.lingrui.rsfz.pojos.report;

import java.util.List;

public class XlReport extends BaseReport{

	@Override
	public List<String> getTitle() {
		if(0 == title.size()) {

			title.add("人事一级部门");
			title.add("初中");
			title.add("高中");
			title.add("中专");
			title.add("大专");
			title.add("本科");
			title.add("研究生");
			title.add("博士");
		}
		return title;
	}
	
}
