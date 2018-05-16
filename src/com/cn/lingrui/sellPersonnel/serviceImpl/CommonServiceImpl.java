package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.PersonManageDao;
import com.cn.lingrui.sellPersonnel.db.dao.SupportDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;
import com.cn.lingrui.sellPersonnel.service.CommonService;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;

import net.sf.json.JSONArray;

@Service("commonService")
public class CommonServiceImpl extends SellPBaseService implements CommonService{

	private static Logger log = LogManager.getLogger();

	@Override
	protected String getFunNum() {
		return null;
	}
	@Resource(name = "personManageDao")
	private PersonManageDao personManageDao;

	@Resource(name = "supportDao")
	private SupportDao supportDao;
	
	@Override
	public ModelAndView receiveCurrentTerminals() throws Exception {

		this.before();

		// 初始化返回
		ModelAndView mv = null;

		try {
			
			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(), this.getConnection());

			mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030402"));

			// 如果为null,则登录人员为后勤
			if(null == loginPerson) {
				
				// 除信息专员外,获取所有人信息
				List<NBPT_VIEW_CURRENTPERSON> persons = supportDao.receiveAllPersons(this.getConnection());
				
				// 查询所有部门
				List<NBPT_VIEW_REGION> regions = supportDao.receiveRegion(null, null, this.getConnection());

				// 合计信息
				List<StatisticsTable> totalInfos = SupportServiceUtils.dealCurrentPerson_total(persons);
				
				// 分类统计信息
				Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyByType = CommonUtil.classify(persons, "NBPT_SP_PERSON_TYPE", NBPT_VIEW_CURRENTPERSON.class);
				
				// 后勤人员查询OTC统计信息处理
				List<StatisticsTable> OTCInfos = SupportServiceUtils.dealCurrentPerson_otc(classifyByType.get("2"),regions);
				
				mv.addObject("totalInfos", totalInfos);
				mv.addObject("OTCInfos", OTCInfos);
				
			} else {

				// 查询该登录人员的负责区域
				NBPT_VIEW_REGION responsRegion = personManageDao.receiveRegionByResper(loginPerson.getNBPT_SP_PERSON_PID(), "2", this.getConnection());
				
				if(null == responsRegion) {
					
					mv.addObject("message", "您没有被分配管理地区,请联系后勤人员或管理员");
					
					return this.after(mv);
				}else {
					
					// 查询所有下级人员信息
					List<NBPT_VIEW_CURRENTPERSON> persons = personManageDao.receiveTerminal(null, null, responsRegion.getNBPT_SP_REGION_UID(), null, this.getConnection());
					
					Map<String, List<NBPT_VIEW_CURRENTPERSON>> personClassifyed = CommonServiceUtils.dealPersonsByKey(persons, "NBPT_SP_PERSON_FLAG");
					
					// 1.查询该地总配额
					mv.addObject("thisNeed", responsRegion.getNBPT_SP_REGION_NEED());
					
					// 2.该地总手下在职人数
					mv.addObject("thisInfact", CommonUtil.getListInMapByKey(personClassifyed, "2").size());
										
					// 在职数据
					mv.addObject("personInJobInfos", CommonUtil.getListInMapByKey(personClassifyed, "2"));

					List<NBPT_VIEW_CURRENTPERSON> unCheckedPersonInfos = new ArrayList<>();
					unCheckedPersonInfos.addAll(CommonUtil.getListInMapByKey(personClassifyed, "0"));
					unCheckedPersonInfos.addAll(CommonUtil.getListInMapByKey(personClassifyed, "1"));
					
					// 审核中数据
					mv.addObject("unCheckedPersonInfos", unCheckedPersonInfos);
					
					// 离职数据
					mv.addObject("personDimissionInfos", CommonUtil.getListInMapByKey(personClassifyed, "3"));
					
					
				}
			}
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员出错");
			throw new Exception();
		}
	}

	@Override
	public ModelAndView receiveAllCurrentTerminals() throws Exception {

		try {

			this.before();

			// 初始化返回
			ModelAndView mv = null ;
			
			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
	
			List<NBPT_VIEW_CURRENTPERSON> persons = new ArrayList<NBPT_VIEW_CURRENTPERSON>();
			
			// 后勤人员获取
			if(null == loginPerson) {
				
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030407"));
				persons = personManageDao.receiveTerminal(null, null, null, null, this.getConnection());
			}
			
			// 如果是大区总
			else if("21".equals(loginPerson.getNBPT_SP_PERSON_JOB()) || "26".equals(loginPerson.getNBPT_SP_PERSON_JOB())) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030505"));
				persons = personManageDao.receiveTerminal(loginPerson.getNBPT_SP_PERSON_REGION_UID(), null, null, null, this.getConnection());
			}
			
			// 如果是信息专员
			else if("27".equals(loginPerson.getNBPT_SP_PERSON_JOB())) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030607"));
				// 处理信息专员管理大区
				String[] reginUids = loginPerson.getNBPT_SP_PERSON_DEPT_ID().split(",");
				
				for(String regionUid : reginUids) {
					
					persons.addAll(personManageDao.receiveTerminal(regionUid, null, null, null, this.getConnection()));
				}
			}

			mv.addObject("personInfos", persons);
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员详细信息出错");
			throw new Exception();
		}
	}
	
	@Override
	public String receiveSelect(String parentId) throws Exception {

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
			log.error("获取终端户籍失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public String receiveLoginId(String loginId) throws Exception {
		try {
			this.before();
			return this.after(JSONArray.fromObject(personManageDao.receiveLoginId(loginId, this.getConnection())).toString());
		}catch (SQLException e) {

			this.closeException();
			log.error("验证登录ID时候可用出错" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public String transferSearchPerson(String personName, String transferType) throws Exception {

		try {

			this.before();
			
			// 初始化查询结果
			List<NBPT_VIEW_CURRENTPERSON> persons = new ArrayList<>();
			
			// 地总相关调职
			if("2".equals(transferType)) {
				
				// 查地总
				persons = personManageDao.receivePerson("22", personName, "2", this.getConnection());
			} 
			
			// 区县总调职相关
			else {
				
				// 查区县总
				persons = personManageDao.receivePerson(new String[] {"23", "24", "25"}, personName, "2", this.getConnection());
			}
			return this.after(JSONArray.fromObject(persons).toString());
		}catch (SQLException e) {

			this.closeException();
			log.error("获取要调职的人员失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public String transferSearchRegion(String regionName) throws Exception {
		try {

			this.before();
			
			// 初始化查询结果
			List<NBPT_VIEW_REGION> persons = new ArrayList<>();
			
			persons = personManageDao.receiveRegion(regionName, "2", null, this.getConnection());
			
			return this.after(JSONArray.fromObject(persons).toString());
		}catch (SQLException e) {

			this.closeException();
			log.error("获取要调职的人员失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}



}
