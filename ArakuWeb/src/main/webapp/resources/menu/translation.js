/**
 * javascript for 商品名置換
 */
var columnDefs = [
    {headerName: "商品名・項目・選択肢 置換前", field: "beforeTrans", width: 300
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
];

// rowData 초기화
var rowData = [];

// 수정 전후를 파악할 변수 선언 / 선택된 데이터 변수
var selectedData, startBeforeTrans, stopBeforeTrans, startAfterTrans, stopAfterTrans;

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
    },
    onCellEditingStopped: function(event) {
        var afterData = event.node.data;
        stopBeforeTrans = afterData.beforeTrans;
        stopAfterTrans = afterData.afterTrans;
        
        if (!(startBeforeTrans == stopBeforeTrans) || 
        		!(startAfterTrans == stopAfterTrans)) {
        	console.log("modified!");
        	modifiedData.push({
        		seq_id:afterData.seq_id
				, before_trans:afterData.beforeTrans
				, after_trans:afterData.afterTrans
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

alertify.set({
	labels : {
		ok     : "はい",
		cancel : "いいえ"
	},
	delay : 5000,
	buttonReverse : true,
	buttonFocus   : "ok"
});

$.ajax({
    url: "getTrans"
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
		}
		rowData.push(row);
	}
	transGridOptions.api.setRowData(rowData);
}

$('#btn_srch').on('click', function() {
	console.log("search");
	var form = $("#transForm");
    var url = form.attr('action');
    
    $.ajax({
        type: "post",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData
    });
	
});

$("#btn_create").on("click", function() {
	console.log("create");
	var rowData = {beforeTrans: "置換前", afterTrans: "置換後"};
	transGridOptions.api.updateRowData({add:[rowData], addIndex:0});
});

$("#btn_commit").on("click", function() {
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "modTrans"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: function(result){
			var rowData = [];
			for (var i=0; i<result.length; i++) {
				rowData.push({
					seq_id: result[i].seq_id
					, beforeTrans:result[i].before_trans
					, afterTrans:result[i].after_trans
					, register_date:result[i].register_date
					, update_date:result[i].update_date});
			}
			
			transGridOptions.api.setRowData(rowData);
				
			// 수정데이터 초기화
			modifiedData = [];
    	}
	});
});

$("#btn_delete").on("click", function() {
	var selectedRows = transGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
	
	var delArr = [];
	delArr.push();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "delTrans"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:{seq_id:selectedData.seq_id}
			    , success: setRowData
			});
		} else {
			return false;
		}
	});
});