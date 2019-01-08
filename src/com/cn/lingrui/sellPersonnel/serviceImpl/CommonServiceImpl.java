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

import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_DICTIONARY;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.db.dbpojos.NBPT_VIEW_DICTIONARY;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.PersonManageDao;
import com.cn.lingrui.sellPersonnel.db.dao.RegionManageDao;
import com.cn.lingrui.sellPersonnel.db.dao.SupportDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_PROCEDURE_TREE;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.JsonDataBack;
import com.cn.lingrui.sellPersonnel.pojos.TreePojo;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;
import com.cn.lingrui.sellPersonnel.pojos.createMap.ProvinceData;
import com.cn.lingrui.sellPersonnel.pojos.createMap.TotalData;
import com.cn.lingrui.sellPersonnel.pojos.xls.AllCurrentPerson;
import com.cn.lingrui.sellPersonnel.service.CommonService;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("commonService")
public class CommonServiceImpl extends SellPBaseService implements CommonService {

	private static Logger log = LogManager.getLogger();

	@Override
	protected String getFunNum() {
		return null;
	}

	@Resource(name = "personManageDao")
	private PersonManageDao personManageDao;

	@Resource(name = "supportDao")
	private SupportDao supportDao;

	@Resource(name = "regionManageDao")
	private RegionManageDao regionDao;

	@Override
	public ModelAndView receiveCurrentTerminals() throws Exception {

		this.before();

		// 初始化返回
		ModelAndView mv = null;

		try {

			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(),
					this.getConnection());

			mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030402"));

			// 如果为null,则登录人员为后勤
			if (null == loginPerson) {

				// 除信息专员外,获取所有人信息
				List<NBPT_VIEW_CURRENTPERSON> persons = supportDao.receiveAllPersons(this.getConnection());

				// 查询所有部门
				List<NBPT_VIEW_REGION> regions = supportDao.receiveRegion(null, null, this.getConnection());

				// 合计信息
				List<StatisticsTable> totalInfos = SupportServiceUtils.dealCurrentPerson_total(persons);

				// 分类统计信息
				Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyByType = CommonUtil.classify(persons,
						"NBPT_SP_PERSON_TYPE", NBPT_VIEW_CURRENTPERSON.class);

				// 后勤人员查询OTC统计信息处理
				List<StatisticsTable> OTCInfos = SupportServiceUtils.dealCurrentPerson_otc(classifyByType.get("2"),
						regions);

				mv.addObject("totalInfos", totalInfos);
				mv.addObject("OTCInfos", OTCInfos);

			} else {

				// 查询该登录人员的负责区域
				NBPT_VIEW_REGION responsRegion = personManageDao
						.receiveRegionByResper(loginPerson.getNBPT_SP_PERSON_PID(), "2", this.getConnection());

				if (null == responsRegion) {

					mv.addObject("message", "您没有被分配管理地区,请联系后勤人员或管理员");

					return this.after(mv);
				} else {

					// 查询所有下级人员信息
					List<NBPT_VIEW_CURRENTPERSON> persons = personManageDao.receiveTerminal(null, null,
							responsRegion.getNBPT_SP_REGION_UID(), null, this.getConnection());

					Map<String, List<NBPT_VIEW_CURRENTPERSON>> personClassifyed = CommonServiceUtils
							.dealPersonsByKey(persons, "NBPT_SP_PERSON_FLAG");

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

					// 驳回数据
					List<NBPT_VIEW_CURRENTPERSON> rejectPersonInfos = new ArrayList<>();
					rejectPersonInfos.addAll(CommonUtil.getListInMapByKey(personClassifyed, "4"));
					rejectPersonInfos.addAll(CommonUtil.getListInMapByKey(personClassifyed, "5"));
					mv.addObject("personRejectInfos", rejectPersonInfos);

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
			ModelAndView mv = null;

			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(),
					this.getConnection());

			List<NBPT_VIEW_CURRENTPERSON> persons = new ArrayList<NBPT_VIEW_CURRENTPERSON>();

			// 后勤人员获取
			if (null == loginPerson) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030407"));
				persons = personManageDao.receiveTerminal(null, null, null, null, this.getConnection());
			}

			// 如果是大区总
			else if ("21".equals(loginPerson.getNBPT_SP_PERSON_JOB())
					|| "26".equals(loginPerson.getNBPT_SP_PERSON_JOB())) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030505"));
				persons = personManageDao.receiveTerminal(loginPerson.getNBPT_SP_PERSON_REGION_UID(), null, null, null,
						this.getConnection());
			}

			// 如果是信息专员
			else if ("27".equals(loginPerson.getNBPT_SP_PERSON_JOB())) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030607"));
				// 处理信息专员管理大区
				String[] reginUids = loginPerson.getNBPT_SP_PERSON_DEPT_ID().split(",");

				for (String regionUid : reginUids) {

					persons.addAll(personManageDao.receiveTerminal(regionUid, null, null, null, this.getConnection()));
				}
			}

			List<NBPT_VIEW_CURRENTPERSON> personInfos = CommonUtil.getListInMapByKey(
					CommonUtil.classify(persons, "NBPT_SP_PERSON_FLAG", NBPT_VIEW_CURRENTPERSON.class), "2");
			mv.addObject("personInfos", personInfos);

			return this.after(mv);

		} catch (SQLException e) {

			this.closeException();
			log.error("查询当前所有人员详细信息出错");
			throw new Exception();
		}
	}

	/**
	 * 获取行政区县下拉框
	 */
	@Override
	public String receiveSelect(String parentId) throws Exception {

		try {
			this.before();

			JsonDataBack back = new JsonDataBack();
			// 获取省级划分
			List<NBPT_COMMON_XZQXHF> xzqxhfs = personManageDao.getXzqxhfs(parentId, this.getConnection());

			xzqxhfs.add(0, new NBPT_COMMON_XZQXHF());

			back.setData(JSONArray.fromObject(xzqxhfs).toString());
			return this.after(back.toJsonString());
		} catch (SQLException e) {

			this.closeException();
			log.error("获取省份下拉框失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}
	
	@Override
	public String receiveSelectPost(String parentId) throws Exception {

		try {
			this.before();

			JsonDataBack back = new JsonDataBack();
			
			// 获取省级划分
			List<NBPT_COMMON_XZQXHF> xzqxhfs = personManageDao.getXzqxhfs(parentId, this.getConnection());

			xzqxhfs.add(0, new NBPT_COMMON_XZQXHF());
			back.setData(JSONArray.fromObject(xzqxhfs).toString());
			return this.after(back.toJsonString());
		} catch (SQLException e) {

			this.closeException();
			log.error("获取省份下拉框失败" + CommonUtil.getTrace(e));
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
		} catch (SQLException e) {

			this.closeException();
			log.error("获取添加地总大区总页面失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public String receiveTerminalXzqx(String changePersonPid) throws Exception {

		try {

			this.before();
			return this.after(
					JSONArray.fromObject(personManageDao.getTerminalResponsXzqx(changePersonPid, this.getConnection()))
							.toString());
		} catch (SQLException e) {

			this.closeException();
			log.error("获取终端负责区县失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public String receiveTerminalPlace(String idNum) throws Exception {

		try {
			this.before();
			JsonDataBack back = new JsonDataBack();
			back.setData(JSONArray.fromObject(personManageDao.checkPlace(idNum.substring(0, 6), this.getConnection())).toString());
			return this.after(back.toJsonString());
		} catch (SQLException e) {
			JsonDataBack back = new JsonDataBack();
			back.setStateError();
			log.error("获取终端户籍失败" + CommonUtil.getTrace(e));
			return this.after(back.toJsonString());
		}
	}

	@Override
	public String receiveLoginId(String loginId) throws Exception {
		try {
			this.before();
			return this.after(
					JSONArray.fromObject(personManageDao.receiveLoginId(loginId, this.getConnection())).toString());
		} catch (SQLException e) {

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
			if ("2".equals(transferType)) {

				// 查地总
				persons = personManageDao.receivePerson("22", personName, "2", this.getConnection());
			}

			// 区县总调职相关
			else {

				// 查区县总
				persons = personManageDao.receivePerson(new String[] { "23", "24", "25" }, personName, "2",
						this.getConnection());
			}
			return this.after(JSONArray.fromObject(persons).toString());
		} catch (SQLException e) {

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
		} catch (SQLException e) {

			this.closeException();
			log.error("获取要调职的人员失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public String createMap() throws Exception {
		try {

			this.before();

			// 初始化查询结果
			List<NBPT_VIEW_CURRENTPERSON> persons = new ArrayList<>();

			persons = personManageDao.receiveTerminal(null, null, null, null, connection);

			// 初始化返回数据
			TotalData totalData = new TotalData();

			// 1.获取省份分类数据
			Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifiedResults = CommonUtil.classify(persons,
					"NBPT_SP_PERSON_PROVINCE_NAME", NBPT_VIEW_CURRENTPERSON.class);

			// 2.循环处理数据
			for (String province_name : classifiedResults.keySet()) {

				// 初始化各省份返回数据
				ProvinceData provinceData = new ProvinceData();

				// 各省份数据
				List<NBPT_VIEW_CURRENTPERSON> provincePersons = CommonUtil.getListInMapByKey(classifiedResults,
						province_name);

				// 个省份按状态分类数据
				Map<String, List<NBPT_VIEW_CURRENTPERSON>> provincePersons_classfied = CommonUtil
						.classify(provincePersons, "NBPT_SP_PERSON_FLAG", NBPT_VIEW_CURRENTPERSON.class);

				// 2.1 省份信息
				provinceData.setProvinceName(province_name);
				provinceData.setName(CommonServiceUtils.simplefiProvinceName(province_name));
				provinceData.setRegionName(provincePersons.get(0).getNBPT_SP_PERSON_REGION_NAME());
				provinceData.setRegionUid(provincePersons.get(0).getNBPT_SP_PERSON_REGION_UID());

				// 2.2 在职数据
				List<NBPT_VIEW_CURRENTPERSON> onJobPersons = CommonUtil.getListInMapByKey(provincePersons_classfied,
						"2");
				provinceData.setOnJobTotal(onJobPersons.size());
				provinceData.setValue(onJobPersons.size());
				// 2.3离职数据
				List<NBPT_VIEW_CURRENTPERSON> leavePersons = CommonUtil.getListInMapByKey(provincePersons_classfied,
						"3");
				provinceData.setLeaveTotal(leavePersons.size());

				totalData.getProvinceData().add(provinceData);
			}
			System.out.println(JSONObject.fromObject(totalData).toString());
			return this.after(JSONObject.fromObject(totalData).toString());

		} catch (SQLException e) {

			this.closeException();
			log.error("生成地图数据失败" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public String receiveDictionaryList(String params) throws Exception {
		try {

			this.before();
			String[] types = params.split(",");

			// 初始化查询结果
			List<NBPT_VIEW_DICTIONARY> dicList = new ArrayList<NBPT_VIEW_DICTIONARY>();

			dicList = personManageDao.receiveDictionarys(types, this.getConnection());

			Map<String, List<NBPT_VIEW_DICTIONARY>> classifyResult = CommonUtil.classify(dicList,
					"NBPT_COMMON_DICTIONARY_KEY_ID", NBPT_VIEW_DICTIONARY.class);

			for (String key : classifyResult.keySet()) {

				classifyResult.get(key).add(0, new NBPT_VIEW_DICTIONARY());
			}
			// return this.after(JSONArray.fromObject(dicList).toString());
			return this.after(JSONObject.fromObject(classifyResult).toString());
		} catch (Exception e) {

			this.closeException();
			log.error("获取" + CommonUtil.getTrace(e));
			throw new Exception();
		}

	}

	@Override
	public String receiveXzqxs(String parentId) throws Exception {

		try {
			this.before();
			JsonDataBack back = new JsonDataBack();
			back.setStateOk();

			List<NBPT_COMMON_XZQXHF> xzqxhfs = personManageDao.getXzqxhfs(parentId, this.getConnection());
			xzqxhfs.add(0, new NBPT_COMMON_XZQXHF());
			back.setData(JSONArray.fromObject(xzqxhfs).toString());
			return this.after(JSONObject.fromObject(back).toString());
		} catch (SQLException e) {

			log.error("AJAX获取行政区县列表出错" + CommonUtil.getTrace(e));
			JsonDataBack back = new JsonDataBack();
			back.setStateError();
			return this.after(JSONObject.fromObject(back).toString());
		}
	}

	@Override
	public String createAllCurrentTerminals(String fileName) throws Exception {
		try {

			this.before();

			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(),
					this.getConnection());

			List<NBPT_VIEW_CURRENTPERSON> persons = new ArrayList<NBPT_VIEW_CURRENTPERSON>();

			// 后勤人员获取
			if (null == loginPerson) {

				persons = personManageDao.receiveTerminal(null, null, null, null, this.getConnection());
			}

			// 如果是大区总
			else if ("21".equals(loginPerson.getNBPT_SP_PERSON_JOB())
					|| "26".equals(loginPerson.getNBPT_SP_PERSON_JOB())) {

				persons = personManageDao.receiveTerminal(loginPerson.getNBPT_SP_PERSON_REGION_UID(), null, null, null,
						this.getConnection());
			}

			// 如果是信息专员
			else if ("27".equals(loginPerson.getNBPT_SP_PERSON_JOB())) {

				// 处理信息专员管理大区
				String[] reginUids = loginPerson.getNBPT_SP_PERSON_DEPT_ID().split(",");

				for (String regionUid : reginUids) {

					persons.addAll(personManageDao.receiveTerminal(regionUid, null, null, null, this.getConnection()));
				}
			}

			// 只导出在职人员
			List<NBPT_VIEW_CURRENTPERSON> personInfos = CommonUtil.getListInMapByKey(
					CommonUtil.classify(persons, "NBPT_SP_PERSON_FLAG", NBPT_VIEW_CURRENTPERSON.class), "2");

			AllCurrentPerson<NBPT_VIEW_CURRENTPERSON> allPersonExcel = new AllCurrentPerson<>(fileName, personInfos);
			allPersonExcel.toExcel();
			return this.after("");

		} catch (SQLException e) {

			this.closeException();
			log.error("创建所有人员详细信息文件出错");
			throw new Exception();
		}

	}

	@Override
	public String createTreeRegion(String level) {

		try {
			this.before();
			
			// 初始化返回结果
			JsonDataBack back = new JsonDataBack();
			// 最外层
			List<TreePojo> wrapTrees = new ArrayList<>();
			// 查询所有分类
			List<NBPT_COMMON_DICTIONARY> dics = personManageDao.receiveDictionarys("SAILTYPE", this.getConnection());

			for (NBPT_COMMON_DICTIONARY dic : dics) {
				TreePojo treeNode = new TreePojo();
				treeNode.setName(dic.getNBPT_COMMON_DICTIONARY_KEY_NAME());
				treeNode.setOpen("false");
				treeNode.setIsParent("true");
				treeNode.setCurrentId(dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE());
				treeNode.setType("type");
				treeNode.setTypeValue(dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE());
				wrapTrees.add(treeNode);
			}
			
			// 查询所有省份
			List<NBPT_COMMON_XZQXHF> xzqxs = personManageDao.receiveCommonXzqxs(null,"1", this.getConnection());
			// 每个分类有两个分类,一个大区一个地区
			for (TreePojo wrapTree : wrapTrees) {

				TreePojo treeNodeRegion = new TreePojo();
				treeNodeRegion.setIsParent("false");
				treeNodeRegion.setName("大区");
				treeNodeRegion.setOpen("true");
				treeNodeRegion.setCurrentId("");
				treeNodeRegion.setType("region");
				treeNodeRegion.setTypeValue(wrapTree.getTypeValue());
				treeNodeRegion.setClickAble(true);
				wrapTree.getChildren().add(treeNodeRegion);
				
				TreePojo treeNodeArea = new TreePojo();
				treeNodeArea.setIsParent("true");
				treeNodeArea.setName("地区");
				treeNodeArea.setOpen("false");
				treeNodeArea.setCurrentId("");
				treeNodeArea.setType("");
				treeNodeArea.setTypeValue(wrapTree.getTypeValue());
				wrapTree.getChildren().add(treeNodeArea);
				
				for(NBPT_COMMON_XZQXHF province : xzqxs) {
					TreePojo treeNodeProvince = new TreePojo();
					treeNodeProvince.setIsParent("false");
					treeNodeProvince.setName(province.getNBPT_COMMON_XZQXHF_NAME());
					treeNodeProvince.setOpen("true");
					treeNodeProvince.setCurrentId(province.getNBPT_COMMON_XZQXHF_ID());
					treeNodeProvince.setType("province");
					treeNodeProvince.setTypeValue(wrapTree.getTypeValue());
					treeNodeProvince.setClickAble(true);
					treeNodeArea.getChildren().add(treeNodeProvince);
				}
			}
			
			back.setData(JSONArray.fromObject(wrapTrees).toString());
			return back.toJsonString();
			
//			
//			// 查询所有大区
//			List<NBPT_VIEW_REGION> regions = personManageDao.receiveRegion(null, "1", null, this.getConnection());
//			
//			// 以有效划分
//			Map<String, List<NBPT_VIEW_REGION>> regionClassifys_byFlag = CommonUtil.classify(regions, "NBPT_SP_REGION_FLAG",
//					NBPT_VIEW_REGION.class);
//			
//			// 以type划分
//			Map<String, List<NBPT_VIEW_REGION>> regionClassifys = CommonUtil.classify(CommonUtil.getListInMapByKey(regionClassifys_byFlag, "1"), "NBPT_SP_REGION_TYPE",
//					NBPT_VIEW_REGION.class);
//			
//
//			for (TreePojo wrapTree : wrapTrees) {
//				
//				
//
//				for (NBPT_VIEW_REGION region : CommonUtil.getListInMapByKey(regionClassifys, wrapTree.getCurrentId())) {
//
//					TreePojo treeNode = new TreePojo();
//					treeNode.setName(region.getNBPT_SP_REGION_NAME());
//					treeNode.setOpen("false");
//					treeNode.setIsParent("true");
//					treeNode.setCurrentId(region.getNBPT_SP_REGION_UID());
//					treeNode.setType("region");
//					treeNode.setTypeValue(region.getNBPT_SP_REGION_TYPE());
//					wrapTree.getChildren().add(treeNode);
//				}
//			}
//
//			if ("1".equals(level)) {
//				back.setData(JSONArray.fromObject(wrapTrees).toString());
//				return back.toJsonString();
//			}
//
//			// 查询所有省份
//			List<NBPT_VIEW_XZQX> xzqxs = personManageDao.receiveXzqxs(null, "1", "11", this.getConnection());
//			Map<String, List<NBPT_VIEW_XZQX>> currentXzqxs = CommonUtil.classify(xzqxs, "NBPT_SP_REGION_UID",
//					NBPT_VIEW_XZQX.class);
//
//			for (TreePojo wrapTree : wrapTrees) {
//
//				for (TreePojo regionPojo : wrapTree.getChildren()) {
//					for (NBPT_VIEW_XZQX xzqx : CommonUtil.getListInMapByKey(currentXzqxs, regionPojo.getCurrentId())) {
//						TreePojo treeNode = new TreePojo();
//						treeNode.setName(xzqx.getNBPT_COMMON_XZQXHF_NAME());
//						treeNode.setOpen("false");
//						treeNode.setIsParent("true");
//						treeNode.setCurrentId(xzqx.getNBPT_COMMON_XZQXHF_ID());
//						treeNode.setType("province");
//						treeNode.setTypeValue(xzqx.getNBPT_SP_REGION_TYPE());
//						regionPojo.getChildren().add(treeNode);
//					}
//				}
//			}
//
//			if ("2".equals(level)) {
//				back.setData(JSONArray.fromObject(wrapTrees).toString());
//				return back.toJsonString();
//			}
//
//			// 查询所有地区
//			List<NBPT_VIEW_REGION> areas = personManageDao.receiveRegion(null, "2", null, this.getConnection());
//			// 以有效划分
//			Map<String, List<NBPT_VIEW_REGION>> currentAreas_byFlag = CommonUtil.classify(areas, "NBPT_SP_REGION_FLAG",
//					NBPT_VIEW_REGION.class);
//			Map<String, List<NBPT_VIEW_REGION>> currentAreas = CommonUtil.classify(CommonUtil.getListInMapByKey(currentAreas_byFlag, "1"), "NBPT_SP_REGION_PROVINCE_ID",
//					NBPT_VIEW_REGION.class);
//
//			for (TreePojo wrapTree : wrapTrees) {
//				// 循环大区
//				for (TreePojo regionPojo : wrapTree.getChildren()) {
//					// 循环省份
//					for (TreePojo provincePojo : regionPojo.getChildren()) {
//
//						for (NBPT_VIEW_REGION area : CommonUtil.getListInMapByKey(currentAreas,
//								provincePojo.getCurrentId())) {
//
//							if(regionPojo.getTypeValue().equals(area.getNBPT_SP_REGION_TYPE())) {
//								TreePojo treeNode = new TreePojo();
//
//								treeNode.setName(area.getNBPT_SP_REGION_NAME());
//								treeNode.setOpen("false");
//								treeNode.setIsParent("false");
//								treeNode.setCurrentId(area.getNBPT_SP_REGION_UID());
//								treeNode.setType("area");
//								treeNode.setTypeValue(area.getNBPT_SP_REGION_TYPE());
//								provincePojo.getChildren().add(treeNode);
//							}
//						}
//					}
//				}
//			}
//
//			if ("3".equals(level)) {
//				back.setData(JSONArray.fromObject(wrapTrees).toString());
//				return back.toJsonString();
//			}
//
//			back.setData(JSONArray.fromObject(wrapTrees).toString());
//			return back.toJsonString();
		} catch (Exception e) {

			JsonDataBack back = new JsonDataBack();
			back.setStateError("获取部门数据出错,请联系后台管理员");
			log.info("获取部门树出错");
			return this.after("");
		}
	}

	@Override
	public String createRegionTree(String pid, String rank) {

		try {

			this.before();
			List<NBPT_PROCEDURE_TREE> treeList = regionDao.receiveRegionTree(pid, this.getConnection());
			
			rank = (null == rank || "".equals(rank)) ? "4" : rank;
			TreePojo wrapPojo = addChildren(treeList.get(treeList.size() - 1)
					, CommonUtil.classify(treeList, "ZTREE_PID", NBPT_PROCEDURE_TREE.class), 1, Integer.valueOf(rank));
			
			JsonDataBack back = new JsonDataBack();
			back.setData(JSONObject.fromObject(wrapPojo).toString());
			return this.after(back.toJsonString());
		} catch (Exception e) {

			log.error("获取部门选取树错误");
			return null;
		}
	}
	
	private TreePojo addChildren(NBPT_PROCEDURE_TREE p, Map<String, List<NBPT_PROCEDURE_TREE>> classfiedByLevel, int currentRank, int rank) {
		
		TreePojo wrapPojo = new TreePojo();
		wrapPojo.setCurrentId(p.getZTREE_ID());
		wrapPojo.setName(p.getZTREE_NAME());
		wrapPojo.setType(p.getZTREE_TYPE());
		wrapPojo.setTypeValue(p.getZTREE_SAILTYPE());
		wrapPojo.setClickAble(true);

		if(currentRank > rank) wrapPojo.setIsParent("false");
		if(0 != CommonUtil.getListInMapByKey(classfiedByLevel, p.getZTREE_ID()).size()) {

			if(currentRank > rank) return wrapPojo;
			for(NBPT_PROCEDURE_TREE childP : CommonUtil.getListInMapByKey(classfiedByLevel, p.getZTREE_ID())) {

				wrapPojo.getChildren().add(addChildren(childP, classfiedByLevel, currentRank + 1, rank));
			}
		} else {
			wrapPojo.setIsParent("false");
		}
		return wrapPojo;
	}
	
	@Override
	public String createPersonTree() {

		try {

			this.before();
			List<NBPT_PROCEDURE_TREE> treeList = regionDao.receivePersonTree(this.getConnection());
			
			TreePojo wrapPojo = addPersonTreeChildren(treeList.get(treeList.size() - 1)
					, CommonUtil.classify(treeList, "ZTREE_PID", NBPT_PROCEDURE_TREE.class), 1);
			
			JsonDataBack back = new JsonDataBack();
			back.setData(JSONObject.fromObject(wrapPojo).toString());
			return this.after(back.toJsonString());
		} catch (Exception e) {

			log.error("获取部门选取树错误");
			return null;
		}
	}
	
	private TreePojo addPersonTreeChildren(NBPT_PROCEDURE_TREE p, Map<String, List<NBPT_PROCEDURE_TREE>> classfiedByLevel, int currentRank) {
		
		TreePojo wrapPojo = new TreePojo();
		wrapPojo.setCurrentId(p.getZTREE_ID());
		wrapPojo.setName(p.getZTREE_NAME());
		wrapPojo.setType(p.getZTREE_TYPE());
		wrapPojo.setTypeValue(p.getZTREE_SAILTYPE());
		wrapPojo.setClickAble(true);

		if(currentRank > 3) wrapPojo.setIsParent("false");
		if(0 != CommonUtil.getListInMapByKey(classfiedByLevel, p.getZTREE_ID()).size()) {

			if(currentRank > 3) return wrapPojo;
			for(NBPT_PROCEDURE_TREE childP : CommonUtil.getListInMapByKey(classfiedByLevel, p.getZTREE_ID())) {

				wrapPojo.getChildren().add(addPersonTreeChildren(childP, classfiedByLevel, currentRank + 1));
			}
		}
		
		return wrapPojo;
	}

	@Override
	public String receiveRegionSelect(String parentUid) {

		try {
			this.before();
			JsonDataBack back = new JsonDataBack();
			List<NBPT_VIEW_REGION> regions = personManageDao.receiveRegions(parentUid, null, "2", null, this.getConnection());
			back.setData(JSONArray.fromObject(regions).toString());
			return this.after(back.toJsonString());
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
}
































