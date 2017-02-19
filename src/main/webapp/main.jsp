<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生请假系统-主界面</title>
<%
	// 权限验证
	if(session.getAttribute("currentMemberShip")==null){
		response.sendRedirect("login.jsp");
		return;
	}
%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">

	var url;
	
	function openTab(text,url,iconCls){
		if($("#tabs").tabs("exists",text)){
			$("#tabs").tabs("select",text);
		}else{
			var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='${pageContext.request.contextPath}/page/"+url+"'></iframe>";
			$("#tabs").tabs("add",{
				title:text,
				iconCls:iconCls,
				closable:true,
				content:content
			});
		}
	}
	
	function openPasswordModifyDialog(){
		$("#dlg").dialog("open").dialog("setTitle","修改密码");
		url="${pageContext.request.contextPath}/user/modifyPassword.do?id=${currentMemberShip.user.id }";
	}
	
	function modifyPassword(){
		$("#fm").form("submit",{
			url:url,
			onSubmit:function(){
				var oldPassword=$("#oldPassword").val();
				var newPassword=$("#newPassword").val();
				var newPassword2=$("#newPassword2").val();
				if(!$(this).form("validate")){
					return false;
				}
				if(oldPassword!='${currentMemberShip.user.password }'){
					$.messager.alert("系统提示","用户原密码输入错误！");
					return false;
				}
				if(newPassword!=newPassword2){
					$.messager.alert("系统提示","确认密码输入错误！");
					return false;
				}
				return true;
			},
			success:function(result){
				var result=eval('('+result+')');
				if(result.success){
					$.messager.alert("系统提示","密码修改成功，下一次登录生效！");
					resetValue();
					$("#dlg").dialog("close");
				}else{
					$.messager.alert("系统提示","密码修改失败！");
					return;
				}
			}
		 });
	}
	
	function closePasswordModifyDialog(){
		resetValue();
		$("#dlg").dialog("close");
	}
	
	function resetValue(){
		$("#oldPassword").val("");
		$("#newPassword").val("");
		$("#newPassword2").val("");
	}
	
	function logout(){
		$.messager.confirm("系统提示","您确定要退出系统吗？",function(r){
			if(r){
				window.location.href='${pageContext.request.contextPath}/user/logout.do';
			} 
		 });
	}
	
	function logout(){
		$.messager.confirm("系统提示","您确定要退出系统吗？",function(r){
			if(r){
				window.location.href='${pageContext.request.contextPath}/user/logout.do';
			} 
		 });
	}
	
</script>
</head>
<body class="easyui-layout">
<div region="north" style="height: 78px;background-color: #E0ECFF">
	<table style="padding: 5px" width="100%">
		<tr>
			<td width="50%">
				<img alt="logo" src="${pageContext.request.contextPath}/static/images/logo.png">
			</td>
			<td valign="bottom" align="right" width="50%">
				<font size="3">&nbsp;&nbsp;<strong>欢迎：</strong>${currentMemberShip.user.id }(${currentMemberShip.user.firstName }${currentMemberShip.user.lastName })【${currentMemberShip.group.name }】</font>
			</td>
		</tr>
	</table>
</div>
<div region="center">
	<div class="easyui-tabs" fit="true" border="false" id="tabs">
		<div title="首页" data-options="iconCls:'icon-home'">
			<div align="center" style="padding-top: 100px"><font color="red" size="10">欢迎使用</font></div>
		</div>
	</div>
</div>
<div region="west" style="width: 200px" title="导航菜单" split="true">
	<div class="easyui-accordion" data-options="fit:true,border:false">
	    <c:if test="${currentMemberShip.group.name=='管理员' }">
			<div title="基础数据管理" data-options="selected:true,iconCls:'icon-item'" style="padding: 10px">
				<a href="javascript:openTab('用户管理','userManage.jsp','icon-user')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-user'" style="width: 150px">用户管理</a>
				<a href="javascript:openTab('角色管理','groupManage.jsp','icon-role')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-role'" style="width: 150px">角色管理</a>
				<a href="javascript:openTab('用户权限管理','authManage.jsp','icon-power')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-power'" style="width: 150px">用户权限管理</a>
			</div>
			<div title="流程管理"  data-options="iconCls:'icon-flow'" style="padding:10px;">
				<a href="javascript:openTab('流程部署管理','deployManage.jsp','icon-deploy')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-deploy'" style="width: 150px;">流程部署管理</a>
				<a href="javascript:openTab('流程定义管理','processDefinitionManage.jsp','icon-definition')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-definition'" style="width: 150px;">流程定义管理</a>
			</div>
	    </c:if>
		<div title="任务管理" data-options="iconCls:'icon-task'" style="padding:10px">
			<a href="javascript:openTab('待办任务管理','daibanManage.jsp','icon-daiban')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-daiban'" style="width: 150px;">待办任务管理</a>
			<a href="javascript:openTab('已办任务管理','yibanManage.jsp','icon-yiban')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-yiban'" style="width: 150px;">已办任务管理</a>
			<a href="javascript:openTab('历史任务管理','lishiManage.jsp','icon-lishi')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-lishi'" style="width: 150px;">历史任务管理</a>
		</div>
		<c:if test="${currentMemberShip.group.name=='学生' }">
			<div title="业务管理"  data-options="iconCls:'icon-yewu'" style="padding:10px">
				<a href="javascript:openTab('请假申请','leaveManage.jsp','icon-apply')" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-apply'" style="width: 150px">请假申请</a>
			</div>
		</c:if>
		<div title="系统管理"  data-options="iconCls:'icon-system'" style="padding:10px">
			<a href="javascript:openPasswordModifyDialog()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-modifyPassword'" style="width: 150px;">修改密码</a>
			<a href="javascript:logout()" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-exit'" style="width: 150px;">安全退出</a>
		</div>
	</div>
</div>
<div region="south" style="height: 26px;padding: 5px" align="center">
	Copyright © devlop by tb
</div>

<div id="dlg" class="easyui-dialog" style="width:400px;height:250px;padding: 10px 20px"
   closed="true" buttons="#dlg-buttons">
   
   <form id="fm" method="post">
   	<table cellspacing="8px">
   		<tr>
   			<td>用户名：</td>
   			<td><input type="text" id="userId" name="userId" readonly="readonly" value="${currentMemberShip.user.id }" style="width: 200px"/></td>
   		</tr>
   		<tr>
   			<td>原密码：</td>
   			<td><input type="password" id="oldPassword" name="oldPassword" class="easyui-validatebox" required="true" style="width: 200px"/></td>
   		</tr>
   		<tr>
   			<td>新密码：</td>
   			<td><input type="password" id="newPassword" name="newPassword" class="easyui-validatebox" required="true" style="width: 200px"/></td>
   		</tr>
   		<tr>
   			<td>确认新密码：</td>
   			<td><input type="password" id="newPassword2" name="newPassword2" class="easyui-validatebox" required="true" style="width: 200px"/></td>
   		</tr>
   	</table>
   </form>
 </div>
 
 <div id="dlg-buttons">
 	<a href="javascript:modifyPassword()" class="easyui-linkbutton" iconCls="icon-ok">保存</a>
 	<a href="javascript:closePasswordModifyDialog()" class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
 </div>
 
</body>
</html>