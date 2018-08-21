<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="./resources/ag-grid/ag-grid.css">
<link rel="stylesheet" href="./resources/ag-grid/ag-theme-balham.css">

<script src="./resources/jquery/jquery.min.js"></script>
<script src="./resources/ag-grid/ag-grid.min.noStyle.js"></script>

<script>
$(document).ready(function() {
	$("#order").addClass("active");
	
	console.log($("#id_lst").val());
	getData();
});
</script>
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
<button id="btn_down" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">ダウンロード</button>
<div id="resultGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./resources/menu/transResult.js"></script>
<form id="frm_down" action="yuDown" method="post">
<input type="hidden" id="id_lst" name="id_lst" value="${idList }">
</form>
</body>
</html>