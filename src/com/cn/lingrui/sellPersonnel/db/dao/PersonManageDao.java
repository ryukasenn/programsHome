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

	public CurrentPerson receiveCurrentPerson(String userId, Connection conn) throws SQLException ;

	public List<NBPT_COMMON_XZQXHF> getXzqxhfs(String parentId, Connection connection) throws SQLException ;

	public void addTerminal(NBPT_SP_PERSON person, Connection conn) throws SQLException;

	public List<CurrentPerson> receiveCurrentTerminals(NBPT_SP_PERSON person, Connection conn) throws SQLException;

	public List<NBPT_SP_REGION> getRegions(Object object, Connection connection) throws SQLException;
	
	public void addReposeAreas(List<NBPT_SP_PERSON_XZQX> reponseAreas, Connection conn) throws SQLException;

	public List<CurrentPerson> receiveCurrentPersonInfos(CurrentPerson nbpt_SP_PERSON, Connection connection) throws SQLException;

	public List<NBPT_COMMON_XZQXHF> getAreaSelects(String loginId, Connection connection) throws SQLException;

	public List<NBPT_COMMON_XZQXHF> getAreasSelect(String areaId, Connection connection) throws SQLException;

	public List<NBPT_COMMON_XZQXHF> getTerminalResponsXzqx(String terminalId, Connection connection) throws SQLException;

	public void addTerminalDeptId(String pid, String regionId, Connection connection) throws SQLException;

	public NBPT_SP_REGION getTerminalDeptInfo(String terminalId, Connection connection) throws SQLException;

	public CurrentPerson receiveCurrentTerminal(String changePersonPid, Connection connection) throws SQLException;

	void updateTerminal(NBPT_SP_PERSON person, Connection connection) throws SQLException;

}
