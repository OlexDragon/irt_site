<%@page import="irt.data.dao.ComponentDAO"%>
<%
	String contains = request.getParameter("q");
response.setHeader("Content-Type", "text/html");

String[] r = new ComponentDAO().getPartNumberContain(contains.replaceAll("-", ""), 10);
if(r!=null)
	for(String s:r)
	out.print(s+"\n");
%>