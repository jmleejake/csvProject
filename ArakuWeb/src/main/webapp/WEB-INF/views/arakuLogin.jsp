<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String svrInfo = "http://"+request.getServerName()+":"+request.getLocalPort();
%>
<!DOCTYPE html>
<html>
<head>
<title>アラクシステム</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="<%=svrInfo %>/resources/bootstrap-notify/bootstrap-notify.min.js"></script>
<script src="<%=svrInfo %>/resources/common.js"></script>
<link rel="stylesheet" href="<%=svrInfo %>/resources/common.css">
</head>
<body>
<nav class="navbar navbar-default">
	<div class="container-fluid">
		<div class="navbar-header">
			<!-- Logo -->
			<a class="navbar-brand" href="/araku">
			<img src="<%=svrInfo %>/resources/img/kana-a.png" alt="Brand" style="width:60px; margin-top:-14px;">
			</a>
		</div>
	</div>
</nav>
<div class="container-fluid">
<div class="well container-fluid">
<div class="col-sm-3"></div>
<div class="col-sm-6">
<form id="frm_login" action="/araku/login" method="post">
	<div class="input-group">
		<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
		<input type="text" class="form-control" id="user_id" name="user_id" placeholder="ID">
	</div>
	<div class="input-group">
		<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
		<input type="password" class="form-control" id="user_pass" name="user_pass" placeholder="パスワード"
		onkeypress = "if(event.keyCode==13){ loginGo(); event.preventDefault(); }">
	</div>
</form>
<div>
<div class="col-sm-6">
<button id="btn_login" type="button" class="btn btn-success" style="width:100%;">確認</button>
</div>
<div class="col-sm-6">
<button id="btn_cancel" type="button" class="btn btn-danger" style="width:100%;">閉じる</button>
</div>
</div>
</div>
<div class="col-sm-3"></div>
</div>
</div>
<script src="<%=svrInfo %>/resources/araku/login.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	showNotify(${type});
});
</script>
</body>
</html>