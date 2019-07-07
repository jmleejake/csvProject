<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="./../resources/menu/regionMaster.css">
<script src="./../resources/jquery/jquery.min.js"></script>
<script>
$(document).ready(function() {
	$("#region").addClass("active");
	showRegionMaster();
	showExceptionMaster();
});
</script>
</head>
<body>
<c:if test="${'R' == type }">
<jsp:include page="../top.jsp"></jsp:include>
</c:if>
<c:if test="${'A' == type }">
<jsp:include page="../amazonTop.jsp"></jsp:include>
</c:if>
<c:if test="${'Q' == type }">
<jsp:include page="../q10Top.jsp"></jsp:include>
</c:if>
<div class="container-fluid">
<div class="well container-fluid">
<h3>地域区分マスタ</h3>

<div class="well container-fluid">
	<form id="regionFrm" action="showRegionMaster">
	<div class="col-sm-6">
	<label>地域名称</label>
	<input type="text" name="keyword" class="form-control" style="width: 300px;">
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_srch" type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_commit" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	</div>
</div>

<div id="regionGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./../resources/menu/regionMaster.js"></script>
</div>

<div class="container-fluid">
<div class="well container-fluid">
<h3>例外データマスタ</h3>

<div class="well container-fluid">
	<form id="exceptionFrm" action="showExceptionMaster">
	<div class="col-sm-6">
	<label>例外データ</label>
	<input type="text" name="keyword" class="form-control" style="width: 300px;">
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_srch2" type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_add" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">列追加</button>
	<button id="btn_commit2" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_delete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
	</div>
</div>

<div id="exceptionGrid" style="width:auto; height: 500px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="./../resources/menu/exceptionMaster.js"></script>
</div>
</body>
</html>