package DBManagers;

import ibd.web.DBManagers.GenericDBSuperclass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MarketIndexAnalysisDB extends GenericDBSuperclass{

	public static void IndexAnalysisTableInitialization(Connection connection,	String[] indexList) {
		System.out.println("");
		System.out.println("--------------------------------------------------------------------");
		System.out.println("Starting Index Analysis Database Initialization");
		
		//Iteration tracking variable for System.out.printing and debugging
		int interationCounter = 0;
		
		//Loop for each Price Volume DBs for each index
		for(String index:indexList) {
			interationCounter++;
			System.out.println("Loop Iteration " + interationCounter + ":");
			/*
			 * Checking to see if a table with the index name exists
			 * If it does, print to the command prompt
			 * if not create the table
			 */
			
			String indexAnalysisTableName = getTableName(index);
			
			System.out.println("     -Checking if table " + indexAnalysisTableName + " exists.");
			if(!tableExists(indexAnalysisTableName, connection)) {
				// Table does not exist, so create it
				String createTableSQL = "CREATE TABLE IF NOT EXISTS `" + indexAnalysisTableName + "` (" +
						" id INT not NULL AUTO_INCREMENT," +
						" PVD_id INT," +
						" IsDDay TINYINT(1)," +
						" DDayCounter BIGINT(50)," +
						" PRIMARY KEY (id)," +
						" FOREIGN KEY (PVD_id) REFERENCES `" + index + "`(id))";
				createTable(createTableSQL, connection, indexAnalysisTableName);
			}
		}
	}
	
	private static String getTableName(String index) {
		return index +"DataAnalysis";
	}

	public static void addDDay(Connection connection, String index, int id) throws SQLException {
		String tableName = getTableName(index);
		
		String insertQuery = "INSERT INTO `" + tableName + "` "
				+ "(PVD_id,isDDay) VALUES(?,1)";
		
		PreparedStatement ps = null;
		
		//prepare the statement
		ps = connection.prepareStatement(insertQuery);
		
		ps.setInt(1, id);
		
		ps.execute();
	}

	public static void countDDaysInWindow(Connection connection, String index, int dDayWindow) {
		// TODO START HERE TOMORROW
		
	}
}
