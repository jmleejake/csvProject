<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="./../resources/menu/regionMaster.css">
<script src="./../resources/jquery/jquery.min.js"></script>
<script>
$(document).ready(function() {
	$("#region").addClass("active");
});
</script>
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<form id="regionFrm" action="showRegionMaster">
	<div class="col-sm-6">
	<label>地域名称</label>
	<input type="text" name="keyword" class="form-control" style="width: 300px;">
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_srch" type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_commit" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	</div>
</div>

<div id="regionGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./../resources/menu/regionMaster.js"></script>
</body>
</html>