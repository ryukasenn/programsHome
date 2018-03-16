$(function(){
	
	/**
	 * 本文件为管理员单独使用
	 */ 
	$("#exout").on("click", function(){
		
		// 点击导出按钮时
		$("#checkBoxForm").attr("action","http://10.0.9.63:9000/rsManage/report/exout");
		$("#checkBoxForm").submit();
	});
})