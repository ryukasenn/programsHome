package com.cn.lingrui.sellPersonnel.service;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.AddPersonPojoIn;

public interface SupportSerivce {

	/**
	 * 获取添加管理人员页面
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getSupportAdd() throws Exception;

	/**
	 * 生成考核列表
	 * @param endTime 截止时间
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView createEvaluationForm(String endTime) throws Exception;

	/**
	 * 生成考核列表的权限验证
	 * @return
	 * @throws Exception 
	 */
	public String getRoleCheck() throws Exception;

	/**
	 * 生成考勤列表
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView createAttendance() throws Exception;

	/**
	 * 删除指定终端人员
	 * @param terminalPid 指定的人员
	 * @param regionUid 返回需要
	 * @param areaUid 返回需要
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteTerminal(String terminalPid, String areaUid, String regionUid) throws Exception;

	/**
	 * 获取未审核列表
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView receiveUnchecks() throws Exception;

	/**
	 * 获取指定未审核人员
	 * @param uncheckpid
	 * @param type 添加页面标志
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView receiveUncheck(String uncheckpid, String type) throws Exception;

	/**
	 * 通过审核
	 * @param uncheckPid 未审核人员PID
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView agreeUncheck(String uncheckPid, String type) throws Exception;

	/**
	 * 提交添加大区总地总
	 * @param in
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView postSupportAdd(AddPersonPojoIn in) throws Exception;

	/**
	 * 获取调岗页面
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getTransfer() throws Exception;

	/**
	 * 提交调岗请求
	 * @return
	 */
	public ModelAndView postTransfer();
}
