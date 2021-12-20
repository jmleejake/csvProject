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

<link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-grid.css">
<link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-balham.css">
<script src="https://unpkg.com/ag-grid-community/dist/ag-grid-community.min.noStyle.js"></script>

<link rel="stylesheet" href="<%=svrInfo %>/resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.min.css">
<link rel="stylesheet" href="<%=svrInfo %>/resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker3.min.css">
<script src="<%=svrInfo %>/resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.min.js"></script>
<script src="<%=svrInfo %>/resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.ja.min.js"></script>

<link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.css" rel="stylesheet">
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment-with-locales.min.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>

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
