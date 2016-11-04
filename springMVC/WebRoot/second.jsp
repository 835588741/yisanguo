<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css"
			href="${pageContext.request.contextPath}/easyui/demo//demo.css">
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/easyui/jquery-1.8.0.min.js"></script>
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/easyui/jquery.easyui.min.js"></script>
			
		<script type="text/javascript">
		$(function(){
			$.post("loginAction/doJsonTest.action",{},function(data,state){
				alert(data[0].name);
			});
				
		});
		</script>
   
    <title>My JSP 'second.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
  </head>
  
  <body>
  <a>111</a>
  <!--  
    <c:forEach items="${myList}" var = "mybean">
    	${mybean.name}
    </c:forEach>
    -->
  </body>
</html>
