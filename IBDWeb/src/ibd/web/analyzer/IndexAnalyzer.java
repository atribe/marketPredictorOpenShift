package ibd.web.analyzer;

import ibd.web.DBManagers.MarketIndexAnalysisDB;
import ibd.web.DBManagers.MarketIndexDB;
import ibd.web.DBManagers.MarketIndexParametersDB;
import ibd.web.DataObjects.IndexAnalysisRow;
import ibd.web.DataObjects.YahooDOHLCVARow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

public class IndexAnalyzer {
	/*
	 * These variables are class member properties and once set they can be used by 
	 * all methods of the class. However, because this is a static class they need to be
	 * set every time you enter into the class from outside.
	 */

	//TODO make a get method for each variable, then if it is called and the variable is null then throw an error so I can quickly catch it
	//Database Connection
	static Connection m_con;

	//Database names
	static String m_index;
	static String m_indexParametersDBName;

	//member variables related to dates or number of days
	static private int m_bufferDays;
	static private int m_loopBeginId;
	static private int m_loopEndId;

	public static void runIndexAnalysis(Connection connection, String index, String indexParametersDBName) {
		/*
		 * Future Index Analysis
		 * 
		 * 1.Determine what dates are needed based off the start and end date, as well as the
		 * 		extra days required by the buffer
		 * 
		 * 2.Calculates and stores D dates in the date range
		 * 
		 * 3.Calculates and stores churning dates in the date range
		 * 
		 * 4.Calculates and stores follow through dates
		 * 
		 * 5.Uses d-dates,churning dates, follow through dates to set buy and sell dates
		 * 
		 * 6.Calculates a return based off the buy and sell periods
		 * 
		 * 7.Saves needed data to DB so that it maybe displayed on the website.
		 */
		m_con = connection;
		m_index = index;
		m_indexParametersDBName = indexParametersDBName;
		
		System.out.println("");
		System.out.println("--------------------------------------------------------------------");
		System.out.println("Starting Index Analyzer for " + m_index);

		//1. Setting the number of buffer days needed to calc averages and such
		setBufferDays();

		//2. Calculating the id's of the start and end date of the loop
		setLoopBeginId();

		setLoopEndId();

		//2. Calculate and store d-dates
		distributionDayAnalysis();
	}

	private static void setBufferDays(){
		/*
		 * Conditions should be put in order of most amount days to fewest.
		 * That way the if statements will progress through and stop on the longest one that is active.
		 */
		String bufferConditionCheck1 = "churnAVG50On";
		String bufferConditionCheck2 = "pivotTrend35On";
		if(MarketIndexParametersDB.getBooleanValue(m_con, m_indexParametersDBName, bufferConditionCheck1)) {
			m_bufferDays=50;
		} else if(MarketIndexParametersDB.getBooleanValue(m_con, m_indexParametersDBName, bufferConditionCheck2)) {
			m_bufferDays=35;
		} else {
			m_bufferDays=0;
		}
	}

	private static int getBufferDays(){
		return m_bufferDays;
	}

	/**
	 * Gets the loopEndId by pulling the end date from the parameter database and then looking that date up in the price/volume database
	 * 
	 */
	private static void setLoopEndId() {
		String keyOriginalEndDate = "endDate";
		LocalDate endDate = MarketIndexParametersDB.getDateValue(m_con, m_indexParametersDBName, keyOriginalEndDate);

		m_loopEndId = MarketIndexDB.getIdByDate(m_con, m_index, endDate, false);
	}

	private static void setLoopBeginId() {
		String keyStartDate = "startDate";
		LocalDate startDate = MarketIndexParametersDB.getDateValue(m_con, m_indexParametersDBName, keyStartDate);

		int beginId = MarketIndexDB.getIdByDate(m_con, m_index, startDate, true);
		if(beginId-m_bufferDays<1) {
			m_loopBeginId = 1;
		} else {
			m_loopBeginId = beginId-m_bufferDays;
		}
	}

	private static void distributionDayAnalysis(){
		System.out.println("     Starting D-Day Counting and recording");

		List<IndexAnalysisRow> analysisRows = MarketIndexDB.getDataBetweenIds(m_con, m_index, m_loopBeginId, m_loopEndId);
		
		String tableName = MarketIndexAnalysisDB.getTableName(m_index);
		
		try {
			/* 
			 * TODO there should probably be a table that has the timestamp of when the pricevolume tables have been updated
			 * and when the parameter tables have been updated
			 * and when the analysis tables have been updated
			 * 
			 * Then when this method is run it would first check to see if there have been any changes to either table since the last update
			 */		

			//Reset the table so that the data can be reanalyzed
			MarketIndexAnalysisDB.resetTable(m_con, tableName);
			
			//Check and record all d days in the DB
			analysisRows = checkForDDays(analysisRows);
			
			//TODO put the churning D Day finder here
			analysisRows = checkForChurningDays(analysisRows);
			
			//Getting window length from parameter database
			String keydDayWindow = "dDayWindow";
			int dDayWindow = MarketIndexParametersDB.getIntValue(m_con, m_indexParametersDBName, keydDayWindow);
		
			//Counting up d-day that have fallen in a given window is handled in the following function
			countDDaysInWindow(dDayWindow);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<IndexAnalysisRow> checkForDDays(List<IndexAnalysisRow> analysisRows) throws SQLException {
		System.out.println("          Checking to see if each day is a D-Day");
		
		int rowCount = analysisRows.size();
		int ddayCount=0;
		
		for(int i = 1; i < rowCount; i++) //Starting at i=1 so that i can use i-1 in the first calculation 
		{
			/*
			 * D day rules
			 * 1. Volume Higher than the previous day
			 * 2. Price drops by X% (IBD states .2%)
			 */
			long todaysVolume = analysisRows.get(i).getVolume();
			long previousDaysVolume = analysisRows.get(i-1).getVolume();
			
			float todaysClose = analysisRows.get(i).getClose();
			float previousDaysClose = analysisRows.get(i-1).getClose();

			float closePercentChange = (todaysClose/previousDaysClose-1);
			float closePercentRequiredDrop = (float) -0.002; //TODO make this come from the parameter database

			if( todaysVolume > previousDaysVolume /*This is rule #1*/ && closePercentChange < closePercentRequiredDrop /*This is rule #1*/)
			{
				ddayCount++;
				analysisRows.get(i).setDDay(true);
				//MarketIndexAnalysisDB.addDDayStatus(ps, analysisRows.get(i).getPVD_id(), true);
			}
			else
			{
				analysisRows.get(i).setDDay(false);
				//MarketIndexAnalysisDB.addDDayStatus(ps, analysisRows.get(i).getPVD_id(), false);
			}
		}
		
		return analysisRows;
	}
	
	private static List<IndexAnalysisRow> checkForChurningDays(List<IndexAnalysisRow> analysisRows) {
		System.out.println("          Checking to see if each day is a Churning Day");
		
		int rowCount = analysisRows.size();
		int churningDayCount=0;
		//Getting window length from parameter database
		String keychurnVolRange = "churnVolRange";
		float churnVolRange = MarketIndexParametersDB.getFloatValue(m_con, m_indexParametersDBName, keychurnVolRange);
		String keychurnPriceRange = "churnPriceRange";
		float churnPriceRange = MarketIndexParametersDB.getFloatValue(m_con, m_indexParametersDBName, keychurnPriceRange);
		
		for(int i = 1; i < rowCount; i++) //Starting at i=1 so that i can use i-1 in the first calculation 
		{
			/*
			 * this part gets churning ddays
			 * churning is defined as
			 * 1. price must close in the bottom half of its range
			 * 2. volume must be within 3% of the previous days volume
			 * 3. priceClose must be less than 102% of the previous day
			 * The next rules can be turned on or off from the parameter DB
			 * 4. priceClose must be greater than or equal to previous day
			 * 5. volume must be greater than avg daily
			 * 6. price must be on upswing over  previous 35 days
			 */
			
			float todaysHigh = analysisRows.get(i).getHigh();			
			float previousDaysHigh = analysisRows.get(i-1).getHigh();
			
			float todaysLow = analysisRows.get(i).getLow();			
			float previousDaysLow = analysisRows.get(i-1).getLow();
			
			float todaysClose = analysisRows.get(i).getClose();
			float previousDaysClose = analysisRows.get(i-1).getClose();
			
			long todaysVolume = analysisRows.get(i).getVolume();
			long previousDaysVolume = analysisRows.get(i-1).getVolume();

			if( todaysClose < (todaysHigh + todaysLow)/2 /*rule 1*/ &&
					todaysVolume >= previousDaysVolume*(1-churnVolRange) /*rule 2a*/ &&
					todaysVolume <= previousDaysVolume*(1+churnVolRange) /*rule 2b*/ &&
					todaysClose <= previousDaysClose*(1+churnPriceRange) /*rule 3*/)
			{
				churningDayCount++;
				analysisRows.get(i).setDDay(true);
				//MarketIndexAnalysisDB.addDDayStatus(ps, analysisRows.get(i).getPVD_id(), true);
			}
			//No else because they have all already been set to false by the d-day method.
		}
		
		
		return analysisRows;
	}
	
	public static void countDDaysInWindow(int dDayWindow) {
		System.out.println("          Looking at each day to see how many D-Dates at in the current window (" + dDayWindow + " days).");
		
		/* TODO START HERE TOMORROW
		 * 		Done 1. Pull from d-days table and join them to the table with the date
		 * 		Done 1b) Store this in a new type of class? New type could hold all the computational data needed
		 * 		2. For each loop of all the data
		 * 		3. As the loop progresses through each row, look back in the data the number of days in the window
		 * 			and see how many d-days there are
		 * 		4. Write the results to the database 
		*/
		try {
			//TODO This is all messed up because the analysisRows is messed up
			List<IndexAnalysisRow> analysisRows = MarketIndexAnalysisDB.getAllDDayData(m_con, m_index);
			
			int counter = 0;
			String pizza; 
			//This list starts with the newest date, which means the loop is goes back in time with each iteration 
			for(int i=0; i<analysisRows.size(); i++) {

				for(int j=i; j<i+dDayWindow && j<analysisRows.size(); j++) { //This loop starts at i and then goes back dDayWindow days adding up all the d days

					if(analysisRows.get(j).isDDay())
						analysisRows.get(i).addDDayCounter();
				}
				//insert row back into db
				//how to do this with batch statements?
				MarketIndexAnalysisDB.updateRow(m_con, m_index, analysisRows.get(i));
			}
			
			int i =5;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
