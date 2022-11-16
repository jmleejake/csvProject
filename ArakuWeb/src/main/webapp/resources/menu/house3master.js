/**
 * 第三倉庫マスタ
 */
// specify the columns
var house3ColumnDefs = [
	{headerName: "例外データ", field: "house3_data", width: 500, editable: true}
];

//rowData 초기화
var house3RowData = [];

//수정 전후를 파악할 변수 선언
var house3StartData, house3StopData;

//수정데이터 배열
var house3ModData = [];

var house3GridOption = {
		// 첫번째 컬럼 체크박스 세팅
		defaultColDef: {
			width: 100
			, headerCheckboxSelection: isFirstColumn
			, checkboxSelection: isFirstColumn
			, resizable: true
		},
	    suppressRowClickSelection: false,
	    rowSelection: 'multiple',
		columnDefs: house3ColumnDefs,
		rowData: house3RowData,
		onCellEditingStarted: function(event) {
	    	var start = event.node.data;
	    	house3StartData = start.house3_data;
	    },
	    onCellEditingStopped: function(event) {
	    	var stop = event.node.data;
	    	house3StopData = stop.house3_data;
	    	if (!(house3StartData == house3StopData)) {
	    		house3ModData.push({
	    			seq_id: stop.seq_id
	    			, house3_data: house3StopData
	    		});
	    	}
	    }
};

//lookup the container we want the Grid to use
var house3GridDiv = document.querySelector('#house3Grid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(house3GridDiv, house3GridOption);

function showHouse3Master() {
	$.ajax({
	    url: "getHouse3"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data:{}
	    , success: setHouse3RowData
	});
}

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

function setHouse3RowData(result) {
	house3RowData = [];
	house3ModData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, house3_data: result[i].house3_data
		}
		
		house3RowData.push(row);
	}
	house3GridOption.api.setRowData(house3RowData);
}

$("#btn_commit_house3").on("click", function() {
	if (house3ModData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "modHouse3"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(house3ModData)
		, success: setHouse3RowData
	});
});

var house3Search = function() {
	var form = $("#house3Frm");
    var url = form.attr('action');
    
    $.ajax({
        type: "post",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setHouse3RowData
    });
}

$('#btn_srch_house3').on('click', function() {
	house3Search();
});

$("#btn_add_house3").on("click", function() {
	house3ModData.push({house3_data: "第三倉庫データ"});
	$.ajax({
		url: "modHouse3"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(house3ModData)
		, success: setHouse3RowData
	});
});

$("#btn_delete_house3").on("click", function() {
	var selectedRows = house3GridOption.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "delHouse3"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setHouse3RowData
			});
		} else {
			return false;
		}
	});
});