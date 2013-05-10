package ibd.web.IBD50;

import ibd.web.Resource.LoadProperties;
import ibd.web.beans.StockData;
import ibd.web.classes.MarketDB;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataset;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
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
