$(function(){
	
	$("#moduleController").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sys/pmModule");
		$("#controlForm").submit();
	})
	
	$("#funcController").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sys/pmFunc");
		$("#controlForm").submit();
	})
	
	$("#pageController").on("click", function(){
		
		$("#controlForm").attr("action", baseUrl + "/sys/pmPage");
		$("#controlForm").submit();
	})
	
	$('#0106Modal').on('hidden.bs.modal', function(e){
		
		$('#0106Modal').find(".modal-body").empty();
		$('#0106Modal').find(".modal-title").html("");
	})
	
	$('#0106Modal').find("#confirm").on('click', function(){
		
		$('#0106Modal').find("form").submit();
	})
})


	/**
	 * 页面管理弹出框统一生成方法
	 * @param obj
	 * @returns
	 */
	function createForm(obj){
		
		var $_modalForm = $("<form></form>",{
			id : 'modalForm',
			action : baseUrl + "/sys/" + obj.type + obj.page,
			method : 'POST'
		});
		
		for(var i = 0 ; i < obj.inputs.length; i ++){
			var input = obj.inputs[i];
			var $_thisInput = $("<div class='form-group'>" +
					"<label for='" + input.inputLableFor + "'>" + input.inputLableHtml + "</label>" +
					"<input id='" + input.inputId + "' class='form-control' name='" + input.inputId + "' ></div>");
			
			if(obj.type == 'add'){
				
				$_thisInput.find('input').attr("placeholder", input.inputVal)
			} else {
				$_thisInput.find('input').val(input.inputVal);
			}
			$_modalForm.append($_thisInput);
		}
		$_modalForm.appendTo(obj.modal.find(".modal-body"));
		

		if(obj.title != undefined){
			
			obj.modal.find(".modal-title").html(obj.title)
		}
		
		return $_modalForm;
	}
