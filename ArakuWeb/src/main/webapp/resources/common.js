/**
 * 
 */

function getDate(range) {
	var d = new Date();
	var month = ''+(d.getMonth() + 1);
    var day = ''+(d.getDate()+range);
    var year = d.getFullYear();

	if (month.length < 2) month = '0' + month;
	if (day.length < 2) day = '0' + day;
	
	var date = year +'/'+ month + '/' + day;
	return date;
}

function pleaseSelectNotify(msg) {
	$.notify({
		// options
		message: msg
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
}

function alertInit() {
	alertify.set({
		labels : {
			ok     : "はい",
			cancel : "いいえ"
		},
		delay : 5000,
		buttonReverse : true,
		buttonFocus   : "ok"
	});
}
