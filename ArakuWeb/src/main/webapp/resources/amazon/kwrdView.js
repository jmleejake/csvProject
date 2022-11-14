/**
 * 在庫管理(Keyword別)
 */
var columnDefs = [
    {headerName: "メーカー名", field: "kwrd1", width: 300, editable: true}
    , {headerName: "キーワード名", field: "kwrd2", width: 300, editable: true}
    , {headerName: "検索", width: 200, cellRenderer: 'btnRenderer'}
    , {headerName: "検索", width: 200, cellRenderer: 'btnRenderer2'}
    , {headerName: "検索", width: 200, cellRenderer: 'btnRenderer3'}
    , {headerName: "メモ", field: "memo", width: 100
    	, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '100',
            cols: '50',
            rows: '6'
        }
    }
];

var rowData = [];
var modifiedData = [];
var sKwrd1, eKwrd1
,sKwrd2, eKwrd2
,sMemo, eMemo;

var gridOptions = {
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
        sKwrd1 = previousData.kwrd1;
        sKwrd2 = previousData.kwrd2;
        sMemo = previousData.memo;
    },
    onCellEditingStopped: function(event) {
        var afterData = event.node.data;
        eKwrd1 = afterData.kwrd1;
        eKwrd2 = afterData.kwrd2;
        eMemo = afterData.memo;
        
        if (!(sKwrd1 == eKwrd1) || 
    		!(sKwrd2 == eKwrd2) ||
    		!(sMemo == eMemo)) {
        	console.log("modified!");
        	modifiedData.push(afterData);
        }
    },
    components: {
		btnRenderer: function(param) {
			var html = "<a class='custm_btn srch' href='https://sellercentral.amazon.co.jp/inventory/ref=xx_invmgr_dnav_xx?tbla_myitable=sort:%7B%22sortOrder%22%3A%22DESCENDING%22%7D;search:"+param.data.kwrd2+";pagination:1;' target='_blank'>";
			html += "アマゾン在庫";
			html += "</a>";
			return html;
		}
	    , btnRenderer2: function(param) {
	    	var html = "<a class='custm_btn srch' href='https://www.amazon.co.jp/s?k="+param.data.kwrd2+"'>";
	    	html += "アマゾン";
	    	html += "</a>";
			return html;
		}
	    , btnRenderer3: function(param) {
			var html = "<a class='custm_btn srch' href='https://search.rakuten.co.jp/search/mall/"+param.data.kwrd2+"'>";
			html += "楽天";
			html += "</a>";
			return html;
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

//lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#srchGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, gridOptions);

$.ajax({
	url: "getKwrdInfo"
	, type:"get"
	, dataType: "json"
	, contentType: 'application/json'
	, success: setRowData
});

function setRowData(result) {
	rowData = [];
	console.log(result);
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, kwrd1: result[i].kwrd1
			, kwrd2: result[i].kwrd2
			, memo: result[i].memo
			, register_date: result[i].register_date
			, update_date: result[i].update_date
		};
		rowData.push(row);
	}
	gridOptions.api.setRowData(rowData);
				
	// 초기화
	modifiedData = []; // 수정데이터
}

function srch() {
	$.ajax({
		url: "getKwrdInfo"
		, type:"get"
		, data: $("#frm").serialize()
		, dataType: "json"
		, contentType: 'application/json'
		, success: setRowData
	});
}

$('#btn_srch').click('on', function() {
	srch();
});

$('#btn_add').click('on', function() {
	modifiedData.push({
		kwrd1: 'メーカー'
		, kwrd2: 'キーワード'
		, memo: 'メモ'
	});
	$.ajax({
		url: "modKwrdInfo"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: setRowData
	});
});

$('#btn_commit').click('on', function() {
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	$.ajax({
		url: "modKwrdInfo"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: setRowData
	});
});

$('#btn_delete').click('on', function() {
	var selectedRows = gridOptions.api.getSelectedRows();
    
    if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
				url: "delKwrdInfo"
				, type:"post"
				, dataType: "json"
				, contentType: 'application/json'
				, data:JSON.stringify(selectedRows)
				, success: setRowData
			});
		} else {
			return false;
		}
	});
});

