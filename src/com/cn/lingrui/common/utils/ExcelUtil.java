package com.cn.lingrui.common.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import com.cn.lingrui.common.db.dbpojos.PMBASE;

import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil {

	/**
	 * 数据list转化成excel
	 * @param fileName
	 * @param extra 额外设置某一列标红
	 */
	public static void listToExcel(String fileName, List<PMBASE> list, String... extra ) {
		
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
			
			Field[] fields = PMBASE.class.getDeclaredFields();
			
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
				PMBASE pmbase = list.get(row - 1);
				for(Field f : fields) {
					
					// 将列名写入excel表中
					boolean flag = f.isAccessible();
					f.setAccessible(true);
					sheet.addCell(new Label(col, row, (String) f.get(pmbase)));
					col ++;
					f.setAccessible(flag);
				}
			}
			
			workbook.write();
			workbook.close();
			
		}catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
}
