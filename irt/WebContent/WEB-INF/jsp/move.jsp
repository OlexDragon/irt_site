<%@page import="irt.data.companies.Company"%>
<%@page import="irt.data.components.movement.ComponentsMovement"%>
<%@page import="irt.web.ComponentsMovementServlet"%>
<%@page import="irt.data.Error"%>
<jsp:useBean id="cm" scope="request" type="irt.data.components.movement.ComponentsMovement" />
<%@ include file="inc/top.tag" %>

<%
	isCM = true;
	String description = cm.getDescription();
%>
<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>

<div id="content">
	<form method="post" action="/irt/components_movement">
	<p>
		Components moving from	<%=cm.getFrom()!=null	? cm.getFrom().getName(): cm.getPlacesHTML(cm.getFrom(),ComponentsMovementServlet.MOVE_FROM)%>
							 	<%=cm.showFromDetails() ? cm.getDetailsHTML(cm.getFrom(), cm.getFromDetail(),	ComponentsMovementServlet.MOVE_FROM_DETAIL) : ""%>
							 to <%=cm.getTo()!=null		? cm.getTo().getName()	: cm.getPlacesHTML(cm.getTo(),	ComponentsMovementServlet.MOVE_TO)%>
							 	<%=cm.showToDetails() ?  cm.getDetailsHTML(cm.getTo(),cm.getToDetail(),	ComponentsMovementServlet.MOVE_TO_DETAIL) : ""%>
		<input type="text" id="<%=ComponentsMovementServlet.TXT_DESCRIPTION%>" name="<%=ComponentsMovementServlet.TXT_DESCRIPTION%>" <%=description!=null ?  "value=\""+description+"\"" : ""%>/>
		<input type="submit" name="submit" id="submit" value="Submit" />
		<input type="submit" name="submit_reset" id="submit_reset" value="Reset" />
<%
	if(cm.isFromToChecked()){
%>
		<input type="submit" name="submit_move" id="submit_move" value="Move" />
<%
	}
%>
	<br /><input type="text" name="pn" id="pn" class="c8em" />
<script type="text/javascript">
	jQuery(function(){
		$("#pn").autocomplete("/irt/autocomplete/parse.jsp");
	});
</script>
		<input type="submit" name="submit_add" id="submit_add" value="Add" />
		<input type="submit" name="submit_history" id="submit_history" value="History" />
		<input type="submit" name="submit_create_kit" id="submit_create_kit" value="Create new KIT" onclick="return confirm('Do you want to Create New KIT?');" />
<%
	if(!cm.getDetails().isEmpty()){
%>			<input type="submit" name="submit_cancel" id="submit_cancel" value="Cancel" onclick="return confirm('Do you want to cansel?');" />
<%
	}
		if(cm.getTo()!=null && cm.getTo().getId()==Company.KIT && cm.getToDetail()!=null && cm.kitHasComponents()){
%>			<input type="submit" name="submit_view_kit" id="submit_view_kit" value="View KIT" />
			<input type="submit" name="submit_add_to_kit" id="submit_add_to_kit" value="Add to KIT" />
<%
	}
%>	</p>
<%
	Error e = cm.getErrorMessage();
	if(e.isError()){
%>
		<h4 class="red"><%=e.getErrorMessage()%></h4>
<%}%>
	<%=cm.getTable()%>
	</form>
	<p>	<span class="red" >Red - you ask more than in stock; </span>
		<span class="cBlue">Blue - about the same as in stock; </span>
		<span class="cGreen">Green - less then in stock;</span>
	</p>
</div>

<%@ include file="inc/bottom.tag" %>
