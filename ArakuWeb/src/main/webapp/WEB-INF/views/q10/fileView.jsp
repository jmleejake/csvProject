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
    
    $('#q-txt').on('click', 'button', function () {
        $('#q-upload').click();
        return false;
    });

    $('#q-upload').on('change', function() {
        //選択したファイル情報を取得し変数に格納
        var file = $(this).prop('files')[0];
        //アイコンを選択中に変更
        $('#q-txt').find('.icon').addClass('select').html('選択中');
        //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
        if(!($('.q-filename').length)){
            $('#q-txt').append('<div class="q-filename"></div>');
        };
        //ファイル名を表示
        $('.q-filename').html('ファイル名：' + file.name);
    });
    
});
</script>
</head>
<body>
<jsp:include page="../q10Top.jsp"></jsp:include>

<div class="container-fluid">

<div class="well container-fluid">
<form id="q-frm" action="qUpload" method="post" enctype="multipart/form-data" onsubmit="return preCheck('q10');">
<div id="q-txt" class="file-up col-sm-8">
<input type="file" id="q-upload" name="upload" multiple="multiple" style="display:none">
<button class="original_btn">QOO10CSV</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<button id="btn_q" class="original_btn">アップロード</button>
</div>
</form>
</div>

</div>
</body>
</html>