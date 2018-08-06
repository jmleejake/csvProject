/**
 * javascript for 注文情報
 */
// specify the columns
var columnDefs = [
    {headerName: "Make", field: "make", width: 120
		, cellStyle: {cursor: 'pointer'}
		, cellRenderer: function(params) {
		        return '<a onclick="getInfo('+params.node.data.id+');">'+params.value+'</a>';
		}
    }
    , {headerName: "Model", field: "model", width: 120}
    , {headerName: "Price", field: "price", width: 120}
];

// specify the data
var rowData = [
    {make: "Toyota", model: "Celica", price: 35000, id:11},
    {make: "Ford", model: "Mondeo", price: 32000, id:22},
    {make: "Porsche", model: "Boxter", price: 72000, id:33}
];

var previousData, afterData;
var modifiedData = [];

// let the grid know which columns and what data to use
var rFileDownGridOptions = {
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
//    onRowSelected: onRowSelected,
    rowData: rowData
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
var eGridDiv = document.querySelector('#rFileDownGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, rFileDownGridOptions);

function getInfo(data) {
        console.log("getInfo");
        console.log(data);
        $('form').attr('action', "getInfo");
        $('form').attr('method', "post");
        $("#car_id").val(data);
        $("form").submit();
}