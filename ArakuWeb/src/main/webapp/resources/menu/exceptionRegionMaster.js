/**
 * javascript for 例外地域データ
 */
// specify the columns
var columnDefs73 = [
	{headerName: "例外地域データ", field: "exception_data", width: 500
		, editable: true
    	, cellEditor: 'agPopupTextCellEditor'	
	}
];

//rowData 초기화
var rowData73 = [];

//수정 전후를 파악할 변수 선언
var startData73, stopData73, id73;

//수정데이터 배열
var modifiedData73 = [];

var gridOption73 = {
		// 첫번째 컬럼 체크박스 세팅
		defaultColDef: {
			width: 100
			, headerCheckboxSelection: isFirstColumn
			, checkboxSelection: isFirstColumn
			, resizable: true
		},
	    suppressRowClickSelection: false,
	    rowSelection: 'multiple',
		columnDefs: columnDefs73,
		rowData: rowData73,
		onCellEditingStarted: function(event) {
	    	var start = event.node.data;
	    	startData73 = start.exception_data;
	    },
	    onCellEditingStopped: function(event) {
	    	var stop = event.node.data;
	    	stopData73 = stop.exception_data;
	    	id73 = stop.seq_id;
	    	if (!(startData73 == stopData73)) {
	    		console.log("modified!");
	    		modifiedData73.push({
	    			seq_id: id73
	    			, exception_data: stopData73
	    		});
	    	}
	    }
};

//lookup the container we want the Grid to use
var eGridDiv73 = document.querySelector('#exceptionRegionGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv73, gridOption73);

function showExceptionRegionMaster() {
	$.ajax({
	    url: "showExceptionRegionMaster"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data:{}
	    , success: setRowData73
	});
}

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

function setRowData73(result) {
	rowData73 = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, exception_data: result[i].exception_data
		}
		
		rowData73.push(row);
	}
	gridOption73.api.setRowData(rowData73);
}

$("#btn_regionCommit").on("click", function() {
	if (modifiedData73.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "modExceptionRegionMaster"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData73)
		, success: function(result){
			rowData73 = [];
			
			for (var i=0; i<result.length; i++) {
				var row = {
					seq_id: result[i].seq_id
					, exception_data: result[i].exception_data
				}
				
				rowData73.push(row);
			}
			
			gridOption73.api.setRowData(rowData73);
				
			// 수정데이터 초기화
			modifiedData73 = [];
    	}
	});
});

var exceptionRegionSearch = function() {
	var form = $("#exceptionRegionFrm");
    var url = form.attr('action');
    
    $.ajax({
        type: "post",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData73
    });
}

$('#btn_RegionSrch').on('click', function() {
	exceptionRegionSearch();
});

$("#btn_regionAdd").on("click", function() {
	var rowData = {exception_data: "例外データ入力"};
	gridOption73.api.updateRowData({add:[rowData], addIndex:0});
});

$("#btn_regionDelete").on("click", function() {
	var selectedRows = gridOption73.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "delExceptionRegionMaster"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setRowData73
			});
		} else {
			return false;
		}
	});
});