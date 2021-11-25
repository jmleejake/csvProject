/**
 * javascript for 取引先マスタ
 */
// specify the columns
var columnDefs4 = [
	{headerName: "取引先コード", field: "dealer_id", width: 200
		, editable: true
	},
	{headerName: "取引先名", field: "dealer_nm", width: 250
		, editable: true
	},
	{headerName: "電話", field: "dealer_tel", width: 100
		, editable: true
	},
	{headerName: "FAX", field: "dealer_fax", width: 100
		, editable: true
	},
	{headerName: "携帯", field: "dealer_mobile", width: 100
		, editable: true
	},
	{headerName: "郵便番号", field: "dealer_post", width: 100
		, editable: true
	},
	{headerName: "住所", field: "dealer_add", width: 200
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
	},
	{headerName: "納品希望日", field: "est_delivery_dt", width: 150
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
	},
	{headerName: "納品先", field: "destination", width: 150
		, editable: true
	},
	{headerName: "備考", field: "remark", width: 300
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
	},
];

//rowData 초기화
var rowData4 = [];

//수정 전후를 파악할 변수 선언
var startId, stopId;
var startNm, stopNm;
var startTel, stopTel;
var startFax, stopFax;
var startMobile, stopMobile;
var startPost, stopPost;
var startAdd, stopAdd;
var startEstDt, stopEstDt;
var startDest, stopDest;
var startRmk, stopRmk;

//수정데이터 배열
var modifiedData4 = [];

var gridOption4 = {
		// 첫번째 컬럼 체크박스 세팅
		defaultColDef: {
	        width: 100,
	        headerCheckboxSelection: isFirstColumn,
	        checkboxSelection: isFirstColumn
	    },
	    enableColResize: true,
	    suppressRowClickSelection: false,
	    rowSelection: 'multiple',
		columnDefs: columnDefs4,
		rowData: rowData4,
		onCellEditingStarted: function(event) {
	    	var start = event.node.data;
	    	startId = start.dealer_id;
	    	startNm = start.dealer_nm;
	    	startTel = start.dealer_tel;
	    	startFax = start.dealer_fax;
	    	startMobile = start.dealer_mobile;
	    	startPost = start.dealer_post;
	    	startAdd = start.dealer_add;
	    	startEstDt = start.est_delivery_dt;
	    	startDest = start.destination;
	    	startRmk = start.remark;
	    },
	    onCellEditingStopped: function(event) {
	    	var stop = event.node.data;
	    	stopId = stop.dealer_id;
	    	stopNm = stop.dealer_nm;
	    	stopTel = stop.dealer_tel;
	    	stopFax = stop.dealer_fax;
	    	stopMobile = stop.dealer_mobile;
	    	stopPost = stop.dealer_post;
	    	stopAdd = stop.dealer_add;
	    	stopEstDt = stop.est_delivery_dt;
	    	stopDest = stop.destination;
	    	stopRmk = stop.remark;
	    	
	    	if (!(startId == stopId)||!(startNm == stopNm)||!(startTel == stopTel)
	    			||!(startFax == stopFax)||!(startMobile == stopMobile)||!(startPost == stopPost)
	    			||!(startAdd == stopAdd)||!(startEstDt == stopEstDt)
	    			||!(startDest == stopDest)||!(startRmk == stopRmk)) {
	    		modifiedData4.push({
	    			seq_id: stop.seq_id
	    			, dealer_id: stopId
	    			, dealer_nm: stopNm
	    			, dealer_tel: stopTel
	    			, dealer_fax: stopFax
	    			, dealer_mobile: stopMobile
	    			, dealer_post: stopPost
	    			, dealer_add: stopAdd
	    			, est_delivery_dt: stopEstDt
	    			, destination: stopDest
	    			, remark: stopRmk
	    		});
	    	}
	    }
};

//lookup the container we want the Grid to use
var eGridDiv4 = document.querySelector('#dealerGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv4, gridOption4);

function showDealerMaster() {
	$.ajax({
	    url: "showDealerMaster"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data:{}
	    , success: setRowData4
	});
}

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

function setRowData4(result) {
	rowData4 = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, dealer_id: result[i].dealer_id
				, dealer_nm: result[i].dealer_nm
				, dealer_tel: result[i].dealer_tel
				, dealer_fax: result[i].dealer_fax
				, dealer_mobile: result[i].dealer_mobile
				, dealer_post: result[i].dealer_post
				, dealer_add: result[i].dealer_add
				, est_delivery_dt: result[i].est_delivery_dt
				, destination: result[i].destination
				, remark: result[i].remark
		}
		
		rowData4.push(row);
	}
	gridOption4.api.setRowData(rowData4);
}

$("#btn_dealer_commit").on("click", function() {
	if (modifiedData4.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "maniDealerMaster"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData4)
		, success: function(result){
			setRowData4(result);
			// 수정데이터 초기화
			modifiedData4 = [];
    	}
	});
});

$('#btn_dealer_srch').on('click', function() {
	var form = $("#prdFrm");
    var url = form.attr('action');
    
    $.ajax({
        type: "get",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData4
    });
	
});

$("#btn_dealer_add").on("click", function() {
	var rowData = {};
	gridOption4.api.updateRowData({add:[rowData], addIndex:0});
	
	modifiedData4.push({
		dealer_id: ''
		, dealer_nm:''
	});
	
	$.ajax({
		url: "maniDealerMaster"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData4)
		, success: function(result){
			setRowData4(result);
			// 수정데이터 초기화
			modifiedData4 = [];
    	}
	});
});

$("#btn_dealer_delete").on("click", function() {
	var selectedRows = gridOption4.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "delDealerMaster"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setRowData4
			});
		} else {
			return false;
		}
	});
});