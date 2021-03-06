package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;

public interface PersonManageDao extends SellPersonnelBaseDao{

	/**
	 * 获取国家行政区县划分列表,按层级划分
	 */
	public List<NBPT_COMMON_XZQXHF> getXzqxhfs(String parentId, Connection connection) throws SQLException ;

	/**
	 * 添加终端人员
	 * @param person
	 * @param conn
	 * @throws SQLException
	 */
	public void addTerminal(NBPT_SP_PERSON person, Connection conn) throws SQLException;

	/**
	 * 更新终端人员信息
	 * @param person
	 * @param connection
	 * @throws SQLException
	 */
	public void updateTerminal(NBPT_SP_PERSON person, Connection connection) throws SQLException;
	
	/**
	 * 添加终端人员负责区县数据
	 * @param reponseAreas 负责区县列表
	 * @param conn
	 * @throws SQLException
	 */
	public void addReposeAreas(List<NBPT_SP_PERSON_XZQX> reponseAreas, Connection conn) throws SQLException;


	/**
	 * 获取地总下辖行政区县
	 * @param loginId
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_COMMON_XZQXHF> getAreaSelects(String loginId, Connection connection) throws SQLException;

	public List<NBPT_COMMON_XZQXHF> getAreasSelect(String areaId, Connection connection) throws SQLException;

	/**
	 * AJAX获取终端负责行政区县方法
	 * @param terminalId 终端32位PID
	 * @param connection 
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_COMMON_XZQXHF> getTerminalResponsXzqx(String terminalPId, Connection connection) throws SQLException;


	/**
	 * 根据身份证号码返回该身份证所在的市级行政划分
	 * @param idNum 身份证号码
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_COMMON_XZQXHF> checkPlace(String idNum, Connection connection) throws SQLException;

	/**
	 * 获取登录号为loginId的用户
	 * @param loginId
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<NBPT_VIEW_CURRENTPERSON> receiveLoginId(String loginId, Connection connection) throws SQLException;

	/**
	 * 删除终端人员负责区域
	 * @param terminalId 终端人员ID
	 * @return
	 * @throws SQLException 
	 */
	public void deleteTerminalAreas(String terminalId, Connection connection) throws SQLException;

	/**
	 * 添加人员部门绑定关系
	 * @param insertPersonRegion
	 * @param connection
	 * @throws SQLException 
	 */
	public void addPersonRegion(NBPT_SP_PERSON_REGION insertPersonRegion, Connection connection) throws SQLException;



}
