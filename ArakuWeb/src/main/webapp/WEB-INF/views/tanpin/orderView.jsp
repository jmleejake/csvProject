<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix ="c" uri ="http://java.sun.com/jsp/jstl/core" %>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<jsp:include page="../tanpinTop.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<div class="col-sm-6" >
	<div class="dropdown">
		<button class="btn btn-primary dropdown-toggle" id="menu1" type="button" data-toggle="dropdown">
		取引先選択<span id="selectedMaker"></span>  <span class="caret"></span></button>
		<ul class="dropdown-menu" role="menu" aria-labelledby="menu1">
			<c:forEach items="${dealers }" var="dealer">
			<li role="presentation"><a role="menuitem" href="javascript:showOrder('${dealer.dealer_id }')">${dealer.dealer_nm }</a></li>
			</c:forEach>
		</ul>
	</div>
	</div>
	<div class="col-sm-6" >
	<button type="button" id="btn_down" class="btn btn-info" style="width: 120px;">ダウンロード</button>
	</div>
</div>
<div class="well container-fluid">
<div class="row">
</div>
<div id="orderGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/tanpin/orderView.js"></script>
</body>
<script>
$(document).ready(function() {
	$("#order").addClass("active");
});
</script>
</html>