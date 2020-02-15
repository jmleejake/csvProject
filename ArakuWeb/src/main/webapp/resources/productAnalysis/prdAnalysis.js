/**
 * javascript for 注文情報
 */
// specify the columns

var columnDefs = [
	{headerName: "MEMO", field: "memo", width: 150, editable: true}
	, {headerName: "商品名", field: "prd_nm", width: 300
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
	, {headerName: "ＳＫＵ", field: "sku", width: 150
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '300',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "URL", field: "url", width: 120
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
		, cellRenderer: function(params) {
			return '<a href="'+params.value+'" target="_blank">ホームページ</a>';
		}
	}
	, {headerName: "商品構成", field: "prd_config", width: 100, editable: true}
	, {headerName: "1個当たり仕入金額(税別)", field: "prd_price", width: 150, editable: true}
	, {headerName: "包装(箱+印刷+他)", field: "prd_pkg", width: 150, editable: true}
	, {headerName: "送料", field: "ship_cost", width: 100, editable: true}
	, {headerName: "追加送料", field: "add_ship_cost", width: 100, editable: true}
	, {headerName: "合計仕入価格", field: "ttl_price", width: 150}
	, {headerName: "販売手数料", field: "sales_comm_ratio", width: 120, editable: true}
	, {headerName: "販売手数料金額", field: "sales_comm_price", width: 120}
	, {headerName: "販売価格", field: "sales_price", width: 120, editable: true}
	, {headerName: "利益", field: "benefit", width: 120}
	, {headerName: "利益率", field: "benefit_ratio", width: 120}
	, {headerName: "備考", field: "etc", width: 80}
];

// specify the data
var rowData = [];
// 수정데이터 배열
var modifiedData = [];

var id;
var prevMemo, afterMemo;
var prevPrdNm, afterPrdNm;
var prevSku, afterSku;
var prevUrl, afterUrl;
var prevPrdConfig, afterPrdConfig;
var prevPrdPrice, afterPrdPrice;
var prevPrdPkg, afterPrdPkg;
var prevShipCost, afterShipCost;
var prevAddShipCost, afterAddShipCost;
var prevSalesCommRatio, afterSalesCommRatio;
var prevSalesPrice, afterSalesPrice;
var prevEtc, afterEtc;

// let the grid know which columns and what data to use
var orderGridOptions = {
		defaultColDef: {
			width: 100,
			headerCheckboxSelection: isFirstColumn,
			checkboxSelection: isFirstColumn
		},
		enableColResize: true,
		suppressRowClickSelection: false,
		rowSelection: 'multiple',
		columnDefs: columnDefs,
		rowData: rowData,
		onCellEditingStarted: function(event) {
	    	var start = event.node.data;
	    	
	    	prevMemo = start.memo;
	    	prevPrdNm = start.prd_nm;
	    	prevSku = start.sku;
	    	prevUrl = start.url;
	    	prevPrdConfig = start.prd_config;
	    	prevPrdPrice = start.prd_price;
	    	prevPrdPkg = start.prd_pkg;
	    	prevShipCost = start.ship_cost;
	    	prevAddShipCost = start.add_ship_cost;
	    	prevSalesCommRatio = start.sales_comm_ratio;
	    	prevSalesPrice = start.sales_price;
	    	prevEtc = start.etc;
	    },
	    onCellEditingStopped: function(event) {
	    	var stop = event.node.data;
	    	
	    	afterMemo = stop.memo;
	    	afterPrdNm = stop.prd_nm;
	    	afterSku = stop.sku;
	    	afterUrl = stop.url;
	    	afterPrdConfig = stop.prd_config;
	    	afterPrdPrice = stop.prd_price;
	    	afterPrdPkg = stop.prd_pkg;
	    	afterShipCost = stop.ship_cost;
	    	afterAddShipCost = stop.add_ship_cost;
	    	afterSalesCommRatio = stop.sales_comm_ratio;
	    	afterSalesPrice = stop.sales_price;
	    	afterEtc = stop.etc;
	    	
	    	id = stop.seq_id;
	    	if (!(prevMemo==afterMemo) ||
	    			!(prevPrdNm==afterPrdNm) ||
	    			!(prevSku==afterSku) ||
	    			!(prevUrl==afterUrl) ||
	    			!(prevPrdConfig==afterPrdConfig) ||
	    			!(prevPrdPrice==afterPrdPrice) ||
	    			!(prevPrdPkg==afterPrdPkg) ||
	    			!(prevShipCost==afterShipCost) ||
	    			!(prevAddShipCost==afterAddShipCost) ||
	    			!(prevSalesCommRatio==afterSalesCommRatio) ||
	    			!(prevSalesPrice==afterSalesPrice) ||
	    			!(prevEtc==afterEtc)) {
	    		console.log("modified!");
	    		modifiedData.push({
	    			seq_id: id
	    			, memo: afterMemo
	    			, prd_nm: afterPrdNm
	    			, sku: afterSku
	    			, url: afterUrl
	    			, prd_config: afterPrdConfig
	    			, prd_price: afterPrdPrice
	    			, prd_pkg: afterPrdPkg
	    			, ship_cost: afterShipCost
	    			, add_ship_cost: afterAddShipCost
	    			, sales_comm_ratio: afterSalesCommRatio
	    			, sales_price: afterSalesPrice
	    			, etc: afterEtc
	    		});
	    	}
	    },
	    rowClassRules: {
	    	'trans-error' : function(params) {
	    		var target = Number(params.data.benefit_ratio);
	    		return target < 0;
	    	}
	    }
};

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#prdMGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, orderGridOptions);

function setRowData(result) {
	rowData = [];
	$('#selectedMaker').html("");
	$('#hidMakerCd').val("");
	$('#selectedDealer').html("");
	$('#hidDealerId').val("");
	$('input[name=prd_nm]').val("");
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, memo: result[i].memo
				, prd_nm: result[i].prd_nm
				, sku: result[i].sku
				, url: result[i].url
				, prd_config: result[i].prd_config
				, prd_price: result[i].prd_price
				, prd_pkg: result[i].prd_pkg
				, ship_cost: result[i].ship_cost
				, sales_comm_ratio: result[i].sales_comm_ratio
				, sales_price: result[i].sales_price
				, etc: result[i].etc
				, ttl_price: result[i].ttl_price
				, sales_comm_price: result[i].sales_comm_price
				, benefit: result[i].benefit
				, benefit_ratio: result[i].benefit_ratio
		}
		rowData.push(row);
	}
	orderGridOptions.api.setRowData(rowData);
}


function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}


$("#btn_srch").on("click", function() {
	var form = $("#frm");
    
    $.ajax({
        type: "get"
        , url: "getPrdAnal"
        , dataType: "json"
        , contentType: 'application/json'
        , data: form.serialize() // serializes the form's elements.
        , success: setRowData
    });

});

$("#btn_del").on("click", function() {
	var selectedRows = orderGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			 $.ajax({
					url: "delPrdAnal"
					, type:"post"
					, dataType: "json"
					, contentType: 'application/json'
					, data:JSON.stringify(selectedRows)
					, success: setRowData
				});
		}else {
			return false;
		}
	});
});

$("#btn_down").on("click", function() {
	var selectedRows = orderGridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    var id_lst = [];
    
    for (var i=0; i<selectedRows.length; i++) {
		id_lst.push(selectedRows[i].seq_id);
	}
    
	$("#seq_id_list").val(id_lst);
	$("#frm_down").submit();
});

$("#btn_upd").on("click", function() {
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "updPrdAnal"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: function(result){
			setRowData(result);
			// 수정데이터 초기화
			modifiedData = [];
    	}
	});
});

