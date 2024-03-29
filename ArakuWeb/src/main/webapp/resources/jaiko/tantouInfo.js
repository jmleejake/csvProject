/*
------------------
grid setting S
------------------
*/

var tantouCal = [
	{headerName: "ID", field: "tantou_id", width: 150, editable: true}
	, {headerName: "パスワード", field: "tantou_pass", width: 150, editable: true}
	, {headerName: "担当者名", field: "tantou_nm", width: 150, editable: true}
	, {headerName: "電話番号", field: "tantou_tel", width: 150, editable: true}
	, {headerName: "権限", field: "tantou_auth", width: 100, editable: true}
];

// specify the data
var rowData = [];

// 수정데이터 배열
var tantouModified = [];
var prevTantouNm, afterTantouNm;
var prevTantouTel, afterTantouTel;
var prevId, afterId;
var prevPass, afterPass;
var prevAuth, afterAuth;

// let the grid know which columns and what data to use
var tantouGridOptions = {
	// checkbox on first column
	defaultColDef: {
		width: 100
		, headerCheckboxSelection: isFirstColumn
		, checkboxSelection: isFirstColumn
		, resizable: true
	},
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: tantouCal,
	rowData: rowData,
	rowClassRules: {
    	'trans-created': function(params) {
    		var target = params.data.register_date;
    		return target === getDate(0);
    	},
    	'trans-modified': function(params) {
    		var target = params.data.update_date;
    		return target === getDate(0);
    	}
    },
    onCellEditingStarted: function(event) {
        var prev = event.node.data;
        prevTantouNm = prev.tantou_nm;
        prevTantouTel = prev.tantou_tel;
        prevId = prev.tantou_id;
        prevPass = prev.tantou_pass;
        prevAuth = prev.tantou_auth;
    },
    onCellEditingStopped: function(event) {
        var after = event.node.data;
        afterTantouNm = after.tantou_nm;
        afterTantouTel = after.tantou_tel;
        afterId = after.tantou_id;
        afterPass = after.tantou_pass;
        afterAuth = after.tantou_auth;
        
        if (!(prevTantouNm == afterTantouNm) ||
        	!(prevTantouTel == afterTantouTel) ||
        	!(prevId == afterId) ||
        	!(prevPass == afterPass) ||
        	!(prevAuth == afterAuth)) {
        	console.log("modified!");
        	tantouModified.push(after);
        }
    }
};

// lookup the container we want the Grid to use
var tantouGridDiv = document.querySelector('#tantouGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(tantouGridDiv, tantouGridOptions);

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

/*
------------------
grid setting E
------------------
*/


/*
------------------
button action S
------------------
*/
function setTantouRow(result) {
	rowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, tantou_nm:result[i].tantou_nm
			, tantou_tel:result[i].tantou_tel
			, tantou_id: result[i].tantou_id
			, tantou_pass: result[i].tantou_pass
			, tantou_auth: result[i].tantou_auth
			, register_date:result[i].reg_dt
			, update_date:result[i].upd_dt
		};
		rowData.push(row);
	}
	tantouGridOptions.api.setRowData(rowData);
				
	// 초기화
	tantouModified = []; // 수정데이터
	$("#keyword").val("");
	$("#area_feedback2").css("display", "none");
	$("#ret_msg2").val('');
}

$("#btn_tantou_add").on("click", function() {
	tantouModified.push({
		tantou_nm: "担当者名"
		, tantou_tel: "電話番号"
		, tantou_id: 'ID'
		, tantou_pass: 'パスワード'
		, tantou_auth: '1'	
	});
	$.ajax({
		url: "/jaiko/partner/tantou/manipulate"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(tantouModified)
		, success: setTantouRow
	});
});

$("#btn_tantou_commit").on("click", function() {
	if (tantouModified.length == 0) {
		$("#area_feedback2").css("display", "block");
		$("#ret_msg2").val('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "/jaiko/partner/tantou/manipulate"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(tantouModified)
		, success: setTantouRow
	});
});

$("#btn_tantou_delete").on("click", function() {
	var selectedRows = tantouGridOptions.api.getSelectedRows();
	
	if (selectedRows.length == 0) {
    	$("#area_feedback2").css("display", "block");
		$("#ret_msg2").val('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "/jaiko/partner/tantou/delete"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setTantouRow
			});
		} else {
			return false;
		}
	});
});

$("#btn_tantou_search").on("click", function() {
	srchTantou();
});

function srchTantou() {
	$.ajax({
		url: "/jaiko/partner/tantou/getInfo"
		, type:"get"
		, data: $('#frm_tantou').serialize()
		, dataType: "json"
		, contentType: 'application/json'
		, success: setTantouRow
	});
}

$("#btn_tantou_select").on("click", function() {
	var selectedRows = tantouGridOptions.api.getSelectedRows();
    
    if(selectedRows.length == 0) {
    	$("#area_feedback2").css("display", "block");
		$("#ret_msg2").val('担当者を選択してください。');
        return;
    }
    
    if(selectedRows.length > 1) {
   		$("#area_feedback2").css("display", "block");
		$("#ret_msg2").val('一つの担当者を選んでください。');
        return;
    }
    
    $("#tantouModal").modal("hide");
    $("#tantou_nm").html(selectedRows[0].tantou_nm);
    $("#tantou_id").val(selectedRows[0].tantou_id);
    $("#jan_cd").focus();
});

/*
------------------
button action E
------------------
*/
