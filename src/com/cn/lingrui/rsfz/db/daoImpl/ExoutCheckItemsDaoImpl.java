package com.cn.lingrui.rsfz.db.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cn.lingrui.common.db.dao.ExoutCheckItemsDao;
import com.cn.lingrui.common.db.daoImpl.BaseDaoImpl;
import com.cn.lingrui.common.db.dbpojos.PMBASE;
import com.cn.lingrui.common.utils.DBUtils;

@Repository("exoutCheckItemsDao")
public class ExoutCheckItemsDaoImpl extends BaseDaoImpl implements ExoutCheckItemsDao{


	private static Logger log = LogManager.getLogger();
	
	/**
	 * 根据年龄获取相关错误数据
	 * @throws SQLException 
	 */
	@Override
	public List<PMBASE> getCheckItems(String procName, Connection conn) throws SQLException {
		
		// 初始化返回数据
		List<PMBASE> resultList = new ArrayList<PMBASE>();
		
		ResultSet rs = this.callProcedure(procName, conn);
		
		try {
			
			resultList = DBUtils.rsToBean(PMBASE.class, rs);
			
		} catch (SQLException e) {
			
			log.info("数据转换出错" + e.getMessage());
		}
		return resultList;
	}

	
}
