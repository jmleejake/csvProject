<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page session="false" %>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
<title>アラクシステム</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<jsp:include page="cdnList.jsp"></jsp:include>
</head>
<body>
<div class="container-fluid">
	<div class="well container-fluid">
	<div>
	<img src="<%=svrInfo %>/resources/img/kana-a.png" alt="Brand" style="width:50px;">
	</div>
	<button id="btn_rakuten" class="btn btn-lg" style="width: 150px;">RAKUTEN</button>
	<button id="btn_amazon" class="btn btn-lg" style="width: 150px;">AMAZON</button>
	<button id="btn_q10" class="btn btn-lg" style="width: 150px;">QOO10</button>
	<button id="btn_yahoo" class="btn btn-lg" style="width: 150px;">YAHOO</button>
	</div>
	
	<div class="well container-fluid">
	<button id="btn_tablet" class="btn btn-lg" style="width: 150px;">タブレット</button>
	</div>
	
	<div class="well container-fluid">
	<button id="btn_tanpin" class="btn btn-lg" style="width: 150px;">単品商品管理</button>
	<button id="btn_prdAnal" class="btn btn-lg" style="width: 150px;">商品分析</button>
	<button id="btn_order" class="btn btn-lg" style="width: 150px;">発注書</button>
	<button id="btn_estimate" class="btn btn-lg" style="width: 150px;">見積書</button>
	</div>
</div>
<script src="<%=svrInfo %>/resources/main.js"></script>
</body>
</html>