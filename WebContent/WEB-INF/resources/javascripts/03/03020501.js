$(function(){
		
	/**
	 * 添加下辖省份
	 */
	$(".add").on("click", function(){
		
		// 负责区域的单选框点击事件
		AjaxForGet(baseUrl + "/sellPersonnel/regionController/receiveAreaContainSelects", {parentId : $("#changeRegion_provinceId").val()},function(jsonData){
			
			$("#citySelect").empty();
			for(var i = 0; i < jsonData.length; i++){
				
				$("<option value='" + jsonData[i].NBPT_COMMON_XZQXHF_ID + "'>" + jsonData[i].NBPT_COMMON_XZQXHF_NAME + "</option>").appendTo($("#citySelect"));
			}
			
			$("#030205Modal").modal('show')
		})
	})
	
	/**
	 * 提交添加
	 */
	$("#addAreaConfirm").on("click", function(){
		
		// 1.获取选择的市
		var cityValue = $("#citySelect").val();
		
		// 2.获取选择的区县
		var contyValue = $("#contySelect").val();
		
		// 如果市级选项为空,什么都不做
		if("" == cityValue){
			
			return ;
		} else {

			$("input[name='addAreaContain_regionUid']").val($("#changeRegion_regionUid").val());
			$("input[name='addAreaContain_regionId']").val($("#changeRegion_regionId").val());
			
			if("" == contyValue){
				new Confirm({
					
					"message" : "是否添加市级单位",
					"cancelCallBack" : function(){
						return;
					},
					"sureCallBack" : function(){

						if(checkRepeat(cityValue)){
	
							$("#addAreaContain").attr("action", baseUrl + "/sellPersonnel/regionController/addRegionXzqx").attr("method", "POST").submit();
						} else {
							
							new Confirm({
								"type" : "alert",
								"message" : "重复添加"
							});
						}
					}
				});
			} else {

				if(checkRepeat(contyValue)){

					$("#addAreaContain").attr("action", baseUrl + "/sellPersonnel/regionController/addRegionXzqx").attr("method", "POST").submit();
				} else {

					new Confirm({
						"type" : "alert",
						"message" : "重复添加"
					});
				}
			}
			
		}
	})
	
	/**
	 * 如果重复返回false
	 */
	function checkRepeat(value){
		
		var $_values = $(".delete");
		for(var i = 0; i < $_values.length; i++){
			
			if(value == $_values.eq(i).val()){
				
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 提交删除
	 */
	$(".delete").on("click", function(){

		// 1.传递地区编号
		var regionId = $("#changeRegion_regionId").val();

		// 2.传递区县编号
		var cityValue = $(this).val();
		
		// 3.传递地区32UID
		var regionUid = $("#changeRegion_regionUid").val();
		
		if(confirm("删除将同时删除负责该地区的终端相关信息")){

			window.location.href = baseUrl + "/sellPersonnel/regionController/deleteRegionXzqx?regionUid=" + regionUid + "&regionId=" + regionId + "&cityValue=" + cityValue;
		}
			
	})
	
	
	$("#citySelect").on("change",function (){
		
		var $_thisCity = $(this);

		// 判断选择
		if("" == $_thisCity.val().trim()){
			$("#contySelect").empty();
			return;
		}
		
		// 市级下拉框选择后,生成区县级下拉框
		AjaxForGet(baseUrl + "/sellPersonnel/regionController/receiveAreaContainSelects", {parentId : $_thisCity.val()},function(jsonData){
			
			$("#contySelect").empty();
			
			for(var i = 0; i < jsonData.length; i++){
				
				$("<option value='" + jsonData[i].NBPT_COMMON_XZQXHF_ID + "'>" + jsonData[i].NBPT_COMMON_XZQXHF_NAME + "</option>").appendTo($("#contySelect"));
			}
		})
	})
	
})