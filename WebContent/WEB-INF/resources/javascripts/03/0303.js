$(function(){
	
	/**
	 * 查询未审核列表
	 */
	$("#receiveUnchecks").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/support/receiveUnchecks").attr("method", "GET").submit();
	})
	
	
})