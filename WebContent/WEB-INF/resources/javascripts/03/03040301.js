$(function(){

	if("" == $("#NBPT_SP_PERSON_PID").val()){

		// 隐藏申请离职按钮
		$("#dimissTerminal").css("display", "none");
		
	} 
	
	// 如果PID不为空,是修改,修改页面的初始化操作
	else {
		// 负责区域初始化选定事件
		AjaxForGet(baseUrl + "/sellPersonnel/receiveTerminalXzqx", {TerminalId : $("#NBPT_SP_PERSON_PID").val()},function(jsonData){
						
			$_copyParent = $(".reponseAreas").eq(0);

			for(var i = 0; i < jsonData.length; i++){
				
				if(0 == i){
					$_copyParent.find("select").val(jsonData[i].NBPT_COMMON_XZQXHF_ID);
				}
				
				else {
					
					
					 $_copy = $_copyParent.clone().insertAfter($_copyParent);
					 $_copy.find("select").val(jsonData[i].NBPT_COMMON_XZQXHF_ID);
					 $_copy.children().find("button").remove();
				}
			}
			
		})
		
		// 锁死部分项目
		var inputItems = new Array("NBPT_SP_PERSON_NAME","NBPT_SP_PERSON_IDNUM","NBPT_SP_PERSON_PLACE");
		var radioItems = new Array("NBPT_SP_PERSON_JOB","NBPT_SP_PERSON_MALE");
		
		$("input[name='NBPT_SP_PERSON_ENTRYDATA']").datetimepicker('remove');
		setReadOnly({
			inputItems : inputItems,
			radioItems : radioItems
		});
	}
	
	// 必填项目列表
	var inputItems = new Array("NBPT_SP_PERSON_NAME","NBPT_SP_PERSON_IDNUM","NBPT_SP_PERSON_PLACE",
							   "NBPT_SP_PERSON_MOB1","NBPT_SP_PERSON_MOB2",
							   "NBPT_SP_PERSON_ENTRYDATA")
//							   ,"NBPT_SP_PERSON_POLICYNO"
//							   ,
//							   "NBPT_SP_PERSON_POLICY_DATA1","NBPT_SP_PERSON_POLICY_DATA2",
//							   "NBPT_SP_PERSON_QQ","NBPT_SP_PERSON_CHAT",
//							   "NBPT_SP_PERSON_MAIL","NBPT_SP_PERSON_SCHOOL",
//							   "NBPT_SP_PERSON_PROFESS");
	
	// 需要验证的单选框
	var radioItems = new Array("NBPT_SP_PERSON_MALE", "NBPT_SP_PERSON_JOB");
	
	// 需要验证的下拉框
	var selectItems = new Array("NBPT_SP_PERSON_DEGREE");
	
	/**
	 * 点击提交事件
	 */
	$("#addPerson").on("click", function(){
				
		if(1 == $(".reponseAreas").length && "" == $(".reponseAreas").find("select").val()){
			
			$(".reponseAreas").addClass("has-error");
			new Confirm({
				
				'message' : '有必填项目未填写',
				'type' : 'alert'
			});
			return;
		}
		
		// 输入框统一为空检验
		if(necessaryCheck(inputItems,radioItems,selectItems)){
			
			// 如果没有错误了,提交
			if(0 == $(".has-error").length){

				var areanos = "";
				
				for(var i = 0; i < $(".reponseAreas").length; i++){
					
					var selectValue = $(".reponseAreas").eq(i).find("select").val();
					
					if(null != selectValue && "" != selectValue ){
						
						areanos += selectValue + "&" ;
					} 
				}

				$("#NBPT_SP_PERSON_PID").val() && $("#addPersonType").val("2") || $("#addPersonType").val("1")				
				$("input[name='NBPT_SP_PERSON_AREANO']").val(areanos);
				$("#addPersonForm").attr("action", baseUrl + "/sellPersonnel/addTerminal").attr("method", "POST").submit();
			}
			
		} else {
			return;
		}
	})

	/**
	 * 离职事件
	 */
	$("#dimissTerminal").on("click", function(){
		
		$("#0304Modal").modal('show')
	});
	
	/**
	 * 离职提交
	 */
	$("#dimissConfirm").on("click",function(){
		
		if(isTime($("#dimissTime"))){
			
			var beginDate=$("#dimissTime").val(); 
			var now = getNowFormatDate();
			var d1 = new Date(beginDate.replace(/\-/g, "\/")); 
			var d2 = new Date(now.replace(/\-/g, "\/")); 

			if(beginDate!="" && d1 < d2) 
			{
				new Confirm({
					message : '离职日期必须大于等于当前日期',
					type : 'alter'
				});
				
			} else {
				
				$("#dimissTerminalPid").val($("#NBPT_SP_PERSON_PID").val());
				$("#dimissForm").attr("method", "POST").attr("action", baseUrl + "/sellPersonnel/dimissTerminal").submit();
			}
		} else {

			new Confirm({
				message : '请填写离职日期',
				type : 'alter'
			});
		}
		
	})
	
	/**
	 * 职务点击事件,职务点击要跟身份证验证同时进行
	 */
	$("input[name='NBPT_SP_PERSON_JOB']").on("click", function(){
		
		if("" == $("#NBPT_SP_PERSON_PID").val()){

			$_this = $("input[name='NBPT_SP_PERSON_IDNUM']");
			$_thisParent = $_this.parents(".form-group");
			idNumCheck($_this, $_thisParent);
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
					
					if(idNumCheck($_this, $_thisParent)){
						
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
	
	$(".extraBtn").on("click",function (){
		
		// 多地区管理的额外点击事件
		var $_this = $(this);
		$_copy = $_this.parents(".form-group").clone(true,true);
		$_copy.children().find("button").remove();
		$_copy.find("label").empty();
		$_copy.insertAfter($_this.parents(".form-group"));
	})
	
	// 下拉框去错处理
	$(".form-group").on("change","select",function (){
		
		if($(this).val != ''){
			
			$(this).parents(".form-group").removeClass("has-error");
		}
	})
	
	/**
	 * 身份证验证
	 */
	function idNumCheck($_this, $_thisParent){
		
		// 判断身份证是否合法
		if(isIdCard($_this.val())){
			
			$_thisParent.removeClass("has-error");
			$(".errorMessage").empty();
			return true;
		} else {

			$_thisParent.addClass("has-error");
			$(".errorMessage").empty();	
			$(".errorMessage").append($("<font color='red' ><p>请输入正确的身份证信息</p></font>"))
			return false;
		}
		
		if("" == $("#NBPT_SP_PERSON_PID").val()){
			// 判断年龄是否符合要求
			if(null == $("input[name='NBPT_SP_PERSON_JOB']:checked").val()){
				
				// 如果没有选择职位
				$_thisParent.addClass("has-error");
				$(".errorMessage").empty();	
				$(".errorMessage").append($("<font color='red' ><p>请选择职务后再确认身份证</p></font>"))
				return false;
				
			} else if("25" == $("input[name='NBPT_SP_PERSON_JOB']:checked").val()){
				
				// 如果是推广经理,要求55以下
				if($_this.val().substr(6,4) < 1963){
					alert($_this.val().substr(6,4));
					$_thisParent.addClass("has-error");
					$(".errorMessage").empty();	
					$(".errorMessage").append($("<font color='red' ><p>推广经理要求55岁以下</p></font>"))
					return false;
				} else {

					$_thisParent.removeClass("has-error");
					$(".errorMessage").empty();
					return true;
				}
			} else {
				
				// 如果不是推广经理,要求35以下
				if($_this.val().substr(6,4) < 1983){

					$_thisParent.addClass("has-error");
					$(".errorMessage").empty();	
					$(".errorMessage").append($("<font color='red' ><p>非推广经理要求35岁以下</p></font>"))
					return false;
				} else {

					$_thisParent.removeClass("has-error");
					$(".errorMessage").empty();
					return true;
				}
			}
		}
		
		
	}
	

})