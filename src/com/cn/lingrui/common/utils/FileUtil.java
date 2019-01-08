package com.cn.lingrui.common.utils;

import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	
	/**
	 * 调查问卷上传方法
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public static void saveFile(MultipartFile file, String rootPath, String filename) throws Exception {
		
		File filedir = new File(rootPath);
		
		//判断上传文件的保存目录是否存在
		if (!filedir.exists() && !filedir.isDirectory()) {
		    //创建目录
		    filedir.mkdir();
		}
		
		file.transferTo(new File(rootPath + filename));

	}
}
