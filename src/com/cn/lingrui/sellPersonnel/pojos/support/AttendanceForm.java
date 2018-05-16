package com.cn.lingrui.sellPersonnel.pojos.support;

import com.cn.lingrui.common.db.dbpojos.BaseReport;

public class AttendanceForm extends BaseReport {

	public AttendanceForm() {
		
		this.title = new String[] {"大区","地区","地总","姓名","性别","出勤天数","职务","身份证号","联系方式"};
	}
}
