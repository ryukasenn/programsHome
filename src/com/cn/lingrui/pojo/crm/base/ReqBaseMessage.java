package com.cn.lingrui.pojo.crm.base;


/**
 * 消息类型父类
 * @author lhc
 *
 */
public class ReqBaseMessage {

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
	 * 消息id，64位整型
	 */
	public String MsgId;
	/**
	 * 企业应用的id，整型
	 */
	public String AgentID;
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
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	public String getAgentID() {
		return AgentID;
	}
	public void setAgentID(String agentID) {
		AgentID = agentID;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	
}
