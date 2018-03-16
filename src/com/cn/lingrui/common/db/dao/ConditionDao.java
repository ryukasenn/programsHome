package com.cn.lingrui.common.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_CONDITION;

/**
 * RSFZCONDITON表数据获取
 * @author lhc
 *
 */
public interface ConditionDao extends BaseDao{

	public List<NBPT_RSFZ_CONDITION> receiveCondition(Connection conn) throws SQLException;
}
