	var oTable;

    var codebase, brand, lang, country;
    var errorContainer, keyTxt, descrTxt, valueTxt;

	$(document).ready(function() {

        initCrumbs();

        errorContainer = $('#error_container');

        keyTxt      = $('#key_txt');
        descrTxt    = $('#descr_txt');
        valueTxt    = $('#value_txt');

		clearInputs();
	
		/* Add a click handler for the create button */
		$('#create_btn').click( function() {{
            createRow();
		}});
		
		/* Add a click handler for the update button */
		$('#update_btn').click( function() {{
            var anSelected = fnGetSelected( oTable );
            if (anSelected.length > 0){
			    updateRow(anSelected);
            }
		}});

		/* Add a click handler for the clear button */
		$('#clear_btn').click( function() {{
			clearInputs();
		}});
	
		/* Add a click handler for the delete button */
		$('#delete_btn').click( function() {
            var anSelected = fnGetSelected( oTable );
            if (anSelected.length > 0){
                if (confirm("Do you want to delete this key and all associated messages?")){
                    deleteRow(anSelected);
                }
            }
		} );

		/* Init the table */
		oTable = $('#data_table').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers",
			"bProcessing": true,
			"sAjaxSource": "/services/shoppingcart/v1/manage/keys/list?codebase="+codebase,
			"aoColumns": [
				{ "mDataProp": "key" },
				{ "mDataProp": "description",
                    "fnRender" : function(oObj) {
                        var description = oObj.aData["description"];
                        if (description == null){
                            return description;
                        }
                        return $('<div/>').text(description).html();
                    },
                    "bUseRendered" : false
                },
                { "mDataProp": "defaultValue",
                    "fnRender" : function(oObj) {
                        var defaultValue = oObj.aData["defaultValue"];
                        if (defaultValue == null){
                            return defaultValue;
                        }
                        return $('<div/>').text(defaultValue).html();
                    },
                    "bUseRendered" : false
                }
			]
		});

		/* Add a click handler to the rows - this could be used as a callback */
		$('#data_table tbody').on( 'click', 'tr', function () {
			if ( $(this).hasClass('row_selected') ) {
			$(this).removeClass('row_selected');
		}else {
			oTable.$('tr.row_selected').removeClass('row_selected');
			$(this).addClass('row_selected');
			populateInputsForSelectedRow();
		}
		});	

	} );


	/* Get the rows which are currently selected */
	function fnGetSelected( oTableLocal )
	{
		return oTableLocal.$('tr.row_selected');
	}
	
	function populateInputsForSelectedRow()
	{
		var anSelected = fnGetSelected( oTable );
        var rowData = oTable.fnGetData( anSelected[0] );

        keyTxt.val(rowData["key"]);
        descrTxt.val(rowData["description"]);
        valueTxt.val(rowData["defaultValue"]);
	}
	
	function clearInputs()
	{
        keyTxt.val('');
		descrTxt.val('');
        valueTxt.val('');
        errorContainer.text('');

        if (oTable){
            oTable.$('tr.row_selected').removeClass('row_selected');
        }
	}

    function deleteRow(anSelected)
    {
        var selectedKey = anSelected[0].cells[0].firstChild.data;

        $.ajax({
            url: "/services/shoppingcart/v1/manage/keys/delete",
            type: "POST",
            data: {key: selectedKey, codebase: codebase},
            success: function(result){
                if (result > 0){
                    oTable.fnDeleteRow( anSelected[0] );
                    clearInputs();
                }else{
                    errorContainer.text('Record not found on server');
                }
            },
            error:function(){
                errorContainer.text('Unable to delete record on server');
            }
        });
    }

    function createRow()
    {
        var newKey =    keyTxt.val();
        var newDescr =  descrTxt.val();
        var newValue =  valueTxt.val();

        if (newKey.length == 0 || newValue.length ==0){
            errorContainer.text('Key and default value fields are mandatory');
            return;
        }

        $.ajax({
            url: "/services/shoppingcart/v1/manage/keys/create",
            type: "POST",
            data: {key: newKey, description: newDescr, value: newValue, codebase: codebase, brand: brand, language: lang, country: country},
            success: function(result){
                if (result > 0){
                    var arAddData = {
                        "key": newKey,
                        "description": newDescr,
                        "defaultValue": newValue
                    };
                    $('#data_table').dataTable().fnAddDataAndDisplay ( arAddData );
                    clearInputs();
                }else{
                    errorContainer.text('Unable to create record on server')
                }
            },
            error:function(){
                errorContainer.text('Unable to create record on server');
            }
        });
    }


    function updateRow(anSelected)
    {
        var selectedKey = oTable.fnGetData( anSelected[0], 0);
        var newDescr =  descrTxt.val();
        var newValue =  valueTxt.val();

        if (newValue.length ==0){
            errorContainer.text('Default value is mandatory');
            return;
        }

        $.ajax({
            url: "/services/shoppingcart/v1/manage/keys/update",
            type: "POST",
            data: {key: selectedKey, description: newDescr, codebase: codebase, value: newValue},
            success: function(result){
                if (result > 0){
                    var arUpdateData = {
                        "key": selectedKey,
                        "description": newDescr,
                        "defaultValue": newValue
                    };
                    $('#data_table').dataTable().fnUpdate ( arUpdateData, anSelected[0] );
                    clearInputs();
                }else{
                    errorContainer.text('Record not found server')
                }
            },
            error:function(){
                errorContainer.text('Unable to update record on server');
            }
        });
    }

    function initCrumbs(){
        codebase = $("#codebase_code").text();
        brand    = $("#brand_code").text();
        lang     = $("#lang_code").text();
        country  = $("#country_code").text();

        if (codebase.trim().length == 0){
            codebase = null;
            $("#codebase_crumb").hide();
        }

        if (brand.trim().length == 0){
            brand = null;
            $("#brand_crumb").hide();
        }

        if (lang.trim().length == 0){
            lang = null;
            $("#lang_crumb").hide();
        }

        if (country.trim().length == 0){
            country = null;
            $("#country_crumb").hide();
        }
    }