package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON_XZQX;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson;

public interface PersonManageDao extends SellPersonnelBaseDao{

	public List<NBPT_COMMON_XZQXHF> getXzqxhfs(String parentId, Connection connection) throws SQLException ;

	public void addTerminal(NBPT_SP_PERSON person, Connection conn) throws SQLException;

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

	public void addTerminalDeptId(String pid, String regionId, Connection connection) throws SQLException;

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

	public List<CurrentPerson> receiveCurrentPersonInfos(String areaUid, Connection connection) throws SQLException;

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
