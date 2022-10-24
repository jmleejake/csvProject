<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<title>アラクシステム</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="cdnList.jsp"></jsp:include>

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
				<li id="trans" class="nav-item">
				<a class="nav-link" href="translationView">商品名置換</a>
				</li>
				<li id="prdTrans" class="nav-item">
				<a class="nav-link" href="prdTransView">商品中間マスタ</a>
				</li>
				<li id="kwrd" class="nav-item">
				<a class="nav-link" href="kwrdView">在庫管理(Keyword別)</a>
				</li>
				<li id="region" class="nav-item">
				<a class="nav-link" href="regionView">マスタ管理</a>
				</li>
				<li id="aFile" class="nav-item">
				<a class="nav-link" href="aFileDownView">Amazon更新ファイル</a>
				</li>
				<li id="tabChumon" class="nav-item">
				<a class="nav-link" href="tabChumonView">タブレット注文画面</a>
				</li>
				<li id="sales" class="nav-item">
				<a class="nav-link" href="salesView">売上計算</a>
				</li>
			</ul>
		</div>
	</div>
</nav>
