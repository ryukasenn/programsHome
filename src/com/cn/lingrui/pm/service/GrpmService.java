package com.cn.lingrui.pm.service;

import org.springframework.web.servlet.ModelAndView;

public interface GrpmService {

	public ModelAndView getGrpmInfoView() throws Exception;

	public ModelAndView getSameDqPmView(String pmrq) throws Exception;
	
	
	public ModelAndView getslbView(String pmrq,String mc) throws Exception;
	

	public ModelAndView getgrxxView(String rq, String xm, String dw) throws Exception;
}
