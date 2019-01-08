$(function(){
	
	
	$("#transferConfirm").on('click', function(){
		
		var $_personPid = $("#personNameHidden").val() && $("#personNameHidden").val().trim();
		var $_regionUid = $("#regionNameHidden").val() && $("#regionNameHidden").val().trim();
		var $_personLoginId = $("#personLoginId").val() && $("#personLoginId").val().trim();
		var transferType = $("input[name='transferType']:checked").val() && $("input[name='transferType']:checked").val().trim();

		
		if(undefined == transferType) {
			new Confirm({
				message : '选择功能呀',
				type : 'alter'
			})
			return;
		}
		
		// 如果是区县调地区
		else if('0' == transferType){
			if ($_personPid && $_regionUid && $("#regionPanel")[0] && $("#personPanel")[0]){}
			else {
				new Confirm({
					message : '看看啥不是选的',
					type : 'alter'
				});return;
			}
		} 
		
		// 如果是区县总升地总
		else if('1' == transferType){
			if ($_personPid && $_personLoginId && $("#personPanel")[0]){}
			else {
				new Confirm({
					message : '选人啊',
					type : 'alter'
				})
				return;
			}
		}
		
		// 如果是地总升大区总
		else if('2' == transferType){
			if ($_personPid && $("#personPanel")[0]){}
			else {
				new Confirm({
					message : '选人啊',
					type : 'alter'
				})
				return;
			}
		}
		
		$("#transferForm").attr("method", "POST").attr("action", baseUrl + "/sellPersonnel/support/transfer").submit();
		
	})
	
	// 登录ID验证
	$("#personLoginId").on('focusout', function(){
		
		var $_this = $(this);
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
	})
	
	$("#transferForm").on('click', 'input[type="radio"]', function(){
		
		var $_this = $(this)
		
		if($_this.attr("name") == 'regionSelected'){
			
			$("#regionName").val($_this.parent().next().html());
			$("#regionNameHidden").val($_this.val());
			
			return ;
		} 
		
		else if($_this.attr("name") == 'personSelected'){
			
			$("#personName").val($_this.parent().next().html());
			$("#personNameHidden").val($_this.val());
			$("#personIdHidden").val($_this.parent().next().next().html());
			return ;
		}
		
		else if($_this.attr("name") == 'transferType'){

			$("#transferForm")[0].reset();
			$("form .panel").remove();
			$_this.prop("checked", "checked");
			
			if('0' == $_this.val()){
				
				$(".targetRegion").css("display", "block");
				$(".personLoginId").css("display", "none");
			}
			
			else if ('1' == $_this.val()){

				$(".targetRegion").css("display", "none");
				$(".personLoginId").css("display", "block");
			}
			
			else if('2' == $_this.val()){

				$(".targetRegion").css("display", "none");
				$(".personLoginId").css("display", "none");
			}
			return ;
		}
	})
	
	// 地区查询按钮
	$("#searchRegion").on('click', function(){
		
		if($("#regionName").val().trim() == ''){
			
			new Confirm({
				message : '请输入地区名称,支持模糊查询',
				type : 'alert'
			});
			return ;
		}
		
		AjaxForGet(baseUrl + "/sellPersonnel/transferSearchRegion", 
				{regionName : $("#regionName").val().trim()}
		,function(jsonData){
			
			$("#waitModal").modal({
				backdrop:'static'
			})
			
			$("#regionName").val("");
			if(0 == jsonData.length){
				$("#transferForm").find("#regionPanel").remove();
				new Confirm({
					message : '没有找到相关地区信息,请再次确认',
					type : 'alert'
				});
				return;
			} else {
				
				$("#transferForm").find("#regionPanel").remove();
				// 添加外框
				var $_panel = createPanel({
					id : 'regionPanel',
					td : ['#','地区','地总','所属省份','所属大区']
				});
				// 添加人员信息
				for(var i = 0; i < jsonData.length; i++){
					
					var region = jsonData[i];
					var $_person_tr = $("<tr style='text-align:center;'>" +
											"<th style='vertical-align:middle;text-align:center;',scope='row'>" +
												"<input type='radio' name='regionSelected' value='" + region.NBPT_SP_REGION_UID + "'/>" +
											"</th>" +
											"<td style='vertical-align:middle'>" + region.NBPT_SP_REGION_NAME + "</td>" +
											"<td style='vertical-align:middle'>" + region.NBPT_SP_REGION_RESPONSIBLER_NAME + "</td>" +
											"<td style='vertical-align:middle'>" + region.NBPT_SP_REGION_PROVINCE_NAME + "</td>" +
											"<td style='vertical-align:middle'>" + region.NBPT_SP_REGION_PARENT_NAME + "</td>" +
										"</tr>");
					$_panel.find(".table").append($_person_tr);
				}
				
				$("#personPanel")[0] && $("#personPanel").before($_panel) || $("#transferForm").append($_panel)
			}
			
			$("#waitModal").modal('hide');
		})
	})
	
	// 人员查询按钮
	$("#searchPerson").on('click', function(){
		
		if($("#personName").val().trim() == ''){
			
			new Confirm({
				message : '请输入人员名称,支持模糊查询',
				type : 'alert'
			});
			return ;
		}
		
		// 验证通过后,同时如果能取到籍贯信息,添加籍贯信息
		AjaxForGet(baseUrl + "/sellPersonnel/transferSearchPerson", 
							{personName : $("#personName").val().trim(), 
							 transferType : $("input[name='transferType']:checked").val().trim()}
		,function(jsonData){
			
			if(0 == jsonData.length){
				$("#transferForm").find("#personPanel").remove();
				
				new Confirm({
					message : '没有找到相关人员信息',
					type : 'alert'
				});
				return;
			} else {

				$("#personName").val("");
				$("#transferForm").find("#personPanel").remove();
				// 添加外框
				var $_panel = createPanel({
					id : 'personPanel',
					td : ['#','姓名','职务','所属大区','所属地区','身份证号码','籍贯']
				});
				
				// 添加人员信息
				for(var i = 0; i < jsonData.length; i++){
					
					var person = jsonData[i];
					var $_person_tr = $("<tr style='text-align:center;'>" +
											"<th style='vertical-align:middle;text-align:center;',scope='row'>" +
												"<input type='radio' name='personSelected' value='" + person.NBPT_SP_PERSON_PID + "'/>" +
											"</th>" +
											"<td style='vertical-align:middle'>" + person.NBPT_SP_PERSON_NAME + "</td>" +
											"<td style='vertical-align:middle;display:none'>" + person.NBPT_SP_PERSON_ID + "</td>" +
											"<td style='vertical-align:middle'>" + person.NBPT_SP_PERSON_REALJOB + "</td>" +
											"<td style='vertical-align:middle'>" + person.NBPT_SP_PERSON_REGION_NAME + "</td>" +
											"<td style='vertical-align:middle'>" + person.NBPT_SP_PERSON_AREA_NAME + "</td>" +
											"<td style='vertical-align:middle'>" + person.NBPT_SP_PERSON_IDNUM + "</td>" +
											"<td style='vertical-align:middle'>" + person.NBPT_SP_PERSON_PLACE + "</td>" +
										"</tr>");
					$_panel.find(".table").append($_person_tr);
				}

				$("#regionPanel")[0] && $("#regionPanel").before($_panel) || $("#transferForm").append($_panel)
			}
				
			
		})
	});
	
	function createPanel(option){
		
		var $_panel = $("<div class='panel panel-default' id='" + option.id + "'></div>");
		var $_panel_heading = $("<div class='panel-heading'></div>");
		var $_table = $("<table class='table table-hover'>" +
							"<tr></tr>" +
						"</table>");
		for(var i = 0; i < option.td.length; i++){
			
			var $_th = $("<th style='text-align:center'>" + option.td[i] +"</th>");
			$_th.appendTo($_table.find("tr"));
		}
		$_panel.append($_panel_heading).append($_table);
		return $_panel;
	}
	
})