package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dao.BaseDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson;

public interface PersonManageDao extends BaseDao{

	/**
	 * 获取当前登录人员的人员信息
	 * @param userId 登录ID
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public CurrentPerson receiveCurrentPerson(String userId, Connection conn) throws SQLException ;

	public List<NBPT_COMMON_XZQXHF> getXzqxhfs(String parentId, Connection connection) throws SQLException ;

	public void addTerminal(NBPT_SP_PERSON person, Connection conn) throws SQLException;

	public List<CurrentPerson> receiveCurrentTerminals(NBPT_SP_PERSON person, Connection conn) throws SQLException;

	/**
	 * 获取部门的方法
	 * @param level 1:查询所有大区  2:查询所有地区  3:查询全部
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_SP_REGION> getRegions(String level, Connection connection) throws SQLException;
	
	/**
	 * 添加终端人员负责区县数据
	 * @param reponseAreas 负责区县列表
	 * @param conn
	 * @throws SQLException
	 */
	public void addReposeAreas(List<NBPT_SP_PERSON_XZQX> reponseAreas, Connection conn) throws SQLException;

	/**
	 * 查询当前人员信息,地总和后勤权限
	 * @param nbpt_SP_PERSON
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<CurrentPerson> receiveCurrentPersonInfos(CurrentPerson nbpt_SP_PERSON, Connection connection) throws SQLException;

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

	public void addTerminalDeptId(String pid, String regionId, Connection connection) throws SQLException;

	public NBPT_SP_REGION getTerminalDeptInfo(String terminalId, Connection connection) throws SQLException;

	public CurrentPerson receiveCurrentTerminal(String changePersonPid, Connection connection) throws SQLException;

	void updateTerminal(NBPT_SP_PERSON person, Connection connection) throws SQLException;

	/**
	 * 根据身份证号码返回该身份证所在的市级行政划分
	 * @param idNum 身份证号码
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_COMMON_XZQXHF> checkPlace(String idNum, Connection connection) throws SQLException;

	/**
	 * 根据大区UID,查询大区下所有人员
	 * @param region_UID
	 * @param connection
	 * @return
	 * @throws SQLException 
	 */
	public List<CurrentPerson> receiveCurrentProvincePersonInfos(String region_UID, Connection connection) throws SQLException;

}
