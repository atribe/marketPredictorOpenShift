package ibd.web.DBManagers;

import ibd.web.classes.MarketRetriever;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This subclass handles all database operations involving the price and volume data for each index
 * @author Allan
 *
 */
public class MarketIndexParametersDB extends GenericDBSuperclass{
	
	/**
	 * @param indexList
	 */
	public static void indexModelParametersInitialization(String[] indexParameterList) {
		//Get a database connection
		Connection connection = MarketIndexDB.getConnection();
		
		//Loop for each Price Volume DBs for each index
		for(String indexParams:indexParameterList) {
			/*
			 * Checking to see if a table with the indexParams name exists
			 * If it does, print to the command prompt
			 * if not create the table
			 */
			if(!tableExists(indexParams, connection)) {
				// Table does not exist, so create it
				String createTableSQL = "CREATE TABLE IF NOT EXISTS `" + indexParams + "_vars` " +
						"(id INTEGER not NULL," +
						" Var_Name VARCHAR(100)," +
						" Var_Value VARCHAR(50)," +
						" primary key (id))";
				createTable(createTableSQL, connection);
			}
			
			/*
			 * Checking to see if the tables are empty
			 * If they are populate them from Yahoo
			 * If not, check if they are up to date
			 * 		If not, update them
			 */
			if(tableEmpty(indexParams, connection)){
				//if table is empty
				//populate it
				populateFreshParamDB(connection, indexParams);
			}
		}
		
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static void populateFreshParamDB(Connection connection, String indexParams){
		
	}
}
