<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
<title>アラクシステム</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="../cdnList.jsp"></jsp:include>
<script src="<%=svrInfo %>/resources/test/angular.js"></script>
<script src="<%=svrInfo %>/resources/test/directives.js"></script>
<script src="<%=svrInfo %>/resources/test/controllers.js"></script>
<script src="<%=svrInfo %>/resources/test/services.js"></script>
<link rel="stylesheet" href="<%=svrInfo %>/resources/test/angular.css">
<script>
var svr = '<%=svrInfo %>';
</script>
</head>
<body ng-app="todo" ng-controller="TodoCtrl">
<!-- 
Directives	extend HTML with custom attributes and elements

Expressions	access variables and functions from the scope

Module	a container for the different parts of an app including 
controllers, services, filters, directives which configures the Injector

Controller	the business logic behind views

Service	reusable business logic independent of views
 -->
	<div class="container">
		<!-- 
		<h3>Todo</h3>
		 -->
		<todo-title></todo-title>
		<todo-form></todo-form>
		<pre>{{ todoFrm | json }}</pre>
		<ul class="list-unstyled">
			<li ng-repeat="todo in todos | filter:stsFilter">
				<todo-item></todo-item>
			</li>
		</ul>
		<todo-filters></todo-filters>
	</div>
</body>
</html>