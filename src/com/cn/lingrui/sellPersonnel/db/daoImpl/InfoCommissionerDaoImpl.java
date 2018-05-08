package com.cn.lingrui.sellPersonnel.db.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.cn.lingrui.common.db.dbpojos.NBPT_COMMON_XZQXHF;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.sellPersonnel.db.dao.InfoCommissionerDao;

@Repository("infoCommissionerDao")
public class InfoCommissionerDaoImpl extends SellPersonnelBaseDaoImpl implements InfoCommissionerDao {

	private static Logger log = LogManager.getLogger();

	@Override
	public List<NBPT_COMMON_XZQXHF> receiveTerminalResponsAreas(String terminalPid, Connection connection) throws SQLException {

		String sql =    "  SELECT C.*" + 
						"  FROM NBPT_SP_PERSON_XZQX A" + 
						"  LEFT JOIN NBPT_SP_PERSON B" + 
						"  ON A.NBPT_SP_PERSON_XZQX_PID = B.NBPT_SP_PERSON_ID" + 
						"  LEFT JOIN NBPT_COMMON_XZQXHF C" + 
						"  ON A.NBPT_SP_PERSON_XZQX_XID = C.NBPT_COMMON_XZQXHF_ID" + 
						"  WHERE B.NBPT_SP_PERSON_PID = '" + terminalPid + "'";
		try {
			
			
			List<NBPT_COMMON_XZQXHF> respons = this.queryForClaszs(sql, connection, NBPT_COMMON_XZQXHF.class);
			
			return respons;
		} catch (SQLException e) {
			
			log.error("查询终端人员负责区域" + CommonUtil.getTraceInfo());
			throw new SQLException();
		}
		
	}

	
	

}
























