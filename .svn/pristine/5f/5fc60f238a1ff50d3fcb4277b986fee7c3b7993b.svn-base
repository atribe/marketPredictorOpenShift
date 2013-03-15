<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<% if(ibd.web.Constants.Constants.outputDow!=null && ibd.web.Constants.Constants.outputDow.lastDataDate!=null){%>
<table width="100%" bgcolor="pink" border="15">
	    <th><spring:message code="label.todayMarketData" /></th>
		    <tr>
			    <td width="35%"><h3>Data for <% if(ibd.web.Constants.Constants.outputDow!=null && ibd.web.Constants.Constants.outputDow.lastDataDate!=null){%><%=ibd.web.Constants.Constants.outputDow.lastDataDate%><%} %></h3></td>
			    <td><h3><spring:message code="label.SP500" /></h3></td><td><h3><spring:message code="label.Dow" /></h3></td>
			    <td><h3><spring:message code="label.Nasdaq" /></h3></td>
		    </tr>
		    <%if(ibd.web.Constants.Constants.outputSP500.buyOrSellToday!=null && ibd.web.Constants.Constants.outputDow.buyOrSellToday!=null && ibd.web.Constants.Constants.outputNasdaq.buyOrSellToday!=null){ %>
		    <tr>
			    <td><h3><spring:message code="label.marketCondition" /></h3></td>
			    <td><h3><%=ibd.web.Constants.Constants.outputSP500.buyOrSellToday%></h3></td>
			    <td><h3><%=ibd.web.Constants.Constants.outputDow.buyOrSellToday%></h3></td>
			    <td><h3><%=ibd.web.Constants.Constants.outputNasdaq.buyOrSellToday%></h3></td>
		    </tr>
		    <%}
		    	if(ibd.web.Constants.Constants.outputSP500.lastBuyDate!=null && ibd.web.Constants.Constants.outputDow.lastBuyDate!=null && ibd.web.Constants.Constants.outputNasdaq.lastBuyDate!=null){ %>
		    <tr>
			    <td><spring:message code="label.lastBuyDay" /></td>
			    <td><%=ibd.web.Constants.Constants.outputSP500.lastBuyDate%></td>
			    <td><%=ibd.web.Constants.Constants.outputDow.lastBuyDate%></td>
			    <td><%=ibd.web.Constants.Constants.outputNasdaq.lastBuyDate%></td>
		    </tr>
		    <%} 
		    	if(ibd.web.Constants.Constants.outputSP500.lastSellDate!=null && ibd.web.Constants.Constants.outputDow.lastSellDate!=null && ibd.web.Constants.Constants.outputNasdaq.lastSellDate!=null){ %>
		    <tr>
			    <td><spring:message code="label.lastSellDay" /></td>
			    <td><%=ibd.web.Constants.Constants.outputSP500.lastSellDate%></td>
			    <td><%=ibd.web.Constants.Constants.outputDow.lastSellDate%></td>
			    <td><%=ibd.web.Constants.Constants.outputNasdaq.lastSellDate%></td>
		    </tr>
		    <tr>
			    <td><spring:message code="label.potentialMarketBottom" /></td>
			    <td><%=ibd.web.Constants.Constants.outputSP500.rallyDaysToday%></td>
			    <td><%=ibd.web.Constants.Constants.outputDow.rallyDaysToday%></td>
			    <td><%=ibd.web.Constants.Constants.outputNasdaq.rallyDaysToday%></td>
		    </tr>
		    <tr>
			    <td><spring:message code="label.percentageToMarketTop" /></td>
			    <td><%=ibd.web.Constants.Constants.outputSP500.dDaysPerc%>%</td>
			    <td><%=ibd.web.Constants.Constants.outputDow.dDaysPerc%>%</td>
			    <td><%=ibd.web.Constants.Constants.outputNasdaq.dDaysPerc%>%</td>
		    </tr>
		    <%} %>
</table>
<%} %>