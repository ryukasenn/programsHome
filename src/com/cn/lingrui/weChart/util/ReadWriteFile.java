package com.cn.lingrui.weChart.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ReadWriteFile {

	public void write(String filePath, String content) {
		
		File file = new File(filePath);
		createFile(file);
		
		try {
			FileWriter write = new FileWriter(file);
			
			write.write(content);
			write.flush();
			write.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	private void createFile(File fileName) {
		
		try {
			if(!fileName.exists()){
				
				fileName.createNewFile();
			}
			
		}catch (Exception e){
			
		}
		
	}
}
