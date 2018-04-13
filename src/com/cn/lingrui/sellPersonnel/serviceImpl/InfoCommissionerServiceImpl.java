package com.cn.lingrui.sellPersonnel.serviceImpl;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.InfoCommissionerDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.infoCommissioner.UncheckPerson;
import com.cn.lingrui.sellPersonnel.pojos.infoCommissioner.InfoCommissionerPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.infoCommissioner.StatisticsTable;
import com.cn.lingrui.sellPersonnel.service.InfoCommissionerService;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;

/**
 * 信息专员权限业务service
 * @author lhc
 *
 */
@Service("checkManageService")
public class InfoCommissionerServiceImpl extends SellPBaseService implements InfoCommissionerService {

	private static Logger log = LogManager.getLogger();
	
	@Resource(name="infoCommissionerDao")
	private InfoCommissionerDao infoCommissionerDao;

	@Override
	protected String getFunNum() {
		return null;
	}

	@Override
	public ModelAndView receiveUnchecks() throws Exception {


		this.before();

		// 初始化返回
		ModelAndView mv = null;

		try {
			
			mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030302"));
			// 获取登录名
			String userId = this.getRequest().getAttribute("userID").toString();
			
			// 查询该登录人员下未审核名单
			List<UncheckPerson> uncheckList = infoCommissionerDao.receiveUnchecks(userId, this.getConnection());
			
			mv.addObject("uncheckList", uncheckList);
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员出错");
			throw new Exception();

		}
	}

	@Override
	public ModelAndView receiveUncheck(String uncheckpid) throws Exception {

		this.before();

		try {
			
			ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030303"));
			
			// 查询该登录人员下未审核名单
			NBPT_SP_PERSON person = infoCommissionerDao.receiveUncheck(uncheckpid, this.getConnection());
			
			// 获取该人员的负责区域
			List<NBPT_COMMON_XZQXHF> responsAreas = infoCommissionerDao.receiveTerminalResponsAreas(uncheckpid, this.getConnection());
			
			mv.addObject("person", person);
			mv.addObject("controllAreas", responsAreas);
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员出错");
			throw new Exception();

		}
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
			List<List<NBPT_VIEW_CURRENTPERSON>> classfyedResults = InfoCommissionerServiceUtils.dealByKey(persons, "NBPT_SP_PERSON_REGION_UID");
			
			// 处理结果
			List<StatisticsTable> resultTables = InfoCommissionerServiceUtils.countClassifyList_byRegion(classfyedResults);
			
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
			
			// 分类人员信息
			List<List<NBPT_VIEW_CURRENTPERSON>> classfyedResults = InfoCommissionerServiceUtils.dealByKey(persons, "NBPT_SP_PERSON_PROVINCE_ID");
			
			// 处理结果
			List<StatisticsTable> resultTables = InfoCommissionerServiceUtils.countClassifyList_byProvince(classfyedResults);
			
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
			
			// 分类人员信息
			List<List<NBPT_VIEW_CURRENTPERSON>> classfyedResults = InfoCommissionerServiceUtils.dealByKey(persons, "NBPT_SP_PERSON_AREA_UID");
			
			// 处理结果
			List<StatisticsTable> resultTables = InfoCommissionerServiceUtils.countClassifyList_byArea(classfyedResults);
			
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

			persons = infoCommissionerDao.receiveTerminal(null, null, in.getAreaUid(), null, this.getConnection());
			
			mv.addObject("persons", persons);

			mv.addObject("provinceId", persons.get(0).getNBPT_SP_PERSON_PROVINCE_ID());
			mv.addObject("thisNeed", persons.get(0).getNBPT_SP_PERSON_AREA_NEED());
			mv.addObject("thisInfact", persons.size());
			mv.addObject("thisResper", persons.get(0).getNBPT_SP_PERSON_AREA_RESPONSIBLER_NAME());
			mv.addObject("thisArea", persons.get(0).getNBPT_SP_PERSON_AREA_NAME());
			
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





























