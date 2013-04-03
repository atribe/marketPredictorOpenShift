<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page isELIgnored="false" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><spring:message code="label.applicationTitle" /></title>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="viewport" content="width=320; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;">	
	
	<style>
	table a:link {
	color: #666;
	font-weight: bold;
	text-decoration:none;
	}
	table a:visited {
		color: #999999;
		font-weight:bold;
		text-decoration:none;
	}
	table a:active,
	table a:hover {
		color: #bd5a35;
		text-decoration:underline;
	}
	table {
		font-family:Arial, Helvetica, sans-serif;
		color:#666;
		font-size:12px;
		text-shadow: 1px 1px 0px #fff;
		background:#eaebec;
		margin:20px;
		border:#ccc 1px solid;
	
		-moz-border-radius:3px;
		-webkit-border-radius:3px;
		border-radius:3px;
	
		-moz-box-shadow: 0 1px 2px #d1d1d1;
		-webkit-box-shadow: 0 1px 2px #d1d1d1;
		box-shadow: 0 1px 2px #d1d1d1;
	}
	table th {
		padding:21px 25px 22px 25px;
		border-top:1px solid #fafafa;
		border-bottom:1px solid #e0e0e0;
	
		background: #ededed;
		background: -webkit-gradient(linear, left top, left bottom, from(#ededed), to(#ebebeb));
		background: -moz-linear-gradient(top,  #ededed,  #ebebeb);
	}
	table th:first-child {
		text-align: left;
		padding-left:20px;
	}
	table tr:first-child th:first-child {
		-moz-border-radius-topleft:3px;
		-webkit-border-top-left-radius:3px;
		border-top-left-radius:3px;
	}
	table tr:first-child th:last-child {
		-moz-border-radius-topright:3px;
		-webkit-border-top-right-radius:3px;
		border-top-right-radius:3px;
	}
	table tr {
		text-align: center;
		padding-left:20px;
	}
	table td:first-child {
		text-align: left;
		padding-left:20px;
		border-left: 0;
	}
	table td {
		padding:18px;
		border-top: 1px solid #ffffff;
		border-bottom:1px solid #e0e0e0;
		border-left: 1px solid #e0e0e0;
	
		background: #fafafa;
		background: -webkit-gradient(linear, left top, left bottom, from(#fbfbfb), to(#fafafa));
		background: -moz-linear-gradient(top,  #fbfbfb,  #fafafa);
	}
	table tr.even td {
		background: #f6f6f6;
		background: -webkit-gradient(linear, left top, left bottom, from(#f8f8f8), to(#f6f6f6));
		background: -moz-linear-gradient(top,  #f8f8f8,  #f6f6f6);
	}
	table tr:last-child td {
		border-bottom:0;
	}
	table tr:last-child td:first-child {
		-moz-border-radius-bottomleft:3px;
		-webkit-border-bottom-left-radius:3px;
		border-bottom-left-radius:3px;
	}
	table tr:last-child td:last-child {
		-moz-border-radius-bottomright:3px;
		-webkit-border-bottom-right-radius:3px;
		border-bottom-right-radius:3px;
	}
	table tr:hover td {
		background: #f2f2f2;
		background: -webkit-gradient(linear, left top, left bottom, from(#f2f2f2), to(#f0f0f0));
		background: -moz-linear-gradient(top,  #f2f2f2,  #f0f0f0);	
	}
	
	
	/*
	For Combo Box
	*/
	.styled-select select {
	   background: transparent;
	   width: 268px;
	   padding: 5px;
	   font-size: 16px;
	   line-height: 1;
	   border: 0;
	   border-radius: 0;
	   height: 34px;
	   -webkit-appearance: none;
	   }
	   .styled-select {
		   width: 240px;
		   height: 34px;
		   overflow: hidden;
		   background: url(images/down_arrow_select.png) no-repeat right #ddd;
		   border: 1px solid #ccc;
		}
	</style>
	
</head>

<body>

<form id="datesForm" name="datesForm" method="post" action="showData50.do">
<spring:message code="label.dataDate" />     <b>   ${model.showDate}</b>
<br/><br/>
<div class="styled-select">
	<spring:message code="label.archivedData" /><select id="allDates" name="allDates" onchange="datesForm.submit();">
		<c:forEach var="singleDate" items="${model.allDates}">
			<option value='<c:out value="${singleDate}" />'><c:out value="${singleDate}" /></option>
		</c:forEach>
	</select>
</div>
<br/>
<a href="index.do">GO BACK!!!</a>
<br/>
</form>
	<table cellspacing='0'>
	<thead>
	     <tr>
	     	<th><spring:message code="label.tableTitle" /></th>
	        <th><spring:message code="label.rank" /></th>
	        <th><spring:message code="label.companyName" /></th>
	        <th><spring:message code="label.symbol" /></th>
	        <th><spring:message code="label.smartSelectCompositeRating" /></th>
	        <th><spring:message code="label.epsRating" /></th>
	        <th><spring:message code="label.rsRating" /></th>
	        <th><spring:message code="label.indGroupRelativeStrength" /></th>
	        <th><spring:message code="label.smrRating" /></th>
	        <th><spring:message code="label.accDis" /></th>
	        <th><spring:message code="label.weekHigh52" /></th>
	        <th><spring:message code="label.closingPrice" /></th>
	        <th><spring:message code="label.dollarChange" /></th>
	        <th><spring:message code="label.volChange" /></th>
	        <th><spring:message code="label.volume" /></th>
	        <th><spring:message code="label.pe" /></th>
	        <th><spring:message code="label.sponRating" /></th>
	        <th><spring:message code="label.divYield" /></th>
	        <th><spring:message code="label.offHigh" /></th>
	        <th><spring:message code="label.annualEpsEstChange" /></th>
	        <th><spring:message code="label.lastQtrEpsChange" /></th>
	        <th><spring:message code="label.nextQtrEpsChange" /></th>
	        <th><spring:message code="label.lastQtrSalesChange" /></th>
	        <th><spring:message code="label.roe" /></th>
	        <th><spring:message code="label.pretaxmargin" /></th>
	        <th><spring:message code="label.managementOwns" /></th>
	        <th><spring:message code="label.qtrEpsCountGreaterThan15" /></th>
	        <th><spring:message code="label.description" /></th>
	        <th><spring:message code="label.footNote" /></th>
	    </tr> 
	</thead>
	    <% int counter = 1;%>
	    <c:forEach var="data50" items="${model.data50List}">
	    <tbody>
	        <tr <% if(counter%2==0){ %>class="even"<%} %>>
	        	<td><%=counter %></td><%counter++; %>
				<td>${data50.rank}</td>
				<td>${data50.companyName}</td>
				<td>${data50.symbol}</td>
				<td>${data50.smartSelectCompositeRating}</td>
				<td>${data50.epsRating}</td>
				<td>${data50.rsRating}</td>
				<td>${data50.indGroupRelativeStrength}</td>
				<td>${data50.smrRating}</td>
				<td>${data50.accDis}</td>
				<td>${data50.weekHigh52}</td>
				<td>${data50.closingPrice}</td>
				<td>${data50.dollarChange}</td>
				<td>${data50.volChange}</td>
				<td>${data50.volume}</td>
				<td>${data50.pe}</td>
				<td>${data50.sponReading}</td>
				<td>${data50.divYield}</td>
				<td>${data50.offHigh}</td>
				<td>${data50.annualEpsEstChange}</td>
				<td>${data50.lastQtrEpsChange}</td>
				<td>${data50.nextQtrEpsChange}</td>
				<td>${data50.lastQtrSalesChange}</td>
				<td>${data50.roe}</td>
				<td>${data50.pretaxmargin}</td>
				<td>${data50.managementOwns}</td>
				<td>${data50.qtrEpsCountGreaterThan15}</td>
				<td>${data50.description}</td>
				<td>${data50.footNote}</td>
	        </tr>
	 </tbody>
	    </c:forEach>
	</table>
</body>
</html>

