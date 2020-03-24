var posting;
function getECRs(){

	if(posting) posting.abort();

	var data = {};
	data['start'] = $('#ecr_accordion .card').length;
	data['size'] = 10;
	data['status'] = [];
	$('#ecr_filter').find('input[name="ecr_status"]:checked').each(function(){
		data['status'].push($(this).val());
	});

	posting = $.post("/engineering/ecr/ajax", data);
	posting.done(function(data){
		$('#ecr_accordion').append(data);
	})
	.fail(function(error) {
		alert("$.post('/engineering/ecr/ajax', data)\n" + error.responseText);
	});
}
//getECRs();

var timeoutId;
$('#ecr_filter').on('change', 'input', function(){

	if(timeoutId)
		clearTimeout(timeoutId);

	timeoutId = setTimeout(
			function(){
				$('#ecr_accordion').empty();
				getECRs();
			},1000);
});

$('#ecr_show_more').click(getECRs);


function f_enableForward(node){

	var group	 = $(node).closest('.input-group');
	var button	 = group.find('button');

	if(node.value){

		button.attr('disabled', false);
		button.removeClass('disabled');

	}else{

		button.attr('disabled', true);
		button.addClass('disabled');
	}
}


// Check file sizes
function f_checkFiles(file){

	var maxSize = /*[[${maxSize}]]*/ 1048576;
	var maxRequest = /*[[${maxRequest}]]*/ 10485760;
	var totalSize = 0;
	var files = [];

	$(file).closest('.card-body').find('.file_upload').each(function(){

		if(this.files.length == 0)
			return false;

		var size = this.files[0].size;

		if(size == 0){
			alert('The file "' + this.files[0].name + '" is empty.');
			$(this).val(null).trigger('change');
			return false;
		}

		if(size > maxSize){
			alert('The file size is to big ("' + this.files[0].name + '"). Maximum allowed size is ' + maxSize + ' bytes');
			$(this).val(null).trigger('change');
			return false;
		}

		totalSize += size;
		if(totalSize > maxRequest){
			alert('The total files size is to big . Maximum allowed size is ' + maxRequest + ' bytes');
			$(this).val(null).trigger('change');
			return false;
		}

		files.push(this.files[0].name);
	});

	// Check for duplicate files
	if(files.filter((value,index)=>files.indexOf(value) === index).length !== files.length){
		alert('You have specified two or more links to the same file.');
		$(file).val(null).trigger('change');
	}
}
