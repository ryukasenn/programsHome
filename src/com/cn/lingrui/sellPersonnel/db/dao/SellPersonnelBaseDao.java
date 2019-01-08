package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dao.BaseDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_RECORD;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_XZQX;

public interface SellPersonnelBaseDao extends BaseDao{

	/**
	 * 获取登录人员信息
	 * @param loginId
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public NBPT_VIEW_CURRENTPERSON receiveLoginPerson(String loginId, Connection conn) throws SQLException;
	
	/**
	 * 获取下级部门信息
	 * @param parentUid 指定上级部门编号,可空,查所有
	 * @param provinceId 指定所在省份ID,可空,查所有
	 * @param type 可选参数,如果type==1:包含上级部门信息
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_REGION> receiveRegion(String parentUid, String provinceId, Connection conn) throws SQLException;

	/**
	 * 获取指定UID部门信息
	 * @param regionUid 指定部门编号,不可为空
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public NBPT_VIEW_REGION receiveRegion(String regionUid, Connection conn) throws SQLException;
	
	/**
	 * 获取指定条件下所有部门信息
	 * @param parentUid
	 * @param province
	 * @param level
	 * @param type
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_VIEW_REGION> receiveRegions(String parentUid, String province, String level, String type, Connection conn) throws SQLException;


	/**
	 * 获取指定名称部门信息(模糊查询)
	 * @param regionName 指定部门名称
	 * @param level 部门级别
	 * @param provinceId 省份
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_REGION> receiveRegion(String regionName, String level, String provinceId, Connection conn) throws SQLException;
	
	/**
	 * 获取指定多UID部门信息
	 * @param regionUids 指定部门编号列表,如果为空,查询所有
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_REGION> receiveRegion(List<String> regionUids, Connection conn) throws SQLException;
	
	/**
	 * 获取指定多UID部门信息
	 * @param regionUids 指定部门编号列表
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_REGION> receiveRegion(String[] regionUids, Connection conn) throws SQLException;

	/**
	 * 根据不同参数,查询OTC终端人员
	 * @param regionUid 大区ID,其他为空,查询当前大区下所有人员信息
	 * @param provinceId 省份ID,其他为空,查询选定省份下所有人员信息
	 * @param AreaId 地区ID,其他为空,查询选定地区下所有人员信息
	 * @param terminalPid 终端ID,其他为空,查询特定终端信息
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_CURRENTPERSON> receiveTerminal(String regionUid, String provinceId, String AreaId, String terminalPid,
			Connection connection) throws SQLException;
	
	/**
	 * 查询指定人员
	 * @param regionUid 查询特定人员信息
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public NBPT_VIEW_CURRENTPERSON receivePerson(String personPid, Connection connection) throws SQLException;
	
	/**
	 * 查询指定人员
	 * @param regionUid 查询特定人员信息
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_CURRENTPERSON> receivePerson(String[] personPids, Connection connection) throws SQLException;
	
	/**
	 * 根据职位和类型查询人员信息
	 * @param personJob 职位,如果为null,查所有职位
	 * @param personName 人员姓名
	 * @param personType 类型,如果为空,查所有类型
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_CURRENTPERSON> receivePerson(String personJob, String personName, String personType, Connection connection) throws SQLException;
	
	/**
	 * 根据多职位和类型查询人员信息
	 * @param personJob 职位,如果为null,查所有职位
	 * @param personName 人员姓名
	 * @param personType 类型,如果为空,查所有类型
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_CURRENTPERSON> receivePerson(String[] personJob, String personName, String personType, Connection connection) throws SQLException;
	
	/**
	 * 根据多职位和类型查询人员信息
	 * @param personJob 职位,如果为null,查所有职位
	 * @param personName 人员姓名
	 * @param personType 类型,如果为空,查所有类型
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_CURRENTPERSON> receivePerson(List<String> personJob, String personName, String personType, Connection connection) throws SQLException;
	
	/**
	 * 根据部门查询人员信息
	 * @param regionUid 大区UID
	 * @param areaUid 地区UID
	 * @param provinceId 省份Id
	 * @param personType 分类ID
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_CURRENTPERSON> receivePersonByDepart(String regionUid, String areaUid, String provinceId, String personType, Connection connection) throws SQLException;
	
	/**
	 * 获取终端人员负责区域
	 * @param uncheckpid
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_COMMON_XZQXHF> receiveTerminalResponsAreas(String terminalPid, Connection connection) throws SQLException;
	
	/**
	 * 获取行政区县信息
	 * @param uncheckpid
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public NBPT_COMMON_XZQXHF receiveXzqx(String xzqxId, Connection connection) throws SQLException;
	
	/**
	 * 通过负责人查询大区信息
	 * @param nbpt_SP_PERSON_PID 负责人PID
	 * @param type 1:查询大区,2:查询地区...
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public NBPT_VIEW_REGION receiveRegionByResper(String nbpt_SP_PERSON_PID, String type, Connection connection) throws SQLException;
	
	/**
	 * 查询指定国家行政区县划分
	 * @param xzqxId 行政区县ID,根据行政区县编号查询
	 * @param type 绑定类型,若不为空,则添加与部门的绑定类型:11,大区省份,21,地区省份,22,地区区县
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public NBPT_VIEW_XZQX receiveXzqx(String xzqxId, String type, Connection connection) throws SQLException;
	
	/**
	 * 查询指定国家行政区县划分信息
	 * @param xzqxId 行政区县IDs,如果为空,查所有
	 * @param level 行政区县级别1:省,2:市,3:县级
	 * @param type 绑定类型 11,大区省份,21,省份地区,22地区区县
	 * @param connection 
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_VIEW_XZQX> receiveXzqxs(String[] xzqxIds, String level, String type, Connection connection) throws SQLException;
	
	/**
	 * 根据省份查询国家行政区县划分信息
	 * @param provinceId 省份ID
	 * @param connection 
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_VIEW_XZQX> receiveXzqxsByProvince(String provinceId, Connection connection) throws SQLException;
	
	/**
	 * 根据地区ID查询地区行政区县绑定关系
	 * @param xzqxId 地区UID
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_VIEW_XZQX> receiveRegionXzqx(String regionUid, Connection connection) throws SQLException;
	
	/**
	 * 修改终端人员状态
	 * @param uncheckPid
	 * @param type 0:离职审核,1:录入审核,2:在职,3:离职
	 * @throws SQLException 
	 */
	public void changeTerminalState(String uncheckPid, String type, Connection conn, String... leaveTime) throws SQLException;

	/**
	 * 添加操作记录
	 * @param target 目标
	 * @param by 操作人
	 * @param type  1:地总添加终端申请
					2:地总添加终端通过
					3:地总下终端离职申请
					4:地总下终端离职通过
					5:后勤删除终端
					6:后勤添加地区
					7:后勤添加大区
					8:后勤添加大区总地总
					等等等
	 * @throws SQLException 
	 */
	public void addRecord(String target, String by, String type, Connection conn, String... note) throws SQLException;

	/**
	 * 获取操作记录
	 * @param target 目标PID
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public NBPT_SP_RECORD getRecord(String target, Connection conn) throws SQLException;
	
	/**
	 * 根据人员PID,获取单条人员信息
	 * @param personPid
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public NBPT_SP_PERSON receiveSpPerson(String personPid, Connection conn) throws SQLException;

	/**
	 * 获取国家行政区县
	 * @param xzqxId 指定多ID,为null查所有
	 * @param level 指定多级别,为null查所有
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_COMMON_XZQXHF> receiveCommonXzqxs(List<String> xzqxId, String level, Connection connection) throws SQLException;

	/**
	 * 获取人员部门负责关系
	 * @param regionUId 根据部门UID查询负责关系
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public NBPT_SP_PERSON_REGION receivePersonRegionResper(String regionUId, Connection connection) throws SQLException;
	
	/**
	 * 获取人员部门从属关系
	 * @param personPid 根据人员ID查询从属关系
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public NBPT_SP_PERSON_REGION receivePersonRegion(String personPid, Connection connection) throws SQLException;

	/**
	 * 获取部门信息
	 * @param uid
	 * @param responsiblerPid
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public NBPT_SP_REGION receiveSpRegion(String uid, String responsiblerPid, Connection conn) throws SQLException;
	
	/**
	 * 删除指定终端
	 * @param terminalPid
	 * @throws SQLException 
	 */
	public void deleteTerminal(String terminalPid, Connection conn) throws SQLException;
	
	/**
	 * 删除指定终端绑定信息
	 * @param terminalPid
	 * @throws SQLException 
	 */
	public void deletePersonRegion(String terminalPid, Connection conn) throws SQLException;
	

}
