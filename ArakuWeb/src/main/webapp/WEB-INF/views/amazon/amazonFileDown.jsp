<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="./../resources/jquery/jquery.min.js"></script>
<script>
$(document).ready(function() {
	$("#aFile").addClass("active");
	
	$("#register_date").datepicker({
		language: "ja"
		, autoclose: true
	});
});
</script>

</head>
<body>
<jsp:include page="../amazonTop.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<form action="showAList" id="aFileDownForm">
	<input type="hidden" name="search_type" value="srch">
	<div class="col-sm-4">
	<label>登録日付</label>
	<!-- datepicker -->
	<input type="text" name="register_date" class="form-control" style="width: 250px;" id="register_date">
	</div>
	<div class="col-sm-4">
	<label>注文ID</label>
	<input type="text" name="order_id" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-4">
	<label>お荷物伝票番号</label>
	<input type="text" name="baggage_claim_no" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-6" style="margin-top: 10px;">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_adown" type="button" class="btn btn-default" style="width: 180px; margin-bottom: 10px;">Amazon更新ファイル</button>
	</div>
</div>

<div id="aFileDownGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./../resources/amazon/aFileDown.js"></script>

<form id="frm_aFileDown" action="aFileDown" method="post">
<input type="hidden" id="seq_id_list" name="id_lst">
</form>
</body>
</html>