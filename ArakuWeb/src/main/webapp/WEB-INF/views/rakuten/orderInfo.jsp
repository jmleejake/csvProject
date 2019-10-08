<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="./../resources/fileView.css">
<script src="./../resources/jquery/jquery.min.js"></script>
<script src="./../resources/fileView.js"></script>
<script src="./../resources/bootstrap-notify/bootstrap-notify.min.js"></script>
<script>
$(document).ready(function() {
	$("#order").addClass("active");
	
	$("#register_date").datepicker({
		language: "ja"
		, autoclose: true
	});
	
	errMsg("${errSize}");
	
	$('#del-csv').on('click', 'button', function () {
        $('#del-upload').click();
        return false;
    });
	
	$('#del-upload').on('change', function() {
	    //選択したファイル情報を取得し変数に格納
	    var file = $(this).prop('files')[0];
	    //アイコンを選択中に変更
	    $('#del-csv').find('.icon').addClass('select').html('選択中');
	    //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
	    if(!($('.del-filename').length)){
	        $('#del-csv').append('<div class="del-filename"></div>');
	    };
	    //ファイル名を表示
	    $('.del-filename').html('ファイル名：' + file.name);
	});
});
</script>
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<form action="showRList" id="orderForm">
	<input type="hidden" name="search_type" value="srch">
	<div class="col-sm-4">
	<label>登録日付</label>
	<!-- datepicker -->
	<input type="text" name="register_date" class="form-control" style="width: 250px;" id="register_date">
	</div>
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
	<div class="col-sm-4" style="margin-top: 10px;">
	<label>注文番号</label>
	<input type="text" name="order_no" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-4" style="margin-top: 10px;">
	<label>お荷物伝票番号</label>
	<input type="text" name="baggage_claim_no" class="form-control" style="width: 250px;">
	</div>
	<div class="col-sm-4" style="margin-top: 10px;">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<button id="btn_trans" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">置換</button>
<button id="btn_delete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>

<div class="well container-fluid">
<form id="del-frm" action="delFileUpload" method="post" enctype="multipart/form-data" onsubmit="return preCheck('del');" >
<div id="del-csv" class="file-up col-sm-8">
<input type="file" id="del-upload" name="delUpload" multiple="multiple" style="display:none">
<button class="original_btn" style="width: 150px;">問合受注番号削除</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<input type="submit" class="original_btn" value="アップロード">
</div>
</form>
</div>

<div id="orderGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./../resources/rakuten/orderInfo.js"></script>
<form action="resultView" id="order_frm" method="post">
<input type="hidden" id="hid_data" name="list" >
</form>
<form action="errListDown" id="edown_frm" method="post"></form>
<form action="orderView" id="callView"></form>
</body>
</html>