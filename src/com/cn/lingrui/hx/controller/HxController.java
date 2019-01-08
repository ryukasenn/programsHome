package com.cn.lingrui.hx.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.hx.pojos.zdhx.HxProvincePojoIn;
import com.cn.lingrui.hx.pojos.zdhx.HxYwyPojoIn;
import com.cn.lingrui.hx.services.ZdhxService;
import com.cn.lingrui.common.utils.HttpUtil;


@Controller
@RequestMapping("/zdhx")
public class HxController {

	@Resource(name="zdhxService")
	private ZdhxService zdhxService;

	private static Logger log = LogManager.getLogger();
	/**
	 * 自动核销页面请求
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public ModelAndView province(HttpServletRequest req) {
		
//		log.info("用户: " + CommonUtil.getCookieValue(req, "userName") + " 进入自动核销功能页面");
		String[] XSDDHXS = new String[0];
		ModelAndView mv = HttpUtil.getModelAndView("04/04020101");
		mv.addObject("XSDDHXS", XSDDHXS);
//		mv.addObject("niko", CommonUtil.getCookieValue(req, "userName"));
		return mv;
	}
	

	/**
	 * 自动核销,根据省份核销
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/zdhxByProvince", method=RequestMethod.POST)
	public String provinceSelected(HxProvincePojoIn in) throws Exception {		

		return zdhxService.hxByProvinceSelect(in);
	}

	/**
	 * 自动核销,根据业务员核销
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/zdhxByYwy", method=RequestMethod.POST)
	public String ywySelected(HxYwyPojoIn in) throws Exception {		

		return zdhxService.hxByYwySelect(in);
	}

	/**
	 * 添加特殊处理
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/addNotHx", method=RequestMethod.POST)
	public String addNotHxXsddmx(String type,String dh, String cpbh) {
		
		return zdhxService.addNotHx(type,dh,cpbh);
	}
	
	/**
	 * 查特殊处理
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@RequestMapping(value = "/receiveNoHx", method=RequestMethod.POST)
	public String receiveNotHx(String type) {
		
		return zdhxService.receiveNoHx(type);
	}
	
}
