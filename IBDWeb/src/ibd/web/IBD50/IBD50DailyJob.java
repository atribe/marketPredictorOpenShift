package ibd.web.IBD50;

import ibd.web.classes.MarketDB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class IBD50DailyJob {
	/**
	 * @author Shakeel Shahzad
	 * @description This function is responsible for handling the whole teedixibd50pricesvolumes data Downloading and Insertion.
	 */
	public static void processIBD50DailyJob(){
		/**
		 * @author Shakeel Shahzad
		 * @description First get all the symbols which are already present in Constants.teedixIbd50PricesVolumes
		 * @explaination About null, as our application when deployed directly runs the job, so for the very
		 * first time this list will be null, so no need of any processing.
		 */
		if(ibd.web.Constants.Constants.teedixIbd50PricesVolumes!=null){
			/**
			 * @author Shakeel Shahzad
			 * @description Do the whole Data transaction for Each Symbol.
			 */
			int counter = 0;
			while(counter < ibd.web.Constants.Constants.teedixIbd50PricesVolumes.size()){
				downloadAndInsertData(ibd.web.Constants.Constants.teedixIbd50PricesVolumes.get(counter));
				counter++;
			}
		}
	}
	
	/**
	 * @author Shakeel Shahzad
	 * @description This function will download the data against the symbol and insert into Database.
	 * @param symbol Symbol being passed by processIBD50DailyJob()
	 */
	private static void downloadAndInsertData(String symbol){
		/**
		 * @author Shakeel Shahzad
		 * @description First get the oldest Date (One year = -365 Days)
		 */
		String date = getStartingDate();
		/**
		 * @author Shakeel Shahzad
		 * @description Now prepare the Yahoo URL
		 */
		final String url = getYahooURL(symbol,date);
		//System.out.println("YAHOO URL: "+url);
		/**
		 * @author Shakeel Shahzad
		 * @description Now parse this URL and save it in Database.
		 */
		parseAndSave(url,symbol);
	}
	
	/**
	 * @author Shakeel Shahzad
	 * @description This function takes the URL and parses it, saves it in database.
	 * @param URL
	 */
	private static void parseAndSave(final String URL,final String symbol){
		/**
		 * @author Shakeel Shahzad
		 * @description Try to parse this URL for 5 times and then leave it.
		 */
		int counter = 0;
		while(counter < 5){
			try{
				URL ur = new URL(URL);
				HttpURLConnection HUC = (HttpURLConnection) ur.openConnection();
				counter = 5;// Do it 5 as it is successful.
				BufferedReader in = new BufferedReader(new InputStreamReader(HUC.getInputStream()));
				String line;
				in.readLine();
				ibd.web.beans.IBD50DailyStock ibd50DailyStock = new ibd.web.beans.IBD50DailyStock();
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
				    ibd50DailyStock.setName(symbol);
				    ibd50DailyStock.setDate(dateStr);
				    //System.out.println("SET DATESTRING: "+dateStr);
				    ibd50DailyStock.setOpen(Float.parseFloat(priceOpenStr));
				    ibd50DailyStock.setClose(Float.parseFloat(priceCloseStr));
				    ibd50DailyStock.setHigh(Float.parseFloat(priceHighStr));
				    ibd50DailyStock.setLow(Float.parseFloat(priceLowStr));
				    ibd50DailyStock.setVolume(Float.parseFloat(volumeStr));
					/**
					 * @author Shakeel Shahzad
					 * @description Save the Stock for this Symbol
					 */
					saveIBD50DailyStock(ibd50DailyStock);
				}
			}catch(Exception e){
				//e.printStackTrace();
				counter++;
			}
		}
	}
	
	/**
	 * @author Shakeel Shahzad
	 * @description This function gets the Bean and saves the data in database in relevant Table
	 * @param ibd50DailyStock Bean
	 */
	private static void saveIBD50DailyStock(ibd.web.beans.IBD50DailyStock ibd50DailyStock){
		  Connection con = null;
		  Statement stmt = null;
		  try{
			  con = MarketDB.getConnectionIBD50PricesVolumes();
			  stmt = con.createStatement();
			  //System.out.println("SAVE DATESTR: "+ibd50DailyStock.getDate());
			  String query = "INSERT INTO `^"+ibd50DailyStock.getName().toLowerCase()+"` VALUES('"+ibd50DailyStock.getDate()+"',"+ibd50DailyStock.getOpen()+","+ibd50DailyStock.getHigh()+","+ibd50DailyStock.getLow()+","+ibd50DailyStock.getClose()+","+ibd50DailyStock.getVolume()+")";
			  System.out.println(query);
			  stmt.executeUpdate(query);
		  }catch(Exception e){
			  //e.printStackTrace();
		  }finally{
			  try{
				  stmt.close();
				  con.close();
			  }catch(Exception e){
				  //e.printStackTrace();
			  }
		  }
	}
	
	/**
	 * @author Shakeel Shahzad
	 * @description This function gets the symbol with the Starting Date and returns Yahoo URL to parse.
	 * @param sym Symbol
	 * @param fromD From Date
	 * @return returns the String URL for Yahoo Finance API 
	 */
	private static String getYahooURL(String sym, String fromD){
		String temp = new Date().toString();
		String [] toDate = temp.split(" ");
		String [] fromDate = fromD.split("-");
		toDate[1] = Integer.toString(returnMonth(toDate[1]));
		if(1 == toDate[1].trim().length()){
			toDate[1] = "0"+toDate[1];
		}
		String url = "http://ichart.finance.yahoo.com/table.csv?s="
				+ sym.toUpperCase() + "&a=" + (Integer.parseInt(fromDate[1])-1) + "&b=" + fromDate[2] + "&c=" + fromDate[0] + "&g=d&d=" + (Integer.parseInt(toDate[1])-1) + "&e=" + toDate[2]
				+ "&f=" + toDate[0] + "&ignore=.csv";
		return url;
	}
	
	/**
	 * @author Shakeel Shahzad
	 * @description This function returns the one year back Date(String) according to the current Date.
	 * @return String Date
	 */
	private static String getStartingDate(){
		Calendar cal = GregorianCalendar.getInstance();
		cal.add( Calendar.DAY_OF_YEAR, -365);
		Date date = cal.getTime();
		String[] array = ((cal.getTime()).toString()).split(" ");
		//System.out.println(date);//System.exit(-1);
		array[1] = Integer.toString(returnMonth(array[1]));
		if(1 == array[1].trim().length()){
			array[1] = "0"+array[1];
		}
		return (array[5]+"-"+array[1]+"-"+array[2]);
	}
	
	/**
	 * @author Shakeel Shahzad
	 * @description Gets the String Month and returns Integer Month
	 * @param month
	 * @return Integer month
	 */
	private static Integer returnMonth(String month){
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
