package ibd.web.analyzer;

import ibd.web.DBManagers.MarketIndexDB;
import ibd.web.DBManagers.MarketIndexParametersDB;

import java.sql.Connection;
import java.sql.Date;

import org.joda.time.Days;
import org.joda.time.LocalDate;

public class IndexAnalyzer {
	/*
	 * These variables are class member properties and once set they can be used by 
	 * all methods of the class. However, because this is a static class they need to be
	 * set every time you enter into the class from outside.
	 */
	
	//Database Connection
	static Connection m_con;
	
	//Database names
	static String m_index;
	static String m_indexParametersDBName;
	
	//member variables related to dates or number of days
	static private int m_bufferDays;
	static private Days m_loopDays;
	

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
		
		setLoopDays();
		
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
	
	private static void setLoopDays() {
		String keyStartDate = "startDate";
		String keyEndDate = "endDate";
		LocalDate startDate = MarketIndexParametersDB.getDateValue(m_con, m_indexParametersDBName, keyStartDate);
		LocalDate endDate = MarketIndexParametersDB.getDateValue(m_con, m_indexParametersDBName, keyEndDate);
		
		m_loopDays = Days.daysBetween(startDate, endDate);
		m_loopDays = m_loopDays.plus(m_bufferDays);
		// TODO figure out how to get the difference between two days and then add buffer days
		//Date adjStartDate = startDate - m_bufferDays
	}
	private static void distributionDayAnalysis(){
		
	}
}
