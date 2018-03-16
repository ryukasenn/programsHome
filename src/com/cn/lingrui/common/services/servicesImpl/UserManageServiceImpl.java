package com.cn.lingrui.common.services.servicesImpl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.db.dao.RoleManageDao;
import com.cn.lingrui.common.db.dao.UserManageDao;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_ROLE;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_USER;
import com.cn.lingrui.common.db.dbpojos.NBPT_RSFZ_U_R;
import com.cn.lingrui.common.pojos.userManage.UmUserPojoIn;
import com.cn.lingrui.common.pojos.userManage.UmUserPojoOut;
import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.services.UserManageService;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;

@Service("userManageService")
public class UserManageServiceImpl extends BServiceLogic implements UserManageService{

	private static Logger log = LogManager.getLogger();
	
	@Resource(name="userManageDao")
	private UserManageDao userManageDao;

	@Resource(name="roleManageDao")
	private RoleManageDao roleManageDao;
	@Override
	protected String getFunNum() {
		
		return "01030101";
	}
	
	@Override
	public ModelAndView getHomePage() {

		this.before();
		// 初始化返回结果
		ModelAndView mv = HttpUtil.getModelAndView("01/" + this.getFunNum());
		
		// 获取所有的角色信息
		List<NBPT_RSFZ_USER> userList = userManageDao.getUserList(this.getConnection()); 
		
		mv.addObject("users", userList);
		
		return this.after(mv, "admin");
	}

	@Override
	public ModelAndView getAddUser(UmUserPojoIn in) throws Exception {
		
		this.before();
		// 初始化返回结果
		ModelAndView mv = HttpUtil.getModelAndView("01/01030201");
		
		try {
			
			List<NBPT_RSFZ_ROLE> roles = roleManageDao.getRoleList(this.getConnection());
					
			mv.addObject("roleSelects", roles);
		} catch (SQLException e) {

			this.closeException();
			throw new Exception();
		}
		
		return this.after(mv, "admin");
	}

	@Override
	public ModelAndView postAddUser(UmUserPojoIn in) throws Exception {
		
		this.before();

		// 初始化返回视图
		ModelAndView mv = null;
		
		try {
			
			// 1.验证重复性
			List<NBPT_RSFZ_USER> users = userManageDao.getUser(this.getConnection(), in.getNewUserId());
			
			if(0 == users.size()) {
				
				// 没有重复用户
			} else {
				
				UmUserPojoOut out = new UmUserPojoOut();
				out.getMessages().add("用户ID" + in.getNewUserId() + "重复");
				mv = HttpUtil.getModelAndView("01/01030201");
				mv.addObject("messages", out.getMessages());
				return this.after(mv);
			}
			
			// 插入新用户
			NBPT_RSFZ_USER user = new NBPT_RSFZ_USER();
			user.setNBPT_RSFZ_USER_ID(in.getNewUserId());
			user.setNBPT_RSFZ_USER_NAME(in.getNewUserName());
			user.setNBPT_RSFZ_USER_PHONE(in.getNewUserPhone());
			user.setNBPT_RSFZ_USER_BZ(in.getNewUserBz());
			user.setNBPT_RSFZ_USER_EMALL(in.getNewUserEmall());
			user.setNBPT_RSFZ_USER_PASSWORD(in.getNewUserPassword());
			userManageDao.postAddUser(this.getConnection(), user);
			
			// 插入新用户角色
			NBPT_RSFZ_U_R rsfz_U_R = new NBPT_RSFZ_U_R();
			rsfz_U_R.setNBPT_RSFZ_U_R_RID(in.getNewUserRid());
			rsfz_U_R.setNBPT_RSFZ_U_R_UID(in.getNewUserId());
			mv = HttpUtil.getModelAndView("redirect:/sys/ruHome");
			userManageDao.postAddU_R(this.getConnection(), rsfz_U_R);
					
			return this.after(mv, "admin");
			
		} catch (SQLException e) {
			
			log.info("添加新用户出错" + CommonUtil.getTrace(e));
			throw new Exception();
		}
	}

	@Override
	public ModelAndView getUpdateUser(UmUserPojoIn in) {

		
		return null;
	}

	@Override
	public ModelAndView postUpdateUser(UmUserPojoIn in) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public ModelAndView postDeleteUser(UmUserPojoIn in) {
		// TODO 自动生成的方法存根
		return null;
	}

}
