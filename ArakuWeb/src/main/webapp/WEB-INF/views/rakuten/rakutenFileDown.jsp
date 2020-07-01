<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="./../resources/jquery/jquery.min.js"></script>
<script>
$(document).ready(function() {
	$("#rFile").addClass("active");
	
	$("#register_date").datepicker({
		language: "ja"
		, autoclose: true
	});
});
</script>

</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<form action="showRList" id="rFileDownForm">
	<input type="hidden" name="search_type" value="srch">
	<div class="col-sm-4">
	<label>登録日付</label>
	<!-- datepicker -->
	<input type="text" name="register_date" class="form-control" style="width: 250px;" id="register_date">
	</div>
	<div class="col-sm-4">
	<label>注文番号</label>
	<input type="text" name="order_no" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-4">
	<label>お荷物伝票番号</label>
	<input type="text" name="baggage_claim_no" class="form-control" style="width: 250px;">
	</div>
	<!-- 
	<div class="col-sm-4">
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
	<div class="col-sm-4">
	<label>電話番号</label>
	<div class="row">
	<input type="text" name="delivery_tel1" class="form-control" style="width:100px; margin-right: 2px; margin-left: 15px; float: left;">
	<label style="float: left; font-size: 25px;">-</label>
	<input type="text" name="delivery_tel2" class="form-control" style="width:100px; margin-right: 2px; float: left;">
	<label style="float: left; font-size: 25px;">-</label>
	<input type="text" name="delivery_tel3" class="form-control" style="width:100px;">
	</div>
	</div>
	-->
	<div class="col-sm-6" style="margin-top: 10px;">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<!-- 
	<button id="btn_commit" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	-->
	<!-- 
	<button id="btn_rdown" type="button" class="btn btn-default" style="width: 140px; margin-bottom: 10px;">楽天更新ファイル</button>
	-->
	<button id="btn_yama" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">YAMATO</button>
	<button id="btn_saga" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">SAGAWA</button>
	</div>
</div>

<div id="rFileDownGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./../resources/rakuten/rFileDown.js"></script>

<form id="frm_rFileDown" action="rFileDown" method="post">
<input type="hidden" id="seq_id_list" name="id_lst">
<input type="hidden" id="delCompany" name="delivery_company">
</form>
</body>
</html>