<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<title>在庫管理システム</title>
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
				<li id="prdInfo" class="nav-item">
				<a class="nav-link" href="/jaiko/prdInfo">商品情報</a>
				</li>
			</ul>
			<ul class="nav navbar-nav">
				<li id="prdInventory" class="nav-item">
				<a class="nav-link" href="/jaiko/prdInventory">商品在庫</a>
				</li>
			</ul>
		</div>
	</div>
</nav>
