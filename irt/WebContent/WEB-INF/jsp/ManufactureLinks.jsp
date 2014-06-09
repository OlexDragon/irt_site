<jsp:useBean id="manuf" scope="request" type="irt.data.manufacture.Manufacture" />

<%@ include file="inc/top.tag" %>

<% isML = true; %>
<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>

<div id="content">
<%=manuf.getHTML()%>
<%	if((manuf.isError())){ %>
		<h3 class="red" ><%=manuf.getErrorMessage()%></h3>
<%}%>
<%=manuf.getTable()%>
</div>

<%@ include file="inc/bottom.tag" %>
