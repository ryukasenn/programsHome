package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;
import com.cn.lingrui.sellPersonnel.pojos.person.AddPersonPojoIn;

public class SupportServiceUtils {

	private static Logger log = LogManager.getLogger();
	
	public static List<StatisticsTable> dealCurrentPerson_total(List<NBPT_VIEW_CURRENTPERSON> persons) throws Exception{
		
		// 初始化返回结果
		List<StatisticsTable> totalInfos = new ArrayList<>();
		
		try {
			
			Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyByTypePersons = CommonUtil.classify(persons, "NBPT_SP_PERSON_TYPENAME", NBPT_VIEW_CURRENTPERSON.class);
		
			for(String key : classifyByTypePersons.keySet()) {
				
				StatisticsTable info = new StatisticsTable();
				
				info.setRegionName(key);
				
				// 合计只统计在职人员
				List<NBPT_VIEW_CURRENTPERSON> inJobPersons = CommonUtil.getListInMapByKey(
						CommonUtil.classify(
								CommonUtil.getListInMapByKey(classifyByTypePersons, key), "NBPT_SP_PERSON_FLAG", NBPT_VIEW_CURRENTPERSON.class), "2");
				
				info.setTotal(inJobPersons.size());
				
				totalInfos.add(info);
			}

			StatisticsTable table = new StatisticsTable();
			table.setRegionName("合计");
			
			for(StatisticsTable info : totalInfos) {

				table.setTotal(table.getTotal() + info.getTotal());
			}
			totalInfos.add(table);
			return totalInfos;
			
		} catch (NoSuchFieldException e) {
			
			log.error("统计各分类合计信息时出错");
			throw new Exception();
		}
	}
	
	/**
	 * 
	 * @param persons 该分类下人员信息
	 * @param regions 该分类下部门信息
	 * @return
	 * @throws Exception
	 */
	public static List<StatisticsTable> dealCurrentPerson_otc(List<NBPT_VIEW_CURRENTPERSON> persons, List<NBPT_VIEW_REGION> regions) throws Exception{
		
		try {
			// 初始化返回结果
			List<StatisticsTable> OTCInfos = new ArrayList<>();

			Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyByRegions = CommonUtil.classify(persons, "NBPT_SP_PERSON_REGION_UID", NBPT_VIEW_CURRENTPERSON.class);
			
			for(NBPT_VIEW_REGION region : regions) {

				if(!"1".equals(region.getNBPT_SP_REGION_LEVEL())) {
					
					continue;
				}
				
				StatisticsTable info = new StatisticsTable();
				
				info.setNeed(info.getNeed() + CommonUtil.objToInteger(region.getNBPT_SP_REGION_NEED()));
				info.setRegionUid(region.getNBPT_SP_REGION_UID());
				info.setRegionName(region.getNBPT_SP_REGION_NAME());
				
				// 统计数据
				for(NBPT_VIEW_CURRENTPERSON otcType : CommonUtil.getListInMapByKey(classifyByRegions, region.getNBPT_SP_REGION_UID())) {
					CommonServiceUtils.count(info, otcType);
				}
								
				OTCInfos.add(info);
			}
			
			StatisticsTable totalTable = new StatisticsTable();
			totalTable.setRegionName("合计");
			for(StatisticsTable info : OTCInfos) {

				totalTable.setTotal(totalTable.getTotal() + info.getTotal());
				totalTable.setXzquResper(totalTable.getXzquResper() + info.getXzquResper());
				totalTable.setXzquResper_preparatory(totalTable.getXzquResper_preparatory() + info.getXzquResper_preparatory());
				totalTable.setPromote(totalTable.getPromote() + info.getPromote());
				totalTable.setNeed(totalTable.getNeed() + info.getNeed());
				totalTable.setDismission(totalTable.getDismission() + info.getDismission());
				
				totalTable.setAreaResper(totalTable.getAreaResper() + info.getAreaResper());
				totalTable.setRegionResper(totalTable.getRegionResper() + info.getRegionResper());
			}
			OTCInfos.add(totalTable);
			CommonServiceUtils.otherCompute(OTCInfos);
			return OTCInfos; 
		}catch (NoSuchFieldException e) {
			
			log.error("统计OTC合计信息时出错");
			throw new Exception();
		}
	}
	
	public static NBPT_SP_PERSON checkDataIn(NBPT_SP_PERSON person, AddPersonPojoIn in) {
		
		if(null != in.getNBPT_SP_PERSON_PID() && !"".equals(in.getNBPT_SP_PERSON_PID())) {

			// 注入原PID
			person.setNBPT_SP_PERSON_PID(in.getNBPT_SP_PERSON_PID());

			// 人员状态标志,
			person.setNBPT_SP_PERSON_FLAG(person.getNBPT_SP_PERSON_FLAG());
			
		} else {

			// 生成随机ID码
			person.setNBPT_SP_PERSON_PID(CommonUtil.getUUID_32());
			// 人员状态标志,
			person.setNBPT_SP_PERSON_FLAG("2"); 
		}
		
		// 人员类型
		person.setNBPT_SP_PERSON_TYPE(in.getNBPT_SP_PERSON_TYPE());
		
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
		person.setNBPT_SP_PERSON_ENTRYDATA(CommonUtil.formateTiemToBasic(in.getNBPT_SP_PERSON_ENTRYDATA()));
		
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
		person.setNBPT_SP_PERSON_POLICY_DATA1(CommonUtil.formateTiemToBasic(in.getNBPT_SP_PERSON_POLICY_DATA1()));
		
		// 保单结束时间
		person.setNBPT_SP_PERSON_POLICY_DATA2(CommonUtil.formateTiemToBasic(in.getNBPT_SP_PERSON_POLICY_DATA2()));
		
		// 登录名
		person.setNBPT_SP_PERSON_LOGINID(in.getNBPT_SP_PERSON_LOGINID());
		
		// 备注
		person.setNBPT_SP_PERSON_NOTE(in.getNBPT_SP_PERSON_NOTE());
		
		// 保单类型
		person.setNBPT_SP_PERSON_POLICYTYPE(in.getNBPT_SP_PERSON_POLICYTYPE());
		
		
		person.setNBPT_SP_PERSON_ENTERINGTIME(CommonUtil.getYYYYMMDD()); // 设置系统录入时间
		
		return person;
	}
}
























