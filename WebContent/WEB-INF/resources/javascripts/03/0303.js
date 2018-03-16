$(function(){
	
	/**
	 * 查看当前终端点击事件
	 */
	$("#receiveCurrents").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/areas").attr("method", "GET").submit();
	})
	
})