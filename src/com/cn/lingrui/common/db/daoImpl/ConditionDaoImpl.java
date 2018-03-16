package com.cn.lingrui.common.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.dao.ConditionDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_CONDITION;

@Repository("conditionDao")
public class ConditionDaoImpl extends BaseDaoImpl implements ConditionDao{

	@Override
	public List<NBPT_RSFZ_CONDITION> receiveCondition(Connection conn) throws SQLException {

		String sql = "SELECT * FROM RSFZCONDITION ORDER BY RSFZCONDITION_TYPE+0 ASC,RSFZCONDITION_ITEM+0 ASC ";
		List<NBPT_RSFZ_CONDITION> resultList = this.query(sql, conn, NBPT_RSFZ_CONDITION.class);
		return resultList;
	}

}
