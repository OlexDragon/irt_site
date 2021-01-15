
var idValue = Cookies.get("id_input");

if(idValue)
	search($('#id_input').val(idValue));
else{
	var pnValue = Cookies.get("pn_input");
	var mfrPNValue = Cookies.get("mfrPN_input");
	var mfrValue = Cookies.get("mfr_input");
	var descriptionValue = Cookies.get("description_input");
	var valueValue = Cookies.get("value_input");
	
	$("#pn_input").val(pnValue);
	$("#mfrPN_input").val(mfrPNValue);
	$("#mfr_input").val(mfrValue);
	$("#description_input").val(descriptionValue);
	search($("#value_input").val(valueValue));
}

// Input listener
timer = 0;
$('.searchInput').on('input', function(){
    if (timer) {
    	clearTimeout(timer);
    }
    timer = setTimeout(search, 600, $(this));
});

$('select').change(function(){
	var $this = $(this);
	var id = $this.attr('id').replace('out', 'input');
	var $input = $('#' + id);
	$input.val($this.find("option:selected").text());
	search($input);
});

var $post;
function search($this){

	var attrId = $this.prop('id');

	if(attrId=='id_input'){
		$('#pn_input').val('');
		$('#mfrPN_input').val('');
		$('#mfr_input').val('');
		$('#description_input').val('');
		$('#value_input').val('');
		Cookies.remove('pn_input');
		Cookies.remove('mfrPN_input');
		Cookies.remove('mfr_input');
		Cookies.remove('description_input');
		Cookies.remove('value_input');
	}else{
		$('#id_input').val('');
		Cookies.remove('id_input');
	}

	Cookies.set(attrId, $this.val(), { expires: 7 });

	var id = $('#id_input').val();
	if(!id || id<=0){
		id = '';
		$('#id_input').val('');
	}

	var pn = $('#pn_input').val();
	var mfrPN = $('#mfrPN_input').val();
	var mfr = $('#mfr_input').val();
	var description = $('#description_input').val();
	var val = $('#value_input').val();
	var _csrf = $( "input[name='_csrf']" ).val();

	if(!(id || pn || mfrPN || mfr || description || val))
		return;

	if($post)
		$post.abort();

	$post = $.post('/search', {id : id, pn : pn, mfrPN : mfrPN, mfr : mfr, description : description, val : val, _csrf : _csrf})
	.done(function(data){
		var $ids = $('#id_out').empty();
		var $pns = $('#pn_out').empty();
		var $mfrPN = $('#mfrPN_out').empty();
		var $mfr = $('#mfr_out').empty();
		var $descr = $('#description_out').empty();
		var $val = $('#value_out').empty();

		var tableBody = $('#tableBody').empty();

		$(data).each(function(){

			var partNumber = partNumberAddDashes(this.partNumber);
			var manufPartNumber = this.manufPartNumber;


			$ids.append($('<option>', {value : this.id, text : this.id}));
			$pns.append($('<option>', {value : this.id, text : partNumber}));

			if(this.manufPartNumber) $mfrPN.append($('<option>', {value : this.id, text : manufPartNumber}));

			if(this.manufacture){
				if($mfr.find("option[value='"+ this.manufacture.id +"']").length==0)
					$mfr.append($('<option>', {value : this.manufacture.id, text : this.manufacture.name}));
			}

			if(this.description){
				if($descr.find("option[value='"+ this.description.replace(/(['"])/g, "\\$1") +"']").length==0)
					$descr.append($('<option>', {value : this.description, text : this.description, title : this.description}));
			}

			if(this.value){
				if($val.find("option[value='"+ this.value +"']").length==0)
					$val.append($('<option>', {value : this.value, text : this.value}));
			}

			var $anchor = $('<a>', {href : '\\', text : partNumber})
			$anchor.click(function(){
				Cookies.set("desiredPN", partNumber, { expires: 7 });
			});

			var $thPN = $('<th>').append($anchor);
			var $tdMfrPN = $('<td>');

			if(partNumber && partNumber.indexOf('PCA')==0){
				$anchorMfrPN = $('<a>', {href : '/bom', text : manufPartNumber, target : '_blank'});
				$anchorMfrPN.click(function(){
					Cookies.set("desiredPCA", partNumber, { expires: 7 });
				});
				$tdMfrPN.append($anchorMfrPN);
			}else{
				if(this.link)
					$tdMfrPN.append($('<a>', {href : 'http://irttechnologies:8080' + this.link.link, target : '_blank', text : this.manufPartNumber ? this.manufPartNumber : "N/A" }));
				else
					$tdMfrPN.text(manufPartNumber);
			}

			var row = $('<tr>')
					.append($('<td>', {text : this.id}))
					.append($thPN)
					.append($tdMfrPN)
					.append($('<td>', {text : this.manufacture ? this.manufacture.name : ''}))
					.append($('<td>', {text : this.description}))
					.append($('<td>', {text : this.qty}))
					.append($('<td>', {text : this.value}));

			if(this.componentObsolete!=null && this.componentObsolete.status=='OBSOLETE')
				row.addClass('obsolete');

			tableBody
			.append(row);
		});
	})
	.fail(function(error) {
		if(error.statusText!='abort')
			alert(error.responseText);
	});
}