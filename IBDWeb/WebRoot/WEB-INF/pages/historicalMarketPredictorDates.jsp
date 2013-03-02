<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<table width="100%" bgcolor="red" border="15">
	    <th><spring:message code="label.historicalBuyAndSellDates" /></th>
	    <tr>
		    <td>
			    <img align="center" alt="<spring:message code="label.imageAlt" />" src="historyChartGenerator?fund=SP500" height="300" width="100%" border="1" />
			    <img align="center" alt="<spring:message code="label.imageAlt" />" src="historyChartGenerator?fund=DOW" height="300" width="100%" border="1" />
			    <img align="center" alt="<spring:message code="label.imageAlt" />" src="historyChartGenerator?fund=NAS" height="300" width="100%" border="1" />
			</td>
		</tr>
</table>