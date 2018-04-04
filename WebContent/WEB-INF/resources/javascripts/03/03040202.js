$(function(){
	
	
	// 获取需要统计的列
	var statisticsItems = new Array("OTC_regionResper_total",
									"OTC_areaResper_total",
									"OTC_xzquResper_total",
									"OTC_xzquResper_preparatory_total",
									"OTC_promote_total",
									"OTC_heji_total",
									"OTC_need_total",
									"OTC_dismission_total");

	/**
	 * 初始化,合计列表统计
	 */
	for(var i = 0; i < statisticsItems.length; i++){
		
		var thisItem = statisticsItems[i];
		var thisTarget = thisItem.replace(/_total/, "")
		var statistics = $("." + thisTarget);
		var sum = 0;
		statistics.each(function(){
			
			sum += parseInt($(this).html());
		})
		$("." + thisItem).html(sum);
	}
	
	$(".downLoadCheckOut").on('click', function(){
		
		tableToExcel($(this).val());
	})
	
	
})