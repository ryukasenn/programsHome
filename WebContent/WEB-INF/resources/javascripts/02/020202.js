$(function(){

	/**
	 * 生成报表操作
	 */
	$("button[name='createReport']").on("click", function(){
				
		// 点击修改时,获取id,传入后台,走相应的foru
		$("#controlForm").attr("action", baseUrl + "/report").attr("method", "POST").submit();
	});
	
	/**
	 * 导出操作
	 */
	$("button[name='importOut']").on("click", function(){
				
		// 点击修改时,获取id,传入后台,走相应的foru
		$("#controlForm").attr("action", baseUrl + "/report/exout").attr("method", "POST").submit();
	});
	
	$('#020202Modal').find("#020202ModalConfirm").on('click', function(){
		
		$('#020202Modal').find("form").submit();
	})
		
	$("button[name='importIn']").on("click", function(){
		
		$('#020202Modal').modal('show')
	});
	
	
})