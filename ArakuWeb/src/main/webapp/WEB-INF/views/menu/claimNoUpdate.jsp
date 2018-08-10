<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<script src="./resources/jquery/jquery.min.js"></script>
<link rel="stylesheet" href="./resources/ag-grid/ag-grid.css">
<link rel="stylesheet" href="./resources/ag-grid/ag-theme-balham.css">
<script src="./resources/ag-grid/ag-grid.min.noStyle.js"></script>

<script>
$(document).ready(function() {
	$("#claimNo").addClass("active");
});
</script>
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<div class="col-sm-6">
	<label>名前</label>
	<input type="text" class="form-control" style="width: 400px;">
	</div>
	<div class="col-sm-6">
	<label>電話番号</label>
	<input type="text" class="form-control" style="width: 400px;">
	</div>
	<div class="col-sm-6" style="margin-top: 10px;">
	<label>注文番号</label>
	<input type="text" class="form-control" style="width: 400px;">
	</div>
	<div class="col-sm-6" style="margin-top: 10px;">
	<label></label>
	<button type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
</div>
<div class="well container-fluid">
<div id="claimUpGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./resources/menu/claimUp.js"></script>
</body>
</html>