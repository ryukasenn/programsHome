$(function(){

	/**
	 * 初始化检测是否有废弃大区
	 */
	AjaxForGet(baseUrl + "/sellPersonnel/regionController/checkRegion",{type:'1'},function(jsons){
		
		if(0 == jsons.length){
			
		} else {
			new Confirm({
			    message:'有废弃的大区,请直接修改',
				cancelCallBack:function(){},
				sureCallBack:function(){
					window.location.href = baseUrl + "/sellPersonnel/regionController/changeRegion?regionUid=" + jsons[0].NBPT_SP_REGION_UID;
				}
			});
	    }
	});
	
	/**
	 * 添加负责人按钮
	 */
	$('.addResponsibler').on('click', function(){
		
		$_this = $(this);
		
		createModal(baseUrl + "/sellPersonnel/regionController/receiveRegionReper",{personType : '26'}, '可选大区总名单');
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