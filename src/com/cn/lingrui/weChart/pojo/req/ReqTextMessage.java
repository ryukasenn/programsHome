package com.cn.lingrui.weChart.pojo.req;

import com.cn.lingrui.weChart.pojo.base.ReqBaseMessage;

/**
 * 文本消息类型
 * @author lhc
 *
 */
public class ReqTextMessage extends ReqBaseMessage {

	/**
	 * 消息的内容
	 */
	public String Content;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
}
