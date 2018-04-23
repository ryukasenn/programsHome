$(function(){
	
	/**
	 * 人员配额初始化输入
	 */
//	AjaxForGet(baseUrl + "/sellPersonnel/receiveAreaContainSelects", {parentId : $("#provinceId").val()},function(jsonData){
//		
//		for(var i = 0; i < jsonData.length; i++){
//			
//		}
//		
//	})
	
	/**
	 * 添加负责人按钮
	 */
	$('.addResponsibler').on('click', function(){
		
		$_this = $(this);
		
		createModal(baseUrl + "/sellPersonnel/regionController/receiveRegionReper",{personType : '26'}, '大区总名单');
	});
	
	/**
	 * 删除负责人按钮
	 */
	$('.deleteResponsibler').on('click', function(){
				
		$('#ResperName').val("");
		$('#ResperPid').val("");
	});

	
	/**
	 * 添加负责人modal的查询操作
	 */
	$("#searchResper").on('click', function(){
		
		if('' == $('#searchName').val().trim()){
			
		} else {
			
			createModal(baseUrl + "/sellPersonnel/regionController/receiveRegionReper",{personType : '26', searchName : $('#searchName').val().trim()}, '大区总名单');
		}
	});
	
	
	/**
	 * 大区基本信息修改
	 */
	$("#addRegionConfirm").on("click", function(){
		
		$('#addRegion').attr('action', baseUrl + "/sellPersonnel/regionController/addRegion").attr('method', 'POST').submit();
	})
	
})