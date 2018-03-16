$(function(){
	
	/**
	 * 界面初始化,输入框自动补全
	 */ 
	var regions = AjaxForGet(baseUrl + "/sellPersonnel/receiveRegionsSelect","",function(jsonData){
		
		var regions = new Array();
		for(var i = 0; i < jsonData.length; i++){
			
			var regionId = jsonData[i].NBPT_SP_REGION_ID;
			var regionName = jsonData[i].NBPT_SP_REGION_NAME;
			
			regions.push(regionId + " " + regionName);
		}
		
		// 自动补全提示
		$("#regionsPickup").typeahead({
			source: regions,
			items: 5,
			updater: function(item){
			 return item;
			},
			delay: 300 
		});
		
		// 二次点击触发提示
		$("#regionsPickup").on("click", function(){
			$("#regionsPickup").typeahead("lookup")
		});
		
		return regions;
		
	})
	
	/**
	 * 查看当前终端点击事件
	 */
	$("#receiveRegions").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/regions").attr("method", "POST").submit();
	})
	
	$('#searchConfirm').on('click', function(){
		
		$_modalNameConfirmRadio = $("input[name='modalNameConfirm']:checked");
		if(undefined == $_modalNameConfirmRadio.val()){
			
		} else {
			
			var pid = $_modalNameConfirmRadio.val();
			var name = $_modalNameConfirmRadio.parents('td').next('td').children('p').html();
			$('#ResperName').val(name);
			$('#ResperPid').val(pid);
			$('#0302Modal').modal('hide')
			
		}
	})
})

/**
 * 添加负责人模态框
 * @param url
 * @param data
 * @returns
 */
function createModal(url, data, title){
	
	AjaxForGet(url, data, function(jsonData){
		
		if(0 == jsonData.length){
			
		}else {
			$('.bodyTable').empty();
			$('.modal-title').html(title);
			var $_table = $("<div>" +
								"<table class='table'>" +
									"<colgroup>" +
										"<col class='col-xs-1 col-sm-1'/>" +
										"<col class='col-xs-2 col-sm-2'/>" +
									"</colgroup>" +
									"<tr style='text-align:center'>" +
										"<td>#</td>" +
										"<td>姓名</td>" +
									"</tr>" +
								"</table>" +
							"</div>" +
							"<div style='overflow-x: auto; overflow-y: auto; height: 300px;' >" +
								"<table class='table tableBody'>" +
									"<colgroup>" +
										"<col class='col-xs-1 col-sm-1'/>" +
										"<col class='col-xs-2 col-sm-2'/>" +
									"</colgroup>" +
								"</table>" +
							"</div>").appendTo($('.bodyTable'));
			
			for(var i = 0; i < jsonData.length; i++){
				
				var $_tr = $("<tr style='text-align:center'></tr>").appendTo($('.tableBody'));
				$("<td><label><input type='radio' name='modalNameConfirm' value='" + jsonData[i].NBPT_SP_PERSON_PID + "'/></label></td>").appendTo($_tr);
				$("<td><p>" + jsonData[i].NBPT_SP_PERSON_NAME + "</p></td>").appendTo($_tr);
			}
			
			$('#0302Modal').modal('show')
		}		
	})
}


	
