<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<table width="100%" border="15" bgcolor="white" >
	    <tr>
		    <td width="35%"><h3><spring:message code="label.historicalPerformance" /></h3></td>
		    <td><h3><spring:message code="label.SP500" /></h3></td><td><h3><spring:message code="label.Dow" /></h3></td>
		    <td><h3><spring:message code="label.Nasdaq" /></h3></td>
	    </tr>
	    <tr>
		    <td><spring:message code="label.fiveYearMarketGainLoss" /></td>
		    <td><%=ibd.web.Constants.Constants.outputSP500.histReturns[0][0]%><br><%=ibd.web.Constants.Constants.outputSP500.histReturns[0][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputDow.histReturns[0][0]%><br><%=ibd.web.Constants.Constants.outputDow.histReturns[0][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[0][0]%><br><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[0][1]%></td>
	    </tr>
	    <tr>
		    <td><spring:message code="label.tenYearMarketGainLoss" /></td>
		    <td><%=ibd.web.Constants.Constants.outputSP500.histReturns[1][0]%><br><%=ibd.web.Constants.Constants.outputSP500.histReturns[1][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputDow.histReturns[1][0]%><br><%=ibd.web.Constants.Constants.outputDow.histReturns[1][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[1][0]%><br><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[1][1]%></td>
	    </tr>
	    <tr>
		    <td><spring:message code="label.fifteenYearMarketGainLoss" /></td>
		    <td><%=ibd.web.Constants.Constants.outputSP500.histReturns[2][0]%><br><%=ibd.web.Constants.Constants.outputSP500.histReturns[2][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputDow.histReturns[2][0]%><br><%=ibd.web.Constants.Constants.outputDow.histReturns[2][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[2][0]%><br><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[2][1]%></td>
	    </tr>
	    <tr>
		    <td><spring:message code="label.twentyYearMarketGainLoss" /></td>
		    <td><%=ibd.web.Constants.Constants.outputSP500.histReturns[3][0]%><br><%=ibd.web.Constants.Constants.outputSP500.histReturns[3][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputDow.histReturns[3][0]%><br><%=ibd.web.Constants.Constants.outputDow.histReturns[3][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[3][0]%><br><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[3][1]%></td>
	    </tr>
	    <tr>
		    <td><spring:message code="label.thirtyYearMarketGainLoss" /></td>
		    <td><%=ibd.web.Constants.Constants.outputSP500.histReturns[4][0]%><br><%=ibd.web.Constants.Constants.outputSP500.histReturns[4][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputDow.histReturns[4][0]%><br><%=ibd.web.Constants.Constants.outputDow.histReturns[4][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[4][0]%><br><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[4][1]%></td>
	    </tr>
	    <tr>
		    <td><spring:message code="label.fortyYearMarketGainLoss" /></td>
		    <td><%=ibd.web.Constants.Constants.outputSP500.histReturns[5][0]%><br><%=ibd.web.Constants.Constants.outputSP500.histReturns[5][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputDow.histReturns[5][0]%><br><%=ibd.web.Constants.Constants.outputDow.histReturns[5][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[5][0]%><br><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[5][1]%></td>
	    </tr>
	    <tr>
		    <td><spring:message code="label.fiftyYearMarketGainLoss" /></td>
		    <td><%=ibd.web.Constants.Constants.outputSP500.histReturns[6][0]%><br><%=ibd.web.Constants.Constants.outputSP500.histReturns[6][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputDow.histReturns[6][0]%><br><%=ibd.web.Constants.Constants.outputDow.histReturns[6][1]%></td>
		    <td><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[6][0]%><br><%=ibd.web.Constants.Constants.outputNasdaq.histReturns[6][1]%></td>
	    </tr>
	    <tr>
	    	<td><spring:message code="label.buyAndSellDays" /></td>
	    <td>
		    <table width="100%" border="0">
				<tr>
					<td><spring:message code="label.buyDays" /></td><td><spring:message code="label.sellDays" /></td>
				</tr>
					<% int i = ibd.web.Constants.Constants.outputSP500.buyDates.length - 1;
						    while (i >= 0) {
							try {
					%>
				<tr>
					<td><%=ibd.web.Constants.Constants.outputSP500.buyDates[i]%></td>
					<td><%=ibd.web.Constants.Constants.outputSP500.sellDates[i]%></td>
				</tr>
					<% i--;
							} catch (IndexOutOfBoundsException e) {
							    break;
							}
						    }
					%>
		    </table>
		</td>
		<td>
		    <table width="100%" border="0">
				<tr>
					<td><spring:message code="label.buyDays" /></td><td><spring:message code="label.sellDays" /></td>
				</tr>
					<% i = ibd.web.Constants.Constants.outputDow.buyDates.length - 1;
						    while (i >= 0) {
							try {
					%>
				<tr>
					<td><%=ibd.web.Constants.Constants.outputDow.buyDates[i]%></td>
					<td><%=ibd.web.Constants.Constants.outputDow.sellDates[i]%></td>
				</tr>
					<% i--;
							} catch (IndexOutOfBoundsException e) {
							    break;
							}
						    }
					%>
		    </table>
		</td>
		<td>
		    <table width="100%" border="0">
				<tr>
					<td><spring:message code="label.buyDays" /></td><td><spring:message code="label.sellDays" /></td>
				</tr>
					<%i = ibd.web.Constants.Constants.outputNasdaq.buyDates.length - 1;
						    while (i >= 0) {
							try {
					%>
				<tr>
					<td><%=ibd.web.Constants.Constants.outputNasdaq.buyDates[i]%></td>
					<td><%=ibd.web.Constants.Constants.outputNasdaq.sellDates[i]%></td>
				</tr>
					<% i--;
							} catch (IndexOutOfBoundsException e) {
							    break;
							}
						    }
					%>
		    </table>
		</td>
</table>