$(function(){
	
	/**
	 * 查看当前终端点击事件
	 */
	$("#receivePersonalPm").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/pm/grpm").attr("method", "GET").submit();
	})

	
})