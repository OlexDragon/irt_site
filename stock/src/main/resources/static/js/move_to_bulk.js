
$('#descriptionToBulk').on('input', enableButtun);

$('#qtyToBulk').on('input', function(){
	enableButtun();

	if(this.value){

		var val = parseInt(this.value);

		if(val > stockQty){	// stockQty -> from 'move_to_bulk.html' file
			this.value = stockQty;
			$(this).addClass('text-danger');
		}else
			$(this).removeClass('text-danger');
	}
});

$('#confirmBtn').click(function(){

	
	var action = '/component/bulk/';
	var _csrf = $( "input[name='_csrf']" ).val();
	var qty = $('#qtyToBulk').val();
	var descriptionVal = $('#descriptionToBulk').val();

	$.post( action, {_csrf : _csrf, qty : qty, componentId : componentId, description : descriptionVal})
	.done(function(){
		fillFields();
	})
	.fail(function(error) {
		alert(error.responseText);
	});
})

function enableButtun() {

	var description = $('#descriptionToBulk').val();
	var qtyToBulk = $('#qtyToBulk').val();

	if (description && qtyToBulk > 0)
		$('#confirmBtn').prop('disabled', false);
	else
		$('#confirmBtn').prop('disabled', true);
}
