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
