package ibd.web.DBManagers;

import ibd.web.DataObjects.IndexAnalysisRow;
import ibd.web.DataObjects.YahooDOHLCVARow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
				String createTableSQL = tableCreationString(index, indexAnalysisTableName);
				createTable(createTableSQL, connection, indexAnalysisTableName);
			}
		}
	}
	
	public static String getTableName(String index) {
		return index +"DataAnalysis";
	}

	public static String tableCreationString(String index, String indexAnalysisTableName) {
		 return "CREATE TABLE IF NOT EXISTS `" + indexAnalysisTableName + "` (" +
				" id INT not NULL AUTO_INCREMENT," +
				" PVD_id INT," +
				" IsDDay TINYINT(1)," +
				" DDayCounter BIGINT(50)," +
				" PRIMARY KEY (id)," +
				" FOREIGN KEY (PVD_id) REFERENCES `" + index + "`(id))";
	}
	
	public static void addDDayStatus(PreparedStatement ps, int id, boolean isDDay) throws SQLException {
		ps.setInt(1, id);
		if(isDDay)
			ps.setInt(2, 1);
		else
			ps.setInt(2, 0);
		ps.execute();
	}

	public static List<IndexAnalysisRow> getAllDDayData(Connection connection, String indexTableName) throws SQLException {
		String tableName = getTableName(indexTableName);
		
		String query = "SELECT A.PVD_id, I.Date, A.IsDDay" 
				+ " FROM `" + indexTableName + "` I"
				+ " INNER JOIN `" + tableName + "` A ON I.id = A.PVD_id"
				+ " ORDER BY I.Date DESC";
		PreparedStatement ps = null;
		ps = connection.prepareStatement(query);
		ResultSet rs = ps.executeQuery();
		
		List<IndexAnalysisRow> AnalysisRows = new ArrayList<IndexAnalysisRow>();
		
		while (rs.next()) {
			IndexAnalysisRow singleResult = new IndexAnalysisRow();
			singleResult.setPVD_id(rs.getInt("PVD_id"));
			singleResult.setDate(rs.getDate("Date"));
			boolean isDDay;
			if(rs.getInt("IsDDay")==1)
				isDDay=true;
			else
				isDDay=false;
			singleResult.setDDay(isDDay);
			
			AnalysisRows.add(singleResult);
		}
		
		return AnalysisRows;
	}

	public static void updateRow(Connection connection, String indexTableName, IndexAnalysisRow analysisRow) throws SQLException {
		String tableName = getTableName(indexTableName);
		
		String updateQuery = "UPDATE `" + tableName + "`"
				+ " SET DDayCounter=?"
				+ " WHERE PVD_id=?";
		PreparedStatement ps = connection.prepareStatement(updateQuery);
		ps.setInt(1, analysisRow.getdDayCounter());
		ps.setInt(2, analysisRow.getPVD_id());
		int rowsUpdated = ps.executeUpdate();
		rowsUpdated = rowsUpdated;//just something to stop the debugging
	}
}