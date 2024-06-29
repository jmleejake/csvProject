<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String svrInfo = "http://" + request.getServerName() + ":" + request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Allmart Price Management</title>
    <link rel="stylesheet" type="text/css" href="<%=svrInfo %>/resources/css/style.css">
    <script src="<%=svrInfo %>/resources/js/jquery.min.js"></script>
    <script src="<%=svrInfo %>/resources/js/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="../tanpinTop.jsp"></jsp:include>
<div class="container-fluid">
    <div class="well container-fluid">
        <div class="row">
            <form id="frm">
            	<div class="row">
					<div class="col-sm-4">
					<label>登録日付</label>
					<div class="row">
					<!-- datepicker -->
					<div class="col-sm-6">
					<input type="text" name="start_date" class="form-control" style="width: 200px;" id="start_date" placeholder="From">
					</div>
					<div class="col-sm-6">
					<input type="text" name="end_date" class="form-control" style="width: 200px;" id="end_date" placeholder="To">
					</div>
					</div>
					</div>
					<div class="col-sm-4">
					</div>
					<div class="col-sm-4">
					</div>
				</div>
                <div class="col-sm-4">
                    <input type="text" name="prd_nm" class="form-control" style="width: 250px;" placeholder="商品名"
                           onkeypress="if(event.keyCode==13){ srch(); event.preventDefault(); }">
                    <input type="hidden" name="search_type" value="srch">
                </div>
                <div class="col-sm-4">
                    <input type="text" name="jan_cd" class="form-control" style="width: 250px;" placeholder="商品コード(JAN)"
                           onkeypress="if(event.keyCode==13){ srch(); event.preventDefault(); }">
                </div>
            </form>
            <div class="col-sm-4">
                <button type="button" id="btn_srch" class="btn btn-default" style="width: 120px;">検索</button>
            </div>
        </div>
    </div>
    <div class="well container-fluid">
        <div class="col-sm-8">
            <form id="prdAllmart-frm" action="prdAllFileUp" method="post" enctype="multipart/form-data" onsubmit="return preCheck('prdAllmart-frm');">
                <div id="prdAllmart-csv" class="file-up col-sm-8">
                    <input type="file" id="prdAllmart-upload" name="upload" multiple="multiple" style="display:none">
                    <button type="button" class="original_btn" style="width: 150px;">Allmart価格ファイル</button>
                    <span class="icon">未選択</span>
                </div>
                <div class="col-sm-4">
                    <input type="submit" class="original_btn" value="アップロード">
                </div>
            </form>
        </div>
        <div class="col-sm-4">
            <button type="button" id="btn_down" class="btn btn-info" style="width: 120px;">ダウンロード</button>
            <button type="button" id="btn_commit" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">修正</button>
            <button type="button" id="btn_del" class="btn btn-danger" style="width: 120px; margin-bottom: 10px;">削除</button>
        </div>
    </div>
    <div class="well container-fluid">
        <div id="allMGrid" style="width:auto; height: 600px;" class="ag-theme-balham"></div>
    </div>
</div>
<script src="<%=svrInfo %>/resources/tanpin/martManage.js"></script>

<form id="frm_down" action="downAllmart" method="post">
    <input type="hidden" id="product_id_list" name="id_lst">
</form>
</body>
<script>
$(document).ready(function() {
    $("#allprice").addClass("active");

	$("#start_date").datepicker({
		language: "ja",
		autoclose: true
	});
	
	$("#end_date").datepicker({
		language: "ja",
		autoclose: true
	});
	
    /* File Upload */
    $('#prdAllmart-csv').on('click', 'button', function () {
        $('#prdAllmart-upload').click();
        return false;
    });

    $('#prdAllmart-upload').on('change', function() {
        var file = $(this).prop('files')[0];
        $('#prdAllmart-csv').find('.icon').addClass('select').html('選択中');
        if (!$('.prdAllmart-filename').length) {
            $('#prdAllmart-csv').append('<div class="prdAllmart-filename"></div>');
        }
        $('.prdAllmart-filename').html('ファイル名：' + file.name);
    });
    /* //File Upload */
});
</script>
</html>
