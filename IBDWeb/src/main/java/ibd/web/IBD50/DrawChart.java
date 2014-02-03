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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

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

public class DrawChart {
	TimeSeries t1;
	TimeSeries t2;
	public TimeSeries getT1() {
		return t1;
	}

	public void setT1(TimeSeries t1) {
		this.t1 = t1;
	}

	public TimeSeries getT2() {
		return t2;
	}

	public void setT2(TimeSeries t2) {
		this.t2 = t2;
	}

	private OHLCDataset createPriceDataset(List<StockData> stockData, String tableName) {
		int ind = 0;
		//the following data is taken from http://finance.yahoo.com/
		//for demo purposes...
		OHLCSeries s1 = new OHLCSeries(tableName);
		t1 = new TimeSeries("Price Moving Average");
		t2 = new TimeSeries("Volume Moving Average");
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
				t2.add(new Day(date), s.getVolume());
				ind++;
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
			StockData ss = stockData.get(ind);
			System.out.println(tableName+ss.getDataDate());
			e.printStackTrace();
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
	private IntervalXYDataset createVolumeDataset(List<StockData> stockdata) {

		/*//create dataset 2...
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
    	}*/
		/*try {
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

		//return new TimeSeriesCollection(s1);
		// create dataset 2...
		TimeSeries s1 = new TimeSeries("Down Volume");
		TimeSeries s2 = new TimeSeries("Up Volume");
		for(int i=0;i<stockdata.size();i++){
			StockData obj = stockdata.get(i);
			String dated = obj.getDataDate();
			if(0 != i && obj.getClose()<stockdata.get(i-1).getClose()){
				s1.add(new Day(Integer.parseInt(dated.substring(8)), Integer.parseInt(dated.substring(5,7)), Integer.parseInt(dated.substring(0,4))), obj.getVolume());
			}else{
				s2.add(new Day(Integer.parseInt(dated.substring(8)), Integer.parseInt(dated.substring(5,7)), Integer.parseInt(dated.substring(0,4))), obj.getVolume());
			}
		}

		TimeSeriesCollection dataSet = new TimeSeriesCollection();
		dataSet.addSeries(s1);
		dataSet.addSeries(s2);

		return dataSet;
		//return new TimeSeriesCollection(s1);

	}

	JFreeChart createChart(List<StockData> stockData, String tableName) {

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
		renderer2.setMargin(0.6);
		renderer2.setSeriesPaint(0, Color.RED);
		renderer2.setSeriesPaint(1, Color.BLUE);
		renderer2.setSeriesPaint(2, Color.GREEN);
		renderer2.setSeriesPaint(3, Color.BLACK);
		XYPlot plot2 = new XYPlot(data2, null, new NumberAxis("Volume"), renderer2);
		plot2.setBackgroundPaint(Color.lightGray);
		plot2.setDomainGridlinePaint(Color.white);
		plot2.setRangeGridlinePaint(Color.white);
		//plot2.setRenderer((XYItemRenderer) new MySBRenderer());
		TimeSeries dataset5 = MovingAverage.createMovingAverage(t2, "50VLT", 50, 0);
		TimeSeriesCollection collection2 = new TimeSeriesCollection();
		collection2.addSeries(dataset5);
		plot2.setDataset(3, collection2);
		StandardXYItemRenderer a = new StandardXYItemRenderer();
		a.setSeriesPaint(0, Color.GREEN);
		a.setSeriesPaint(1, Color.orange);
		a.setSeriesPaint(2, Color.BLACK);
		plot2.setRenderer(3, a);
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

	List<StockData> getDataFrom(String tableName){
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
