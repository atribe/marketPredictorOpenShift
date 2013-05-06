import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class OHLCVolumeChart extends ApplicationFrame {

  public OHLCVolumeChart(String titel) {
  super(titel);
  List<String> tables = getAllTables();
  List<StockData> stockData = null;
	for(String tableName:tables){
		stockData = getDataFrom(tableName);// Get All Data From the Table
		break;
	}
  final JFreeChart chart = createChart(stockData);
  final ChartPanel chartPanel = new ChartPanel(chart);
  chartPanel.setPreferredSize(
   new java.awt.Dimension(600, 450));
  setContentPane(chartPanel);
  }

  public double[][] run() {
  double[][] run = new double[][]{
  {10, 6, 2, 4, 7, 2, 8, 12, 9, 4},
  {2, 6, 3, 8, 1, 6, 4, 9, 2, 10}
  };
  return run;
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

  private CategoryDataset createRunDataset1(List<StockData> stockData) {
  final DefaultCategoryDataset dataset = 
  new DefaultCategoryDataset();

  for (int i = 0; i < stockData.size(); i++) {
	  StockData obj = stockData.get(i);
	  String d = obj.getDataDate();
  dataset.addValue(obj.getOpen(),"Open", "" + d.substring(0, 7));
  dataset.addValue(obj.getHigh(),"High", "" + d.substring(0, 7));
  dataset.addValue(obj.getLow(),"Low", "" + d.substring(0, 7));
  dataset.addValue(obj.getClose(),"Close", "" + d.substring(0, 7));
  }
  return dataset;
  }

  private CategoryDataset createRunDataset2(List<StockData> stockData) {
  final DefaultCategoryDataset dataset = 
   new DefaultCategoryDataset();

  double[] run = run()[1];

  for (int i = 0; i < stockData.size(); i++) {
	  StockData obj = stockData.get(i);
	  String d = obj.getDataDate();
  dataset.addValue(obj.getVolume(),"Volume", "" + d.substring(0, 7));
  }
  return dataset;
  }

  private CategoryDataset createRunRateDataset1() {
  final DefaultCategoryDataset dataset 
   = new DefaultCategoryDataset();

  double[] run = run()[0];
  float num = 0;

  for (int i = 0; i < run.length; i++) {
  num += run[i];
  dataset.addValue(num / (i + 1), 
  "Team1 Runrate", "" + (i + 1));
  }
  return dataset;
  }

  private CategoryDataset createRunRateDataset2() {
	  final DefaultCategoryDataset dataset =
	   new DefaultCategoryDataset();
	
	  double[] run = run()[1];
	  float num = 0;
	
	  for (int i = 0; i < run.length; i++) {
	  num += run[i];
	  dataset.addValue(num / (i + 1), 
	  "Team2 Runrate", "" + (i + 1));
	  }
	  return dataset;
  }

  private JFreeChart createChart(List<StockData> stockData) {

  final CategoryDataset dataset1 = createRunDataset2(stockData);
  final NumberAxis rangeAxis1 = new NumberAxis("Volume");
  rangeAxis1.setStandardTickUnits(
  NumberAxis.createIntegerTickUnits());
  final BarRenderer renderer1 = new BarRenderer();
  renderer1.setSeriesPaint(0, Color.red);
  renderer1.setBaseToolTipGenerator(
   new StandardCategoryToolTipGenerator());
  final CategoryPlot subplot1 = 
  new CategoryPlot(dataset1, null, 
  rangeAxis1, renderer1);
  subplot1.setDomainGridlinesVisible(true);

  final CategoryDataset dataset2 = createRunDataset1(stockData);
  final NumberAxis rangeAxis2 = new NumberAxis("Price");
  rangeAxis2.setStandardTickUnits(
   NumberAxis.createIntegerTickUnits());
  final BarRenderer renderer2 = new BarRenderer();
  renderer2.setSeriesPaint(0, Color.blue);
  renderer2.setBaseToolTipGenerator(
  new StandardCategoryToolTipGenerator());
  final CategoryPlot subplot2 = 
  new CategoryPlot(dataset2, null, 
  rangeAxis2, renderer2);
  subplot2.setDomainGridlinesVisible(true);

  final CategoryAxis domainAxis = new CategoryAxis("OHLC-Volume Chart");
  final CombinedDomainCategoryPlot plot = 
  new CombinedDomainCategoryPlot(domainAxis);

  plot.add(subplot1, 1);
  plot.add(subplot2, 1);


  final JFreeChart chart = new JFreeChart(
  "OHLC-Volume Chart", new Font("SansSerif", Font.BOLD, 12),
  plot, true);
  return chart;
  }

  public static void main(final String[] args) {

  final String title = "OHLC-Volume Stock Chart";
  final OHLCVolumeChart chart = new OHLCVolumeChart(title);
  chart.pack();
  RefineryUtilities.centerFrameOnScreen(chart);
  chart.setVisible(true);
  }
}