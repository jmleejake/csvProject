/**
 * javascript for 商品名管理マスタ
 */
// specify the columns
var columnDefs3 = [
	{headerName: "商品管理番号", field: "prd_cd", width: 250
		, editable: true
	},
	{headerName: "ＪＡＮコード", field: "jan_cd", width: 150
		, editable: true
	},
	{headerName: "商品名", field: "prd_nm", width: 400
		, editable: true
		, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }	
	},
	{headerName: "商品詳細", field: "prd_dtl", width: 400
		, editable: true
		, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }	
	},
	{headerName: "商品数", field: "prd_cnt", width: 100
		, editable: true
	},
	{headerName: "順番", field: "order_no", width: 100
		, editable: true
	}
];

//rowData 초기화
var rowData3 = [];

//수정 전후를 파악할 변수 선언
var startPrdCd, stopPrdCd;
var startJanCd, stopJanCd;
var startPrdNm, stopPrdNm;
var startPrdDtl, stopPrdDtl;
var startPrdCnt, stopPrdCnt;
var startOrdNo, stopOrdNo, id3;

//수정데이터 배열
var modifiedData3 = [];

var gridOption3 = {
		// 첫번째 컬럼 체크박스 세팅
		defaultColDef: {
	        width: 100,
	        headerCheckboxSelection: isFirstColumn,
	        checkboxSelection: isFirstColumn
	    },
	    enableColResize: true,
	    suppressRowClickSelection: false,
	    rowSelection: 'multiple',
		columnDefs: columnDefs3,
		rowData: rowData3,
		onCellEditingStarted: function(event) {
	    	var start = event.node.data;
	    	startPrdCd = start.prd_cd;
	    	startJanCd = start.jan_cd;
	    	startPrdNm = start.prd_nm;
	    	startPrdDtl = start.prd_dtl;
	    	startPrdCnt = start.prd_cnt;
	    	startOrdNo = start.order_no;
	    },
	    onCellEditingStopped: function(event) {
	    	var stop = event.node.data;
	    	stopPrdCd = stop.prd_cd;
	    	stopJanCd = stop.jan_cd;
	    	stopPrdNm = stop.prd_nm;
	    	stopPrdDtl = stop.prd_dtl;
	    	stopPrdCnt = stop.prd_cnt;
	    	stopOrdNo = stop.order_no;
	    	
	    	id3 = stop.seq_id;
	    	if (!(startPrdCd == stopPrdCd)||!(startJanCd == stopJanCd)
	    			||!(startPrdNm == stopPrdNm)||!(startPrdDtl == stopPrdDtl)
	    			||!(startPrdCnt == stopPrdCnt)||!(startOrdNo == stopOrdNo)) {
	    		console.log("modified!");
	    		modifiedData3.push({
	    			seq_id: id3
	    			, prd_cd: stopPrdCd
	    			, jan_cd: stopJanCd
	    			, prd_nm: stopPrdNm
	    			, prd_dtl: stopPrdDtl
	    			, prd_cnt: stopPrdCnt
	    			, order_no: stopOrdNo
	    		});
	    	}
	    }
};

//lookup the container we want the Grid to use
var eGridDiv3 = document.querySelector('#prdGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv3, gridOption3);

function showPrdMaster() {
	$.ajax({
	    url: "showPrdMaster"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data:{}
	    , success: setRowData3
	});
}

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

function setRowData3(result) {
	rowData3 = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, prd_cd: result[i].prd_cd
				, jan_cd: result[i].jan_cd
				, prd_nm: result[i].prd_nm
				, prd_dtl: result[i].prd_dtl
				, prd_cnt: result[i].prd_cnt
				, order_no: result[i].order_no
		}
		
		rowData3.push(row);
	}
	gridOption3.api.setRowData(rowData3);
}

$("#btn_prd_commit").on("click", function() {
	if (modifiedData3.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "maniPrdMaster"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData3)
		, success: function(result){
			setRowData3(result);
			// 수정데이터 초기화
			modifiedData3 = [];
    	}
	});
});

$('#btn_prd_srch').on('click', function() {
	var form = $("#prdFrm");
    var url = form.attr('action');
    
    $.ajax({
        type: "get",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData3
    });
	
});

$("#btn_prd_add").on("click", function() {
	var rowData = {};
	gridOption3.api.updateRowData({add:[rowData], addIndex:0});
	
	modifiedData3.push({
		prd_cd: ''
	});
	
	$.ajax({
		url: "maniPrdMaster"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData3)
		, success: function(result){
			setRowData3(result);
			// 수정데이터 초기화
			modifiedData3 = [];
    	}
	});
});

$("#btn_prd_delete").on("click", function() {
	var selectedRows = gridOption3.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "delPrdMaster"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setRowData3
			});
		} else {
			return false;
		}
	});
});