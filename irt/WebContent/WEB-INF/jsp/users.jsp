<%@page import="irt.data.dao.UserBeanDAO"%>
<%@page import="irt.data.Error"%>
<jsp:useBean id="userToEdit" scope="request" type="irt.data.user.UserBean" />
<jsp:useBean id="command" scope="request" type="java.lang.String" />
<jsp:useBean id="error" scope="request" type="irt.data.Error" />
<%@ include file="inc/top.tag" %>

<% isU = true; %>
<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>

<div id="content" class="cCenter">
<%	if((error.isError())){
%>		<h3 class="red" ><%=error.getErrorMessage()%></h3>
<%}
	if(ub.isUser() || !command.equals("non")){ %>
	<form id="users" method="post" action="/irt/users<%=command.equals("non") ? "" : "?command="+command%>">
<%		if((ub.isAdmin() || ub.isUserEdit()) && (userToEdit.getID()>0 || command.equals("New User"))){ %>
			<div class="sticker">
				<%@ include file="inc/users/user.tag" %>
			</div>
<%		}else{
			if(command.equals("user_name")){ %>
				<table class="sticker">
					<tr>
						<td>User Name:</td><th><%=ub.getUsername()%></th>
					</tr>
					<tr>
						<td><input type="submit" id="btn_username" name="btn_username" value="Chage Username" /></td><td><input type="submit" id="btn_password" name="btn_password" value="Chage Password" /></td>
					</tr>
				</table>
<%			}else if(command.equals("username")){%>
			<table class="sticker">
				<tr>
					<td>New User Name:</td><td><input type="text" name="u_username" id="u_username" value="<%=ub.getUsername()%>" /></td>
				</tr>
				<tr>
					<td>Password:</td><td><input type="password" name="u_password" id="u_password" /></td>
				</tr>
				<tr>
					<td></td><td><input type="submit" id="btn_submitUsername" name="btn_submitUsername" value="Submit" /></td>
				</tr>
			</table>
<%			}else if(command.equals("password")){%>
			<table class="sticker">
				<tr>
					<td>Old Password:</td><td><input type="password" name="u_password" id="u_password" /></td>
				</tr>
				<tr>
					<td>New Password:</td><td><input type="password" name="u_newPasword" id="u_newPassword" /></td>
				</tr>
				<tr>
					<td></td><td><input type="submit" id="btn_submitPassword" name="btn_submitPassword" value="Submit" /></td>
				</tr>
			</table>
<%			}else if(ub.isAdmin() || ub.isUserEdit()){
%>				<div><input type="submit" name="btn_newUser" id="btn_newUser" value="Add New User" /></div>
<%			}
		}
		if(ub.isUser() || ub.isUserEdit() || ub.isAdmin()){
%>	<div class="sticker">
		<%=new UserBeanDAO().getTable(ub.isUserEdit() || ub.isAdmin(), null) %>
	</div>
<%		}
%>	</form>
<%	}
%>
</div>

<%@ include file="inc/bottom.tag" %>
