package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_DICTIONARY;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.CheckManageDao;
import com.cn.lingrui.sellPersonnel.db.dao.PersonManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson_statistics;
import com.cn.lingrui.sellPersonnel.pojos.AddPersonPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.SellPersonnelPojoOut;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;

import net.sf.json.JSONArray;

import com.cn.lingrui.sellPersonnel.service.PersonManageService;

@Service("personManageService")
public class PersonManageServiceImpl extends SellPBaseService implements PersonManageService {

	private static Logger log = LogManager.getLogger();

	@Resource(name = "personManageDao")
	private PersonManageDao personManageDao;

	@Resource(name="checkManageDao")
	private CheckManageDao checkManageDao;
	
	@Override
	protected String getFunNum() {
		return null;
	}
	
	/**
	 * 下级人员查询
	 */
	@Override
	public ModelAndView receiveCurrentTerminals_support() throws Exception {

		this.before();

		// 初始化返回
		ModelAndView mv = null;

		// 初始化返回内容
		SellPersonnelPojoOut out = new SellPersonnelPojoOut();

		try {

			// 获取登录名
			String userId = this.getRequest().getAttribute("userID").toString();

			// 获取当前登录人员信息
			CurrentPerson person = personManageDao.receiveCurrentPerson(userId, this.getConnection());

			// 处理当前登陆人员信息
			PersonManageServiceUtils.dealCurrentPerson(person, out);

			mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030402"));

			if(null == person) {

				// 查询所有下级人员信息
				List<CurrentPerson> personInfos = personManageDao.receiveCurrentPersonInfos(out.getPerson(),this.getConnection());

				// 查询所有的大区信息
				List<NBPT_SP_REGION> regionList = personManageDao.getRegions("1", this.getConnection());
				
				// 后勤人员查询合计信息处理
				List<CurrentPerson_statistics> infos = PersonManageServiceUtils.dealCurrentPerson_total(personInfos,regionList);
				
				// 后勤人员查询OTC统计信息处理
				List<CurrentPerson_statistics> otcInfos = PersonManageServiceUtils.dealCurrentPerson_region(personInfos);
				
				// 合计信息
				mv.addObject("totalInfos", infos);
				
				// OTC统计信息
				mv.addObject("OTCInfos", otcInfos);
			} else {

				if("null".equals(person.getNBPT_SP_REGION_NEED()) || "".equals(person.getNBPT_SP_REGION_NEED())) {
					
					mv.addObject("message", "您没有被分配管理地区,请联系后勤人员或管理员");
					
					return this.after(mv);
				}else {
					
					// 如果是地总
					if("22".equals(person.getNBPT_SP_PERSON_JOB()) || "26".equals(person.getNBPT_SP_PERSON_JOB())) {
						
						// 查询所有下级人员信息
						List<CurrentPerson> personInfos = personManageDao.receiveCurrentPersonInfos(out.getPerson(),this.getConnection());
						
						// 1.查询该地总配额
						mv.addObject("thisNeed", person.getNBPT_SP_REGION_NEED());
						
						// 2.该地总手下人数
						mv.addObject("thisInfact", personInfos.size());
						
						// 返回数据
						mv.addObject("personInfos", personInfos);
					}
				}
			}
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员出错");
			throw new Exception();

		}
	}

	/**
	 * 获取地总添加人员页面
	 * @throws Exception 
	 */
	@Override
	public ModelAndView getAddTerminal() throws Exception {

		this.before();
		
		// 初始化返回
		ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030403"));

		// 1.生成管理地区单选框
		List<NBPT_COMMON_XZQXHF> controllAreas = null;
		
		// 2.生成保单类型下拉框
		List<NBPT_COMMON_DICTIONARY> dictionarys = null;
		try {

			// 获取当前登录人员信息
			CurrentPerson loginPerson = this.getLoginPerson();
			
			if("null".equals(loginPerson.getNBPT_SP_REGION_NEED()) || "".equals(loginPerson.getNBPT_SP_REGION_NEED())) {
				
				String message = "您没有被分配管理地区,请联系后勤人员或管理员";
				mv.addObject("message", message);
			}else {
				
				// 获取地总管理地区
				controllAreas = personManageDao.getAreaSelects(loginPerson.getNBPT_SP_PERSON_LOGINID(), this.getConnection());
				controllAreas.add(0, new NBPT_COMMON_XZQXHF());
				
				dictionarys = personManageDao.receiveDictionarys("POLICYTYPE", this.getConnection());
	
				mv.addObject("controllAreas", controllAreas);
				mv.addObject("policytypeSelects", dictionarys);
				
			}

			return this.after(mv);
		} catch (SQLException e) {

			this.closeException();
			log.error("获取添加终端人员页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	/**
	 * 提交添加终端人员页面
	 * @throws Exception 
	 */
	@Override
	public ModelAndView postAddTerminal(AddPersonPojoIn in) throws Exception {

		this.before();
		
		// 初始化保存数据
		NBPT_SP_PERSON person = new NBPT_SP_PERSON();

		ModelAndView mv = null;
		
		try {

			SellPersonnelPojoOut out = new SellPersonnelPojoOut();

			// 当前登录人员
			CurrentPerson loginPerson = this.getLoginPerson();
			out.setPerson(loginPerson);

			
			if(null == in.getNBPT_SP_PERSON_PID() || "".equals(in.getNBPT_SP_PERSON_PID()) ) {

				// 最大人员ID
				String maxId = personManageDao.receiveMaxId("RECEIVEMAXID", this.getConnection(), "NBPT_SP_PERSON");
				out.setMaxId(maxId);
				
				// 管理区县
				List<NBPT_SP_PERSON_XZQX> reposAreas = new ArrayList<NBPT_SP_PERSON_XZQX>();
				
				// 生成数据
				PersonManageServiceUtils.checkInData(in, person, reposAreas, out);
				
				// 存储新的人员信息
				personManageDao.addTerminal(person, this.getConnection());
				
				personManageDao.addReposeAreas(reposAreas, this.getConnection());
			} 
			
			// 如果PID不为空
			else {
				
				// 管理区县
				List<NBPT_SP_PERSON_XZQX> reposAreas = new ArrayList<NBPT_SP_PERSON_XZQX>();
				
				// 新数据导入
				PersonManageServiceUtils.checkInData(in, person, reposAreas, out);
				
				// 存储新的人员信息
				personManageDao.updateTerminal(person, this.getConnection());
				personManageDao.addReposeAreas(reposAreas, this.getConnection());
			}
			

			mv = HttpUtil.getModelAndView("redirect:/sellPersonnel/persons");
			return this.after(mv);
			
		} catch (SQLException e) {

			this.closeException();
			log.error("添加终端人员错" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public String receiveSelect(String parentId) throws Exception{

		try {
			this.before();
			
			// 获取省级划分
			List<NBPT_COMMON_XZQXHF> xzqxhfs = personManageDao.getXzqxhfs(parentId, this.getConnection());

			xzqxhfs.add(0, new NBPT_COMMON_XZQXHF());
			
			return this.after(JSONArray.fromObject(xzqxhfs).toString());
		} catch (SQLException e) {
			
			this.closeException();
			log.error("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	
	/**
	 * 获取后勤添加人员页面
	 */
	@Override
	public ModelAndView getSupportAdd() throws Exception {

		try {
			this.before();
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030403"));
			
			// 1.生成大区下拉框
			List<NBPT_SP_REGION> regions = new ArrayList<NBPT_SP_REGION>();;
						
			regions = personManageDao.getRegions("1", this.getConnection());
			regions.add(0, new NBPT_SP_REGION());
						
			mv.addObject("regions", regions);
			
			// 2.生成保单类型下拉框
			mv.addObject("policytypeSelects", personManageDao.receiveDictionarys("POLICYTYPE", this.getConnection()));
			
			return after(mv);
		} catch (SQLException e) {
			
			this.closeException();
			log.error("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	/**
	 * 获取管理区县ajax
	 * @throws Exception 
	 */
	@Override
	public String receiveAreasSelect(String areaId) throws Exception {

		try {
			
			this.before();
		
			// 管理区县
			List<NBPT_COMMON_XZQXHF> xzqxhfs = personManageDao.getAreasSelect(areaId, this.getConnection());
			xzqxhfs.add(0, new NBPT_COMMON_XZQXHF());
			
			return this.after(JSONArray.fromObject(xzqxhfs).toString());
		}catch (SQLException e) {

			this.closeException();
			log.error("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	/**
	 * 获取修改终端页面
	 */
	@Override
	public ModelAndView getChangePerson(String changePersonPid) throws Exception {

		try {

			this.before();
			
			// 1.生成管理地区单选框
			List<NBPT_COMMON_XZQXHF> controllAreas = null;
			// 2.生成保单类型下拉框
			List<NBPT_COMMON_DICTIONARY> dictionarys = null;
			
			// 初始化返回数据
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030403"));
			
			// 获取地总管理地区
			controllAreas = personManageDao.getAreaSelects(this.getLoginId(), this.getConnection());
			controllAreas.add(0, new NBPT_COMMON_XZQXHF());

			dictionarys = personManageDao.receiveDictionarys("POLICYTYPE", this.getConnection());
			
			// 1.查询人员信息
			CurrentPerson person = personManageDao.receiveCurrentTerminal(changePersonPid, this.getConnection());
			
			// 2.处理数据
			PersonManageServiceUtils.getChangePerson_dealCurrentPerson(person);
			mv.addObject("controllAreas", controllAreas);
			mv.addObject("policytypeSelects", dictionarys);
			mv.addObject("person", person);

			return after(mv);
		} catch (SQLException e) {
			
			this.closeException();
			log.error("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}

	}

	/**
	 * 获取终端负责区县ajax
	 */
	@Override
	public String receiveTerminalXzqx(String changePersonPid) throws Exception {

		try {
			
			this.before();
			return this.after(JSONArray.fromObject(personManageDao.getTerminalResponsXzqx(changePersonPid, this.getConnection())).toString());
		}catch (SQLException e) {

			this.closeException();
			log.error("获取终端负责区县失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public String receiveTerminalPlace(String idNum) throws Exception {

		try {
			this.before();
			return this.after(JSONArray.fromObject(personManageDao.checkPlace(idNum.substring(0, 6), this.getConnection())).toString());
		}catch (SQLException e) {

			this.closeException();
			log.error("获取终端负责区县失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public ModelAndView getProvincePersons(String regionUid) throws Exception {


		try {
			
			this.before();
			
			// 初始化大区下所有人员信息
			List<CurrentPerson> personInfos = new ArrayList<>();
			ModelAndView mv = null;
			
			// 1.获取登录信息
			CurrentPerson loginPerson = this.getLoginPerson();

			// 如果为空,则是后勤登录
			if(null == loginPerson) {

				// 2.1查询大区下所有人员信息
				personInfos = personManageDao.receiveCurrentProvincePersonInfos(regionUid, this.getConnection());

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030404"));
				// 2.2添加页面信息
				mv.addObject("regionUid", regionUid);

			}
			
			// 如果是大区总
			else if("21".equals(loginPerson.getNBPT_SP_PERSON_JOB()) || "26".equals(loginPerson.getNBPT_SP_PERSON_JOB())) {
				
				
				// 2.1查询大区总下所有人员信息
				personInfos = personManageDao.receiveCurrentProvincePersonInfos(loginPerson.getREGION_UID(), this.getConnection());
				
				// 2.2添加页面信息
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030502"));
				mv.addObject("regionUid", loginPerson.getREGION_UID());
			} 

			
			// 后勤人员查询合计信息处理
			List<CurrentPerson_statistics> infos = PersonManageServiceUtils.provincePersons_check(personInfos);
			
			mv.addObject("infos", infos);
			
			return this.after(mv);
		}catch (SQLException e) {

			this.closeException();
			log.error("获取终端负责区县失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}
	
	@Override
	public ModelAndView getAreaPersons(String areaUid, String regionUid) throws Exception {

		try {
			
			this.before();

			// 初始化返回信息
			ModelAndView mv = null;
			
			// 所有人员信息
			List<CurrentPerson> personInfos = personManageDao.receiveCurrentPersonInfos(areaUid,this.getConnection());

			CurrentPerson areaResper = new CurrentPerson();
			String resper = "";
			for(CurrentPerson person : personInfos) {
				
				
				if("22".equals(person.getNBPT_SP_PERSON_JOB())) {
					
					resper = person.getNBPT_SP_PERSON_NAME() + "(地总)";
				} else if("26".equals(person.getNBPT_SP_PERSON_JOB())) {

					resper = person.getNBPT_SP_PERSON_NAME() + "(大区总兼地总)";
				}

				if("".equals(resper)) {
					
				} else {

					areaResper = person;
					personInfos.remove(person);
					break;
				}
			}
			
			// 1.获取登录信息
			CurrentPerson loginPerson = this.getLoginPerson();

			// 如果为空,则是后勤登录
			if(null == loginPerson) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030405"));
				
			}
			
			// 如果是大区总
			else if("21".equals(loginPerson.getNBPT_SP_PERSON_JOB()) || "26".equals(loginPerson.getNBPT_SP_PERSON_JOB())) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030503"));
				
			}
			
			// 1.查询该地总配额
			mv.addObject("thisNeed", areaResper.getNBPT_SP_REGION_ONAME());

			// 2.该地总手下人数
			mv.addObject("thisInfact", personInfos.size());
			
			// 3.该地区负责人
			mv.addObject("thisResper", resper);
			
			// 4.该地区名称
			mv.addObject("thisArea", areaResper.getNBPT_SP_REGION_NAME());
			
			// 5.后退大区UID
			mv.addObject("regionUid", regionUid);
			
			// 6.传递地区UID
			mv.addObject("areaUid", areaUid);
			
			// 返回数据
			mv.addObject("personInfos", personInfos);
			
			return this.after(mv);
			
		}catch (SQLException e) {

			this.closeException();
			log.error("获取终端负责区县失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public ModelAndView receivePerson(String areaUid, String regionUid, String personPid) throws Exception {
		
		this.before();

		try {
			
			ModelAndView mv = null;
			
			// 如果是混合大区总地总,或者是大区总
			if("600008".equals(this.getRole()) || "600006".equals(this.getRole())) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030504"));
			} 
			
			// 如果是后勤人员
			else if("600005".equals(this.getRole())) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030406"));
			}
			
			// 查询该登录人员下未审核名单
			NBPT_SP_PERSON person = checkManageDao.receiveUncheck(personPid, this.getConnection());
			
			// 获取该人员的负责区域
			List<NBPT_COMMON_XZQXHF> responsAreas = checkManageDao.receiveUncheckResponsAreas(personPid, this.getConnection());
			
			mv.addObject("person", person);
			mv.addObject("controllAreas", responsAreas);
			mv.addObject("regionUid", regionUid);
			mv.addObject("areaUid", areaUid);
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员出错");
			throw new Exception();

		}
	}
	
	/**
	 * 获取登录人员信息方法
	 * @return
	 * @throws SQLException
	 */
	private CurrentPerson getLoginPerson() throws SQLException {

		String loginId = this.getLoginId();

		// 当前登录人员
		CurrentPerson loginPerson = personManageDao.receiveCurrentPerson(loginId, this.getConnection());
		
		return loginPerson;
	}
	
	/**
	 * 获取登录人员的id
	 * @return
	 */
	private String getLoginId() {
		
		return this.getRequest().getAttribute("userID").toString();
	}

}














