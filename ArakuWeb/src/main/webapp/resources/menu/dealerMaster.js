/**
 * javascript for 取引先マスタ
 */
// specify the columns
var columnDefs4 = [
	{headerName: "取引先ID", field: "dealer_id", width: 150
		, editable: true
	},
	{headerName: "取引先名", field: "dealer_nm", width: 200
		, editable: true
	},
];

//rowData 초기화
var rowData4 = [];

//수정 전후를 파악할 변수 선언
var startDealId, stopDealId;
var startDealNm, stopDealNm;

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
	    	startDealId = start.dealer_id;
	    	startDealNm = start.dealer_nm;
	    },
	    onCellEditingStopped: function(event) {
	    	var stop = event.node.data;
	    	stopDealId = stop.dealer_id;
	    	stopDealNm = stop.dealer_nm;
	    	
	    	id4 = stop.seq_id;
	    	if (!(startDealId == stopDealId)||!(startDealNm == stopDealNm)) {
	    		console.log("modified!");
	    		modifiedData4.push({
	    			seq_id: id4
	    			, dealer_id: stopDealId
	    			, dealer_nm: stopDealNm
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