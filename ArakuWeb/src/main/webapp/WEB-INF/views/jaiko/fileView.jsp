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

<div class="well container-fluid">
<form id="k-center-frm" enctype="multipart/form-data" onsubmit="return preCheck('k-center');" >
<div id="k-center-csv" class="file-up col-sm-8">
<input type="file" id="k-center-upload" name="upload" style="display:none">
<button class="original_btn">韓国広場　物流センター</button>
<span class="icon">未選択</span>
</div>
</form>
<div class="col-sm-4">
<!-- <input type="submit" class="original_btn" value="アップロード"> -->
<button id="k-center-submit" class="original_btn">アップロード</button>
</div>
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
	
	// 株式会社　韓国広場　物流センター
	$('#k-center-csv').on('click', 'button', function () {
        $('#k-center-upload').click();
        return false;
    });
	
	$('#k-center-upload').on('change', function() {
	    //選択したファイル情報を取得し変数に格納
	    var file = $(this).prop('files')[0];
	    //アイコンを選択中に変更
	    $('#k-center-csv').find('.icon').addClass('select').html('選択中');
	    //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
	    if(!($('.k-center-filename').length)){
	        $('#k-center-csv').append('<div class="k-center-filename"></div>');
	    };
	    //ファイル名を表示
	    $('.k-center-filename').html('ファイル名：' + file.name);
	});
	
	$('#k-center-submit').on('click', function() {
		var formData = new FormData($("#k-center-frm")[0]);
		 
	    $.ajax({
	        type : 'post',
	        url : '/jaiko/warehouse/kCenterImg',
	        data : formData,
	        processData : false,
	        dataType : "json",
	        contentType : false,
	        success : function(data) {
	        	console.log(data);
	            alert("파일 업로드 성공.");
	            return;
	        },
	        error : function(error) {
	            alert("파일 업로드에 실패하였습니다.");
	            return;
	        }
	    });
	});
});
</script>
</html>