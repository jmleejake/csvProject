/**
 * javascript for 例外データマスタ
 */
// specify the columns
var columnDefs2 = [
	{headerName: "例外データ", field: "exception_data", width: 130
		, editable: true
    	, cellEditor: 'agPopupTextCellEditor'	
	}
];

//rowData 초기화
var rowData2 = [];

//수정 전후를 파악할 변수 선언
var startData2, stopData2, id2;

//수정데이터 배열
var modifiedData2 = [];

var gridOption2 = {
		// 첫번째 컬럼 체크박스 세팅
		defaultColDef: {
	        width: 100,
	        headerCheckboxSelection: isFirstColumn,
	        checkboxSelection: isFirstColumn
	    },
	    enableColResize: true,
	    suppressRowClickSelection: false,
	    rowSelection: 'multiple',
		columnDefs: columnDefs2,
		rowData: rowData2,
		onCellEditingStarted: function(event) {
	    	var start = event.node.data;
	    	startData2 = start.exception_data;
	    },
	    onCellEditingStopped: function(event) {
	    	var stop = event.node.data;
	    	stopData2 = stop.exception_data;
	    	id2 = stop.seq_id;
	    	if (!(startData2 == stopData2)) {
	    		console.log("modified!");
	    		modifiedData2.push({
	    			seq_id: id2
	    			, exception_data: stopData2
	    		});
	    	}
	    }
};

//lookup the container we want the Grid to use
var eGridDiv2 = document.querySelector('#exceptionGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv2, gridOption2);

function showExceptionMaster() {
	$.ajax({
	    url: "showExceptionMaster"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data:{}
	    , success: setRowData2
	});
}

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

function setRowData2(result) {
	rowData2 = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, exception_data: result[i].exception_data
		}
		
		rowData2.push(row);
	}
	gridOption2.api.setRowData(rowData2);
}

$("#btn_commit2").on("click", function() {
	if (modifiedData2.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "modExceptionMaster"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData2)
		, success: function(result){
			rowData2 = [];
			
			for (var i=0; i<result.length; i++) {
				var row = {
					seq_id: result[i].seq_id
					, exception_data: result[i].exception_data
				}
				
				rowData2.push(row);
			}
			
			gridOption2.api.setRowData(rowData2);
				
			// 수정데이터 초기화
			modifiedData2 = [];
    	}
	});
});

$('#btn_srch2').on('click', function() {
	var form = $("#exceptionFrm");
    var url = form.attr('action');
    
    $.ajax({
        type: "post",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData2
    });
	
});

$("#btn_add").on("click", function() {
	var rowData = {exception_data: "例外データ入力"};
	gridOption2.api.updateRowData({add:[rowData], addIndex:0});
});

$("#btn_delete").on("click", function() {
	var selectedRows = gridOption2.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "delExceptionMaster"
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