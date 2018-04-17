package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.PersonManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson_statistics;
import com.cn.lingrui.sellPersonnel.pojos.SellPersonnelPojoOut;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;
import com.cn.lingrui.sellPersonnel.service.SupportSerivce;

@Service("supportService")
public class SupportServiceImpl extends SellPBaseService implements SupportSerivce{
	
	private static Logger log = LogManager.getLogger();

	@Resource(name = "personManageDao")
	private PersonManageDao personManageDao;

	@Override
	protected String getFunNum() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ModelAndView receiveAllTerminals() throws Exception {

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

			// 如果为null,则登录人员为后勤
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

				
			}
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员出错");
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
}
