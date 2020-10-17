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
