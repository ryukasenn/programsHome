$(function(){
	
	/**
	 * 查看部门划分点击事件
	 */
	$("#receiveRegions").on("click", function(){
		$("#controlForm").attr("action", baseUrl + "/sellPersonnel/regionController/regions").attr("method", "POST").submit();
	})
	
	$('#searchConfirm').on('click', function(){
		$_modalNameConfirmRadio = $("input[name='modalNameConfirm']:checked");
		if(undefined == $_modalNameConfirmRadio.val()){
			
		} else {
			
			var pid = $_modalNameConfirmRadio.val();
			var name = $_modalNameConfirmRadio.parents('td').next('td').children('p').html();
			$('#ResperName').val(name);
			$('#ResperPid').val(pid);
			$('#0302Modal').modal('hide')
		}
	})
	
	$('#addSection').on('click', function(){
		var treeObj = $.fn.zTree.getZTreeObj("treeMenu");
		var node = treeObj.getSelectedNodes()[0];
		if(!node){
			$.nbptMsg.alert("请选择左侧添加目标")
		}
		var usualCallBack = function(){
			$("#"+node.tId+"_a").click();
			var treeObj = initTree();
			var newNodes = treeObj.getNodesByParam("currentId", node.currentId, null);
			treeObj.selectNode(newNodes[0]);
			return true;
		}
		switch (node.type){
		case 'type' :
			var newForm = $.nbptAjax.post(baseUrl + "/sp/rm/addSection",{type:'type', typeValue:node.typeValue}, true)
			newForm.sureCallback = function(){
				var url = baseUrl + "/sp/rm/postAddSection";
				var param = $.nbptCommon.formItemsToParams($("#" + newForm.formId));
				var callBack = usualCallBack;
				param.newSectionName && param.newSectionNeed || $.nbptMsg.alert("请填写必填项")
				param.newSectionName && param.newSectionNeed && $.nbptAjax.post(url,param,callBack);
			}
			$.nbptModal.createFormModal(newForm).modal({backdrop:'static'})
			break;
		case 'region' :
			var newForm = $.nbptAjax.post(baseUrl + "/sp/rm/addSection",{type:'region', typeValue:node.typeValue, parentId:node.currentId}, true)
			newForm.sureCallback = function(){
				var url = baseUrl + "/sp/rm/postAddSection";
				var param = $.nbptCommon.formItemsToParams($("#" + newForm.formId));
				var callBack = usualCallBack;
				param.newXzqxid || $.nbptMsg.alert("请选择要添加的省份")
				param.newXzqxid && $.nbptAjax.post(url,param,callBack);
			}
			$.nbptModal.createFormModal(newForm).modal({backdrop:'static'})
			break;
		case 'province' :
			var newForm = $.nbptAjax.post(baseUrl + "/sp/rm/addSection",{type:'province', typeValue:node.typeValue, parentId:node.currentId}, true)
			newForm.sureCallback = function(){
				var url = baseUrl + "/sp/rm/postAddSection";
				var param = $.nbptCommon.formItemsToParams($("#" + newForm.formId));
				var callBack = usualCallBack;
				param.newSectionName && param.newSectionNeed || $.nbptMsg.alert("请填写必填项")
				param.newSectionName && param.newSectionNeed && $.nbptAjax.post(url,param,callBack);
			}
			$.nbptModal.createFormModal(newForm).modal({backdrop:'static'})
			break;
		case 'area' :
			var newForm = $.nbptAjax.post(baseUrl + "/sp/rm/addSection",{type:'area', typeValue:node.typeValue, parentId:node.currentId}, true)
			newForm.sureCallback = function(){
				var url = baseUrl + "/sp/rm/postAddSection";
				var param = $.nbptCommon.formItemsToParams($("#" + newForm.formId));
				var callBack = usualCallBack;
				param.newXzqxid_city || $.nbptMsg.alert("至少要选择市级单位")
				param.newXzqxid_city && $.nbptAjax.post(url,param,callBack);
			}
			$.nbptModal.createFormModal(newForm).modal({backdrop:'static'})
			$("body").on("change", "#newXzqxid_city",function(){
				var that = $("#newXzqxid_city")
				$.nbptAjax.post(baseUrl + "/sellPersonnel/receiveSelect",{parentId : that.val()}, function(options){
					$("#newXzqxid_conty").empty();
					for(var i = 0 ; i < options.length ; i++){
						$("#newXzqxid_conty").append($("<option value=" + options[i].NBPT_COMMON_XZQXHF_ID + ">" + options[i].NBPT_COMMON_XZQXHF_NAME + "</option>"))
					}
				})
			})
			break;
			default:break;
		}
	});
	$('#changeSection').on('click', function(){
		var rowData = $.nbptTable.getSelected();
		if(!rowData){  $.nbptMsg.alert("请选择修改的对象"); return false;}
		if("province" === rowData.SECTION_TYPE || "xzqx" === rowData.SECTION_TYPE) {$.nbptMsg.alert("国家行政区县划分,不允许修改"); return false}
		var treeObj = $.fn.zTree.getZTreeObj("treeMenu");
		var node = treeObj.getSelectedNodes()[0];
		var usualCallBack = function(){
			$("#"+node.tId+"_a").click();
			var treeObj = initTree();
			var newNodes = treeObj.getNodesByParam("currentId", node.currentId, null);
			treeObj.selectNode(newNodes[0]);
			return true;
		}
		var newForm = $.nbptAjax.post(baseUrl + "/sp/rm/changeSection",rowData, true);
		newForm.sureCallback = function(){
			$("#waitModal").modal({backdrop:'static'})
			var url = baseUrl + "/sp/rm/postChangeSection";
			var param = $.nbptCommon.formItemsToParams($("#" + newForm.formId));
			if(!$.nbptAjax.post(url,param,true)){$.nbptMsg.alert();return false;}
			var callBack = usualCallBack;
			$.nbptAjax.post(url,param,callBack);
		}
		$.nbptModal.createFormModal(newForm).modal({backdrop:'static'})
	})
	initTree();
})


function createTable(node){
	var node = node || this;
	var options = {}; 
	options.columns = [
		{id:'SECTION_NAME',title:'部门名称'}
		,{id:"SECTION_RESPONSIBLER_NAME", title:"部门负责人"}
		,{id:"SECTION_PARENT_NAME", title:"上级部门"}];
	options.url = baseUrl + "/sp/rm/allSection";options.urlParam = {parentId:node.currentId, type:node.type, typeValue: node.typeValue};
	$.nbptTable.createTable(options);
}

/**
 * 添加负责人模态框
 * @param url
 * @param data
 * @returns
 */
function createModal(url, data, title){
	AjaxForGet(url, data, function(jsonData){
		if(0 == jsonData.length){
			var name = (data.personType == '26')?'大区总':'地总'
			new Confirm({
				type:'alert',
				message:'当前没有可选' + name
			});
		}else {
			$('.bodyTable').empty();
			$('.modal-title').html(title);
			var $_table = $("<table class='table table-striped table-bordered tableBody'></table>").appendTo($('.bodyTable'));
			for(var i = 0; i < jsonData.length; i++){
				var $_tr = $("<tr style='text-align:center'></tr>").appendTo($('.tableBody'));
				$("<td><label><input type='radio' name='modalNameConfirm' value='" + jsonData[i].NBPT_SP_PERSON_PID + "'/></label></td>").appendTo($_tr);
				$("<td><p>" + jsonData[i].NBPT_SP_PERSON_NAME + "</p></td>").appendTo($_tr);
			}
			$('#0302Modal').modal('show')
		}		
	})
}

function initTree(){
	var ztreeObjec;
	var zNodes = $.nbptAjax.post(baseUrl + "/sellPersonnel/createTreeRegion",{}, true);
	var setting = {
			callback: {
				onClick : function(event, treeId, treeNode){createTable(treeNode)}
			}};
	ztreeObjec = $.fn.zTree.init($("#treeMenu"), setting, zNodes);
	return ztreeObjec;
}

































	
