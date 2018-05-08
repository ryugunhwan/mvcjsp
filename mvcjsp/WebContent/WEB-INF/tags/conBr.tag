<%@ tag language="java" body-content="scriptless" pageEncoding="UTF-8"%>
<jsp:doBody var="str" scope="page"/>
<%
	String str = (String)jspContext.getAttribute("str");
	String res = str.trim().replaceAll("\n", "<br>");
%>

<%=res %>
