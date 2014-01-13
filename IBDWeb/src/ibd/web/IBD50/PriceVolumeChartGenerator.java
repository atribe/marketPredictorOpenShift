package ibd.web.IBD50;

import ibd.web.beans.StockData;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

/**
 * Servlet implementation class PriceVolumeChartGenerator
 */
public class PriceVolumeChartGenerator extends HttpServlet {
	private static final long serialVersionUID = 1L;



	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PriceVolumeChartGenerator() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String index = request.getParameter("fund");
		List<StockData> stockData = null;
		DrawChart obj = new DrawChart();
		stockData = obj.getDataFrom(index);// Get All Data From the Table
		JFreeChart chart = obj.createChart(stockData, index);
		//writeImage(chart,index);
		OutputStream out = null;
		try {
			if (chart != null) {
				out = response.getOutputStream();
				response.setContentType("image/png");
				ChartUtilities.writeChartAsPNG(out, chart, 1000, 600);    			
				out.flush();
				out.close();
			} 

		} catch (Exception ex) {
			System.err.println(ex.toString());
			ibd.web.Constants.Constants.logger.info("Exception in PriceVolumeChartGenerator.java"+ex);
			//Logger.getLogger(GetFundData.class.getName()).log(Level.SEVERE, null, ex);
			RequestDispatcher rd = request.getRequestDispatcher("/error.do");
			rd.forward(request, response);
		}/* 
        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 800, 600);*/
	}

}
