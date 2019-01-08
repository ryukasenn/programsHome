$(function(){
	
	$('#addSection').on('click', function(){
		var treeObj = $.fn.zTree.getZTreeObj("treeMenu");
		var nodes = treeObj.getSelectedNodes();
		nodes.length != 0 && addRegion(nodes[0]) || $.nbptMsg.alert("请选择上级分类或省份");
	});
	$("body").on("change", ":input[name='newXzqxid_city']",function(){
		var that = $(this)
		$.nbptAjax.silencePost(baseUrl + "/sellPersonnel/receiveSelect",{parentId : that.val()}, function(options){
			var conty = that.parents("form").find(":input[name='newXzqxid_conty']").empty();
			for(var i = 0 ; i < options.length ; i++){
				conty.append($("<option value=" + options[i].NBPT_COMMON_XZQXHF_ID + ">" + options[i].NBPT_COMMON_XZQXHF_NAME + "</option>"))
			}
		})
	})
	initTree();
})


function createTable(node){
	var node = node || this;
	var options = {};
	options.columns = [
		{id:'SECTION_NAME',title:'部门名称'}
		,{id:"SECTION_FLAG", title:"部门状态"}
		,{id:"SECTION_CRETETIME", title:"创建时间"}];
	options.url = baseUrl + "/sp/rm/allSection";options.urlParam = {parentId:node.currentId, type:node.type, typeValue: node.typeValue};
	options.onChangeClick = function(row){		
		var newForm = $.nbptAjax.post(baseUrl + "/sp/rm/changeSection",row);
		newForm.sureCallback = function(){
			$("#waitModal").modal({backdrop:'static'})
			var url = baseUrl + "/sp/rm/postChangeSection";
			var param = $.nbptCommon.formItemsToParams($("#" + newForm.formId));
			param && $.nbptAjax.post(url,param,function(){$("#"+node.tId+"_a").click();});
			if(param) return true
			else return false;
		}
		$.nbptModal.createFormModal(newForm);
	}
	options.onAddClick = function(row){
		addXzqx(row,node)
	}
	$.nbptTable.createRegionTable(options);
}

function initTree(){
	var ztreeObjec;
	var zNodes = $.nbptAjax.post(baseUrl + "/sellPersonnel/createTreeRegion",{}, true);
	var setting = {
			callback: {
				onClick : function(event, treeId, treeNode){
					treeNode.clickAble && createTable(treeNode);
					}
			}};
	ztreeObjec = $.fn.zTree.init($("#treeMenu"), setting, zNodes);
	return ztreeObjec;
}

function addRegion(node){
	switch (node.type){
	case 'type' :
	case 'region' :
		var newForm = $.nbptAjax.post(baseUrl + "/sp/rm/addSection",{type:'type', typeValue:node.typeValue}, true)
		newForm.sureCallback = function(){
			var url = baseUrl + "/sp/rm/postAddSection";
			var param = $.nbptCommon.formItemsToParams($("#" + newForm.formId));
			param.newSectionName && param.newSectionNeed || $.nbptMsg.alert("请填写必填项")
			param.newSectionName && param.newSectionNeed && $.nbptAjax.post(url,param,function(){$("#"+node.tId+"_a").click();});
			if(param.newSectionName && param.newSectionNeed) return true;
		}
		$.nbptModal.createFormModal(newForm)
		break;
	case 'province':
		var newForm = $.nbptAjax.post(baseUrl + "/sp/rm/addSection",{type:'province', typeValue:node.typeValue, parentId:node.currentId})
		newForm.sureCallback = function(){
			var url = baseUrl + "/sp/rm/postAddSection";
			var param = $.nbptCommon.formItemsToParams($("#" + newForm.formId));
			param.newSectionName && param.newSectionNeed || $.nbptMsg.alert("请填写必填项")
			param.newSectionName && param.newSectionNeed && $.nbptAjax.post(url,param,function(){$("#"+node.tId+"_a").click();});
			if(param.newSectionName && param.newSectionNeed) return true;
		}
		$.nbptModal.createFormModal(newForm)
		break;
		default: 
			$.nbptMsg.alert("请选择上级分类或省份")
			break;
	}
	return true;
}
function addXzqx(row, node){

	switch (row.SECTION_LEVEL){
	case 'region' :
		var newForm = $.nbptAjax.post(baseUrl + "/sp/rm/addSection",{type:'region', typeValue:row.SECTION_TYPE, parentId:row.SECTION_UID}, true)
		newForm.sureCallback = function(){
			var url = baseUrl + "/sp/rm/postAddSection";
			var param = $.nbptCommon.formItemsToParams($("#" + newForm.formId));
			param.newXzqxid || $.nbptMsg.alert("请选择要添加的省份")
			param.newXzqxid && $.nbptAjax.post(url,param,function(){$("#"+node.tId+"_a").click();});
			if(param.newXzqxid) return true;
		}
		$.nbptModal.createFormModal(newForm)
		break;
	case 'area':
		var newForm = $.nbptAjax.post(baseUrl + "/sp/rm/addSection",{type:'area', typeValue:row.SECTION_TYPE, parentId:row.SECTION_UID}, true)
		newForm.sureCallback = function(){
			var url = baseUrl + "/sp/rm/postAddSection";
			var param = $.nbptCommon.formItemsToParams($("#" + newForm.formId));
			param.newXzqxid_city || $.nbptMsg.alert("至少要选择市级单位")
			param.newXzqxid_city && $.nbptAjax.post(url,param,function(){$("#"+node.tId+"_a").click();});
			if(param.newXzqxid_city) return true;
		}
		$.nbptModal.createFormModal(newForm)
		
		break;
		default: break;
	}
	return true;
}
































	
