$(function(){

	/**
	 * 修改操作
	 */
	$("button[name='update']").on("click", function(){
		
		// 添加属性
		$("#NBPT_RSFZ_ROLE_ID").val($(this).parents("tr").find("td").eq(0).html());

		// 点击修改时,获取id,传入后台,走相应的form
		$("#controlForm").attr("action", baseUrl + "/sys/rmUpdateRole").attr("method", "GET").submit();
	});
	
	/**
	 * 删除操作
	 */
	$("button[name='delete']").on("click", function(){

		// 添加属性
		$("input[name='RSFZROLE_ID']").val($(this).parents("tr").find("td").eq(0).html());
		
		// 点击修改时,获取id,传入后台,走相应的form
		$("#controlForm").attr("action", baseUrl + "/sys/rmDeleteRole").attr("method", "POST");
		if(confirm("确定删除")){
			$("#controlForm").submit();
		};
		
	});
	
	/**
	 * 添加操作
	 */
	$("button[name='add']").on("click", function(){

		// 点击修改时,获取id,传入后台,走相应的form
		$("#controlForm").attr("action", baseUrl + "/sys/rmAddRole").attr("method", "GET").submit();
	});
	
});