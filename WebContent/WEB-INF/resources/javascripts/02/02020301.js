$(function(){


	// name为exoutFile的按钮点击事件绑定
	$("button[name='exoutFile']").on("click", function(){
		
		// 1.当点击按钮时,获取相邻的hiddenInput的value值
		$_this = $(this);
		$_this.next("form").submit();
	})
})
