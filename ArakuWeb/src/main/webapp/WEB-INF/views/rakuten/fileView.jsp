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
	$("#home").addClass("active");
	
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
	
	// CSV一括編集
	$('#items-csv').on('click', 'button', function () {
        $('#items-upload').click();
        return false;
    });
	
	$('#items-upload').on('change', function() {
	    //選択したファイル情報を取得し変数に格納
	    var file = $(this).prop('files')[0];
	    //アイコンを選択中に変更
	    $('#items-csv').find('.icon').addClass('select').html('選択中');
	    //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
	    if(!($('.items-filename').length)){
	        $('#items-csv').append('<div class="items-filename"></div>');
	    };
	    //ファイル名を表示
	    $('.items-filename').html('ファイル名：' + file.name);
	});
	
	// SAGAWA CSV
	$('#saga-csv').on('click', 'button', function () {
        $('#saga-upload').click();
        return false;
    });
	
	$('#saga-upload').on('change', function() {
	    //選択したファイル情報を取得し変数に格納
	    var file = $(this).prop('files')[0];
	    //アイコンを選択中に変更
	    $('#saga-csv').find('.icon').addClass('select').html('選択中');
	    //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
	    if(!($('.saga-filename').length)){
	        $('#saga-csv').append('<div class="saga-filename"></div>');
	    };
	    //ファイル名を表示
	    $('.saga-filename').html('ファイル名：' + file.name);
	});
	
	// YAMATO CSV
	$('#yama-csv').on('click', 'button', function () {
        $('#yama-upload').click();
        return false;
    });
	
	$('#yama-upload').on('change', function() {
	    //選択したファイル情報を取得し変数に格納
	    var file = $(this).prop('files')[0];
	    //アイコンを選択中に変更
	    $('#yama-csv').find('.icon').addClass('select').html('選択中');
	    //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
	    if(!($('.yama-filename').length)){
	        $('#yama-csv').append('<div class="yama-filename"></div>');
	    };
	    //ファイル名を表示
	    $('.yama-filename').html('ファイル名：' + file.name);
	});
	
	// SAGAWA CSV 202006
	$('#saga-csv2006').on('click', 'button', function () {
        $('#saga-upload2006').click();
        return false;
    });
	
	$('#saga-upload2006').on('change', function() {
	    //選択したファイル情報を取得し変数に格納
	    var file = $(this).prop('files')[0];
	    //アイコンを選択中に変更
	    $('#saga-csv2006').find('.icon').addClass('select').html('選択中');
	    //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
	    if(!($('.saga-filename2006').length)){
	        $('#saga-csv2006').append('<div class="saga-filename2006"></div>');
	    };
	    //ファイル名を表示
	    $('.saga-filename2006').html('ファイル名：' + file.name);
	});
    
});
</script>
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>

<div class="container-fluid">

<div class="well container-fluid">
<form id="rak-frm" action="csvUpload" method="post" enctype="multipart/form-data" onsubmit="return preCheck('rak');" >
<div id="rak-csv" class="file-up col-sm-8">
<input type="file" id="rak-upload" name="rakUpload" multiple="multiple" style="display:none">
<button class="original_btn">楽天CSV</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<input type="submit" class="original_btn" value="アップロード">
</div>
</form>
</div>

<div class="well container-fluid">
<form id="items-frm" action="itemsUpload" method="post" enctype="multipart/form-data" onsubmit="return preCheck('items');" >
<div id="items-csv" class="file-up col-sm-8">
<input type="file" id="items-upload" name="itemsUpload" multiple="multiple" style="display:none">
<button class="original_btn">CSV一括編集</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<input type="submit" class="original_btn" value="アップロード">
</div>
</form>
</div>

<div class="well container-fluid">
<form id="saga-frm" action="sagaUpload" method="post" enctype="multipart/form-data" onsubmit="return preCheck('saga');" >
<div id="saga-csv" class="file-up col-sm-8">
<input type="file" id="saga-upload" name="sagaUpload" multiple="multiple" style="display:none">
<button class="original_btn">SAGAWA CSV</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<input type="submit" class="original_btn" value="アップロード">
</div>
</form>
</div>

<div class="well container-fluid">
<form id="yama-frm" action="yamaUpload" method="post" enctype="multipart/form-data" onsubmit="return preCheck('yama');" >
<div id="yama-csv" class="file-up col-sm-8">
<input type="file" id="yama-upload" name="yamaUpload" multiple="multiple" style="display:none">
<button class="original_btn">YAMATO CSV</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<input type="submit" class="original_btn" value="アップロード">
</div>
</form>
</div>

<div class="well container-fluid">
<form id="saga-frm2006" action="sagaUpload2006" method="post" enctype="multipart/form-data" onsubmit="return preCheck('saga2006');" >
<div id="saga-csv2006" class="file-up col-sm-8">
<input type="file" id="saga-upload2006" name="sagaUpload2006" multiple="multiple" style="display:none">
<button class="original_btn">グローバル<br/>SAGAWA CSV</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<input type="submit" class="original_btn" value="アップロード">
</div>
</form>
</div>

</div>
</body>
</html>