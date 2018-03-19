$(function(){
	
	/**
	 * 人员名称点击事件
	 */
	$(".changePerson").on("click", function(){
		
		$("#changePersonPid").val($(this).val());
		$("#changePersonForm").attr("action", baseUrl + "/sellPersonnel/changePerson").attr("method", "POST").submit();
	})
})