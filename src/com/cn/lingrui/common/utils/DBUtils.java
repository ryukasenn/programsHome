package com.cn.lingrui.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cn.lingrui.common.db.DBConnect;
import com.cn.lingrui.common.db.dao.ConditionDao;
import com.cn.lingrui.common.db.dbpojos.GSDBZTXX;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_CONDITION;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_U_R;
import com.cn.lingrui.common.db.dbpojos.SYSUSERLIST;
import com.cn.lingrui.common.pojos.login.LoginPojoIn;
import com.cn.lingrui.common.pojos.login.LoginPojoOut;

import net.sf.ehcache.Element;

public class DBUtils {

	private static Logger log = LogManager.getLogger();

	/**
	 * 该工具方法用于将查询的数据转化为相应的javaBean
	 * 
	 * @param classz
	 * @param rs
	 * @return
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> rsToBean(Class<T> classz, ResultSet rs) throws SQLException {

		// 初始化返回数据
		List<T> list = new ArrayList<T>();

		// 获取表的列名
		List<String> columnNames = new ArrayList<String>();
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int columnCount = rsMetaData.getColumnCount();
		for (int i = 1; i <= columnCount; i++) {

			columnNames.add(rsMetaData.getColumnName(i).toUpperCase());
		}
		
		while (rs.next()) {

			T entity = null;

			try {
				entity = classz.newInstance();

				Field[] fs = entity.getClass().getDeclaredFields();

				for (Field f : fs) {

					if (columnNames.contains(f.getName())) {

						// 如果属性包含该属性,则添加
						boolean flag = f.isAccessible();
						f.setAccessible(true);
						f.set(entity, String.valueOf(null == rs.getObject(f.getName())?"":rs.getObject(f.getName())));
						f.setAccessible(flag);
					}
				}
			} catch (InstantiationException e) {

				log.info("数据转换时出现错误" + e.getMessage());
			} catch (IllegalAccessException e) {

				log.info("数据转换时出现错误" + e.getMessage());
			}
			list.add(entity);
		}

		return list;
	}

	/**
	 * 用户登录时,获取公共账户
	 * 
	 * @param in
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	@Deprecated
	public static LoginPojoOut checkUser(LoginPojoIn in) {

		// 初始化返回结果
		LoginPojoOut out = new LoginPojoOut();
		out.setFlag(false);
		ResultSet rs = null;
		PreparedStatement ps = null;
		DBConnect dbc = null;
		try {

			dbc = getUserDBC(in.getUserId(), in.getPassword());
			// 如果公共账户密码为空,则需要重新获取
			if ("".equals(GlobalParams.RSFZ_PASSWORD) || "".equals(GlobalParams.RSFZ_USERNAME)) {

//				ps = dbc.getConnection()
//						.prepareStatement("SELECT * FROM GSDBZTXX WHERE GSDBZTXX_ZTBH = " + GlobalParams.ZTBH + " ");

				rs = ps.executeQuery();

				List<GSDBZTXX> listGSDBZTXX = DBUtils.rsToBean(GSDBZTXX.class, rs);
				if (1 == listGSDBZTXX.size()) {

					GlobalParams.RSFZ_PASSWORD = CommonUtil.decodeRs(listGSDBZTXX.get(0).getGSDBZTXX_PASS());
					GlobalParams.RSFZ_USERNAME = listGSDBZTXX.get(0).getGSDBZTXX_OWNER();
					GlobalParams.RSFZ_DBNAME = listGSDBZTXX.get(0).getGSDBZTXX_BASE();
				} else {

					out.getMessages().add("用户登录出错,取不到公共账号");
					return out;
				}
				
				closePsRs(ps, rs);
			}

			// 登录验证,重新获取用户名
//			ps = dbc.getConnection().prepareStatement("SELECT * FROM SYSUSERLIST WHERE SYSUSERLIST_USERID = '"
//					+ in.getUserId() + "' AND SYSUSERLIST_ACCID = " + GlobalParams.ZTBH);

			rs = ps.executeQuery();

			List<SYSUSERLIST> listSYSUSERLIST = DBUtils.rsToBean(SYSUSERLIST.class, rs);
			if (1 == listSYSUSERLIST.size()) {

				out.setUserName(listSYSUSERLIST.get(0).getSYSUSERLIST_USERNAME());
			} else {

				out.getMessages().add("用户登录出错,取不到用户名");
				return out;
			}

		} catch (Exception e) {

			log.info("获取公共用户是出现错误:" + e.getMessage());
			return out;
		} finally {

			try {

				closePsRs(ps, rs);
				dbc.closeConnection();
			} catch (Exception e) {

				log.info("获取公共用户是出现错误:" + e.getMessage());
				return out;
			}
		}
		out.setFlag(true);
		return out;
	}

	/**
	 * 将bean转化成sql语句
	 * 
	 * @param classz
	 *            要转化的bean
	 * @param type
	 *            sql语句的类型,INSERT,SELECT,UPDATE
	 * @param tableName
	 *            表名
	 * @param wheres
	 *            where条件
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> String beanToSql(Class<T> clasz, String type, String tableName, T... t) {

		StringBuffer sb = new StringBuffer();
		Field[] fs = clasz.getDeclaredFields();
		try {
			switch (type.toUpperCase()) {
	
			case "SELECT":
	
				sb.append("SELECT ");
				for (Field f : fs) {
					sb.append(f.getName().toUpperCase() + ", ");
				}
				sb.deleteCharAt(sb.lastIndexOf(", "));
	
				sb.append("FROM " + tableName + " ");
	
				break;
			case "INSERT":
				
				sb.append("INSERT INTO ");
				sb.append(tableName + " (");
	
				for(Field f : fs) {
					sb.append(f.getName().toUpperCase() + ",");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append(") values (");
	
				for(Field f : fs) {
					
					Object content = clasz.getMethod("get" + f.getName()).invoke(t[0]);
					
					if(null == content) {

						sb.append("'',");
					} else {
						
						sb.append("'" + content.toString() + "',");
					}
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append(") ");
				
				break;
			case "UPDATE":
				
				sb.append("UPDATE " + tableName + " SET ");
				
				for(Field f : fs) {

					Object content = clasz.getMethod("get" + f.getName()).invoke(t[0]);
					if(null == content) {
					
					}else {
						sb.append(f.getName().toUpperCase() + " = " + "'" + content.toString() + "',");
					}
				}
				sb.deleteCharAt(sb.length() - 1);
				break;
			}
		} catch (Exception e) {

			log.info("sql语句生成错误" + CommonUtil.getTrace(e));
		}

		return sb.toString();
	}

	/**
	 * 获取公共数据库连接
	 * 
	 * @return
	 */
	public static DBConnect getCommonDBC() {

		try {

			// 当前公共数据库设定在ekp数据库中
			return new DBConnect(GlobalParams.SELLPERSONNEL_USERNAME, GlobalParams.SELLPERSONNEL_PASSWORD, GlobalParams.SELLPERSONNEL_IP, GlobalParams.SELLPERSONNEL_DBNAME);
		} catch (SQLException e) {

			log.info("业务操作异常" + e.getMessage());
			return null;
		}
	}
	
	/**
	 * 获取集团人事系统数据库连接
	 * 
	 * @return
	 */
	public static DBConnect getRSFZDBC() {

		try {
			
			return new DBConnect(GlobalParams.RSFZ_USERNAME, GlobalParams.RSFZ_PASSWORD, GlobalParams.RSFZ_DBNAME);
		} catch (SQLException e) {

			log.info("业务操作异常" + e.getMessage());
			return null;
		}
	}
	
	/**
	 * 获取销售人事系统数据库连接
	 * 
	 * @return
	 */
	public static DBConnect getSELLPERSONNELDBC() {

		try {
			
			return new DBConnect(GlobalParams.SELLPERSONNEL_USERNAME, GlobalParams.SELLPERSONNEL_PASSWORD, GlobalParams.SELLPERSONNEL_IP, GlobalParams.SELLPERSONNEL_DBNAME);
		} catch (SQLException e) {

			log.info("业务操作异常" + e.getMessage());
			return null;
		}
	}
	
	/**
	 * 获取浪潮贴膏剂数据库连接 
	 * 
	 * @return
	 */
	public static DBConnect getERPTGJDBC() {

		try {
			
			return new DBConnect(GlobalParams.ERPTGJ_USERNAME, GlobalParams.ERPTGJ_PASSWORD, GlobalParams.ERPTGJ_DBNAME);
		} catch (SQLException e) {

			log.info("业务操作异常" + e.getMessage());
			return null;
		}
	}

	/**
	 * 获取用户数据库连接
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public static DBConnect getUserDBC(String userName, String password) {

		try {

			return new DBConnect(userName, password);
		} catch (SQLException e) {

			log.info("业务操作异常" + e.getMessage());
			return null;
		}
	}

	@Deprecated
	public static LoginPojoOut checkUserRole(LoginPojoIn in, LoginPojoOut out) {

		ResultSet rs = null;
		PreparedStatement ps = null;
		DBConnect dbc = null;
		try {

			dbc = getCommonDBC();

			ps = dbc.getConnection()
					.prepareStatement("SELECT * FROM RSFZ_U_R WHERE RSFZ_U_R_UID = '" + in.getUserId() + "'");

			rs = ps.executeQuery();

			List<NBPT_RSFZ_U_R> listUSERS = DBUtils.rsToBean(NBPT_RSFZ_U_R.class, rs);

			if (1 == listUSERS.size()) {

				// 登录时添加用户角色
				out.setUserRole(listUSERS.get(0).getNBPT_RSFZ_U_R_RID());
			} else {

				out.getMessages().add("用户登录出错,无法验证用户角色");
				return out;
			}

		} catch (SQLException e) {
			
			log.info("获取用户角色出错" + e.getMessage());
			e.printStackTrace();
		} finally {

			try {
				closePsRs(ps, rs);
				dbc.closeConnection();
			} catch (Exception e) {

				log.info("获取用户角色出错:" + e.getMessage());
				return out;
			}
		}
		return out;
	}

	/**
	 * 尝试关闭rsps
	 * @param ps
	 * @param rs
	 */
	public static void closePsRs(PreparedStatement ps, ResultSet rs) {

		if (null == ps) {

		} else {
			try {
				ps.close();
				ps = null;
			} catch (SQLException e) {

				log.info("关闭连接出错" + CommonUtil.getTrace(e));;
			}
		}

		if (null == rs) {

		} else {
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				
				log.info("关闭数据集出错" + CommonUtil.getTrace(e));
			}
		}
	}
	
	/**
	 * 获取报表分界条件数据
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public static List<String> receiveCondition(String conditionType, ConditionDao dao, Connection conn, String... type) throws SQLException {
		
		Element el = CacheUtil.getDBCacheContent(conditionType);

		if (null == el) {

			if(0 != type.length) {
				
				log.info("2次查询,查询出错");
				return new ArrayList<String>();
			}
			
			// 如果缓存中不存在该角色的权限信息,重新查询数据库
			List<NBPT_RSFZ_CONDITION> conditions = dao.receiveCondition(conn);
			
			List<String> checkList = new ArrayList<String>();

			Map<String, List<String>> titleMap = new HashMap<String, List<String>>();
			
			for(NBPT_RSFZ_CONDITION condition : conditions) {
				
				if(checkList.contains(condition.getNBPT_RSFZ_CONDITION_TYPE())) {
					
					titleMap.get(condition.getNBPT_RSFZ_CONDITION_TYPE()).add(condition.getNBPT_RSFZ_CONDITION_ITEM());
				} else {
					
					titleMap.put(condition.getNBPT_RSFZ_CONDITION_TYPE(), new ArrayList<String>());
					checkList.add(condition.getNBPT_RSFZ_CONDITION_TYPE());

					titleMap.get(condition.getNBPT_RSFZ_CONDITION_TYPE()).add(condition.getNBPT_RSFZ_CONDITION_ITEM());
				}
			}

			for(String key : titleMap.keySet()) {
				
				CacheUtil.setDBCacheContent(key, titleMap.get(key));
			}
			return receiveCondition(conditionType, dao, conn, "second");
			
		} else {
			
			List<String> conditions = (List<String>) el.getObjectValue();
			
			return conditions;
		}
	}
	
	public static void receiveGlobalParam(String key) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		System.out.println(GlobalParams.class.getField(key).get(GlobalParams.class));
	}
}




























