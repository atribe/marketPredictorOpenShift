package ibd.web.analyzer;

import ibd.web.DBManagers.MarketIndexDB;
import ibd.web.DBManagers.MarketIndexParametersDB;
import ibd.web.DataObjects.YahooDOHLCVARow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.joda.time.LocalDate;

import DBManagers.MarketIndexAnalysisDB;

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

		System.out.println("");
		System.out.println("--------------------------------------------------------------------");
		System.out.println("Starting Index Analyzer");

		m_con = connection;
		m_index = index;
		m_indexParametersDBName = indexParametersDBName;

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
		/*TODO Start here.
		 * INPUT m_con
		 * INPUT Index name
		 */
		
		//1.Pull data between the start and end ids
		//2.Cycle through the data and look for lower price on higher volume than the preceeding day
		//3.When a date where the above is true...
			//3a)Add it to the table?
			//3b)Also have a running tally for a given period of time based on the parameters
		//PriceVolumeData pvd = MarketIndexDB.getDataBetweenIds(m_con, m_index, m_loopBeginId, m_loopEndId);
		List<YahooDOHLCVARow> rowsFromDB = MarketIndexDB.getDataBetweenIds(m_con, m_index, m_loopBeginId, m_loopEndId);
		
		int rowCount = rowsFromDB.size();
		
		int ddayCount=0;
		
		/*
		 * PreparedStatement prep. This will speed up this loop by not requiring a compiling of the query
		 * every iteration
		 */
			String tableName = MarketIndexAnalysisDB.getTableName(m_index);
		
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
			
			if( todaysVolume > previousDaysVolume /*This is rule #1*/ && closePercentChange < closePercentRequiredDrop /*This is rule #1*/)
			{
				ddayCount++;
				
				try {
					MarketIndexAnalysisDB.addDDayStatus(ps, rowsFromDB.get(i).getId(), true);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try {
					MarketIndexAnalysisDB.addDDayStatus(ps, rowsFromDB.get(i).getId(), false);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		//Getting window length from parameter database
		String keydDayWindow = "dDayWindow";
		int dDayWindow = MarketIndexParametersDB.getIntValue(m_con, m_indexParametersDBName, keydDayWindow);

		countDDaysInWindow(dDayWindow);
		int k = 5;
		k++;

	}
	public static void countDDaysInWindow(int dDayWindow) {
		/* TODO START HERE TOMORROW
		 * 		1. Pull from d-days table and join them to the table with the date
		 * 		1b) Store this in a new type of class? New type could hold all the computational data needed
		 * 		2. For each loop of all the data
		 * 		3. As the loop progresses through each row, look back in the data the number of days in the window
		 * 			and see how many d-days there are
		 * 		4. Write the results to the database 
		*/
		try {
			MarketIndexAnalysisDB.getAllDDayData(m_con, m_index);
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
