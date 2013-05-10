/* ---------------------
 * PriceVolumeDemo2.java
 * ---------------------
 * (C) Copyright 2007-2009, by Object Refinery Limited.
 *
 */



import java.awt.Color;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
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
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demonstration application showing how to create a price-volume chart.
 */
public class PriceVolumeDemo2 extends ApplicationFrame {

    /**
     * Constructs a new demonstration application.
     *
     * @param title  the frame title.
     */
    public PriceVolumeDemo2(String title) {
        super(title);
        List<String> tables = getAllTables();
        List<StockData> stockData = null;
        String t = null;
		for(String tableName:tables){
			t = tableName;
			stockData = getDataFrom(tableName);// Get All Data From the Table
			break;
		}
        JFreeChart chart = createChart(stockData, t);
        ChartPanel panel = new ChartPanel(chart, true, true, true, false, true);
        panel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(panel);
    }
    
    private static Connection getConnection() {
    	Connection connection = null;
    	String host, port, dbURL, username, password;
    	try {
    	    Class.forName("com.mysql.jdbc.Driver");
    	    
    	    
    	 // ************For Local Account************
    	    
	    	    host = "localhost";
	    	    port="3306";
	    	    dbURL = "jdbc:mysql://"+host+":"+port+"/moneytreeibd50pricesvolumes";
	    	    username = "root";
	    	    password = "root";
    	    
    	    
    	    connection = DriverManager.getConnection(dbURL, username, password);
    	} catch (ClassNotFoundException e) {
    		e.printStackTrace();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return connection;
    }
	
	private static List<String> getAllTables(){
		Connection c = getConnection();
		List<String> tableNames = new ArrayList<String>();
		DatabaseMetaData md = null;
		try {
			md = c.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    ResultSet rs = null;
		try {
			rs = md.getTables(null, null, "%", null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    try {
			while (rs.next()) {
			  String name = rs.getString(3);
			  name = name.substring(1);
			  tableNames.add(name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(c!=null)
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	    return tableNames;
	}
	
	private static List<StockData> getDataFrom(String tableName){
		List<StockData> data = new ArrayList<StockData>();
		Connection con = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  try{
			  con = getConnection();
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

    /**
     * Creates a chart.
     *
     * @return a chart.
     */
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
        renderer2.setMargin(0.5);
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
        /*IntervalXYDataset volumeData1 = createVolumeDatasetMovingAvg(stockData);
        XYDataset dataset4 = MovingAverage.createMovingAverage(volumeData1, "-100PMAVG", 100 * 24 * 60 * 60 * 1000L, 0L);
        plot.setDataset(2,dataset4);
        XYDataset dataset5 = MovingAverage.createMovingAverage(volumeData1, "-200PMAVG", 200 * 24 * 60 * 60 * 1000L, 0L);
        plot.setDataset(3,dataset5);*/
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

    /**
     * Creates a sample dataset.  Here the data creation is hard-coded, but
     * in a real application you would normally read the data from a database
     * or some other source.
     *
     * @return A sample dataset.
     */
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
    
    private static IntervalXYDataset createVolumeDatasetMovingAvg(List<StockData> stockData) {

        // create dataset 2...
        TimeSeries s1 = new TimeSeries("Volume");
        for(int i=0;i<stockData.size();i++){
        	StockData obj = stockData.get(i);
        	String dated = obj.getDataDate();
        		s1.add(new Day(Integer.parseInt(dated.substring(8)), Integer.parseInt(dated.substring(5,7)), Integer.parseInt(dated.substring(0,4))), obj.getVolume());
        	
        }
        
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        dataSet.addSeries(s1);
        
        return dataSet;
        //return new TimeSeriesCollection(s1);

    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    /*public static JPanel createDemoPanel() {
        JFreeChart chart = createChart();
        return new ChartPanel(chart);
    }*/

    /**
     * Starting point for the price/volume chart demo application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {

        PriceVolumeDemo2 demo = new PriceVolumeDemo2(
                "OHLC-Volume Multiple Axis");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

}
