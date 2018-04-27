package com.cn.lingrui.sellPersonnel.service;

import org.springframework.web.servlet.ModelAndView;

public interface CommonService {

	/**
	 * 查询当前所有人(根据不同权限,生成不同页面)
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView receiveCurrentTerminals() throws Exception;
	
	/**
	 * 查询当前所有人员信息(根据不同权限,生成不同页面)
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView receiveAllCurrentTerminals() throws Exception;

	public String receiveSelect(String parentId) throws Exception;

	/**
	 * AJAX通过地区ID,获取地区下辖行政区县列表
	 * @param areaId 地区ID
	 * @return
	 * @throws Exception
	 */
	public String receiveAreasSelect(String areaId) throws Exception;


	/**
	 * AJAX获取终端人员负责行政区县
	 * @param TerminalId 终端人员32位PID
	 * @return
	 * @throws Exception
	 */
	public String receiveTerminalXzqx(String changePersonPid) throws Exception;

	/**
	 * AJAX根据身份证获取籍贯信息,到市
	 * @param idNum 身份证号码
	 * @return
	 * @throws Exception
	 */
	public String receiveTerminalPlace(String idNum) throws Exception;
}
