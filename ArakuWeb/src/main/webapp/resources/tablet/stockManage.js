/**
 * javascript for 注文情報
 */
// specify the columns

var stockColumnDefs = [
	{headerName: "取引先名", field: "dealer_nm", width: 200}
	, {headerName: "商品名", field: "prd_nm", width: 200}
	, {headerName: "入庫日", field: "receive_date", width: 120}
	, {headerName: "ＪＡＮコード", field: "jan_cd", width: 200}
	, {headerName: "商品数量", field: "prd_cnt", width: 150, editable: true}
];

// specify the data
var stockRowData = [];

// 수정데이터 배열
var modifiedData = [];

var id, previousData, afterData;

// let the grid know which columns and what data to use
var stockGridOptions = {
	// checkbox on first column
	defaultColDef: {
		width: 100,
		headerCheckboxSelection: isFirstColumn,
		checkboxSelection: isFirstColumn
	},
	enableColResize: true,
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: stockColumnDefs,
	rowData: stockRowData,
	onCellEditingStarted: function(event) {
    	var start = event.node.data;
    	previousData = start.prd_cnt;
    },
    onCellEditingStopped: function(event) {
    	var stop = event.node.data;
    	afterData = stop.prd_cnt;
    	
    	id = stop.seq_id;
    	if (!(previousData == afterData)) {
    		console.log("modified!");
    		modifiedData.push({
    			seq_id: id
    			, prd_cnt: afterData
    			, jan_cd: stop.jan_cd
    		});
    	}
    }
};

// lookup the container we want the Grid to use
var stockGridDiv = document.querySelector('#stockMGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(stockGridDiv, stockGridOptions);

function setStockRowData(result) {
	stockRowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, dealer_id: result[i].dealer_id
			, dealer_nm:result[i].dealer_nm
			, prd_nm:result[i].prd_nm
			, receive_date:result[i].receive_date
			, jan_cd:result[i].jan_cd
			, prd_cnt:result[i].prd_cnt
			, register_date:result[i].register_date
			, update_date:result[i].update_date
		}
		stockRowData.push(row);
	}
	stockGridOptions.api.setRowData(stockRowData);
}

//-------------------------------------------------------------------------------

var dealerColumnDef = [
	{headerName: "取引先ID", field: "dealer_id", width: 150}
	, {headerName: "取引先名", field: "dealer_nm", width: 200}
];

//specify the data
var dealerRowData = [];
var selectedID, selectedNm;

// let the grid know which columns and what data to use
var dealerGridOptions = {
	enableColResize: true,
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: dealerColumnDef,
	rowData: dealerRowData,
	onSelectionChanged: fnSelectedDealerInfo
};

// lookup the container we want the Grid to use
var dealerGridDiv = document.querySelector('#dealerGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(dealerGridDiv, dealerGridOptions);



$.ajax({
    url: "showDealerMaster"
    , dataType: "json"  
    , contentType : "application/json"
    , data:{}
    , success: setDealerRowData
});

function setDealerRowData(result) {
	dealerRowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, dealer_id: result[i].dealer_id
				, dealer_nm:result[i].dealer_nm
				, register_date:result[i].register_date
				, update_date:result[i].update_date
		}
		dealerRowData.push(row);
	}
	dealerGridOptions.api.setRowData(dealerRowData);
}


function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

$("#btn_srch").on("click", function() {
	var jan_cd = $("input[name='jan_cd'").val();
	var receive_date = $("input[name='receive_date'").val();
	var dealer_id = $("input[name='dealer_id'").val();
	
	if(jan_cd === "" && receive_date === "" && dealer_id === "") {
		$.notify({
			// options
			message: '検索項目を入力してください。' 
		},{
			// settings
			placement: {
				from: "top",
				align: "left"
			},
			z_index: 1031,
			delay: 2000,
			timer: 1000,
			animate: {
				enter: 'animated fadeInDown',
				exit: 'animated fadeOutUp'
			},
			type: 'danger'
		});
		return false;
	}
	
	var form = $("#stockForm");
    var url = form.attr('action');
    
    $.ajax({
        type: "post",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setStockRowData
    });

});

$("#btn_commit").on("click", function() {
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	if('undefined' === typeof selectedID) {
		pleaseSelectNotify('取引先を選択してください。');
		return;
	}
	
	$.ajax({
		url: "maniStockMng"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: function(result){
			setStockRowData(result);
			// 수정데이터 초기화
			modifiedData = [];
    	}
	});
});

$("#btn_down").on("click", function() {
	var selectedRows = stockGridOptions.api.getSelectedRows();
	if(selectedRows < 1) {
		pleaseSelectNotify('データを選択してください。');
		return;
	}
	var arr = [];
	for(i=0; i<selectedRows.length; i++){
		var seq_id = selectedRows[i].seq_id;
		arr.push(seq_id)
	}
	$("#hidSeqId").val(arr);
	$("#frmDown").submit();
});

$("#btn_test").on("click", function() {
	var jan_cd = $("#inp_jancd").val();
	
	if('' === jan_cd) {
		pleaseSelectNotify('JANコードを入力してください。');
		return;
	}
	
	if('undefined' === typeof selectedID) {
		pleaseSelectNotify('取引先を選択してください。');
		return;
	}
	
	$("#sp_jancd").html("バーコード: "+jan_cd);
	$("#sp_jancd").css("font-size", "25px");
	
	modifiedData.push({
		dealer_id: selectedID
		, jan_cd: jan_cd
	});
	
	$.ajax({
		url: "maniStockMng"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: function(result){
			setStockRowData(result);
			// 수정데이터 초기화
			modifiedData = [];
    	}
	});
});

/*selected 取引先*/
function fnSelectedDealerInfo() {
	selectedID = dealerGridOptions.api.getSelectedRows()[0].dealer_id;
	selectedNm = dealerGridOptions.api.getSelectedRows()[0].dealer_nm;
	$("#sp_dealer").html("取引先名: "+selectedNm);
	$("#sp_dealer").css("font-size", "25px");
}

// propertychange change keyup paste input
$("#inp_jancd").on("change paste", function() {
var jan_cd = $("#inp_jancd").val();
	
	if('' === jan_cd) {
		pleaseSelectNotify('JANコードを入力してください。');
		return;
	}
	
	if('undefined' === typeof selectedID) {
		pleaseSelectNotify('取引先を選択してください。');
		return;
	}
	
	$("#sp_jancd").html("バーコード: "+jan_cd);
	$("#sp_jancd").css("font-size", "25px");
	
	modifiedData.push({
		dealer_id: selectedID
		, jan_cd: jan_cd
	});
	
	$.ajax({
		url: "maniStockMng"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: function(result){
			setStockRowData(result);
			// 수정데이터 초기화
			modifiedData = [];
			$("#inp_jancd").val("");
			$("#inp_jancd").focus();
    	}
	});
});
