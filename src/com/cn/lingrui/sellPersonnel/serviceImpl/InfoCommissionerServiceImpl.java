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
import com.cn.lingrui.sellPersonnel.db.dao.InfoCommissionerDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;
import com.cn.lingrui.sellPersonnel.pojos.infoCommissioner.InfoCommissionerPojoIn;
import com.cn.lingrui.sellPersonnel.service.InfoCommissionerService;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;

/**
 * 信息专员权限业务service
 * @author lhc
 *
 */
@Service("infoCommissionerService")
public class InfoCommissionerServiceImpl extends SellPBaseService implements InfoCommissionerService {

	private static Logger log = LogManager.getLogger();
	
	@Resource(name="infoCommissionerDao")
	private InfoCommissionerDao infoCommissionerDao;

	@Override
	protected String getFunNum() {
		return null;
	}

	

	@Override
	public ModelAndView receiveAllTerminals() throws Exception {
		
		this.before();

		try {
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030602"));
			
			// 获取登录的信息专员
			NBPT_VIEW_CURRENTPERSON loginPerson = infoCommissionerDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
			
			// 查询登录人员管理下所有人员信息
			List<NBPT_VIEW_CURRENTPERSON> persons = new ArrayList<>();
			
			// 处理信息专员管理大区
			String[] reginUids = loginPerson.getNBPT_SP_PERSON_DEPT_ID().split(",");
			
			for(String regionUid : reginUids) {
				
				persons.addAll(infoCommissionerDao.receiveTerminal(regionUid, null, null, null, this.getConnection()));
			}
			
			
			// 分类人员信息
			Map<String, List<NBPT_VIEW_CURRENTPERSON>> classfyedResults = CommonServiceUtils.dealPersonsByKey(persons, "NBPT_SP_PERSON_REGION_UID");
			
			// 获取大区信息
			List<NBPT_VIEW_REGION> regions = infoCommissionerDao.receiveRegion(reginUids, this.getConnection());
			
			// 处理结果
			List<StatisticsTable> resultTables = InfoCommissionerServiceUtils.countClassifyList_byRegion(classfyedResults, regions);
			
			mv.addObject("tables", resultTables);
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员出错");
			throw new Exception();
		} 
	}

	@Override
	public ModelAndView receiveRegionTerminals(InfoCommissionerPojoIn in) throws Exception {
		
		try {
			
			this.before();
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030603"));
			
			// 查询登录人员管理下所有人员信息
			List<NBPT_VIEW_CURRENTPERSON> persons = new ArrayList<>();
			persons = infoCommissionerDao.receiveTerminal(in.getRegionUid(), null,  null, null, this.getConnection());
			
			// 查询登录人员管理下所有省份下的地区
			List<NBPT_VIEW_REGION> regions = infoCommissionerDao.receiveRegion(in.getRegionUid(), null, this.getConnection());
			
			// 分类地区信息
			Map<String, List<NBPT_VIEW_REGION>> classfyedRegions = CommonServiceUtils.dealRegionsByKey(regions, "NBPT_SP_REGION_PROVINCE_ID");
			
			// 分类人员信息
			Map<String, List<NBPT_VIEW_CURRENTPERSON>> classfyedPersons = CommonServiceUtils.dealPersonsByKey(persons, "NBPT_SP_PERSON_PROVINCE_ID");
			
			// 处理结果
			List<StatisticsTable> resultTables = InfoCommissionerServiceUtils.countClassifyList_byProvince(classfyedRegions, classfyedPersons);
			
			mv.addObject("tables", resultTables);
			
			return this.after(mv);
	
		} catch (SQLException e) {

			this.closeException();
			log.error("查询大区下所有人员出错");
			throw new Exception();
		} 
	}

	@Override
	public ModelAndView receiveProvinceTerminals(InfoCommissionerPojoIn in) throws Exception {

		try {
			
			this.before();
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030604"));

			// 查询登录人员管理下所有人员信息
			List<NBPT_VIEW_CURRENTPERSON> persons = new ArrayList<>();
			persons = infoCommissionerDao.receiveTerminal(null, in.getProvinceId(), null, null, this.getConnection());
			
			// 查询所有地区
			List<NBPT_VIEW_REGION> regions = infoCommissionerDao.receiveRegion(null, in.getProvinceId(), this.getConnection());
			
			// 分类人员信息
			Map<String, List<NBPT_VIEW_CURRENTPERSON>> classfyedPersons = CommonServiceUtils.dealPersonsByKey(persons, "NBPT_SP_PERSON_AREA_UID");
			
			// 处理结果
			List<StatisticsTable> resultTables = InfoCommissionerServiceUtils.countClassifyList_byArea(regions, classfyedPersons);
			
			mv.addObject("tables", resultTables);

			mv.addObject("regionUid", resultTables.get(0).getRegionUid());
			
			return this.after(mv);
	
		} catch (SQLException e) {

			this.closeException();
			log.error("查询省区下所有人员出错");
			throw new Exception();
		} 
	}

	@Override
	public ModelAndView receiveAreaTerminals(InfoCommissionerPojoIn in) throws Exception {

		try {
			
			this.before();
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030605"));

			// 查询地区下所有人员信息
			List<NBPT_VIEW_CURRENTPERSON> persons = new ArrayList<>();

			NBPT_VIEW_REGION region = infoCommissionerDao.receiveRegion(in.getAreaUid(), this.getConnection());
			
			persons = infoCommissionerDao.receiveTerminal(null, null, in.getAreaUid(), null, this.getConnection());
			
			Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifiedPersons = CommonServiceUtils.dealPersonsByKey(persons, "NBPT_SP_PERSON_FLAG");
			
			// 在职人员
			mv.addObject("persons", CommonUtil.getListInMapByKey(classifiedPersons, "2"));
			// 离职人员
			mv.addObject("personsDimission", CommonUtil.getListInMapByKey(classifiedPersons, "3"));

			List<NBPT_VIEW_CURRENTPERSON> unCheckedPersonInfos = new ArrayList<>();
			unCheckedPersonInfos.addAll(CommonUtil.getListInMapByKey(classifiedPersons, "0"));
			unCheckedPersonInfos.addAll(CommonUtil.getListInMapByKey(classifiedPersons, "1"));
			
			// 审核中数据
			mv.addObject("unCheckedPersonInfos", unCheckedPersonInfos);
			

			mv.addObject("provinceId", region.getNBPT_SP_REGION_PROVINCE_ID());
			mv.addObject("thisNeed", region.getNBPT_SP_REGION_NEED());
			mv.addObject("thisInfact", classifiedPersons.get("2").size());
			mv.addObject("thisResper", region.getNBPT_SP_REGION_RESPONSIBLER_NAME());
			mv.addObject("thisArea", region.getNBPT_SP_REGION_NAME());
			
			return this.after(mv);
	
		} catch (SQLException e) {

			this.closeException();
			log.error("查询省区下所有人员出错");
			throw new Exception();
		} 
	}

	@Override
	public ModelAndView receiveTerminal(InfoCommissionerPojoIn in) throws Exception {

		try {
			
			this.before();
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030606"));

			// 查询人员详细信息
			NBPT_VIEW_CURRENTPERSON person = infoCommissionerDao.receiveTerminal(null, null, null, in.getTerminalPid(), this.getConnection()).get(0);

			// 获取该人员的负责区域
			List<NBPT_COMMON_XZQXHF> responsAreas = infoCommissionerDao.receiveTerminalResponsAreas(in.getTerminalPid(), this.getConnection());
			
			mv.addObject("person", person);

			mv.addObject("controllAreas", responsAreas);
			mv.addObject("areaUid", person.getNBPT_SP_PERSON_AREA_UID());
			
			return this.after(mv);
	
		} catch (SQLException e) {

			this.closeException();
			log.error("查询省区下所有人员出错");
			throw new Exception();
		} 
	}
	

}





























