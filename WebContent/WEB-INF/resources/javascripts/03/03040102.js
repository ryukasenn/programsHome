$(function(){
	var ztreeObjec = initTree();
})
function initTree(){
	var ztreeObjec;
	var zNodes = $.nbptAjax.post(baseUrl + "/sellPersonnel/createRegionTree",{pid : 'root', rank : '3'});
	var setting = {
			callback: {
				onClick : function(event, treeId, treeNode){
					createTable(treeNode);
					}
			}};
	ztreeObjec = $.fn.zTree.init($("#treeMenu"), setting, zNodes);
	return ztreeObjec;
}

function createTable(treeNode){
	console.log(treeNode);
	switch(treeNode.type){
	case 'root' :
		var options = {url : baseUrl + "/sellPersonnel/support/receivePersonStatisticsTable", urlParam : {type : 'root', id : treeNode.currentId}};
		options.columns = [
			{id:'REGIONNAME',title:'分类'}
			,{id:"TOTAL", title:"合计"}
		];
		$.nbptTable.createTable(options);
		break;
	case 'type' :
		//$.nbptAjax.post(baseUrl + "/sellPersonnel/support/receivePerson",{type : 'type', id : treeNode.currentId});
		var options = {url : baseUrl + "/sellPersonnel/support/receivePersonStatisticsTable", urlParam : {type : 'type', id : treeNode.currentId}};
		options.columns = [
			{id:'regionName',title:'大区'}
			,{id:'regionResper',title:'大区总'}
			,{id:"areaResper", title:"地总"}
			,{id:"xzquResper", title:"正式区县总"}
			,{id:"xzquResper_preparatory", title:"预备区县总"}
			,{id:"promote", title:"推广经理"}
			,{id:"total", title:"已配"}
			,{id:"need", title:"应配"}
			,{id:"balance", title:"差额"}
			,{id:"dismission", title:"离职合计"}
			,{id:"dismissionRate", title:"离职率"}
		];
		$.nbptTable.createTable(options);
		break;
	case "region" :
		var options = {url : baseUrl + "/sellPersonnel/support/receivePersonStatisticsTable", urlParam : {type : 'region', id : treeNode.currentId}};
		options.columns = [
			{id:'provinceName',title:'省份'}
			,{id:'areaName',title:'大区'}
			,{id:"areaHeader", title:"大区总"}
			,{id:"xzquResper", title:"正式区县总"}
			,{id:"xzquResper_preparatory", title:"预备区县总"}
			,{id:"promote", title:"推广经理"}
			,{id:"total", title:"已配"}
			,{id:"need", title:"应配"}
			,{id:"balance", title:"差额"}
			,{id:"dismission", title:"离职合计"}
			,{id:"dismissionRate", title:"离职率"}
		];
		$.nbptTable.createTable(options);
		break;
	case "province" :
		var options = {url : baseUrl + "/sellPersonnel/support/receivePersonStatisticsTable", urlParam : {type : 'province', id : treeNode.currentId}};
		options.columns = [
			{id:'provinceName',title:'省份'}
			,{id:'areaName',title:'地区'}
			,{id:"areaHeader", title:"地区总"}
			,{id:"xzquResper", title:"正式区县总"}
			,{id:"xzquResper_preparatory", title:"预备区县总"}
			,{id:"promote", title:"推广经理"}
			,{id:"total", title:"已配"}
			,{id:"need", title:"应配"}
			,{id:"balance", title:"差额"}
			,{id:"dismission", title:"离职合计"}
			,{id:"dismissionRate", title:"离职率"}
		];
		$.nbptTable.createTable(options);
		break;
	default :
		break;
	}
}





















