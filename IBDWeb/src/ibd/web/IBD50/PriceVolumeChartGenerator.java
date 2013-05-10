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
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
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
import org.jfree.data.xy.XYDataset;

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
		stockData = getDataFrom(index);// Get All Data From the Table
        JFreeChart chart = createChart(stockData, index);
        //writeImage(chart,index);
        OutputStream out = null;
    	try {
    	    if (chart != null) {
    			out = response.getOutputStream();
    			response.setContentType("image/png");
    			ChartUtilities.writeChartAsPNG(out, chart, 800, 600);
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
	
	

	
	private static OHLCDataset createPriceDataset(List<StockData> stockData, String tableName) {

        // the following data is taken from http://finance.yahoo.com/
        // for demo purposes...
        OHLCSeries s1 = new OHLCSeries(tableName);
        for(int i=0;i<stockData.size();i++){
        	StockData obj = stockData.get(i);
        	String dated = obj.getDataDate();
            s1.add(new Day(Integer.parseInt(dated.substring(8)), Integer.parseInt(dated.substring(5,7)), Integer.parseInt(dated.substring(0,4))), obj.getOpen(), obj.getHigh(), obj.getLow(), obj.getClose());
        }

        OHLCSeriesCollection dataset = new OHLCSeriesCollection();
        dataset.addSeries(s1);
        return dataset;

    }

    /**
     * Creates a sample dataset.
     *
     * @return A sample dataset.
     */
    private static IntervalXYDataset createVolumeDataset(List<StockData> stockData) {

        // create dataset 2...
        TimeSeries s1 = new TimeSeries("Volume");
        TimeSeries s2 = new TimeSeries("Volume");
        for(int i=0;i<stockData.size();i++){
        	StockData obj = stockData.get(i);
        	String dated = obj.getDataDate();
        	if(0 != i && obj.getClose()<stockData.get(i-1).getClose()){
        		s1.add(new Day(Integer.parseInt(dated.substring(8)), Integer.parseInt(dated.substring(5,7)), Integer.parseInt(dated.substring(0,4))), obj.getVolume());
        	}else{
        		s2.add(new Day(Integer.parseInt(dated.substring(8)), Integer.parseInt(dated.substring(5,7)), Integer.parseInt(dated.substring(0,4))), obj.getVolume());
        	}
        }
        
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        dataSet.addSeries(s2);
        dataSet.addSeries(s1);
        return dataSet;
        //return new TimeSeriesCollection(s1);

    }
	
	private static JFreeChart createChart(List<StockData> stockData, String tableName) {

        OHLCDataset priceData = createPriceDataset(stockData,tableName);
        String title = "OHLC-Volume Chart for "+tableName.toUpperCase();
        JFreeChart chart = ChartFactory.createHighLowChart(title, "Date",
                "Price", priceData, true);
        XYPlot plot = (XYPlot) chart.getPlot();
        DateAxis xAxis = (DateAxis) plot.getDomainAxis();
        xAxis.setLowerMargin(0.01);
        xAxis.setUpperMargin(0.01);
        NumberAxis rangeAxis1 = (NumberAxis) plot.getRangeAxis();
        rangeAxis1.setLowerMargin(0.60);  // to leave room for volume bars
        rangeAxis1.setAutoRangeIncludesZero(false);

        XYItemRenderer renderer1 = plot.getRenderer();
        renderer1.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));

        NumberAxis rangeAxis2 = new NumberAxis("Volume");
        rangeAxis2.setUpperMargin(1.00);  // to leave room for price line
        plot.setRangeAxis(1, rangeAxis2);
        IntervalXYDataset volumeData = createVolumeDataset(stockData);
        plot.setDataset(1, volumeData);
        plot.setRangeAxis(1, rangeAxis2);
        plot.mapDatasetToRangeAxis(1, 1);
        XYBarRenderer renderer2 = new XYBarRenderer();
        renderer2.setDrawBarOutline(false);
        renderer2.setBaseToolTipGenerator(
            new StandardXYToolTipGenerator(
                StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
                new SimpleDateFormat("d-MMM-yyyy"),
                new DecimalFormat("0,000.00")));
        
        /*XYDataset dataset2 = MovingAverage.createMovingAverage(
                priceData, "-MAVG", 50 * 24 * 60 * 60 * 1000L, 0L);
            XYDataset dataset3 = MovingAverage.createMovingAverage(
                    priceData, "-MAVG", 200 * 24 * 60 * 60 * 1000L, 0L);
            plot.setDataset(1, dataset2);
            plot.setRenderer(1, new StandardXYItemRenderer());
            plot.setDataset(2, dataset3);
            plot.setRenderer(2, new StandardXYItemRenderer());*/
        XYDataset dataset2 = MovingAverage.createMovingAverage(priceData, "-50PMAVG", 50 * 24 * 60 * 60 * 1000L, 0L);
        plot.setDataset(2,dataset2);
        XYDataset dataset3 = MovingAverage.createMovingAverage(priceData, "-200PMAVG", 200 * 24 * 60 * 60 * 1000L, 0L);
        plot.setDataset(3,dataset3);
        /*XYDataset dataset4 = MovingAverage.createMovingAverage(volumeData, "-VMAVG", 100 * 24 * 60 * 60 * 1000L, 0L);
        plot.setDataset(4,dataset4);*/
        ChartUtilities.applyCurrentTheme(chart);
        renderer2.setShadowVisible(false);
        renderer2.setBarPainter(new StandardXYBarPainter());
        /*XYDataset dataset4 = MovingAverage.createMovingAverage(volumeData, "-VMAVG", 50 * 24 * 60 * 60 * 1000L, 0L);
        plot.setDataset(4,dataset4);*/
        renderer2.setSeriesPaint(1, Color.RED);
        //renderer2.setSeriesPaint(1, Color.GREEN);
        plot.setRenderer(1, renderer2);
        plot.setRenderer(2, new StandardXYItemRenderer());
        plot.setRenderer(3, new StandardXYItemRenderer());
        //plot.setRenderer(4, new StandardXYItemRenderer());
        return chart;

    }
	
	private static List<StockData> getDataFrom(String tableName){
		List<StockData> data = new ArrayList<StockData>();
		Connection con = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  try{
			  con = MarketDB.getConnectionIBD50PricesVolumes();
			  stmt = con.createStatement();
			  String query = "SELECT * FROM `^"+tableName.toLowerCase()+"`";
			  rs = stmt.executeQuery(query);
			  while(rs.next()){
				  StockData obj = new StockData();
				  obj.setDataDate(rs.getString(1));
				  obj.setOpen(rs.getFloat(2));
				  obj.setHigh(rs.getFloat(3));
				  obj.setLow(rs.getFloat(4));
				  obj.setClose(rs.getFloat(5));
				  obj.setVolume(rs.getDouble(6));
				  data.add(obj);
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{
			  try{
				  stmt.close();
				  con.close();
				  rs.close();
			  }catch(Exception e){
				  e.printStackTrace();
			  }
		  }
		return data;
	}
	
	
	public void writeImage(JFreeChart chart,String fund){
		BufferedImage objBufferedImage=chart.createBufferedImage(600,800);
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		        try {
		            ImageIO.write(objBufferedImage, "png", bas);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }

		byte[] byteArray=bas.toByteArray();
		try{
			InputStream in = new ByteArrayInputStream(byteArray);
			BufferedImage image = ImageIO.read(in);
			File outputfile = new File(LoadProperties.serverPath+"/images/image"+fund+".png");
			ImageIO.write(image, "png", outputfile);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private Integer returnMonth(String month){
		if(month.contains("Jan")){
			return 1;
		}else if(month.contains("Feb")){
			return 2;
		}else if(month.contains("Mar")){
			return 3;
		}else if(month.contains("Apr")){
			return 4;
		}else if(month.contains("May")){
			return 5;
		}else if(month.contains("Jun")){
			return 6;
		}else if(month.contains("Jul")){
			return 7;
		}else if(month.contains("Aug")){
			return 8;
		}else if(month.contains("Sep")){
			return 9;
		}else if(month.contains("Oct")){
			return 10;
		}else if(month.contains("Nov")){
			return 11;
		}else{
			return 12;
		}
	}

}
