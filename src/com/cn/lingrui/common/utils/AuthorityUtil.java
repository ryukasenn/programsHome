package com.cn.lingrui.common.utils;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.db.dao.AuthorityDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_R_P;
import com.cn.lingrui.common.pojos.authority.AuthorityPojo;

import net.sf.ehcache.Element;

public class AuthorityUtil {

	private static Logger log = LogManager.getLogger();
	
	/**
	 * 获取用户可许页面
	 * @param role 角色
	 * @param content 页面模块功能编号
	 * @param conn 数据库连接
	 * @param authorDao 数据库查询dao
	 * @param type 二次查询标识,缓存不存在时才会迭代
	 * @return
	 */
	public static String rightCheck(String role, String content, Connection conn, AuthorityDao authorDao, String... type) {

		if("admin".equals(role)) {
			
			return content + "01";
		}
		Element el = CacheUtil.getURCacheContent(role);

		if (null == el) {

			if(0 != type.length) {
				
				log.debug("页面权限2次查询,查询出错");
				return "";
			}
			// 如果缓存中不存在该角色的权限信息,重新查询数据库
			List<NBPT_RSFZ_R_P> authorList = authorDao.getAuthorityList(conn);
			dealAuthorityList(authorList);
			return rightCheck(role, content, conn, authorDao, "second");
		} else {
			
			AuthorityPojo authorityPojo = (AuthorityPojo) el.getObjectValue();

			for(String page : authorityPojo.getPageRight()) {
				
				if(page.substring(0, 6).equals(content)) {
					
					return page;
				}
			}
		}

		return "";
	}
	
	/**
	 * 角色模块权限列表
	 * @param role 角色
	 * @param conn 数据库连接
	 * @param authorDao
	 * @param type
	 * @return
	 */
	public static List<String> moduleList(String role,  Connection conn, AuthorityDao authorDao, String... type){

		// 获取缓存角色权限信息
		Element el = CacheUtil.getURCacheContent(role);

		if (null == el) {

			if(0 != type.length) {
				
				log.debug("模块权限2次查询,查询出错");
				return null;
			}
			// 如果缓存中不存在该角色的权限信息,重新查询数据库
			List<NBPT_RSFZ_R_P> authorList = authorDao.getAuthorityList(conn);
			dealAuthorityList(authorList);
			return moduleList(role, conn, authorDao, "second");
		} else {

			AuthorityPojo authorityPojo = (AuthorityPojo) el.getObjectValue();
			return authorityPojo.getModuleRight();
		}		
	}

	/**
	 * 角色功能权限列表
	 * @param role 角色
	 * @param conn 数据库连接
	 * @param authorDao
	 * @param type
	 * @return
	 */
	public static List<String> functionRightLsit(String role, Connection conn, AuthorityDao authorDao, String... type){
		
		// 获取缓存角色权限信息
		Element el = CacheUtil.getURCacheContent(role);

		if (null == el) {

			if(0 != type.length) {
				
				log.debug("角色功能2次查询,查询出错");
				return null;
			}
			// 如果缓存中不存在该角色的权限信息,重新查询数据库
			List<NBPT_RSFZ_R_P> authorList = authorDao.getAuthorityList(conn);
			dealAuthorityList(authorList);
			return functionRightLsit(role, conn, authorDao, "second");
		} else {

			AuthorityPojo authorityPojo = (AuthorityPojo) el.getObjectValue();
			return authorityPojo.getFunctionRight();
		}		
	}

	/**
	 * 更新权限
	 * @param authorDao
	 * @param conn
	 */
	public static void updateAuthority(AuthorityDao authorDao, Connection conn) {
		
		// 1.删除当前缓存权限
		CacheUtil.removeURCacheContentAll();
		
		// 2.查询新的缓存
		List<NBPT_RSFZ_R_P> authorList = authorDao.getAuthorityList(conn);
		
		// 3.更新缓存
		dealAuthorityList(authorList);
	}
	
	/**
	 * 处理查询到的数据,放入缓存中
	 * @param authorList
	 */
	private static void dealAuthorityList(List<NBPT_RSFZ_R_P> authorList) {
		
		// 初始化模块-功能页面处理结果
		Map<String, AuthorityPojo> rightMap = new HashMap<String, AuthorityPojo>();
		
		// 首先将数据按角色分成三类,一类是模块,一类是功能页面,一类是具体页面
		for(NBPT_RSFZ_R_P rp : authorList) {
			
			// 如果rightMap中不包含该角色,则添加至rightMap中,并记录
			if(!rightMap.containsKey(rp.getNBPT_RSFZ_R_P_RID())) {

				// 新建分类
				AuthorityPojo authorityPojo = new AuthorityPojo();

				authorityPojo.setRoleId(rp.getNBPT_RSFZ_R_P_RID());
				// 0表示模块
				if("0".equals(rp.getNBPT_RSFZ_R_P_PTYPE())) {
					
					authorityPojo.getModuleRight().add(rp.getNBPT_RSFZ_R_P_PID());
				} 
				// 1表示功能
				else if ("1".equals(rp.getNBPT_RSFZ_R_P_PTYPE())) {
					
					authorityPojo.getFunctionRight().add(rp.getNBPT_RSFZ_R_P_PID());
				}
				// 2表示功能
				else if ("2".equals(rp.getNBPT_RSFZ_R_P_PTYPE())) {
					
					authorityPojo.getPageRight().add(rp.getNBPT_RSFZ_R_P_PID());
				}
				
				rightMap.put(rp.getNBPT_RSFZ_R_P_RID(), authorityPojo);
			} else {
				
				AuthorityPojo currentPojo = rightMap.get(rp.getNBPT_RSFZ_R_P_RID());

				// 0表示模块
				if("0".equals(rp.getNBPT_RSFZ_R_P_PTYPE())) {
					
					currentPojo.getModuleRight().add(rp.getNBPT_RSFZ_R_P_PID());
				} 
				// 1表示功能
				else if ("1".equals(rp.getNBPT_RSFZ_R_P_PTYPE())) {
					
					currentPojo.getFunctionRight().add(rp.getNBPT_RSFZ_R_P_PID());
				}
				// 2表示功能
				else if ("2".equals(rp.getNBPT_RSFZ_R_P_PTYPE())) {
					
					currentPojo.getPageRight().add(rp.getNBPT_RSFZ_R_P_PID());
				}
			}
		}

		// 处理好pojo后,放入缓存中
		for(String key : rightMap.keySet()) {

			CacheUtil.setURCacheContent(key, rightMap.get(key));
		}
		
	}

}
