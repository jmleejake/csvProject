<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
<title>アラクシステム</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" href="./resources/bootstrap-3.3.7/bootstrap.min.css">

<script src="./resources/bootstrap-3.3.7/jquery.min.js"></script>
<script src="./resources/bootstrap-3.3.7/bootstrap.min.js"></script>
<script src="./resources/bootstrap-notify/bootstrap-notify.min.js"></script>
</head>
<body>
<nav class="navbar navbar-default">
<!-- Logo -->
<a class="navbar-brand" href="">
<img src="./resources/img/kana-a.png" alt="logo" style="width:50px; margin-top:-15px;">
</a>

<!-- Nav Pills -->
<ul class="nav nav-pills" role="tablist">
<li class="nav-item active">
<a class="nav-link" data-toggle="pill" href="#home">情報取り込み</a>
</li>
<li class="nav-item">
<a class="nav-link" data-toggle="pill" href="#menu1">商品名置換</a>
</li>
<li class="nav-item">
<a class="nav-link" data-toggle="pill" href="#menu2">注文情報</a>
</li>
<li class="nav-item">
<a class="nav-link" data-toggle="pill" href="#menu3">荷物伝票番号更新</a>
</li>
<li class="nav-item">
<a class="nav-link" data-toggle="pill" href="#menu4">楽天更新ファイル</a>
</li>
</ul>
</nav><!-- Nav Bar -->

<div class="container-fluid">
<div class="tab-content">
<!-- 情報取り込み -->
<div id="home" class="tab-pane active">
<jsp:include page="menu/fileView.jsp" />
</div>
<!-- 商品名置換 -->
<div id="menu1" class="tab-pane">
<jsp:include page="menu/translation.jsp" />
</div>
<!-- 注文情報 -->
<div id="menu2" class="tab-pane">
<jsp:include page="menu/orderInfo.jsp" />
</div>
<!-- 荷物伝票番号更新 -->
<div id="menu3" class="tab-pane">
<jsp:include page="menu/claimNoUpdate.jsp" />
</div>
<!-- 楽天更新ファイル -->
<div id="menu4" class="tab-pane">
<jsp:include page="menu/rakutenFileDown.jsp" />
</div>
<!-- ゆプリある -->
</div>
</div><!-- container fluid -->
</body>
</html>