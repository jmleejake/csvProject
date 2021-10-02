<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<c:if test="${'Y' == type }">
<jsp:include page="../yahooTop.jsp"></jsp:include>
</c:if>
<div class="container-fluid">
<div class="well container-fluid">
	<form action="getPrdTrans" id="transForm">
	<input type="hidden" name="search_type" value="screen">
	<div class="col-sm-6">
	<label>商品名</label>
	<input type="text" name="keyword" class="form-control" style="width: 400px;" onkeypress = "if(event.keyCode==13){ search(); event.preventDefault(); }">
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_srch" type="button" class="btn btn-default" style="width: 120px; margin-top: 18px;">検索</button>
	</div>
	</form>
</div>
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
	<input type="hidden" id="transTarget" value="${type}">
	<button id="btn_create" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">列追加</button>
	<button id="btn_commit" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button>
	<button id="btn_sum" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">総商品数</button>
	<button id="btn_sum_jan" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">JAN商品数</button>
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_delete" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
	</div>
</div>
<div id="translateGrid" style="width:auto; height: 350px;" class="ag-theme-balham"></div>
</div>

<!-- 総商品数 -->
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
		<form id="frm" action="sumDown" method="post"></form>
		<button id="btn_down" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">ダウンロード</button>
<!-- 	<button id="btn_create" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">列追加</button> -->
<!-- 	<button id="btn_commit" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">登録</button> -->
<!-- 	<button id="btn_sum" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">総商品数</button> -->
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_order_sum_del" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
	</div>
</div>
<div id="orderSumGrid" style="width:auto; height: 350px;" class="ag-theme-balham"></div>
</div>

<!-- JAN商品数 -->
<div class="well container-fluid">
<div class="row">
	<div class="col-sm-6" style="margin-top: 7px;">
		<form id="frm" action="sumDown" method="post"></form>
		<button id="btn_jan_down" type="button" class="btn btn-success" style="width: 120px; margin-bottom: 10px;">ダウンロード</button>
		<button id="btn_hanei" type="button" class="btn btn-primary" style="width: 120px; margin-bottom: 10px;">反映</button>
	</div>
	<div class="col-sm-6" style="margin-top: 7px;">
	<button id="btn_order_jan_sum_del" type="button" class="btn btn-default" style="width: 120px; margin-bottom: 10px;">削除</button>
	</div>
</div>
<div id="orderJanSumGrid" style="width:auto; height: 350px;" class="ag-theme-balham"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/menu/prdTrans.js"></script>
</body>
<script>
$(document).ready(function() {
	$("#prdTrans").addClass("active");
});
</script>
</html>