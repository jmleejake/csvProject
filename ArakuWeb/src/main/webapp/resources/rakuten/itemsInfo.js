/**
 * javascript for CSV一括編集情報
 */

// specify the columns
var columnDefs = [
	{headerName: "コントロールカラム", field: "ctrl_col", width: 80
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '100',
            cols: '50',
            rows: '6'
        }	
	}
	, {headerName: "商品管理番号（商品URL）", field: "item_url", width: 150}
	, {headerName: "商品番号", field: "item_num", width: 100}
	, {headerName: "全商品ディレクトリID", field: "directory_id", width: 100
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '300',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "PC用キャッチコピー", field: "pc_catch_cp", width: 150}
	, {headerName: "モバイル用キャッチコピー", field: "mobile_catch_cp", width: 150}
	, {headerName: "商品名", field: "item_name", width: 200
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
		, editable: true
		, cellEditor: 'agLargeTextCellEditor'
		, cellEditorParams: {
	        maxLength: '500',
	        cols: '50',
	        rows: '6'
	    }
	}
	, {headerName: "販売価格", field: "sell_price", width: 100
		, editable: true
    	, cellEditor: 'agLargeTextCellEditor'
    	, cellEditorParams: {
            maxLength: '100',
            cols: '50',
            rows: '6'
        }
	}
	, {headerName: "PC用商品説明文", field: "desc_pc_item", width: 300
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "スマートフォン用商品説明文", field: "desc_phone_item", width: 300
		// 길이가 긴 항목에 대해서 툴팁 추가.  
		, tooltip: function(params) {
			return params.value;
		}
	}
	, {headerName: "PC用販売説明文", field: "desc_pc_sales", width: 300}
	, {headerName: "在庫タイプ", field: "inven_type", width: 100}
	, {headerName: "在庫数", field: "inven_num", width: 80}
	, {headerName: "白背景画像URL", field: "white_bg_image_url", width: 200}
];

// specify the data
var rowData = [];

//수정데이터 배열
var modifiedData = [];

var st1, st2, st3, st4, ed1, ed2, ed3, ed4;

// let the grid know which columns and what data to use
var itemsGridOptions = {
	// checkbox on first column
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
	onCellEditingStarted: function(event) {
		var start = event.node.data;
		
		st1 = start.ctrl_col;
		st2 = start.directory_id;
		st3 = start.item_name;
		st4 = start.sell_price;
	},
	onCellEditingStopped: function(event) {
		var stop = event.node.data;
		
		ed1 = stop.ctrl_col;
		ed2 = stop.directory_id;
		ed3 = stop.item_name;
		ed4 = stop.sell_price;
		
		if (!(st1 == ed1) || !(st2 == ed2) || !(st3 == ed3) || !(st4 == ed4)) {
			console.log("modified!");
			
			modifiedData.push({
        		seq_id:stop.seq_id
				, ctrl_col:stop.ctrl_col
				, directory_id:stop.directory_id
				, item_name:stop.item_name
				, sell_price:stop.sell_price
				, start_date:$("#start_date").val()
				, end_date:$("#end_date").val()
        	});
		}
	}
};

// lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#itemsGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, itemsGridOptions);

$.ajax({
    url: "getItems"
    , dataType: "json"  
    , contentType : "application/json"
    , data:{}
    , success: setRowData
});

function setRowData(result) {
	rowData = [];
	
	for (var i=0; i<result.length; i++) {
		var row = {
				seq_id: result[i].seq_id
				, ctrl_col: result[i].ctrl_col
				, item_url:result[i].item_url
				, item_num:result[i].item_num
				, directory_id:result[i].directory_id
				, pc_catch_cp:result[i].pc_catch_cp
				, mobile_catch_cp:result[i].mobile_catch_cp
				, item_name:result[i].item_name
				, sell_price:result[i].sell_price
				, desc_pc_item:result[i].desc_pc_item
				, desc_phone_item:result[i].desc_phone_item
				, desc_pc_sales:result[i].desc_pc_sales
				, inven_type:result[i].inven_type
				, inven_num:result[i].inven_num
				, white_bg_image_url:result[i].white_bg_image_url
		}
		rowData.push(row);
	}
	itemsGridOptions.api.setRowData(rowData);
}


function isFirstColumn(params) {
	var displayedColumns = params.columnApi.getAllDisplayedColumns();
	var thisIsFirstColumn = displayedColumns[0] === params.column;
	return thisIsFirstColumn;
}

$("#btn_srch").on("click", function() {
	var form = $("#itemsForm");
    var url = form.attr('action');
    
    $.ajax({
        type: "post",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData
    });

});

$("#end_date").attr("disabled", "disabled");
$("#start_date").on("change", function() {
	var empty = $("#start_date").val() == "";
	
	$("#idown_std").val($("#start_date").val());
	$("#catdown_std").val($("#start_date").val());
	$("#seldown_std").val($("#start_date").val());
	
	if(empty) {
		$("#end_date").val("");
		$("#idown_std").val("");
		$("#idown_ed").val("");
		$("#catdown_std").val("");
		$("#catdown_ed").val("");
		$("#seldown_std").val("");
		$("#seldown_ed").val("");
		
		$("#end_date").attr("disabled", "disabled");
	} else {
		$("#end_date").removeAttr("disabled");
		
		$("#idown_ed").val($("#end_date").val());
		$("#catdown_ed").val($("#end_date").val());
		$("#seldown_ed").val($("#end_date").val());
	}
});

$("#btn_item_down").on("click", function() {
	console.log("item down");
	console.log($("#idown_std").val());
	console.log($("#idown_ed").val());
	
	$("#frm_idown").submit();
});

$("#btn_cat_down").on("click", function() {
	console.log("item-cat down");
	console.log($("#catdown_std").val());
	console.log($("#catdown_ed").val());
	
	$("#frm_catdown").submit();
});

$("#btn_sel_down").on("click", function() {
	console.log("select down");
	console.log($("#seldown_std").val());
	console.log($("#seldown_ed").val());
	
	$("#frm_seldown").submit();
});

$("#btn_commit").on("click", function() {
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
//	console.log(modifiedData);
	$.ajax({
		url: "updateItem"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: function(result){
			// 수정데이터 초기화
			modifiedData = [];
			
			setRowData(result);
    	}
	});
});
