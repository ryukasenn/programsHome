package com.cn.lingrui.sellPersonnel.service;

import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.sellPersonnel.pojos.person.AddPersonIn;
import com.cn.lingrui.sellPersonnel.pojos.person.ChangePersonIn;
import com.cn.lingrui.sellPersonnel.pojos.person.PostAddPersonIn;
import com.cn.lingrui.sellPersonnel.pojos.person.PostTransferPersonIn;

public interface PersonManageService {

	/**
	 * 查看大区下省区信息
	 * @param regionUid 大区32位UID
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getProvincePersons(String regionUid) throws Exception;

	/**
	 * 查看地区下人员详细信息
	 * @param areaUid
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView getAreaPersons(String areaUid, String regionUid) throws Exception;

	/**
	 * 根据人员PID,获取人员详细信息
	 * @param areaUid 所在地区
	 * @param regionUid 所在大区
	 * @param personPid 人员PID
	 * @return
	 * @throws Exception
	 */
	public ModelAndView receivePerson(String areaUid, String regionUid, String personPid) throws Exception;

	/**
	 * 获取添加人员
	 * @param in
	 * @return
	 */
	public String getAddPerson(AddPersonIn in);

	/**
	 * 执行添加人员
	 * @param in
	 * @return
	 */
	public String postAddPerson(PostAddPersonIn in);
	
	/**
	 * 获取修改人员
	 * @param in
	 * @return
	 */
	public String getChangePerson(ChangePersonIn in);

	/**
	 * 执行修改人员
	 * @param in
	 * @return
	 */
	public String postChangePerson(PostAddPersonIn in);

	/**
	 * 获取调岗人员
	 * @param personPid
	 * @return
	 */
	public String getTransferPerson(String personPid);

	/**
	 * 执行人员调岗
	 * @param in
	 * @return
	 */
	public String postTransferPerson(PostTransferPersonIn in);

	/**
	 * 添加新用户
	 * @param newUserLoginId
	 * @param newUserPassword
	 * @param newUserPid
	 * @return
	 */
	public String postAddNewUser(String newUserLoginId, String newUserPassword, String newUserPid);

	/**
	 * 获取申请离职
	 * @param dimissTerminalPid
	 * @return
	 */
	public String getDimissTerminal(String dimissTerminalPid);

	/**
	 * 提交离职申请
	 * @param dimissTerminalPid
	 * @param dimissTime
	 * @param note
	 * @return
	 */
	public String postDimissTerminal(String dimissTerminalPid, String dimissTime, String note);

	/**
	 * 执行人员删除
	 * @param personPid
	 * @return
	 */
	public String postDeletePerson(String personPid);
}
