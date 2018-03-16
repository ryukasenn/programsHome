package com.cn.lingrui.common.interceptor;

import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.utils.GlobalParams;
import com.cn.lingrui.common.utils.HttpUtil;

public class LoginCheckInterceptor implements HandlerInterceptor {

	private static Logger log = LogManager.getLogger();

	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse resp, Object arg2, Exception arg3)
			throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object arg2) throws Exception {

//		if ("admin".equals(req.getAttribute("userID"))) {
//			return true;
//		}
//		
		String[] s = req.getRequestURI().split("\\/");
		if(2 == s.length) {
			if("login".equals(s[2])) {
				
				Map<String, Cookie> cookieMap = HttpUtil.readCookie(req);
				if(null == cookieMap.get("userName")) {
					
				}
			}
		}
		Map<String, Cookie> cookieMap = HttpUtil.readCookie(req);
		if (userCheckCookie(cookieMap)) {

			if (!userCheckSession(cookieMap, req)) {
				
				// 用户在服务器没有登录或者登录session过期
				log.debug("验证失败,用户在服务器没有登录或者登录session过期");
				resp.sendRedirect(GlobalParams.LOGIN_URL);
				
				return false;
			}
		} else {

			// 如果没有cookie,则直接返回到登录页面
			log.debug("验证失败,用户没有登录");
			resp.sendRedirect(GlobalParams.LOGIN_URL);
			return false;
		}
				

		// 添加用户信息
		req.setAttribute("userName", cookieMap.get("userName").getValue());
		req.setAttribute("userID", cookieMap.get("userID").getValue());
		
		return true;
	}

	/**
	 * 验证用户cookie
	 * 
	 * @param req
	 * @return
	 */
	private boolean userCheckCookie(Map<String, Cookie> cookieMap) {
		boolean flag = false;

		if (null != cookieMap.get("userName") && "" != cookieMap.get("userName").getValue()) {
			
			flag = true;
		}
		return flag;
	}

	/**
	 * 验证用户session
	 * 
	 * @param cookieMap
	 * @param req
	 * @return
	 */
	private boolean userCheckSession(Map<String, Cookie> cookieMap, HttpServletRequest req) {

		boolean flag = false;

		if (null == cookieMap.get("userID") || "".equals(cookieMap.get("userID").getValue())) {

			// 如果获取userId Cookie 为空,直接返回false
		} else {

			if (null == req.getSession().getAttribute(cookieMap.get("userID").getValue()) || "".equals(req.getSession().getAttribute(cookieMap.get("userID").getValue()))) {

				// 如果为空,则该用户在服务器没有登录或者session过期

			} else {

				flag = true;
			}
		}
		
		return flag;
	}

}


















