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
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;
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
	
	@Override
	public ModelAndView getProvincePersons(String regionUid) throws Exception {

		try {
			
			this.before();
			
			// 初始化大区下所有人员信息
			List<NBPT_VIEW_CURRENTPERSON> personInfos = new ArrayList<>();

			// 初始化该大区下所有地区
			List<NBPT_VIEW_REGION> regions =new ArrayList<>();
			
			ModelAndView mv = null;
			
			// 如果为空,则是后勤登录
			if(null != regionUid) {

				// 2.1查询大区下所有终端人员信息
				personInfos = personManageDao.receiveTerminal(regionUid, null, null, null, this.getConnection());

				// 2.2 查询该大区下所有地区
				regions = personManageDao.receiveRegion(regionUid, null, this.getConnection());

				// 2.3添加页面信息
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030404"));				
				mv.addObject("regionUid", regionUid);
			}
			
			// 如果是大区总
			else {
				
				NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
				
				// 2.1查询大区总下所有人员信息
				personInfos = personManageDao.receiveTerminal(loginPerson.getNBPT_SP_PERSON_REGION_UID(), null, null, null, this.getConnection());

				// 2.2 查询该大区下所有地区
				regions = personManageDao.receiveRegion(loginPerson.getNBPT_SP_PERSON_REGION_UID(), null, this.getConnection());
				
				// 2.3添加页面信息
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030502"));				
				mv.addObject("regionUid", loginPerson.getNBPT_SP_PERSON_REGION_UID());
			} 

			
			// 按地区分类人员信息
			Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyiedPersons = CommonServiceUtils.dealPersonsByKey(personInfos, "NBPT_SP_PERSON_AREA_UID");
			
			// 人员信息处理
			List<StatisticsTable> infos = PersonManageServiceUtils.countClassifyList_byArea(regions, classifyiedPersons);
			
			mv.addObject("infos", infos);
			
			return this.after(mv);
		}catch (SQLException e) {

			this.closeException();
			log.error("获取省区终端信息失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}
	
	@Override
	public ModelAndView getAreaPersons(String areaUid, String regionUid) throws Exception {

		try {
			
			this.before();

			// 初始化返回信息
			ModelAndView mv = null;
			
			// 如果为空,则是后勤登录
			if("600005".equals(this.getRole())) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030405"));
			}
			
			// 如果是大区总
			else {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030503"));
			}
			
			// 获取终端人员信息
			List<NBPT_VIEW_CURRENTPERSON> personInfos = personManageDao.receiveTerminal(null, null, areaUid, null, this.getConnection());
			
			Map<String, List<NBPT_VIEW_CURRENTPERSON>> personsClassifyed = CommonServiceUtils.dealPersonsByKey(personInfos, "NBPT_SP_PERSON_FLAG");
			// 获取地区信息
			NBPT_VIEW_REGION regionInfo = personManageDao.receiveRegion(areaUid, this.getConnection());
			
			// 1.该地总手下人数
			mv.addObject("thisInfact", personsClassifyed.get("2").size());
			
			// 2.该地区负责人
			mv.addObject("thisResper", "".equals(regionInfo.getNBPT_SP_REGION_RESPONSIBLER_NAME()) ? "" :
					(regionInfo.getNBPT_SP_REGION_RESPONSIBLER_NAME() + 
							("22".equals(regionInfo.getNBPT_SP_REGION_RESPONSIBLER_JOB()) ? "(地总)" : "(大区总兼地总)")));
			
			// 3.传递地区UID
			mv.addObject("areaUid", areaUid);
			// 3.传递大区UID
			mv.addObject("regionUid", regionUid);
			
			// 返回在职数据
			mv.addObject("personInfos", CommonUtil.getListInMapByKey(personsClassifyed, "2"));
			
			// 返回离职数据
			mv.addObject("personDimissionInfos", CommonUtil.getListInMapByKey(personsClassifyed, "3"));

			List<NBPT_VIEW_CURRENTPERSON> unCheckedPersonInfos = new ArrayList<>();
			unCheckedPersonInfos.addAll(CommonUtil.getListInMapByKey(personsClassifyed, "0"));
			unCheckedPersonInfos.addAll(CommonUtil.getListInMapByKey(personsClassifyed, "1"));
			
			// 审核中数据
			mv.addObject("unCheckedPersonInfos", unCheckedPersonInfos);
			
			// 返回地区信息
			mv.addObject("regionInfo", regionInfo);
			
			return this.after(mv);
			
		}catch (SQLException e) {

			this.closeException();
			log.error("获取地区终端数据失败" + CommonUtil.getTrace(e));
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
			List<NBPT_VIEW_CURRENTPERSON> person = personManageDao.receiveTerminal(null,null,null,personPid,this.getConnection());
			
			// 获取该人员的负责区域
			List<NBPT_COMMON_XZQXHF> responsAreas = personManageDao.receiveTerminalResponsAreas(personPid, this.getConnection());
			
			mv.addObject("person", person.get(0));
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
}





































