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
	
	$("#receive_date").datepicker({
		language: "ja"
		, autoclose: true
	});
	
	$("#inp_jancd").focus();
});
</script>
</head>
<body>
<jsp:include page="../tabletTop.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<form action="showStockMng" id="stockForm">
	<div class="col-sm-3">
	<label>ＪＡＮコード</label>
	<input type="text" name="jan_cd" class="form-control" style="width: 200px;">
	</div>
	<div class="col-sm-3">
	<label>入庫日</label>
	<!-- datepicker -->
	<input type="text" name="receive_date" class="form-control" style="width: 150px;" id="receive_date">
	</div>
	<div class="col-sm-3">
	<label>取引先</label>
	<input type="text" name="dealer_id" class="form-control" style="width: 200px;" placeholder="IDまたは名">
	</div>
	<div class="col-sm-3">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px; margin-top: 25px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="well container-fluid">
<div class="col-sm-6">
<span id="sp_dealer" style="margin-right: 10px;"></span>
<span id="sp_jancd"></span>
</div>
<div class="col-sm-3">
<input type="text" id="inp_jancd" class="form-control" style="width: 200px;" placeholder="TEST ＪＡＮコード">
</div>
<div class="col-sm-3">
<button id="btn_test" type="button" class="btn btn-default" style="width: 100px;">TEST</button>
</div>
</div>
<div class="col-sm-8">
<div id="stockMGrid" style="width:auto; height: 300px;" class="ag-theme-balham"></div>
</div>
<div class="col-sm-4">
<div id="dealerGrid" style="width:auto; height: 300px;" class="ag-theme-balham"></div>
</div>
<div class="well container-fluid">
<button id="btn_commit" type="button" class="btn btn-default" style="width: 140px; margin-bottom: 10px;">登録</button>
<button id="btn_down" type="button" class="btn btn-default" style="width: 140px; margin-bottom: 10px;">在庫ダウンロード</button>
</div>
</div>
</div>
<script src="./../resources/tablet/stockManage.js"></script>
<form id="frmDown" action="stockDown" method="post">
<input type="hidden" name="hidSeqId" id="hidSeqId">
</form>
</body>
</html>