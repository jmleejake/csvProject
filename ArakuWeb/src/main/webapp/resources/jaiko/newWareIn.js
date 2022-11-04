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
	{headerName: "商品名", field: "prd_nm", width: 400}
	, {headerName: "数量", field: "prd_quantity", width: 100, editable: true}
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
var prevCnt, afterCnt;
var prevUnit, afterUnit;
var prevLoc, afterLoc;

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
        prevCnt = prev.prd_quantity;
        prevUnit = prev.prd_unit;
        prevLoc = prev.ware_loc;
    },
    onCellEditingStopped: function(event) {
        var after = event.node.data;
        afterCnt = after.prd_quantity;
        afterUnit = after.prd_unit;
        afterLoc = after.ware_loc;
        if(!(prevCnt === afterCnt) || !(prevUnit === afterUnit) || !(prevLoc === afterLoc)) {
            modifiedData.push({
	         	seq_id: after.seq_id
	         	, partner_id: $("#partner_id").val()
	         	, partner_nm: $("#partner_nm").html()
	         	, tantou_id: $("#tantou_id").val()
	         	, tantou_nm: $("#tantou_nm").html()
	         	, jan_cd: after.jan_cd
	         	, prd_cd: after.prd_cd
	         	, prd_nm: after.prd_nm
	         	, prd_quantity: afterCnt
	         	, prd_unit: afterUnit
	         	, ware_loc: afterLoc
            });
            $.ajax({
        		url: "/jaiko/warehouse/temp/mani"
        		, type:"post"
        		, dataType: "json"
        		, contentType: 'application/json'
        		, data:JSON.stringify(modifiedData)
        		, success: function(res) {
        			console.log('mani');
        			console.log(res);
        		}
        	});
        }
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
	
	$.ajax({
		url: "/jaiko/warehouse/proc"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, success: function(res) {
			console.log('mani');
			console.log(res);
		}
	});
});

$('#btn_srch').on('click', function() {
	srch();
});

function srch() {
	if($("#partner_id").val() === "") {
		pleaseSelectNotify('取引先を選択してください。');
		return;
	}
	
	if($("#tantou_id").val() === "") {
		pleaseSelectNotify('担当者を選択してください。');
		return;
	}
	
	$.ajax({
	    url: "/jaiko/warehouse/getPrd"
	    , dataType: "json"  
	    , contentType : "application/json"
	    , data: $('#frm_srch').serialize()
	    , success: function(result) {
	    	rowData = [];
	    	
	    	for (var i=0; i<result.length; i++) {
	    		console.log(result[i]);
	    		var row = {
	    			seq_id: result[i].seq_id
	    			, partner_id: $("#partner_id").val()
		         	, partner_nm: $("#partner_nm").html()
		         	, tantou_id: $("#tantou_id").val()
		         	, tantou_nm: $("#tantou_nm").html()
	    			, prd_nm:result[i].prd_nm
	    			, jan_cd:result[i].jan_cd1
	    			, prd_cd: result[i].prd_cd
	    			, prd_quantity: '0'
	    			, prd_unit: '1'
	    			, ware_loc: '1'
	    		};
	    		rowData.push(row);
	    	}
	    	prdWareInGridOptions.api.setRowData(rowData);
	    				
	    	$.ajax({
	    		url: "/jaiko/warehouse/temp/mani"
	    		, type:"post"
	    		, dataType: "json"
	    		, contentType: 'application/json'
	    		, data:JSON.stringify(rowData)
	    		, success: function(res) {
	    			console.log('mani');
	    			console.log(res);
	    		}
	    	});
	    }
    });
}

function deleteRow() {
	var selectedData = prdWareInGridOptions.api.getSelectedRows();
	prdWareInGridOptions.api.applyTransaction({ remove: selectedData });
}

/*
------------------
button action E
------------------
*/
