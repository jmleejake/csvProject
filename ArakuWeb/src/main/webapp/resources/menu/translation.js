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

var previousData, afterData;

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
        previousData = event.node.data.afterTrans;
    },
    onCellEditingStopped: function(event) {
        afterData = event.node.data.afterTrans;

        console.log("previous : " + previousData);
        console.log("after : " + afterData);
        if (!(previousData == afterData)) {
                console.log("modified!");
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
	console.log(event.node.data);
}

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#translateGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, transGridOptions);

$('#btn_srch').on('click', function() {
	console.log("search");
	var rowData = [
	    {beforeTrans: "ホンチョ３本セット", afterTrans: "ホンチョ sa⋇３"},
	    {beforeTrans: "ミチョ3本セット", afterTrans: "ミチョ sa⋇３"}
	];
	transGridOptions.api.setRowData(rowData);
});

$("#btn_create").on("click", function() {
	console.log("create");
	var rowData = {beforeTrans: "置換前", afterTrans: "置換後"};
	transGridOptions.api.updateRowData({add:[rowData]});
});

$("#btn_delete").on("click", function() {
	console.log("delete");
});