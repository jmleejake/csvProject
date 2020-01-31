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
    
    $('#ama-txt').on('click', 'button', function () {
        $('#ama-upload').click();
        return false;
    });

    $('#ama-upload').on('change', function() {
        //選択したファイル情報を取得し変数に格納
        var file = $(this).prop('files')[0];
        //アイコンを選択中に変更
        $('#ama-txt').find('.icon').addClass('select').html('選択中');
        //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
        if(!($('.ama-filename').length)){
            $('#ama-txt').append('<div class="ama-filename"></div>');
        };
        //ファイル名を表示
        $('.ama-filename').html('ファイル名：' + file.name);
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
    
});
</script>
</head>
<body>
<jsp:include page="../amazonTop.jsp"></jsp:include>

<div class="container-fluid">

<div class="well container-fluid">
<form id="ama-frm" action="amaUpload" method="post" enctype="multipart/form-data" onsubmit="return preCheck('ama');">
<div id="ama-txt" class="file-up col-sm-8">
<input type="file" id="ama-upload" name="upload" multiple="multiple" style="display:none">
<button class="original_btn">アマゾンCSV</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<button id="btn_ama" class="original_btn">アップロード</button>
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

</div>
</body>
</html>