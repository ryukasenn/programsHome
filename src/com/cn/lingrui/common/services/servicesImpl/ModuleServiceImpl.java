package com.cn.lingrui.common.services.servicesImpl;


import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.services.BServiceLogic;
import com.cn.lingrui.common.services.ModuleService;
import com.cn.lingrui.common.utils.HttpUtil;

@Service("moduleService")
public class ModuleServiceImpl extends BServiceLogic implements ModuleService {

	@Override
	public ModelAndView pageJump() {

		this.before();
		
		// 初始化返回结果
		ModelAndView mv = null;

		// 如果是模块点击事件,则返回默认起始页面,也就是编号加01
		String page = this.getCheckPage(this.getFunNum());
		mv = HttpUtil.getModelAndView(page.substring(0, 2) + "/" + page);
		
		return this.after(mv);
	}

	@Override
	protected String getFunNum() {
		
		// 获取module参数
		String module = this.getRequest().getParameter("module");
		
		// 判断是模块点击事件,还是功能点击事件
		if(2 == module.length()) {
			
			return module + "0101";
		} else if(4 == module.length()) {
			
			// 如果是功能点击事件
			return module + "01";
		}
		return null;
	}

}
