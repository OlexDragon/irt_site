<%@ page import="irt.data.Error" %>
<jsp:useBean id="message" scope="request" type="java.lang.String" />
<jsp:useBean id="error" scope="request" type="irt.data.Error" />

<%@ include file="inc/top.tag" %>

<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>

<div id="content">
	<hr /><h3>IRT Add Link.</h3><hr />

	<%=message%>

	<%
		if(error.isError()){
	%>
			<%=error.getErrorMessage()%>
	<%}%>
</div>

<%@ include file="inc/bottom.tag" %>
