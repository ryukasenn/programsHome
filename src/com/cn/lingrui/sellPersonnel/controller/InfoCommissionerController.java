package com.cn.lingrui.sellPersonnel.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.check.CheckPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.infoCommissioner.InfoCommissionerPojoIn;
import com.cn.lingrui.sellPersonnel.service.InfoCommissionerService;

/**
 * 信息专员权限管理
 * @author lhc
 *
 */
@Controller
@RequestMapping("/sellPersonnel/infoCommissioner")
public class InfoCommissionerController {

	@Resource(name = "infoCommissionerService")
	private InfoCommissionerService infoCommissionerService;

	/**
	 * 获取未审核人员列表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/receiveUnchecks", method = RequestMethod.GET)
	public ModelAndView receiveUnchecks() throws Exception {

		ModelAndView mv = infoCommissionerService.receiveUnchecks();

		return mv;
	}
	
	/**
	 * 获取未审核人员详细信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/receiveUncheck", method = RequestMethod.POST)
	public ModelAndView receiveUncheck(CheckPojoIn in) throws Exception {

		ModelAndView mv = infoCommissionerService.receiveUncheck(in.getUNCHECKPID());

		return mv;
	}
	
	/**
	 * 获取该信息专员管理大区下所有人员信息展示
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/allTerminals", method = RequestMethod.GET)
	public ModelAndView receiveAllTerminals() throws Exception {

		ModelAndView mv = infoCommissionerService.receiveAllTerminals();

		return mv;
	}

	/**
	 * 信息专员点击大区后,获取大区下所有省份信息展示
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/regionTerminals", method = RequestMethod.GET)
	public ModelAndView receiveRegionTerminals(InfoCommissionerPojoIn in) throws Exception {

		ModelAndView mv = infoCommissionerService.receiveRegionTerminals(in);

		return mv;
	}
	
	/**
	 * 信息专员点击省份后,获取省份下所有地区信息展示
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/provinceTerminals", method = RequestMethod.GET)
	public ModelAndView receiveProvinceTerminals(InfoCommissionerPojoIn in) throws Exception {

		ModelAndView mv = infoCommissionerService.receiveProvinceTerminals(in);

		return mv;
	}
	
	/**
	 * 信息专员点击省份后,获取省份下所有地区信息展示
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/areaTerminals", method = RequestMethod.GET)
	public ModelAndView receiveAreaTerminals(InfoCommissionerPojoIn in) throws Exception {

		ModelAndView mv = infoCommissionerService.receiveAreaTerminals(in);

		return mv;
	}
	
	/**
	 * 信息专员点击省份后,获取省份下所有地区信息展示
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/terminal", method = RequestMethod.GET)
	public ModelAndView receiveTerminal(InfoCommissionerPojoIn in) throws Exception {

		ModelAndView mv = infoCommissionerService.receiveTerminal(in);

		return mv;
	}
}
