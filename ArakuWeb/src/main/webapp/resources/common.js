/**
 * 
 */
function getDate(range, type) {
	if(undefined === type) {
		type = '/';
	}
	var d = new Date();
	var month = ''+(d.getMonth() + 1);
    var day = ''+(d.getDate()+range);
    var year = d.getFullYear();

	if (month.length < 2) month = '0' + month;
	if (day.length < 2) day = '0' + day;
	
	var date = year+''+type+''+month+''+type+''+day;
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

function successNotify(msg) {
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
		type: 'success'
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

/*
로딩이미지 S
*/
function loadMask(gif) {
    //화면의 높이와 너비를 구합니다.
    var maskHeight = $(document).height();
    var maskWidth  = window.document.body.clientWidth;
     
    //화면에 출력할 마스크를 설정해줍니다.
    var mask       ="<div id='mask' style='position:absolute; z-index:1000; background-color:#000000; display:none; left:0; top:0;'></div>";
    var loadingImg ='';
      
    loadingImg +=" <img src='"+ gif +"' style='position: fixed; z-index:2000; display: block; margin: 0px auto;'/>";
 
    //화면에 레이어 추가
    $('body')
        .append(mask)
 
    //마스크의 높이와 너비를 화면 것으로 만들어 전체 화면을 채웁니다.
    $('#mask').css({
            'width' : maskWidth,
            'height': maskHeight,
            'opacity' :'0.3'
    });
  
    //마스크 표시
    $('#mask').show();
  
    //로딩중 이미지 표시
    $('#loadingImg').append(loadingImg);
    $('#loadingImg').show();
}
 
function closeMask() {
    $('#mask, #loadingImg').hide();
    $('#mask, #loadingImg').empty(); 
}
/*
로딩이미지 E
*/
