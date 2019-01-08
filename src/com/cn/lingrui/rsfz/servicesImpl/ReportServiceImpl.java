package com.cn.lingrui.rsfz.servicesImpl;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cn.lingrui.common.db.dao.ConditionDao;
import com.cn.lingrui.common.db.dao.ExoutCheckItemsDao;
import com.cn.lingrui.common.db.dbpojos.PMBASE;
import com.cn.lingrui.common.pojos.exoutCheckItems.CheckItemsIn;
import com.cn.lingrui.common.pojos.exoutCheckItems.CheckItemsOut;
import com.cn.lingrui.common.pojos.exoutCheckItems.Items;
import com.cn.lingrui.common.utils.CommonUtil;
import com.cn.lingrui.common.utils.DBUtils;
import com.cn.lingrui.common.utils.ExcelUtil;
import com.cn.lingrui.common.utils.GlobalParams;
import com.cn.lingrui.common.utils.HttpUtil;
import com.cn.lingrui.rsfz.db.dao.ReportDao;
import com.cn.lingrui.rsfz.pojos.report.AgeReport;
import com.cn.lingrui.rsfz.pojos.report.BatchUpdateIn;
import com.cn.lingrui.rsfz.pojos.report.ReportPojoIn;
import com.cn.lingrui.rsfz.pojos.report.ReportPojoOut;
import com.cn.lingrui.rsfz.pojos.report.WorkAgeReport;
import com.cn.lingrui.rsfz.services.RSFZBaseService;
import com.cn.lingrui.rsfz.services.ReportService;
import com.cn.test.XZQXPOJO;

import jxl.Sheet;
import jxl.Workbook;

@Service("reportService")
public class ReportServiceImpl extends RSFZBaseService implements ReportService{

	@Resource(name="reportDao")
	private ReportDao reportDao;

	@Resource(name="conditionDao")
	private ConditionDao conditionDao;

	@Resource(name = "exoutCheckItemsDao")
	private ExoutCheckItemsDao checkItemsDao;
	
	@Override
	protected String getFunNum() {
		return "020202";
	}
	
	private static Logger log = LogManager.getLogger();
	
	/**
	 * 生成报表
	 */
	public ModelAndView createReport(ReportPojoIn in) throws Exception {
		
		this.before();
		
		log.info("用户"+ this.getUserName() +"执行了生成报表功能");
		
		// 报表页面业务流程
		// 初始化返回数据
		ReportPojoOut out = new ReportPojoOut();
		
		ModelAndView mv = HttpUtil.getModelAndView("02/" + this.getCheckPage("020202"));

		// 1.获取报表所需要的数据
		try {
			
			out.setListPMBASE(reportDao.getReportData(getConnection()));
		} catch (SQLException e) {
			
			this.closeException();
			throw new Exception();
		}

		// 2.生成分类数据
		this.reportDataClassify(in, out);
		
		// 3.分配分类数据
		mv.addObject("report", out.getReportData());
		mv.addObject("messages", out.getMessages());
		
		return this.after(mv);
	}

	/**
	 * 分类报表所需数据
	 * 
	 * @param out
	 * @throws SQLException 
	 */
	private void reportDataClassify(ReportPojoIn in, ReportPojoOut out) throws SQLException {

		for (String require : in.getChoses()) {

			// 年龄构成
			if ("reportByAge".equals(require)) {
				
				// 生成列标题
				List<String> condition = DBUtils.receiveCondition("01", conditionDao, getConnection());
				List<String> title = new ArrayList<String>();
				title.add("人事一级部门");
				for(int i = 0; i <= condition.size(); i ++) {
					
					if(i == 0) {
						
						title.add(condition.get(i) + "岁以下");
					} else if(i == condition.size()) {
						
						title.add(condition.get(i - 1) + "岁以上");
					} else {
						
						title.add(condition.get(i - 1) + "-" + CommonUtil.subtract(condition.get(i), "1") + "岁");
					}
				}
				AgeReport ageReport = new AgeReport();
				ageReport.setTitle(title);
				// 获取报表
				out.getReportData().setAgeReport(reportDao.getAgeReport("reportByAge", getConnection(), ageReport));
			} 
			
			// 学历构成
			else if ("reportByXl".equals(require)) {

				// 获取学历报表数据
				out.getReportData().setXlReport(reportDao.getXlReport("reportByXl", getConnection()));
			}
			
			// 职称构成
			else if ("reportByZhicheng".equals(require)) {
				
			}
			
			// 工龄构成
			else if ("reportByWorkAge".equals(require)) {
				// 生成列标题
				List<String> condition = DBUtils.receiveCondition("02", conditionDao, getConnection());
				List<String> title = new ArrayList<String>();
				title.add("人事一级部门");
				for(int i = 0; i <= condition.size(); i ++) {
					
					if(i == 0) {
						
						title.add(condition.get(i) + "年以下");
					} else if(i == condition.size()) {
						
						title.add(condition.get(i - 1) + "年以上");
					} else {
						
						title.add(condition.get(i - 1) + "-" + condition.get(i) + "年");
					}
				}
				WorkAgeReport workAgeReport = new WorkAgeReport();
				workAgeReport.setTitle(title);
				// 获取学历报表数据
				out.getReportData().setWorkAgeReport(reportDao.getWorkAgeReport("reportByWorkAge", getConnection(),workAgeReport));
			}
			
			// 性别构成
			else if ("reportBySex".equals(require)) {
				// 获取学历报表数据
				out.getReportData().setSexReport(reportDao.getSexReport("reportBySex", getConnection()));
			}
			
			// 工种构成
			else if ("reportByWorkType".equals(require)) {
				
			}
			
			// 职务级别
			else if ("reportByWorkLevel".equals(require)) {

				out.getReportData().setWorkLevelReport(reportDao.getWorkLevelReport("reportByWorkLevel", getConnection()));
			}

		}

	}


	/**
	 * 导出相关有误数据
	 */
	@Override
	public ModelAndView importOutWrongData(CheckItemsIn in) throws Exception {

		this.before();
		
		// 初始化返回数据
		ModelAndView mv = HttpUtil.getModelAndView("02/" + this.getCheckPage("020203"));

		// 初始化处理数据
		CheckItemsOut out = new CheckItemsOut();

		// 根据checkboxs选项分别查询相应数据
		try {
			
			this.getDataClassifyByCheckbox(in, out);

			// 分类处理
			this.dealWithData(out);

			// 返回数据
			mv.addObject("checkData", out.getItemsOut());

			return this.after(mv);
			
		} catch (Exception e) {
			
			this.closeException();
			throw new Exception();
		}
	}

	/**
	 * 分类获取相关数据
	 * 
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	private void getDataClassifyByCheckbox(CheckItemsIn in, CheckItemsOut out) throws Exception {

		for (String require : in.getChoses()) {

			// 年龄构成
			if ("reportByAge".equals(require)) {

				// 获取年龄相关错误数据
				out.setItemsByAge(checkItemsDao.getCheckItems("checkItemsByAge", getConnection()));

			}

			// 学历构成
			else if ("reportByXl".equals(require)) {

				// 获取学历相关错误数据
				out.setItemsByXl(checkItemsDao.getCheckItems("checkItemsByXl", getConnection()));
			}

			// 职称构成
			else if ("reportByZhicheng".equals(require)) {

			}

			// 工龄构成
			else if ("reportByWorkAge".equals(require)) {

				// 获取学历相关错误数据
				out.setItemsByWorkAge(checkItemsDao.getCheckItems("checkItemsByWorkAge", getConnection()));
			}

			// 性别构成
			else if ("reportBySex".equals(require)) {

			}

			// 工种构成
			else if ("reportByWorkType".equals(require)) {

			}

			// 职务级别
			else if ("reportByWorkLevel".equals(require)) {

				// 获取职务级别相关错误数据
				out.setItemsByWorkLevel(checkItemsDao.getCheckItems("checkItemsByWorkLevel", getConnection()));
			}

		}

	}

	/**
	 * 分类处理返回数据
	 * 
	 * @param out
	 */
	private void dealWithData(CheckItemsOut out) {

		// 处理年龄相关数据
		if (null != out.getItemsByAge()) {

			// 初始化数据
			List<Items> ageData = new ArrayList<Items>();

			for (PMBASE pmbase : out.getItemsByAge()) {

				Items item = new Items();

				item.setPMBASE_ZGBH(pmbase.getPMBASE_ZGBH());
				item.setPMBASE_ZGXM(pmbase.getPMBASE_ZGXM());
				item.setPMBASE_CHECKITEM(pmbase.getPMBASE_ZJHM());

				ageData.add(item);
			}

			out.getItemsOut().setCheckItemsByAge(ageData);

			//ExcelUtil.listToExcel("证件有误", out.getItemsByAge(), "PMBASE_ZJHM");
		}
		
		// 处理学历相关数据
		if (null != out.getItemsByAge()) {

			// 初始化数据
			List<Items> xlData = new ArrayList<Items>();

			for (PMBASE pmbase : out.getItemsByXl()) {

				Items item = new Items();

				item.setPMBASE_ZGBH(pmbase.getPMBASE_ZGBH());
				item.setPMBASE_ZGXM(pmbase.getPMBASE_ZGXM());
				item.setPMBASE_CHECKITEM(pmbase.getPMBASE_XL());

				xlData.add(item);
			}

			out.getItemsOut().setCheckItemsByXl(xlData);

			//ExcelUtil.listToExcel("学历有误", out.getItemsByXl(), "PMBASE_XL");
		}

		// 处理工龄相关数据
		if (null != out.getItemsByWorkAge()) {

			// 初始化数据
			List<Items> workAgeData = new ArrayList<Items>();

			for (PMBASE pmbase : out.getItemsByWorkAge()) {

				Items item = new Items();

				item.setPMBASE_ZGBH(pmbase.getPMBASE_ZGBH());
				item.setPMBASE_ZGXM(pmbase.getPMBASE_ZGXM());
				item.setPMBASE_CHECKITEM(pmbase.getPMBASE_RZRQ());

				workAgeData.add(item);
			}

			out.getItemsOut().setCheckItemsByWorkAge(workAgeData);

			//ExcelUtil.listToExcel("入职日期有误", out.getItemsByWorkAge(), "PMBASE_RZRQ");
		}

		// 处理职务级别相关数据
		if (null != out.getItemsByWorkLevel()) {

			// 初始化数据
			List<Items> WorkLevelData = new ArrayList<Items>();

			for (PMBASE pmbase : out.getItemsByWorkLevel()) {

				Items item = new Items();

				item.setPMBASE_ZGBH(pmbase.getPMBASE_ZGBH());
				item.setPMBASE_ZGXM(pmbase.getPMBASE_ZGXM());
				item.setPMBASE_CHECKITEM(pmbase.getPMBASE_FZ06());

				WorkLevelData.add(item);
			}

			out.getItemsOut().setCheckItemsByWorkLevel(WorkLevelData);

			//ExcelUtil.listToExcel("职务级别有误", out.getItemsByWorkLevel(), "PMBASE_FZ06");
		}
		
	}

	/**
	 * 上传批量修改数据
	 */
	@Override
	public ModelAndView upLoadBatchUpdate(BatchUpdateIn in) throws Exception {

		ModelAndView mv = HttpUtil.getModelAndView("02/02020102");
		
		MultipartFile file = in.getFile();
		if(!file.isEmpty()) {
            
			/**
			 * 1.保存文件
			 */
			
			//上传文件路径
			String path = GlobalParams.FILE_PATH_UP;
            
            //保存文件名
            String filename = file.getOriginalFilename().replace(".xls", CommonUtil.getYYYYMMDDHHMMSS() + ".xls");
            
            File filepath = new File(path,filename);
            
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) { 
            	
                filepath.getParentFile().mkdirs();
            }
            
            //将上传文件保存到一个目标文件当中
            file.transferTo(new File(path + File.separator + filename));
            
            /**
             * 2.处理文件,生成批量更新sql
             */
            // 读取文件
    		String fileNameTemp = path + filename;
    		
    		File targetFile = new File(fileNameTemp);
    		
			if(!targetFile.exists()) {
				
			} else {

				// 如果存在,获取工作簿
				Workbook book = null;
				book = Workbook.getWorkbook(targetFile);
				
				// 获取sheet页
				int maxSheet = book.getNumberOfSheets();
				for(int sheeti = 0; sheeti < maxSheet; sheeti++){
					
					// 获取sheet页
					Sheet sheet = book.getSheet(sheeti); 
					
					// 获取行数
					int realRows = sheet.getRows();

					// 获取列数
					int realColumns = sheet.getColumns();
					
					// 设定要更新列
					// 初始化
					Map<String, PMBASE> pmbase = new HashMap<String,PMBASE>();

					for(int rowi = 0; rowi < realRows; rowi++) {
					}
				}
			}
			
        } else {
        	// TODO
        }
		return this.after(mv);
	}

}
