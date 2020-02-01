<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<title>アラクシステム</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="./../resources/bootstrap-3.3.7/bootstrap.min.css">
<!-- <link rel="stylesheet" href="./../resources/bootswatch-theme/bootstrap.min.css"> -->
<script src="./../resources/bootstrap-3.3.7/bootstrap.min.js"></script>
<script src="./../resources/bootstrap-notify/bootstrap-notify.min.js"></script>

<link rel="stylesheet" href="./../resources/alertfy-js-0.3.11/alertify.core.css">
<link rel="stylesheet" href="./../resources/alertfy-js-0.3.11/alertify.default.css">
<script src="./../resources/alertfy-js-0.3.11/alertify.min.js"></script>

<link rel="stylesheet" href="./../resources/ag-grid/ag-grid.css">
<link rel="stylesheet" href="./../resources/ag-grid/ag-theme-balham.css">
<script src="./../resources/ag-grid/ag-grid.min.noStyle.js"></script>

<link rel="stylesheet" href="./../resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.min.css">
<link rel="stylesheet" href="./../resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker3.min.css">
<script src="./../resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.min.js"></script>
<script src="./../resources/bootstrap-datepicker-1.6.4/bootstrap-datepicker.ja.min.js"></script>

<script src="./../resources/common.js"></script>
<link rel="stylesheet" href="./../resources/common.css">

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
			<a class="navbar-brand" href="fileView">
			<img src="./../resources/img/kana-a.png" alt="Brand" style="width:45px; margin-top:-11px;">
			</a>
		</div>
	
		<div class="navbar-collapse collapse" id="navbarColor03">
			<ul class="nav navbar-nav">
				<li id="prdM" class="nav-item">
				<a class="nav-link" href="prdMng">単品商品管理</a>
				</li>
			</ul>
		</div>
	</div>
</nav>
