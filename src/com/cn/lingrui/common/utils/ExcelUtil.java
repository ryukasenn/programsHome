package com.cn.lingrui.common.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cn.lingrui.common.db.dbpojos.BaseReport;
import com.cn.lingrui.common.db.dbpojos.PMBASE;
import com.cn.lingrui.weChart.pojo.duty.DutyPerson;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil {

	private static Logger log = LogManager.getLogger();
	/**
	 * List<T>转化成excel
	 * @param fileName
	 * @param list T类型的列表
	 * @param clasz T的类型
	 * @param extra 额外设置某一列标红
	 */
	public static <T> boolean listToExcel(String fileName, List<T> list, Class<T> clasz, String... extra ) {
		
		String fileNameTemp = GlobalParams.FILE_PATH + fileName + ".xls";
		File targetFile = new File(fileNameTemp);
		
		if(list.size() <= 0) {
			return false;
		}
		try {
			
			if(!targetFile.exists()) {
				
			} else {

				// 如果存在直接删除
				targetFile.delete();
			}

			// 创建新的excel文件
			targetFile.createNewFile();
			
			// 1.创建工作簿
			WritableWorkbook workbook = Workbook.createWorkbook(targetFile);
			
			// 2.创建工作表
			WritableSheet sheet = workbook.createSheet("fileName", 0);
			
			Field[] fields = clasz.getDeclaredFields();
			
			int count = 0;
			for(Field f : fields) {
				
				// 将列名写入excel表中
				WritableCell cell = new Label(count, 0, f.getName());
				
				if(0 != extra.length) {
					
					if(extra[0].equals(f.getName())) {
						
						 //创建一个单元格样式
				        WritableCellFormat writableCellFormat = new WritableCellFormat();
				        //设置样式的背景色为红色
				        writableCellFormat.setBackground(Colour.RED);
				        cell.setCellFormat(writableCellFormat);
					}
				}

				sheet.addCell(cell);
				count ++;
			}
			
			for(int row = 1; row <= list.size(); row ++) {
				
				int col = 0;
				T t = list.get(row - 1);
				for(Field f : fields) {
					
					// 将列名写入excel表中
					boolean flag = f.isAccessible();
					f.setAccessible(true);
					sheet.addCell(new Label(col, row, (String) f.get(t)));
					col ++;
					f.setAccessible(flag);
				}
			}
			
			workbook.write();
			workbook.close();
			
			return true;
		}catch (Exception e) {
			
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 存储过程生成报表转化成excel
	 * @param fileName
	 * @param extra 额外设置某一列标红
	 */
	public static void reportToExcel(String fileName, BaseReport report, String... extra ) {
		
		String fileNameTemp = GlobalParams.FILE_PATH + fileName + ".xls";
		File targetFile = new File(fileNameTemp);
		try {
			
			if(!targetFile.exists()) {
				
			} else {

				// 如果存在直接删除
				targetFile.delete();
			}

			// 创建新的excel文件
			targetFile.createNewFile();
			
			// 1.创建工作簿
			WritableWorkbook workbook = Workbook.createWorkbook(targetFile);
			
			// 2.创建工作表
			WritableSheet sheet = workbook.createSheet("fileName", 0);
			
			String [] titles = report.getTitle();
			
			int count = 0;
			for(String t : titles) {
				
				// 将列名写入excel表中
				WritableCell cell = new Label(count, 0, t);
				
				if(0 != extra.length) {
					
					if(extra[0].equals(t)) {
						
						 //创建一个单元格样式
				        WritableCellFormat writableCellFormat = new WritableCellFormat();
				        //设置样式的背景色为红色
				        writableCellFormat.setBackground(Colour.RED);
				        cell.setCellFormat(writableCellFormat);
					}
				}

				sheet.addCell(cell);
				count ++;
			}
			
			for(int row = 1; row <= report.getReportData().size(); row ++) {
				
				int col = 0;
				List<String> contents = report.getReportData().get(row - 1);
				for(String currentContent : contents) {
					
					// 写入每一行的内容
					sheet.addCell(new Label(col, row, currentContent));
					col ++;
				}
			}
			
			workbook.write();
			workbook.close();
			
		}catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static <T> List<T> excelToList(Class clasz,String fileName) throws Exception{
		
		String fileNameTemp = GlobalParams.FILE_PATH + fileName + ".xls";
		File targetFile = new File(fileNameTemp);
		// 查询所有地总
		if(!targetFile.exists()) {

			log.error(fileName + "文件不存在");
			throw new Exception();
		} else {
			try {
				
				Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
				
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
					int realColumns = sheet.getColumns();
					// 初始化
					for(int rowi = 0; rowi < realRows; rowi++) {
						List<String> currentList = new ArrayList<>();
						
						for(int columni = 0; columni < realColumns; columni++) {

							currentList.add(sheet.getCell(columni,rowi).getContents());
						}
						result.put(rowi, currentList);
					}
				}

				return null;
			}catch (Exception e) {
				log.error("excel转化list出错");
				throw new Exception();
			}
		}
		
	}
	
	public static List<DutyPerson> getList() throws Exception{
		
		String fileNameTemp = GlobalParams.FILE_PATH + "person.xls";
		File targetFile = new File(fileNameTemp);
		// 查询所有地总
		if(!targetFile.exists()) {

			log.error("文件不存在");
			throw new Exception();
		} else {
			try {
				
				List<DutyPerson> result = new ArrayList<DutyPerson>();
				
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
					// 初始化
					for(int rowi = 0; rowi < realRows; rowi++) {
						DutyPerson person = new DutyPerson();
						person.setName(sheet.getCell(0,rowi).getContents());
						person.setIndex(sheet.getCell(1,rowi).getContents());
						result.add(person);
					}
				}

				return result;
			}catch (Exception e) {
				CommonUtil.getTrace(e);
				log.error("excel转化list出错");
				throw new Exception();
			}
		}
	}
	
}









