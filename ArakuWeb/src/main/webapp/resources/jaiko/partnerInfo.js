/*
------------------
grid setting S
------------------
*/

var columnDefs = [
	{headerName: "取引先No.", field: "partner_id", width: 170
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '20',
            cols: '30',
            rows: '3'
        }
	}
	, {headerName: "取引先名", field: "partner_nm", width: 250
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '100',
            cols: '30',
            rows: '3'
        }
	}
	/*
	郵便番号
	住所
	電話番号
	FAX番号
	担当者名
	*/
	, {headerName: "詳細情報", width: 250, cellRenderer: 'btnRenderer'}
];

// specify the data
var rowData = [];

// 수정데이터 배열
var partnerModified = [];
var prevPartId, afterPartId;
var prevPartNm, afterPartNm;

// let the grid know which columns and what data to use
var partnerGridOptions = {
	// checkbox on first column
	defaultColDef: {
		width: 100,
		headerCheckboxSelection: isFirstColumn,
		checkboxSelection: isFirstColumn
	},
	enableColResize: true,
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: columnDefs,
	rowData: rowData,
	components: {
		btnRenderer: function(param) {
			var html = "<div>";
			html += "<span>";
			html += "<button type='button' class='btn btn-default' data-toggle='modal' data-target='#partnerDetail' onclick='javascript:showDetail("+param.data.seq_id+");' style='height:25px; margin-bottom:5px;'>詳細</button>";
			html += "</span>";
			html += "</div>"
			return html;
		}
	},
	rowClassRules: {
    	'trans-created': function(params) {
    		var target = params.data.register_date;
    		return target === getDate(0);
    	},
    	'trans-modified': function(params) {
    		var target = params.data.update_date;
    		return target === getDate(0);
    	},
    	'trans-error' : function(params) {
    		var target = params.data.err_text;
    		return target === 'ERR';
    	}
    },
    onCellEditingStarted: function(event) {
        var previousData = event.node.data;
        prevPartId = previousData.partner_id;
        prevPartNm = previousData.partner_nm;
    },
    onCellEditingStopped: function(event) {
        var afterData = event.node.data;
        afterPartId = afterData.partner_id;
        afterPartNm = afterData.partner_nm;
        
        if (!(prevPartId == afterPartId) ||
        	!(prevPartNm == afterPartNm)) {
        	console.log("modified!");
        	partnerModified.push({
        		seq_id:afterData.seq_id
				, partner_id:afterPartId
				, partner_nm:afterPartNm
        	});
        }
    }
};

// lookup the container we want the Grid to use
var partnerGridDiv = document.querySelector('#partnerGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(partnerGridDiv, partnerGridOptions);

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
function setPartnerRow(result) {
	rowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, partner_id:result[i].partner_id
			, partner_nm:result[i].partner_nm
			, register_date:result[i].reg_dt
			, update_date:result[i].upd_dt
		};
		rowData.push(row);
	}
	partnerGridOptions.api.setRowData(rowData);
				
	// 초기화
	partnerModified = []; // 수정데이터
	$("#keyword").val("");
	$("#area_feedback").css("display", "none");
	$("#ret_msg").val('');
}

$("#btn_part_add").on("click", function() {
	console.log("列追加");
	partnerModified.push({
		partner_id: "取引先No."
		, partner_nm: "取引先名"
	});
	$.ajax({
		url: "/jaiko/partner/manipulate"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(partnerModified)
		, success: setPartnerRow
	});
});

$("#btn_part_commit").on("click", function() {
	console.log("登録");
	if (partnerModified.length == 0) {
		$("#area_feedback").css("display", "block");
		$("#ret_msg").val('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "/jaiko/partner/manipulate"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(partnerModified)
		, success: setPartnerRow
	});
});

$("#btn_part_delete").on("click", function() {
	console.log("削除");
	var selectedRows = partnerGridOptions.api.getSelectedRows();
	
	if (selectedRows.length == 0) {
    	$("#area_feedback").css("display", "block");
		$("#ret_msg").val('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "/jaiko/partner/delete"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setPartnerRow
			});
		} else {
			return false;
		}
	});
});

$("#btn_part_search").on("click", function() {
	$.ajax({
		url: "/jaiko/partner/getInfo"
		, type:"get"
		, data: {
			keyword: $('#keyword').val()
		}
		, dataType: "json"
		, contentType: 'application/json'
		, success: setPartnerRow
	});
});

$("#btn_part_select").on("click", function() {
	var selectedRows = partnerGridOptions.api.getSelectedRows();
    
    if(selectedRows.length == 0) {
    	$("#area_feedback").css("display", "block");
		$("#ret_msg").val('取引先を選択してください。');
        return;
    }
    
    if(selectedRows.length > 1) {
   		$("#area_feedback").css("display", "block");
		$("#ret_msg").val('一つの取引先を選んでください。');
        return;
    }
    
    $("#partnerModal").modal("hide");
    $("#partner_id").html(selectedRows[0].partner_id);
    $("#partner_nm").html(selectedRows[0].partner_nm);
    $("#jan_cd").focus();
});

/*
------------------
button action E
------------------
*/


/*
------------------
상세화면S
------------------
*/
function showDetail(data) {
	$.ajax({
		url: "/jaiko/partner/getInfo"
		, type:"get"
		, data: {
			seq_id: data
		}
		, dataType: "json"
		, contentType: 'application/json'
		, success: function(res) {
			if(res.length > 0) {
				var data = res[0];
				$("#part_seq_id").val(data.seq_id);
				$("#part_id").val(data.partner_id);
				$("#part_nm").val(data.partner_nm);
				$("#part_post").val(data.partner_post);
				$("#part_add").val(data.partner_add);
				$("#part_tel").val(data.partner_tel);
				$("#part_fax").val(data.partner_fax);
				$("#part_tan").val(data.tantou_nm);
			}else {
				alert("情報がありません。");
			}
		}
	});
}

function detailCommit() {
	partnerModified.push({
		seq_id: $("#part_seq_id").val()
		, partner_post: $("#part_post").val()
		, partner_add: $("#part_add").val()
		, partner_tel: $("#part_tel").val()
		, partner_fax: $("#part_fax").val()
		, tantou_nm: $("#part_tan").val()
	});
	
	$.ajax({
		url: "/jaiko/partner/manipulate"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(partnerModified)
		, success: function(res) {
			if(res.length > 0) {
				var data = res[0];
				$("#part_seq_id").val(data.seq_id);
				$("#part_id").val(data.partner_id);
				$("#part_nm").val(data.partner_nm);
				$("#part_post").val(data.partner_post);
				$("#part_add").val(data.partner_add);
				$("#part_tel").val(data.partner_tel);
				$("#part_fax").val(data.partner_fax);
				$("#part_tan").val(data.tantou_nm);
				partnerModified = []; // 수정데이터
			}else {
				alert("情報がありません。");
			}
		}
	});
}
/*
------------------
상세화면E
------------------
*/
