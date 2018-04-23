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
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.pojos.AddPersonPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.SellPersonnelPojoOut;
import com.cn.lingrui.sellPersonnel.service.AreaHeadService;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;

@Service("areaHeadService")
public class AreaHeadServiceImpl extends SellPBaseService implements AreaHeadService {

	private static Logger log = LogManager.getLogger();
	
	@Resource(name = "personManageDao")
	private PersonManageDao personManageDao;
	
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
			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(), this.getConnection());

			if(!loginPerson.getNBPT_SP_PERSON_PID().equals(loginPerson.getNBPT_SP_PERSON_AREA_RESPONSIBLER_PID())) {
				
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

	@Override
	public ModelAndView postAddTerminal(AddPersonPojoIn in) throws Exception {

		this.before();
		
		// 初始化保存数据
		NBPT_SP_PERSON person = new NBPT_SP_PERSON();

		ModelAndView mv = null;
		
		try {

			SellPersonnelPojoOut out = new SellPersonnelPojoOut();

			// 当前登录人员
			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
			
			out.setPerson(loginPerson);
			if(null == in.getNBPT_SP_PERSON_PID() || "".equals(in.getNBPT_SP_PERSON_PID()) ) {

				// 最大人员ID
				String maxId = personManageDao.receiveMaxId(this.getConnection(), "NBPT_SP_PERSON");
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
				
				// 更新人员信息
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
			NBPT_VIEW_CURRENTPERSON person = personManageDao.receiveTerminal(null,null,null,changePersonPid, this.getConnection()).get(0);
			
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

	@Override
	protected String getFunNum() {
		// TODO 自动生成的方法存根
		return null;
	}
	
}
