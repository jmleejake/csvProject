<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="./resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.min.css">
<link rel="stylesheet" href="./resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker3.min.css">
<link rel="stylesheet" href="./resources/ag-grid/ag-grid.css">
<link rel="stylesheet" href="./resources/ag-grid/ag-theme-balham.css">

<script src="./resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.min.js"></script>
<script src="./resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.ja.min.js"></script>
<script src="./resources/ag-grid/ag-grid.min.noStyle.js"></script>

<script>
$(document).ready(function() {
	$("#r_order_date").datepicker({
		language: "ja"
		, autoclose: true
	});
});
</script>

</head>
<body>
<div class="well container">
	<div class="col-sm-4">
	<label>登録日付</label>
	<!-- datepicker -->
	<input type="text" class="form-control" style="width: 250px;" id="r_order_date">
	</div>
	<div class="col-sm-4">
	<label>名前</label>
	<input type="text" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-4">
	<label>電話番号</label>
	<input type="text" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-4" style="margin-top: 10px;">
	<label>注文番号</label>
	<input type="text" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-4" style="margin-top: 10px;">
	<label>お荷物伝票番号</label>
	<input type="text" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-4" style="margin-top: 10px;">
	<button type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
</div>
<div class="well container">
<button id="btn_rdown" type="button" class="btn btn-default" style="width: 140px; margin-bottom: 10px;">楽天更新ファイル</button>
<div id="rFileDownGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
<script src="./resources/menu/rFileDown.js"></script>
</body>
</html>