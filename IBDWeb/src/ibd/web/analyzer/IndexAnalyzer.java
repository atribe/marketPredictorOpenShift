package ibd.web.analyzer;

import ibd.web.DBManagers.MarketIndexDB;
import ibd.web.DBManagers.MarketIndexParametersDB;

import java.sql.Connection;
import org.joda.time.Days;
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
		if(MarketIndexParametersDB.getBooleanValue(m_con, m_indexParametersDBName, bufferConditionCheck1))
			m_bufferDays=50;
		else if(MarketIndexParametersDB.getBooleanValue(m_con, m_indexParametersDBName, bufferConditionCheck2))
			m_bufferDays=35;
		else
			m_bufferDays=0;
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
		
		m_loopEndId = MarketIndexDB.getIdByDate(m_con, m_index, endDate);
	}
	
	private static void setLoopBeginId() {
		String keyStartDate = "startDate";
		LocalDate startDate = MarketIndexParametersDB.getDateValue(m_con, m_indexParametersDBName, keyStartDate);
		
		int beginId = MarketIndexDB.getIdByDate(m_con, m_index, startDate);
		if(beginId-m_bufferDays<1) //if the buffer goes past where the data starts, make the loop start at earliest date
			m_loopBeginId = 1;
		else
			m_loopBeginId = beginId-m_bufferDays;
	}
	
	private static void distributionDayAnalysis(){
		//TODO Start here.
		//get the date between the end and beginning (modified by the buffer)
			//i think I need to change the setLoopDays function to get the new beginning date instead of the total loopDays
		//cycle through each date and look at price decrease and volume increase from one day to the next
		//When a date where the above is true...
			//Add it to the table?
			//Also have a running tally for a given period of time based on the parameters
			//
	}
}
