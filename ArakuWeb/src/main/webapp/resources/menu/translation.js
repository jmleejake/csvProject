/**
 * javascript for 商品名置換
 */
var columnDefs = [
    {headerName: "商品名・項目・選択肢 置換前", field: "preTrans", width: 300}
    , {headerName: "商品名・項目・選択肢 置換後", field: "afterTrans", width: 300, editable: true}
];

// specify the data
//var rowData = [
//    {preTrans: "ホンチョ３本セット", afterTrans: "ホンチョ sa⋇３"},
//    {preTrans: "ミチョ3本セット", afterTrans: "ミチョ sa⋇３"}
//];

// init rowData
var rowData = [];

var previousData, afterData;
var modifiedData = [];

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
                modifiedData.push(event.node.data);
                console.log(modifiedData);
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
	console.log("!!");
	var rowData = [
	    {preTrans: "ホンチョ３本セット", afterTrans: "ホンチョ sa⋇３"},
	    {preTrans: "ミチョ3本セット", afterTrans: "ミチョ sa⋇３"}
	];
	
	transGridOptions.api.setRowData(rowData);
});