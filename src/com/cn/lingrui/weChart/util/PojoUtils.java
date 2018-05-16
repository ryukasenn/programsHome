package com.cn.lingrui.weChart.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import org.dom4j.Element;

import com.cn.lingrui.weChart.pojo.base.ReqBaseMessage;
import com.cn.lingrui.weChart.pojo.req.ReqTextMessage;
import com.cn.lingrui.weChart.util.CommonUtils;
import com.cn.lingrui.weChart.weixin.mp.aes.AesException;
import com.cn.lingrui.weChart.weixin.mp.aes.WXBizMsgCrypt;

/**
 * 主要用于消息类型的父子之间的转型
 * @author lhc
 */
public class PojoUtils {
	
	
	
	/**
	 * 上转型
	 * @param type
	 * @return
	 */
	public static ReqBaseMessage choseSunPojo(String type) {
		
		ReqBaseMessage pojo = null;
		
		if(WeChatParames.TYPE_TEXT.equals(type)) {
			pojo = new ReqTextMessage();
		}
		return pojo;
	}
	
	/**
	 * 将解析消息封装到类中
	 * @param cls 子类
	 * @param map 解析的消息
	 * @return
	 * @throws Exception
	 */
	public static <T>T packageDate(Class<T> cls, Map<String , String> map) throws Exception {
		
		// 声明返回结果
		T t = cls.newInstance();
		
		// 遍历map
		for(String key : map.keySet()) {
			
			// 通过反射set方法,将结果注入封装中
			Field f = null;
			try {
				f = cls.getField(key);
			} catch (NoSuchFieldException e) {
			}
			if(f != null) {
				Method method = cls.getMethod("set"+key, f.getType());
				if(method != null) {
					method.invoke(t, map.get(key));
				}			
			}
		}
		return t;
	}
	
	public static Element packageToXml(Object bean, Element parentRoot) throws Exception {
		Class<?> cls = bean.getClass();
		Field[] fields = cls.getFields();
		for(Field field : fields) {
			
			// 节点名称和内容
			String rootName = field.getName();
			
			if("String".equals(field.getType().getSimpleName())) {
				
				// 节点的内容
				Method getMethod = cls.getMethod("get" + field.getName());
				String rootText = (String)getMethod.invoke(bean);
				
				// 1.创建节点
				Element sunRoot = parentRoot.addElement(rootName);
				sunRoot.addCDATA( rootText);
			} else {
				
				Element newPRoot = parentRoot.addElement(rootName);
				
				// 字节点的实体类
				Method getMethod = cls.getMethod("get" + field.getName());
				
				newPRoot = packageToXml(getMethod.invoke(bean), newPRoot);
			}
		}
		
		return parentRoot;
	}

	/**
	 * 获取时间戳
	 * @return
	 */
	public static String getTimeStamp() {
		
		return CommonUtils.getTimeStamp().substring(0, 10);
	} 
	
	/**
	 * 验证URL
	 * @param msgSignature 签名串，对应URL参数的msg_signature
	 * @param timeStamp 时间戳，对应URL参数的timestamp
	 * @param nonce 随机串，对应URL参数的nonce
	 * @param echoStr 随机串，对应URL参数的echostr
	 * 
	 * @return 解密之后的echostr
	 * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信�?
	 */
	public static String VerifyURL(String msgSignature, String timeStamp, String nonce, String echoStr) throws Exception {
		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(WeChatParames.TOKEN, WeChatParames.ENCODINGAESKEY, WeChatParames.CORPID);
		String s = wxcpt.VerifyURL(msgSignature, timeStamp, nonce, echoStr);
		return s;
	}

	/**
	 * 将公众平台回复用户的消息加密打包.
	 * <ol>
	 * 	<li>对要发�?�的消息进行AES-CBC加密</li>
	 * 	<li>生成安全签名</li>
	 * 	<li>将消息密文和安全签名打包成xml格式</li>
	 * </ol>
	 * 
	 * @param replyMsg 公众平台待回复用户的消息，xml格式的字符串
	 * @param timeStamp 时间戳，可以自己生成，也可以用URL参数的timestamp
	 * @param nonce 随机串，可以自己生成，也可以用URL参数的nonce
	 * 
	 * @return 加密后的可以直接回复用户的密文，包括msg_signature, timestamp, nonce, encrypt的xml格式的字符串
	 * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信�?
	 */
	public static String EncryptMsg(String replyMsg, String timeStamp, String nonce) throws Exception {
		WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(WeChatParames.TOKEN, WeChatParames.ENCODINGAESKEY, WeChatParames.CORPID);
		String s = wxcpt.EncryptMsg(replyMsg,timeStamp,nonce);
		return s;
	}
	
	/**
	 * 获取随机串
	 * @return
	 */
	public static String getNonce() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 获取随机ID
	 */
	public static String getUuidId() {
	
		return "lrid" + CommonUtils.getUUIDString().replaceAll("-", "");
	}
}
