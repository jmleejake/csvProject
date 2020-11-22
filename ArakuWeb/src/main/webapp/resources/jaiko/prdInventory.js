/*
------------------
grid setting S
------------------
*/

var columnDefs = [
	{headerName: "商品コード", field: "prd_cd", width: 200}
	, {headerName: "ＪＡＮコード", field: "jan_cd", width: 200}
	, {headerName: "ブランド", field: "brand_nm", width: 200
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "商品名", field: "prd_nm", width: 400
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "入数", field: "prd_qty", width: 200
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "ケース数", field: "prd_case", width: 250
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "バラ数", field: "prd_bara", width: 250
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "現在商品数", field: "now_prd_cnt", width: 250}
	, {headerName: "賞味期限", field: "exp_dt", width: 250, editable: true, cellEditor: 'datePicker'}
	, {headerName: "本体売価", field: "sell_prc", width: 250
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
];

// specify the data
var rowData = [];

// 수정데이터 배열
var modifiedData = [];
var prevBrandNm, afterBrandNm;
var prevPrdNm, afterPrdNm;
var prevPrdQty, afterPrdQty;
var prevPrdCase, afterPrdCase;
var prevPrdBara, afterPrdBara;
var prevExpDt, afterExpDt;
var prevSellPrc, afterSellPrc;

// let the grid know which columns and what data to use
var prdInvenGridOptions = {
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
	rowData: rowData,
	rowClassRules: {
    	'trans-created': function(params) {
    		var target = params.data.register_date;
    		return target === getDate(0);
    	},
    	'trans-modified': function(params) {
    		var target = params.data.update_date;
    		return target === getDate(0);
    	},
    	'trans-error' : function(params) {
    		var target = params.data.err_text;
    		return target === 'ERR';
    	}
    },
    components: {
    	datePicker: getDatePicker(),
    },
    onCellEditingStarted: function(event) {
        var previousData = event.node.data;
        prevBrandNm = previousData.brand_nm;
        prevPrdNm = previousData.prd_nm;
        prevPrdQty = previousData.prd_qty;
        prevPrdCase = previousData.prd_case;
        prevPrdBara = previousData.prd_bara;
        prevExpDt = previousData.exp_dt;
        prevSellPrc = previousData.sell_prc;
    },
    onCellEditingStopped: function(event) {
        var afterData = event.node.data;
        afterBrandNm = afterData.brand_nm;
        afterPrdNm = afterData.prd_nm;
        afterPrdQty = afterData.prd_qty;
        afterPrdCase = afterData.prd_case;
        afterPrdBara = afterData.prd_bara;
        afterExpDt = afterData.exp_dt;
        afterSellPrc = afterData.sell_prc;
        
        if (!(prevBrandNm == afterBrandNm) ||
        	!(prevPrdNm == afterPrdNm) ||
        	!(prevPrdQty == afterPrdQty) ||
        	!(prevPrdCase == afterPrdCase) ||
        	!(prevPrdBara == afterPrdBara) ||
        	!(prevExpDt == afterExpDt) ||
        	!(prevSellPrc == afterSellPrc)) {
        	console.log("modified!");
        	modifiedData.push({
        		seq_id:afterData.seq_id
				, brand_nm:afterBrandNm
				, prd_nm:afterPrdNm
				, prd_qty:afterPrdQty
				, prd_case:afterPrdCase
				, prd_bara:afterPrdBara
				, exp_dt:afterExpDt
				, sell_prc:afterSellPrc
        	});
        }
    }
};

// lookup the container we want the Grid to use
var prdInvenGridDiv = document.querySelector('#prdInvenGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(prdInvenGridDiv, prdInvenGridOptions);

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

$.ajax({
    url: "/jaiko/prdInven/getPrdInven"
    , dataType: "json"  
    , contentType : "application/json"
    , success: setRowData
});

function getDatePicker() {
	// function to act as a class
	function Datepicker() {}
	
	// gets called once before the renderer is used
	Datepicker.prototype.init = function (params) {
		// create the cell
	    this.eInput = document.createElement('input');
	    this.eInput.value = params.value;
	    this.eInput.classList.add('ag-input');
	    this.eInput.style.height = '100%';
	
	    // https://jqueryui.com/datepicker/
	    $(this.eInput).datepicker({
		      language: "ja"
			, autoclose: true
	    });
	};
	
	// gets called once when grid ready to insert the element
	Datepicker.prototype.getGui = function () {
	    return this.eInput;
	};
	
	// focus and select can be done after the gui is attached
	Datepicker.prototype.afterGuiAttached = function () {
	    this.eInput.focus();
	    this.eInput.select();
	};
	
	// returns the new value after editing
	Datepicker.prototype.getValue = function () {
	    return this.eInput.value;
	};
	
	// any cleanup we need to be done here
	Datepicker.prototype.destroy = function () {
	    // but this example is simple, no cleanup, we could
	    // even leave this method out as it's optional
	};
	
	// if true, then this editor will appear in a popup
	Datepicker.prototype.isPopup = function () {
	    // and we could leave this method out also, false is the default
	    return false;
	};
	
	return Datepicker;
}

/*
------------------
grid setting E
------------------
*/


/*
------------------
button action S
------------------
*/
function setRowData(result) {
	rowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, prd_cd:result[i].prd_cd
			, brand_nm:result[i].brand_nm
			, prd_nm:result[i].prd_nm
			, jan_cd:result[i].jan_cd
			, now_prd_cnt:result[i].now_prd_cnt
			, prd_qty:result[i].prd_qty
			, prd_case:result[i].prd_case
			, prd_bara:result[i].prd_bara
			, exp_dt:result[i].exp_dt
			, sell_prc:result[i].sell_prc
			, register_date:result[i].reg_dt
			, update_date:result[i].upd_dt
		};
		rowData.push(row);
	}
	prdInvenGridOptions.api.setRowData(rowData);
				
	// 초기화
	modifiedData = []; // 수정데이터
	$("#jan_cd").val(""); // 바코드번호 (jan_cd)
	$("#prd_cd").val(""); // 상품코드
	$("#prd_nm").val(""); // 상품명
}

$("#btn_add").on("click", function() {
	console.log("列追加");
	var today = new Date();
	var dd = String(today.getDate()).padStart(2, '0');
	var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
	var yyyy = today.getFullYear();
	
	modifiedData.push({
		prd_cd: "商品コード"
		, brand_nm: "ブランド"
		, prd_nm: "商品名"
		, jan_cd: "ＪＡＮコード"
		, now_prd_cnt: "0"
		, prd_qty: "0"
		, prd_case: "0"
		, prd_bara: "0"
		, exp_dt: yyyy+"/"+mm+"/"+dd
		, sell_prc: "0"
	});
	$.ajax({
		url: "/jaiko/prdInven/manipulate"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: setRowData
	});
	//prdInvenGridOptions.api.updateRowData({add:[data], addIndex:0});
});

$("#btn_commit").on("click", function() {
	console.log("登録");
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "/jaiko/prdInven/manipulate"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: setRowData
	});
});

$("#btn_delete").on("click", function() {
	console.log("削除");
	var selectedRows = prdInvenGridOptions.api.getSelectedRows();
	
	if (selectedRows.length == 0) {
    	pleaseSelectNotify('情報を選択してください。');
        return;
    }
    
    alertInit();
	alertify.confirm("本当に削除しますか？", function (e) {
		if (e) {
			$.ajax({
			    url: "/jaiko/prdInven/delete"
		    	, type:"post"
			    , dataType: "json"  
			    , contentType : "application/json"
			    , data:JSON.stringify(selectedRows)
			    , success: setRowData
			});
		} else {
			return false;
		}
	});
});

$("#btn_search").on("click", function() {
	$.ajax({
		url: "/jaiko/prdInven/getPrdInven"
		, type:"get"
		, data: $("#frm_srch").serialize()
		, dataType: "json"
		, contentType: 'application/json'
		, success: setRowData
	});
});

$("#btn_down").on("click", function() {
	$("#frm_down").submit();
});


/*
------------------
button action E
------------------
*/
