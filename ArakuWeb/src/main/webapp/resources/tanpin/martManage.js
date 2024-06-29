/**
 * javascript for Allmart情報
 */
$(function() {
	srch();
	
	$("#end_date").attr("disabled", "disabled");
	$("#start_date").on("change", function() {
		var empty = $("#start_date").val() == "";
		if(empty) {
			$("#end_date").val("");
			$("#end_date").attr("disabled", "disabled");
		} else {
			$("#end_date").removeAttr("disabled");
		}
	});
});
// 수정데이터 배열
var modData = [];
class ColAsDatePicker {
    init(params) {
    	console.log('datePicker');
    	console.log(params);
		// create the cell
		this.eGui = document.createElement('input');
		$(this.eGui).addClass('form-control');
		$(this.eGui).css('width','100px');
		$(this.eGui).css('height','22px');
		$(this.eGui).val(params.value);
		$(this.eGui).datepicker({
			language: "ja"
			 ,format: "yyyy/mm/dd"
			 ,todayHighlight: true
			 ,autoclose: true
		}).on('changeDate', function(){
			modData.push({product_id: params.data.product_id, insert_date: $(this).val()});
			$.ajax({
				url: "modAllMng"
				, type:"post"
				, dataType: "json"
				, contentType: 'application/json'
				, data:JSON.stringify(modData)
				, success: function(result){
					setRowData(result);
					// 수정데이터 초기화
					modData = [];
		    	}
			});
		});
    }

    getGui() {
    	return this.eGui;
    }

    // gets called whenever the cell refreshes
    refresh(params) {
    	return true;
    }

    // gets called when the cell is removed from the grid
    destroy() {
    }

 }

class ColAsDatePicker2 {
    init(params) {
    	console.log('datePicker2');
    	console.log(params);
		// create the cell
		this.eGui = document.createElement('input');
		$(this.eGui).addClass('form-control');
		$(this.eGui).css('width','100px');
		$(this.eGui).css('height','22px');
		$(this.eGui).val(params.value);
		$(this.eGui).datepicker({
			language: "ja"
			 ,format: "yyyy/mm/dd"
			 ,todayHighlight: true
			 ,autoclose: true
		}).on('changeDate', function(){
			modData.push({product_id: params.data.product_id, update_date: $(this).val()});
			$.ajax({
				url: "modAllMng"
				, type:"post"
				, dataType: "json"
				, contentType: 'application/json'
				, data:JSON.stringify(modData)
				, success: function(result){
					setRowData(result);
					// 수정데이터 초기화
					modData = [];
		    	}
			});
		});
    }

    getGui() {
    	return this.eGui;
    }

    // gets called whenever the cell refreshes
    refresh(params) {
    	return true;
    }

    // gets called when the cell is removed from the grid
    destroy() {
    }

 }

var columnDefs = [
    {headerName: "商品ID", field: "product_id", width: 150, editable: true, resizable: true},
    {headerName: "部門ID", field: "department_id", width: 150, editable: true, resizable: true},
    {headerName: "グループコード", field: "group_code", width: 150, editable: true, resizable: true},
    {headerName: "商品コード", field: "product_code", width: 150, editable: true, resizable: true},
    {headerName: "商品名", field: "product_name", width: 300, resizable: true, editable: true, cellEditor: 'agLargeTextCellEditor', cellEditorParams: { maxLength: '500', cols: '30', rows: '6' }},
    {headerName: "商品単価", field: "unit_price", width: 150, editable: true, resizable: true},
    {headerName: "税区分", field: "tax_category", width: 150, editable: true, resizable: true}
];

// specify the data
var rowData = [];
var sProductId, eProductId;
var sDepartmentId, eDepartmentId;
var sGroupCode, eGroupCode;
var sProductCode, eProductCode;
var sProductName, eProductName;
var sUnitPrice, eUnitPrice;
var sTaxCategory, eTaxCategory;

// let the grid know which columns and what data to use
var orderGridOptions = {
	defaultColDef: {
		width: 100
		, headerCheckboxSelection: isFirstColumn
		, checkboxSelection: isFirstColumn
		, resizable: true
	},
	suppressRowClickSelection: false,
	rowSelection: 'multiple',
	columnDefs: columnDefs,
	rowData: rowData,
	components: {
		regDtFrm: ColAsDatePicker
		, expDtFrm: ColAsDatePicker2
	},
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
	onCellEditingStarted: function(event) {
    	var start = event.node.data;
    	sProductId = start.product_id;
    	sDepartmentId = start.department_id;
    	sGroupCode = start.group_code;
    	sProductCode = start.product_code;
    	sProductName = start.product_name;
    	sUnitPrice = start.unit_price;
    	sTaxCategory = start.tax_category;
    },
    onCellEditingStopped: function(event) {
    	var stop = event.node.data;
    	eProductId = stop.product_id;
    	eDepartmentId = stop.department_id;
    	eGroupCode = stop.group_code;
    	eProductCode = stop.product_code;
    	eProductName = stop.product_name;
    	eUnitPrice = stop.unit_price;
    	eTaxCategory = stop.tax_category;
    	
    	if (!(sProductId == eProductId)||!(sDepartmentId == eDepartmentId)
    			||!(sGroupCode == eGroupCode)||!(sProductCode == eProductCode)
    			||!(sProductName == eProductName)||!(sUnitPrice == eUnitPrice)||!(sTaxCategory == eTaxCategory)) {
    		modData.push(stop);
    	}
    }
};

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#allMGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, orderGridOptions);

function setRowData(result) {
	rowData = [];
	for (var i=0; i<result.length; i++) {
		var row = {
				product_id: result[i].product_id
				, department_id: result[i].department_id
				, group_code: result[i].group_code
				, product_code: result[i].product_code
				, product_name: result[i].product_name
				, unit_price:result[i].unit_price
				, tax_category:result[i].tax_category
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
	srch();
});

function srch() {
    var form = $("#frm");

    $.ajax({
        type: "get",
        url: "getAllMng",
        dataType: "json",
        contentType: 'application/json',
        data: form.serialize(), // フォームの要素をシリアライズ
        success: setRowData,
        error: function(jqXHR, textStatus, errorThrown) {
            alert("データの取得中にエラーが発生しました: " + textStatus);
            console.error("AJAX Error: ", textStatus, errorThrown);
        }
    });
}

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
					url: "delAllmart"
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
		id_lst.push(selectedRows[i].product_id);
	}
    
	$("#product_id_list").val(id_lst);
	$("#frm_down").submit();
});

$('#btn_create').on('click', function() {
	var rowData = {
			product_id:"商品ID"
			,department_id:"部門ID"
			,group_code:"グループコード"
			,product_code:"商品コード"
			,product_name:"商品名"
			,unit_price:"商品単価"
			,tax_category:"税区分"
	};
	//orderGridOptions.api.updateRowData({add:[rowData], addIndex:0});
	$.ajax({
        type: "post"
        , url: "addAllmartMng"
    	, data: rowData
        , success: setRowData
    });
});

$('#btn_commit').on('click', function() {
	if (modData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "modAllMng"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modData)
		, success: function(result){
			setRowData(result);
			// 수정데이터 초기화
			modData = [];
    	}
	});
});

