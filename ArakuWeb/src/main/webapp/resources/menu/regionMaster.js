/**
 * javascript for 地域区分マスタ
 */
// specify the cell design
var cellClassRule = {
		'header-cell': 'data.section === "region-title"'
};

// specify the columns
var columnDefs = [
	{headerName: "", field: "parent_region_name", width: 1, 
		colSpan: function(params) {
			if (isHeaderRow(params)) {
				return 4;
			} else {
				return 1;
			}
		},
		cellClassRules: cellClassRule
	}
	, {headerName: "地域", field: "region_name", width: 130}
	, {headerName: "地域(英語)", field: "region_name_en", width: 130}
	, {headerName: "配送会社コード", field: "delivery_company", width: 200
		, editable: true
    	, cellEditor: 'agPopupTextCellEditor'
	}
];

//rowData 초기화
var rowData = [];

//수정 전후를 파악할 변수 선언
var startData, stopData, id;

//수정데이터 배열
var modifiedData = [];

var gridOption = {
		getRowHeight: function(params) {
            return isHeaderRow(params) ? 40 : 25;
		},
		columnDefs: columnDefs,
		rowData: rowData,
		onCellEditingStarted: function(event) {
	    	var start = event.node.data;
	    	startData = start.delivery_company;
	    	console.log("start?");
	    	console.log(startData);
	    },
	    onCellEditingStopped: function(event) {
	    	var stop = event.node.data;
	    	stopData = stop.delivery_company;
	    	id = stop.seq_id;
	    	console.log("stop?");
	    	console.log(stopData);
	    	if (!(startData == stopData)) {
	    		console.log("modified!");
	    		modifiedData.push({
	    			seq_id: id
	    			, delivery_company: stopData
	    		});
	    	}
	    }
};

//lookup the container we want the Grid to use
var eGridDiv = document.querySelector('#regionGrid');

// create the grid passing in the div to use together with the columns & data we want to use
new agGrid.Grid(eGridDiv, gridOption);

function isHeaderRow(params) {
	return params.data.section === 'region-title';
}

$.ajax({
    url: "showRegionMaster"
    , dataType: "json"  
    , contentType : "application/json"
    , data:{}
    , success: setRowData
});

function setRowData(result) {
	rowData = [];
	var prePID=0;
	
	for (var i=0; i<result.length; i++) {
		var p_id = result[i].p_id;
		
		if (!(p_id == prePID)) {
			rowData.push({section:"region-title", parent_region_name:result[i].parent_region_name});
		}
		
		var row = {
				seq_id: result[i].seq_id
				, region_name: result[i].region_name
				, region_name_en: result[i].region_name_en
				, delivery_company: result[i].delivery_company
		}
		
		rowData.push(row);
		
		prePID = p_id;
	}
	gridOption.api.setRowData(rowData);
}

$("#btn_commit").on("click", function() {
	if (modifiedData.length == 0) {
		pleaseSelectNotify('情報を修正してください。');
		return;
	}
	
	$.ajax({
		url: "modRegionMaster"
		, type:"post"
		, dataType: "json"
		, contentType: 'application/json'
		, data:JSON.stringify(modifiedData)
		, success: function(result){
			rowData = [];
			var prePID=0;
			
			for (var i=0; i<result.length; i++) {
				var p_id = result[i].p_id;
				
				if (!(p_id == prePID)) {
					rowData.push({section:"region-title", parent_region_name:result[i].parent_region_name});
				}
				
				var row = {
						seq_id: result[i].seq_id
						, region_name: result[i].region_name
						, region_name_en: result[i].region_name_en
						, delivery_company: result[i].delivery_company
				}
				
				rowData.push(row);
				
				prePID = p_id;
			}
			
			gridOption.api.setRowData(rowData);
				
			// 수정데이터 초기화
			modifiedData = [];
    	}
	});
});

$('#btn_srch').on('click', function() {
	var form = $("#regionFrm");
    var url = form.attr('action');
    
    $.ajax({
        type: "post",
        url: url,
        data: form.serialize(), // serializes the form's elements.
        success: setRowData
    });
	
});