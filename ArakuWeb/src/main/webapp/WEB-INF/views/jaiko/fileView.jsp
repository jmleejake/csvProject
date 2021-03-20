<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<jsp:include page="../jaikoTop.jsp"></jsp:include>

<div class="container-fluid">

<div class="well container-fluid">
<form id="rak-frm" action="/jaiko/prdInven/csvUpload" method="post" enctype="multipart/form-data" onsubmit="return preCheck('rak');" >
<div id="rak-csv" class="file-up col-sm-8">
<input type="file" id="rak-upload" name="upload" style="display:none">
<button class="original_btn">棚卸入力表</button>
<span class="icon">未選択</span>
</div>
<div class="col-sm-4">
<input type="submit" class="original_btn" value="アップロード">
</div>
</form>
</div>

</div>
</body>
<script>
$(document).ready(function() {
	$("#fileView").addClass("active");
	
	// 棚卸入力表
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
</html>