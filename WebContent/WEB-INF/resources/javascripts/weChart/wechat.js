$(function(){
	var rootstr = window.location.href.substr(0, 37);
	var weixincheck = false;
	$.ajax({
		url : rootstr + 'jscheck',
		type : 'GET',
		data: {
			localurl : window.location.href
				},
		timeout : 5000,
		success : function(res){
			var resResult = JSON.parse(res);
			wx.config({
				debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			    appId: resResult.appId, // 必填，企业微信的cropID
			    timestamp: resResult.timestamp, // 必填，生成签名的时间戳
			    nonceStr: resResult.nonceStr, // 必填，生成签名的随机串
			    signature: resResult.signature,// 必填，签名，见附录1
			    jsApiList: ['onMenuShareAppMessage', 'chooseImage','scanQRCode']   
			});
			wx.ready(function(){
				wx.checkJsApi({
				    jsApiList: ['scanQRCode'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
				    success: function(res) {
				        // 以键值对的形式返回，可用的api值true，不可用为false
				        // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
				    	if(res.checkResult.scanQRCode){
				    		
				    	}else{
				    		alert("不支持支持扫一扫功能");
				    	}
				    }
				});
			});
		}				
	})
})