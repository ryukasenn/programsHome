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
import com.cn.lingrui.common.db.dao.UserManageDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_DICTIONARY;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_USER;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_U_R;
import com.cn.lingrui.common.pojos.CreateForm;
import com.cn.lingrui.common.pojos.FormItem;
import com.cn.lingrui.common.pojos.Option;
import com.cn.lingrui.common.pojos.Radio;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.PersonManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.JsonDataBack;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;
import com.cn.lingrui.sellPersonnel.pojos.person.AddPersonIn;
import com.cn.lingrui.sellPersonnel.pojos.person.ChangePersonIn;
import com.cn.lingrui.sellPersonnel.pojos.person.PostAddPersonIn;
import com.cn.lingrui.sellPersonnel.pojos.person.PostTransferPersonIn;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;
import com.cn.lingrui.sellPersonnel.service.PersonManageService;

@Service("personManageService")
public class PersonManageServiceImpl extends SellPBaseService implements PersonManageService {

	private static Logger log = LogManager.getLogger();

	@Resource(name = "personManageDao")
	private PersonManageDao personManageDao;

	@Resource(name="userManageDao")
	private UserManageDao userManageDao;
	
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
			mv.addObject("thisInfact", CommonUtil.getListInMapByKey(personsClassifyed, "2").size());
			
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
			unCheckedPersonInfos.addAll(CommonUtil.getListInMapByKey(personsClassifyed, "4"));
			unCheckedPersonInfos.addAll(CommonUtil.getListInMapByKey(personsClassifyed, "5"));
			
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

	@Override
	public String getAddPerson(AddPersonIn in) {

		// TODO,目前是只贴合OTC的人员添加,其他分类因为没有具体方针,暂时不管20180821
		try {
			this.before();
			// 验证登录人身份
			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
			if(null != loginPerson) { // 如果是后勤登录
				in.setJob("area");
				in.setTypeValue(loginPerson.getNBPT_SP_PERSON_TYPE());
			}
			
			
			// 初始化返回数据
			JsonDataBack back = new JsonDataBack();
			// 1.获取分类
			List<NBPT_COMMON_DICTIONARY> dics = personManageDao.receiveDictionarys("SAILTYPE", this.getConnection());

			// 公共item
			FormItem degrees = new FormItem("newPersonDegree", "学历", "select", false, "");
			degrees.getOptions().add(new Option("小学", "小学"));
			degrees.getOptions().add(new Option("初中", "初中"));
			degrees.getOptions().add(new Option("高中", "高中"));
			degrees.getOptions().add(new Option("中专", "中专"));
			degrees.getOptions().add(new Option("大专", "大专"));
			degrees.getOptions().add(new Option("本科", "本科"));
			degrees.getOptions().add(new Option("研究生", "研究生"));

			FormItem maleRadios = new FormItem("newPersonMale", "性别", "radio", false, "", "");
			maleRadios.getRadios().add(new Radio("1", "男"));
			maleRadios.getRadios().add(new Radio("0", "女"));
			
			switch (in.getJob()) {
			case "type": // 如果添加大区总
				
				CreateForm regionResper = new CreateForm("addNewRegionResper", "添加大区总");
				for(NBPT_COMMON_DICTIONARY dic : dics) {
					if(dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE().equals(in.getTypeValue())) {
						regionResper.getColumns().add(new FormItem("newPersonType", "分类", "text", true, dic.getNBPT_COMMON_DICTIONARY_KEY_NAME() + "大区总"));
						regionResper.getColumns().add(new FormItem("newPersonTypeValue", "", "hidden", false, dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE()));
						regionResper.getColumns().add(new FormItem("newPersonRegionUid", "", "hidden", false, "regionUnset"));
						break;
					}
				}
				regionResper.getColumns().add(new FormItem("newPersonName", "新大区总名称", "text", false, "", "必须项", true));
				regionResper.getColumns().add(new FormItem("newPersonLoginId", "新大区总名称拼音", "text", false, "", "必须项,拼写错误,将导致无法登录", true));
				regionResper.getColumns().add(new FormItem("newPersonPassword", "新大区总登录密码", "text", false, "", "非必须项,默认为lingrui123"));
				if("2".equals(in.getTypeValue())) {
					FormItem jonExtrRadios = new FormItem("newPersonJob", "是否兼任地总", "radio");
					jonExtrRadios.getRadios().add(new Radio("26", "是"));
					jonExtrRadios.getRadios().add(new Radio("21", "否", true));
					regionResper.getColumns().add(jonExtrRadios);
				}
				regionResper.getColumns().add(new FormItem("newPersonIdNum", "身份证号码", "idCard", false, true));
				regionResper.getColumns().add(new FormItem("newPersonPlace", "籍贯", "text", false, "", "填写身份证后若无自动填充,请手动填写", true));
				
				regionResper.getColumns().add(maleRadios);

				regionResper.getColumns().add(new FormItem("newPersonMob1", "个人手机", "text", false, "", "必填项", true));
				regionResper.getColumns().add(new FormItem("newPersonMob2", "紧急联系人", "text"));
				regionResper.getColumns().add(new FormItem("newPersonEntryDate", "入职时间", "date", true, true));

				regionResper.getColumns().add(degrees);
				
				regionResper.getColumns().add(new FormItem("newPersonQq", "QQ", "text"));
				regionResper.getColumns().add(new FormItem("newPersonWechat", "微信", "text"));
				regionResper.getColumns().add(new FormItem("newPersonEmail", "邮箱", "text"));
				regionResper.getColumns().add(new FormItem("newPersonSchool", "毕业院校", "text"));
				regionResper.getColumns().add(new FormItem("newPersonProfess", "专业", "text"));
				regionResper.getColumns().add(new FormItem("newPersonTitle", "职称", "text"));
				regionResper.getColumns().add(new FormItem("newPersonNote", "备注", "textarea"));

				back.setData(regionResper.toJsonString());
				return this.after(back.toJsonString());
			case "region": // 如果添加地总
								
				CreateForm areaResper = new CreateForm("addNewAreaResper", "添加地总");
				for(NBPT_COMMON_DICTIONARY dic : dics) {
					if(dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE().equals(in.getTypeValue())) {
						areaResper.getColumns().add(new FormItem("newPersonType", "分类", "text", true, dic.getNBPT_COMMON_DICTIONARY_KEY_NAME() 
								+ "  地总"));
						areaResper.getColumns().add(new FormItem("newPersonTypeValue", "", "hidden", false, dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE()));
						areaResper.getColumns().add(new FormItem("newPersonJob", "", "hidden", false, in.getTypeValue()+"2"));
						areaResper.getColumns().add(new FormItem("newPersonRegionUid", "", "hidden", false, "regionUnset"));
						break;
					}
				}
				
				areaResper.getColumns().add(new FormItem("newPersonName", "新地总名称", "text", false, true));
				areaResper.getColumns().add(new FormItem("newPersonLoginId", "新地总名称拼音", "text", false, "", "必须项,拼写错误,将导致无法登录", true));
				areaResper.getColumns().add(new FormItem("newPersonPassword", "新地总登录密码", "text", false, "", "非必须项,默认为lingrui123"));
				areaResper.getColumns().add(new FormItem("newPersonIdNum", "身份证号码", "idCard", false, true));
				areaResper.getColumns().add(new FormItem("newPersonPlace", "籍贯", "text", false, "", "填写身份证后若无自动填充,请手动填写", true));

				areaResper.getColumns().add(maleRadios);

				areaResper.getColumns().add(new FormItem("newPersonMob1", "个人手机", "text", false, "", "必填项", true));
				areaResper.getColumns().add(new FormItem("newPersonMob2", "紧急联系人", "text", false, ""));
				areaResper.getColumns().add(new FormItem("newPersonEntryDate", "入职时间", "date", true, true));
				
				areaResper.getColumns().add(degrees);

				areaResper.getColumns().add(new FormItem("newPersonQq", "QQ", "text"));
				areaResper.getColumns().add(new FormItem("newPersonWechat", "微信", "text"));
				areaResper.getColumns().add(new FormItem("newPersonEmail", "邮箱", "text"));
				areaResper.getColumns().add(new FormItem("newPersonSchool", "毕业院校", "text"));
				areaResper.getColumns().add(new FormItem("newPersonProfess", "专业", "text"));
				areaResper.getColumns().add(new FormItem("newPersonTitle", "职称", "text"));
				areaResper.getColumns().add(new FormItem("newPersonNote", "备注", "textarea"));
				
				back.setData(areaResper.toJsonString());
				return this.after(back.toJsonString());
			case "area": // 如果添加终端
				if(null == loginPerson) { // 如果是后勤登录
					back.setStateError("还是交给地总加人吧");
					return this.after(back.toJsonString());
				} else {
					
					if(null == loginPerson.getNBPT_SP_PERSON_AREA_UID() || "".equals(loginPerson.getNBPT_SP_PERSON_AREA_UID())) {
						back.setStateError("没有为你分配管理地区");
						return this.after(back.toJsonString());
					}
					in.setTypeValue(loginPerson.getNBPT_SP_PERSON_TYPE());
					in.setId(loginPerson.getNBPT_SP_PERSON_AREA_UID());
					
					CreateForm terminal = new CreateForm("addNewTerminal", "添加下级");
					for(NBPT_COMMON_DICTIONARY dic : dics) {
						if(dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE().equals(in.getTypeValue())) {
							terminal.getColumns().add(new FormItem("newPersonType", "分类", "text", true
									, dic.getNBPT_COMMON_DICTIONARY_KEY_NAME() + " " + loginPerson.getNBPT_SP_PERSON_AREA_NAME() + " 销售人员"));
							terminal.getColumns().add(new FormItem("newPersonTypeValue", "", "hidden", false, dic.getNBPT_COMMON_DICTIONARY_KEY_VALUE()));
							terminal.getColumns().add(new FormItem("newPersonRegionUid", "", "hidden", false, loginPerson.getNBPT_SP_PERSON_AREA_UID()));
							break;
						}
					}
					terminal.getColumns().add(new FormItem("newPersonName", "人员名称", "text", false, true));
					
					FormItem terminalJobRadios = new FormItem("newPersonJob", "职务", "radio", false, true);
					if("2".equals(in.getTypeValue())) {
						terminalJobRadios.getRadios().add(new Radio("23", "区县总", true));
						terminalJobRadios.getRadios().add(new Radio("24", "预备区县总"));
						terminalJobRadios.getRadios().add(new Radio("25", "推广经理"));
					}
					terminal.getColumns().add(terminalJobRadios);
					
					terminal.getColumns().add(new FormItem("newPersonIdNum", "身份证号码", "idCard", false, true));
					terminal.getColumns().add(new FormItem("newPersonPlace", "籍贯", "text", false, "", "填写身份证后若无自动填充,请手动填写", true));
					
					terminal.getColumns().add(maleRadios);

					terminal.getColumns().add(new FormItem("newPersonMob1", "个人手机", "text", false, "", "必填项", true));
					terminal.getColumns().add(new FormItem("newPersonMob2", "紧急联系人", "text", false, ""));
					terminal.getColumns().add(new FormItem("newPersonEntryDate", "入职时间", "date", true, true));
					
					FormItem policyTypeSelects = new FormItem("newPersonPolicyType", "保单编号", "select", false, "");
					List<NBPT_COMMON_DICTIONARY> policyTypes = personManageDao.receiveDictionarys("POLICYTYPE", this.getConnection());
					
					// 只选取在职人员
					for(NBPT_COMMON_DICTIONARY policyType : policyTypes) {
							Option newOption = new Option(policyType.getNBPT_COMMON_DICTIONARY_KEY_VALUE(), policyType.getNBPT_COMMON_DICTIONARY_KEY_NAME());
							policyTypeSelects.getOptions().add(newOption);
					}
					terminal.getColumns().add(policyTypeSelects);
					terminal.getColumns().add(new FormItem("newPersonPolicyNo", "", "text", false, "", "此处填写保单编号,注意选择保单类型"));
					terminal.getColumns().add(new FormItem("newPersonPolicyDate1", "保单起始时间", "date", true, false));
					terminal.getColumns().add(new FormItem("newPersonPolicyDate2", "保单结束时间", "date", true, false));
					
					terminal.getColumns().add(degrees);

					terminal.getColumns().add(new FormItem("newPersonQq", "QQ", "text"));
					terminal.getColumns().add(new FormItem("newPersonWechat", "微信", "text"));
					terminal.getColumns().add(new FormItem("newPersonEmail", "邮箱", "text"));
					terminal.getColumns().add(new FormItem("newPersonSchool", "毕业院校", "text"));
					terminal.getColumns().add(new FormItem("newPersonProfess", "专业", "text"));
					terminal.getColumns().add(new FormItem("newPersonTitle", "职称", "text"));
					terminal.getColumns().add(new FormItem("newPersonNote", "备注", "textarea"));
					
					back.setData(terminal.toJsonString());
					return this.after(back.toJsonString());
				}
			default:
				throw new Exception();
			}
		} catch (Exception e) {
			JsonDataBack back = new JsonDataBack();
			back.setStateError("出现错误,请联系后台管理员");
			return this.after(back.toJsonString());
		}
	}

	@Override
	public String postAddPerson(PostAddPersonIn in) {
		try {
			this.before();

			// 初始化返回数据
			JsonDataBack back = new JsonDataBack();
			// 验证登录人身份
			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(), this.getConnection());

			NBPT_SP_PERSON insertPerson = new NBPT_SP_PERSON();
			
			// 查询当前ID
			String maxId = personManageDao.receiveMaxId(this.getConnection(), "NBPT_SP_PERSON");
			insertPerson.setNBPT_SP_PERSON_ID(maxId); // 新纪录ID
			insertPerson.setNBPT_SP_PERSON_AREANO("");
			insertPerson.setNBPT_SP_PERSON_BIRS(CommonUtil.birthDay_from_idCard(in.getNewPersonIdNum()));
			insertPerson.setNBPT_SP_PERSON_CHAT(in.getNewPersonWechat());
			insertPerson.setNBPT_SP_PERSON_DEGREE(in.getNewPersonDegree());
			insertPerson.setNBPT_SP_PERSON_DEPT_ID("");
			insertPerson.setNBPT_SP_PERSON_ENTERINGTIME(CommonUtil.getYYYYMMDD());
			insertPerson.setNBPT_SP_PERSON_ENTRYDATA(CommonUtil.formateTiemToBasic(in.getNewPersonEntryDate()));
			
			insertPerson.setNBPT_SP_PERSON_IDNUM(in.getNewPersonIdNum());
			insertPerson.setNBPT_SP_PERSON_JOB(in.getNewPersonJob());
//			insertPerson.setNBPT_SP_PERSON_LEAVE_APPLY_DATA("");
//			insertPerson.setNBPT_SP_PERSON_LEAVE_REAL_DATA("");
//			insertPerson.setNBPT_SP_PERSON_LEAVEDATA("");
			insertPerson.setNBPT_SP_PERSON_LOGINID(in.getNewPersonLoginId());
			insertPerson.setNBPT_SP_PERSON_MAIL(in.getNewPersonEmail());
			insertPerson.setNBPT_SP_PERSON_MALE(in.getNewPersonMale());
			insertPerson.setNBPT_SP_PERSON_MOB1(in.getNewPersonMob1());
			insertPerson.setNBPT_SP_PERSON_MOB2(in.getNewPersonMob2());
			insertPerson.setNBPT_SP_PERSON_NAME(in.getNewPersonName());
			insertPerson.setNBPT_SP_PERSON_NOTE(in.getNewPersonNote());
			insertPerson.setNBPT_SP_PERSON_PID(CommonUtil.getUUID_32());
			insertPerson.setNBPT_SP_PERSON_PLACE(in.getNewPersonPlace());
			insertPerson.setNBPT_SP_PERSON_POLICY_DATA1(CommonUtil.formateTiemToBasic(in.getNewPersonPolicyDate1()));
			insertPerson.setNBPT_SP_PERSON_POLICY_DATA2(CommonUtil.formateTiemToBasic(in.getNewPersonPolicyDate2()));
			insertPerson.setNBPT_SP_PERSON_POLICYNO(in.getNewPersonPolicyNo());
			insertPerson.setNBPT_SP_PERSON_PROFESS(in.getNewPersonProfess());
			insertPerson.setNBPT_SP_PERSON_QQ(in.getNewPersonQq());
			insertPerson.setNBPT_SP_PERSON_SCHOOL(in.getNewPersonSchool());
			insertPerson.setNBPT_SP_PERSON_TITLE(in.getNewPersonTitle());
			insertPerson.setNBPT_SP_PERSON_TYPE(in.getNewPersonTypeValue());
			if(null != loginPerson) { // 如果不是后勤
				insertPerson.setNBPT_SP_PERSON_FLAG("1"); // 不是后勤,进入录入审核
				insertPerson.setNBPT_SP_PERSON_LEVEL("3"); // 不是后勤为3级人员
				// 记录插入行为
				personManageDao.addRecord(insertPerson.getNBPT_SP_PERSON_PID(), this.getLoginId(), "1", this.getConnection());

				back.setStateOk("添加人员成功,进入后台审核中");
			} else {
				insertPerson.setNBPT_SP_PERSON_FLAG("2"); // 后勤,直接在职
				// 记录插入行为
				personManageDao.addRecord(insertPerson.getNBPT_SP_PERSON_PID(), this.getLoginId(), "8", this.getConnection());
				// 创建登录账号
				NBPT_RSFZ_USER user = new NBPT_RSFZ_USER();
				user.setNBPT_RSFZ_USER_ID(insertPerson.getNBPT_SP_PERSON_LOGINID());
				user.setNBPT_RSFZ_USER_NAME(insertPerson.getNBPT_SP_PERSON_NAME());
				user.setNBPT_RSFZ_USER_PHONE("");
				user.setNBPT_RSFZ_USER_BZ("");
				user.setNBPT_RSFZ_USER_EMALL(insertPerson.getNBPT_SP_PERSON_MAIL());
				user.setNBPT_RSFZ_USER_PASSWORD("".equals(in.getNewPersonPassword()) ? "lingrui123" : in.getNewPersonPassword());
				user.setNBPT_RSFZ_USER_FLAG("1");
				userManageDao.postAddUser(this.getConnection(), user);

				// 插入权限信息
				NBPT_RSFZ_U_R rsfz_U_R = new NBPT_RSFZ_U_R();

				String role = "";
				if("22".equals(insertPerson.getNBPT_SP_PERSON_JOB())) {
					insertPerson.setNBPT_SP_PERSON_LEVEL("2"); // 添加地总,2级人员
					role = "600003";
				} else if("21".equals(insertPerson.getNBPT_SP_PERSON_JOB())){
					insertPerson.setNBPT_SP_PERSON_LEVEL("1"); // 添加大区总,1级人员
					role = "600006";
				} else if("26".equals(insertPerson.getNBPT_SP_PERSON_JOB())){
					insertPerson.setNBPT_SP_PERSON_LEVEL("1"); // 添加混合大区总,1级人员
					role = "600008";
				}
				rsfz_U_R.setNBPT_RSFZ_U_R_RID(role);
				rsfz_U_R.setNBPT_RSFZ_U_R_UID(insertPerson.getNBPT_SP_PERSON_LOGINID());
				rsfz_U_R.setNBPT_RSFZ_U_R_UID(insertPerson.getNBPT_SP_PERSON_LOGINID());
				userManageDao.postAddU_R(this.getConnection(), rsfz_U_R);
				back.setStateOk("添加人员成功");
			}
			// 添加部门绑定
			NBPT_SP_PERSON_REGION insertPersonRegion = new NBPT_SP_PERSON_REGION();
			
			if("21".equals(in.getNewPersonJob()) || "26".equals(in.getNewPersonJob())) {
				insertPersonRegion.setNBPT_SP_PERSON_REGION_RID("");
			} else {
				insertPersonRegion.setNBPT_SP_PERSON_REGION_RID(in.getNewPersonRegionUid());
			}
			insertPersonRegion.setNBPT_SP_PERSON_REGION_UID(CommonUtil.getUUID_32());
			insertPersonRegion.setNBPT_SP_PERSON_REGION_PID(insertPerson.getNBPT_SP_PERSON_PID());
			insertPersonRegion.setNBPT_SP_PERSON_REGION_TYPE("2");
			// 插入部门关系
			personManageDao.addPersonRegion(insertPersonRegion, this.getConnection());
			// 执行插入新纪录
			personManageDao.addTerminal(insertPerson, this.getConnection());
			
			return this.after(back.toJsonString());
		} catch (Exception e) {
			JsonDataBack back = new JsonDataBack();
			back.setStateError("出现错误,请联系后台管理员");
			return this.after(back.toJsonString());
		}
	}

	@Override
	public String getChangePerson(ChangePersonIn in) {

		try {
			this.before();
			JsonDataBack back = new JsonDataBack();
			
			// 获取原信息
			NBPT_VIEW_CURRENTPERSON oldPerson = personManageDao.receivePerson(in.getPersonPid(), this.getConnection());
			// 获取登录人员信息
			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
			// 返回修改页面
			if("21".equals(oldPerson.getNBPT_SP_PERSON_JOB()) || "26".equals(oldPerson.getNBPT_SP_PERSON_JOB())) {
				
				// 修改大区总混合地总信息
				CreateForm regionResper = new CreateForm("addNewRegionResper", "修改大区总");

				regionResper.getColumns().add(new FormItem("newPersonType", "分类", "text", true, oldPerson.getNBPT_SP_PERSON_TYPENAME() + "大区总"));
				regionResper.getColumns().add(new FormItem("newPersonTypeValue", "", "hidden", false, oldPerson.getNBPT_SP_PERSON_TYPE()));
				regionResper.getColumns().add(new FormItem("newPersonPid", "", "hidden", false, oldPerson.getNBPT_SP_PERSON_PID()));
				
				regionResper.getColumns().add(new FormItem("newPersonName", "新大区总名称", "text", false, oldPerson.getNBPT_SP_PERSON_NAME(), "必须项", true));
				regionResper.getColumns().add(new FormItem("newPersonLoginId", "新大区总名称拼音", "text", false, oldPerson.getNBPT_SP_PERSON_LOGINID(), "必须项,拼写错误,将导致无法登录", true));
				
				FormItem jonExtrRadios = new FormItem("newPersonJob", "是否兼任地总", "radio");
				jonExtrRadios.getRadios().add(new Radio("26", "是", ("26".equals(oldPerson.getNBPT_SP_PERSON_JOB()) ? true : false)));
				jonExtrRadios.getRadios().add(new Radio("21", "否", ("21".equals(oldPerson.getNBPT_SP_PERSON_JOB()) ? true : false)));
				regionResper.getColumns().add(jonExtrRadios);
				
				regionResper.getColumns().add(new FormItem("newPersonIdNum", "身份证号码", "text", false, oldPerson.getNBPT_SP_PERSON_IDNUM()));
				regionResper.getColumns().add(new FormItem("newPersonPlace", "籍贯", "text", false, oldPerson.getNBPT_SP_PERSON_PLACE(), "填写身份证后若无自动填充,请手动填写", true));

				// 公共item
				FormItem regionDegrees = new FormItem("newPersonDegree", "学历", "select", false, "");
				oldPerson.getNBPT_SP_PERSON_DEGREE();
				regionDegrees.getOptions().add(new Option("小学", "小学", ("小学".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				regionDegrees.getOptions().add(new Option("初中", "初中", ("初中".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				regionDegrees.getOptions().add(new Option("高中", "高中", ("高中".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				regionDegrees.getOptions().add(new Option("中专", "中专", ("中专".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				regionDegrees.getOptions().add(new Option("大专", "大专", ("大专".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				regionDegrees.getOptions().add(new Option("本科", "本科", ("本科".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				regionDegrees.getOptions().add(new Option("研究生", "研究生", ("研究生".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				regionResper.getColumns().add(regionDegrees);
				
				FormItem maleRadios = new FormItem("newPersonMale", "性别", "radio", false, "", "");
				maleRadios.getRadios().add(new Radio("1", "男", ("1".equals(oldPerson.getNBPT_SP_PERSON_MALE()) ? true : false)));
				maleRadios.getRadios().add(new Radio("0", "女", ("0".equals(oldPerson.getNBPT_SP_PERSON_MALE()) ? true : false)));
				regionResper.getColumns().add(maleRadios);

				regionResper.getColumns().add(new FormItem("newPersonMob1", "个人手机", "text", false, oldPerson.getNBPT_SP_PERSON_MOB1(), "必填项", true));
				regionResper.getColumns().add(new FormItem("newPersonMob2", "紧急联系人", "text", false, oldPerson.getNBPT_SP_PERSON_MOB2()));
				regionResper.getColumns().add(new FormItem("newPersonEntryDate", "入职时间", "date"
						, true, CommonUtil.formateTimeToPage(oldPerson.getNBPT_SP_PERSON_ENTRYDATA()), "", true));

				
				regionResper.getColumns().add(new FormItem("newPersonQq", "QQ", "text", false, oldPerson.getNBPT_SP_PERSON_QQ()));
				regionResper.getColumns().add(new FormItem("newPersonWechat", "微信", "text", false, oldPerson.getNBPT_SP_PERSON_CHAT()));
				regionResper.getColumns().add(new FormItem("newPersonEmail", "邮箱", "text", false, oldPerson.getNBPT_SP_PERSON_MAIL()));
				regionResper.getColumns().add(new FormItem("newPersonSchool", "毕业院校", "text", false, oldPerson.getNBPT_SP_PERSON_SCHOOL()));
				regionResper.getColumns().add(new FormItem("newPersonProfess", "专业", "text", false, oldPerson.getNBPT_SP_PERSON_PROFESS()));
				regionResper.getColumns().add(new FormItem("newPersonTitle", "职称", "text", false, oldPerson.getNBPT_SP_PERSON_TITLE()));
				regionResper.getColumns().add(new FormItem("newPersonNote", "备注", "textarea", false, oldPerson.getNBPT_SP_PERSON_NOTE()));

				back.setData(regionResper.toJsonString());
				return this.after(back.toJsonString());
			} else if ("22".equals(oldPerson.getNBPT_SP_PERSON_JOB())){
				
				// 修改地总信息
				CreateForm areaResper = new CreateForm("addNewAreaResper", "修改地总");
				areaResper.getColumns().add(new FormItem("newPersonType", "分类", "text", true, oldPerson.getNBPT_SP_PERSON_TYPENAME() + "  地总"));
				areaResper.getColumns().add(new FormItem("newPersonTypeValue", "", "hidden", false, oldPerson.getNBPT_SP_PERSON_TYPE()));
				areaResper.getColumns().add(new FormItem("newPersonJob", "", "hidden", false, oldPerson.getNBPT_SP_PERSON_JOB()));
				areaResper.getColumns().add(new FormItem("newPersonPid", "", "hidden", false, oldPerson.getNBPT_SP_PERSON_PID()));
				
				areaResper.getColumns().add(new FormItem("newPersonName", "新地总名称", "text", false, oldPerson.getNBPT_SP_PERSON_NAME() , "",true));
				areaResper.getColumns().add(new FormItem("newPersonLoginId", "新地总名称拼音", "text", false, oldPerson.getNBPT_SP_PERSON_LOGINID(), "必须项,拼写错误,将导致无法登录", true));
				areaResper.getColumns().add(new FormItem("newPersonIdNum", "身份证号码", "text", false, oldPerson.getNBPT_SP_PERSON_IDNUM()));
				areaResper.getColumns().add(new FormItem("newPersonPlace", "籍贯", "text", false, oldPerson.getNBPT_SP_PERSON_PLACE(), "填写身份证后若无自动填充,请手动填写", true));

				FormItem areaMaleRadios = new FormItem("newPersonMale", "性别", "radio", false, "", "");
				areaMaleRadios.getRadios().add(new Radio("1", "男", ("1".equals(oldPerson.getNBPT_SP_PERSON_MALE()) ? true : false)));
				areaMaleRadios.getRadios().add(new Radio("0", "女", ("0".equals(oldPerson.getNBPT_SP_PERSON_MALE()) ? true : false)));
				areaResper.getColumns().add(areaMaleRadios);

				areaResper.getColumns().add(new FormItem("newPersonMob1", "个人手机", "text", false, oldPerson.getNBPT_SP_PERSON_MOB1(), "必填项", true));
				areaResper.getColumns().add(new FormItem("newPersonMob2", "紧急联系人", "text", false, ""));
				areaResper.getColumns().add(new FormItem("newPersonEntryDate", "入职时间", "date"
						, true, CommonUtil.formateTimeToPage(oldPerson.getNBPT_SP_PERSON_ENTRYDATA()), "", true));

				FormItem areaDegrees = new FormItem("newPersonDegree", "学历", "select", false, "");
				oldPerson.getNBPT_SP_PERSON_DEGREE();
				areaDegrees.getOptions().add(new Option("小学", "小学", ("小学".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				areaDegrees.getOptions().add(new Option("初中", "初中", ("初中".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				areaDegrees.getOptions().add(new Option("高中", "高中", ("高中".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				areaDegrees.getOptions().add(new Option("中专", "中专", ("中专".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				areaDegrees.getOptions().add(new Option("大专", "大专", ("大专".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				areaDegrees.getOptions().add(new Option("本科", "本科", ("本科".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				areaDegrees.getOptions().add(new Option("研究生", "研究生", ("研究生".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				areaResper.getColumns().add(areaDegrees);

				areaResper.getColumns().add(new FormItem("newPersonQq", "QQ", "text", false, oldPerson.getNBPT_SP_PERSON_QQ()));
				areaResper.getColumns().add(new FormItem("newPersonWechat", "微信", "text", false, oldPerson.getNBPT_SP_PERSON_CHAT()));
				areaResper.getColumns().add(new FormItem("newPersonEmail", "邮箱", "text", false, oldPerson.getNBPT_SP_PERSON_MAIL()));
				areaResper.getColumns().add(new FormItem("newPersonSchool", "毕业院校", "text", false, oldPerson.getNBPT_SP_PERSON_SCHOOL()));
				areaResper.getColumns().add(new FormItem("newPersonProfess", "专业", "text", false, oldPerson.getNBPT_SP_PERSON_PROFESS()));
				areaResper.getColumns().add(new FormItem("newPersonTitle", "职称", "text", false, oldPerson.getNBPT_SP_PERSON_TITLE()));
				areaResper.getColumns().add(new FormItem("newPersonNote", "备注", "textarea", false, oldPerson.getNBPT_SP_PERSON_NOTE()));
				
				back.setData(areaResper.toJsonString());
				return this.after(back.toJsonString());
			} else {
				
				// 修改终端信息
				CreateForm terminal = new CreateForm("addNewTerminal", "修改下级");
				terminal.getColumns().add(new FormItem("newPersonType", "分类", "text", true
						, oldPerson.getNBPT_SP_PERSON_TYPENAME() + " " + oldPerson.getNBPT_SP_PERSON_AREA_NAME() + " 销售人员"));
				terminal.getColumns().add(new FormItem("newPersonTypeValue", "", "hidden", false, oldPerson.getNBPT_SP_PERSON_TYPE()));
				terminal.getColumns().add(new FormItem("newPersonRegionUid", "", "hidden", false, oldPerson.getNBPT_SP_PERSON_AREA_UID()));
				terminal.getColumns().add(new FormItem("newPersonPid", "", "hidden", false, oldPerson.getNBPT_SP_PERSON_PID()));
				
				terminal.getColumns().add(new FormItem("newPersonName", "人员名称", "text", true, oldPerson.getNBPT_SP_PERSON_NAME(), "", true));
				
				FormItem terminalJobRadios = new FormItem("newPersonJob", "职务", "radio", false, true);
				terminalJobRadios.getRadios().add(new Radio("23", "区县总", ("23".equals(oldPerson.getNBPT_SP_PERSON_JOB()) ? true : false)));
				terminalJobRadios.getRadios().add(new Radio("24", "预备区县总", ("24".equals(oldPerson.getNBPT_SP_PERSON_JOB()) ? true : false)));
				terminalJobRadios.getRadios().add(new Radio("25", "推广经理", ("25".equals(oldPerson.getNBPT_SP_PERSON_JOB()) ? true : false)));
				terminal.getColumns().add(terminalJobRadios);
				
				terminal.getColumns().add(new FormItem("newPersonIdNum", "身份证号码", "idCard", false, oldPerson.getNBPT_SP_PERSON_IDNUM(), "", true));
				terminal.getColumns().add(new FormItem("newPersonPlace", "籍贯", "text", false, oldPerson.getNBPT_SP_PERSON_PLACE(), "填写身份证后若无自动填充,请手动填写", true));

				FormItem terminalMaleRadios = new FormItem("newPersonMale", "性别", "radio", false, "", "");
				terminalMaleRadios.getRadios().add(new Radio("1", "男", ("1".equals(oldPerson.getNBPT_SP_PERSON_MALE()) ? true : false)));
				terminalMaleRadios.getRadios().add(new Radio("0", "女", ("0".equals(oldPerson.getNBPT_SP_PERSON_MALE()) ? true : false)));
				terminal.getColumns().add(terminalMaleRadios);

				terminal.getColumns().add(new FormItem("newPersonMob1", "个人手机", "text", false, oldPerson.getNBPT_SP_PERSON_MOB1(), "必填项", true));
				terminal.getColumns().add(new FormItem("newPersonMob2", "紧急联系人", "text", false, oldPerson.getNBPT_SP_PERSON_MOB2()));
				terminal.getColumns().add(new FormItem("newPersonEntryDate", "入职时间", "text"
						, true, CommonUtil.formateTimeToPage(oldPerson.getNBPT_SP_PERSON_ENTRYDATA()), "", true));
				
				FormItem policyTypeSelects = new FormItem("newPersonPolicyType", "保单编号", "select", false, "");
				List<NBPT_COMMON_DICTIONARY> policyTypes = personManageDao.receiveDictionarys("POLICYTYPE", this.getConnection());
				
				// 获取保单类型分类
				for(NBPT_COMMON_DICTIONARY policyType : policyTypes) {
					Option newOption = null;
						if(oldPerson.getNBPT_SP_PERSON_POLICYTYPE().equals(policyType.getNBPT_COMMON_DICTIONARY_KEY_VALUE())) {
							newOption = new Option(policyType.getNBPT_COMMON_DICTIONARY_KEY_VALUE(), policyType.getNBPT_COMMON_DICTIONARY_KEY_NAME(), true);
						} else {
							newOption = new Option(policyType.getNBPT_COMMON_DICTIONARY_KEY_VALUE(), policyType.getNBPT_COMMON_DICTIONARY_KEY_NAME());
						}
						policyTypeSelects.getOptions().add(newOption);
				}
				terminal.getColumns().add(policyTypeSelects);
				terminal.getColumns().add(new FormItem("newPersonPolicyNo", "", "text", false, oldPerson.getNBPT_SP_PERSON_POLICYNO(), "此处填写保单编号,注意选择保单类型"));
				terminal.getColumns().add(new FormItem("newPersonPolicyDate1", "保单起始时间", "date", true, oldPerson.getNBPT_SP_PERSON_POLICY_DATA1(), "", false));
				terminal.getColumns().add(new FormItem("newPersonPolicyDate2", "保单结束时间", "date", true, oldPerson.getNBPT_SP_PERSON_POLICY_DATA2(), "", false));
				
				// 公共item
				FormItem terminalDegrees = new FormItem("newPersonDegree", "学历", "select", false, "");
				oldPerson.getNBPT_SP_PERSON_DEGREE();
				terminalDegrees.getOptions().add(new Option("小学", "小学", ("小学".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				terminalDegrees.getOptions().add(new Option("初中", "初中", ("初中".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				terminalDegrees.getOptions().add(new Option("高中", "高中", ("高中".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				terminalDegrees.getOptions().add(new Option("中专", "中专", ("中专".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				terminalDegrees.getOptions().add(new Option("大专", "大专", ("大专".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				terminalDegrees.getOptions().add(new Option("本科", "本科", ("本科".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				terminalDegrees.getOptions().add(new Option("研究生", "研究生", ("研究生".equals(oldPerson.getNBPT_SP_PERSON_DEGREE()) ? true : false)));
				terminal.getColumns().add(terminalDegrees);
				
				terminal.getColumns().add(new FormItem("newPersonQq", "QQ", "text", false, oldPerson.getNBPT_SP_PERSON_QQ()));
				terminal.getColumns().add(new FormItem("newPersonWechat", "微信", "text", false, oldPerson.getNBPT_SP_PERSON_CHAT()));
				terminal.getColumns().add(new FormItem("newPersonEmail", "邮箱", "text", false, oldPerson.getNBPT_SP_PERSON_MAIL()));
				terminal.getColumns().add(new FormItem("newPersonSchool", "毕业院校", "text", false, oldPerson.getNBPT_SP_PERSON_SCHOOL()));
				terminal.getColumns().add(new FormItem("newPersonProfess", "专业", "text", false, oldPerson.getNBPT_SP_PERSON_PROFESS()));
				terminal.getColumns().add(new FormItem("newPersonTitle", "职称", "text", false, oldPerson.getNBPT_SP_PERSON_TITLE()));
				terminal.getColumns().add(new FormItem("newPersonNote", "备注", "textarea", false, oldPerson.getNBPT_SP_PERSON_NOTE()));
				
				back.setData(terminal.toJsonString());
				return this.after(back.toJsonString());
			}
		} catch (Exception e) {
			JsonDataBack back = new JsonDataBack();
			back.setStateError("出现错误,请联系后台管理员");
			return this.after(back.toJsonString());
		}
	}

	@Override
	public String postChangePerson(PostAddPersonIn in) {

		try {
			this.before();

			// 获取登录人员
			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
			
			// 获取原登录信息
			NBPT_SP_PERSON updatePerson = personManageDao.receiveSpPerson(in.getNewPersonPid(), this.getConnection());
			
//			updatePerson.setNBPT_SP_PERSON_PID(in.getNewPersonPid());
//			String maxId = personManageDao.receiveMaxId(this.getConnection(), "NBPT_SP_PERSON");
//			updatePerson.setNBPT_SP_PERSON_ID(maxId); // 新纪录ID
			updatePerson.setNBPT_SP_PERSON_AREANO("");
			updatePerson.setNBPT_SP_PERSON_BIRS(CommonUtil.birthDay_from_idCard(in.getNewPersonIdNum()));
			updatePerson.setNBPT_SP_PERSON_CHAT(in.getNewPersonWechat());
			updatePerson.setNBPT_SP_PERSON_DEGREE(in.getNewPersonDegree());
			updatePerson.setNBPT_SP_PERSON_DEPT_ID("");
//			updatePerson.setNBPT_SP_PERSON_ENTERINGTIME(CommonUtil.getYYYYMMDD());
			updatePerson.setNBPT_SP_PERSON_ENTRYDATA(CommonUtil.formateTiemToBasic(in.getNewPersonEntryDate()));
			updatePerson.setNBPT_SP_PERSON_IDNUM(in.getNewPersonIdNum());
			updatePerson.setNBPT_SP_PERSON_JOB(in.getNewPersonJob());
//			updatePerson.setNBPT_SP_PERSON_LEAVE_APPLY_DATA("");
//			updatePerson.setNBPT_SP_PERSON_LEAVE_REAL_DATA("");
//			updatePerson.setNBPT_SP_PERSON_LEAVEDATA("");
			updatePerson.setNBPT_SP_PERSON_MAIL(in.getNewPersonEmail());
			updatePerson.setNBPT_SP_PERSON_MALE(in.getNewPersonMale());
			updatePerson.setNBPT_SP_PERSON_MOB1(in.getNewPersonMob1());
			updatePerson.setNBPT_SP_PERSON_MOB2(in.getNewPersonMob2());
			updatePerson.setNBPT_SP_PERSON_NAME(in.getNewPersonName());
			updatePerson.setNBPT_SP_PERSON_NOTE(in.getNewPersonNote());
//			updatePerson.setNBPT_SP_PERSON_PID(CommonUtil.getUUID_32());
			updatePerson.setNBPT_SP_PERSON_PLACE(in.getNewPersonPlace());
			updatePerson.setNBPT_SP_PERSON_POLICY_DATA1(CommonUtil.formateTiemToBasic(in.getNewPersonPolicyDate1()));
			updatePerson.setNBPT_SP_PERSON_POLICY_DATA2(CommonUtil.formateTiemToBasic(in.getNewPersonPolicyDate2()));
			updatePerson.setNBPT_SP_PERSON_POLICYNO(in.getNewPersonPolicyNo());
			updatePerson.setNBPT_SP_PERSON_PROFESS(in.getNewPersonProfess());
			updatePerson.setNBPT_SP_PERSON_QQ(in.getNewPersonQq());
			updatePerson.setNBPT_SP_PERSON_SCHOOL(in.getNewPersonSchool());
			updatePerson.setNBPT_SP_PERSON_TITLE(in.getNewPersonTitle());
			updatePerson.setNBPT_SP_PERSON_TYPE(in.getNewPersonTypeValue());
			if(null != loginPerson) { // 如果不是后勤
				// 记录插入行为
				personManageDao.addRecord(updatePerson.getNBPT_SP_PERSON_PID(), this.getLoginId(), "9", this.getConnection());
			} else {
				updatePerson.setNBPT_SP_PERSON_LOGINID(in.getNewPersonLoginId());
				// 记录插入行为
				personManageDao.addRecord(updatePerson.getNBPT_SP_PERSON_PID(), this.getLoginId(), "18", this.getConnection());
			}
			// 更新记录
			personManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON.class, "update", "NBPT_SP_PERSON", updatePerson), this.getConnection());

			JsonDataBack back = new JsonDataBack("OK", "修改成功", "");
			return this.after(back.toJsonString());
		} catch (Exception e) {
			JsonDataBack back = new JsonDataBack();
			back.setStateError("出现错误,请联系后台管理员");
			return this.after(back.toJsonString());
		}
	}

	@Override
	public String getTransferPerson(String personPid) {
		try {
			this.before();
			// 获取人员信息
			NBPT_VIEW_CURRENTPERSON person = personManageDao.receivePerson(personPid, this.getConnection());
			CreateForm terminal = new CreateForm("transferPerson", "人员调岗");
			terminal.getColumns().add(new FormItem("personName", "人员姓名", "text", true, person.getNBPT_SP_PERSON_NAME(), "", true));
			terminal.getColumns().add(new FormItem("personPid", "", "hidden", true, personPid, "", true));
			FormItem terminalMaleRadios = new FormItem("personJob", "职务", "radio", false, "", "");
			terminalMaleRadios.getRadios().add(new Radio("21", "大区总", ("21".equals(person.getNBPT_SP_PERSON_JOB()) ? true : false)));
			terminalMaleRadios.getRadios().add(new Radio("26", "混合大区总", ("26".equals(person.getNBPT_SP_PERSON_JOB()) ? true : false)));
			terminalMaleRadios.getRadios().add(new Radio("22", "地总", ("22".equals(person.getNBPT_SP_PERSON_JOB()) ? true : false)));
			terminalMaleRadios.getRadios().add(new Radio("23", "区县总", ("23".equals(person.getNBPT_SP_PERSON_JOB()) ? true : false)));
			terminalMaleRadios.getRadios().add(new Radio("24", "预备区县总", ("24".equals(person.getNBPT_SP_PERSON_JOB()) ? true : false)));
			terminalMaleRadios.getRadios().add(new Radio("25", "推广经理", ("25".equals(person.getNBPT_SP_PERSON_JOB()) ? true : false)));
			terminal.getColumns().add(terminalMaleRadios);
			// 获取所有大区
			List<NBPT_VIEW_REGION> regions = personManageDao.receiveRegions(null, null, "1", "2", this.getConnection());
			regions = CommonUtil.getListInMapByKey(CommonUtil.classify(regions, "NBPT_SP_REGION_FLAG", NBPT_VIEW_REGION.class), "1");
			FormItem regionSelect = new FormItem("personRegionUid", "大区", "select");
			regionSelect.getOptions().add(new Option("",""));
			for(NBPT_VIEW_REGION region : regions) {
				Option newOption = null;
				if(region.getNBPT_SP_REGION_UID().equals(person.getNBPT_SP_PERSON_REGION_UID())) {
					newOption = new Option(region.getNBPT_SP_REGION_UID(), region.getNBPT_SP_REGION_NAME(), true);
				} else {
					newOption = new Option(region.getNBPT_SP_REGION_UID(), region.getNBPT_SP_REGION_NAME());
				}
				regionSelect.getOptions().add(newOption);
			}
			terminal.getColumns().add(regionSelect);
			// 获取当前大区下所有地区
			List<NBPT_VIEW_REGION> areas = personManageDao.receiveRegions(person.getNBPT_SP_PERSON_REGION_UID(), null, null, "2", this.getConnection());
			areas = CommonUtil.getListInMapByKey(CommonUtil.classify(areas, "NBPT_SP_REGION_FLAG", NBPT_VIEW_REGION.class), "1");
			FormItem areaSelect = new FormItem("personAreaUid", "地区", "select");
			areaSelect.getOptions().add(new Option("",""));
			for(NBPT_VIEW_REGION area : areas) {
				Option newOption = null;
				if(area.getNBPT_SP_REGION_UID().equals(person.getNBPT_SP_PERSON_AREA_UID())) {
					newOption = new Option(area.getNBPT_SP_REGION_UID(), area.getNBPT_SP_REGION_NAME(), true);
				} else {
					newOption = new Option(area.getNBPT_SP_REGION_UID(), area.getNBPT_SP_REGION_NAME());
				}
				areaSelect.getOptions().add(newOption);
			}
			terminal.getColumns().add(areaSelect);
			JsonDataBack back = new JsonDataBack();
			back.setData(terminal.toJsonString());
			return this.after(back.toJsonString());
		} catch (Exception e) {
			JsonDataBack back = new JsonDataBack();
			back.setStateError("出现错误,请联系后台管理员");
			return this.after(back.toJsonString());
		}
	}

	@Override
	public String postTransferPerson(PostTransferPersonIn in) {
		try {
			this.before();
			// 获取要修改的人员信息
			NBPT_VIEW_CURRENTPERSON currentPerson = personManageDao.receivePerson(in.getPersonPid(), this.getConnection());
			NBPT_SP_PERSON_REGION person_region = 
					personManageDao.receivePersonRegion(currentPerson.getNBPT_SP_PERSON_PID(), this.getConnection());
			JsonDataBack back = new JsonDataBack();
			switch (currentPerson.getNBPT_SP_PERSON_JOB()){
			case "21":
			case "26": // 原本是大区总或混合大区总
				switch (in.getPersonJob()) {
				case "21":
				case "26": // 如果修改成大区总或混合大区总
					// 先改了你的job
					this.changeJob(in, "1");
					// 清空原有的部门关系,包括从属和负责
					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_RID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '2'", this.getConnection());
					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_PID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '1'", this.getConnection());
					back.setStateOk("原部门关系已清空,请在部门管理中重新配置负责大区");
					break;
				case "22":
					// 哦,有个SB被降级了,先修改这SB的job
					this.changeJob(in, "2");

					// 清空原有的部门关系,包括从属和负责
					personManageDao.excuteUpdate(
							"UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_RID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '2'", this.getConnection());
					personManageDao.excuteUpdate(
							"UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_PID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '1'", this.getConnection());
					back.setStateOk("原部门关系已清空,请在部门管理中重新配置负责地区");
					break;
//				case "23":
//				case "24":
//				case "25":
//					// 哦,这SB犯了什么错,降这么厉害,先改了你的job
//					this.changeJob(in);
//					// 解除负责关系
//					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
//							+ "SET NBPT_SP_PERSON_REGION_RID = '',"
//							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' AND NBPT_SP_PERSON_REGION_TYPE = '1'", this.getConnection());
//					// 添加从属关系
//					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
//							+ "SET NBPT_SP_PERSON_REGION_RID = '" + in.getPersonAreaUid() + "',"
//							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' AND NBPT_SP_PERSON_REGION_TYPE = '2'", this.getConnection());
//					
//					// 嗯,还要封了你的账号,好可怜
////					userManageDao.getUser(this.getConnection(), currentPerson.getNBPT_SP_PERSON_LOGINID());
//					break;
				}
				break;
			case "22": // 如果原来是地总
				switch (in.getPersonJob()) {
				case "21":
				case "26": // 哟哟,这货升级了,恭喜
					// 先修改了你的job
					this.changeJob(in, "1");
					// 清空原有的部门关系,包括从属和负责
					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_RID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '2'", this.getConnection());
					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_PID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '1'", this.getConnection());
					back.setStateOk("原部门关系已清空,请在部门管理中重新配置负责大区");
					break;
				case "22":
					// 清空原有的部门关系,包括从属和负责
					// 丫有病
					// 清空原有的部门关系,包括从属和负责
					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_RID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '2'", this.getConnection());
					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_PID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '1'", this.getConnection());
					back.setStateOk("原部门关系已清空,请在部门管理中重新配置负责地区");
					break;
				case "23":
				case "24":
				case "25":
					// 哦,这SB犯了什么错,降级了,先改了你的job
					this.changeJob(in, "3");
					// 重新绑定从属关系
					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_RID = '" + in.getPersonAreaUid() + "' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '2'", this.getConnection());
					// 还要置你的账户为无效
					personManageDao.excuteUpdate(
							"UPDATE NBPT_RSFZ_USER SET NBPT_RSFZ_USER_FLAG = '0' "
							+ "WHERE NBPT_RSFZ_USER_ID = '" + currentPerson.getNBPT_SP_PERSON_LOGINID() + "'"
							, this.getConnection());
					back.setStateOk(currentPerson.getNBPT_SP_PERSON_NAME() + "降职成功");
					break;
				}
				break;
			case "23":
			case "24":
			case "25":
				switch (in.getPersonJob()) {
				case "23":
				case "24":
				case "25": // 平调哦,反正就是换个地方
					this.changeJob(in, "3");
					
					// 然后修改新的从属关系
					person_region.setNBPT_SP_PERSON_REGION_RID(in.getPersonAreaUid());
					personManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON_REGION.class, "UPDATE", "NBPT_SP_PERSON_REGION", person_region), this.getConnection());
					back.setStateOk(currentPerson.getNBPT_SP_PERSON_NAME() + "调岗成功");
					break;
				case "22":// 升级了
					// 先改了你的job
					this.changeJob(in, "2");
					// TODO 记得申请账号和密码
					// 改变新的从属关系,变为未绑定关系
					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_RID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '2'", this.getConnection());
					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_PID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '1'", this.getConnection());

					if(null == currentPerson.getNBPT_SP_PERSON_LOGINID() || "".equals(currentPerson.getNBPT_SP_PERSON_LOGINID())) {
						CreateForm areaResper = new CreateForm("addNewAreaResperLogin", "添加新地总");
						areaResper.getColumns().add(new FormItem("newPersonName", "新地总名称", "text", true, currentPerson.getNBPT_SP_PERSON_NAME(), "", true));
						areaResper.getColumns().add(new FormItem("newPersonPid", "", "hidden", false, currentPerson.getNBPT_SP_PERSON_PID()));
						areaResper.getColumns().add(new FormItem("newPersonLoginId", "新地总名称拼音", "text", false, "", "必须项,拼写错误,将导致无法登录", true));
						areaResper.getColumns().add(new FormItem("newPersonPassword", "新地总登录密码", "text", false, "", "非必须项,默认为lingrui123"));
						back.setData(areaResper.toJsonString());
					} else {
						personManageDao.excuteUpdate(
								"UPDATE NBPT_RSFZ_USER SET NBPT_RSFZ_USER_FLAG = '1' " + 
								"WHERE NBPT_RSFZ_USER_ID = '" + currentPerson.getNBPT_SP_PERSON_LOGINID() + "'", this.getConnection());
						// 更新权限
						String rid = "22".equals(currentPerson.getNBPT_SP_PERSON_JOB()) ? "600003" : "21".equals(currentPerson.getNBPT_SP_PERSON_JOB()) ? "600006" :"600008";
						personManageDao.excuteUpdate("UPDATE NBPT_RSFZ_U_R SET NBPT_RSFZ_U_R_RID = '" + rid + "' " + 
								"WHERE NBPT_RSFZ_U_R_UID = '" + currentPerson.getNBPT_SP_PERSON_LOGINID() + "'", this.getConnection());
						back.setStateOk("原账户恢复成功");
					}
					break;
				case "21":
				case "26": // 流弊,大升
					this.changeJob(in, "1");
					// TODO 记得申请账号和密码
					// 改变绑定关系
					// 改变新的从属关系,变为未绑定关系
					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_RID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '2'", this.getConnection());
					personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
							+ "SET NBPT_SP_PERSON_REGION_PID = '' "
							+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + in.getPersonPid() + "' "
							+ "AND NBPT_SP_PERSON_REGION_TYPE = '1'", this.getConnection());
					
					if(null == currentPerson.getNBPT_SP_PERSON_LOGINID() || "".equals(currentPerson.getNBPT_SP_PERSON_LOGINID())) {
						CreateForm regionResper = new CreateForm("addNewAreaResperLogin", "添加新大区总");
						regionResper.getColumns().add(new FormItem("newPersonName", "新大区总名称", "text", true, currentPerson.getNBPT_SP_PERSON_NAME(), "", true));
						regionResper.getColumns().add(new FormItem("newPersonPid", "", "hidden", false, currentPerson.getNBPT_SP_PERSON_PID()));
						regionResper.getColumns().add(new FormItem("newPersonLoginId", "新大区总名称拼音", "text", false, "", "必须项,拼写错误,将导致无法登录", true));
						regionResper.getColumns().add(new FormItem("newPersonPassword", "新大区总登录密码", "text", false, "", "非必须项,默认为lingrui123"));
						back.setData(regionResper.toJsonString());
					} else {
						// 恢复账号
						personManageDao.excuteUpdate(
								"UPDATE NBPT_RSFZ_USER SET NBPT_RSFZ_USER_FLAG = '1' " + 
								"WHERE NBPT_RSFZ_USER_ID = '" + currentPerson.getNBPT_SP_PERSON_LOGINID() + "'", this.getConnection());
						// 更新权限
						String rid = "22".equals(currentPerson.getNBPT_SP_PERSON_JOB()) ? "600003" : "21".equals(currentPerson.getNBPT_SP_PERSON_JOB()) ? "600006" :"600008";
						personManageDao.excuteUpdate("UPDATE NBPT_RSFZ_U_R SET NBPT_RSFZ_U_R_RID = '" + rid + "' " + 
								"WHERE NBPT_RSFZ_U_R_UID = '" + currentPerson.getNBPT_SP_PERSON_LOGINID() + "'", this.getConnection());
						back.setStateOk("原账户恢复成功");
					}
					break;
				}
			}
			return this.after(back.toJsonString());
		} catch (Exception e) {
			JsonDataBack back = new JsonDataBack();
			back.setStateError();
			return this.after(back.toJsonString());
		}
	}
	
	private void changeJob(PostTransferPersonIn in,String level) throws SQLException {
		NBPT_SP_PERSON updateperson = new NBPT_SP_PERSON();
		updateperson.setNBPT_SP_PERSON_PID(in.getPersonPid());
		updateperson.setNBPT_SP_PERSON_JOB(in.getPersonJob());
		updateperson.setNBPT_SP_PERSON_LEVEL(level);
		personManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON.class, "UPDATE", "NBPT_SP_PERSON", updateperson), this.getConnection());
	}

	@Override
	public String postAddNewUser(String newUserLoginId, String newUserPassword, String newUserPid) {
		try {
			this.before();
			
			// 获取用户
			NBPT_VIEW_CURRENTPERSON person = personManageDao.receivePerson(newUserPid, this.getConnection());
			
			if(null == person.getNBPT_SP_PERSON_LOGINID() || "".equals(person.getNBPT_SP_PERSON_LOGINID())) {
				// 创建新登录账号
				NBPT_RSFZ_USER user = new NBPT_RSFZ_USER();
				user.setNBPT_RSFZ_USER_ID(newUserLoginId);
				user.setNBPT_RSFZ_USER_NAME(person.getNBPT_SP_PERSON_NAME());
				user.setNBPT_RSFZ_USER_PHONE("");
				user.setNBPT_RSFZ_USER_BZ("");
				user.setNBPT_RSFZ_USER_EMALL(person.getNBPT_SP_PERSON_MAIL());
				user.setNBPT_RSFZ_USER_PASSWORD("".equals(newUserPassword) ? "lingrui123" : newUserPassword);
				user.setNBPT_RSFZ_USER_FLAG("1");
				userManageDao.postAddUser(this.getConnection(), user);
				
				// 更新用户
				NBPT_SP_PERSON updatePerson = new NBPT_SP_PERSON();
				updatePerson.setNBPT_SP_PERSON_PID(person.getNBPT_SP_PERSON_PID());
				updatePerson.setNBPT_SP_PERSON_LOGINID(newUserLoginId);
				personManageDao.excuteUpdate(DBUtils.beanToSql(NBPT_SP_PERSON.class, "UPDATE", "NBPT_SP_PERSON", updatePerson), this.getConnection());
				
				// 添加权限
				String rid = "22".equals(person.getNBPT_SP_PERSON_JOB()) ? "600003" : "21".equals(person.getNBPT_SP_PERSON_JOB()) ? "600006" :"600008";
				
				NBPT_RSFZ_U_R rsfz_U_R = new NBPT_RSFZ_U_R();
				rsfz_U_R.setNBPT_RSFZ_U_R_RID(rid);
				rsfz_U_R.setNBPT_RSFZ_U_R_UID(newUserLoginId);
				userManageDao.postAddU_R(this.getConnection(), rsfz_U_R);
			} else {
				// 恢复账户
				personManageDao.excuteUpdate("UPDATE NBPT_RSFZ_USER SET NBPT_RSFZ_USER_FLAG = '1' WHERE NBPT_RSFZ_USER_ID = '" + newUserLoginId + "'", this.getConnection());
			}
			
			JsonDataBack back = new JsonDataBack();
			back.setStateOk("新账户创建或旧用户恢复成功");
			return this.after(back.toJsonString());
		} catch (Exception e) {
			JsonDataBack back = new JsonDataBack();
			back.setStateError();
			return this.after(back.toJsonString());
		}
	}

	@Override
	public String getDimissTerminal(String dimissTerminalPid) {
		try {
			this.before();

			JsonDataBack back = new JsonDataBack();
			// 获取登录人员
			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
			// 获取要修改的人员
			NBPT_VIEW_CURRENTPERSON terminal = personManageDao.receivePerson(dimissTerminalPid, this.getConnection());
			if(null != loginPerson) {
				// 返回form
				CreateForm terminalForm = new CreateForm("transferPerson", "人员离职");
				terminalForm.getColumns().add(new FormItem("personName", "人员姓名", "text", true, terminal.getNBPT_SP_PERSON_NAME(), "", true));
				terminalForm.getColumns().add(new FormItem("personPid", "", "hidden", true, terminal.getNBPT_SP_PERSON_PID(), "", true));
				terminalForm.getColumns().add(new FormItem("personLeaveTime", "离职时间", "date", true, true));
				terminalForm.getColumns().add(new FormItem("personLeaveReason", "离职原因", "text", false, ""));
				back.setData(terminalForm.toJsonString());
			} else {
				// 因为页面控制,后勤的离职只有地总和大区总,直接离职
				// 修改状态为离职审核
				personManageDao.changeTerminalState(terminal.getNBPT_SP_PERSON_PID(), "3", this.getConnection()
						, new String[] {CommonUtil.formateTiemToBasic(CommonUtil.getYYYYMMDD())});
				// 添加操作记录
				personManageDao.addRecord(terminal.getNBPT_SP_PERSON_PID(), this.getLoginId(), "19", this.getConnection());
				
				// 还要清楚绑定关系
				personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
						+ "SET NBPT_SP_PERSON_REGION_RID = '' "
						+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + terminal.getNBPT_SP_PERSON_PID() + "' "
						+ "AND NBPT_SP_PERSON_REGION_TYPE = '2'", this.getConnection());
				personManageDao.excuteUpdate("UPDATE NBPT_SP_PERSON_REGION "
						+ "SET NBPT_SP_PERSON_REGION_PID = '' "
						+ "WHERE NBPT_SP_PERSON_REGION_PID = '" + terminal.getNBPT_SP_PERSON_PID() + "' "
						+ "AND NBPT_SP_PERSON_REGION_TYPE = '1'", this.getConnection());

				// 还要置你的账户为无效
				personManageDao.excuteUpdate(
						"UPDATE NBPT_RSFZ_USER SET NBPT_RSFZ_USER_FLAG = '0' "
						+ "WHERE NBPT_RSFZ_USER_ID = '" + terminal.getNBPT_SP_PERSON_LOGINID() + "'"
						, this.getConnection());
				back.setStateOk("离职调整成功");
			}
			return after(back.toJsonString());
		} catch (Exception e) {
			log.error("获取终端离职页面失败" + CommonUtil.getTrace(e));
			JsonDataBack back = new JsonDataBack();
			back.setStateError();
			return this.after(back.toJsonString());
		}
	}

	@Override
	public String postDimissTerminal(String personPid, String personLeaveTime, String personLeaveReason) {
		try {
			this.before();
			// 获取登录人员
			NBPT_VIEW_CURRENTPERSON loginPerson = personManageDao.receiveLoginPerson(this.getLoginId(), this.getConnection());
			// 获取要修改的人员
			NBPT_VIEW_CURRENTPERSON terminal = personManageDao.receivePerson(personPid, this.getConnection());
			if(loginPerson.getNBPT_SP_PERSON_PID().equals(terminal.getNBPT_SP_PERSON_AREA_RESPONSIBLER_PID())) {
				// 修改状态为离职审核
				personManageDao.changeTerminalState(personPid, "0", this.getConnection(), new String[] {CommonUtil.formateTiemToBasic(personLeaveTime)});
				// 添加操作记录
				personManageDao.addRecord(personPid, this.getLoginId(), "3", this.getConnection(), new String[] {personLeaveReason});
			}
			JsonDataBack back = new JsonDataBack();
			return after(back.toJsonString());
		} catch (Exception e) {
			
			log.error("终端离职失败" + CommonUtil.getTrace(e));
			JsonDataBack back = new JsonDataBack();
			back.setStateError("未知错误,请联系后台管理员");
			return this.after(back.toJsonString());
		}
	}

	@Override
	public String postDeletePerson(String personPid) {
		try {
			this.before();
			// 获取terminal
			NBPT_VIEW_CURRENTPERSON terminal = personManageDao.receivePerson(personPid, this.getConnection());
			
			// 执行删除,同时删除负责区域
			personManageDao.deleteTerminal(personPid, this.getConnection());
			personManageDao.deleteTerminalAreas(terminal.getNBPT_SP_PERSON_ID(), this.getConnection());
			// 删除绑定关系 TODO
			personManageDao.deletePersonRegion(personPid, this.getConnection());
			personManageDao.addRecord(personPid, this.getLoginId(), "5", this.getConnection());
			JsonDataBack back = new JsonDataBack();
			back.setStateOk("删除成功");
			return this.after(back.toJsonString());
		} catch (Exception e) {
			
			log.error("终端删除失败" + CommonUtil.getTrace(e));
			JsonDataBack back = new JsonDataBack();
			back.setStateError("位置错误,请联系后台管理员");
			return this.after(back.toJsonString());
		}
	}
	
}





































