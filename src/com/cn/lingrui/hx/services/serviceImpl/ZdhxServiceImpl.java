package com.cn.lingrui.hx.services.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_XSDD;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_XSDDMX;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_XSHK;
import com.cn.lingrui.hx.db.dbpojos.hx.Hx_insert_xsddhx;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_update_xsdd;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_update_xsddmx;
import com.cn.lingrui.hx.db.dbpojos.hx.Zdhx_update_xshk;
import com.cn.lingrui.hx.services.ZdhxService;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.hx.pojos.province.ClassifyByshdkh_rybh;
import com.cn.lingrui.hx.pojos.province.ProvincePojoIn;
import com.cn.lingrui.hx.pojos.province.ProvincePojoOut;

@Service
@Transactional
public class ZdhxServiceImpl implements ZdhxService {

//	@Resource(name="hxDao")
//	private ZdhxDao zdhxDao;

	private static Logger log = LogManager.getLogger();

	@Override
	public ModelAndView provinceSelected(ProvincePojoIn in) throws Exception {

		// 初始化返回数据
		ModelAndView mv = HttpUtil.getModelAndView("hx/provinceSel", "自动核销");
		ProvincePojoOut out = new ProvincePojoOut();

		// 销售订单处理
		this.xsddsHandler(in, out);

		// 销售订单相关回款单处理
		this.xshksHandler(in, out);

		// 销售订单明细处理
		this.xsddmxHandler(in, out);

		// 分类完毕,开始核销
		this.hsHandler(in, out);
		
		// 核销完毕,生成其他sql
		this.createSql(out);

		// 执行数据库更新
		this.updateDB(out);
		
		return mv;
	}

	private void updateDB(ProvincePojoOut out) {
		
		// 循环所有的sql参数
		for(ClassifyByshdkh_rybh classifyByshdkh_rybh : out.getResults()) {
			
			
			if(0 != classifyByshdkh_rybh.getInsertXsddhxParams().size()) {

				// xsddhx的插入
//				zdhxDao.insertXSDDHX(classifyByshdkh_rybh.getInsertXsddhxParams());
			}
			if(0 != classifyByshdkh_rybh.getUpdateXsddParams().size()) {

				// xsdd的更新
//				zdhxDao.updateXSDD(classifyByshdkh_rybh.getUpdateXsddParams());
			}
			if(0 != classifyByshdkh_rybh.getUpdateXsddmxParams().size()) {

				// xsddmx的更新
//				zdhxDao.updateXSDDMX(classifyByshdkh_rybh.getUpdateXsddmxParams());
			}
			if(0 != classifyByshdkh_rybh.getUpdateXshkParams().size()) {

				// xshk的更新
//				zdhxDao.updateXSHK(classifyByshdkh_rybh.getUpdateXshkParams());
			}
			
			
		}
		
	}
	
	/**
	 * 数据库更新相关参数设定
	 * @param in
	 * @param out
	 */
	private void createSql(ProvincePojoOut out) {
		
		// 1.生成xsdd更新参数
		this.createSql_update_xsdd(out);
		
		// 2.生成xshk更新参数
		this.createSql_update_xshk(out);
		
		// 3.生成XSDDMX更新参数
		this.createSql_update_xsddmx(out);
		
		
	}
	
	/**
	 * 生成XSDDMX更新相关参数
	 * @param out
	 */	
	private void createSql_update_xsddmx(ProvincePojoOut out) {
		
		// 遍历所有分类
		for(ClassifyByshdkh_rybh classifyByshdkh_rybh : out.getResults()) {
			
			for(Zdhx_XSDDMX xsddmx : classifyByshdkh_rybh.getXsddmxs()) {
				
				if("1".equals(xsddmx.getChangeFlag())) {

					// 确定核销语句后,确定XSDDMX的更新语句
					Zdhx_update_xsddmx update_xsddmx = new Zdhx_update_xsddmx();
					
					update_xsddmx.setXSDDMX_DDLS(xsddmx.getXSDDMX_DDLS()); // 发货单流水
					update_xsddmx.setXSDDMX_DDFL(xsddmx.getXSDDMX_DDFL()); // 发货单明细流水
					update_xsddmx.setXSDDMX_HKHX(xsddmx.getXSDDMX_HKHX()); // 核销标志
					update_xsddmx.setXSDDMX_HXSJHK(xsddmx.getXSDDMX_HXSJHK()); // 核销实际金额
					update_xsddmx.setXSDDMX_YHXJE(xsddmx.getXSDDMX_YHXJE()); // 核销实际金额
					
					classifyByshdkh_rybh.getUpdateXsddmxParams().add(update_xsddmx);
				}else {
					
					// do nothing
				}
			}
		}
	}
	
	/**
	 * 生成XSHK更新相关参数
	 * @param out
	 */
	private void createSql_update_xshk(ProvincePojoOut out) {
		
		// 遍历所有分类
		for(ClassifyByshdkh_rybh classifyByshdkh_rybh : out.getResults()) {
			
			for(Zdhx_XSHK xshk : classifyByshdkh_rybh.getXshks()) {
				
				if("1".equals(xshk.getChangeFlag())) {
					

					Zdhx_update_xshk update_xshk = new Zdhx_update_xshk();
					
					update_xshk.setXSHK_DDHX(xshk.getXSHK_DDHX());
					update_xshk.setXSHK_YDHSJHK(xshk.getXSHK_YDHSJHK());
					update_xshk.setXSHK_YDHXE(xshk.getXSHK_YDHXE());
					update_xshk.setXSHK_HKLS(xshk.getXSHK_HKLS());
					classifyByshdkh_rybh.getUpdateXshkParams().add(update_xshk);
				}else {
					
					 // do nothing
				}
				
			}
		}
	}

	
	/**
	 * 生成XSDD更新相关参数
	 * @param out
	 */
	private void createSql_update_xsdd(ProvincePojoOut out) {
				
		// 遍历所有分类
		for(ClassifyByshdkh_rybh classifyByshdkh_rybh : out.getResults()) {

			// 初始化当前分类下不处理的XSDD的ddls列表
			List<String> undealXsddlss = new ArrayList<String>();
			
			// 遍历当前分类下的xsddmx
			for(Zdhx_XSDDMX xsddmx : classifyByshdkh_rybh.getXsddmxs()) {
				
				if("1".equals(xsddmx.getXSDDMX_HKHX())) { 
					
					// 如果当前发货单明细处理完毕,do nothing
				} else{
					
					// 如果没有处理完毕,则将该订单流水放入数组中
					if(undealXsddlss.contains(xsddmx.getXSDDMX_DDLS())) {
						
						// 如果不处理列表中包含,则不继续添加
					}else {
						
						// 如果不包含,则添加
						undealXsddlss.add(xsddmx.getXSDDMX_DDLS());
					}
				}
			}
			
			// 获得不处理列表后,从该分类的ddls数组去除这些
			for(String ddls : undealXsddlss) {
				
				classifyByshdkh_rybh.getXsddlss().remove(ddls);
			}
			
			for(String ddls : classifyByshdkh_rybh.getXsddlss()) {
				
				Zdhx_update_xsdd update_xsdd = new Zdhx_update_xsdd();
				update_xsdd.setXSDD_DDLS(ddls);
				update_xsdd.setXSDD_HKHX("1");
				classifyByshdkh_rybh.getUpdateXsddParams().add(update_xsdd);
			}
		}
	}
	
	/**
	 * 自动核销核心方法
	 * 
	 * @param out
	 */
	private void hsHandler(ProvincePojoIn in, ProvincePojoOut out) {

		// 获取所有分类
		List<ClassifyByshdkh_rybh> list = out.getResults();

		// 遍历所有分类进行处理
		for (ClassifyByshdkh_rybh classifyByshdkh_rybh : list) {

			// 获取XSDD
			List<Zdhx_XSDD> xsdds = classifyByshdkh_rybh.getXsdds();

			// 获取XSHK
			List<Zdhx_XSHK> xshks = classifyByshdkh_rybh.getXshks();

			// 获取XSDDMX
			List<Zdhx_XSDDMX> xsddmxs = classifyByshdkh_rybh.getXsddmxs();

			if (0 == xsdds.size() || 0 == xshks.size() || 0 == xsddmxs.size()) {
				// 如果没有销售订单,则直接遍历下一个分类
				continue;
			}

			// 确认结束,开始核销
			for(Zdhx_XSHK xshk : xshks) {
				
				// 计算剩余可核销额
				String remain = CommonUtil.subtract(xshk.getXSHK_YHKE(), xshk.getXSHK_YDHSJHK()).toString(); // XSHK_YHKE - XSHK_YDHSJHK, 原回款额-已核销金额
				
				if(0 == CommonUtil.compare(remain, "0")) { // 如果剩余可核销金额为0,是不应该的,因为回款单未核销,
					
					log.info("请检查HKLS编号为" + xshk.getXSHK_HKLS() + "的回款单据是否标记为已核销状态");
					
					// 继续下一条回款核销
					continue;
				}
				
				if(-1 == CommonUtil.compare(remain, "0")) {
					
					log.info("严重错误,请核实HKLS编号为" + xshk.getXSHK_HKLS() + "的回款单据是否处于异常状态");
					
					// 继续下一条回款核销
					continue;
				}
				
				// 如果还有剩余可核销金额,开始进行核销
				remain = this.hs_start(remain, xshk, classifyByshdkh_rybh, in);
				
				// 设定已核销金额
				Double yhxje = CommonUtil.subtract(xshk.getXSHK_YHKE(), remain).doubleValue();
				
				if(0 == CommonUtil.compare(remain, "0")) { // 当前回款单核销完毕,如果剩余可核销额为0
					
					xshk.setXSHK_DDHX("1");
				}
				
				xshk.setXSHK_YDHSJHK(yhxje);
				xshk.setXSHK_YDHXE(yhxje);

				// 处理过该hk
				xshk.setChangeFlag("1");
			}

		}
	}

	
	/**
	 * 对当前xshk开始核销
	 * @param remain 当前xshk的剩余可核销额
	 * @param xshk 当前xshk
	 * @param classifyByshdkh_rybh 当前分类
	 * @param in 前台传递参数
	 * @return
	 */
	private String hs_start(String remain, Zdhx_XSHK xshk, ClassifyByshdkh_rybh classifyByshdkh_rybh, ProvincePojoIn in) {
				
		// 获取要处理xsddmxs
		List<Zdhx_XSDDMX> xsddmxs = classifyByshdkh_rybh.getXsddmxs();

		// 获取当前时间
		String nowDate = CommonUtil.getYYYYMMDD();
		
		// 开始核销销售订单明细
		for(Zdhx_XSDDMX xsddmx : xsddmxs) {
			
			if("1".equals(xsddmx.getXSDDMX_HKHX())) {// 如果该明细已核销完毕,则直接进行下一个循环
				
				continue;
			}
			
			// 该明细剩余应核销金额
			String remain_xsddmx = CommonUtil.subtract(xsddmx.getXSDDMX_YYFE(), xsddmx.getXSDDMX_YHXJE()).toString();
			
			if(0 == CommonUtil.compare(remain_xsddmx, "0")) {// 如果需要核销
				
				log.info("请检查DDLS为" + xsddmx.getXSDDMX_DDLS() + ", DDFL为" + xsddmx.getXSDDMX_DDFL() + "的订单明细,已核销完毕,没有修改标志");
			}
			if(0 <= CommonUtil.compare(remain, remain_xsddmx)) { // 如果剩余可核销额大于剩余应核销额
				
				// 注入InsertXsddhx参数
				classifyByshdkh_rybh.getInsertXsddhxParams()
					.add(this.hs_insert_xsddhx(xsddmx, xshk, nowDate, remain_xsddmx, in));
				
				// 该明细核销完毕,则将标志改为1
				xsddmx.setXSDDMX_HKHX("1");

				// 修改当前mx应核销额
				xsddmx.setXSDDMX_YHXJE(CommonUtil.add(remain_xsddmx, xsddmx.getXSDDMX_YHXJE()).doubleValue());
				xsddmx.setXSDDMX_HXSJHK(CommonUtil.add(remain_xsddmx, xsddmx.getXSDDMX_HXSJHK()).doubleValue());
				
				// 处理过该mx
				xsddmx.setChangeFlag("1");
				
				// 正常核销后,修改可核销额
				remain = CommonUtil.subtract(remain, remain_xsddmx).toString();
				
				// 如果剩余可回款额核销完毕,则退出循环
				if(0 == CommonUtil.compare(remain, "0")) {
					return remain;
				}
			} else { // 如果可核销额小于该明细的应核销额,则直接进行核销
				
				// 注入销售订单核销sql参数
				classifyByshdkh_rybh.getInsertXsddhxParams()
					.add(this.hs_insert_xsddhx(xsddmx, xshk, nowDate, remain, in));
				
				// 修改当前已应核销额
				xsddmx.setXSDDMX_YHXJE(CommonUtil.add(remain, xsddmx.getXSDDMX_YHXJE()).doubleValue());
				xsddmx.setXSDDMX_HXSJHK(CommonUtil.add(remain, xsddmx.getXSDDMX_HXSJHK()).doubleValue());

				// 处理过该mx
				xsddmx.setChangeFlag("1");
				return "0";
			}
		}
		
		return remain;
	}
	
	/**
	 * 生成订单核销sql参数
	 * @param xsddmx 订单明细
	 * @param xshk 当前销售回款单
	 * @param nowDate 当前时间
	 * @param remain_xsddmx 当前明细的剩余应核销额
	 * @param in 前台参数
	 * @return
	 */
	private Hx_insert_xsddhx hs_insert_xsddhx(Zdhx_XSDDMX xsddmx, Zdhx_XSHK xshk, String nowDate, String remain_xsddmx, ProvincePojoIn in) {
		// 确定更新数据
		Hx_insert_xsddhx insert_xsddhx = new Hx_insert_xsddhx();
		insert_xsddhx.setXSDDHX_DDLS(xsddmx.getXSDDMX_DDLS()); // 订单流水
		insert_xsddhx.setXSDDHX_DDFL(xsddmx.getXSDDMX_DDFL()); // 订单内流水
		insert_xsddhx.setXSDDHX_HKLS(xshk.getXSHK_HKLS()); // 回款单流水
		insert_xsddhx.setXSDDHX_WLBH(xsddmx.getXSDDMX_WLBH()); // 产品编号
		insert_xsddhx.setXSDDHX_PCH(""); // PCH
		insert_xsddhx.setXSDDHX_HXRQ(nowDate); // 核销时间
		insert_xsddhx.setXSDDHX_HXSJHK(Double.valueOf(remain_xsddmx)); // 核销金额
		insert_xsddhx.setXSDDHX_HXDKU1(Double.valueOf("0")); // XSDDHX_HXDKU1
		insert_xsddhx.setXSDDHX_HXDKU2(Double.valueOf("0")); // XSDDHX_HXDKU2
		insert_xsddhx.setXSDDHX_HXDKU3(Double.valueOf("0")); // XSDDHX_HXDKU3
		insert_xsddhx.setXSDDHX_HXDKU4(Double.valueOf("0")); // XSDDHX_HXDKU4
		insert_xsddhx.setXSDDHX_HXDKU5(Double.valueOf("0")); // XSDDHX_HXDKU5
		insert_xsddhx.setXSDDHX_HXJE(Double.valueOf(remain_xsddmx)); // 核销金额
		insert_xsddhx.setXSDDHX_HXXM(in.getUserName()); // 核销人姓名
		
		return insert_xsddhx;
	}
	
	
	/**
	 * 发货单处理方法
	 * 该方法中,添加好所有的分类,并将所有的XSDD及XSDDLS添加至分类中,
	 * 
	 * @param mv
	 * @param in
	 * @param out
	 */
	private void xsddsHandler(ProvincePojoIn in, ProvincePojoOut out) {

		// 1.获得要处理的所有订单
//		List<Zdhx_XSDD> XSDDS = zdhxDao.getXSDDS(in.getPrivinceId());

		// 2.将订单按照 "售达客户_业务员" 进行分类
		List<String> checkList = out.getCheckList(); // 分类用checklist

//		for (Zdhx_XSDD xsdd : XSDDS) {
//
//			String shdkh_rybh = xsdd.getXSDD_SHDKH() + "^" + xsdd.getXSDD_RYBH(); // 拼接字符串
//
//			// 遍历check数组
//			if (checkList.contains(shdkh_rybh)) {
//
//				// 如果包含,则获取该分类,并将XSDD添加到该分类的XSDDS中,ddls添加到ddlss
//				ClassifyByshdkh_rybh classifyByshdkh_rybh = out.getResults().get(checkList.indexOf(shdkh_rybh));
//				classifyByshdkh_rybh.getXsdds().add(xsdd);
//				classifyByshdkh_rybh.getXsddlss().add(xsdd.getXSDD_DDLS());
//			} else {
//
//				// 如果不包含,则添加新的分类,并添加至checkList中
//				out.getResults().add(this.addNewClassify(xsdd, shdkh_rybh));
//				checkList.add(shdkh_rybh);
//			}
//		}
	}

	/**
	 * 添加新分类
	 * 
	 * @param xsdd
	 * @param shdkh_rybh
	 * @return
	 */
	private ClassifyByshdkh_rybh addNewClassify(Zdhx_XSDD xsdd, String shdkh_rybh) {

		ClassifyByshdkh_rybh classifyByshdkh_rybh = new ClassifyByshdkh_rybh();
		classifyByshdkh_rybh.setRybh(xsdd.getXSDD_RYBH()); // 获取业务员
		classifyByshdkh_rybh.setShdkh(xsdd.getXSDD_SHDKH()); // 获取客户
		classifyByshdkh_rybh.setShdkh_rybh(shdkh_rybh); // 拼接字符串
		classifyByshdkh_rybh.getXsdds().add(xsdd); // 添加订单
		classifyByshdkh_rybh.getXsddlss().add(xsdd.getXSDD_DDLS()); // 添加订单流水
		return classifyByshdkh_rybh;
	}

	/**
	 * 回款单处理方法
	 * 该方法将所有的XSHK添加至相应的分类
	 * @param mv
	 * @param in
	 * @param out
	 */
	private void xshksHandler(ProvincePojoIn in, ProvincePojoOut out) {

		// 1.根据分类获取客户回款
//		List<Zdhx_XSHK> xshks = zdhxDao.getXSHKS(in.getPrivinceId(), in.getTimeEnd());

		// 2.将订单按照 "售达客户_业务员" 进行分类
		List<String> checkList = out.getCheckList(); // 分类用checklist

		// 将汇款添加至相应分类
//		for (Zdhx_XSHK xshk : xshks) {
//
//			// 提取人员编号
//			String shdkh_rybh = xshk.getXSHK_SHDKH() + "^" + xshk.getXSHK_RYBH(); // 拼接字符串
//
//			// 如果已存在该分类,则添加至该分类中
//			if (checkList.contains(shdkh_rybh)) {
//
//				// 如果包含,则将XSDD添加到该分类的XSDDS中
//				ClassifyByshdkh_rybh classifyByshdkh_rybh = out.getResults().get(checkList.indexOf(shdkh_rybh));
//				classifyByshdkh_rybh.getXshks().add(xshk);
//			} else {
//
//				// 如果不包含,则什么都不做
//			}
//		}

	}

	/**
	 * 发货单明细处理方法
	 * 该方法将所有的XSDDMX添加至相应的分类
	 * @param mv
	 * @param in
	 * @param out
	 */
	private void xsddmxHandler(ProvincePojoIn in, ProvincePojoOut out) {

//		// 1.获取所有XSDDMX
//		List<Zdhx_XSDDMX> XSDDMXS = zdhxDao.getXSDDMXS(in.getPrivinceId());
//
//		// 2.将XSDDMX进行分类
//		for (Zdhx_XSDDMX xsddmx : XSDDMXS) {
//
//			for (ClassifyByshdkh_rybh classifyByshdkh_rybh : out.getResults()) {
//
//				// 根据流水号,添加ddmx
//				if (classifyByshdkh_rybh.getXsddlss().contains(xsddmx.getXSDDMX_DDLS())) {
//
//					// 如果包含该流水号,则添加至该分类中
//					classifyByshdkh_rybh.getXsddmxs().add(xsddmx);
//					continue;
//				}
//			}
//		}
	}

}
