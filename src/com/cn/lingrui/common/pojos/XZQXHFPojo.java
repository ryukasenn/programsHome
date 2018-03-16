package com.cn.lingrui.common.pojos;

import java.util.List;

public class XZQXHFPojo {

	private String id;
	private String name;
	private List<XZQXHFPojo> contents = null;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<XZQXHFPojo> getContents() {
		return contents;
	}
	public void setContents(List<XZQXHFPojo> contents) {
		this.contents = contents;
	}
	
	
}
