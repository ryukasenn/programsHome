package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
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


	
}
