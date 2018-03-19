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
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_REGION;
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
		// TODO 自动生成的方法存根
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
			this.dealCurrentPerson(person, out);

			// 查询所有下级人员信息
			List<CurrentPerson> personInfos = personManageDao.receiveCurrentPersonInfos(out.getPerson(),this.getConnection());
			
//			List<CurrentPerson> terminals = personManageDao.receiveCurrentTerminals(out.getPerson(),
//					this.getConnection());

			
			mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030402"));

			if(null == person) {
				
				// 后勤人员查询合计信息处理
				List<CurrentPerson_statistics> infos = this.dealCurrentPerson_total(personInfos);
				
				// 后勤人员查询OTC统计信息处理
				List<CurrentPerson_statistics> otcInfos = this.dealCurrentPerson_region(personInfos);
				
				// 合计信息
				mv.addObject("totalInfos", infos);
				
				// OTC统计信息
				mv.addObject("OTCInfos", otcInfos);
			} else {
				
				// 如果是地总
				if("22".equals(person.getNBPT_SP_PERSON_JOB())) {
					
					// 1.查询该地总配额
					mv.addObject("thisNeed", person.getNBPT_SP_REGION_NEED());
					
					// 2.该地总手下人数
					mv.addObject("thisInfact", personInfos.size());
				}
				// 处理返回数据
				mv.addObject("personInfos", personInfos);
			}
			
			return this.after(mv);

		} catch (SQLException e) {

			log.info("查询当前所有人员出错");
			throw new Exception();

		}
	}

	/**
	 * 处理当前登录人员信息
	 * 
	 * @param persons
	 * @param out
	 * @throws Exception
	 */
	private void dealCurrentPerson(CurrentPerson person, SellPersonnelPojoOut out) throws Exception {

		// 验证唯一性
		if (null == person) {

			// 如果有权限操作没有相应的人员信息,是后勤
			out.setLoginDeptId("0");
		} else {

			// 获取部门信息
			out.setLoginDeptId(person.getNBPT_SP_PERSON_DEPT_ID());

			// 存储登录人员信息
			out.setPerson(person);
		}

	}

	/**
	 *  后勤人员查询合计信息处理
	 * @return
	 */
	private List<CurrentPerson_statistics> dealCurrentPerson_total(List<CurrentPerson> personInfos){
		
		List<String> checkItems = new ArrayList<>();
		List<CurrentPerson_statistics> resultList = new ArrayList<>();
		for(CurrentPerson person : personInfos) {
			
			String type = person.getNBPT_SP_PERSON_TYPE();
			if(checkItems.contains(type)) {
				
				// 获取当前信息
				CurrentPerson_statistics thisInfo = resultList.get(checkItems.indexOf(type));
				addPerson_total(thisInfo, person);
			} else {
				
				// 加入检查列表
				checkItems.add(type);

				// 新增当前信息
				CurrentPerson_statistics thisInfo = new CurrentPerson_statistics();
				addPerson_total(thisInfo, person);
				resultList.add(thisInfo);
			}
		}
		return resultList;
	}

	/**
	 * 统计信息辅助方法
	 * @param thisInfo
	 * @param person
	 */
	private void addPerson_total(CurrentPerson_statistics thisInfo,CurrentPerson person) {
		
		String type = person.getNBPT_SP_PERSON_TYPE();
		// 如果是商务的
		if("1".equals(type)) {

			thisInfo.setName("商务");
			// TODO
			thisInfo.setAreaResper(thisInfo.getAreaResper() + 1);
		} 
		
		// 如果是OTC
		else if("2".equals(type)) {
			
			thisInfo.setName("OTC");
			
			// 职务类型
			String job = person.getNBPT_SP_PERSON_JOB();
			
			switch (job) {
			
			case "21": // 如果是大区总
				
				thisInfo.setRegionResper(thisInfo.getRegionResper() + 1);
				break;
				
			case "22": // 如果是地总
				
				thisInfo.setAreaResper(thisInfo.getAreaResper() + 1);
				break;
				
			case "23": // 如果是区县总
				
				thisInfo.setXzquResper(thisInfo.getXzquResper() + 1);
				break;
				
			case "24": // 如果是预备区县总
				
				thisInfo.setXzquResper_preparatory(thisInfo.getXzquResper_preparatory() + 1);
				break;
				
			case "25": // 如果是推广经理
				
				thisInfo.setPromote(thisInfo.getPromote() + 1);
				break;
				
			default:
				break;			
			}
		}
		
		// 如果是临床
		else if("3".equals(type)) {

			thisInfo.setName("临床");
			// TODO
			thisInfo.setAreaResper(thisInfo.getAreaResper() + 1);
		}
		
		// 如果是诊所
		else if("4".equals(type)) {

			thisInfo.setName("诊所");
			// TODO
			thisInfo.setAreaResper(thisInfo.getAreaResper() + 1);			
		}
	}
	/**
	 * 后勤人员查询OTC统计信息处理
	 * @return
	 */
	private List<CurrentPerson_statistics> dealCurrentPerson_region(List<CurrentPerson> personInfos){
		
		List<String> checkItems = new ArrayList<>();
		List<CurrentPerson_statistics> resultList = new ArrayList<>(); 
		for(CurrentPerson person : personInfos) {
			
			String region = person.getNBPT_SP_PERSON_DEPT_ID();
			
			// 按大区添加信息
			if(checkItems.contains(region)) {
				
				CurrentPerson_statistics thisInfo = resultList.get(checkItems.indexOf(region));
				addPerson_otc(thisInfo, person);
			} else {

				// 加入检查列表
				checkItems.add(region);
				
				// 新增当前信息
				CurrentPerson_statistics thisInfo = new CurrentPerson_statistics();
				thisInfo.setName(person.getNBPT_SP_REGION_NAME());
				
				addPerson_otc(thisInfo, person);
				resultList.add(thisInfo);
			}
		}
		return resultList;
	}
	
	/**
	 * 统计OTC信息辅助方法
	 * @param thisInfo
	 * @param person
	 */
	private void addPerson_otc(CurrentPerson_statistics thisInfo, CurrentPerson person) {
		
		String job = person.getNBPT_SP_PERSON_JOB();
		switch (job) {
		
			// 如果是大区总
			case "21":
				thisInfo.setRegionResper(thisInfo.getRegionResper() + 1);
				break;
			
			// 如果是地总
			case "22":
				thisInfo.setAreaResper(thisInfo.getAreaResper() + 1);
				break;
			
			// 如果是区县总
			case "23":
				thisInfo.setXzquResper(thisInfo.getXzquResper() + 1);
				break;
				
			// 如果是预备区县总
			case "24":
				thisInfo.setXzquResper_preparatory(thisInfo.getXzquResper_preparatory() + 1);
				break;
				
			// 如果是推广经理
			case "25":
				thisInfo.setPromote(thisInfo.getPromote() + 1);
				break;
				
			// 如果是混合大区总
			case "26":
				thisInfo.setRegionResper(thisInfo.getRegionResper() + 1);
				break;
		default:
			break;
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
			
			// 获取地总管理地区
			controllAreas = personManageDao.getAreaSelects(loginId, this.getConnection());
			controllAreas.add(0, new NBPT_COMMON_XZQXHF());
			
			dictionarys = personManageDao.receiveDictionarys("POLICYTYPE", this.getConnection());

			mv.addObject("controllAreas", controllAreas);
			mv.addObject("policytypeSelects", dictionarys);
			
			return this.after(mv);
		} catch (SQLException e) {

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
			
			// 最大人员ID
			String maxId = personManageDao.receiveMaxId("RECEIVEMAXID", this.getConnection(), "NBPT_SP_PERSON");
			out.setMaxId(maxId);
			
			// 管理区县
			List<NBPT_SP_PERSON_XZQX> reposAreas = new ArrayList<NBPT_SP_PERSON_XZQX>();
			
			// 生成数据
			checkInData(in, person, reposAreas, out);
			
			// 存储新的人员信息
			personManageDao.addTerminal(person, this.getConnection());
			
			personManageDao.addReposeAreas(reposAreas, this.getConnection());
			
			mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030402"));
						
			return this.after(mv);
			
		} catch (Exception e) {

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

	/**
	 * 地总添加人员信息注入
	 * @param in
	 * @param person
	 * @param reposAreas 
	 * @param reposAreas
	 * @param out
	 */
	private void checkInData(AddPersonPojoIn in, NBPT_SP_PERSON person, List<NBPT_SP_PERSON_XZQX> reposAreas, SellPersonnelPojoOut out) {
		
		// 管理区域添加
		String[] areas = in.getNBPT_SP_PERSON_AREANO().split("&");
		List<String> checkItems = new ArrayList<>();
		for(int i = 0 ; i < areas.length ; i++) {
			
			if(checkItems.contains(areas[i])) {
				
			}else if(null == areas[i] || "".equals(areas[i]) ){
				
			}else {
				checkItems.add(areas[i]);
				reposAreas.add(new NBPT_SP_PERSON_XZQX(person.getNBPT_SP_PERSON_ID(), areas[i]));				
			}
		}
		
		// 生成随机ID码
		person.setNBPT_SP_PERSON_PID(CommonUtil.getUUID_32());
		
		// 最大编号
		person.setNBPT_SP_PERSON_ID(out.getMaxId());

		// 添加部门
		person.setNBPT_SP_PERSON_DEPT_ID(out.getPerson().getNBPT_SP_PERSON_DEPT_ID());
		
		// 人员类型
		person.setNBPT_SP_PERSON_TYPE(out.getPerson().getNBPT_SP_PERSON_TYPE());
		
		// 人员姓名
		person.setNBPT_SP_PERSON_NAME(in.getNBPT_SP_PERSON_NAME());
		
		// 人员性别
		person.setNBPT_SP_PERSON_MALE(in.getNBPT_SP_PERSON_MALE());
		
		// 人员生日,由身份证号码截取
		if(15 == in.getNBPT_SP_PERSON_IDNUM().length()) {
			
			person.setNBPT_SP_PERSON_BIRS("19" + in.getNBPT_SP_PERSON_IDNUM().substring(6, 12));
		} else if(18 == in.getNBPT_SP_PERSON_IDNUM().length()) {
			
			person.setNBPT_SP_PERSON_BIRS(in.getNBPT_SP_PERSON_IDNUM().substring(6, 14));
		}
		
		// 身份证号码
		person.setNBPT_SP_PERSON_IDNUM(in.getNBPT_SP_PERSON_IDNUM());
		
		// 个人联系电话
		person.setNBPT_SP_PERSON_MOB1(in.getNBPT_SP_PERSON_MOB1());
		
		// 紧急联系人电话
		person.setNBPT_SP_PERSON_MOB2(in.getNBPT_SP_PERSON_MOB2());
		
		// QQ号码
		person.setNBPT_SP_PERSON_QQ(in.getNBPT_SP_PERSON_QQ());
	
		// 微信
		person.setNBPT_SP_PERSON_CHAT(in.getNBPT_SP_PERSON_CHAT());
		
		// 邮箱
		person.setNBPT_SP_PERSON_MAIL(in.getNBPT_SP_PERSON_MAIL());
		
		// 入职时间
		person.setNBPT_SP_PERSON_ENTRYDATA(in.getNBPT_SP_PERSON_ENTRYDATA());
		
		// 学历
		person.setNBPT_SP_PERSON_DEGREE(in.getNBPT_SP_PERSON_DEGREE());
		
		// 籍贯
		person.setNBPT_SP_PERSON_PLACE(in.getNBPT_SP_PERSON_PLACE());
		
		// 毕业学校
		person.setNBPT_SP_PERSON_SCHOOL(in.getNBPT_SP_PERSON_SCHOOL());
		
		// 专业
		person.setNBPT_SP_PERSON_PROFESS(in.getNBPT_SP_PERSON_PROFESS());
		
		// 职称
		person.setNBPT_SP_PERSON_TITLE(in.getNBPT_SP_PERSON_TITLE());
		
		// 职务
		person.setNBPT_SP_PERSON_JOB(in.getNBPT_SP_PERSON_JOB());
		
		// 保单编号
		person.setNBPT_SP_PERSON_POLICYNO(in.getNBPT_SP_PERSON_POLICYNO());
		
		// 保单开始时间
		person.setNBPT_SP_PERSON_POLICY_DATA1(in.getNBPT_SP_PERSON_POLICY_DATA1());
		
		// 保单结束时间
		person.setNBPT_SP_PERSON_POLICY_DATA2(in.getNBPT_SP_PERSON_POLICY_DATA2());
		
		// 登录名
		person.setNBPT_SP_PERSON_LOGINID(in.getNBPT_SP_PERSON_LOGINID());
		
		// 备注
		person.setNBPT_SP_PERSON_NOTE(in.getNBPT_SP_PERSON_NOTE());
		
		// 保单类型
		person.setNBPT_SP_PERSON_POLICYTYPE(in.getNBPT_SP_PERSON_POLICYTYPE());
		
		
	}

	@Override
	public String receiveSelect(String parentId) throws SQLException{

		this.before();
		
		// 获取省级划分
		List<NBPT_COMMON_XZQXHF> xzqxhfs = personManageDao.getXzqxhfs(parentId, this.getConnection());
		
		// 获取市级划分,需要上级编号
		xzqxhfs.add(0, new NBPT_COMMON_XZQXHF());
		
		JSONArray ja = JSONArray.fromObject(xzqxhfs);
		
		return this.after(ja.toString());
	}

	
	/**
	 * 获取后勤添加人员页面
	 */
	@Override
	public ModelAndView getSupportAdd() throws Exception {
		
		this.before();
		ModelAndView mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030403"));
		

		// 1.生成大区下拉框
		List<NBPT_SP_REGION> regions = new ArrayList<NBPT_SP_REGION>();;
		
		// 2.生成保单类型下拉框
		List<NBPT_COMMON_DICTIONARY> dictionarys = new ArrayList<NBPT_COMMON_DICTIONARY>();
		try {
			regions = personManageDao.getRegions(null, this.getConnection());
			regions.add(0, new NBPT_SP_REGION());
			
			// 2.生成保单类型下拉框
			dictionarys = personManageDao.receiveDictionarys("POLICYTYPE", this.getConnection());

			
			mv.addObject("regions", regions);
			mv.addObject("policytypeSelects", dictionarys);
			
			return after(mv);
		} catch (SQLException e) {

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

			log.info("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

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

			log.info("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}

	}

	@Override
	public String receiveTerminalXzqx(String changePersonPid) throws Exception {

		try {
			
			this.before();
		
			// 管理区县
			List<NBPT_COMMON_XZQXHF> terminalResponsXzqx = personManageDao.getTerminalResponsXzqx(changePersonPid, this.getConnection());
			
			JSONArray ja = JSONArray.fromObject(terminalResponsXzqx);
			
			return this.after(ja.toString());
		}catch (SQLException e) {

			log.info("获取终端负责区县失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}
}














