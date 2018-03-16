package com.cn.lingrui.common.db.dao;

import java.sql.Connection;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_R_P;

public interface AuthorityDao extends BaseDao{
	
	public List<NBPT_RSFZ_R_P> getAuthorityList(Connection conn);
}
