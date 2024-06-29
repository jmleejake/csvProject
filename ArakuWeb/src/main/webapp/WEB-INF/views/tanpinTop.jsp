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
				<li id="prdM" class="nav-item">
				<a class="nav-link" href="prdMng">単品商品管理</a>
				</li>
			</ul>
			<ul class="nav navbar-nav">
				<li id="allprice" class="nav-item">
				<a class="nav-link" href="allMng">ALLMART価格管理</a>
				</li>
			</ul>
			<ul class="nav navbar-nav">
				<li id="expire" class="nav-item">
				<a class="nav-link" href="expMng">賞味期限管理</a>
				</li>
			</ul>
			<ul class="nav navbar-nav">
				<li id="prdA" class="nav-item">
				<a class="nav-link" href="listView">商品分析</a>
				</li>
			</ul>
			<ul class="nav navbar-nav">
				<li id="order" class="nav-item">
				<a class="nav-link" href="orderView">発注書</a>
				</li>
			</ul>
			<ul class="nav navbar-nav">
				<li id="estimate" class="nav-item">
				<a class="nav-link" href="estimate">見積書</a>
				</li>
			</ul>
		</div>
	</div>
</nav>
