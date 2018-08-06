<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="./resources/menu/fileView.css">

<script>
$(document).ready(function() {
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
    
    $('#yu-txt').on('click', 'button', function () {
        $('#yu-upload').click();
        return false;
    });

    $('#yu-upload').on('change', function() {
        //選択したファイル情報を取得し変数に格納
        var file = $(this).prop('files')[0];
        //アイコンを選択中に変更
        $('#yu-txt').find('.icon').addClass('select').html('選択中');
        //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
        if(!($('.yu-filename').length)){
            $('#yu-txt').append('<div class="yu-filename"></div>');
        };
        //ファイル名を表示
        $('.yu-filename').html('ファイル名：' + file.name);
    });
    
    $("#btn_rak").on("click", function() {
    	var file = $('#rak-upload').val();
    	
    	if(file.length == 0) return false;
    	
    	$("#rak-frm").submit();
    });
});
</script>
</head>
<body>
<div class="well container">
<form id="rak-frm" action="csvUpload" method="post" enctype="multipart/form-data">
<div id="rak-csv" class="file-up col-sm-8">
<!-- 非表示にするボタン -->
<input type="file" id="rak-upload" name="upload" multiple="multiple" style="display:none">
<!-- オリジナルボタン -->
<button class="original_btn">楽天CSV</button>
<!-- アイコン -->
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<button id="btn_rak" class="original_btn">アップロード</button>
</div>
</form>
</div>

<div class="well container">
<form id="ama-frm" action="csvUpload" method="post" enctype="multipart/form-data">
<div id="ama-txt" class="file-up col-sm-8">
<!-- 非表示にするボタン -->
<input type="file" id="ama-upload" name="upload" multiple="multiple" style="display:none">
<!-- オリジナルボタン -->
<button class="original_btn">アマゾンCSV</button>
<!-- アイコン -->
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<button id="btn_ama" class="original_btn">アップロード</button>
</div>
</form>
</div>

<div class="well container-fluid">
<form id="yu-frm" action="csvUpload" method="post" enctype="multipart/form-data">
<div id="yu-txt" class="file-up col-sm-8">
<!-- 非表示にするボタン -->
<input type="file" id="yu-upload" name="upload" multiple="multiple" style="display:none">
<!-- オリジナルボタン -->
<button class="original_btn">郵便局TXT</button>
<!-- アイコン -->
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<button id="btn_yu" class="original_btn">アップロード</button>
</div>
</form>
</div>

</body>
</html>