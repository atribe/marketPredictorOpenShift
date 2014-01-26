<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="ibd.web.Constants.Constants"%>
<%@page import="java.util.Locale"%>

<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@page isELIgnored="false" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head><% System.out.println("Starting Head of Index.jsp"); %>
<title><spring:message code="label.applicationTitle" /></title>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="viewport" content="width=320; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">
	
	<link href="mpTheme/css/style.css" rel="stylesheet">
	
	<script src="http://code.createjs.com/easeljs-0.5.0.min.js"></script> 
	<script src="http://code.jquery.com/jquery-1.8.2.js"></script>
	
	<script src="js/base64.js" type="text/javascript"></script>
	<script src="js/canvas2image.js" type="text/javascript"></script>
	
	<!-- Current Locale : ${pageContext.response.locale}  -->	
	<% System.out.println("End Head of Index.jsp"); %>
</head>

<body>
	<div id="container">
	<% System.out.println("Start of Body of index.jsp"); %>
	<jsp:include page="mpHeader.jsp" />

	<c:if test="${ibd.web.Constants.Constants.jobRunning}">
		<div id="jobRunning"><spring:message code="label.jobRunning" />Test Me Too</div>
	</c:if>
	
	<c:if test="${(ibd.web.Constants.Constants.outputDow == null 
			|| ibd.web.Constants.Constants.outputNasdaq == null 
			|| ibd.web.Constants.Constants.outputSP500 == null) }">
		<div id="dataGeneratedError"><h1><spring:message code="label.dataGeneratedError" /></h1></div>		
	</c:if>
	
	<h2>Oh, and by the way, nothing below here works at the moment.</h2>
	<c:if test="${!ibd.web.Constants.Constants.jobRunning}">
		<div id="top50Links">
			<a href="showData50.do"><spring:message code="label.seeData50" /></a><br />
			<a href="showDataIndices50.do"><spring:message code="label.seeDataIndices50" /></a>
		</div>
	</c:if>

	<div id="fundChecker">
		<jsp:include page="fundChecker.jsp" />  
	</div>
	<div id="marketData">
		<!-- Currently Blank -->
		<jsp:include page="marketData.jsp" />
	</div>
	
	<c:if test="${ (ibd.web.Constants.Constants.outputDow!=null 
			&& ibd.web.Constants.Constants.outputDow.lastDataDate!=null) }">
		<div id="notSure">
			<div id="historicalMarketPredictorDates">
				<jsp:include page="historicalMarketPredictorDates.jsp" /> 
			</div>
			<div id="historicalMarketPredictorPerformance">
				<jsp:include page="historicalMarketPredictorPerformance.jsp" />
			</div>
			<div id="historicalPerformance">
				<jsp:include page="historicalPerformance.jsp" />
			</div>
		</div>
	</c:if>
	
	<div id="learningCenter">
	<jsp:include page="learningCenter.jsp" />
	</div>
	
	<jsp:include page="mpFooter.jsp" />
	</div> <!-- close div container -->
</body>
</html>

