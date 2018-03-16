package com.cn.lingrui.rsfz.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.pojos.exoutCheckItems.CheckItemsIn;
import com.cn.lingrui.rsfz.pojos.report.BatchUpdateIn;
import com.cn.lingrui.rsfz.pojos.report.ReportPojoIn;
import com.cn.lingrui.rsfz.services.ReportService;

@Controller
@RequestMapping("/report")
public class ReportController {
	
	@Resource(name="reportService")
	private ReportService reportService;
		
	/**
	 * 生成报表
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="", method=RequestMethod.POST)
	public ModelAndView createReport(ReportPojoIn in) throws Exception {
		
		ModelAndView mv = reportService.createReport(in);
		return mv;
	}

	/**
	 * 导出报表
	 * @param in
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/exout", method=RequestMethod.POST)
	public ModelAndView exout(CheckItemsIn in) throws Exception {
		
		ModelAndView mv = reportService.importOutWrongData(in);
		
		return mv;
	}
	
	/**
	 * 上传批量导入
	 * @param in
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/batchUpdate", method=RequestMethod.POST)	
	public ModelAndView batchUpdate(BatchUpdateIn in) throws Exception {
		
		ModelAndView mv = reportService.upLoadBatchUpdate(in);
		return mv;
	}
}
