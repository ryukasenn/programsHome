$(function(){
	
	$("#confirm").on("click", function(){
		
		// 点击确认修改按钮
		$("#controlForm").attr("action", baseUrl + "/sys/rmUpdateRole").attr("method", "POST").submit();
	})
	
	// 初始化操作,权限设置
	function init(){
		
		// CheckBox选中
		// 模块状态
		if('[]' != modules){
			modules = modules.replace(/\[/g, "").replace(/\]/g, "");
			modules = modules.split(", ");
			for(var i= 0; i < modules.length; i ++){
				
				var checkedModuleId = modules[i];
				$("#" + checkedModuleId).prop("checked", true);
			}
		}
		// 功能状态
		if('[]' != funcs){
			funcs = funcs.replace(/\[/g, "").replace(/\]/g, "");
			funcs = funcs.split(", ");
			for(var i= 0; i < funcs.length; i ++){
				
				var checkedFuncId = funcs[i];
				$("#" + checkedFuncId).prop("checked", true);
			}			
		}

		if('[]' != funcs){
			// 页面状态
			pages = pages.replace(/\[/g, "").replace(/\]/g, "");
			pages = pages.split(", ");
			for(var i= 0; i < pages.length; i ++){
				
				var checkedPageId = pages[i];
				$("#" + checkedPageId).prop("checked", true);
			}
		}
	}
	
	init();
	
	// 选择模块时的动作
	$("input[name='modules']").on("click", function(){
		
		// 初始化
		var $_this = $(this);
		
		// 模块ID
		var $_thisId = $_this.attr("id");
		
		// 获取默认功能ID
		var $_this_funcId = $_thisId + '01';
		
		// 获取默认页面ID
		var $_this_pageId = $_this_funcId + '0101';
		
		// 1.检测点击之后状态
		if($_this.is(":checked")){
			
			// 选中默认功能和默认首页
			$("#" + $_this_funcId).prop("checked", true);
			$("#" + $_this_pageId).prop("checked", true);
			
		} else {
			
			// 该模块下所有功能不选
			$("input[id^='" + $_thisId + "']").prop("checked", false);
			
		}
	})
	
	// 选择功能时的动作
	$("input[name='funcs']").on("click", function(){
		
		// 初始化
		var $_this = $(this);
		
		// 功能ID
		var $_thisId = $_this.attr("id");
		
		// 所在模块ID
		var $_this_moduleId = $_thisId.substring(0,2);
		
		// 页面ID
		var $_this_pageId = $_thisId + "0101";

		// 1.检测点击之后状态
		if($_this.is(":checked")){

			// 如果选中,则选中该功能下页面
			$("#" + $_this_pageId).prop("checked", true);
			
			// 同时选中所在模块
			$("#" + $_this_moduleId).prop("checked", true);
			
			// 判断是否是默认功能
			if("01" == $_thisId.substring(2, 4)){
				
			} else {
				
				// 如果不是默认功能,同时还要选中默认功能,和默认页面
				$("#" + $_this_moduleId + '01').prop("checked", true);
				$("#" + $_this_moduleId + '010101').prop("checked", true);
			}
		} else {
			
			// 如果是取消,判断是否是默认功能
			if("01" == $_thisId.substring(2, 4)){
				
				// 如果是默认功能,删除该模块所有功能及页面
				if(comfirm("删除默认功能,将删除该模块所有功能")){

					$("input[id^='" + $_this_moduleId + "']").prop("checked", false);
				} else {
					
					// donothing
				}
				
			} else {

				// 如果不是默认,则只删除该功能下的页面
				$("input[id^='" + $_thisId + "']").prop("checked", false);
			}
		}
	});

	// 选择页面时的动作
	$("input[name='pages']").on("click", function(){

		// 初始化
		var $_this = $(this);
		
		// 选择页面ID
		var $_thisId = $_this.attr("id");
		
		// 所在功能ID
		var $_this_FuncId = $_thisId.substring(0, 4);

		// 所在模块ID
		var $_this_ModuleId = $_thisId.substring(0, 2);

		// 1.检测点击之后状态
		if($_this.is(":checked")){

			// 要同时选中所在模块和功能
			$("#" + $_this_ModuleId).prop("checked", true);
			$("#" + $_this_FuncId).prop("checked", true);

			
			// 如果是默认页面
			if("01" == $_thisId.substring(4, 6)){
				
				if("01" == $_thisId.substring(2, 4)){
					
					// 如果是默认功能,同类页面取消
					$("input[id^='" + $_thisId.substring(0, 6) + "']").prop("checked", false)
					$_this.prop("checked", true);
				} else {
					
					// 如果不是默认功能,还要选中默认功能
					$("#" + $_this_ModuleId + "010101").prop("checked", true);
					$("#" + $_this_ModuleId + "01").prop("checked", true);
				}
			} else {
				
				// 如果不是默认页面,还要选中默认
				$("#" + $_this_ModuleId + "010101").prop("checked", true);
				$("#" + $_this_ModuleId + "01").prop("checked", true);
			}
			
		} else {
			
			// 如果是取消,则判断是否默认
			if("01" == $_thisId.substring(4, 6)){
				
				// 如果是功能默认,还要取消功能及功能下的默认页面
				if(confirm("删除默认页面,将删除该功能下所有页面")){
					
					// 判断是否是默认功能
					if("01" == $_thisId.substring(2, 4)){
						
						// 如果是默认功能,删除该模块所有功能
						$("input[id^='" + $_this_ModuleId + "']").prop("checked", false)
					} else {
						
						// 如果不是默认功能,只删除该功能下页面
						$("input[id^='" + $_this_FuncId + "']").prop("checked", false)
					}
				}
			}
		}
	});
	
	
	
	
})



























