package com.cn.lingrui.sellPersonnel.serviceImpl;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.db.dao.UserManageDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_USER;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_U_R;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.SupportDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.AddPersonPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.support.AttendanceForm;
import com.cn.lingrui.sellPersonnel.pojos.support.EvaluationForm;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;
import com.cn.lingrui.sellPersonnel.service.SupportSerivce;

import net.sf.json.JSONObject;


/**
 * 后勤权限逻辑
 * @author lhc
 *
 */
@Service("supportSerivce")
public class SupportServiceImpl extends SellPBaseService implements SupportSerivce{
	
	private static Logger log = LogManager.getLogger();

	@Resource(name = "supportDao")
	private SupportDao supportDao;

	@Resource(name="userManageDao")
	private UserManageDao userManageDao;
	
	@Override
	protected String getFunNum() {
		// TODO 自动生成的方法存根
		return null;
	}

	/**
	 * 获取后勤添加人员页面
	 */
	@Override
	public ModelAndView getSupportAdd() throws Exception {

		try {
			
			this.before();
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030403"));
	
			// 1.生成保单类型下拉框
			mv.addObject("policytypeSelects", supportDao.receiveDictionarys("POLICYTYPE", this.getConnection()));
	
			return this.after(mv);
		} catch (Exception e) {
			
			this.closeException();
			log.error("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public ModelAndView postSupportAdd(AddPersonPojoIn in) throws Exception {
		
		try {
			
			this.before();
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030401"));
	
			// 初始化人员信息
			NBPT_SP_PERSON person = new NBPT_SP_PERSON();
			
			// 处理页面传入参数
			SupportServiceUtils.checkDataIn(person,in);
			
			// 插入新的人员信息
			supportDao.supportAddPerson(person, this.getConnection());

			// 创建登录账号
			NBPT_RSFZ_USER user = new NBPT_RSFZ_USER();
			user.setNBPT_RSFZ_USER_ID(in.getNBPT_SP_PERSON_LOGINID());
			user.setNBPT_RSFZ_USER_NAME(in.getNBPT_SP_PERSON_NAME());
			user.setNBPT_RSFZ_USER_PHONE(in.getNBPT_SP_PERSON_MOB1());
			user.setNBPT_RSFZ_USER_BZ(in.getNBPT_SP_PERSON_NOTE());
			user.setNBPT_RSFZ_USER_EMALL(in.getNBPT_SP_PERSON_MAIL());
			user.setNBPT_RSFZ_USER_PASSWORD("".equals(in.getNBPT_SP_PERSON_OAPSWD()) ? "lingrui123" : in.getNBPT_SP_PERSON_OAPSWD());
			userManageDao.postAddUser(this.getConnection(), user);

			// 插入权限信息
			NBPT_RSFZ_U_R rsfz_U_R = new NBPT_RSFZ_U_R();
			String role = "21".equals(in.getNBPT_SP_PERSON_JOB()) ? "600006" : "22".equals(in.getNBPT_SP_PERSON_JOB()) ? "600003" : "600008";
			rsfz_U_R.setNBPT_RSFZ_U_R_RID(role);
			rsfz_U_R.setNBPT_RSFZ_U_R_UID(in.getNBPT_SP_PERSON_LOGINID());
			userManageDao.postAddU_R(this.getConnection(), rsfz_U_R);
			
			mv.addObject("message", "添加成功");
			
			return this.after(mv);
		} catch (Exception e) {
			
			this.closeException();
			log.error("提交添加地总大区总业务失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}
	
	@Override
	public ModelAndView createEvaluationForm(String endTime) throws Exception {

		try {
			this.before();
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030702"));
			
			EvaluationForm evaluationForm = supportDao.receiveEvalutionForm(CommonUtil.formateTiemToBasic(endTime), this.getConnection());
			
			mv.addObject("evaluationForm", evaluationForm);
			mv.addObject("formTime", endTime);
			return after(mv);
		} catch (Exception e) {
			
			this.closeException();
			log.error("生成人员配置考核页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}



	@Override
	public String getRoleCheck() throws Exception {

		try {
			this.before();
			NBPT_VIEW_CURRENTPERSON person = supportDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
			
			JSONObject json = null;
			if(null == person) {
				
				json = JSONObject.fromObject("{flag : true}");
			}else {

				json = JSONObject.fromObject("{flag : false}");
			}
			
			return after(json.toString());
		} catch (Exception e) {
			
			this.closeException();
			log.error("角色权限验证失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}



	@Override
	public ModelAndView createAttendance() throws Exception {

		try {
			this.before();
			
			// 获取登录人员
			NBPT_VIEW_CURRENTPERSON person = supportDao.receiveLoginPerson(this.getLoginId(), this.getConnection());

			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030703"));
			
			// 初始化返回报表
			AttendanceForm report = null;
			
			// 根据权限返回不同数据
			if(null == person) {
				
				report = supportDao.receiveAttendance("", this.getConnection());
			}
			
			else if("27".equals(person.getNBPT_SP_PERSON_JOB())) {
				
				report = supportDao.receiveAttendance(person.getNBPT_SP_PERSON_DEPT_ID(), this.getConnection());
			}
			
			mv.addObject("report", report);
			return after(mv);
		} catch (Exception e) {
			
			this.closeException();
			log.error("生成人员配置考核页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}



	@Override
	public ModelAndView deleteTerminal(String terminalPid, String areaUid, String regionUid) throws Exception {
		
		try {
			this.before();
			
			// 获取登录人员
			NBPT_VIEW_CURRENTPERSON person = supportDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
			ModelAndView mv = null;
			
			if(null == person && "600005".equals(this.getRole())) {

				if(CommonUtil.isEmpty(areaUid)) {
					
					mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/support/receiveUnchecks");
				} else {

					mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/areaPersons?areaUid="+ areaUid +"&regionUid=" + regionUid);
				}
				
				// 执行删除
				supportDao.deleteTerminal(terminalPid, this.getConnection());
			}
			
			return after(mv);
			
		} catch (Exception e) {
			
			this.closeException();
			log.error("删除终端失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}
	
	@Override
	public ModelAndView receiveUnchecks() throws Exception {


		this.before();

		// 初始化返回
		ModelAndView mv = null;

		try {
			
			mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030302"));
						
			// 查询该登录人员下未审核名单
			List<NBPT_VIEW_CURRENTPERSON> uncheckList = supportDao.receiveUnchecks(this.getConnection());
			
			Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyedPersons = CommonUtil.classify(uncheckList, "NBPT_SP_PERSON_FLAG", NBPT_VIEW_CURRENTPERSON.class);
			
			mv.addObject("newUncheckList", CommonUtil.getListInMapByKey(classifyedPersons, "1"));
			mv.addObject("deleteUncheckList", CommonUtil.getListInMapByKey(classifyedPersons, "0"));
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员出错");
			throw new Exception();

		}
	}

	@Override
	public ModelAndView receiveUncheck(String uncheckpid, String type) throws Exception {

		this.before();

		try {
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030303"));
			
			// 查询该登录人员下未审核名单
			NBPT_SP_PERSON person = supportDao.receiveUncheck(uncheckpid, this.getConnection());
			
			// 获取该人员的负责区域
			List<NBPT_COMMON_XZQXHF> responsAreas = supportDao.receiveTerminalResponsAreas(uncheckpid, this.getConnection());
			
			mv.addObject("person", person);
			mv.addObject("controllAreas", responsAreas);
			mv.addObject("type", type);
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员出错");
			throw new Exception();

		}
	}



	@Override
	public ModelAndView agreeUncheck(String uncheckPid, String type) throws Exception {

		this.before();

		try {
			
			ModelAndView mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/support/receiveUnchecks");
			
			if("0".equals(type)) {// 入职审核

				// 更新状态
				supportDao.changeTerminalState(uncheckPid, "2", this.getConnection());
			}
			
			else if("1".equals(type)) {// 离职审核

				// 更新状态
				supportDao.changeTerminalState(uncheckPid, "3", this.getConnection());
			}
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("通过审核出错");
			throw new Exception();

		}
	}

	@Override
	public ModelAndView getTransfer() throws Exception {

		try {
			
			this.before();
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030408"));
			
			List<NBPT_VIEW_REGION> regions = supportDao.receiveRegion(null, null, this.getConnection());
			
			mv.addObject("regions", CommonUtil.getListInMapByKey(CommonUtil.classify(regions, "NBPT_SP_REGION_LEVEL", NBPT_VIEW_REGION.class), "1"));
			
			return this.after(mv);

		} catch (Exception e) {

			this.closeException();
			log.error("查询当前所有人员出错");
			throw new Exception();

		}
	}

	@Override
	public ModelAndView postTransfer() {
		// TODO 自动生成的方法存根
		return null;
	}


}


























