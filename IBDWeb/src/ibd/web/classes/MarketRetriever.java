/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibd.web.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.joda.time.Days;
import org.joda.time.LocalDate;

/**
 * @author Aaron
 */
public class MarketRetriever {

	/**
	 * indices to obtain data for
	 */
	//    public static String[] list = {"^MID","^SML"};
	/**
	 * number of days back from today to obtain database
	 */
	//    public static int numDays = 55000;
	/**
	 *
	 * @param var
	 * @throws IOException
	 */

	public static void main(Variables var) throws IOException{

		Connection connection = MarketDB.getConnection();
		Data data = null;
		//	for (int i = 0; i < list.length; ++i) {
		int numDays = getNumDays(var); //
		boolean loop;//if true this decreases the numDays by 1 and sends back through loop
		do {
			loop=false;
			try {//this is the try for the getRecord method
				try {
					data = getData(var.list, numDays); //var.list=^DJI
				} catch (URISyntaxException e) {
				} catch (MalformedURLException e) {
				} catch (IOException e) {
					break;
				} catch (Exception e) {
				}

				Date[] dates = data.dateData;
				float[] opens = data.priceDataOpen;
				float[] highs = data.priceDataHigh;
				float[] lows = data.priceDataLow;
				float[] closes = data.priceDataClose;
				long[] volumes = data.volumeData;

				MarketDB.addRecord(connection, var.list, dates, opens, highs, lows, closes, volumes);
			} catch (SQLException e) {
				ibd.web.Constants.Constants.logger.info("Can not add record for NumDays="+numDays+" in MarketRetreiver.java "+e);
				//System.err.println("Cannot add record for numDays=" + numDays);
				numDays -= 1;
				loop=true;
			}
		} while (loop==true&numDays>0);
		//	}end of for loop
	}

	/**
	 * Overseeing method for retrieving stock-specific price and volume data,
	 * getYahooStockURL() is first called to assemble the appropriate URL, then
	 * dataParser compiles the stock and volume data into a Data object
	 * @param symbol  the stock for which data is to be gathered
	 * @param daysAgo  how many startDaysAgo for which to gather data
	 * @return a Data object
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static Data getData(String symbol, int daysAgo) throws URISyntaxException, MalformedURLException, IOException {//these three are what dataParser throw as well
		Data data = null;
		int flag = 0;
		while (flag < 20) {
			try {

				String URL = getYahooURL(symbol, daysAgo);//gets URL for S&P500,dow,and nasdaq data
				//System.out.println(URL);
				data = dataParser(URL);// extract price and volume data for URL, # of yahoo days
				flag = 20;  //this kicks the obtain data out of the loop since flag=20
			} catch (NumberFormatException e) {
				++flag;
				//System.err.println("Proper connection getYahooMarketURL failed, trying again...");
				ibd.web.Constants.Constants.logger.info("Proper Connection getYahooMarketURL failed, trying again..."+e);
			} catch (Exception e) {//catch any other exception
				ibd.web.Constants.Constants.logger.info("Exception in MarketRetriever.java"+e);
				break;
				//		System.exit(1);

			}
		}
		return data;
	}

	/**
	 * reads data from the specified URL, parses price and volume data
	 * and stores them in the Data class.
	 * @param url  the URL to read from
	 * @param startDaysAgo the number of previous startDaysAgo from which data should be
	 * gathered
	 * @return an instance of the Data class, containing an array
	 * of prices and an array of corresponding volumes
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static Data dataParser(String url) throws MalformedURLException, IOException, URISyntaxException {
		ArrayList<Float> priceListClose = new ArrayList<Float>();
		ArrayList<Float> priceListHigh = new ArrayList<Float>();    //this is added
		ArrayList<Float> priceListLow = new ArrayList<Float>(); //this is added
		ArrayList<Float> priceListOpen = new ArrayList<Float>(); //this is added
		ArrayList<Long> volumeList = new ArrayList<Long>();
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
			volumeList.add(Long.parseLong(volumeStr));
			//++lineCount;
		}
		//stores the data arrays in a Data object and returns it
		return new Data(dateList, priceListOpen, priceListHigh, priceListLow, priceListClose, volumeList);
	}

	/**
	 * *Builds url for a user supplied # of startDaysAgo
	 * @param symbol    market or stock for which data is desired
	 * @param daysAgo  # of startDaysAgo to retrieve data
	 * @return  construed URL
	 */
	public static String getYahooURL(String symbol, int daysAgo) {
		LocalDate endDate = new LocalDate();
		LocalDate startDate = endDate.minusDays(daysAgo);
		//TODO remove old date code
		//GregorianCalendar calendarStart = new GregorianCalendar();
		//calendarStart.add(Calendar.DAY_OF_MONTH, -daysAgo);//this subtracts the number of startDaysAgo from todays date.  The add command changes the calendar object
		int a_startMonth, b_startDay, c_startYear;
		int d_endMonth, e_endDay, f_endYear; 
		a_startMonth = startDate.getMonthOfYear()-1;//Yahoo uses zero based month numbering, this gets the beginning dates month
		b_startDay = startDate.getDayOfMonth();//this gets beginning dates day
		c_startYear = startDate.getYear();//this gets beginning dates year

		GregorianCalendar calendarEnd = new GregorianCalendar();
		d_endMonth = endDate.getMonthOfYear()-1;//Yahoo uses zero based month numbering,this gets todays month
		e_endDay = endDate.getDayOfMonth();//this gets todays day of month
		f_endYear = endDate.getYear();//this gets todays year

		System.out.println("month="+a_startMonth+" day="+b_startDay+" year="+c_startYear);

		String str = "http://ichart.finance.yahoo.com/table.csv?s="
				+ symbol.toUpperCase() + "&a=" + a_startMonth + "&b=" + b_startDay + "&c=" + c_startYear + "&g=d&d=" + d_endMonth + "&e=" + e_endDay
				+ "&f=" + f_endYear + "&ignore=.csv";
		System.out.println(str);
		ibd.web.Constants.Constants.logger.info("Fetched data according to: "+str);
		return str;
	}

	public static int getNumDays(Variables var) {
		Connection connection = MarketDB.getConnection();
		Date date = var.endDate; //today, but is changable
		int numDays = -1;
		try {
			while (MarketDB.isMatch(connection, var.list, date) == false) { //var.list=^DJI
				numDays += 1;
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(date);
				cal.add(Calendar.DAY_OF_MONTH, -1);
				java.util.Date utilDate = cal.getTime();
				date = new java.sql.Date(utilDate.getTime());//convert to sql.date
			}
		} catch (SQLException e) {
			ibd.web.Constants.Constants.logger.info("Can not perform isMatch Method in MarketRetriever.java "+e);
			//	    System.err.println("cannot perform isMatch method");
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		return numDays;
	}

	public static int getNumberOfDaysFromNow(LocalDate date){
		LocalDate today = new LocalDate(); //Variable with today's date

		return Days.daysBetween(date, today).getDays();
		// TODO remove this old date code when Joda Time takes over
		//long diffTime = today.getTime() - date.getTime(); //difference in milliseconds between today and the date supplied to this method
		//int diffDays =(int) (diffTime / (1000 * 60 * 60 * 24)); //calculating days from milliseconds and converting to an int
		//return diffDays;
	}
}
