$(function(){
	
	// 必填项目列表
	var inputItems = new Array("NBPT_SP_PERSON_NAME","NBPT_SP_PERSON_IDNUM","NBPT_SP_PERSON_PLACE",
							   "NBPT_SP_PERSON_ENTRYDATA","NBPT_SP_PERSON_LOGINID")

	// 需要验证的时间格式
	var timeItems = new Array("NBPT_SP_PERSON_ENTRYDATA");
	
	$("#supportAddConfirm").on('click', function(){
		
		if(necessaryCheck(inputItems,undefined,undefined)){
			// 如果没有错误了,提交
			if(0 == $(".has-error").length){
	
				$("#supportAddForm").attr('method', 'POST').attr('action', baseUrl + '/sellPersonnel/support/supportAdd').submit();
			} else {
				
				new Confirm({
					type : 'alert',
					message : '填写有错误'
				});
			}
		}
	})
	
	/**
	 * 输入框统一去除错误提示
	 */
	$("input").on("focusout", function(){
		
		$_this = $(this)
		$_thisParent = $_this.parents(".form-group");
		
		if(-1 != inputItems.indexOf($_this.attr('name'))){
			
			// 如果不为空
			if("" != $_this.val().trim()){

				// 身份证验证
				if ("NBPT_SP_PERSON_IDNUM" == $_this.attr('name')){
					
					if(isIdCard($_this.val())){
						
						if($_this.val().charAt(16)%2 == 0){
							
							// 验证通过后,可以确定男女
							$("input[name='NBPT_SP_PERSON_MALE'][value='0']").prop("checked", "checked");
						} else {

							// 验证通过后,可以确定男女
							$("input[name='NBPT_SP_PERSON_MALE'][value='1']").prop("checked", "checked");
						}
						
						// 验证通过后,同时如果能取到籍贯信息,添加籍贯信息
						AjaxForGet(baseUrl + "/sellPersonnel/receiveTerminalPlace", {idNum : $_this.val().trim()},function(jsonData){
							
							if(0 == jsonData.length){
								return;
							}
								
							for(var i = 0; i < jsonData.length; i++){
								$("input[name='NBPT_SP_PERSON_PLACE']").val("");
								$("input[name='NBPT_SP_PERSON_PLACE']").val(jsonData[i].NBPT_COMMON_XZQXHF_NAME);
							}
						})
						$_this.parents('.form-group').removeClass("has-error");
					} else {
						
						$_this.parents('.form-group').addClass("has-error");
						return;
					}
				}
				
				// 登录ID验证
				if ("NBPT_SP_PERSON_LOGINID" == $_this.attr('name')){
					
					if($_this.val() == ''){

						$_this.parents('.form-group').addClass('has-error');
					} else {
						
						AjaxForGet(baseUrl + "/sellPersonnel/receiveLoginId", {loginId : $_this.val()},function(jsonData){
							
							if (jsonData.length > 0){

								$_this.parents('.form-group').addClass('has-error');
								$_this.parents('.form-group').find('.errorMessage').empty();
								$_this.parents('.form-group').find('.errorMessage').append($("<font color='red' ><p>该OA账号已存在,请联系信息部确认</p></font>"))
							} else{

								$_this.parents('.form-group').removeClass('has-error');
								$_this.parents('.form-group').find('.errorMessage').empty();
								$_this.parents('.form-group').find('.errorMessage').append($("<font color='green' ><p>可用</p></font>"))
							}
						})
					}
					
					
				}
				
				if (timeItems.indexOf($_this.attr('name')) >= 0 ){ // 如果是时间项目,要验证时间格式
					
					if(isTime($_this)){

						$_thisParent.removeClass("has-error");
					} else {

						$_thisParent.addClass("has-error");
						return;
					}
					
				}
				
				$_thisParent.removeClass("has-error");
				
			} 
			
			// 如果为空
			else {
				$_thisParent.addClass("has-error");
			}
			
		}
	})
	
	
})