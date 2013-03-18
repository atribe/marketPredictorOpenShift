<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<TABLE width="100%" BORDER=15 BGCOLOR="white">
	    <TH><spring:message code="label.fundCheckerHeading" /></TH>
		    <TR>
				<TD>
				<form action="outputImage" method="post" target="fundChartFrame">
					<spring:message code="label.enterFunds" /><input type="text" name="fund" size="20" />
					<!--<spring:message code="label.enterTime" /><input type="text" name="time" size="20" /><spring:message code="label.exampleTime" /><br/>-->
					<input type="submit" value=<spring:message code="label.submit" /> align="center" />
				</form>
				<br>
				<!--an iframe is needed so the form stays visible even with a chart displayed.Otherwise the form would get written over-->
				<IFRAME marginheight="10" scrolling="no" height="350" NAME="fundChartFrame"
					WIDTH=100% marginwidth="10" frameborder="0">
				</IFRAME>
				</TD>
		    </TR>
</TABLE>