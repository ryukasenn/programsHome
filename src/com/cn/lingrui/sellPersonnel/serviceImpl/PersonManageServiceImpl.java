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

			
//			List<CurrentPerson> terminals = personManageDao.receiveCurrentTerminals(out.getPerson(),
//					this.getConnection());

			
			mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030402"));

			if(null == person) {

				// 查询所有下级人员信息
				List<CurrentPerson> personInfos = personManageDao.receiveCurrentPersonInfos(out.getPerson(),this.getConnection());

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
					
					String message = "您没有被分配管理地区,请联系后勤人员或管理员";
					mv.addObject("message", message);
					
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
			log.info("查询当前所有人员出错");
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

			// 获取登录人员信息
			String loginId = this.getRequest().getAttribute("userID").toString();

			// 获取当前登录人员信息
			CurrentPerson person = personManageDao.receiveCurrentPerson(loginId, this.getConnection());
			
			if("null".equals(person.getNBPT_SP_REGION_NEED()) || "".equals(person.getNBPT_SP_REGION_NEED())) {
				
				String message = "您没有被分配管理地区,请联系后勤人员或管理员";
				mv.addObject("message", message);
			}else {
				// 获取地总管理地区
				controllAreas = personManageDao.getAreaSelects(loginId, this.getConnection());
				controllAreas.add(0, new NBPT_COMMON_XZQXHF());
				
				dictionarys = personManageDao.receiveDictionarys("POLICYTYPE", this.getConnection());
	
				mv.addObject("controllAreas", controllAreas);
				mv.addObject("policytypeSelects", dictionarys);
				
			}

			return this.after(mv);
		} catch (SQLException e) {

			this.closeException();
			log.info("获取添加终端人员页面失败" + CommonUtil.getTrace(e));
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
		
		// 获取登录人员信息
		String loginId = this.getRequest().getAttribute("userID").toString();
		try {

			SellPersonnelPojoOut out = new SellPersonnelPojoOut();

			// 当前登录人员
			CurrentPerson loginPerson = personManageDao.receiveCurrentPerson(loginId, this.getConnection());
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
			log.info("添加终端人员错" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}


//	/**
//	 * 获取添加终端人员负责区域
//	 * @throws Exception 
//	 */
//	@Override
//	public ModelAndView getAddTerminalRespons(String parentId, String terminalId) throws Exception {
//		
//		this.before();
//		
//		// 初始化返回
//		ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030404"));
//		
//		// 1.生成管理地区单选框
//		List<NBPT_SP_REGION> controllAreas = null;
//		
//		try {
//			
//
//			// 获取登录人员信息
//			String loginId = this.getRequest().getAttribute("userID").toString();
//			
//			// 获取地总管理地区
//			controllAreas = personManageDao.getAreaSelects(loginId, this.getConnection());			
//			controllAreas.add(0, new NBPT_SP_REGION());
//			
//			// 获取人员的部门所属信息
//			NBPT_SP_REGION deptInfo = personManageDao.getTerminalDeptInfo(terminalId, this.getConnection());
//			
//			// 1.如果是新添加人员
//			if(null != parentId || !"".equals(parentId)) {}
//			
//			// 2.如果是修改
//			else {
//				
//				// 获取原有负责区域
//				List<NBPT_COMMON_XZQXHF> terminalResponsXzqxs = personManageDao.getTerminalResponsXzqx(terminalId, this.getConnection());
//				
//				mv.addObject("terminalResponsXzqxs", terminalResponsXzqxs);
//			}
//
//			mv.addObject("deptInfo", deptInfo);
//			mv.addObject("controllAreas", controllAreas);
//			
//			return this.after(mv);
//		} catch (SQLException e) {
//
//			log.info("获取添加终端人员负责区域页面失败" + CommonUtil.getTrace(e));
//			throw new Exception();
//		}
//	}

	

	@Override
	public String receiveSelect(String parentId) throws Exception{

		try {
			this.before();
			
			// 获取省级划分
			List<NBPT_COMMON_XZQXHF> xzqxhfs = personManageDao.getXzqxhfs(parentId, this.getConnection());
			
			// 获取市级划分,需要上级编号
			xzqxhfs.add(0, new NBPT_COMMON_XZQXHF());
			
			JSONArray ja = JSONArray.fromObject(xzqxhfs);
			
			return this.after(ja.toString());
		} catch (SQLException e) {
			
			this.closeException();
			log.info("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
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
			
			// 2.生成保单类型下拉框
			List<NBPT_COMMON_DICTIONARY> dictionarys = new ArrayList<NBPT_COMMON_DICTIONARY>();
			
			regions = personManageDao.getRegions("1", this.getConnection());
			regions.add(0, new NBPT_SP_REGION());
			
			// 2.生成保单类型下拉框
			dictionarys = personManageDao.receiveDictionarys("POLICYTYPE", this.getConnection());

			
			mv.addObject("regions", regions);
			mv.addObject("policytypeSelects", dictionarys);
			
			return after(mv);
		} catch (SQLException e) {
			
			this.closeException();
			log.info("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
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
			
			JSONArray ja = JSONArray.fromObject(xzqxhfs);
			
			return this.after(ja.toString());
		}catch (SQLException e) {

			this.closeException();
			log.info("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	/**
	 * 获取修改终端页面
	 */
	@Override
	public ModelAndView getChangePerson(String changePersonPid) throws Exception {



		// 1.生成管理地区单选框
		List<NBPT_COMMON_XZQXHF> controllAreas = null;
		// 2.生成保单类型下拉框
		List<NBPT_COMMON_DICTIONARY> dictionarys = null;
		try {

			// 初始化返回数据
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030403"));
			
			this.before();

			// 获取登录人员信息
			String loginId = this.getRequest().getAttribute("userID").toString();
			
			// 获取地总管理地区
			controllAreas = personManageDao.getAreaSelects(loginId, this.getConnection());
			controllAreas.add(0, new NBPT_COMMON_XZQXHF());

			dictionarys = personManageDao.receiveDictionarys("POLICYTYPE", this.getConnection());
			
			// 1.查询人员信息
			CurrentPerson person = personManageDao.receiveCurrentTerminal(changePersonPid, this.getConnection());
			
			mv.addObject("controllAreas", controllAreas);
			mv.addObject("policytypeSelects", dictionarys);
			mv.addObject("person", person);

			return after(mv);
		} catch (SQLException e) {
			
			this.closeException();
			log.info("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
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
		
			// 管理区县
			List<NBPT_COMMON_XZQXHF> terminalResponsXzqx = personManageDao.getTerminalResponsXzqx(changePersonPid, this.getConnection());
			
			JSONArray ja = JSONArray.fromObject(terminalResponsXzqx);
			
			return this.after(ja.toString());
		}catch (SQLException e) {

			this.closeException();
			log.info("获取终端负责区县失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public String receiveTerminalPlace(String idNum) throws Exception {

		try {
			
			this.before();
		

			// 获取国家行政区县划分
			List<NBPT_COMMON_XZQXHF> personIdqx = personManageDao.checkPlace(idNum.substring(0, 6), this.getConnection());
			
			JSONArray ja = JSONArray.fromObject(personIdqx);
			
			return this.after(ja.toString());
		}catch (SQLException e) {

			this.closeException();
			log.info("获取终端负责区县失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}
}














