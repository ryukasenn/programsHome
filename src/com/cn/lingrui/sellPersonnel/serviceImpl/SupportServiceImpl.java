package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.db.dao.UserManageDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_USER;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_U_R;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.ExcelUtil;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.PersonManageDao;
import com.cn.lingrui.sellPersonnel.db.dao.SupportDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_RECORD;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.TOTAL;
import com.cn.lingrui.sellPersonnel.pojos.JsonDataBack;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;
import com.cn.lingrui.sellPersonnel.pojos.person.AddPersonPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.person.AreaStatus;
import com.cn.lingrui.sellPersonnel.pojos.support.AttendanceForm;
import com.cn.lingrui.sellPersonnel.pojos.support.EvaluationForm;
import com.cn.lingrui.sellPersonnel.pojos.support.TransferPojoIn;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;
import com.cn.lingrui.sellPersonnel.service.SupportSerivce;
import net.sf.json.JSONArray;
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

	@Resource(name = "personManageDao")
	private PersonManageDao personManageDao;
	
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
			
			supportDao.addRecord(person.getNBPT_SP_PERSON_PID(), this.getLoginId(), "8", this.getConnection());
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
				
				// 如果是后勤
				report = supportDao.receiveAttendance("", this.getConnection());
			}
			
			// 如果是信息专员
			else if("27".equals(person.getNBPT_SP_PERSON_JOB())) {
				
				report = supportDao.receiveAttendance(person.getNBPT_SP_PERSON_DEPT_ID(), this.getConnection());
			}
			
			mv.addObject("report", report);
			return after(mv);
		} catch (Exception e) {
			
			this.closeException();
			log.error("生成考勤页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public void createAttendanceFile(String fileName) throws Exception {
		try {
			this.before();
			
			// 获取登录人员
			NBPT_VIEW_CURRENTPERSON person = supportDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
			
			// 初始化返回报表
			AttendanceForm report = null;
			
			// 根据权限返回不同数据
			if(null == person) {
				
				// 如果是后勤
				report = supportDao.receiveAttendance("", this.getConnection());
			}
			
			// 如果是信息专员
			else if("27".equals(person.getNBPT_SP_PERSON_JOB())) {
				
				report = supportDao.receiveAttendance(person.getNBPT_SP_PERSON_DEPT_ID(), this.getConnection());
			}
			
			ExcelUtil.reportToExcel(fileName, report);
		} catch (Exception e) {
			
			this.closeException();
			log.error("生成考勤页面失败" + CommonUtil.getTrace(e));
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
				
				// 获取terminal
				NBPT_VIEW_CURRENTPERSON terminal = supportDao.receivePerson(terminalPid, this.getConnection());
				
				// 执行删除,同时删除负责区域
				supportDao.deleteTerminal(terminalPid, this.getConnection());
				personManageDao.deleteTerminalAreas(terminal.getNBPT_SP_PERSON_ID(), this.getConnection());
				
				// 添加操作记录
				supportDao.addRecord(terminalPid, this.getLoginId(), "5", this.getConnection());
				
				// 同时删除绑定关系
				supportDao.excuteUpdate("DELETE FROM NBPT_SP_PERSON_REGION WHERE NBPT_SP_PERSON_REGION_PID = '" + terminalPid + "' "
						+ "AND NBPT_SP_PERSON_REGION_TYPE = '2' ", this.getConnection());
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
			
			// 查询对应id的未审核人员详细信息
			NBPT_SP_PERSON person = supportDao.receiveUncheck(uncheckpid, this.getConnection());
			
			// 获取该人员的负责区域
			List<NBPT_COMMON_XZQXHF> responsAreas = supportDao.receiveTerminalResponsAreas(uncheckpid, this.getConnection());
			
			// 如果是离职审核,要获取离职原因
			if("1".equals(type)) {
				
				NBPT_SP_RECORD record = supportDao.getRecord(person.getNBPT_SP_PERSON_PID(), this.getConnection());
				mv.addObject("note", record);
			}
			
			mv.addObject("person", person);
			mv.addObject("controllAreas", responsAreas);
			mv.addObject("type", type);
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("获取当前未审核人员出错");
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
				supportDao.addRecord(uncheckPid, this.getLoginId(), "2", this.getConnection());
			}
			
			else if("1".equals(type)) {// 离职审核

				// 更新状态
				supportDao.changeTerminalState(uncheckPid, "3", this.getConnection());
				supportDao.addRecord(uncheckPid, this.getLoginId(), "4", this.getConnection());
			}
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("通过审核出错");
			throw new Exception();

		}
	}


	@Override
	public ModelAndView rejectUncheck(String uncheckPid) throws Exception {

		this.before();

		try {
			
			ModelAndView mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/support/receiveUnchecks");

			// 查询对应id的未审核人员详细信息
			NBPT_SP_PERSON person = supportDao.receiveUncheck(uncheckPid, this.getConnection());
			
			// 判断审核意向,修改为入职驳回状态
			if("1".equals(person.getNBPT_SP_PERSON_FLAG())) {// 如果是入职审核{

				supportDao.changeTerminalState(uncheckPid, "4", this.getConnection());
				supportDao.addRecord(uncheckPid, this.getLoginId(), "11", this.getConnection());
			}
				
			// 判断审核意向,修改为在职
			if("0".equals(person.getNBPT_SP_PERSON_FLAG())) {// 如果是离职审核

				supportDao.changeTerminalState(uncheckPid, "2", this.getConnection());
				supportDao.addRecord(uncheckPid, this.getLoginId(), "10", this.getConnection());
			}
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("驳回出错");
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
	public ModelAndView postTransfer(TransferPojoIn in) throws Exception {

		ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030401"));

		if("".equals(in.getPersonPid())) {
			mv.addObject("message", "人没选上");
			return this.after(mv);
		}

		try {
			
			this.before();
			
			switch (in.getTransferType()) {		
			case "1": // 如果是区县总升地总,修改指定personPid的人员JOB,要添加登录ID

				personManageDao.deleteTerminalAreas(in.getPersonId(), this.getConnection());
				supportDao.changePersonJob(in.getPersonPid(), in.getPersonLoginId(), "22", this.getConnection());
				
				NBPT_VIEW_CURRENTPERSON personInfo = supportDao.receivePerson(in.getPersonPid(), this.getConnection());
				
				// 创建登录账号
				NBPT_RSFZ_USER user = new NBPT_RSFZ_USER();
				user.setNBPT_RSFZ_USER_ID(in.getPersonLoginId());
				user.setNBPT_RSFZ_USER_NAME(personInfo.getNBPT_SP_PERSON_NAME());
				user.setNBPT_RSFZ_USER_PHONE("");
				user.setNBPT_RSFZ_USER_BZ("");
				user.setNBPT_RSFZ_USER_EMALL("");
				user.setNBPT_RSFZ_USER_PASSWORD("lingrui123");
				userManageDao.postAddUser(this.getConnection(), user);

				// 插入权限信息
				NBPT_RSFZ_U_R rsfz_U_R = new NBPT_RSFZ_U_R();
				String role = "600003";
				rsfz_U_R.setNBPT_RSFZ_U_R_RID(role);
				rsfz_U_R.setNBPT_RSFZ_U_R_UID(in.getPersonLoginId());
				userManageDao.postAddU_R(this.getConnection(), rsfz_U_R);
				
				supportDao.addRecord(personInfo.getNBPT_SP_PERSON_PID(), this.getLoginId(), "12", this.getConnection());
				
				break;
	
			case "0": // 如果是区县总调地区
				
				// 修改地区,同时删除负责区域
				personManageDao.deleteTerminalAreas(in.getPersonId(), this.getConnection());
				supportDao.changeTerminalArea(in.getPersonPid(), in.getRegionUid(), this.getConnection());
				
				break;
				
			case "2": // 如果是地总升大区总
				
				supportDao.changePersonJob(in.getPersonId(), this.getLoginId(),"21", this.getConnection());
				break;
			default:

				mv.addObject("message", "出现错误");
				break;
			}

			mv.addObject("message", "调岗成功");
			return this.after(mv);
		} catch (Exception e) {

			this.closeException();
			log.error("人员调岗出错");
			throw new Exception();

		}
	}

	@Override
	public ModelAndView changeTerminal(AddPersonPojoIn in) throws Exception {
		
		try {
			
			
			this.before();
			NBPT_SP_PERSON currentTerminal = supportDao.receiveSpPerson(in.getNBPT_SP_PERSON_PID(), this.getConnection());

			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030401"));
			
			currentTerminal.setNBPT_SP_PERSON_JOB(in.getNBPT_SP_PERSON_JOB());
			
			// 更新人员信息
			personManageDao.updateTerminal(currentTerminal, this.getConnection());
			
			// 添加添加终端申请记录
			personManageDao.addRecord(currentTerminal.getNBPT_SP_PERSON_PID(), this.getLoginId(), "14", this.getConnection());
		
			return this.after(mv);
		} catch (Exception e) {
			this.closeException();
			throw new Exception();
		}

	}

	@Override
	public String receivePerson(String type, String id, String typeValue) {

		try {
			
			this.before();
			JsonDataBack back = new JsonDataBack();
			switch(type) {
			case "root":
				// 除信息专员外,获取所有人信息
				List<TOTAL> totalInfos = supportDao.queryForClaszs(
						"SELECT COUNT(*) AS total,B.NBPT_COMMON_DICTIONARY_KEY_NAME AS regionName " + 
						"FROM NBPT_VIEW_CURRENTPERSON A LEFT JOIN NBPT_COMMON_DICTIONARY B " + 
						"ON A.NBPT_SP_PERSON_TYPE = B.NBPT_COMMON_DICTIONARY_KEY_VALUE " + 
						"AND B.NBPT_COMMON_DICTIONARY_KEY = 'SAILTYPE' " + 
						"WHERE A.NBPT_SP_PERSON_JOB <> '27' AND A.NBPT_SP_PERSON_FLAG = '2' " + 
						"GROUP BY(B.NBPT_COMMON_DICTIONARY_KEY_NAME)", this.getConnection(), TOTAL.class);
				
				back.setData(JSONArray.fromObject(totalInfos).toString());
				return this.after(back.toJsonString());
			case "type":
				// 除信息专员外,获取所有人信息
				List<NBPT_VIEW_CURRENTPERSON> allPersonsByType = supportDao.receivePersonByDepart(null, null, null, "2", this.getConnection());

				// 查询所有部门
				List<NBPT_VIEW_REGION> regions = supportDao.receiveRegion(null, "1", null, this.getConnection());
				
				// 分类可用部门
				regions = CommonUtil.getListInMapByKey(CommonUtil.classify(regions, "NBPT_SP_REGION_FLAG", NBPT_VIEW_REGION.class), "1");
				
				// OTC部门和人员统计结果
				List<StatisticsTable> OTCInfos = SupportServiceUtils.dealCurrentPerson_otc(
						allPersonsByType,
						CommonUtil.getListInMapByKey(CommonUtil.classify(regions, "NBPT_SP_REGION_TYPE", NBPT_VIEW_REGION.class), id));
				back.setData(JSONArray.fromObject(OTCInfos).toString());
				return this.after(back.toJsonString());
			case "region":

				// 该大区下所有人员
				List<NBPT_VIEW_CURRENTPERSON> allPersonsByRegionUid = supportDao.receivePersonByDepart(id, null,  null, typeValue,this.getConnection());

				// 查询所有省份
				List<NBPT_VIEW_XZQX> provinces = supportDao.receiveXzqxs(null, "1", "11", this.getConnection());
				
				// 获取大区下省份
				provinces = CommonUtil.getListInMapByKey(CommonUtil.classify(provinces, "NBPT_SP_REGION_UID", NBPT_VIEW_XZQX.class), id);
				
				// 获取大区下所有地区,获取配额合计
				List<NBPT_VIEW_REGION> areas = supportDao.receiveRegion(id, null, this.getConnection());
				
				areas = CommonUtil.getListInMapByKey(CommonUtil.classify(areas, "NBPT_SP_REGION_FLAG", NBPT_VIEW_REGION.class), "1");
				
				// 按省份分类人员信息
				Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyiedPersons_byProvince 
						= CommonServiceUtils.dealPersonsByKey(allPersonsByRegionUid, "NBPT_SP_PERSON_PROVINCE_ID");
				
				// 人员信息处理
				List<StatisticsTable> privinceInfos = PersonManageServiceUtils.countClassifyList_byProvince(provinces, classifyiedPersons_byProvince, areas);

				back.setData(JSONArray.fromObject(privinceInfos).toString());
				return this.after(back.toJsonString());
				
			case "province" :

				// 获取该省份下所有人员
				List<NBPT_VIEW_CURRENTPERSON> allPersonsByProvince = supportDao.receivePersonByDepart(null, null, id, typeValue, this.getConnection());

				// 查询所有部门,
				List<NBPT_VIEW_REGION> province_areas = supportDao.receiveRegion(null, null, this.getConnection());
				
				// 分类可用部门
				province_areas = CommonUtil.getListInMapByKey(CommonUtil.classify(province_areas, "NBPT_SP_REGION_FLAG", NBPT_VIEW_REGION.class), "1");
				
				// 查询该省份下所有地区
				province_areas = CommonUtil.getListInMapByKey(CommonUtil.classify(province_areas, "NBPT_SP_REGION_PROVINCE_ID", NBPT_VIEW_REGION.class), id);
				province_areas = CommonUtil.getListInMapByKey(CommonUtil.classify(province_areas, "NBPT_SP_REGION_LEVEL", NBPT_VIEW_REGION.class), "2");
				
				
				// 按地区分类人员信息
				Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyiedPersons_byArea = CommonServiceUtils.dealPersonsByKey(allPersonsByProvince, "NBPT_SP_PERSON_AREA_UID");
				
				// 信息处理
				List<StatisticsTable> areaInfos = PersonManageServiceUtils.countClassifyList_byArea(province_areas, classifyiedPersons_byArea);
				
				back.setData(JSONArray.fromObject(areaInfos).toString());
				
				return this.after(back.toJsonString());
				default: 
					back.setStateError("后台错误,请联系管理员");
					return this.after(back.toJsonString());
			}
			
				
			
		} catch (Exception e) {
			JsonDataBack back = new JsonDataBack();
			log.error("获取人员信息出错" + CommonUtil.getTrace(e));
			back.setStateError("后台错误,请联系管理员");
			return this.after(back.toJsonString());
		}
	}

	@Override
	public String receivePersonTable(String type, String id, String typeValue) {
		try {
			
			this.before();
			JsonDataBack back = new JsonDataBack();
			switch(type) {
			case "type": // 点击分类,获取分类下所有大区总信息
				List<NBPT_VIEW_CURRENTPERSON> regionResper = supportDao.receivePerson(new String[] {"21", "26"}, null, typeValue, this.getConnection());
				back.setData(JSONArray.fromObject(regionResper).toString());
				return this.after(back.toJsonString());
			case "region":
				List<NBPT_VIEW_CURRENTPERSON> areaResper = supportDao.receivePerson("22", null, typeValue, this.getConnection());
				if("regionUnset".equals(id)) {
					areaResper = CommonUtil.getListInMapByKey(CommonUtil.classify(areaResper, "NBPT_SP_PERSON_REGION_UID", 
							NBPT_VIEW_CURRENTPERSON.class), "");
				} else {
					areaResper = CommonUtil.getListInMapByKey(CommonUtil.classify(areaResper, "NBPT_SP_PERSON_REGION_UID", 
							NBPT_VIEW_CURRENTPERSON.class), id);
				}
				back.setData(JSONArray.fromObject(areaResper).toString());
				return this.after(back.toJsonString());
			case "area" :
				NBPT_VIEW_CURRENTPERSON loginPerson = supportDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
				
				if(null == loginPerson) {
					List<NBPT_VIEW_CURRENTPERSON> areaPerson = supportDao.receivePersonByDepart(null, id, null, typeValue, this.getConnection());
					areaPerson = CommonUtil.getListInMapByKey(CommonUtil.classify(areaPerson, "NBPT_SP_PERSON_LEVEL", NBPT_VIEW_CURRENTPERSON.class), "3");
					back.setData(JSONArray.fromObject(areaPerson).toString());
					return this.after(back.toJsonString());
				}
				else {
					String areaUid = "";
					typeValue = loginPerson.getNBPT_SP_PERSON_TYPE();

					AreaStatus currentStatus = new AreaStatus();
					
					NBPT_VIEW_REGION area = supportDao.receiveRegionByResper(loginPerson.getNBPT_SP_PERSON_PID(), "2", this.getConnection());
					areaUid = area.getNBPT_SP_REGION_UID();
					currentStatus.setNeed(area.getNBPT_SP_REGION_NEED());
					
					List<NBPT_VIEW_CURRENTPERSON> areaPerson = supportDao.receivePersonByDepart(null, areaUid, null, typeValue, this.getConnection());
					areaPerson = CommonUtil.getListInMapByKey(CommonUtil.classify(areaPerson, "NBPT_SP_PERSON_LEVEL", NBPT_VIEW_CURRENTPERSON.class), "3");
					currentStatus.setNow(CommonUtil.getListInMapByKey(CommonUtil.classify(areaPerson, "NBPT_SP_PERSON_FLAG", NBPT_VIEW_CURRENTPERSON.class), "2").size());

					currentStatus.setPersons(areaPerson);
					back.setData(currentStatus.toJsonString());
					return this.after(back.toJsonString());
				} 
				default: 
					back.setStateError("后台错误,请联系管理员");
					return this.after(back.toJsonString());
			}
		} catch (Exception e) {
			JsonDataBack back = new JsonDataBack();
			log.error("获取人员信息出错" + CommonUtil.getTrace(e));
			back.setStateError("后台错误,请联系管理员");
			return this.after(back.toJsonString());
		}
	}
	
	@Test
	public void test() {
		System.out.println(CommonUtil.getUUID_32());
	}

}


























