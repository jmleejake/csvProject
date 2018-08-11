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

// init rowData
var rowData = [];

var selectedData, startBeforeTrans, stopBeforeTrans, startAfterTrans, stopAfterTrans;

// let the grid know which columns and what data to use
var transGridOptions = {
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
    onRowSelected: onRowSelected,
    rowData: rowData,
    onCellEditingStarted: function(event) {
        previousData = event.node.data;
        console.log("editing start pre");
        startBeforeTrans = previousData.beforeTrans;
        startAfterTrans = previousData.afterTrans;
        console.log(startBeforeTrans + ' -- ' + startAfterTrans);
    },
    onCellEditingStopped: function(event) {
    	console.log("editing stop");
        afterData = event.node.data;
        stopBeforeTrans = afterData.beforeTrans;
        stopAfterTrans = afterData.afterTrans;
        console.log(stopBeforeTrans + ' -- ' + stopAfterTrans);
        
        if (!(startBeforeTrans == stopBeforeTrans) || 
        		!(startAfterTrans == stopAfterTrans)) {
        	console.log("modified!");
        	console.log(afterData);
        	$.ajax({
        		url: "modTrans"
        			, dataType: "json"  
        				, contentType : "application/json"
        					, data:{
        						seq_id:afterData.seq_id
        						, before_trans:afterData.beforeTrans
        						, after_trans:afterData.afterTrans
        					}
        	, success: setRowData
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
	console.log("row selected");
	selectedData = event.node.data;
	console.log(selectedData);
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
	transGridOptions.api.updateRowData({add:[rowData]});
});

$("#btn_delete").on("click", function() {
	console.log("delete");
	
	alertify.confirm("本当に「"+selectedData.beforeTrans+"」を削除しますか？", function (e) {
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