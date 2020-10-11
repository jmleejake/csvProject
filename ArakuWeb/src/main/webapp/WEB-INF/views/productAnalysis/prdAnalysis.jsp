<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix ="c" uri ="http://java.sun.com/jsp/jstl/core" %>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<jsp:include page="../tanpinTop.jsp"></jsp:include>
<div class="container-fluid">
<div class="well container-fluid">
	<form id="frm">
	<input type="hidden" name="search_type" value="srch"> 
	<div class="col-sm-4" >
	<input type="text" name="memo" class="form-control" style="width: 300px;" placeholder="MEMO">
	</div>
	<div class="col-sm-4" >
	<input type="text" name="prd_nm" class="form-control" style="width: 300px;" placeholder="商品名">
	</div>
	</form>
	<div class="col-sm-4">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px;">検索</button>
	</div>
</div>
<div class="well container-fluid">
	<div class="col-sm-8">
	<form id="prdAnalysis-frm" action="prdAnalFileUp" method="post" enctype="multipart/form-data" onsubmit="return preCheck('prdAnal');" >
		<div id="prdAnalysis-csv" class="file-up col-sm-8">
		<input type="file" id="prdAnalysis-upload" name="upload" multiple="multiple" style="display:none">
		<button class="original_btn" style="width: 150px;">商品分析ファイル</button>
		<span class="icon">未選択</span>
		</div>
		<div class="col-sm-4">
		<input type="submit" class="original_btn" value="アップロード">
		</div>
	</form>
	</div>
	<div class="col-sm-4">
	<button type="button" id="btn_down" class="btn btn-info" style="width: 120px;">ダウンロード</button>
	<button type="button" id="btn_upd" class="btn btn-default" style="width: 120px;">修正</button>
	<button type="button" id="btn_del" class="btn btn-danger" style="width: 120px;">削除</button>
	</div>
</div>
<div class="well container-fluid">

<div id="prdMGrid" style="width:auto; height: 600px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/productAnalysis/prdAnalysis.js"></script>

<form id="frm_down" action="downPrdAnal" method="post">
<input type="hidden" id="seq_id_list" name="id_lst">
</form>
</body>
<script>
$(document).ready(function() {
	$("#prdA").addClass("active");
	
	/* File Upload*/
	$('#prdAnalysis-csv').on('click', 'button', function () {
        $('#prdAnalysis-upload').click();
        return false;
    });
	
	$('#prdAnalysis-upload').on('change', function() {
	    //選択したファイル情報を取得し変数に格納
	    var file = $(this).prop('files')[0];
	    //アイコンを選択中に変更
	    $('#prdAnalysis-csv').find('.icon').addClass('select').html('選択中');
	    //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
	    if(!($('.prdAnalysis-filename').length)){
	        $('#prdAnalysis-csv').append('<div class="prdAnalysis-filename"></div>');
	    };
	    //ファイル名を表示
	    $('.prdAnalysis-filename').html('ファイル名：' + file.name);
	});
	/* //File Upload*/
});
</script>
</html>