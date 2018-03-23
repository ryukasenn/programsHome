package com.cn.lingrui.sellPersonnel.serviceImpl;


import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.CheckManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.check.UncheckPerson;
import com.cn.lingrui.sellPersonnel.service.CheckManageService;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;

@Service("checkManageService")
public class CheckManageServiceImpl extends SellPBaseService implements CheckManageService {

	private static Logger log = LogManager.getLogger();
	
	@Resource(name="checkManageDao")
	private CheckManageDao checkManageDao;

	@Override
	protected String getFunNum() {
		// TODO 自动生成的方法存根
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
			List<UncheckPerson> uncheckList = checkManageDao.receiveUnchecks(userId, this.getConnection());
			
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
			NBPT_SP_PERSON person = checkManageDao.receiveUncheck(uncheckpid, this.getConnection());
			
			// 获取该人员的负责区域
			List<NBPT_COMMON_XZQXHF> responsAreas = checkManageDao.receiveUncheckResponsAreas(uncheckpid, this.getConnection());
			
			mv.addObject("person", person);
			mv.addObject("controllAreas", responsAreas);
			
			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员出错");
			throw new Exception();

		}
	}
	

}





























