$(function(){
	
	/**
	 * 选择省份时,查询是否有废弃页面
	 */
	$("select[name='provinceId']").on('change', function(){
		
		$_this = $(this);
		if('' == $_this.val()){}
		else{
			AjaxForGet(baseUrl + "/sellPersonnel/regionController/checkRegion",{type:'2', provinceId:$_this.val()},function(jsons){
				
				if(0 == jsons.length){
					alert('暂时没有废弃的地区')
				} else {
					new Confirm({
					    message: $_this.find("option:selected").text() + '有废弃的地区,请直接修改',
						cancelCallBack:function(){},
						sureCallBack:function(){
							window.location.href = baseUrl + "/sellPersonnel/regionController/changeRegion?regionUid=" + jsons[0].NBPT_SP_REGION_UID;
						}
					});
			    }
			});
		}
	});
	/**
	 * 添加负责人按钮
	 */
	$('.addResponsibler').on('click', function(){
		
		$_this = $(this);
		
		createModal(baseUrl + "/sellPersonnel/regionController/receiveRegionReper",{personType : '22'}, '可选地总名单');
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
	 * 添加地区
	 */
	$("#addAreaConfirm").on("click", function(){
		
		$('#addArea').attr('action', baseUrl + "/sellPersonnel/regionController/addArea").attr('method', 'POST').submit();
	})
	
})