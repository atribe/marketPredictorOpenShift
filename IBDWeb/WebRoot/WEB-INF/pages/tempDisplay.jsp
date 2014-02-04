<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="ibd.web.Constants.Constants"%>
<%@page import="java.util.Locale"%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@page isELIgnored="false" %>

<jsp:include page="mpHead.jsp" />

<body>
	<div id="container">
	<% System.out.println("Start of Body Tag"); %>
	<jsp:include page="mpHeader.jsp" />
	
	<jsp:include page="mpFooter.jsp" />
	</div> <!-- close div container -->
</body>
</html>