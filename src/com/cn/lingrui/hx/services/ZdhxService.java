package com.cn.lingrui.hx.services;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.hx.pojos.province.ProvincePojoIn;

public interface ZdhxService {

	public ModelAndView provinceSelected(ProvincePojoIn in) throws Exception;
}
