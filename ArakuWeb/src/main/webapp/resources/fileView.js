/**
 * 
 */

function preCheck(type) {
//	var hasFile = $("#js-upload")[0].files.length;
	var hasFile;
	console.log(type);
	if ("rak" == type) {
		hasFile = $("#rak-upload")[0].files.length;
	}
	if ("ama" == type) {
		hasFile = $("#ama-upload")[0].files.length;
	}
	if ("yu" == type) {
		hasFile = $("#yu-upload")[0].files.length;
	}
	if ("del" == type) {
		hasFile = $("#del-upload")[0].files.length;
	}
	if ("items" == type) {
		hasFile = $("#items-upload")[0].files.length;
	}
	if ("yama" == type) {
		hasFile = $("#yama-upload")[0].files.length;
	}
	if ("saga" == type) {
		hasFile = $("#saga-upload")[0].files.length;
	}
	if ("q10" == type) {
		hasFile = $("#q-upload")[0].files.length;
	}
	if ("yhsh" == type) {
		hasFile = $("#yhsh-upload")[0].files.length;
	}
	if ("yhch" == type) {
		hasFile = $("#yhch-upload")[0].files.length;
	}
	if ("tanpin" == type) {
		hasFile = $("#tanpin-upload")[0].files.length;
	}
	if ("prdAnal" == type) {
		hasFile = $("#prdAnalysis-upload")[0].files.length;
	}
	if ("saga2006" == type) {
		hasFile = $("#saga-upload2006")[0].files.length;
	}
	if ("trans" == type) {
		hasFile = $("#trans-upload")[0].files.length;
	}
	
	if (hasFile <= 0) {
		$.notify({
			// options
			message: 'ファイルを選択してください。' 
		},{
			// settings
			placement: {
				from: "top",
				align: "left"
			},
			z_index: 1031,
			delay: 2000,
			timer: 1000,
			animate: {
				enter: 'animated fadeInDown',
				exit: 'animated fadeOutUp'
			},
			type: 'danger'
		});
		return false;
	}
	
}