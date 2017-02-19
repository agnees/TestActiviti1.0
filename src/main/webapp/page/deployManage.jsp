<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"  isELIgnored="false"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程部署管理</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">

	var url;
	
	function searchDeploy(){
		 $("#dg").datagrid('load',{
			"s_name":$("#s_name").val()
		 });
	}
	
	function openDeployAddDialog(){
		 $("#dlg").dialog("open").dialog("setTitle","添加流程部署");
		 url="${pageContext.request.contextPath}/deploy/deploy.do";
	}
	

	function saveDeploy(){
		 $("#fm").form("submit",{
			url:url,
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","部署成功！");
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}else{
					$.messager.alert("系统提示","部署失败，请联系管理员！");
					return;
				}
			}
		 });
	}
	
	function closeDeployDialog(){
		 $("#dlg").dialog("close");
	}
	
	function deleteDeploy(){
		 var selectedRows=$("#dg").datagrid("getSelections");
		 if(selectedRows.length==0){
			 $.messager.alert("系统提示","请选择要删除的数据！");
			 return;
		 }
		 var strIds=[];
		 for(var i=0;i<selectedRows.length;i++){
			 strIds.push(selectedRows[i].id);
		 }
		 var ids=strIds.join(",");
		 $.messager.confirm("系统提示","您确定要删除这<font color=red>"+selectedRows.length+"</font>条数据吗？",function(r){
			if(r){
				$.post("${pageContext.request.contextPath}/deploy/delete.do",{ids:ids},function(result){
					if(result.success){
						 $.messager.alert("系统提示","数据已成功删除！");
						 $("#dg").datagrid("reload");
					}else{
						$.messager.alert("系统提示","数据删除失败，请联系系统管理员！");
					}
				},"json");
			} 
		 });
	}
	
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="流程部署管理" class="easyui-datagrid" 
   fitColumns="true" pagination="true" rownumbers="true" fit="true"
   url="${pageContext.request.contextPath}/deploy/list.do" toolbar="#tb">
   <thead>
   	<tr>
   		<th field="cb" checkbox="true" align="center"></th>
   		<th field="id" width="50" align="center">编号</th>
   		<th field="name" width="50" align="center">流程名称</th>
   		<th field="deploymentTime" width="50" align="center">部署时间</th>
   	</tr>
   </thead>
 </table>
 <div id="tb">
 	<div>
 		<a href="javascript:openDeployAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
 		<a href="javascript:deleteDeploy()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
 	</div>
 	<div>
 		&nbsp;流程名称：&nbsp;<input type="text" id="s_name" size="20" onkeydown="if(event.keyCode==13) searchDeploy()"/>
 		<a href="javascript:searchDeploy()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
 	</div>
 </div>
 
 <div id="dlg" class="easyui-dialog" style="width:320px;height:150px;padding: 10px 20px"
   closed="true" buttons="#dlg-buttons">
   
   <form id="fm" method="post" enctype="multipart/form-data">
   	<input type="file" name="deployFile" class="easyui-validatebox" required="true"/>
   </form>
 </div>
 
 <div id="dlg-buttons">
 	<a href="javascript:saveDeploy()" class="easyui-linkbutton" iconCls="icon-ok">上传</a>
 	<a href="javascript:closeDeployDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
 </div>
</body>
</html>