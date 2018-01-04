
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<style type="text/css">
.auto-style1 {
	text-align: center;
	font-size: x-large;
}
</style>


<title>Multi-Objective navigator</title>
</head>
<%
	    out.println("<h2> Select problem file to load </h2>");
    	out.println("<form name='form1' id='form1' action='loadData' method='post' enctype='multipart/form-data'> ");
    	out.println("<br>");
		out.println("<input type='file' size='50' name='dataFile'><br><br>");
		out.println("<input type='submit' value='Upload File'>");
		out.println("</form>");
		
%>
</body>
</html>