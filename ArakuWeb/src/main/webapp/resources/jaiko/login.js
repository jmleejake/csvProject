$("#btn_login").on("click", function() {
	$("#frm_login").submit();
});

$("#btn_cancel").on("click", function() {
	window.close();
});

function showNotify(type) {
	if(type == 1) {
		pleaseSelectNotify("IDを確認してください。");
		$("#user_id").parent("div").addClass("has-error has-feedback");
		$("#user_id").val("");
		$("#user_id").focus();
	}else if(type == 2) {
		pleaseSelectNotify("パスワードを確認してください。");
		$("#user_pass").parent("div").addClass("has-error has-feedback");
		$("#user_pass").val("");
		$("#user_pass").focus();
	}
}