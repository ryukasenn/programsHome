package com.cn.lingrui.weChart.pojo.base;


/**
 * 消息类型父类
 * @author lhc
 *
 */
public class RespBaseMessage {

	/**
	 * 企业微信CorpID
	 */
	public String ToUserName; 
	/**
	 * 消息创建时间（整型）
	 */
	public String CreateTime; 
	/**
	 * 成员UserID
	 */
	public String FromUserName;
	/**
	 * 消息的类型
	 */
	public String MsgType;
	
	/**
	 * get set
	 */
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	
}
