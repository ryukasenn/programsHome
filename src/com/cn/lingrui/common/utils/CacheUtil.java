package com.cn.lingrui.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


public class CacheUtil {

	private static Logger log = LogManager.getLogger();
	private static CacheManager cacheManager = null; 

	private static CacheManager getCacheManager() {
		
		if(null == cacheManager) {
			
			cacheManager = CacheManager.create();;
		}
		return cacheManager;
	}

	private static final String DB_CACHE = "dbDataCache";// 数据库个别信息
	private static final String UR_CACHE = "usersRoleCache";// 用户权限
	

	
	/**
	 * 获得一个Cache，没有则显示日志。
	 * @param cacheName
	 * @return
	 */
	private static Cache getCache(String cacheName) {
		
		Cache c = getCacheManager().getCache(cacheName);
		if(null == c) {
			
			log.info("该缓存不存在");
		}
		return c;
	}
	
	/**
	 * 获取DB_CACHE缓存
	 * @param key
	 * @return
	 */
	public static Cache getDBCache() {
		return getCache(DB_CACHE);
	}
	
	/**
	 * 获取DBCache中的内容
	 * @param key
	 * @return
	 */
	public static Element getDBCacheContent(String key) {
		
		return getDBCache().get(key);
	}
	
	/**
	 * 将缓存内容写入DBCache
	 * @param e
	 */
	public static void setDBCacheContent(String key, Object obj) {

		Element e = new Element(key, obj);
		
		put(DB_CACHE, e);
	}

	/**
	 * 移除DB_CACHE中的缓存内容
	 * @param key
	 */
	public static void removeDBCacheContent(String key) {
		
		remove(DB_CACHE, key);
	}
	
	/**
	 * 从DBCache缓存中移除所有
	 */
	public static void removeDBCacheContentAll() {
		
		removeAll(DB_CACHE);
	}
	
	/**
	 * 获取URCache缓存
	 * @param key
	 * @return
	 */
	private static Cache getURCache() {
		return getCache(UR_CACHE);
	}
	
	/**
	 * 获取URCache中的内容
	 * @param key
	 * @return
	 */
	public static Element getURCacheContent(String key) {
		
		return getURCache().get(key);
	}
	
	/**
	 * 将缓存内容写入URCache
	 * @param e
	 */
	public static void setURCacheContent(String key, Object obj) {
		
		Element e = new Element(key, obj);
		
		put(UR_CACHE, e);
	}

	/**
	 * 移除URCache中的缓存内容
	 * @param key
	 */
	public static void removeURCacheContent(String key) {
		
		remove(UR_CACHE, key);
	}
	
	/**
	 * 从URCache缓存中移除所有
	 */
	public static void removeURCacheContentAll() {
		
		removeAll(UR_CACHE);
	}

	/**
	 * 从缓存中移除
	 * @param cacheName
	 * @param key
	 */
	private static void remove(String cacheName, String key) {
		getCache(cacheName).remove(getKey(key));
	}
	
	/**
	 * 写入缓存
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	private static void put(String cacheName, Element e) {
		
		getCache(cacheName).put(e);
	}

	/**
	 * 从缓存中移除所有
	 * @param cacheName
	 */
	private static void removeAll(String cacheName) {
		Cache cache = getCache(cacheName);
		cache.removeAll();
	}
	
	/**
	 * 获取缓存键名，多数据源下增加数据源名称前缀
	 * @param key
	 * @return
	 */
	private static String getKey(String key){
//		String dsName = DataSourceHolder.getDataSourceName();
//		if (StringUtils.isNotBlank(dsName)){
//			return dsName + "_" + key;
//		}
		return key;
	}
	
}
