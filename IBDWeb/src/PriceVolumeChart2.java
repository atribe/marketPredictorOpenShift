import java.awt.Color;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.LogAxis;
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
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PriceVolumeChart2 extends ApplicationFrame{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//final static String filename = "D:\\A.txt";
static TimeSeries t1 = new TimeSeries("50-day Price moving average");
static TimeSeries t2 = new TimeSeries("50-day Volume moving average");
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
 * Default constructor
 */
public PriceVolumeChart2(String title)
{
    super(title);
    List<String> tables = getAllTables();
    List<StockData> stockData = null;
    String t = null;
	for(String tableName:tables){
		t = tableName;
		stockData = getDataFrom(tableName);// Get All Data From the Table
		break;
	}
    JPanel panel = createDemoPanel(stockData,t);
    panel.setPreferredSize(new Dimension(500, 270));
    setContentPane(panel);
}

//create price dataset
private static OHLCDataset createPriceDataset(List<StockData> stockData,String tableName)
{
    //the following data is taken from http://finance.yahoo.com/
    //for demo purposes...

    OHLCSeries s1 = new OHLCSeries(tableName);

    try {
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	for(StockData s:stockData){
    		Date date       = sf.parse(s.getDataDate());
            double open     = s.getOpen();
            double high     = s.getHigh();
            double low      = s.getLow();
            double close    = s.getClose();
            s1.add(new Day(date), open, high, low, close);
            t1.add(new Day(date), close);
    	}/*
        BufferedReader in = new BufferedReader(new FileReader(filename));
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String inputLine;
        in.readLine();
        while ((inputLine = in.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(inputLine, ",");
            Date date       = df.parse( st.nextToken() );
            double open     = Double.parseDouble( st.nextToken() );
            double high     = Double.parseDouble( st.nextToken() );
            double low      = Double.parseDouble( st.nextToken() );
            double close    = Double.parseDouble( st.nextToken() );
            double volume   = Double.parseDouble( st.nextToken() );
            //double adjClose = Double.parseDouble( st.nextToken() );
            s1.add(new Day(date), open, high, low, close);
            t1.add(new Day(date), close);
        }
        in.close();*/
    }
    catch (Exception e) {
        e.printStackTrace();
    }



    OHLCSeriesCollection dataset = new OHLCSeriesCollection();
    dataset.addSeries(s1);

    return dataset;
}


//create volume dataset
private static IntervalXYDataset createVolumeDataset(List<StockData> stockdata)
{
    //create dataset 2...
    TimeSeries s1 = new TimeSeries("Volume");
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
	for(StockData s:stockdata){
		Date date = null;
		try {
			date = sf.parse(s.getDataDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        double volume   = s.getVolume();
        s1.add(new Day(date), volume);
        t2.add(new Day(date), volume);
	}/*
    try {
        BufferedReader in = new BufferedReader(new FileReader(filename));
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String inputLine;
        in.readLine();
        while ((inputLine = in.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(inputLine, ",");
            Date date = df.parse( st.nextToken() );
            st.nextToken();
            st.nextToken();
            st.nextToken();
            st.nextToken();
            double volume   = Double.parseDouble( st.nextToken() );
            //double adjClose = Double.parseDouble( st.nextToken() );
            s1.add(new Day(date), volume);
        }
        in.close();
    }
    catch (Exception e) {
        e.printStackTrace();
    }*/

    return new TimeSeriesCollection(s1);
}

/**
 * @param stockData
 * @param tableName
 * @return
 */
private static JFreeChart createCombinedChart(List<StockData> stockData,String tableName)
{
    OHLCDataset data1 = createPriceDataset(stockData,tableName);

    XYItemRenderer renderer1 = new HighLowRenderer();
    renderer1.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
        StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
        new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0.00")));
    renderer1.setSeriesPaint(0, Color.blue);
    DateAxis domainAxis = new DateAxis("Date");
    NumberAxis rangeAxis = new NumberAxis("Price");
    rangeAxis.setNumberFormatOverride(new DecimalFormat("$0.00"));
    rangeAxis.setAutoRange(true);
    rangeAxis.setAutoRangeIncludesZero(false);
    XYPlot plot1 = new XYPlot(data1, domainAxis, rangeAxis, renderer1);
    plot1.setBackgroundPaint(Color.lightGray);
    plot1.setDomainGridlinePaint(Color.white);
    plot1.setRangeGridlinePaint(Color.white);
    plot1.setRangePannable(true);

    //Overlay the Long-Term Trend Indicator
    TimeSeries dataset3 = MovingAverage.createMovingAverage(t1, "50PLT", 50, 0);
    TimeSeriesCollection collection = new TimeSeriesCollection();
    collection.addSeries(dataset3);
    plot1.setDataset(1, collection);
    plot1.setRenderer(1, new StandardXYItemRenderer());
    
  //Overlay the Long-Term Trend Indicator
    TimeSeries dataset4 = MovingAverage.createMovingAverage(t1, "200PLT", 200, 0);
    TimeSeriesCollection collection1 = new TimeSeriesCollection();
    collection.addSeries(dataset4);
    plot1.setDataset(2, collection1);
    plot1.setRenderer(2, new StandardXYItemRenderer());
    
  

    //add a second dataset (volume) and renderer
    IntervalXYDataset data2 = createVolumeDataset(stockData);
    XYBarRenderer renderer2 = new XYBarRenderer();
    renderer2.setDrawBarOutline(false);
    renderer2.setBaseToolTipGenerator(new StandardXYToolTipGenerator(
        StandardXYToolTipGenerator.DEFAULT_TOOL_TIP_FORMAT,
        new SimpleDateFormat("d-MMM-yyyy"), new DecimalFormat("0,000.00")));
    renderer2.setSeriesPaint(0, Color.red);

    XYPlot plot2 = new XYPlot(data2, null, new LogAxis("Volume"), renderer2);
    plot2.setBackgroundPaint(Color.lightGray);
    plot2.setDomainGridlinePaint(Color.white);
    plot2.setRangeGridlinePaint(Color.white);
    
  /*//Overlay the Long-Term Trend Volume Indicator
    TimeSeries dataset5 = MovingAverage.createMovingAverage(t2, "50VLT", 50, 0);
    TimeSeriesCollection collection2 = new TimeSeriesCollection();
    collection.addSeries(dataset5);
    plot2.setDataset(3, collection2);
    plot2.setRenderer(3, new StandardXYItemRenderer());*/

    CombinedDomainXYPlot cplot = new CombinedDomainXYPlot(domainAxis);
    cplot.add(plot1, 3);
    cplot.add(plot2, 2);
    cplot.setGap(8.0);
    cplot.setDomainGridlinePaint(Color.white);
    cplot.setDomainGridlinesVisible(true);
    cplot.setDomainPannable(true);
    XYBarRenderer renderer4 = (XYBarRenderer) cplot.getRenderer(); 
    /*TimeSeries dataset5 = MovingAverage.createMovingAverage(t2, "50VLT", 50, 0);
    TimeSeriesCollection collection5 = new TimeSeriesCollection();
    //renderer4.setBasePaint(Color.red);
    collection.addSeries(dataset5);
    cplot.setDataset(3, collection5);
    cplot.setRenderer(3, renderer4);*/
    //return the new combined chart
    JFreeChart chart = new JFreeChart("OHLC-Volume Chart with Moving Average for: "+tableName,
        JFreeChart.DEFAULT_TITLE_FONT, cplot, true);

    ChartUtilities.applyCurrentTheme(chart);
    renderer2.setShadowVisible(false);
    renderer2.setBarPainter(new StandardXYBarPainter());

    return chart;
}

//create a panel
/**
 * @param stockData
 * @param tableName
 * @return
 */
public static JPanel createDemoPanel(List<StockData> stockData,String tableName)
{
    JFreeChart chart = createCombinedChart(stockData,tableName);
    return new ChartPanel(chart);
}

public static void main(String[] args) {
    // TODO code application logic here
    PriceVolumeChart2 demo = new PriceVolumeChart2(
        "OHLC-Volume Chart with Moving Average");
    demo.pack();
    RefineryUtilities.centerFrameOnScreen(demo);
    demo.setVisible(true);
}

//Download data from web
}