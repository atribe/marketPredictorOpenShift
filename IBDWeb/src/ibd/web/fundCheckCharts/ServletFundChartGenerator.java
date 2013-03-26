/* -------------------------------
 * ServletChartGenerator.java
 * -------------------------------
 * (C) Copyright 2002-2004, by Object Refinery Limited.
 *
 */
package ibd.web.fundCheckCharts;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * A servlet that returns one of three charts as a PNG image file.  This servlet is
 * referenced in the HTML generated by ServletDemo2.
 * <P>
 * Three different charts can be generated, controlled by the 'type' parameter.  The possible
 * values are 'pie', 'bar' and 'time' (for time series).
 * <P>
 * This class is described in the JFreeChart Developer Guide.
 */
public class ServletFundChartGenerator extends HttpServlet {
    private String fund;

//    public static JFreeChart chart5=null;
//    public static JFreeChart chart10=null;
    /**
     * Default constructor.
     */
    public ServletFundChartGenerator() {
	// nothing required
    }

    /**
     * Process a GET request.
     *
     * @param request  the request.
     * @param response  the response.
     * @throws ServletException if there is a servlet related problem.
     * @throws IOException if there is an I/O problem.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {

	String timeString = request.getParameter("time");
	int time = 10;
	if(timeString==null || timeString.equalsIgnoreCase(""))
		time = 10; // by default duration of 10 years
	else
		time = Integer.valueOf(timeString);
	fund = request.getParameter("fund");
	if(fund==null || fund.equalsIgnoreCase(""))
		fund = "nflx"; // by default nflx
	
	ibd.web.Constants.Constants.logger.info("Here is the FUND in ServletFundChartGenerator.java"+fund);
//	System.out.println("HERE IS THE FUND! "+fund);
	Vector gains = GetFundData.getData(fund);
	//System.out.println("HERE ARE THE GAINS!!!!!!!!!!!!!!!!");
	ibd.web.Constants.Constants.logger.info("Here are the GAINS in ServletFundChartGenerator.java"+gains);
	//System.out.println(gains);

	JFreeChart chart = null;

	OutputStream out = null;
	try {
	    chart = ReturnFundChart.returnChart(time, gains, fund);

	    if (chart != null) {
			out = response.getOutputStream();
			response.setContentType("image/png");
			ChartUtilities.writeChartAsPNG(out, chart, 400, 300);
			out.flush();
			out.close();
	    } 
	    
	} catch (Exception ex) {
	    System.err.println(ex.toString());
	    ibd.web.Constants.Constants.logger.info("Exception in ServletFundChartGenerator.java"+ex);
	    //Logger.getLogger(GetFundData.class.getName()).log(Level.SEVERE, null, ex);
	    request.setAttribute("error",ex);
	    request.setAttribute("fund",fund);
	    RequestDispatcher rd = request.getRequestDispatcher("/error.do");
	    rd.forward(request, response);
	} 
    }
    
    /**
     * Process a POST request.
     *
     * @param request  the request.
     * @param response  the response.
     * @throws ServletException if there is a servlet related problem.
     * @throws IOException if there is an I/O problem.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
    	doGet(request,response);
    }
}
