<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<form action="showRList" id="claimForm">
	<input type="hidden" name="search_type" value="srch">
	<div class="col-sm-6">
	<label>名前</label>
	<div class="row">
	<div class="col-sm-6">
	<input type="text" name="delivery_surname" class="form-control" style="margin-right: 5px;" placeholder="名字">
	</div>
	<div class="col-sm-6">
	<input type="text" name="delivery_name" class="form-control" style="margin-right: 5px;" placeholder="名前">
	</div>
	</div>
	</div>
	<div class="col-sm-6">
	<label>電話番号</label>
	<div class="row">
	<input type="text" name="delivery_tel1" class="form-control" style="width:100px; margin-right: 2px; margin-left: 15px; float: left;">
	<label style="float: left; font-size: 25px;">-</label>
	<input type="text" name="delivery_tel2" class="form-control" style="width:100px; margin-right: 2px; float: left;">
	<label style="float: left; font-size: 25px;">-</label>
	<input type="text" name="delivery_tel3" class="form-control" style="width:100px;">
	</div>
	</div>
	<div class="col-sm-6" style="margin-top: 10px;">
	<label>注文番号</label>
	<input type="text" name="order_no" class="form-control" style="width: 400px;">
	</div>
	<div class="col-sm-6" style="margin-top: 10px;">
	<label></label>
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div id="claimUpGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/menu/claimUp.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#claimNo").addClass("active");
});
</script>
</body>
</html>