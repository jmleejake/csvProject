<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href="./resources/menu/fileView.css">
<script src="./resources/jquery/jquery.min.js"></script>
<script src="./resources/menu/fileView.js"></script>
<script src="./resources/bootstrap-notify/bootstrap-notify.min.js"></script>

<script>
$(document).ready(function() {
	$("#yuUp").addClass("active");
	
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
    
});
</script>
</head>
<body>
<jsp:include page="../top.jsp"></jsp:include>

<div class="container-fluid">

<div class="well container-fluid">
<form id="yu-frm" action="yuUpload" method="post" enctype="multipart/form-data" onsubmit="return preCheck('yu');">
<div id="yu-txt" class="file-up col-sm-8">
<input type="file" id="yu-upload" name="yuUpload" multiple="multiple" style="display:none">
<button class="original_btn">郵便局</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<button id="btn_yu" class="original_btn">アップロード</button>
</div>
</form>
</div>

</div>
</body>
</html>