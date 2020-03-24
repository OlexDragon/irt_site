function f_enableButtons(node){

	var card	 = $(node).closest('.card-body');
	var toEnable = card.find('.to_enable');

	var enable = true;
	card.find('.required').each(function(index, field){

		enable = field.value.trim() ? true : false;

		if(!enable)
			return false;
	});
	if(enable)
		card.find('.required_at_least_one').each(function(index, field){

			// Check box
			enable = field.checked;

			if(enable)
				return false;
		});

	if(enable){

		toEnable.attr('disabled', false);
		toEnable.removeClass('disabled');

	}else{

		toEnable.attr('disabled', true);
		toEnable.addClass('disabled');
	}
}

function f_addFileRelation(checbox){

	var select = $(checbox).closest('.card-body').find('.file_upload_category');

	if(checbox.checked){
		var option = new Option(checbox.title, checbox.value);
		select.append(option);

	}else{
		select.find("option[value='" + checbox.value + "']").remove();
		select.trigger('change');
	}

	f_enableButtons(checbox);
}

//Add className if the node has value, and delete if does't.
function f_addRequiredClass(node, className){

	var required = $(node).closest('.row').find('.' + className)

	if(node.value)
		required.addClass('required');
	else
		required.removeClass('required');

	f_enableButtons(node);
}

// Add File Upload ow
function f_addFileRow(anchor){

	var fileRow = $(anchor).closest('.file_upload_row');
	var newRow = fileRow.clone();
	fileRow.after(newRow);

	var dropdown = newRow.find('.dropdown-toggle');
	dropdown.dropdown('hide');

	var inp = newRow.find("input")
	inp.val(null);
	newRow.find("label").text("Choose file");


	// Update IDs
	$(anchor).closest('.card-body').find('.file_upload_row').each(function(index, fileRow){

		var row = $(fileRow);
		var input = row.find('input');
		var name = input.attr('name');
		var id =  name+ '_' + index;
		input.attr('id', id);

		var label = row.find('label');
		label.attr('for', id);

	})

	bsCustomFileInput.init()
}

// Remove File Upload
function f_removeFileRow(anchor){

	var rows = $(anchor).closest('.card-body').find('.file_upload_row');
	var row = $(anchor).closest('.file_upload_row');

	if(rows.length>1){

		row.remove();
		f_enableButtons(rows.get(0));

	}else{

		row.find('.file_upload').val(null).trigger('change');
		row.find("label").text("Choose file");
		row.find('.file_upload_category').val('');
	}
}

//Add project links
function f_addProject(anchor){
	var row = $(anchor).closest('.part_number_row');
	var clon = row.clone().insertAfter(row);

	// Reset cloned fields
	var input = clon.find('.part_number');
	input.autocomplete({ source : f_autocomplete });
	input.val('');
	input.trigger('change');

	clon.find('.dropdown-menu')	.dropdown('hide');
}

// Remove project link
function f_removeProject(anchor){

	var rows = $(anchor).closest('.card-body').find('.part_number_row');
	var row = $(anchor).closest('.part_number_row');

	if(rows.length>1){
		row.remove();
		f_enableButtons(rows.get(0));
	}else{
		var input = row.find('.part_number');
		input.val('');
		input.trigger('change');
	}
}

function f_setStatus(button, value){
	$(button).closest('form').find('input[name="new_status"]').val(value);
}

function f_submit(e, form){
	e.preventDefault();

	if(!confirm('Click OK to submit the acrion.'))
		return;

	var buttons = $(form).find('button');
	buttons.attr('disabled', true);
	buttons.addClass('disabled');

	var data = new FormData(form);
	jQuery.ajax({
	    url: form.action,
	    data: data,
	    cache: false,
	    contentType: false,
	    processData: false,
	    method: 'POST',
	    type: 'POST', // For jQuery < 1.9
	    success: function(data){

	    	window.removeEventListener("beforeunload", f_beforeunload);
	   		window.location.replace("/engineering/"+ data);
	    }
	});
	
	var button = $(this).find(':button');
	button			.attr('disabled', true);
	button			.addClass('disabled');

	var input = $(this).find('input');
	input			.attr('disabled', true);
	input			.addClass('disabled');

	var reason = $('#ecr_reason');
	reason.attr('disabled', true);
	reason.addClass('disabled');

	var category = $('.ecr_file_category');
	category.attr('disabled', true);
	category.addClass('disabled');
}
