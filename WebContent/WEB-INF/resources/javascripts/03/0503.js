$(function(){
	
	/**
	 * 查看当前终端点击事件
	 */
	$("#receiveAllPm").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/pm/allpm").attr("method", "GET").submit();
	})

	
})