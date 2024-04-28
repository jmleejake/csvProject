<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix ="c" uri ="http://java.sun.com/jsp/jstl/core" %>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<jsp:include page="../adminTop.jsp"></jsp:include>
<div class="well container-fluid">
	<div class="col-sm-6" >
	</div>
	<div class="col-sm-6" >
	</div>
</div>
<div class="well container-fluid">

<table class="table table-bordered">
<thead>
<tr>
	<th>ユーザーID</th>
	<th>ユーザー名</th>
	<th>RAKUTEN</th>
	<th>AMAZON</th>
	<th>QOO10</th>
	<th>YAHOO</th>
	<th>タブレット</th>
	<th>キャンセル管理</th>
	<th>単品商品管理</th>
	<th>ADMIN</th>
	<th>最近ローグイン日付</th>
</tr>
</thead>
<tbody>
<c:forEach var="user" items="${authList }" varStatus="idx">
<tr>
	<td>${user.user_id }</td>
	<td>${user.user_name }</td>
	<td>
	<div>
	<input type="radio" name="rak${idx.count }" <c:if test='${user.rak eq "Y" }'>checked</c:if> onclick="userUseYn(this);">活性
	<input type="radio" name="rak${idx.count }" <c:if test='${user.rak eq "N" }'>checked</c:if> onclick="userUseYn(this);">非活性
	</div>
	</td>
	<td>
	<div>
	<input type="radio" name="ama${idx.count }" <c:if test='${user.ama eq "Y" }'>checked</c:if> onclick="userUseYn(this);">活性
	<input type="radio" name="ama${idx.count }" <c:if test='${user.ama eq "N" }'>checked</c:if> onclick="userUseYn(this);">非活性
	</div>
	</td>
	<td>
	<div>
	<input type="radio" name="q10${idx.count }" <c:if test='${user.q10 eq "Y" }'>checked</c:if> onclick="userUseYn(this);">活性
	<input type="radio" name="q10${idx.count }" <c:if test='${user.q10 eq "N" }'>checked</c:if> onclick="userUseYn(this);">非活性
	</div>
	</td>
	<td>
	<div>
	<input type="radio" name="yah${idx.count }" <c:if test='${user.yah eq "Y" }'>checked</c:if> onclick="userUseYn(this);">活性
	<input type="radio" name="yah${idx.count }" <c:if test='${user.yah eq "N" }'>checked</c:if> onclick="userUseYn(this);">非活性
	</div>
	</td>
	<td>
	<div>
	<input type="radio" name="tab${idx.count }" <c:if test='${user.tab eq "Y" }'>checked</c:if> onclick="userUseYn(this);">活性
	<input type="radio" name="tab${idx.count }" <c:if test='${user.tab eq "N" }'>checked</c:if> onclick="userUseYn(this);">非活性
	</div>
	</td>
	<td>
	<div>
	<input type="radio" name="can${idx.count }" <c:if test='${user.can eq "Y" }'>checked</c:if> onclick="userUseYn(this);">活性
	<input type="radio" name="can${idx.count }" <c:if test='${user.can eq "N" }'>checked</c:if> onclick="userUseYn(this);">非活性
	</div>
	</td>
	<td>
	<div>
	<input type="radio" name="tan${idx.count }" <c:if test='${user.tan eq "Y" }'>checked</c:if> onclick="userUseYn(this);">活性
	<input type="radio" name="tan${idx.count }" <c:if test='${user.tan eq "N" }'>checked</c:if> onclick="userUseYn(this);">非活性
	</div>
	</td>
	<td>
	<div>
	<input type="radio" name="adm${idx.count }" <c:if test='${user.adm eq "Y" }'>checked</c:if> onclick="userUseYn(this);">活性
	<input type="radio" name="adm${idx.count }" <c:if test='${user.adm eq "N" }'>checked</c:if> onclick="userUseYn(this);">非活性
	</div>
	</td>
	<td>${user.last_login_dt }</td>
	
</tr>
</c:forEach>

</tbody>
</table>

</div>

<script src="<%=svrInfo %>/resources/jaiko/order2.js"></script>
</body>
<script>
$(document).ready(function() {
	$("#order").addClass("active");
});

function userUseYn(obj) {
	let userId = $(obj).parents('tr').children()[0];
	$.ajax({
		url: "/araku/user/modify-exec"
		, type:"post"
		, data:{
			user_id: $(userId).text()
			, menu_id: $(obj).attr('name').substring(0,3)
		}
		, success: function(result){
			console.log(result);
			if('SUCC' == result) {
				successNotify('修正完了。');
				return;
			}
    	}
	});
	
}
</script>
</html>