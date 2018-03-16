
	/**
	 * Ajax的GET提交
	 * @param targetUrl
	 * @param param
	 * @param func
	 * @returns
	 */
	function AjaxForGet(targetUrl, param, func){


		$.ajax({
	        timeout : 5000,
	        type : "GET",
	        url : targetUrl,
	        data : param || "",
	        success : function(data){
	        	func(JSON.parse(data));
	        }
	        //注意：这里不能加下面这行，否则数据会传不到后台
	        //contentType:'application/json;charset=UTF-8',
	    });
	}
	
	/**
	 * 身份证验证
	 * @param idNum
	 * @returns
	 */
	function isIdCard(idNum){
		
		// 分界身份证数字
		var nums = idNum.split("");
		var checkNums = new Array("7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2");
		var realNums = new Array("1","0","X","9","8","7","6","5","4","3","2");
		var addResult = 0;
		for(var i = 0; i < nums.length - 1; i++){
			
			addResult += nums[i]*checkNums[i];
		}
		var result = addResult % 11;
		
		// 如果验证结果跟最后一位不匹配
		if (realNums[result] != nums[17]){
			
			return false;
		}
		return true;
	}
	
	
	/**
	 * 必填项目验证
	 * @param inputNames 输入框项目
	 * @param radioNames 单选框项目
	 * @param selectNames 下拉框项目
	 * @returns
	 */
	function necessaryCheck(inputNames, radioNames, selectNames){
		
		if(undefined == inputNames){
			
		} else {
			// 遍历必须check列表names
			for(var nameI = 0; nameI < inputNames.length; nameI++){
				
				// 获取必填项
				var $_checkItem = $("input[name='" + inputNames[nameI] + "']");
				
				// 验证填写内容
				if("" == $_checkItem.val().trim()){
					
					// 如果必填项为空
					$_checkItem.parents(".form-group").addClass("has-error");
					
					confirm("有必填项没有填写");
					return false;
				}
			}
		}
		
		if(undefined == radioNames){
			
		} else {
			
			// 遍历必须check列表names
			for(var nameI = 0; nameI < radioNames.length; nameI++){

				// 获取必填项
				var $_checkItem = $("input[name='" + radioNames[nameI] + "']:checked");
				
				// 验证填写内容
				if(undefined == $_checkItem.val()){

					// 如果没有选择
					$("input[name='" + radioNames[nameI] + "']").parents(".form-group").addClass("has-error");
					
					confirm("有必填项没有填写");
					return false;
				};
			}
		}
		
		if(undefined == selectNames){
			
		} else {
			
			// 遍历必须check列表names
			for(var nameI = 0; nameI < selectNames.length; nameI++){
				
				// 获取必填项
				var $_checkItem = $("select[name='" + selectNames[nameI] + "']");
				
				for(var i = 0; i < $_checkItem.length; i ++){
					
					// 验证填写内容
					if("block" == $_checkItem.eq(i).css("display")){
						
						if("" == $_checkItem.eq(i).val()){

							// 如果没有选择
							$_checkItem.eq(i).parents(".form-group").addClass("has-error");
							
							confirm("有必填项没有填写");
							return false;
						}
					}
				}
				
			}
		}
		return true;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	