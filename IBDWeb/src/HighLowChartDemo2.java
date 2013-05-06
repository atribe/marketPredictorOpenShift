/* ----------------------
 * HighLowChartDemo2.java
 * ----------------------
 * (C) Copyright 2003-2009, by Object Refinery Limited.
 *
 */



import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * A demo showing a high-low-open-close chart with a moving average overlaid on
 * top.
 */
public class HighLowChartDemo2 extends ApplicationFrame {

    /**
     * A demonstration application showing a high-low-open-close chart.
     *
     * @param title  the frame title.
     */
    public HighLowChartDemo2(String title) {
        super(title);
        List<String> tables = getAllTables();
        List<StockData> stockData = null;
        String t = null;
		for(String tableName:tables){
			t = tableName;
			stockData = getDataFrom(tableName);// Get All Data From the Table
			break;
		}
        JPanel chartPanel = createDemoPanel(stockData, t);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
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

    private static final Calendar calendar = Calendar.getInstance();

    /**
     * Returns a date using the default locale and timezone.
     *
     * @param y  the year (YYYY).
     * @param m  the month (1-12).
     * @param d  the day of the month.
     * @param hour  the hour of the day.
     * @param min  the minute of the hour.
     *
     * @return A date.
     */
    private static Date createDate(int y, int m, int d, int hour, int min) {
        calendar.clear();
        calendar.set(y, m - 1, d, hour, min);
        return calendar.getTime();
    }

    /**
     * Creates a sample high low dataset.
     *
     * @return a sample high low dataset.
     */
    public static OHLCDataset createDataset(List<StockData> stockData) {

    	final int size = stockData.size();
        Date[] date = new Date[size];
        double[] high = new double[size];
        double[] low = new double[size];
        double[] open = new double[size];
        double[] close = new double[size];
        double[] volume = new double[size];

        
        for(int i=0;i<stockData.size();i++){
        	StockData obj = stockData.get(i);
        	String dated = obj.getDataDate();
            date[i]  = createDate(Integer.parseInt(dated.substring(0,4)), Integer.parseInt(dated.substring(5,7)), Integer.parseInt(dated.substring(8)), 12, 0);
            high[i]  = obj.getHigh();
            low[i]   = obj.getLow();
            open[i]  = obj.getOpen();
            close[i] = obj.getClose();
            //volume[i] = obj.getVolume();
        }

        return new DefaultHighLowDataset("OHLC", date, high, low, open, close, volume);

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  a dataset.
     *
     * @return a sample chart.
     */
    private static JFreeChart createChart(OHLCDataset dataset, String tableName) {

        JFreeChart chart = ChartFactory.createHighLowChart(
            "HighLowChartDemo2 for "+tableName.toUpperCase(),
            "Time",
            "Value",
            dataset,
            true
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        XYPlot plot1 = (XYPlot) chart.getPlot();
//        HighLowRenderer renderer = (HighLowRenderer) plot.getRenderer();
//        renderer.setOpenTickPaint(Color.green);
//        renderer.setCloseTickPaint(Color.black);

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        DateAxis axis1 = (DateAxis) plot1.getDomainAxis();
        axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        axis1.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        NumberAxis yAxis1 = (NumberAxis) plot1.getRangeAxis();
        yAxis.setNumberFormatOverride(new DecimalFormat("$0.00"));
        yAxis1.setNumberFormatOverride(new DecimalFormat("$0.00"));

        // overlay the moving average dataset..
        XYDataset dataset2 = MovingAverage.createMovingAverage(
            dataset, "-50MAVG", 50 * 24 * 60 * 60 * 1000L, 0L);
        plot.setDataset(1, dataset2);
        plot.setRenderer(1, new StandardXYItemRenderer());
        
     // overlay the moving average dataset...
        XYDataset dataset3 = MovingAverage.createMovingAverage(
            dataset, "-200MAVG", 200 * 24 * 60 * 60 * 1000L, 0L);
        plot1.setDataset(2, dataset3);
        plot1.setRenderer(2, new StandardXYItemRenderer());

        return chart;

    }

    /**
     * Creates a panel for the demo (used by SuperDemo.java).
     *
     * @return A panel.
     */
    public static JPanel createDemoPanel(List<StockData> stockData,String t) {
        JFreeChart chart = createChart(createDataset(stockData),t);
        return new ChartPanel(chart);
    }

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(String[] args) {
        HighLowChartDemo2 demo = new HighLowChartDemo2(
                "OHLC-Volume Chart 2 Axis");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }

}
