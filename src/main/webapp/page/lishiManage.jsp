<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"  isELIgnored="false"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>历史任务管理</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">

	
	function searchTask(){
		 $("#dg").datagrid('load',{
			"s_taskName":$("#s_taskName").val()
		 });
	}
	
	function openListCommentDialog(taskId){
		$('#dg2').datagrid('load',{
			taskId:taskId
		});
		$("#dlg").dialog("open").dialog("setTitle","查看历史批注");
	}
	
	function openListActionDialog(taskId){
		$('#dg3').datagrid('load',{
			taskId:taskId
		});
		$("#dlg2").dialog("open").dialog("setTitle","流程执行过程");
	}
	
	
	function formatAction(val,row){
		return "<a  href='javascript:openListActionDialog("+row.id+")' >流程执行过程</a>&nbsp;&nbsp;"+"<a  href='javascript:openListCommentDialog("+row.id+")' >历史批注</a>&nbsp;&nbsp;"+"<a target='_blank' href='${pageContext.request.contextPath}/processDefinition/showViewByTaskId.do?taskId="+row.id+"' >查看流程图</a>";
	}
	

	
	
</script>
</head>
<body style="margin: 1px">
<table id="dg" title="历史任务管理" class="easyui-datagrid"
   fitColumns="true" pagination="true" rownumbers="true"
   url="${pageContext.request.contextPath}/task/finishedList.do?groupId=${currentMemberShip.group.id}" fit="true" toolbar="#tb">
   <thead>
   	<tr>
   		<th field="cb" checkbox="true" align="center"></th>
   		<th field="id" width="80" align="center">任务ID</th>
   		<th field="name" width="150" align="center">任务名称</th>
   		<th field="createTime" width="100" align="center">创建时间</th>
   		<th field="endTime" width="100" align="center">结束时间</th>
   		<th field="action" width="150"  align="center" formatter="formatAction">操作</th>
   	</tr>
   </thead>
 </table>
 <div id="tb">
 	<div>
 		&nbsp;任务名称：&nbsp;<input type="text" id="s_taskName" size="20" onkeydown="if(event.keyCode==13) searchTask()"/>
 		<a href="javascript:searchTask()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
 	</div>
 </div>
 
 <div id="dlg" class="easyui-dialog" style="width:750px;height:250px;padding: 10px 20px"
   closed="true" >
   
    <table id="dg2" title="批注列表" class="easyui-datagrid"
   fitColumns="true" url="${pageContext.request.contextPath}/task/listHistoryComment.do" style="width: 700px;height: 200px;">
   <thead>
   	<tr>
   		<th field="time" width="120" align="center">批注时间</th>
   		<th field="userId" width="100" align="center">批注人</th>
   		<th field="message" width="200" align="center">批注信息</th>
   	</tr>
   </thead>
 </table>
   
 </div>
 
 
 <div id="dlg2" class="easyui-dialog" style="width:750px;height:350px;padding: 10px 20px"
   closed="true" >
   
    <table id="dg3" title="流程执行过程列表" class="easyui-datagrid"
   fitColumns="true" url="${pageContext.request.contextPath}/processInstance/listAction.do" style="width: 700px;height: 250px;">
   <thead>
   	<tr>
   		<th field="activityId" width="100" align="center">任务节点ID</th>
   		<th field="activityName" width="150" align="center">任务节点名称</th>
   		<th field="startTime" width="100" align="center">开始时间</th>
   		<th field="endTime" width="100" align="center">结束时间</th>
   	</tr>
   </thead>
 </table>
   
 </div>
 
</body>
</html>