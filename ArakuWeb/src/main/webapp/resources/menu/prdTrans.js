/**
 * javascript for 商品中間マスタ
 */
var columnDefs = [
    {headerName: "注文番号", field: "order_no", width: 100
    	, editable: true
    	, cellEditor: 'agPopupTextCellEditor'
	}
    /*, {headerName: "ＪＡＮコード", field: "jan_cd", width: 100
    	, editable: true
    	, cellEditor: 'agPopupTextCellEditor'
    }*/
    , {headerName: "区分", field: "order_gbn", width: 80
    	, editable: true
    	, cellEditor: 'agPopupTextCellEditor'
    }
    , {headerName: "商品名・項目・選択肢 置換前", field: "beforeTrans", width: 300
    	, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	    // 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
    }
    , {headerName: "商品名・項目・選択肢 置換後", field: "afterTrans", width: 300
    	, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
    }
    /*, {headerName: "商品マスタ反映有無", field: "prd_master_hanei_gbn", width: 80
    	, editable: true
    	, cellEditor: 'agPopupTextCellEditor'
    }*/
    , {headerName: "商品数", field: "prd_cnt", width: 80
    	, editable: true
    	, cellEditor: 'agPopupTextCellEditor'
    }
];

// rowData 초기화
var rowData = [];

// 수정 전후를 파악할 변수 선언 / 선택된 데이터 변수
var selectedData, 
startBeforeTrans, stopBeforeTrans, 
startAfterTrans, stopAfterTrans,
startOrderNo, stopOrderNo,
startJanCd, stopJanCd,
startOrderGbn, stopOrderGbn,
startPrdMHaneiGbn, stopPrdMHaneiGbn,
startPrdCnt, stopPrdCnt;

//수정데이터 배열
var modifiedData = [];

var transGridOptions = {
	// 첫번째 컬럼 체크박스 세팅
	defaultColDef: {
        width: 100,
        headerCheckboxSelection: isFirstColumn,
        checkboxSelection: isFirstColumn
    },
    enableColResize: true,
    suppressRowClickSelection: false,
    rowSelection: 'multiple',
    columnDefs: columnDefs,
    onRowSelected: onRowSelected,
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
        var previousData = event.node.data;
        startBeforeTrans = previousData.beforeTrans;
        startAfterTrans = previousData.afterTrans;
        startOrderNo = previousData.order_no;
        startJanCd = previousData.jan_cd;
        startOrderGbn = previousData.order_gbn;
        startPrdMHaneiGbn = previousData.prd_master_hanei_gbn;
        startPrdCnt = previousData.prd_cnt;
    },
    onCellEditingStopped: function(event) {
        var afterData = event.node.data;
        stopBeforeTrans = afterData.beforeTrans;
        stopAfterTrans = afterData.afterTrans;
        stopOrderNo = afterData.order_no;
        stopJanCd = afterData.jan_cd;
        stopOrderGbn = afterData.order_gbn;
        stopPrdMHaneiGbn = afterData.prd_master_hanei_gbn;
        stopPrdCnt = afterData.prd_cnt;
        
        if (!(startBeforeTrans == stopBeforeTrans) || !(startAfterTrans == stopAfterTrans) 
        	|| !(startOrderNo == stopOrderNo) || !(startJanCd == stopJanCd)
        	|| !(startOrderGbn == stopOrderGbn) || !(startPrdMHaneiGbn == stopPrdMHaneiGbn)
        	|| !(startPrdCnt == stopPrdCnt)) {
        	console.log("modified!");
        	console.log(afterData);
        	modifiedData.push({
        		seq_id:afterData.seq_id
				, before_trans:afterData.beforeTrans
				, after_trans:afterData.afterTrans
				, order_no:afterData.order_no
				, jan_cd:afterData.jan_cd
				, order_gbn:afterData.order_gbn
				, prd_master_hanei_gbn:afterData.prd_master_hanei_gbn
				, prd_cnt:afterData.prd_cnt
				, trans_target_type:afterData.trans_target_type
        	});
        }
    }
};

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

function onRowSelected(event) {
	selectedData = event.node.data;
}

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#translateGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, transGridOptions);

$.ajax({
    url: "getPrdTrans"
    , dataType: "json"  
    , contentType : "application/json"
    , success: setRowData
});

function setRowData(result) {
	rowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, beforeTrans:result[i].before_trans
				, afterTrans:result[i].after_trans
				, register_date:result[i].register_date
				, update_date:result[i].update_date
				, order_no:result[i].order_no
				, jan_cd:result[i].jan_cd
				, order_gbn:result[i].order_gbn
				, prd_master_hanei_gbn:result[i].prd_master_hanei_gbn
				, prd_cnt:result[i].prd_cnt
				, trans_target_type:result[i].trans_target_type
		}
		rowData.push(row);
	}
	transGridOptions.api.setRowData(rowData);
	
	// 수정데이터 초기화
	modifiedData = [];
}

var search = function() {
	console.log("search");
	var form = $("#transForm");
    var url = form.attr('action');
    
    $.ajax({
        type: "post",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData
    });
}

$('#btn_srch').on('click', function() {
	search();
});

$("#btn_create").on("click", function() {
	console.log("create");
	var rowData = {order_no:"99", jan_cd:"999", order_gbn:"0", before_trans: "置換前", after_trans: "置換後", prd_master_hanei_gbn:"0", prd_cnt:"1", trans_target_type:$('#transTarget').val()};
	modifiedData.push(rowData);
	$.ajax({
		url: "modPrdTrans"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: setRowData
	});
});

$("#btn_commit").on("click", function() {
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "modPrdTrans"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: setRowData
	});
});

$("#btn_delete").on("click", function() {
	var selectedRows = transGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "delPrdTrans"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setRowData
			});
		} else {
			return false;
		}
	});
});


/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
/*+++++++++++++++++++++++++総商品数+++++++++++++++++++++++++*/
/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

var columnDefs2 = [
    {headerName: "置換後名", field: "afterTrans", width: 250}
    , {headerName: "総商品数", field: "prd_sum", width: 120}
    , {headerName: "商品マスタ反映有無", field: "prd_master_hanei_gbn", width: 200}
];

// rowData 초기화
var rowData2 = [];

var orderSumGridOptions = {
	// 첫번째 컬럼 체크박스 세팅
	defaultColDef: {
        width: 100,
        headerCheckboxSelection: isFirstColumn2,
        checkboxSelection: isFirstColumn2
    },
    enableColResize: true,
    suppressRowClickSelection: false,
    rowSelection: 'multiple',
    columnDefs: columnDefs2,
    onRowSelected: onRowSelected2,
    rowData: rowData2,
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
        var previousData = event.node.data;
    },
    onCellEditingStopped: function(event) {
        var afterData = event.node.data;
    }
};

function isFirstColumn2(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

function onRowSelected2(event) {
	selectedData = event.node.data;
}

// lookup the container we want the Grid to use
var eGridDiv2 = document.querySelector('#orderSumGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv2, orderSumGridOptions);

function setRowData2(result) {
	rowData2 = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, afterTrans:result[i].after_trans
				, register_date:result[i].register_date
				, update_date:result[i].update_date
				, jan_cd:result[i].jan_cd
				, prd_master_hanei_gbn:result[i].prd_master_hanei_gbn
				, prd_sum:result[i].prd_sum
				, target_type:result[i].target_type
		}
		rowData2.push(row);
	}
	orderSumGridOptions.api.setRowData(rowData2);
}

$.ajax({
    url: "getOrderSum"
    , dataType: "json"  
    , contentType : "application/json"
    , success: setRowData2
});

$('#btn_sum').on('click', function() {
	$.ajax({
	    url: "executeOrderSum"
    	, type:"post"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , success: setRowData2
	});
});

$('#btn_order_sum_del').on('click', function() {
	var selectedRows = orderSumGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "delOrderSum"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setRowData2
			});
		} else {
			return false;
		}
	});
});

$("#btn_down").on("click", function() {
	$("#frm").submit();
});