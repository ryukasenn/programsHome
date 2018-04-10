package com.cn.lingrui.sellPersonnel.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.sellPersonnel.service.PersonManageService;

@Controller
@RequestMapping("/sellPersonnel")
public class PersonManageController {

	@Resource(name = "personManageService")
	private PersonManageService personManageService;

	/**
	 * 根据大区UID,生成省区人员信息列表
	 * @param regionUid
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/provincePersons", method = RequestMethod.GET)
	public ModelAndView getProvincePersons(String regionUid) throws Exception {
		

		ModelAndView mv = personManageService.getProvincePersons(regionUid);
		return mv;
	}
	
	/**
	 * 根据地区UID,生成地区人员信息列表
	 * @param regionUid
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/areaPersons", method = RequestMethod.GET)
	public ModelAndView getAreaPersons(String areaUid, String regionUid) throws Exception {
		

		ModelAndView mv = personManageService.getAreaPersons(areaUid, regionUid);
		return mv;
	}
	
	/**
	 * 根据地区人员PID,获取人员详细信息
	 * @param regionUid
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/receivePerson", method = RequestMethod.GET)
	public ModelAndView receivePerson(String areaUid, String regionUid, String personPid) throws Exception {
		

		ModelAndView mv = personManageService.receivePerson(areaUid,regionUid,personPid);
		return mv;
	}

}
