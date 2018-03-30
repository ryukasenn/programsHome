$(function(){
	
	/**
	 * 查看当前终端点击事件
	 */
	$("#receivePersons").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/provincePersons").attr("method", "GET").submit();
	})
})