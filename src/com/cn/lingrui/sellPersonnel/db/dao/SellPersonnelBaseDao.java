package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dao.BaseDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;

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
	 * 获取部门信息
	 * @param parentUid 指定上级部门编号,可空,查所有
	 * @param provinceId 指定所在省份ID,可空,查所有
	 * @param type 可选参数,如果type==1:包含上级部门信息
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_REGION> receiveRegion(String parentUid, String provinceId, Connection conn, Integer... type) throws SQLException;

	/**
	 * 获取指定部门信息
	 * @param regionUid 指定部门编号
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public NBPT_VIEW_REGION receiveRegion(String regionUid, Connection conn) throws SQLException;
	
	/**
	 * 获取指定多部门信息
	 * @param regionUids 指定部门编号列表
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_REGION> receiveRegion(List<String> regionUids, Connection conn) throws SQLException;
	
	/**
	 * 获取指定多部门信息
	 * @param regionUids 指定部门编号列表
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_REGION> receiveRegion(String[] regionUids, Connection conn) throws SQLException;

	/**
	 * 根据不同参数,查询终端人员
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
	 * 获取终端人员负责区域
	 * @param uncheckpid
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_COMMON_XZQXHF> receiveTerminalResponsAreas(String terminalPid, Connection connection) throws SQLException;
	
	/**
	 * 通过负责人查询大区信息
	 * @param nbpt_SP_PERSON_PID 负责人PID
	 * @param type 1:查询大区,2:查询地区...
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public NBPT_VIEW_REGION receiveRegionByResper(String nbpt_SP_PERSON_PID, String type, Connection connection) throws SQLException;

}
