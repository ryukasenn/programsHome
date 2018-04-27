package com.cn.lingrui.sellPersonnel.pojos.support;

import com.cn.lingrui.common.db.dbpojos.BaseReport;


public class EvaluationForm extends BaseReport{

	public EvaluationForm() {
		
		this.title = new String[] {"大区","省份","地区","姓名","应配人数","已配人数","已配推广经理人数"};
	}
}
