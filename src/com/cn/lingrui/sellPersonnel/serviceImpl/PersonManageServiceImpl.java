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
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.InfoCommissionerDao;
import com.cn.lingrui.sellPersonnel.db.dao.PersonManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson_statistics;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;
import com.cn.lingrui.sellPersonnel.service.PersonManageService;

@Service("personManageService")
public class PersonManageServiceImpl extends SellPBaseService implements PersonManageService {

	private static Logger log = LogManager.getLogger();

	@Resource(name = "personManageDao")
	private PersonManageDao personManageDao;

	@Resource(name="infoCommissionerDao")
	private InfoCommissionerDao checkManageDao;
	
	@Override
	protected String getFunNum() {
		return null;
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
			
			// 如果是信息专员
			else if("27".equals(loginPerson.getNBPT_SP_PERSON_JOB())) {
				
				// 2.1查询信息专员协管下,所有人员信息
				String[] regionUids = loginPerson.getREGION_UID().split(",");
				
				for(String uid : regionUids) {
					personInfos.addAll(personManageDao.receiveCurrentProvincePersonInfos(uid, this.getConnection()));
				}

				// 2.2添加页面信息
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030502"));
				mv.addObject("regionUid", loginPerson.getREGION_UID());
			}

			// 人员信息处理
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
			
			// 获取终端人员信息
			//List<NBPT_VIEW_CURRENTPERSON> personInfos = personManageDao.receiveTerminal(null, null, areaUid, null, this.getConnection());
			
			// 获取负责人信息
			//NBPT_VIEW_CURRENTPERSON resperInfo = personManageDao.re
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
			
			// 如果是大区总,或信息专员
			else if("21".equals(loginPerson.getNBPT_SP_PERSON_JOB()) 
					|| "26".equals(loginPerson.getNBPT_SP_PERSON_JOB())
					|| "27".equals(loginPerson.getNBPT_SP_PERSON_JOB())) {

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
			if("600008".equals(this.getRole()) 
					|| "600006".equals(this.getRole()) 
					|| "600007".equals(this.getRole())) {// 信息专员

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030504"));
			} 
			
			// 如果是后勤人员
			else if("600005".equals(this.getRole())) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030406"));
			}
			
			// 查询人员具体信息
			NBPT_SP_PERSON person = checkManageDao.receiveUncheck(personPid, this.getConnection());
			
			// 获取该人员的负责区域
			List<NBPT_COMMON_XZQXHF> responsAreas = checkManageDao.receiveTerminalResponsAreas(personPid, this.getConnection());
			
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
	


}














