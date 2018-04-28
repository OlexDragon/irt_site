$('#coMfr').change(enableButtun);
$('#qtyToCoMfr').on('input', enableButtun);
$('#descriptionCoMfr').on('input', enableButtun);

$('#confirmBtn').click(function(){

	var action = '/component/to_co_mfr';
	var _csrf = $("input[name='_csrf']").val();
	var qty = $('#qtyToCoMfr').val();
	var coMfr = $('#coMfr option:selected').val();
	var descriptionVal = $('#descriptionCoMfr').val();

	$.post(action, {
		_csrf : _csrf,
		qty : qty,
		componentId : componentId,
		companyId : coMfr,
		description : descriptionVal

	}).done(function() {
		fillFields(); //from 'irt.js' file
	}).fail(function(error) {
		alert(error.responseText);
	})

})

function enableButtun() {

	var coMfr = $('#coMfr option:selected').val();
	var description = $('#descriptionCoMfr').val();
	var qtyToCoMfr = $('#qtyToCoMfr').val();

	if (coMfr && description && qtyToCoMfr > 0)
		$('#confirmBtn').prop('disabled', false);
	else
		$('#confirmBtn').prop('disabled', true);
}
