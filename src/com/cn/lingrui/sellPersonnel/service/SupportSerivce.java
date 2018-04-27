package com.cn.lingrui.sellPersonnel.service;

import org.springframework.web.servlet.ModelAndView;

public interface SupportSerivce {

	/**
	 * 获取添加管理人员页面
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getSupportAdd() throws Exception;

	/**
	 * 生成考核列表
	 * @param endTime 截止时间
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView createEvaluationForm(String endTime) throws Exception;
}
