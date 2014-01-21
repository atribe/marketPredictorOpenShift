package ibd.web.analyzer;

import ibd.web.DBManagers.MarketIndexAnalysisDB;
import ibd.web.DBManagers.MarketIndexDB;
import ibd.web.DBManagers.MarketIndexParametersDB;
import ibd.web.DataObjects.IndexAnalysisRow;
import ibd.web.DataObjects.YahooDOHLCVARow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

		//Check and record all d days in the DB
		checkForDDays();
		
		//TODO put the churning D Day finder here
		checkForChurningDays();
		
			//Getting window length from parameter database
			String keydDayWindow = "dDayWindow";
			int dDayWindow = MarketIndexParametersDB.getIntValue(m_con, m_indexParametersDBName, keydDayWindow);
		
		//Counting up d-day that have fallen in a given window is handled in the following function
		countDDaysInWindow(dDayWindow);
	}

	public static void checkForDDays() {
		String tableName = MarketIndexAnalysisDB.getTableName(m_index);
		
		/* 
		 * TODO there should probably be a table that has the timestamp of when the pricevolume tables have been updated
		 * and when the parameter tables have been updated
		 * and when the analysis tables have been updated
		 * 
		 * Then when this method is run it would first check to see if there have been any changes to either table since the last update
		 */
		try {
			//Reset the table so that the data can be reanalyzed
			MarketIndexAnalysisDB.resetTable(m_con, tableName);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		System.out.println("          Checking to see if each day is a D-Day");
		
		List<YahooDOHLCVARow> rowsFromDB = MarketIndexDB.getDataBetweenIds(m_con, m_index, m_loopBeginId, m_loopEndId);
		
		int rowCount = rowsFromDB.size();
		int ddayCount=0;
		
		/*
		 * PreparedStatement prep. This will speed up this loop by not requiring a compiling of the query
		 * every iteration
		 */		
			String insertQuery = "INSERT INTO `" + tableName + "` "
					+ "(PVD_id,isDDay) VALUES(?,?)";
		
			PreparedStatement ps = null;
			try {
				ps = m_con.prepareStatement(insertQuery);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		for(int i = 1; i < rowCount; i++) //Starting at i=1 so that i can use i-1 in the first calculation 
		{
			/*
			 * D day rules
			 * 1. Volume Higher than the previous day
			 * 2. Price drops by X% (IBD states .2%)
			 */
			long todaysVolume = rowsFromDB.get(i).getVolume();
			long previousDaysVolume = rowsFromDB.get(i-1).getVolume();
			
			float todaysClose = rowsFromDB.get(i).getClose();
			float previousDaysClose = rowsFromDB.get(i-1).getClose();

			float closePercentChange = (todaysClose/previousDaysClose-1);
			float closePercentRequiredDrop = (float) -0.002; //TODO make this come from the parameter database
			try {
				if( todaysVolume > previousDaysVolume /*This is rule #1*/ && closePercentChange < closePercentRequiredDrop /*This is rule #1*/)
				{
					ddayCount++;
					MarketIndexAnalysisDB.addDDayStatus(ps, rowsFromDB.get(i).getId(), true);
				}
				else
				{
					MarketIndexAnalysisDB.addDDayStatus(ps, rowsFromDB.get(i).getId(), false);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void checkForChurningDays() {
		// TODO Create the Churning Day finder
		
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
