package com.cn.lingrui.pm.service;

import org.springframework.web.servlet.ModelAndView;

public interface AllpmService {

	public ModelAndView getAllpmInfoView() throws Exception;
	
	public ModelAndView getPmCxView(String ... args) throws Exception;

	
}
