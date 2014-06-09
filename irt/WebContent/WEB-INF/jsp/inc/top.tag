<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://www.w3.org/1999/xhtml
                          http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd"
>

<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />

	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="-1" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta name="revisit-after" content="1 day" />
	<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>

	<link rel="icon" type="image/ico" href="favicon.ico"></link> 
	<link rel="shortcut icon" href="favicon.ico"></link> 

	<title>IRT Technologies Inc.</title>
	<link rel="stylesheet" href="/irt/css/home.css" type="text/css" />
	<script src="/irt/js/onclick.js" type="text/javascript"></script>
	<script src="/irt/js/position.js" type="text/javascript" ></script>
	<script type="text/javascript" src="/irt/autocomplete/jQuery/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="/irt/autocomplete/jQuery/jquery.autocomplete.js"></script>	
</head>

<body onload="setScrollPosition()"> 

<div id="banner">
	<a href="/irt">
		<img src="/irt/images/logo.png" style="border-style:none" alt="IRT Technologies." />
		<img src="/irt/images/sloganimg.png" style="border-style:none" alt="Connecting the world together" />
	</a>
		<%@ page import="irt.data.user.LogIn"%>
		<%@ page import="java.security.GeneralSecurityException"%>
		<%@ page import="irt.data.user.UsersLogsIn" %>
		<%@ page import="irt.data.user.UserBean" %>
<%	UserBean ub = null;
	LogIn logIn = null;
	try {

		logIn = UsersLogsIn.getLogIn(request);
		if(logIn!=null)
			ub =  logIn.getUserBean();
		else
			ub = new UserBean();

	} catch (GeneralSecurityException e) { throw new RuntimeException(e); }
%>		<a id="name" href="/irt/users?command=user_name"><%=logIn!=null && logIn.isValid() ? ub.getFullName() : "" %></a>
</div>
<%	boolean isPN = false;
	boolean isAL = false;
	boolean isML = false;
	boolean isPS = false;
	boolean isCM = false;
	boolean isPO = false;
	boolean isU = false;
	boolean isH = false;
	boolean isLog = false;
	boolean isCost = false; %>

