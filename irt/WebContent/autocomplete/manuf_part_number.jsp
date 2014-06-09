<%@page import="irt.data.dao.ComponentDAO"%>
<%
String classId = request.getParameter("classId");
//jQuery related start
String contains = (String)request.getParameter("q");
response.setHeader("Content-Type", "text/html");

String[] r = new ComponentDAO().getMfrPartNumbet(classId, contains, 10);
if(r!=null)
	for(String s:r)
	out.print(s+"\n");
%>