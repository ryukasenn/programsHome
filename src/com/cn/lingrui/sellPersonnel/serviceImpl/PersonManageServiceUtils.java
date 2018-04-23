package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.pojos.AddPersonPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.SellPersonnelPojoOut;
import com.cn.lingrui.sellPersonnel.pojos.common.StatisticsTable;

public class PersonManageServiceUtils {
	
	/**
	 * 地总添加人员信息注入
	 * @param in
	 * @param person
	 * @param reposAreas 
	 * @param reposAreas
	 * @param out
	 */
	public static void checkInData(AddPersonPojoIn in, NBPT_SP_PERSON person, List<NBPT_SP_PERSON_XZQX> reposAreas, SellPersonnelPojoOut out) {
		
		if(null != in.getNBPT_SP_PERSON_PID() && !"".equals(in.getNBPT_SP_PERSON_PID())) {

			// 注入原PID
			person.setNBPT_SP_PERSON_PID(in.getNBPT_SP_PERSON_PID());
		} else {

			// 生成随机ID码
			person.setNBPT_SP_PERSON_PID(CommonUtil.getUUID_32());
		}

		if(null != in.getNBPT_SP_PERSON_ID() && !"".equals(in.getNBPT_SP_PERSON_ID())) {

			// 注入原ID
			person.setNBPT_SP_PERSON_ID(in.getNBPT_SP_PERSON_ID());
		} else {

			// 最大编号
			person.setNBPT_SP_PERSON_ID(out.getMaxId());
		}

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
		
		// 人员状态标志
		person.setNBPT_SP_PERSON_FLAG("2"); // TODO ,当前版本设为在职状态
		
		person.setNBPT_SP_PERSON_ENTERINGTIME(CommonUtil.getYYYYMMDD()); // 设置系统录入时间
		
	}
	

	
	/**
	 * 处理查询到的终端人员信息
	 * @param person
	 * @throws Exception
	 */
	public static void getChangePerson_dealCurrentPerson(NBPT_VIEW_CURRENTPERSON person){

		// 入职时间
		person.setNBPT_SP_PERSON_ENTRYDATA(CommonUtil.formateTimeToPage(person.getNBPT_SP_PERSON_ENTRYDATA()));
		
		// 保单开始时间
		person.setNBPT_SP_PERSON_POLICY_DATA1(CommonUtil.formateTimeToPage(person.getNBPT_SP_PERSON_POLICY_DATA1()));
		
		// 保单结束时间
		person.setNBPT_SP_PERSON_POLICY_DATA2(CommonUtil.formateTimeToPage(person.getNBPT_SP_PERSON_POLICY_DATA2()));
	}

	/**
	 * 处理处理大区下所有人信息
	 * @param person
	 * @throws Exception
	 */
	public static List<StatisticsTable> countClassifyList_byArea(List<NBPT_VIEW_REGION> regions, Map<String, List<NBPT_VIEW_CURRENTPERSON>> classifyiedPersons){
		
		// 初始化返回结果
		List<StatisticsTable> resultList = new ArrayList<>();
		
		for(NBPT_VIEW_REGION region : regions) {
			
			StatisticsTable table = new StatisticsTable();
			
			table.setProvinceName(region.getNBPT_SP_REGION_PROVINCE_NAME());
			
			table.setAreaName(region.getNBPT_SP_REGION_NAME());
			
			table.setNeed(CommonUtil.objToInteger(region.getNBPT_SP_REGION_NEED()));
			
			table.setAreaUid(region.getNBPT_SP_REGION_UID());
			
			List<NBPT_VIEW_CURRENTPERSON> currentAreaPersons = CommonServiceUtils.getPersonsByKey(classifyiedPersons, region.getNBPT_SP_REGION_UID());
			
			for(NBPT_VIEW_CURRENTPERSON person : currentAreaPersons) {
				
				CommonServiceUtils.count(table, person);
			}
			
			resultList.add(table);
		}
		
		CommonServiceUtils.otherCompute(resultList);
		
		return resultList;
	}
	
}













