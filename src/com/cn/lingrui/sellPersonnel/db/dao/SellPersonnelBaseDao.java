package com.cn.lingrui.sellPersonnel.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cn.lingrui.common.db.dao.BaseDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_CURRENTPERSON;
import com.cn.lingrui.sellPersonnel.db.dbpojos.NBPT_VIEW_REGION;
import com.cn.lingrui.sellPersonnel.db.dbpojos.person.CurrentPerson;

public interface SellPersonnelBaseDao extends BaseDao{

	/**
	 * 获取当前人员
	 * @param loginId
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public CurrentPerson receiveCurrentPerson(String loginId, Connection conn) throws SQLException;
	
	/**
	 * 获取登录人员信息
	 * @param loginId
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public NBPT_VIEW_CURRENTPERSON receiveLoginPerson(String loginId, Connection conn) throws SQLException;
	
	/**
	 * 获取部门信息
	 * @param parentUid 指定上级部门编号
	 * @param provinceId 指定所在省份ID
	 * @param provinceId 指定所在省份ID
	 * @param type 如果type==1:包含上级部门信息
	 * @return
	 * @throws SQLException
	 */
	public List<NBPT_VIEW_REGION> receiveRegion(String parentUid, String provinceId, Connection conn, Integer... type) throws SQLException;

	/**
	 * 获取指定部门信息
	 * @param regionUid 指定部门编号
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	NBPT_VIEW_REGION receiveRegion(String regionUid, Connection conn) throws SQLException;
}
