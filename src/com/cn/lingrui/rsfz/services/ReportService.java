package com.cn.lingrui.rsfz.services;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.exoutCheckItems.CheckItemsIn;
import com.cn.lingrui.rsfz.pojos.report.BatchUpdateIn;
import com.cn.lingrui.rsfz.pojos.report.ReportPojoIn;

public interface ReportService {

	public ModelAndView createReport(ReportPojoIn in) throws Exception;
	public ModelAndView importOutWrongData(CheckItemsIn in) throws Exception;
	public ModelAndView upLoadBatchUpdate(BatchUpdateIn in) throws Exception;
}
