
<%@page import="irt.data.dao.HistoryDAO"%>
<%@ include file="inc/top.tag" %>

<% isH = true; %>
<div id="static_menu">
<%@ include file="inc/staticMenu.tag" %>
</div>

<div id="content">
<%=new HistoryDAO().getHistoryTable(300) %>
</div>

<%@ include file="inc/bottom.tag" %>
