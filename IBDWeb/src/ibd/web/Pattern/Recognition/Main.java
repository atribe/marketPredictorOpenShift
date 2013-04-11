package ibd.web.Pattern.Recognition;

import ibd.web.beans.Data50;
import ibd.web.patterns.Data;
import ibd.web.patterns.DataAnalyzer;
import ibd.web.patterns.DataRetriever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Main {
	private static LinkedList<String> picks;
	public static void main(String...args){
		Main object = null;
		List<Data50> allStocks = null;
		try{
			object = new Main();
			//loop through all 50 IBD50 stocks starting at rank 1
			allStocks = object.getAllStocks("2013-04-05");
			for(Data50 value:allStocks){
				String url = object.getYahooURL(value.getSymbol(),(365+365));
				Data stock = object.dataParser(url,value.getSymbol());
				DataAnalyzer da = new DataAnalyzer();
				Data marketData,data;
				da.initialize(stock);//initialize the DataAnalyzer object
				if(da.bullOrBear(stock))//assess climate
					/*if(da.checkDDays(stock))// check distribution days
						while(dr.moreToRetrieve()){
							data = dr.retrieve();
							if(data != null)
								da.analyze(data);
						}*/
					da.analyze(stock);
						picks = da.getPicks();
						if(picks != null){
							Iterator<String> iter = picks.iterator();
							System.out.println("Buy recommendations:");
							while(iter.hasNext())
								System.out.println(iter.next());
						}
						else
							System.out.println("No buy recommendations");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private List<Data50> getAllStocks(String tableName){
		List<Data50> allStocks = new ArrayList<Data50>();
		Connection connection = null;
		String query = null;
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		try{
			connection = DataRetriever.getConnection();
			query = "SELECT * FROM `^"+tableName+"` ORDER BY rank ASC";
			pStmt = connection.prepareStatement(query);
			resultSet = pStmt.executeQuery();
			while(resultSet.next()){
				Data50 obj = new Data50();
				obj.setRank(Integer.toString(resultSet.getInt(1)));
				obj.setCompanyName(resultSet.getString(2));
				obj.setSymbol(resultSet.getString(3));
				obj.setSmartSelectCompositeRating(resultSet.getString(4));
				obj.setEpsRating(resultSet.getString(5));
				obj.setRsRating(resultSet.getString(6));
				obj.setIndGroupRelativeStrength(resultSet.getString(7));
				obj.setSmrRating(resultSet.getString(8));
				obj.setAccDis(resultSet.getString(9));
				obj.setWeekHigh52(resultSet.getString(10));
				obj.setClosingPrice(resultSet.getString(11));
				obj.setDollarChange(resultSet.getString(12));
				obj.setVolChange(resultSet.getString(13));
				obj.setVolume(resultSet.getString(14));
				obj.setPe(resultSet.getString(15));
				obj.setSponReading(resultSet.getString(16));
				obj.setDivYield(resultSet.getString(17));
				obj.setOffHigh(resultSet.getString(18));
				obj.setAnnualEpsEstChange(resultSet.getString(19));
				obj.setLastQtrEpsChange(resultSet.getString(20));
				obj.setNextQtrEpsChange(resultSet.getString(21));
				obj.setLastQtrSalesChange(resultSet.getString(22));
				obj.setRoe(resultSet.getString(23));
				obj.setPretaxmargin(resultSet.getString(24));
				obj.setManagementOwns(resultSet.getString(25));
				obj.setQtrEpsCountGreaterThan15(resultSet.getString(26));
				obj.setDescription(resultSet.getString(27));
				obj.setFootNote(resultSet.getString(28));
				obj.setDataAsOf(resultSet.getString(29));
				obj.setIndexAsOf(resultSet.getString(30));
				obj.setExchangeToTrade(resultSet.getString(31));
				obj.setIndexMembership(resultSet.getString(32));
				obj.setMarketCap(resultSet.getString(33));
				obj.setEarningAnnouncement(resultSet.getString(34));
				allStocks.add(obj);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(null != resultSet)
					resultSet.close();
				if(null != pStmt)
					pStmt.close();
				if(null != connection)
					connection.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return allStocks;
	}
	
	private Data dataParser(String url,String symbol) throws MalformedURLException, IOException, URISyntaxException {
		ArrayList<Float> priceListClose = new ArrayList<Float>();
		ArrayList<Float> priceListHigh = new ArrayList<Float>();    //this is added
		ArrayList<Float> priceListLow = new ArrayList<Float>(); //this is added
		ArrayList<Float> priceListOpen = new ArrayList<Float>(); //this is added
		ArrayList<Double> volumeList = new ArrayList<Double>();
		ArrayList<Date> dateList = new ArrayList<Date>();    //this is added
		URL ur = new URL(url);
		HttpURLConnection HUC = (HttpURLConnection) ur.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(HUC.getInputStream()));
		String line;
		//int lineCount = 0;  //lineCount is one line for each day
		in.readLine();//reads the first line, it's just headers
		//the entries on the line are date, open, high, low, close, volume, adj close, misc
		//gets a line of input if available, beginning of main loop
		while ((line = in.readLine()) != null) {// && lineCount <= startDaysAgo) {
		    int len = line.length();    //this is the number of character in the line
		    int lineIndex = 0;  //this is the character index in the line

		    String dateStr = "";
		    // add characters to a string until the next ',' is encountered
		    while (lineIndex < len && line.charAt(lineIndex) != ',') {
			dateStr += line.charAt(lineIndex);
			++lineIndex;
		    }

		    ++lineIndex;//get off the current comma
		    String priceOpenStr = "";
		    // add characters to a string until the next ',' is encountered
		    while (lineIndex < len && line.charAt(lineIndex) != ',') {
			priceOpenStr += line.charAt(lineIndex);
			++lineIndex;
		    }

		    ++lineIndex;//get off the current comma
		    String priceHighStr = "";
		    // add characters to a string until the next ',' is encountered
		    while (lineIndex < len && line.charAt(lineIndex) != ',') {
			priceHighStr += line.charAt(lineIndex);
			++lineIndex;
		    }

		    ++lineIndex;//get off the current comma
		    String priceLowStr = "";
		    // add characters to a string until the next ',' is encountered
		    while (lineIndex < len && line.charAt(lineIndex) != ',') {
			priceLowStr += line.charAt(lineIndex);
			++lineIndex;
		    }

		    ++lineIndex;//get off the current comma
		    String priceCloseStr = "";
		    // add characters to a string until the next ',' is encountered
		    while (lineIndex < len && line.charAt(lineIndex) != ',') {
			priceCloseStr += line.charAt(lineIndex);
			++lineIndex;
		    }

		    ++lineIndex;// get off the current comma
		    String volumeStr = "";
		    while (lineIndex < len && line.charAt(lineIndex) != ',') {
			volumeStr += line.charAt(lineIndex);//same for volumes(they are immediately after prices)
			++lineIndex;
		    }

		    //add current values to arrayLists
		    dateList.add(Date.valueOf(dateStr));
		    priceListOpen.add(Float.parseFloat(priceOpenStr));//these have to be parsed because they come in as strings
		    priceListHigh.add(Float.parseFloat(priceHighStr));
		    priceListLow.add(Float.parseFloat(priceLowStr));
		    priceListClose.add(Float.parseFloat(priceCloseStr));
		    volumeList.add(Double.parseDouble(volumeStr));
		    //++lineCount;
		}
		////stores the data arrays in a Data object and returns it
		return new Data(symbol,priceListClose,volumeList, priceListLow);
		//return new Data(symbol, dateList, priceListOpen, priceListHigh, priceListLow, priceListClose, volumeList);
	    }
	
	private String getYahooURL(String symbol, int daysAgo) {
		
		GregorianCalendar calendarStart = new GregorianCalendar();
		calendarStart.add(Calendar.DAY_OF_MONTH, -daysAgo);//this subtracts the number of startDaysAgo from todays date.  The add command changes the calendar object
		int d, e, f, a, b, c;
		a = calendarStart.get(Calendar.MONTH);//this gets todays month
		b = calendarStart.get(Calendar.DAY_OF_MONTH);//this gets todays day of month
		c = calendarStart.get(Calendar.YEAR);//this gets todays year

		GregorianCalendar calendarEnd = new GregorianCalendar();
		//calendarEnd.add(Calendar.DAY_OF_MONTH, -endDaysAgo);
		d = calendarEnd.get(Calendar.MONTH);//this gets the beginning dates month
		e = calendarEnd.get(Calendar.DAY_OF_MONTH);//this gets beginning dates day
		f = calendarEnd.get(Calendar.YEAR);//this gets beginning dates year

		//System.out.println("month="+a+" day="+b+" year="+c);

		//System.out.println(a+","+b+","+c);
		//System.out.println(d+","+e+","+f);
		String str = "http://ichart.finance.yahoo.com/table.csv?s="
				+ symbol.toUpperCase() + "&a=" + a + "&b=" + b + "&c=" + c + "&g=d&d=" + d + "&e=" + e
				+ "&f=" + f + "&ignore=.csv";
		System.out.println(str);
		return str;
	    }
}
