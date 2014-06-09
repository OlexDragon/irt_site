<div id="bottom">
	<a href="mailto:oleksandr@irttechnologies.com"><small>oleksandr@irttechnologies.com</small></a>
</div>
<div id="to_top">
	<a id="top" href="#page_top">TO TOP</a>
</div>
<script type="text/javascript">
	$(document).ready(
			function() {
				$(document).mousemove(
						function(e) {
							var win = $(window).scrollTop();
							var top = $('#to_top');
							if (win == 0) {
								top.hide("slow");
							} else {
								top.show("slow");
								var height = parseInt(top.css("height")
										.replace("/\D/g", ""), 10);
								top.css({
									"top" : (e.pageY - win - height / 2) + "px"
								});
							}
						});
			});
</script>
</body>
</html>
