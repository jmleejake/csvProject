<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<title>アラクシステム</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="./resources/bootstrap-3.3.7/bootstrap.min.css">
<!-- <link rel="stylesheet" href="./resources/bootswatch-theme/bootstrap.min.css"> -->
<script src="./resources/bootstrap-3.3.7/bootstrap.min.js"></script>

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
			<img src="./resources/img/kana-a.png" alt="Brand" style="width:45px; margin-top:-11px;">
			</a>
		</div>
	
		<div class="navbar-collapse collapse" id="navbarColor03">
			<ul class="nav navbar-nav">
				<li id="home" class="nav-item">
				<a class="nav-link" href="fileView">情報取り込み</a>
				</li>
				<li id="trans" class="nav-item">
				<a class="nav-link" href="translationView">商品名置換</a>
				</li>
				<li id="order" class="nav-item">
				<a class="nav-link" href="orderView">注文情報</a>
				</li>
				<li id="claimNo" class="nav-item">
				<a class="nav-link" href="claimNoView">荷物伝票番号更新</a>
				</li>
				<li id="rFile" class="nav-item">
				<a class="nav-link" href="rFileDownView">楽天更新ファイル</a>
				</li>
				<li id="yuUp" class="nav-item">
				<a class="nav-link" href="">ゆプリある</a>
				</li>
			</ul>
		</div>
	</div>
</nav>
