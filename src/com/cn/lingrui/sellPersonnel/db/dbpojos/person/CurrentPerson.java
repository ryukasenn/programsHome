package com.cn.lingrui.sellPersonnel.db.dbpojos.person;

public class CurrentPerson {

	private String NBPT_SP_PERSON_REGION = ""; // 所属大区
	private String NBPT_SP_PERSON_BIRS = ""; // 生日
	private String NBPT_SP_PERSON_QQ = ""; // 扣扣
	private String NBPT_SP_PERSON_CHAT = ""; // 微信
	private String NBPT_SP_PERSON_MAIL = ""; // 邮箱
	private String NBPT_SP_PERSON_ENTRYDATA = ""; // 入职时间
	private String NBPT_SP_PERSON_LEAVEDATA = ""; // 离职时间
	private String NBPT_SP_PERSON_DEGREE = ""; // 学历
	private String NBPT_SP_PERSON_PLACE = ""; // 籍贯
	private String NBPT_SP_PERSON_SCHOOL = ""; // 毕业学校
	private String NBPT_SP_PERSON_PROFESS = ""; // 专业
	private String NBPT_SP_PERSON_TITLE = ""; // 职称
	private String NBPT_SP_PERSON_POLICYNO = ""; // 保单编号
	private String NBPT_SP_PERSON_POLICY_DATA1 = ""; // 保单开始日期
	private String NBPT_SP_PERSON_POLICY_DATA2 = ""; // 保单结束日期
	private String NBPT_SP_PERSON_AREANO = ""; // 区域编号
	private String NBPT_SP_PERSON_LOGINID = ""; // 登录名
	private String NBPT_SP_PERSON_NOTE = ""; // 备注
	private String NBPT_SP_PERSON_PID = ""; //32位随机码
	private String NBPT_SP_PERSON_ID = ""; // 用户编号
	private String NBPT_SP_PERSON_DEPT_ID = ""; // 部门编号
	private String NBPT_SP_PERSON_TYPE = ""; // 人员类型,RX,OTC,混合
	private String NBPT_SP_PERSON_NAME = ""; // 姓名
	private String NBPT_SP_PERSON_MALE = ""; // 性别
	private String NBPT_SP_PERSON_IDNUM = ""; // 身份证号码
	private String NBPT_SP_PERSON_MOB1 = ""; // 手机1
	private String NBPT_SP_PERSON_MOB2 = ""; // 手机2
	private String NBPT_SP_PERSON_JOB = ""; // 职务
	private String NBPT_SP_PERSON_FLAG = "0"; // 离职标志
	private String NBPT_SP_PERSON_POLICYTYPE = ""; // 保单类型
	private String NBPT_SP_PERSON_AGE; // 年龄
	private String NBPT_SP_REGION_XZQX_REGIONID; // 部门ID
	private String NBPT_SP_PERSON_WORKAGE; // 工龄
	
	
	// 后勤人员统计
	private String NBPT_SP_REGION_NAME; // 所在部门名称
	private String NBPT_SP_REGION_NEED; // 人员配额
	private String NBPT_SP_REGION_RESPONSIBLER; // 负责人
	private String NBPT_SP_REGION_ID; // 人员所在部门ID
	private String NBPT_SP_REGION_ONAME; // 部门配额
	private String NBPT_SP_REGION_LEVEL; // 部门级别
	private String REGION_NAME; // 大区名称
	private String REGION_ONAME; // 大区配额
	
	public String getNBPT_SP_PERSON_PID() {
		return NBPT_SP_PERSON_PID;
	}
	public void setNBPT_SP_PERSON_PID(String nBPT_SP_PERSON_PID) {
		NBPT_SP_PERSON_PID = nBPT_SP_PERSON_PID;
	}
	public String getNBPT_SP_PERSON_ID() {
		return NBPT_SP_PERSON_ID;
	}
	public void setNBPT_SP_PERSON_ID(String nBPT_SP_PERSON_ID) {
		NBPT_SP_PERSON_ID = nBPT_SP_PERSON_ID;
	}
	public String getNBPT_SP_PERSON_DEPT_ID() {
		return NBPT_SP_PERSON_DEPT_ID;
	}
	public void setNBPT_SP_PERSON_DEPT_ID(String nBPT_SP_PERSON_DEPT_ID) {
		NBPT_SP_PERSON_DEPT_ID = nBPT_SP_PERSON_DEPT_ID;
	}
	public String getNBPT_SP_PERSON_TYPE() {
		return NBPT_SP_PERSON_TYPE;
	}
	public void setNBPT_SP_PERSON_TYPE(String nBPT_SP_PERSON_TYPE) {
		NBPT_SP_PERSON_TYPE = nBPT_SP_PERSON_TYPE;
	}
	public String getNBPT_SP_PERSON_NAME() {
		return NBPT_SP_PERSON_NAME;
	}
	public void setNBPT_SP_PERSON_NAME(String nBPT_SP_PERSON_NAME) {
		NBPT_SP_PERSON_NAME = nBPT_SP_PERSON_NAME;
	}
	public String getNBPT_SP_PERSON_MALE() {
		return NBPT_SP_PERSON_MALE;
	}
	public void setNBPT_SP_PERSON_MALE(String nBPT_SP_PERSON_MALE) {
		NBPT_SP_PERSON_MALE = nBPT_SP_PERSON_MALE;
	}
	public String getNBPT_SP_PERSON_IDNUM() {
		return NBPT_SP_PERSON_IDNUM;
	}
	public void setNBPT_SP_PERSON_IDNUM(String nBPT_SP_PERSON_IDNUM) {
		NBPT_SP_PERSON_IDNUM = nBPT_SP_PERSON_IDNUM;
	}
	public String getNBPT_SP_PERSON_MOB1() {
		return NBPT_SP_PERSON_MOB1;
	}
	public void setNBPT_SP_PERSON_MOB1(String nBPT_SP_PERSON_MOB1) {
		NBPT_SP_PERSON_MOB1 = nBPT_SP_PERSON_MOB1;
	}
	public String getNBPT_SP_PERSON_MOB2() {
		return NBPT_SP_PERSON_MOB2;
	}
	public void setNBPT_SP_PERSON_MOB2(String nBPT_SP_PERSON_MOB2) {
		NBPT_SP_PERSON_MOB2 = nBPT_SP_PERSON_MOB2;
	}
	public String getNBPT_SP_PERSON_JOB() {
		return NBPT_SP_PERSON_JOB;
	}
	public void setNBPT_SP_PERSON_JOB(String nBPT_SP_PERSON_JOB) {
		NBPT_SP_PERSON_JOB = nBPT_SP_PERSON_JOB;
	}
	public String getNBPT_SP_PERSON_FLAG() {
		return NBPT_SP_PERSON_FLAG;
	}
	public void setNBPT_SP_PERSON_FLAG(String nBPT_SP_PERSON_FLAG) {
		NBPT_SP_PERSON_FLAG = nBPT_SP_PERSON_FLAG;
	}
	public String getNBPT_SP_PERSON_POLICYTYPE() {
		return NBPT_SP_PERSON_POLICYTYPE;
	}
	public void setNBPT_SP_PERSON_POLICYTYPE(String nBPT_SP_PERSON_POLICYTYPE) {
		NBPT_SP_PERSON_POLICYTYPE = nBPT_SP_PERSON_POLICYTYPE;
	}
	public String getNBPT_SP_PERSON_AGE() {
		return NBPT_SP_PERSON_AGE;
	}
	public void setNBPT_SP_PERSON_AGE(String nBPT_SP_PERSON_AGE) {
		NBPT_SP_PERSON_AGE = nBPT_SP_PERSON_AGE;
	}
	public String getNBPT_SP_REGION_XZQX_REGIONID() {
		return NBPT_SP_REGION_XZQX_REGIONID;
	}
	public void setNBPT_SP_REGION_XZQX_REGIONID(String nBPT_SP_REGION_XZQX_REGIONID) {
		NBPT_SP_REGION_XZQX_REGIONID = nBPT_SP_REGION_XZQX_REGIONID;
	}
	public String getNBPT_SP_PERSON_BIRS() {
		return NBPT_SP_PERSON_BIRS;
	}
	public void setNBPT_SP_PERSON_BIRS(String nBPT_SP_PERSON_BIRS) {
		NBPT_SP_PERSON_BIRS = nBPT_SP_PERSON_BIRS;
	}
	public String getNBPT_SP_PERSON_QQ() {
		return NBPT_SP_PERSON_QQ;
	}
	public void setNBPT_SP_PERSON_QQ(String nBPT_SP_PERSON_QQ) {
		NBPT_SP_PERSON_QQ = nBPT_SP_PERSON_QQ;
	}
	public String getNBPT_SP_PERSON_CHAT() {
		return NBPT_SP_PERSON_CHAT;
	}
	public void setNBPT_SP_PERSON_CHAT(String nBPT_SP_PERSON_CHAT) {
		NBPT_SP_PERSON_CHAT = nBPT_SP_PERSON_CHAT;
	}
	public String getNBPT_SP_PERSON_MAIL() {
		return NBPT_SP_PERSON_MAIL;
	}
	public void setNBPT_SP_PERSON_MAIL(String nBPT_SP_PERSON_MAIL) {
		NBPT_SP_PERSON_MAIL = nBPT_SP_PERSON_MAIL;
	}
	public String getNBPT_SP_PERSON_ENTRYDATA() {
		return NBPT_SP_PERSON_ENTRYDATA;
	}
	public void setNBPT_SP_PERSON_ENTRYDATA(String nBPT_SP_PERSON_ENTRYDATA) {
		NBPT_SP_PERSON_ENTRYDATA = nBPT_SP_PERSON_ENTRYDATA;
	}
	public String getNBPT_SP_PERSON_LEAVEDATA() {
		return NBPT_SP_PERSON_LEAVEDATA;
	}
	public void setNBPT_SP_PERSON_LEAVEDATA(String nBPT_SP_PERSON_LEAVEDATA) {
		NBPT_SP_PERSON_LEAVEDATA = nBPT_SP_PERSON_LEAVEDATA;
	}
	public String getNBPT_SP_PERSON_DEGREE() {
		return NBPT_SP_PERSON_DEGREE;
	}
	public void setNBPT_SP_PERSON_DEGREE(String nBPT_SP_PERSON_DEGREE) {
		NBPT_SP_PERSON_DEGREE = nBPT_SP_PERSON_DEGREE;
	}
	public String getNBPT_SP_PERSON_PLACE() {
		return NBPT_SP_PERSON_PLACE;
	}
	public void setNBPT_SP_PERSON_PLACE(String nBPT_SP_PERSON_PLACE) {
		NBPT_SP_PERSON_PLACE = nBPT_SP_PERSON_PLACE;
	}
	public String getNBPT_SP_PERSON_SCHOOL() {
		return NBPT_SP_PERSON_SCHOOL;
	}
	public void setNBPT_SP_PERSON_SCHOOL(String nBPT_SP_PERSON_SCHOOL) {
		NBPT_SP_PERSON_SCHOOL = nBPT_SP_PERSON_SCHOOL;
	}
	public String getNBPT_SP_PERSON_PROFESS() {
		return NBPT_SP_PERSON_PROFESS;
	}
	public void setNBPT_SP_PERSON_PROFESS(String nBPT_SP_PERSON_PROFESS) {
		NBPT_SP_PERSON_PROFESS = nBPT_SP_PERSON_PROFESS;
	}
	public String getNBPT_SP_PERSON_TITLE() {
		return NBPT_SP_PERSON_TITLE;
	}
	public void setNBPT_SP_PERSON_TITLE(String nBPT_SP_PERSON_TITLE) {
		NBPT_SP_PERSON_TITLE = nBPT_SP_PERSON_TITLE;
	}
	public String getNBPT_SP_PERSON_POLICYNO() {
		return NBPT_SP_PERSON_POLICYNO;
	}
	public void setNBPT_SP_PERSON_POLICYNO(String nBPT_SP_PERSON_POLICYNO) {
		NBPT_SP_PERSON_POLICYNO = nBPT_SP_PERSON_POLICYNO;
	}
	public String getNBPT_SP_PERSON_POLICY_DATA1() {
		return NBPT_SP_PERSON_POLICY_DATA1;
	}
	public void setNBPT_SP_PERSON_POLICY_DATA1(String nBPT_SP_PERSON_POLICY_DATA1) {
		NBPT_SP_PERSON_POLICY_DATA1 = nBPT_SP_PERSON_POLICY_DATA1;
	}
	public String getNBPT_SP_PERSON_POLICY_DATA2() {
		return NBPT_SP_PERSON_POLICY_DATA2;
	}
	public void setNBPT_SP_PERSON_POLICY_DATA2(String nBPT_SP_PERSON_POLICY_DATA2) {
		NBPT_SP_PERSON_POLICY_DATA2 = nBPT_SP_PERSON_POLICY_DATA2;
	}
	public String getNBPT_SP_PERSON_AREANO() {
		return NBPT_SP_PERSON_AREANO;
	}
	public void setNBPT_SP_PERSON_AREANO(String nBPT_SP_PERSON_AREANO) {
		NBPT_SP_PERSON_AREANO = nBPT_SP_PERSON_AREANO;
	}
	public String getNBPT_SP_PERSON_LOGINID() {
		return NBPT_SP_PERSON_LOGINID;
	}
	public void setNBPT_SP_PERSON_LOGINID(String nBPT_SP_PERSON_LOGINID) {
		NBPT_SP_PERSON_LOGINID = nBPT_SP_PERSON_LOGINID;
	}
	public String getNBPT_SP_PERSON_NOTE() {
		return NBPT_SP_PERSON_NOTE;
	}
	public void setNBPT_SP_PERSON_NOTE(String nBPT_SP_PERSON_NOTE) {
		NBPT_SP_PERSON_NOTE = nBPT_SP_PERSON_NOTE;
	}
	public String getNBPT_SP_PERSON_WORKAGE() {
		return NBPT_SP_PERSON_WORKAGE;
	}
	public void setNBPT_SP_PERSON_WORKAGE(String nBPT_SP_PERSON_WORKAGE) {
		NBPT_SP_PERSON_WORKAGE = nBPT_SP_PERSON_WORKAGE;
	}
	public String getNBPT_SP_PERSON_REGION() {
		return NBPT_SP_PERSON_REGION;
	}
	public void setNBPT_SP_PERSON_REGION(String nBPT_SP_PERSON_REGION) {
		NBPT_SP_PERSON_REGION = nBPT_SP_PERSON_REGION;
	}
	public String getNBPT_SP_REGION_NAME() {
		return NBPT_SP_REGION_NAME;
	}
	public void setNBPT_SP_REGION_NAME(String nBPT_SP_REGION_NAME) {
		NBPT_SP_REGION_NAME = nBPT_SP_REGION_NAME;
	}
	public String getNBPT_SP_REGION_NEED() {
		return NBPT_SP_REGION_NEED;
	}
	public void setNBPT_SP_REGION_NEED(String nBPT_SP_REGION_NEED) {
		NBPT_SP_REGION_NEED = nBPT_SP_REGION_NEED;
	}
	public String getNBPT_SP_REGION_RESPONSIBLER() {
		return NBPT_SP_REGION_RESPONSIBLER;
	}
	public void setNBPT_SP_REGION_RESPONSIBLER(String nBPT_SP_REGION_RESPONSIBLER) {
		NBPT_SP_REGION_RESPONSIBLER = nBPT_SP_REGION_RESPONSIBLER;
	}
	public String getNBPT_SP_REGION_ID() {
		return NBPT_SP_REGION_ID;
	}
	public void setNBPT_SP_REGION_ID(String nBPT_SP_REGION_ID) {
		NBPT_SP_REGION_ID = nBPT_SP_REGION_ID;
	}
	public String getNBPT_SP_REGION_ONAME() {
		return NBPT_SP_REGION_ONAME;
	}
	public void setNBPT_SP_REGION_ONAME(String nBPT_SP_REGION_ONAME) {
		NBPT_SP_REGION_ONAME = nBPT_SP_REGION_ONAME;
	}
	public String getNBPT_SP_REGION_LEVEL() {
		return NBPT_SP_REGION_LEVEL;
	}
	public void setNBPT_SP_REGION_LEVEL(String nBPT_SP_REGION_LEVEL) {
		NBPT_SP_REGION_LEVEL = nBPT_SP_REGION_LEVEL;
	}
	public String getREGION_NAME() {
		return REGION_NAME;
	}
	public void setREGION_NAME(String rEGION_NAME) {
		REGION_NAME = rEGION_NAME;
	}
	public String getREGION_ONAME() {
		return REGION_ONAME;
	}
	public void setREGION_ONAME(String rEGION_ONAME) {
		REGION_ONAME = rEGION_ONAME;
	}
	
}
