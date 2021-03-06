package com.cn.lingrui.pm.serviceImpl;

import java.util.List;
import javax.annotation.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.pm.db.dao.AllpmDao;

import com.cn.lingrui.pm.pojos.PmInfo;
import com.cn.lingrui.pm.service.AllpmService;
import com.cn.lingrui.pm.service.PmBaseService;


@Service("allpmService")
public class AllpmServiceImpl extends PmBaseService implements AllpmService{
	
	private static Logger log = LogManager.getLogger();
	
	
	@Resource(name="allpmDao")
	private AllpmDao allpmDao;
	

	@Override
	protected String getFunNum() {
		
		return null;
	}


	@Override
	public ModelAndView getAllpmInfoView() throws Exception {
		this.before();		
		// 初始化返回
		ModelAndView mv = HttpUtil.getModelAndView("05/" + this.getCheckPage("050302"));

		// 1.生成同时期所有人排名
		List<PmInfo> allPmList = null;
		String sort =getFunNum();
		System.out.println(this.getRequest().getParameter("sc"));
		
		try {
			if(sort == null)
				sort="pm";
			allPmList =allpmDao.getAllPm(this.getConnection());
			mv.addObject("allPmList", allPmList);
				
			return this.after(mv);
		}catch (Exception e) {
			this.closeException();
			log.info("个人排名查询出错");			
			throw new Exception();
		}
		
	}


	@Override
	public ModelAndView getPmCxView(String... args) throws Exception {
		String pmrqStr = "";
		String dqmcStr = "";
		String fzdqStr = "";
		switch(args.length) {
			case 0:break;
			case 1:pmrqStr = args[0];break;
			case 2:pmrqStr =args[0];dqmcStr=args[1];break;
			case 3:pmrqStr =args[0];dqmcStr=args[1];fzdqStr= args[2];break;
			default:pmrqStr = "";dqmcStr = "";fzdqStr = "";break;
				
		}
		this.before();		
		// 初始化返回
		ModelAndView mv = HttpUtil.getModelAndView("05/" + this.getCheckPage("050402"));

		// 1.生成同时期所有人排名
		List<PmInfo> cxPmList = null;
		
		try {
			
			cxPmList =allpmDao.getAllCxPm(getConnection(), pmrqStr, dqmcStr, fzdqStr);
			mv.addObject("cxPmList", cxPmList);				
			return this.after(mv);
		}catch (Exception e) {
			this.closeException();
			log.info("个人排名查询出错");			
			throw new Exception();
		}
	
	}
	
	


	

}
