<%@page import="ibd.web.Constants.Constants"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.GregorianCalendar"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page isELIgnored="false" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><spring:message code="label.applicationTitle" /></title>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="viewport" content="width=320; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">
	
	<script src="http://code.createjs.com/easeljs-0.5.0.min.js"></script> 
	<script src="http://code.jquery.com/jquery-1.8.2.js"></script>
	
	<script src="js/base64.js" type="text/javascript"></script>
	<script src="js/canvas2image.js" type="text/javascript"></script>	
	
</head>

<body>

	<%if(ibd.web.Constants.Constants.jobRunning){ %>
		<font size="2" color="red"><spring:message code="label.jobRunning" /></font>
	<%} %>
		<h1><spring:message code="label.applicationTitle" /></h1>
		<h2>${dateOut}</h2>
		
<%
	    if (ibd.web.Constants.Constants.outputDow == null || ibd.web.Constants.Constants.outputNasdaq == null || ibd.web.Constants.Constants.outputSP500 == null) {
%>

		<h1><spring:message code="label.dataGeneratedError" /></h1>
		<!-- Current Locale : ${pageContext.response.locale}  -->

<%
		//return;
	    }
%>
<br/>
	<jsp:include page="fundChecker.jsp" />  
<br/> 
	<jsp:include page="marketData.jsp" />
<br/>
<% if(ibd.web.Constants.Constants.outputDow!=null && ibd.web.Constants.Constants.outputDow.lastDataDate!=null){%>
		<jsp:include page="historicalMarketPredictorDates.jsp" /> 
	<br/>
		<jsp:include page="historicalMarketPredictorPerformance.jsp" />
	<br/>
		<jsp:include page="historicalPerformance.jsp" />
	<br/>
<%}%>
	<jsp:include page="learningCenter.jsp" />
</body>
</html>

