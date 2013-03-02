<%@page import="ibd.web.Constants.Constants"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.GregorianCalendar"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<html>
<head>
<title><spring:message code="label.applicationTitle" /></title>
</head>

<body>
<%
	DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
	java.util.Date today = new java.util.Date();
	String dateOut = dateFormatter.format(today);
%>
		<h1><spring:message code="label.applicationTitle" />.com</h1>
		<h2><%=dateOut%></h2>
		
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
<%} if(ibd.web.Constants.Constants.outputDow!=null && ibd.web.Constants.Constants.outputDow.lastDataDate!=null){%>
	<jsp:include page="historicalMarketPredictorPerformance.jsp" />
<br/>
<%} if(ibd.web.Constants.Constants.outputDow!=null && ibd.web.Constants.Constants.outputDow.lastDataDate!=null){%>
	<jsp:include page="historicalPerformance.jsp" />
<br/>
<%} if(ibd.web.Constants.Constants.outputDow!=null && ibd.web.Constants.Constants.outputDow.lastDataDate!=null){%>
	<jsp:include page="marketData.jsp" />
<br/>
<%}%>
	<jsp:include page="learningCenter.jsp" />
</body>
</html>

