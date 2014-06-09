<%@page import="irt.data.user.UserBean"%>
<%	String newUsername = null;
	if(userToEdit.getUsername().isEmpty())
		newUsername = new UserBeanDAO().getNewUsername();
%>
<table>
	<tr><td>Username:</td><td><%=newUsername!=null ?  newUsername : userToEdit.getUsername() %></td></tr>
	<tr><td>First Name: </td><td><input type="text" id="u_<%=UserBean.FIRST_NAME%>" name="u_<%=UserBean.FIRST_NAME%>" value="<%=userToEdit.getFirstName()%>" /></td></tr>
	<tr><td>Last Name: </td><td><input type="text" id="u_<%=UserBean.LAST_NAME%>" name="u_<%=UserBean.LAST_NAME%>" value="<%=userToEdit.getLastName()%>" /></td></tr>
	<tr><td>Extension: </td><td><input type="text" id="u_<%=UserBean.EXTENSION%>" name="u_<%=UserBean.EXTENSION%>" value="<%=userToEdit.getExtension()%>" /></td></tr>
	<tr><td>e-male: </td><td><input type="text" id="u_<%=UserBean.E_MAIL%>" name="u_<%=UserBean.E_MAIL%>" value="<%=userToEdit.geteMail()%>" /></td></tr>
</table>
<% if(ub.isAdmin()){
%>		<%@ include file="admin.tag" %>
<% }
%><br />
<% 	if(newUsername!=null){
%>		<input type="submit" name="btn_add_<%=newUsername%>" id="btn_add_<%=newUsername%>" value="Add User"/>
<% }else{
%>		<input type="submit" name="btn_update_<%=userToEdit.getID()%>" id="btn_update_<%=userToEdit.getID()%>" value="Updete"/>
<% }
%>
