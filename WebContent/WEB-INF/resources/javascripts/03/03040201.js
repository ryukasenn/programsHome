$(function(){
	
	/**
	 * 人员名称,修改点击事件(直接修改)
	 */
	$(".changePerson").on("click", function(){
		
		$("#changePersonPid").val($(this).val());
		$("#changePersonType").val('030403');
		$("#changePersonForm").attr("action", baseUrl + "/sellPersonnel/changePerson").attr("method", "POST").submit();
	})
	
	/**
	 * 人员名称,修改点击事件(驳回中修改)
	 */
	$(".changeRejectPerson").on("click", function(){
		
		$("#changePersonPid").val($(this).val());
		$("#changePersonType").val('030405');
		$("#changePersonForm").attr("action", baseUrl + "/sellPersonnel/changePerson").attr("method", "POST").submit();
	})
})