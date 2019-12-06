var posting;
function getECOs(){

	if(posting) posting.abort();

	var data = {};
	data['start'] = $('#eco_accordion .card').length;
	data['size'] = 10;
	data['option'] = $('#eco_option').val();
	data['search'] = $('#eco_search').val();
	data['categories'] = [];
	$('#eco_search_form label.active').each(function(){
		data['categories'].push($(this).children('input').val());
	});

	posting = $.post("/engineering/eco/", data);
	posting.done(function(data){
		$('#eco_accordion').append(data);
	});
	posting.fail(function(error) {
		alert("$.post('/engineering/eco/', data)\n" + error.responseText);
	});
}
getECOs();

var timeoutId;
$('#eco_search_form').find(".onchange").change(function(){

	if(timeoutId)
		clearTimeout(timeoutId);

	timeoutId = setTimeout(
			function(){
				$('#eco_accordion').empty();
				getECOs();
			},1000);
});

$('#eco_show_more').click(getECOs);

// Enable/Disable '#eco_btn_reject'
$('#eco_accordion').on('input', 'textarea', function() {

	var index = this.id.replace('eco_comments', '');
	var textArea = $('#eco_btn_reject' + index);

	if(this.value){
		textArea.removeClass("disabled");
		textArea.attr("disabled", false);
    }else{
    	textArea.addClass("disabled");
		textArea.attr("disabled", true);
    }
});