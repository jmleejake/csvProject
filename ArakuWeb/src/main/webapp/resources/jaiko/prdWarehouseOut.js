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
	, {headerName: "入数", field: "prd_qty", width: 100
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
	, {headerName: "単価", field: "prd_unit_prc", width: 100
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "商品税(抜、込)", field: "tax_incld", width: 100
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "税率", field: "tax_rt", width: 100
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
var prevPrdCnt, afterPrdCnt;
var prevPrdUnitPrc, afterPrdUnitPrc;
var prevTaxIncld, afterTaxIncld;
var prevTaxRt, afterTaxRt;
var prevBrandNm, afterBrandNm;
var prevPrdNm, afterPrdNm;
var prevPrdQty, afterPrdQty;
var prevPrdCase, afterPrdCase;
var prevPrdBara, afterPrdBara;
var prevExpDt, afterExpDt;
var prevSellPrc, afterSellPrc;

// let the grid know which columns and what data to use
var prdWareOutGridOptions = {
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
        prevPrdCnt = previousData.prd_cnt;
        prevPrdUnitPrc = previousData.prd_unit_prc;
        prevTaxIncld = previousData.tax_incld;
        prevTaxRt = previousData.tax_rt;
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
        afterPrdCnt = afterData.prd_cnt;
        afterPrdUnitPrc = afterData.prd_unit_prc;
        afterTaxIncld = afterData.tax_incld;
        afterTaxRt = afterData.tax_rt;
        afterBrandNm = afterData.brand_nm;
        afterPrdNm = afterData.prd_nm;
        afterPrdQty = afterData.prd_qty;
        afterPrdCase = afterData.prd_case;
        afterPrdBara = afterData.prd_bara;
        afterExpDt = afterData.exp_dt;
        afterSellPrc = afterData.sell_prc;
        
        console.log("modified!");
        if(!(prevPrdCnt == afterPrdCnt)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, prd_cnt:afterPrdCnt
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevPrdUnitPrc == afterPrdUnitPrc)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, prd_unit_prc:afterPrdUnitPrc
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevTaxIncld == afterTaxIncld)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, tax_incld:afterTaxIncld
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevTaxRt == afterTaxRt)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, tax_rt:afterTaxRt
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevBrandNm == afterBrandNm)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, brand_nm:afterBrandNm
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevPrdNm == afterPrdNm)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, prd_nm:afterPrdNm
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevPrdQty == afterPrdQty)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, prd_qty:afterPrdQty
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevPrdCase == afterPrdCase)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, prd_case:afterPrdCase
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevPrdBara == afterPrdBara)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, prd_bara:afterPrdBara
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevExpDt == afterExpDt)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, exp_dt:afterExpDt
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }else if(!(prevSellPrc == afterSellPrc)) {
        	modifiedData.push({
        		seq_id:afterData.seq_id
        		, jan_cd:afterData.jan_cd
				, sell_prc:afterSellPrc
				, partner_id:$("#partner_id").text()
				, partner_nm:$("#partner_nm").text()
				, search_type:'wareOut'
        	});
        }
    }
};

// lookup the container we want the Grid to use
var prdWareOutGridDiv = document.querySelector('#prdWareOutGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(prdWareOutGridDiv, prdWareOutGridOptions);

function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

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
var janCd;
function setRowData(result) {
	rowData = [];
	
	if(result.length < 1) {
		alert(janCd + "\n登録されていないコードです。");
		return false;
	}
	
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, prd_cd:result[i].prd_cd
			, brand_nm:result[i].brand_nm
			, prd_nm:result[i].prd_nm
			, jan_cd:result[i].jan_cd
			, prd_cnt:result[i].prd_cnt
			, prd_unit_prc:result[i].prd_unit_prc
			, tax_incld:result[i].tax_incld
			, tax_rt:result[i].tax_rt
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
	prdWareOutGridOptions.api.setRowData(rowData);
				
	// 초기화
	modifiedData = []; // 수정데이터
}

function setRowData2(result) {
	rowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
			seq_id: result[i].seq_id
			, prd_cd:result[i].prd_cd
			, brand_nm:result[i].brand_nm
			, prd_nm:result[i].prd_nm
			, jan_cd:result[i].jan_cd
			, prd_cnt:result[i].prd_cnt
			, prd_unit_prc:result[i].prd_unit_prc
			, tax_incld:result[i].tax_incld
			, tax_rt:result[i].tax_rt
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
	prdWareOutGridOptions.api.setRowData(rowData);
				
	// 초기화
	modifiedData = []; // 수정데이터
}

$("#btn_commit").on("click", function() {
	console.log("登録");
	if($("#partner_id").val() === "") {
		pleaseSelectNotify('取引先を選択してください。');
		return;
	}
	
	if(modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	for(var i=0; i<modifiedData.length; i++) {
		modifiedData[i].partner_id = $("#partner_id").val();
		modifiedData[i].partner_nm = $("#partner_nm").text();
	}
	
	$.ajax({
		url: "/jaiko/warehouse/manipulate"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: setRowData
	});
});

$("#btn_search").on("click", function() {
	$.ajax({
	    url: "/jaiko/warehouse/getList"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data: {
	    	delivery_dt: $("#delivery_dt").val()
	    }
	    , success: setRowData2
	});
});

$("#btn_delete").on("click", function() {
	var selectedData = prdWareOutGridOptions.api.getSelectedRows();
	prdWareOutGridOptions.api.applyTransaction({ remove: selectedData });
});

/*
------------------
button action E
------------------
*/


/*
------------------
 propertychange change keyup paste input S
------------------
*/

function search() {
    $.ajax({
	    url: "/jaiko/warehouse/getList"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data: {
	    jan_cd: $("#jan_cd").val()
	    , search_type: 'srch'
	    }
	    , success: function(result) {
	    	var row = {
    			seq_id: result[0].seq_id
    			, prd_cd:result[0].prd_cd
    			, brand_nm:result[0].brand_nm
    			, prd_nm:result[0].prd_nm
    			, jan_cd:result[0].jan_cd
    			, prd_cnt:result[0].prd_cnt
    			, prd_unit_prc:result[0].prd_unit_prc
    			, tax_incld:result[0].tax_incld
    			, tax_rt:result[0].tax_rt
    			, now_prd_cnt:result[0].now_prd_cnt
    			, prd_qty:result[0].prd_qty
    			, prd_case:result[0].prd_case
    			, prd_bara:result[0].prd_bara
    			, exp_dt:result[0].exp_dt
    			, sell_prc:result[0].sell_prc
    			, register_date:result[0].reg_dt
    			, update_date:result[0].upd_dt
    		};
    		rowData.push(row);
    		prdWareOutGridOptions.api.setRowData(rowData);
	    }
	});
}

$("#jan_cd").on("change paste", function() {
	janCd =  $("#jan_cd").val();
	search();
	$("#jan_cd").val("");
	$("#jan_cd").focus();
});

/*
------------------
 propertychange change keyup paste input E
------------------
*/
