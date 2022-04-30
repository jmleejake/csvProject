<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<title>在庫管理システム</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="cdnList.jsp"></jsp:include>

<link rel="stylesheet" href="<%=svrInfo %>/resources/fileView.css">
<script src="<%=svrInfo %>/resources/fileView.js"></script>

<script src="<%=svrInfo %>/resources/common.js"></script>
<link rel="stylesheet" href="<%=svrInfo %>/resources/common.css">

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
			<a class="navbar-brand" href="/jaiko">
			<img src="<%=svrInfo %>/resources/img/jaiko.png" alt="Brand" style="width:60px; margin-top:-14px;">
			</a>
		</div>
	
		<div class="navbar-collapse collapse" id="navbarColor03">
			<ul class="nav navbar-nav">
				<li id="fileView" class="nav-item">
				<a class="nav-link" href="/jaiko/fileView">情報取り込み</a>
				</li>
				<li id="prdInfo" class="nav-item">
				<a class="nav-link" href="/jaiko/prdInfo">商品情報</a>
				</li>
				<li id="order" class="nav-item">
				<a class="nav-link" href="/jaiko/order">注文状況</a>
				</li>
				<li id="sales" class="nav-item">
				<a class="nav-link" href="/jaiko/sales/main">売上一覧</a>
				</li>
				<li id="prdInven" class="nav-item">
				<a class="nav-link" href="/jaiko/prdInven">商品在庫</a>
				</li>
				<li id="prdWareIn" class="nav-item">
				<a class="nav-link" href="/jaiko/prdWareIn">入庫</a>
				</li>
				<li id="prdWareOut" class="nav-item">
				<a class="nav-link" href="/jaiko/prdWareOut">出庫</a>
				</li>
			</ul>
		</div>
	</div>
</nav>
