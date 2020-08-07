<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="./../resources/fileView.css">
<script src="./../resources/jquery/jquery.min.js"></script>
<script src="./../resources/fileView.js"></script>
<script src="./../resources/bootstrap-notify/bootstrap-notify.min.js"></script>

<script>
$(document).ready(function() {
	$("#sales").addClass("active");
	showPrdCdMaster("R");
	
	// 楽天CSV
	$('#rak-csv').on('click', 'button', function () {
        $('#rak-upload').click();
        return false;
    });
	
	$('#rak-upload').on('change', function() {
	    //選択したファイル情報を取得し変数に格納
	    var file = $(this).prop('files')[0];
	    //アイコンを選択中に変更
	    $('#rak-csv').find('.icon').addClass('select').html('選択中');
	    //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
	    if(!($('.rak-filename').length)){
	        $('#rak-csv').append('<div class="rak-filename"></div>');
	    };
	    //ファイル名を表示
	    $('.rak-filename').html('ファイル名：' + file.name);
	});
    
});
</script>
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>

<div class="container-fluid">

<div class="container-fluid">
<!-- S지역예외데이터 마스터 -->
<div class="container-fluid col-sm-6">
<div class="well container-fluid">
<h3>商品番号管理マスタ</h3>

<div class="well container-fluid">
	<form id="prdFrm" action="getPrdCdMaster">
	<div class="col-sm-6">
	<label>商品番号データ</label>
	<input type="hidden" name="target_type" value="R">
	<input type="text" name="prd_cd" class="form-control" style="width: 300px;"
	onkeypress = "if(event.keyCode==13){ prdSearch(); event.preventDefault(); }">
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_prd_srch" type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_prd_add" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">列追加</button>
	<button id="btn_prd_commit" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_prd_delete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
	</div>
</div>

<div id="prdCdGrid" style="width:auto; height: 300px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./../resources/menu/prdCdMaster.js"></script>
</div>
<!-- E지역예외데이터 마스터 -->
</div>

<div class="well container-fluid">
<form id="rak-frm" action="csvUpload" method="post" enctype="multipart/form-data" onsubmit="return preCheck('rak');" >
<div id="rak-csv" class="file-up col-sm-8">
<input type="file" id="rak-upload" name="rakUpload" multiple="multiple" style="display:none">
<input type="hidden" name="type" value="SALES">
<button class="original_btn">楽天CSV</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<input type="submit" class="original_btn" value="アップロード">
</div>
</form>
</div>

<div class="well container-fluid">
<div class="col-sm-6">
<button id="btn_sales_down" type="button" class="btn btn-info" style="width: 120px;">売上CSV</button>
</div>
<div class="col-sm-6">
<button id="btn_srch" type="button" class="btn btn-default" style="width: 120px;">検索</button>
</div>
</div>

<div id="orderGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
<script src="./../resources/rakuten/orderInfo2.js"></script>
<form id="frm" action="uriageDown" method="post"></form>
</body>
</html>