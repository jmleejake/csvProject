<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<jsp:include page="../jaikoTop.jsp"></jsp:include>
<div class="container-fluid">
<!-- search S -->
<div class="well container-fluid">
	<form id="frm_srch">
	<input type="hidden" name="search_type" value="srch">
	<div class="col-sm-3">
	<input type="text" name="jan_cd" class="form-control" id="jan_cd" placeholder="JANコード">
	</div>
	<div class="col-sm-3">
	<input type="text" name="prd_cd" class="form-control" id="prd_cd" placeholder="商品コード">
	</div>
	<div class="col-sm-4">
	<input type="text" name="prd_nm" class="form-control" id="prd_nm" placeholder="商品名">
	</div>
	<div class="col-sm-2">
	<button id="btn_search" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">検索</button>
	</div>
	</form>
</div>
<!-- search E -->

<!-- file upload S -->
<div class="well container-fluid">
	<div class="col-sm-8">
		<form id="jaiko-inven-frm" action="/jaiko/prdInven/up" method="post" enctype="multipart/form-data" onsubmit="return preCheck('jaikoInven');" >
			<div id="jaiko-inven-csv" class="file-up col-sm-8">
			<input type="file" id="jaiko-inven-upload" name="upFile" multiple="multiple" style="display:none">
			<button class="original_btn" style="width: 150px;">商品情報ファイル</button>
			<span class="icon">未選択</span>
			</div>
			<div class="col-sm-4">
			<input type="submit" class="original_btn" value="アップロード">
			</div>
		</form>
	</div>
	<div class="col-sm-4">
	<button type="button" id="btn_inven_down" class="btn btn-info" style="width: 120px;">ダウンロード</button>
	<form id="invenFrm" action="/jaiko/prdInven/down" method="post"></form>
	</div>
</div>
<!-- file upload E -->

<!-- table S -->
<div class="well container-fluid">
<div class="row">
<div class="col-sm-9">
<button id="btn_add" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">列追加</button>
<button id="btn_commit" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">登録</button>
<button id="btn_delete" type="button" class="btn btn-danger" style="width: 120px; margin-bottom: 10px;">削除</button>
</div>
<div class="col-sm-3">
<button id="btn_down" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">棚卸表DL</button>
<form id="frm_down" action="/jaiko/prdInven/csvDown" method="post"></form>
</div>
</div>

<div id="prdInvenGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
<!-- table E -->
</div>
<script src="<%=svrInfo %>/resources/jaiko/prdInventory.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#prdInven").addClass("active");
	
	/* File Upload*/
	$('#jaiko-inven-csv').on('click', 'button', function () {
        $('#jaiko-inven-upload').click();
        return false;
    });
	
	$('#jaiko-inven-upload').on('change', function() {
	    //選択したファイル情報を取得し変数に格納
	    var file = $(this).prop('files')[0];
	    //アイコンを選択中に変更
	    $('#jaiko-inven-csv').find('.icon').addClass('select').html('選択中');
	    //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
	    if(!($('.jaiko-inven-filename').length)){
	        $('#jaiko-inven-csv').append('<div class="jaiko-inven-filename"></div>');
	    };
	    //ファイル名を表示
	    $('.jaiko-inven-filename').html('ファイル名：' + file.name);
	});
	/* //File Upload*/
});
</script>
</body>
</html>