package com.cn.lingrui.common.services;

import java.sql.Connection;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.db.DBConnect;
import com.cn.lingrui.common.pojos.RightsPojo;
import com.cn.lingrui.common.utils.DBUtils;

/**
 * 前台到后端,需要连接数据库的业务处理基类
 * @author lhc
 *
 * @param <I>
 * @param <O>
 */
public abstract class BServiceLogic extends BaseService {

	protected DBConnect dbc = null;
	protected Connection connection = null;

	/**
	 * init方法用于初始化数据库连接,如果需要连接数据库,则在方法执行前执行此方法
	 */
	protected void before() {
		
		dbc = DBUtils.getCommonDBC();
		connection = dbc.getConnection();
	}

	/**
	 * 必行方法,用于关闭数据库连接,提交数据变更,以及添加
	 * @param mv 放回结果
	 * @param user 用于区分管理员和普通用户
	 * @return
	 */
	protected ModelAndView after(ModelAndView mv, String... user) {

		RightsPojo rightsPojo = new RightsPojo();
		if(0 != user.length) {
			
			if("login".equals(user[0])) {
				
				// 每个页面都要获取用户名
				mv.addObject("niko", user[1]);
				
				if(!"系统管理员".equals(user[1])) {

					rightsPojo.setFunctionsRight(this.getFunctionList(user[2]));
					rightsPojo.setModulesRight(this.getModuleList(user[2]));
					mv.addObject("rights", rightsPojo);
				}
			} 
		} else {

			
			rightsPojo.setFunctionsRight(this.getFunctionList());
			rightsPojo.setModulesRight(this.getModuleList());
			mv.addObject("rights", rightsPojo);

			// 每个页面都要获取用户名
			mv.addObject("niko", this.getUserName());
		}

		if (dbc == null) {

		} else {
			
			dbc.closeConnection();
		}
		return mv;
	}
	/**
	 * 必行方法,用于关闭数据库连接,提交数据变更,以及添加
	 * @param mv 放回结果
	 * @param user 用于区分管理员和普通用户
	 * @return
	 */
	protected ModelAndView afterUncheck(ModelAndView mv) {

		if (dbc == null) {

		} else {
			
			dbc.closeConnection();
		}
		return mv;
	}
	/**
	 * 必行方法,用于关闭数据库连接,提交数据变更,以及添加
	 * @param result 放回结果
	 * @return
	 */
	protected String after(String result) {


		if (dbc == null) {

		} else {
			
			dbc.closeConnection();
		}
		return result;
	}
	
	/**
	 * 必行方法,用于关闭数据库连接,提交数据变更,以及添加
	 * @param result 放回结果
	 * @return
	 */
	protected String afterException(String result) {


		this.closeException();
		return result;
	}
	
	/**
	 * 出现异常的关闭
	 */
	protected void closeException() {
		
		dbc.closeException();
	}
	
	@Override
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	/**
	 * 获取登录人员的id
	 * @return
	 */
	protected String getLoginId() {
		
		return this.getRequest().getAttribute("userID").toString();
	}

}
