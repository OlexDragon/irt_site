<%@page import="irt.table.OrderBy"%>
<%@page import="irt.data.Error"%>
<%@ page import="irt.data.manufacture.Manufacture"%>
<%@ page import="irt.data.dao.ManufactureDAO"%>
<%@ page import="java.util.List"%>
<jsp:useBean id="partNumber" scope="request" type="irt.data.partnumber.PartNumber" />
<%@ include file="inc/top.tag" %>

<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>
<div id="content">
	<hr /><h3 class="cBlue" ><%=partNumber.getComponent().getPartNumberF()+" - "+partNumber.getComponent().getDescription() %></h3><hr />
	<h4 class="cCenter">Create Alternative Manufacture Part Number</h4>
	<form id="ampn" method="post" action="alt-mfr-pns" >
	<p>
	Mfr PN: <input type="text" id="mfr-pn" name="mfr-pn" />
<% Manufacture[] all = new ManufactureDAO().getAll(new OrderBy("name"));%>
	Mfr: <select id="mfr" name="mfr">
			<option>Select</option>
<% for(Manufacture m:all){ %><option value="<%=m.getId()%>"><%=m.getName() %></option><% } %>
		</select>

		<input type="submit" id="submit-add" name="submit-add" value="Add" />
	</p>
	</form>

<%
	if(partNumber.isErrorMessage()){
%>
	<%=partNumber.getErrorMessage()%>
<% } %>
</div>

<%@ include file="inc/bottom.tag" %>
