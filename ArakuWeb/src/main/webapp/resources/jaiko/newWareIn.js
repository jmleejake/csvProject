/*
------------------
grid setting S
------------------
*/

// 単位
const unit = {
  1: '単品',
  2: '中品',
  3: '箱'
};

//場所
const loc = {
  1: '倉庫1',
  2: '倉庫2',
  3: '倉庫3'
};

function extractValues(mappings) {
  return Object.keys(mappings);
}

var columnDefs = [
	{headerName: "商品名", field: "prd_nm", width: 400
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '500',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "数量", field: "prd_cnt", width: 100, editable: true}
	, {headerName: "単位", field: "prd_unit", width: 100, editable: true
		, cellEditor: 'agSelectCellEditor'
	    , cellEditorParams: {
	    	values: extractValues(unit)
	    }
		, filter: 'agSetColumnFilter'
		, refData: unit
	}
	, {headerName: "場所", field: "ware_loc", width: 100, editable: true
		, cellEditor: 'agSelectCellEditor'
	    , cellEditorParams: {
	    	values: extractValues(loc)
	    }
		, filter: 'agSetColumnFilter'
		, refData: loc
	}
	, {headerName: "削除", width: 100, cellRenderer: 'btnRenderer'}
];

// specify the data
var rowData = [];

// 수정데이터 배열
var modifiedData = [];

// let the grid know which columns and what data to use
var prdWareInGridOptions = {
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: columnDefs,
	rowData: rowData,
    components: {
    	btnRenderer: function(param) {
			var html = "<div>";
			html += "<span>";
			html += "<button type='button' class='btn btn-danger' onclick='javascript:deleteRow();' style='height:25px; margin-bottom:5px;'>削除</button>";
			html += "</span>";
			html += "</div>"
			return html;
		}
    },
    onCellEditingStarted: function(event) {
        var prev = event.node.data;
        
        console.log('prev');
        console.log(prev);
    },
    onCellEditingStopped: function(event) {
        var after = event.node.data;
        
        console.log('after');
        console.log(after);
    }
};

// lookup the container we want the Grid to use
var prdWareInGridDiv = document.querySelector('#prdWareInGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(prdWareInGridDiv, prdWareInGridOptions);

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
	prdWareInGridOptions.api.setRowData(rowData);
				
	// 초기화
	modifiedData = []; // 수정데이터
}

$("#btn_commit").on("click", function() {
	console.log("登録");
	if($("#partner_id").val() === "") {
		pleaseSelectNotify('取引先を選択してください。');
		return;
	}
	
	if($("#tantou_id").val() === "") {
		pleaseSelectNotify('担当者を選択してください。');
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

$("#btn_delete").on("click", function() {
	var selectedData = prdWareInGridOptions.api.getSelectedRows();
	prdWareInGridOptions.api.applyTransaction({ remove: selectedData });
	prdWareInGridOptions.api.selectAll();
	selectedData = prdWareInGridOptions.api.getSelectedRows();
	rowData = [];
	rowData = selectedData;
});

$("#btn_search").on("click", function() {
	$.ajax({
	    url: "/jaiko/warehouse/getList"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data: {
	    	search_type: 'warehouse',
	    	warehouse_dt: $("#warehouse_dt").val()
	    }
	    , success: setRowData2
	});
});

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
			, prd_case: '' != result[i].prd_case && '0' != result[i].prd_case ? result[i].prd_case : '0'
			, prd_bara:result[i].prd_bara
			, exp_dt:result[i].exp_dt
			, sell_prc:result[i].sell_prc
			, register_date:result[i].reg_dt
			, update_date:result[i].upd_dt
		};
		rowData.push(row);
	}
	prdWareInGridOptions.api.setRowData(rowData);
				
	// 초기화
	modifiedData = []; // 수정데이터
}

$('#btn_srch').on('click', function() {
	srch();
});

function srch() {
	$.ajax({
	    url: "/jaiko/warehouse/getPrd"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data: $('#frm_srch').serialize()
	    , success: function(result) {
	    	rowData = [];
	    	
	    	for (var i=0; i<result.length; i++) {
	    		var row = {
	    			seq_id: result[i].seq_id
	    			, prd_nm:result[i].prd_nm
	    			, jan_cd:result[i].jan_cd
	    			, prd_cnt: '0'
	    			, prd_unit: '1'
	    			, ware_loc: '1'
	    		};
	    		rowData.push(row);
	    	}
	    	prdWareInGridOptions.api.setRowData(rowData);
	    				
	    	// 초기화
	    	modifiedData = []; // 수정데이터
	    }
    });
}

function deleteRow() {
	var selectedData = prdWareInGridOptions.api.getSelectedRows();
	prdWareInGridOptions.api.applyTransaction({ remove: selectedData });
	/*
	prdWareInGridOptions.api.selectAll();
	selectedData = prdWareInGridOptions.api.getSelectedRows();
	rowData = [];
	rowData = selectedData;
	*/
}

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
    url: "/jaiko/warehouse/getPrd"
    , dataType: "json"  
    , contentType : "application/json"
    , data: {
    jan_cd: $("#jan_cd").val()
    , search_type: 'srch'
    }
    , success: function(result) {
    	if(result.length < 1) {
			alert(janCd + "\n登録されていないコードです。");
			return false;
		}
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
			, now_prd_cnt:'0'
			, prd_qty:'0'
			, prd_case:'0'
			, prd_bara:'0'
			, exp_dt:'9999/12/31'
			, sell_prc:'0'
			, register_date:result[0].reg_dt
			, update_date:result[0].upd_dt
		};
		rowData.push(row);
		prdWareInGridOptions.api.setRowData(rowData);
    }
    });
}

$("#jan_cd").on("change paste", function() {
	janCd =  $("#jan_cd").val();
	if("" != janCd) {
		search();
		$("#jan_cd").val("");
		$("#jan_cd").focus();
	}
});

/*
------------------
 propertychange change keyup paste input E
------------------
*/
