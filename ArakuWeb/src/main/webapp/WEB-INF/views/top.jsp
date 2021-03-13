<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<title>アラクシステム</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

<script src="<%=svrInfo %>/resources/bootstrap-notify/bootstrap-notify.min.js"></script>

<link rel="stylesheet" href="<%=svrInfo %>/resources/alertfy-js-0.3.11/alertify.core.css">
<link rel="stylesheet" href="<%=svrInfo %>/resources/alertfy-js-0.3.11/alertify.default.css">
<script src="<%=svrInfo %>/resources/alertfy-js-0.3.11/alertify.min.js"></script>

<link rel="stylesheet" href="<%=svrInfo %>/resources/ag-grid/ag-grid.css">
<link rel="stylesheet" href="<%=svrInfo %>/resources/ag-grid/ag-theme-balham.css">
<script src="<%=svrInfo %>/resources/ag-grid/ag-grid.min.noStyle.js"></script>

<link rel="stylesheet" href="<%=svrInfo %>/resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.min.css">
<link rel="stylesheet" href="<%=svrInfo %>/resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker3.min.css">
<script src="<%=svrInfo %>/resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.min.js"></script>
<script src="<%=svrInfo %>/resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.ja.min.js"></script>

<link rel="stylesheet" href="<%=svrInfo %>/resources/fileView.css">
<script src="<%=svrInfo %>/resources/fileView.js"></script>

<script src="<%=svrInfo %>/resources/common.js"></script>
<link rel="stylesheet" href="<%=svrInfo %>/resources/common.css">

<script>
$(document).ready(function() {
	$(".loader").fadeOut("slow");
});
</script>

<!-- for loading image -->
<div class="loader"></div>
<div id="loadingImg"></div>

<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbarColor03" aria-expanded="false">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<!-- Logo -->
			<a class="navbar-brand" href="/araku">
			<img src="<%=svrInfo %>/resources/img/kana-a.png" alt="Brand" style="width:45px; margin-top:-11px;">
			</a>
		</div>
	
		<div class="navbar-collapse collapse" id="navbarColor03">
			<ul class="nav navbar-nav">
				<li id="home" class="nav-item">
				<a class="nav-link" href="fileView">情報取り込み</a>
				</li>
				<li id="order" class="nav-item">
				<a class="nav-link" href="orderView">注文情報</a>
				</li>
				<li id="items" class="nav-item">
				<a class="nav-link" href="itemsView">一括編集情報</a>
				</li>
				<li id="trans" class="nav-item">
				<a class="nav-link" href="translationView">商品名置換</a>
				</li>
				<li id="prdTrans" class="nav-item">
				<a class="nav-link" href="prdTransView">商品中間マスタ</a>
				</li>
				<li id="region" class="nav-item">
				<a class="nav-link" href="regionView">マスタ管理</a>
				</li>
				<li id="rFile" class="nav-item">
				<a class="nav-link" href="rFileDownView">楽天更新ファイル</a>
				</li>
				<li id="tabChumon" class="nav-item">
				<a class="nav-link" href="tabChumonView">タブレット注文画面</a>
				</li>
				<li id="sales" class="nav-item">
				<a class="nav-link" href="salesView">売上計算</a>
				</li>
				<!--
				<li id="yuUp" class="nav-item">
				<a class="nav-link" href="yuView">ゆプリある</a>
				</li>
				 -->
			</ul>
		</div>
	</div>
</nav>
