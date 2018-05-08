package com.cn.lingrui.sellPersonnel.service;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.infoCommissioner.InfoCommissionerPojoIn;

public interface InfoCommissionerService {
	
	/**
	 * 获取该信息专员管理大区下所有人员信息展示
	 * @return
	 * @throws Exception
	 */
	public ModelAndView receiveAllTerminals() throws Exception;
	
	/**
	 * 信息专员点击大区后,获取大区下所有省份信息展示
	 * @return
	 * @throws Exception
	 */
	public ModelAndView receiveRegionTerminals(InfoCommissionerPojoIn in) throws Exception;
	
	/**
	 * 信息专员点击地区后,获取地区下所有人员信息展示
	 * @return
	 * @throws Exception
	 */
	public ModelAndView receiveAreaTerminals(InfoCommissionerPojoIn in) throws Exception;
	
	/**
	 * 信息专员点击省份后,获取省份下所有地区信息展示
	 * @return
	 * @throws Exception
	 */
	public ModelAndView receiveProvinceTerminals(InfoCommissionerPojoIn in) throws Exception;

	/**
	 * 点击人员名称,获取人员详细信息展示
	 * @param in
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView receiveTerminal(InfoCommissionerPojoIn in) throws Exception;

}
