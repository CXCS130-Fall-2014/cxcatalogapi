	var oTable;

    var codebase, brand, lang, country;
    var errorContainer, keyTxt, descrTxt, messageTxt, valueTxt;

	$(document).ready(function() {
        initCrumbs();

        errorContainer = $('#error_container');

        keyTxt = $('#key_txt');
        descrTxt = $('#descr_txt');
        messageTxt = $('#message_txt');
        valueTxt = $('#value_txt');

		clearInputs();

		
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
                deleteRow(anSelected);
            }
		} );

        /* Add a click handler for the use_default button */
        $('#use_default_btn').click( function() {{
            messageTxt.val(valueTxt.val());
        }});

		/* Init the table */
		oTable = $('#data_table').dataTable({
			"bJQueryUI": true,
			"sPaginationType": "full_numbers",
			"bProcessing": true,
			"sAjaxSource": "/services/shoppingcart/v1/manage/messages/list?codebase="+codebase+"&brand="+brand+"&language="+lang+"&country="+country,
			"aoColumns": [
				{ "mDataProp": "key" },
                { "mDataProp": "message" ,
                    "fnRender" : function(oObj) {
                        var message = oObj.aData["message"];
                        if (message == null){
                            return null;
                        }
                        return $('<div/>').text(message).html();
                    },
                    "bUseRendered" : false
                },

                { "mDataProp": "description" ,
                    "bVisible" : false
                },
                { "mDataProp": "defaultValue" ,
                    "bVisible" : false
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
        messageTxt.val(rowData["message"]);
		descrTxt.val(rowData["description"]);
        valueTxt.val(rowData["defaultValue"]);
	}
	
	function clearInputs()
	{
        keyTxt.val('');
        descrTxt.val('');
        valueTxt.val('');
        messageTxt.val('');
        errorContainer.text('');

        if (oTable){
            oTable.$('tr.row_selected').removeClass('row_selected');
        }
	}

    function updateRow(anSelected)
    {
        var selectedKey = oTable.fnGetData( anSelected[0], 0);
        var description = oTable.fnGetData( anSelected[0], 2);
        var defaultValue = oTable.fnGetData( anSelected[0], 3);
        var newMessage =  messageTxt.val();

        if (newMessage.length ==0){
            errorContainer.text('Translation value is mandatory');
            return;
        }

        $.ajax({
            url: "/services/shoppingcart/v1/manage/messages/update",
            type: "POST",
            data: {"codebase": codebase, "brand": brand, "language":lang, "country": country, "key" : selectedKey, "message": newMessage},
            success: function(result){
                if (result > 0){
                    var arUpdateData = {
                        "key": selectedKey,
                        "message": newMessage,
                        "description" : description,
                        "defaultValue" : defaultValue
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