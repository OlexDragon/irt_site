<%@page import="irt.data.Error"%>
<jsp:useBean id="checked" scope="request" type="java.lang.Boolean" />
<jsp:useBean id="error" scope="request" type="irt.data.Error" />

<%@ include file="inc/top.tag" %>

<% isLog = true; %>
<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>

<div id="content">
	<form id="log" method="post" action="/irt/login">
<div id="loging_box">
		<label>Please enter your username</label>
			<input type="text" name="un" value="<%=checked ? logIn.getUsername() : "" %>" /><br />
		<label>Please enter your password</label>
			<input type="password" name="pw" value="<%=checked ? logIn.getPassword() : "" %>" />
			
		<input type="submit" name="submit_log" id="submit_log" value="submit" />

		<label for="remember" style="cursor: pointer;">Remember</label>
		<input type="checkbox" id="remember" name="remember" value="Remember"
<%	if(checked){
%>			checked="checked"
<%	} %>	style="cursor: pointer;" />
</div>
<script type="text/javascript">
	function one(){
		document.getElementById("submit_log").click();
	}
</script>
	</form>
<%
	if((error.isError())){
%>
		<h3 class="red" ><%=error.getErrorMessage()%></h3>
<%}%>

</div>

<%@ include file="inc/bottom.tag" %>
