<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="./resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.min.css">
<link rel="stylesheet" href="./resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker3.min.css">
<link rel="stylesheet" href="./resources/ag-grid/ag-grid.css">
<link rel="stylesheet" href="./resources/ag-grid/ag-theme-balham.css">
<link rel="stylesheet" href="./resources/alertfy-js-0.3.11/alertify.core.css">
<link rel="stylesheet" href="./resources/alertfy-js-0.3.11/alertify.default.css">

<script src="./resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.min.js"></script>
<script src="./resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.ja.min.js"></script>
<script src="./resources/ag-grid/ag-grid.min.noStyle.js"></script>
<script src="./resources/alertfy-js-0.3.11/alertify.min.js"></script>

<script>
$(document).ready(function() {
	$("#order_date").datepicker({
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
	<input type="text" class="form-control" style="width: 250px;" id="order_date">
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
<button id="btn_trans" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">置換</button>
<div id="orderGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
<button id="btn_down" type="button" class="btn btn-default" style="width: 120px; margin-top: 10px;" disabled="disabled">ダウンロード</button>
</div>
<script src="./resources/menu/orderInfo.js"></script>
</body>
</html>