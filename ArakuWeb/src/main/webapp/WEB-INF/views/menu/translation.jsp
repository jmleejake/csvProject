<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="./resources/ag-grid/ag-grid.css">
<link rel="stylesheet" href="./resources/ag-grid/ag-theme-balham.css">
<script src="./resources/ag-grid/ag-grid.min.noStyle.js"></script>
</head>
<body>
<div class="well container">
	<div class="col-sm-6">
	<label>商品名</label>
	<input type="text" class="form-control" style="width: 400px;">
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_srch" type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
</div>
<div class="well container">
<div id="translateGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
<script src="./resources/menu/translation.js"></script>
</body>
</html>