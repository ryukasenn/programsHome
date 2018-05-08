package com.cn.lingrui.sellPersonnel.service;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.AddPersonPojoIn;

public interface AreaHeadService {

	/**
	 * 获取添加终端人员页面
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getAddTerminal() throws Exception;

	/**
	 * 添加终端人员提交
	 * @param in
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView postAddTerminal(AddPersonPojoIn in) throws Exception;

	/**
	 * 获取修改添加终端人员页面
	 * @param changePersonPid
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getChangePerson(String changePersonPid) throws Exception;

	/**
	 * 申请离职
	 * @param dimissTerminalPid 指定终端PID
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView dimissTerminal(String dimissTerminalPid, String dimissTime) throws Exception;

}
