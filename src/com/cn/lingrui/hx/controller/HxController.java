package com.cn.lingrui.hx.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.hx.services.ZdhxService;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.hx.pojos.province.ProvincePojoIn;

public class HxController {

	
	@Resource
	private ZdhxService zdhxService;
	/**
	 * 自动核销页面请求
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/province", method=RequestMethod.GET)
	public ModelAndView province(HttpServletRequest req) {
		
//		log.info("用户: " + CommonUtil.getCookieValue(req, "userName") + " 进入自动核销功能页面");
		String[] XSDDHXS = new String[0];
		ModelAndView mv = HttpUtil.getModelAndView("hx/provinceSel", "自动核销");
		mv.addObject("XSDDHXS", XSDDHXS);
//		mv.addObject("niko", CommonUtil.getCookieValue(req, "userName"));
		return mv;
	}
	

	/**
	 * 自动核销,根据省份核销
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/province", method=RequestMethod.POST)
	public ModelAndView provinceSelected(HttpServletRequest req, ProvincePojoIn in) {		

//		log.info("用户: " + CommonUtil.getCookieValue(req, "userName") + " 选择了" + in.getProvince() + "进行自动核销");
//		in.setUserName(CommonUtil.getCookieValue(req, "userName"));
		ModelAndView mv = null;
		try {
			mv = zdhxService.provinceSelected(in);
		}catch (Exception e) {
			System.out.println(e.getMessage());//:获得错误信息
			e.printStackTrace();//：在控制台打印出异常种类，错误信息和出错位置等
		}
		return mv;
	}


}
