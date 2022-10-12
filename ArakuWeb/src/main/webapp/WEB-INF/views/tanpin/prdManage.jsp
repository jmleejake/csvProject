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
	<div class="col-sm-3" >
	<div class="dropdown">
		<input type="text" name="maker_cd" class="form-control" placeholder="メーカー名" list ="maker">
		<datalist id="maker" class="dropdown-menu" role="menu">
			<c:forEach items="${makers }" var="maker">
			<option value="${maker.maker_cd }">${maker.maker_nm }</option>
			</c:forEach>
		</datalist>
	</div>
		
	<!-- 
	<div class="dropdown">
		<button class="btn btn-primary dropdown-toggle" id="menu1" type="button" data-toggle="dropdown">
		メーカー名<span id="selectedMaker"></span>  <span class="caret"></span></button>
		<ul class="dropdown-menu" role="menu" aria-labelledby="menu1">
			<c:forEach items="${makers }" var="maker">
			<li role="presentation"><a role="menuitem" href="javascript:setSearch('M', '${maker.maker_cd }', '${maker.maker_nm }')">${maker.maker_nm }</a></li>
			</c:forEach>
		</ul>
	</div>
	 -->
	</div>
	<div class="col-sm-3" >
	<input type="text" name="prd_nm" class="form-control" style="width: 250px;" placeholder="商品名"
	onkeypress = "if(event.keyCode==13){ srch(); event.preventDefault(); }">
	<input type="hidden" name="dealer_id" id="hidDealerId">
	<input type="hidden" name="search_type" value="srch"> 
	</div>
	<div class="col-sm-2">
	<input type="text" name="prd_cd" class="form-control" style="width: 250px;" placeholder="商品コード(JAN)"
	onkeypress = "if(event.keyCode==13){ srch(); event.preventDefault(); }">
	</div>
	</form>
	<div class="col-sm-2" >
	<div class="dropdown">
		<button class="btn btn-primary dropdown-toggle" id="menu1" type="button" data-toggle="dropdown">
		取引先<span id="selectedDealer"></span>  <span class="caret"></span></button>
		<ul class="dropdown-menu" role="menu" aria-labelledby="menu1">
			<c:forEach items="${dealers }" var="dealer">
			<li role="presentation"><a role="menuitem" href="javascript:setSearch('D', '${dealer.dealer_id }', '${dealer.dealer_nm }')">${dealer.dealer_nm }</a></li>
			</c:forEach>
		</ul>
	</div>
	</div>
	<div class="col-sm-2">
	<button type="button" id="btn_srch" class="btn btn-default" style="width: 120px;">検索</button>
	</div>
</div>
<div class="well container-fluid">
	<div class="col-sm-8">
	<form id="tanpin-frm" action="fileUpload" method="post" enctype="multipart/form-data" onsubmit="return preCheck('tanpin');" >
		<div id="tanpin-csv" class="file-up col-sm-8">
		<input type="file" id="tanpin-upload" name="upload" multiple="multiple" style="display:none">
		<button class="original_btn" style="width: 150px;">単品管理ファイル</button>
		<span class="icon">未選択</span>
		</div>
		<div class="col-sm-4">
		<input type="submit" class="original_btn" value="アップロード">
		</div>
	</form>
	</div>
	<div class="col-sm-4">
	<button type="button" id="btn_down" class="btn btn-info" style="width: 120px;">ダウンロード</button>
	<button type="button" id="btn_del" class="btn btn-danger" style="width: 120px;">削除</button>
	</div>
</div>
<div class="well container-fluid">
<div class="row">
	<button id="btn_create" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">列追加</button>
	<button id="btn_commit" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">登録</button>
	<button id="btn_delete" type="button" class="btn btn-danger" style="width: 120px; margin-bottom: 10px;">削除</button>
</div>
<div id="prdMGrid" style="width:auto; height: 600px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/tanpin/prdManage.js"></script>

<form id="frm_down" action="down" method="post">
<input type="hidden" id="seq_id_list" name="id_lst">
</form>
</body>
<script>
$(document).ready(function() {
	$("#prdM").addClass("active");
	
	/* File Upload*/
	$('#tanpin-csv').on('click', 'button', function () {
        $('#tanpin-upload').click();
        return false;
    });
	
	$('#tanpin-upload').on('change', function() {
	    //選択したファイル情報を取得し変数に格納
	    var file = $(this).prop('files')[0];
	    //アイコンを選択中に変更
	    $('#tanpin-csv').find('.icon').addClass('select').html('選択中');
	    //未選択→選択の場合（.filenameが存在しない場合）はファイル名表示用の<div>タグを追加
	    if(!($('.tanpin-filename').length)){
	        $('#tanpin-csv').append('<div class="tanpin-filename"></div>');
	    };
	    //ファイル名を表示
	    $('.tanpin-filename').html('ファイル名：' + file.name);
	});
	/* //File Upload*/
});
</script>
</html>