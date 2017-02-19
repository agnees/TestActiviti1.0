<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"  isELIgnored="false"  %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">

	
	function searchUser(){
		 $("#dg").datagrid('load',{
			"id":$("#s_id").val()
		 });
	}
	
	function openUserAddDialog(){
		 $("#dlg").dialog("open").dialog("setTitle","添加用户信息");
		 $("#flag").val(1);
		 $("#id").attr("readonly",false);
	}
	
	function openUserModifyDialog(){
		 var selectedRows=$("#dg").datagrid("getSelections");
		 if(selectedRows.length!=1){
			 $.messager.alert("系统提示","请选择一条要编辑的数据！");
			 return;
		 }
		 var row=selectedRows[0];
		 $("#dlg").dialog("open").dialog("setTitle","编辑用户信息");
		 $("#fm").form("load",row);
		 $("#flag").val(2);
		 $("#id").attr("readonly",true);
	}
	
	function saveUser(){
		$("#fm").form("submit",{
			url:"${pageContext.request.contextPath}/user/save.do",
			onSubmit:function(){
				return $(this).form("validate");
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","保存成功！");
					resetValue();
					$("#dlg").dialog("close");
					$("#dg").datagrid("reload");
				}else{
					$.messager.alert("系统提示","保存失败！");
					return;
				}
			}
		 });
	}
	
	function checkData(){
		var flag=$("#flag").val();
		if(flag==1){
			$.post("${pageContext.request.contextPath}/user/existUserName.do",{userName:$("#id").val()},function(result){
				if(result.exist){
					 $.messager.alert("系统提示","该用户名已存在，请更换下！");
					 $("#id").focus();
				}else{
					saveUser();
				}
			},"json");
		}else{
			saveUser();
		}
		
		 
	}
	
	function resetValue(){
		 $("#id").val("");
		 $("#password").val("");
		 $("#firstName").val("");
		 $("#lastName").val("");
		 $("#email").val("");
	}
	
	function closeUserDialog(){
		 $("#dlg").dialog("close");
		 resetValue();
	}
	
	function deleteUser(){
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
				$.post("${pageContext.request.contextPath}/user/delete.do",{ids:ids},function(result){
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
<table id="dg" title="用户管理" class="easyui-datagrid"
   fitColumns="true" pagination="true" rownumbers="true"
   url="${pageContext.request.contextPath}/user/list.do" fit="true" toolbar="#tb">
   <thead>
   	<tr>
   		<th field="cb" checkbox="true" align="center"></th>
   		<th field="id" width="80" align="center">用户名</th>
   		<th field="password" width="80" align="center">密码</th>
   		<th field="firstName" width="50" align="center">姓</th>
   		<th field="lastName" width="50" align="center">名</th>
   		<th field="email" width="100" align="center">邮件</th>
   	</tr>
   </thead>
 </table>
 <div id="tb">
 	<div>
 		<a href="javascript:openUserAddDialog()" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
 		<a href="javascript:openUserModifyDialog()" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
 		<a href="javascript:deleteUser()" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
 	</div>
 	<div>
 		&nbsp;用户名：&nbsp;<input type="text" id="s_id" size="20" onkeydown="if(event.keyCode==13) searchUser()"/>
 		<a href="javascript:searchUser()" class="easyui-linkbutton" iconCls="icon-search" plain="true">搜索</a>
 	</div>
 </div>
 
 <div id="dlg" class="easyui-dialog" style="width:620px;height:250px;padding: 10px 20px"
   closed="true" buttons="#dlg-buttons">
   
   <form id="fm" method="post">
   	<table cellspacing="8px">
   		<tr>
   			<td>用户名：</td>
   			<td><input type="text" id="id" name="id" class="easyui-validatebox" required="true"/></td>
   			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
   			<td>密码</td>
   			<td><input type="text" id="password" name="password" class="easyui-validatebox" required="true"/></td>
   		</tr>
   		<tr>
   			<td>姓：</td>
   			<td><input type="text" id="firstName" name="firstName" class="easyui-validatebox" required="true"/></td>
   			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
   			<td>名：</td>
   			<td><input type="text" id="lastName" name="lastName" class="easyui-validatebox" required="true"/></td>
   		</tr>
   		<tr>
   		    <td>邮箱：</td>
   			<td colspan="4">
   				<input type="text" style="width: 200px" id="email" name="email" class="easyui-validatebox" validType="email" required="true"/>
   				<input type="hidden" id="flag" name="flag"/>
   			</td>
   		</tr>
   	</table>
   </form>
 </div>
 
 <div id="dlg-buttons">
 	<a href="javascript:checkData()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
 	<a href="javascript:closeUserDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
 </div>
</body>
</html>