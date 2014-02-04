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

