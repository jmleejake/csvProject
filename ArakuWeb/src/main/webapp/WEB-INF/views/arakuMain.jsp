<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page session="false" %>
<%@ taglib prefix ="c" uri ="http://java.sun.com/jsp/jstl/core" %>
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
	<button type="button" class="btn btn-default btn-sm" onclick="location.href='/araku/logout';">
      <span class="glyphicon glyphicon-log-out"></span>
    </button>
	</div>
	<c:forEach var="item" items="${authList }">
	<c:if test="${item.menu_id eq 'rak' }">
	<button id="btn_rakuten" class="btn btn-lg" style="width: 150px;">RAKUTEN</button>
	</c:if>
	<c:if test="${item.menu_id eq 'ama' }">
	<button id="btn_amazon" class="btn btn-lg" style="width: 150px;">AMAZON</button>
	</c:if>
	<c:if test="${item.menu_id eq 'q10' }">
	<button id="btn_q10" class="btn btn-lg" style="width: 150px;">QOO10</button>
	</c:if>
	<c:if test="${item.menu_id eq 'yah' }">
	<button id="btn_yahoo" class="btn btn-lg" style="width: 150px;">YAHOO</button>
	</c:if>
	</c:forEach>
	</div>
	
	<div class="well container-fluid">
	<c:forEach var="item" items="${authList }">
	<c:if test="${item.menu_id eq 'tab' }">
	<button id="btn_tablet" class="btn btn-lg" style="width: 150px;">タブレット</button>
	</c:if>
	<c:if test="${item.menu_id eq 'can' }">
	<button id="btn_cancel" class="btn btn-lg" style="width: 150px;">キャンセル管理</button>
	</c:if>
	</c:forEach>
	</div>
	
	<c:forEach var="item" items="${authList }">
	<c:if test="${item.menu_id eq 'tan' }">
	<div class="well container-fluid">
	<button id="btn_tanpin" class="btn btn-lg" style="width: 150px;">単品商品管理</button>
	<button id="btn_prdAnal" class="btn btn-lg" style="width: 150px;">商品分析</button>
	<button id="btn_order" class="btn btn-lg" style="width: 150px;">発注書</button>
	<button id="btn_estimate" class="btn btn-lg" style="width: 150px;">見積書</button>
	</div>
	</c:if>
	</c:forEach>
	
	<c:forEach var="item" items="${authList }">
	<c:if test="${item.menu_id eq 'adm' }">
	<div class="well container-fluid">
	<button id="btn_admin" class="btn btn-lg" style="width: 150px;">ADMIN</button>
	</div>
	</c:if>
	</c:forEach>
	
</div>
<script src="<%=svrInfo %>/resources/main.js"></script>
</body>
</html>