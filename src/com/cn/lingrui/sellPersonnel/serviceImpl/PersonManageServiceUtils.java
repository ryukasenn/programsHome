package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson_statistics;
import com.cn.lingrui.sellPersonnel.pojos.AddPersonPojoIn;
import com.cn.lingrui.sellPersonnel.pojos.SellPersonnelPojoOut;

public class PersonManageServiceUtils {


	/**
	 *  后勤人员查询合计信息处理
	 * @param regionList 
	 * @return
	 */
	public static List<CurrentPerson_statistics> dealCurrentPerson_total(List<CurrentPerson> personInfos, List<NBPT_SP_REGION> regionList){
		
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
				
				if("2".equals(type)) {
					
					Integer need = 0;
					
					// 计算OTC合计配额数
					for(NBPT_SP_REGION region : regionList) {
						
						need += Integer.valueOf(region.getNBPT_SP_REGION_ONAME());
					}				
					thisInfo.setNeed(need);
				}
				addPerson_total(thisInfo, person);
				resultList.add(thisInfo);
			}
		}

		for(CurrentPerson_statistics info : resultList) {
			
			Integer balance = info.getNeed() - info.getTotal();
			
			if(0 < balance) {
				
				info.setBalance("差" + balance + "人");
			} else if(0 == balance) {

				info.setBalance("满配");
			} else if(0 > balance) {
				
				info.setBalance("超出" + Math.abs(balance) + "人");
			}
		}
		return resultList;
	}
	
	/**
	 * 后勤人员查询OTC统计信息处理
	 * @return
	 */
	public static List<CurrentPerson_statistics> dealCurrentPerson_region(List<CurrentPerson> personInfos){
		
		// 校验list
		List<String> checkItems = new ArrayList<>();
		List<CurrentPerson_statistics> resultList = new ArrayList<>(); 
		
		for(CurrentPerson person : personInfos) {
			
			
			if("2".equals(person.getNBPT_SP_PERSON_TYPE())) {

				String regionId = person.getNBPT_SP_REGION_ID().substring(0, 2);
				// 按大区添加信息
				if(checkItems.contains(regionId)) {
					
					CurrentPerson_statistics thisInfo = resultList.get(checkItems.indexOf(regionId));
					
					// 添加信息
					addPerson_otc(thisInfo, person);
				} else {

					// 加入检查列表
					checkItems.add(regionId);
					
					// 新增当前信息
					CurrentPerson_statistics thisInfo = new CurrentPerson_statistics();

					thisInfo.setName(person.getREGION_NAME());
					thisInfo.setNeed(Integer.valueOf(person.getREGION_ONAME() == null || "".equals(person.getREGION_ONAME()) ? "0" : person.getREGION_ONAME()));
					addPerson_otc(thisInfo, person);
					resultList.add(thisInfo);
				}
			}
		}

		
		for(CurrentPerson_statistics info : resultList) {
			
			Integer balance = info.getNeed() - info.getTotal();
			
			if(0 < balance) {
				
				info.setBalance("差" + balance + "人");
			} else if(0 == balance) {

				info.setBalance("满配");
			} else if(0 > balance) {
				
				info.setBalance("超出" + Math.abs(balance) + "人");
			}
		}
		return resultList;
	}
	/**
	 * 统计OTC信息辅助方法
	 * @param thisInfo
	 * @param person
	 */
	private static void addPerson_otc(CurrentPerson_statistics thisInfo, CurrentPerson person) {
		
		String job = person.getNBPT_SP_PERSON_JOB();
		
		if("3".equals(person.getNBPT_SP_PERSON_FLAG())) {
			
			// 离职合计数
			thisInfo.setDismission(thisInfo.getDismission() + 1);
			
			return;
		}
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
		
		if("23".equals(job) || "24".equals(job) || "25".equals(job)) {

			// 添加合计数
			thisInfo.setTotal(thisInfo.getTotal()+ 1);
		} else {

		}
		
		
	}
	

	/**
	 * 统计合计信息辅助方法
	 * @param thisInfo
	 * @param person
	 */
	private static void addPerson_total(CurrentPerson_statistics thisInfo,CurrentPerson person) {
		
		String type = person.getNBPT_SP_PERSON_TYPE();
		// 如果是商务的
		if("1".equals(type)) {

			if("1".equals(person.getNBPT_SP_PERSON_FLAG())) {
				
				thisInfo.setDismission(thisInfo.getDismission() + 1);
				
				return;
			}
			thisInfo.setName("商务");
			// TODO
			thisInfo.setAreaResper(thisInfo.getAreaResper() + 1);
		} 
		
		// 如果是OTC
		else if("2".equals(type)) {
			
			thisInfo.setName("OTC");
			
			if("1".equals(person.getNBPT_SP_PERSON_FLAG())) {
				
				thisInfo.setDismission(thisInfo.getDismission() + 1);
				
				return;
			}
			
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
				
			case "26": // 如果是混合

				thisInfo.setRegionResper(thisInfo.getRegionResper() + 1);
				break;
				
			default:
				break;			
			}

			// 如果是地总
			if("23".equals(job) || "24".equals(job) || "25".equals(job)) {

				// 添加合计数
				thisInfo.setTotal(thisInfo.getTotal()+ 1);
			} else {

			}
			
		}
		
		// 如果是临床
		else if("3".equals(type)) {

			thisInfo.setName("临床");
			if("1".equals(person.getNBPT_SP_PERSON_FLAG())) {
				
				thisInfo.setDismission(thisInfo.getDismission() + 1);
				
				return;
			}
			// TODO
			thisInfo.setAreaResper(thisInfo.getAreaResper() + 1);
		}
		
		// 如果是诊所
		else if("4".equals(type)) {

			thisInfo.setName("诊所");
			if("1".equals(person.getNBPT_SP_PERSON_FLAG())) {
				
				thisInfo.setDismission(thisInfo.getDismission() + 1);
				
				return;
			}
			// TODO
			thisInfo.setAreaResper(thisInfo.getAreaResper() + 1);			
		}
	}
	
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
		person.setNBPT_SP_PERSON_ENTRYDATA(in.getNBPT_SP_PERSON_ENTRYDATA().replaceAll("-", ""));
		
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
		
		// 人员状态标志
		person.setNBPT_SP_PERSON_FLAG("2"); // TODO ,当前版本设为在职状态
		
	}
	
	/**
	 * 处理当前登录人员信息
	 * 
	 * @param persons
	 * @param out
	 * @throws Exception
	 */
	public static void dealCurrentPerson(CurrentPerson person, SellPersonnelPojoOut out) throws Exception {

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
	 * 处理查询到的终端人员信息
	 * @param person
	 * @throws Exception
	 */
	public static void getChangePerson_dealCurrentPerson(CurrentPerson person){

		
		// 入职时间
		person.setNBPT_SP_PERSON_ENTRYDATA(CommonUtil.formateTimeToPage(person.getNBPT_SP_PERSON_ENTRYDATA()));
		
		// 保单开始时间
		person.setNBPT_SP_PERSON_POLICY_DATA1(CommonUtil.formateTimeToPage(person.getNBPT_SP_PERSON_POLICY_DATA1()));
		
		// 保单结束时间
		person.setNBPT_SP_PERSON_POLICY_DATA2(CommonUtil.formateTimeToPage(person.getNBPT_SP_PERSON_POLICY_DATA2()));
	}
}
