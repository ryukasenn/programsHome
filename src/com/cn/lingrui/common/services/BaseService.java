package com.cn.lingrui.common.services;

import java.sql.Connection;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.lingrui.common.db.dao.AuthorityDao;
import com.cn.lingrui.common.utils.AuthorityUtil;

/**
 * 前-后业务处理基本类
 * 
 * @author lhc
 *
 */
public abstract class BaseService {

	@Resource(name = "authorityDao")
	protected AuthorityDao authorityDao;
	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpServletResponse response;

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpSession getSession() {
		return request.getSession();
	}

	public HttpServletResponse getResponse() {

		return response;
	}

	/**
	 * 获取用户姓名
	 * 
	 * @return
	 */
	public String getUserName() {
		return getRequest().getAttribute("userName").toString();
	}

	/**
	 * 获取用户角色
	 * 
	 * @return
	 */
	public String getRole() {
		return getSession().getAttribute(this.getRequest().getAttribute("userID").toString()).toString();
	}

	/**
	 * 获取模块List,需要数据库连接
	 * @return
	 */
	public List<String> getModuleList(String... role) {
		
		if(0 == role.length) {
			return AuthorityUtil.moduleList(this.getRole(), this.getConnection(), authorityDao);
		} else {

			return AuthorityUtil.moduleList(role[0], this.getConnection(), authorityDao);			
		}
	}

	/**
	 * 获取功能List,需要数据库连接
	 */
	public List<String> getFunctionList(String... role) {

		if(0 == role.length) {
			return AuthorityUtil.functionRightLsit(this.getRole(), this.getConnection(), authorityDao);
		} else {

			return AuthorityUtil.functionRightLsit(role[0], this.getConnection(), authorityDao);			
		}
	}

	/**
	 * 获取权限页面,需要数据库连接
	 * @return
	 */
	protected String getCheckPage(String pagePrefix, String... role) {


		if(0 == role.length) {
			// 权限检查
			return AuthorityUtil.rightCheck(this.getRole(), pagePrefix, this.getConnection(), authorityDao);
			
		} else {

			return AuthorityUtil.rightCheck(role[0], pagePrefix, this.getConnection(), authorityDao);
		}
	}

	protected abstract Connection getConnection();

	/**
	 * 设定功能编号
	 * @return
	 */
	protected abstract String getFunNum();
}
