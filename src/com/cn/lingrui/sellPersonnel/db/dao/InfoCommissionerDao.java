package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.infoCommissioner.UncheckPerson;

public interface InfoCommissionerDao extends SellPersonnelBaseDao{

	public List<UncheckPerson> receiveUnchecks(String userId, Connection conn) throws SQLException;

	public NBPT_SP_PERSON receiveUncheck(String uncheckpid, Connection conn) throws SQLException;

	/**
	 * 获取终端人员负责区域
	 * @param uncheckpid
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_COMMON_XZQXHF> receiveTerminalResponsAreas(String terminalPid, Connection connection) throws SQLException;

	/**
	 * 根据不同参数,查询信息专员管理下人员信息
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
	
}
