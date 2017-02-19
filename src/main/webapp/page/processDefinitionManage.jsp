<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"  isELIgnored="false"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程定义管理</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">

	var url;
	
	function searchProcessDefinition(){
		 $("#dg").datagrid('load',{
			"s_name":$("#s_name").val()
		 });
	}
	
	function showView(deploymentId,diagramResourceName){
		 $("#dlg").dialog("open").dialog("setTitle","添加流程部署");
		 url="${pageContext.request.contextPath}/processDefinition/processDefinition.do";
	}
	

	function saveProcessDefinition(){
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
	
	function closeProcessDefinitionDialog(){
		 $("#dlg").dialog("close");
	}
	

	function formatAction(val,row){
		return "<a target='_blank' href='${pageContext.request.contextPath}/processDefinition/showView.do?deploymentId="+row.deploymentId+"&diagramResourceName="+row.diagramResourceName+"' >查看流程图</a>";
	}
	
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="流程定义管理" class="easyui-datagrid" 
   fitColumns="true" pagination="true" rownumbers="true" fit="true"
   url="${pageContext.request.contextPath}/processDefinition/list.do" toolbar="#tb">
   <thead>
   	<tr>
   		<th field="id" width="80" align="center">编号</th>
   		<th field="name" width="60" align="center">流程名称</th>
   		<th field="key" width="50" align="center">流程定义的Key</th>
   		<th field="version" width="20" align="center">版本</th>
   		<th field="resourceName" width="70" align="center">流程定义的规则文件名称</th>
   		<th field="diagramResourceName" width="70" align="center">流程定义的规则图片名称</th>
   		<th field="deploymentId" width="30" align="center">流程部署Id</th>
   		<th field="aa" width="30" align="center" formatter="formatAction">操作</th>
   	</tr>
   </thead>
 </table>
 <div id="tb">
 	<div>
 		&nbsp;流程定义名称：&nbsp;<input type="text" id="s_name" size="20" onkeydown="if(event.keyCode==13) searchProcessDefinition()"/>
 		<a href="javascript:searchProcessDefinition()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
 	</div>
 </div>
 

</body>
</html>