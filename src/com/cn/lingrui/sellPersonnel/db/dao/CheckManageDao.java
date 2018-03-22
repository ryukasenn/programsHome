package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.sellPersonnel.db.dbpojos.check.UncheckPerson;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_SP_PERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.check.Province;
import com.cn.lingrui.sellPersonnel.db.dbpojos.check.Region;

public interface CheckManageDao {

	public List<UncheckPerson> receiveUnchecks(String userId, Connection conn) throws SQLException;

	public NBPT_SP_PERSON receiveUncheck(String uncheckpid, Connection conn) throws SQLException;

	public List<NBPT_COMMON_XZQXHF> receiveUncheckResponsAreas(String uncheckpid, Connection connection) throws SQLException;

	
}
