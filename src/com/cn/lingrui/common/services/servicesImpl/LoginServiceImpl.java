package com.cn.lingrui.common.services.servicesImpl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.db.dao.LoginDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_USER;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_U_R;
import com.cn.lingrui.common.pojos.login.CurrentUser;
import com.cn.lingrui.common.pojos.login.LoginPojoIn;
import com.cn.lingrui.common.pojos.login.LoginPojoOut;
import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.services.LoginService;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;

@Service("loginService")
public class LoginServiceImpl extends BServiceLogic implements LoginService {

	private static Logger log = LogManager.getLogger();

	@Resource(name = "loginDao")
	private LoginDao loginDao;

	@Override
	public ModelAndView checkUser(LoginPojoIn in) throws Exception {

		try {
			
			this.before();
			
			log.info("用户 " + in.getUserId() + " 尝试登录");
			
			// 初始化返回信息
			ModelAndView mv = null;
			LoginPojoOut out = new LoginPojoOut();
			
			// 执行登录验证操作
			mv = this.loginConfirm(in, out, mv);
			
			log.info("用户 " + in.getUserId() + " 登录成功");
			
			// 执行结束操作
			return this.after(mv, "login", out.getUserName(), out.getUserRole());
		} catch (Exception e) {

			log.info("普通登录出错");
			this.closeException();
			throw new Exception();
		}
	}

	

	@Override
	protected String getFunNum() {
		return null;
	}

	@Override
	public ModelAndView otherLogin(LoginPojoIn in) throws Exception {

		try {

			log.info("用户 " + in.getUserId() + " 尝试从OA链接登录");
			
			this.before();
			
			// 初始化返回信息
			ModelAndView mv = null;
			LoginPojoOut out = new LoginPojoOut();
			
			// 执行登录验证操作
			mv = this.loginConfirm(in, out, mv);
			
			log.info("用户 " + in.getUserId() + " OA链接登录成功");
			
			// 执行结束操作
			return this.after(mv, "login", out.getUserName(), out.getUserRole());
		} catch (Exception e) {
			
			log.info("OA连接登录出错");
			this.closeException();
			throw new Exception();
		}
	}

	/**
	 * 加入核销系统后,新的登录逻辑
	 * @param in 登录页面传入数据
	 * @param out 登录逻辑后传出数据
	 * @return
	 * @throws Exception
	 */
	private ModelAndView loginConfirm(LoginPojoIn in, LoginPojoOut out, ModelAndView mv) throws Exception {

		// 1.管理员用户登录
		if ("admin".equals(in.getUserId())) {

			mv = HttpUtil.getModelAndView("common/index");
			out.setUserName("系统管理员");
			// 添加登录信息
			addLoginInfo(in, null, "admin");
			return mv;
		}

		// 2.1普通用户登录,登录用户信息并验证
		this.getUserInfo(in, out);
		
		// 2.2普通用户登录,首页跳转,及登录信息
		return this.setLoginPage(in, out);

	}

	/**
	 * 用户验证成功设定返回信息
	 * @param in
	 * @param out
	 */
	private ModelAndView setLoginPage(LoginPojoIn in, LoginPojoOut out) {
		
		ModelAndView mv = null;
		if(!out.getFlag()) {

			mv = HttpUtil.getModelAndView("common/login");
			mv.addObject("messages", out.getMessages());
			return mv;
		}
		
		else if (out.getFlag()) {

			// 添加登录信息
			addLoginInfo(in, out, null);

			// 获取该权限的首页配置
			String firstPage = CommonUtil.getBasePropertieValue(out.getUserRole());

			mv = HttpUtil.getModelAndView(firstPage.substring(0, 2) + "/" + this.getCheckPage(firstPage, out.getUserRole()));	
		}
		
		return mv;
	}

	/**
	 * 获取登录用户信息并验证
	 * @param in
	 * @param out
	 * @throws SQLException 
	 */
	private void getUserInfo(LoginPojoIn in, LoginPojoOut out) throws SQLException {


		CurrentUser userinfo = loginDao.getUserInfo(in, this.getConnection());
		
		if (null == userinfo) {

			// 如果找不到,
			out.setFlag(false);
			out.getMessages().add("用户名或密码错误");
			log.info("用户名为 " + in.getUserId() + " 的用户尝试登录不成功,因为用户不存在或密码错误");
		} else {

			out.setUserName(userinfo.getNBPT_RSFZ_USER_NAME());
			out.setUserInfo(userinfo);
			// 获取用户角色
			String role = userinfo.getNBPT_RSFZ_U_R_RID();

			if (null == role || "" == role) {

				out.getMessages().add("该用户没有分配权限,请联系管理员");
				out.setFlag(false);
			} else {

				out.setUserRole(role);
				out.setFlag(true);
			}
		}
	}
	
	/**
	 * 添加用户登录信息cookies
	 * 
	 * @param in
	 *            页面传入参数
	 * @param resp
	 *            response
	 * @param out
	 *            普通用户处理后的返回参数
	 * @param differ
	 *            管理员与普通用户的区分
	 */
	private void addLoginInfo(LoginPojoIn in, LoginPojoOut out, String differ) {

		if ("admin".equals(differ)) {

			// 添加用户登录session
			getSession().setAttribute("admin", "admin");

			// 添加用户登录cookie
			Cookie cookieName = new Cookie("userName", "系统管理员");
			Cookie cookieId = new Cookie("userID", "admin");
			getResponse().addCookie(cookieName);
			getResponse().addCookie(cookieId);
		} else {

			// 添加用户登录session
			getSession().setAttribute(in.getUserId(), out.getUserRole());

			// 验证成功添加Cookie
			Cookie cookieName = new Cookie("userName", out.getUserName());
			Cookie cookieId = new Cookie("userID", in.getUserId());
			getResponse().addCookie(cookieName);
			getResponse().addCookie(cookieId);
		}
	}
}





























