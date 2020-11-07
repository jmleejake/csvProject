<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<jsp:include page="../yahooTop.jsp"></jsp:include>

<div class="container-fluid">

<div class="well container-fluid">
<form id="yhsh-frm" action="yhShouhin" method="post" enctype="multipart/form-data" onsubmit="return preCheck('yhsh');">
<div id="yhsh-txt" class="file-up col-sm-8">
<input type="file" id="yhsh-upload" name="upload" multiple="multiple" style="display:none">
<button class="original_btn">1.ヤフ商品</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<button id="btn_yhsh" class="original_btn">アップロード</button>
</div>
</form>
</div>

<div class="well container-fluid">
<form id="yhch-frm" action="yhChumon" method="post" enctype="multipart/form-data" onsubmit="return preCheck('yhch');">
<div id="yhch-txt" class="file-up col-sm-8">
<input type="file" id="yhch-upload" name="upload" multiple="multiple" style="display:none">
<button class="original_btn">2.ヤフ注文</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<button id="btn_yhch" class="original_btn">アップロード</button>
</div>
</form>
</div>

</div>
</body>
<script>
$(document).ready(function() {
	$("#home").addClass("active");
    
    // ヤフ商品
	$('#yhsh-txt').on('click', 'button', function () {
        $('#yhsh-upload').click();
        return false;
    });

    $('#yhsh-upload').on('change', function() {
        //選択したファイル情報を取得し変数に格納
        var file = $(this).prop('files')[0];
        //アイコンを選択中に変更
        $('#yhsh-txt').find('.icon').addClass('select').html('選択中');
        //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
        if(!($('.yhsh-filename').length)){
            $('#yhsh-txt').append('<div class="yhsh-filename"></div>');
        };
        //ファイル名を表示
        $('.yhsh-filename').html('ファイル名：' + file.name);
    });
    
    // ヤフ注文
    $('#yhch-txt').on('click', 'button', function () {
        $('#yhch-upload').click();
        return false;
    });

    $('#yhch-upload').on('change', function() {
        //選択したファイル情報を取得し変数に格納
        var file = $(this).prop('files')[0];
        //アイコンを選択中に変更
        $('#yhch-txt').find('.icon').addClass('select').html('選択中');
        //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
        if(!($('.yhch-filename').length)){
            $('#yhch-txt').append('<div class="yhch-filename"></div>');
        };
        //ファイル名を表示
        $('.yhch-filename').html('ファイル名：' + file.name);
    });
    
});
</script>
</html>