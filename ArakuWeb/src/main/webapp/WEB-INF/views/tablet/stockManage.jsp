<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="./../resources/jquery/jquery.min.js"></script>
<script src="./../resources/bootstrap-notify/bootstrap-notify.min.js"></script>
<script>
$(document).ready(function() {
	$("#stockM").addClass("active");
	
	$("#register_date").datepicker({
		language: "ja"
		, autoclose: true
	});
});
</script>
</head>
<body>
<jsp:include page="../tabletTop.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<form action="showRList" id="orderForm">
	<input type="hidden" name="search_type" value="srch">
	<div class="col-sm-3">
	<label>ＪＡＮコード</label>
	<input type="text" name="jan_cd" class="form-control" style="width: 200px;">
	</div>
	<div class="col-sm-3">
	<label>入庫日</label>
	<!-- datepicker -->
	<input type="text" name="register_date" class="form-control" style="width: 150px;" id="register_date">
	</div>
	<div class="col-sm-3">
	<label>電話番号</label>
	<input type="text" name="order_no" class="form-control" style="width: 200px;">
	</div>
	<div class="col-sm-3">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<button id="btn_trans" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">置換</button>
<button id="btn_delete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>

<div id="stockMGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./../resources/tablet/stockManage.js"></script>
</body>
</html>