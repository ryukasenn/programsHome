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
import com.cn.lingrui.common.pojos.login.LoginPojoIn;
import com.cn.lingrui.common.pojos.login.LoginPojoOut;
import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.services.LoginService;
import com.cn.lingrui.common.utils.HttpUtil;

@Service("loginService")
public class LoginServiceImpl extends BServiceLogic implements LoginService {

	private static Logger log = LogManager.getLogger();

	@Resource(name = "loginDao")
	private LoginDao loginDao;

	@Override
	public ModelAndView checkUser(LoginPojoIn in) throws Exception {

		this.before();
		
		// 执行登录验证操作
		log.info("用户 " + in.getUserId() + " 尝试登录");
		ModelAndView mv = null;
		
		LoginPojoOut out = new LoginPojoOut();
		
		try {
			mv = this.excuteLogin(in, out);
		} catch (Exception e) {

			this.closeException();
			throw new Exception();
		}

		// 执行结束操作
		return this.after(mv, "login", out.getUserName(), out.getUserRole());
	}

	/**
	 * 登录验证
	 * 
	 * @param in
	 *            前台传入数据
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	private ModelAndView excuteLogin(LoginPojoIn in, LoginPojoOut out) throws Exception {

		// 初始化返回结果
		ModelAndView mv = null;

		
		// 管理员用户登录
		if ("admin".equals(in.getUserId())) {

			mv = HttpUtil.getModelAndView("common/index");
			out.setUserName("系统管理员");
			// 添加登录信息
			addLoginInfo(in, null, "admin");
			return mv;
		}

		// TODO
		// 新方法
		List<NBPT_RSFZ_USER> users;
		users = loginDao.checkUser(in, this.getConnection());

		if (null == users || 0 == users.size()) {

			// 如果找不到,
			out.setFlag(false);
			out.getMessages().add("用户名或密码错误");
			log.info("用户名或密码错误");
		} else {

			out.setUserName(users.get(0).getNBPT_RSFZ_USER_NAME());
			// 获取用户角色
			List<NBPT_RSFZ_U_R> roles = loginDao.getRole(in, this.getConnection());

			if (null == roles || 0 == roles.size()) {

				out.setFlag(false);
				out.getMessages().add("该用户没有添加角色,请联系管理员");
				log.info("该用户没有添加角色,请联系管理员");
			} else {

				out.setUserRole(roles.get(0).getNBPT_RSFZ_U_R_RID());
				out.setFlag(true);
			}
		}
		// LoginPojoOut out = DBUtils.checkUser(in);

		if (out.getFlag()) {

			// 获取用户信息成功,获取用户权限信息
			// DBUtils.checkUserRole(in, out);

			// 添加登录信息
			addLoginInfo(in, out, null);
			
			// 信息专员权限
			if("600007".equals(out.getUserRole())) {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030101", out.getUserRole()));
			} 
			
			// 大区总权限
			else if("600006".equals(out.getUserRole())){
				
				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030501", out.getUserRole()));
			}
			
			// 默认人员管理页面
			else {

				mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030401", out.getUserRole()));
			}

			log.info("用户 " + in.getUserId() + " 登录成功");
			
		} else {
			mv = HttpUtil.getModelAndView("common/login");
		}
		mv.addObject("messages", out.getMessages());
		return mv;

	}

	/**
	 * 添加用户登录信息
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

	@Override
	protected String getFunNum() {
		return null;
	}

	@Override
	public ModelAndView otherLogin(String username) throws Exception {

		log.info("用户 " + username + " 尝试从OA系统登录");
		this.before();
		
		// 验证用户
		try {
			List<NBPT_RSFZ_USER> users = loginDao.otherLogin(username, this.getConnection());
			
			if(0 == users.size()) {

				throw new Exception();
			} else {
				
				// 获取用户角色
				String role = loginDao.getRole(username, this.getConnection()).get(0).getNBPT_RSFZ_U_R_RID();

				log.info("用户 " + username + " OA登录成功");
				
				ModelAndView mv = null;
				// 信息专员权限
				if("600007".equals(role)) {

					mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030101", role));
				} 
				
				// 大区总权限
				else if("600006".equals(role)){
					
					mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030501", role));
				}
				
				// 默认人员管理页面
				else {

					mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030401", role));
				}

				// 添加登录信息
				// 添加用户登录session
				getSession().setAttribute(username, role);

				// 验证成功添加Cookie
				Cookie cookieName = new Cookie("userName", users.get(0).getNBPT_RSFZ_USER_NAME());
				Cookie cookieId = new Cookie("userID", username);
				
				getResponse().addCookie(cookieName);
				getResponse().addCookie(cookieId);
				return this.after(mv, "login", username, role);
			}
			
		} catch (SQLException e) {
			
			log.info("外部链接登录错误");
			throw new Exception();
		}
	}

}





























