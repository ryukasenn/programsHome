package com.cn.lingrui.rsfz.pojos.report;

import org.springframework.web.multipart.MultipartFile;

public class BatchUpdateIn {

	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
}
