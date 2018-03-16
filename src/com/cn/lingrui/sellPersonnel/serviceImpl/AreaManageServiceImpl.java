package com.cn.lingrui.sellPersonnel.serviceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.sellPersonnel.db.dao.AreaManageDao;
import com.cn.lingrui.sellPersonnel.db.dbpojos.area.Area;
import com.cn.lingrui.sellPersonnel.db.dbpojos.area.Province;
import com.cn.lingrui.sellPersonnel.db.dbpojos.area.Region;
import com.cn.lingrui.sellPersonnel.pojos.area.CurrentAreasPojo;
import com.cn.lingrui.sellPersonnel.pojos.area.CurrentAreas_areas;
import com.cn.lingrui.sellPersonnel.pojos.area.CurrentAreas_provinces;
import com.cn.lingrui.sellPersonnel.service.AreaManageService;
import com.cn.lingrui.sellPersonnel.service.SellPBaseService;

@Service("areaManageService")
public class AreaManageServiceImpl extends SellPBaseService implements AreaManageService {

	private static Logger log = LogManager.getLogger();
	
	@Resource(name="areaManageDao")
	private AreaManageDao areaManageDao;
	@Override
	public ModelAndView receiveCurrentAreas() throws Exception {

		this.before();
		ModelAndView mv = null;
		try {
			// 1.首先要获取大区列表
			List<Region> regions = areaManageDao.receiveRegions(this.getConnection());
			
			// 2.然后要获取大区省份列表
			List<Province> provinces = areaManageDao.receiveProvinces(this.getConnection());
			
			// 3.最后要获取大区省份地区列表
			List<Area> areas = areaManageDao.receiveAreas(this.getConnection());
			
			// 添加数据
			CurrentAreasPojo currentAreasOut = new CurrentAreasPojo();
			currentAreasOut.setRegions(regions);
			currentAreasOut.setProvinces(dealProvinces(provinces));
			currentAreasOut.setAreas(dealAreas(areas));
			
			mv = HttpUtil.getModelAndView("03/" + this.getCheckPage("030302"));
			mv.addObject("currents", currentAreasOut);
			
			return this.after(mv);
		} catch (SQLException e) {
			
			log.info("地区管理出错");
			throw new Exception();
		}
	}

	/**
	 * 处理查询到的省份
	 * @param provinces
	 */
	private List<CurrentAreas_provinces> dealProvinces(List<Province> provinces) {
		
		List<CurrentAreas_provinces> resultProvinces = new ArrayList<>();
		List<String> checkItems = new ArrayList<>();
		
		for(Province p : provinces) {
			
			if(checkItems.contains(p.getREGION_ID())) {
				
				resultProvinces.get(checkItems.indexOf(p.getREGION_ID())).getProvinces().add(p);
				resultProvinces.get(checkItems.indexOf(p.getREGION_ID())).setREGION_ID(p.getREGION_ID());
			} else {
				
				checkItems.add(p.getREGION_ID());
				CurrentAreas_provinces newprovinces = new CurrentAreas_provinces();
				newprovinces.getProvinces().add(p);
				newprovinces.setREGION_ID(p.getREGION_ID());
				resultProvinces.add(newprovinces);
				
			}
		}
		return resultProvinces;
	}

	/**
	 * 处理查询到的地区
	 * @param areas
	 * @return
	 */
	private List<CurrentAreas_areas> dealAreas(List<Area> areas) {

		List<CurrentAreas_areas> resultAreas = new ArrayList<>();
		List<String> checkItems = new ArrayList<>();
		
		for(Area a : areas) {
			
			if(checkItems.contains(a.getPROVINCE_ID())) {
				
				resultAreas.get(checkItems.indexOf(a.getPROVINCE_ID())).getAreas().add(a);
				resultAreas.get(checkItems.indexOf(a.getPROVINCE_ID())).setPROVINCE_ID(a.getPROVINCE_ID());
			} else {
				
				checkItems.add(a.getPROVINCE_ID());
				CurrentAreas_areas newareas = new CurrentAreas_areas();
				newareas.getAreas().add(a);
				newareas.setPROVINCE_ID(a.getPROVINCE_ID());
				resultAreas.add(newareas);
				
			}
		}
		return resultAreas;
	}
	
	@Override
	protected String getFunNum() {
		// TODO 自动生成的方法存根
		return null;
	}

}
