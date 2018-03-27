package com.cn.lingrui.sellPersonnel.service;

import java.sql.SQLException;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.AddPersonPojoIn;

public interface PersonManageService {

	public ModelAndView receiveCurrentTerminals_support() throws Exception ;

	public ModelAndView getAddTerminal() throws Exception;

	public ModelAndView postAddTerminal(AddPersonPojoIn in) throws Exception;

	public String receiveSelect(String parentId) throws Exception;

	public ModelAndView getSupportAdd() throws Exception;

	/**
	 * AJAX通过地区ID,获取地区下辖行政区县列表
	 * @param areaId 地区ID
	 * @return
	 * @throws Exception
	 */
	public String receiveAreasSelect(String areaId) throws Exception;

	/**
	 * 获取修改终端人员页面
	 * @param changePersonPid 需要修改的人员的32位PID
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getChangePerson(String changePersonPid) throws Exception;

	/**
	 * AJAX获取终端人员负责行政区县
	 * @param TerminalId 终端人员32位PID
	 * @return
	 * @throws Exception
	 */
	public String receiveTerminalXzqx(String TerminalId) throws Exception;

	/**
	 * AJAX根据身份证获取籍贯信息,到市
	 * @param idNum 身份证号码
	 * @return
	 * @throws Exception
	 */
	public String receiveTerminalPlace(String idNum) throws Exception;

	/**
	 * 查看大区下省区信息
	 * @param regionUid 大区32位UID
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getProvincePersons(String regionUid) throws Exception;

}
