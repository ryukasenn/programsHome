$(function(){
	
	var data = ["北京 01", "天津 02", "河北 03", "山西 04", "内蒙古自治区 05", "辽宁 06", "吉林 07", "黑龙江 08", "上海 09", "江苏 10", "浙江 11", "广东 19", "广西 20", "海南 21", "重庆 22", "四川 23", "贵州 24", "云南 25", "西藏 26", "陕西 27", "甘肃 28", "青海 29", "宁夏 30", "新疆 31", "香港特别行政区 32", "澳门特别行政区 33", "安徽 12", "福建 13", "江西 14", "山东 15", "河南 16", "湖北 17", "湖南 18", "其它 99"];  

	/**
	 * 提交事件
	 */
	$('#confirm').on('click', function(){
		if(data.indexOf($("#province").val()) == -1 || $("#timeEnd").val() == '' || $("#timeEnd2").val() == ''){$.nbptMsg.alert("请填写正确的省份并选择时间");}

		else {$.nbptAjax.post(baseUrl + "/zdhx/zdhxByProvince", formItemsToParams($('#zdhxByProvince')), function(backData){});}
	});
	
	// 自动补全提示
	$("#province").typeahead({
		source: data,
		items: 5,
		updater: function(item){
		 return item;
		},
		delay: 300 
	});
	
	// 二次点击触发提示
	$("#province").on("click", function(){
		$("#province").typeahead("lookup")
	});
	
	
	$("#timeEnd").datetimepicker({
		autoclose: true,
		language: 'zh',
		format: 'yyyy-mm-dd',
		minView: 'month',
		todayHighlight:true
	});
	$("#timeEnd2").datetimepicker({
		autoclose: true,
		language: 'zh',
		format: 'yyyy-mm-dd',
		minView: 'month',
		todayHighlight:true
	});
})



	
