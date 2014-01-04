package ibd.web.DBManagers;

import ibd.web.classes.MarketRetriever;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
		
		//Loop for each each index to create a databse to hold model parameters
		for(String indexParams:indexParameterList) {
			/*
			 * Checking to see if a table with the indexParams name exists
			 * If it does, print to the command prompt
			 * if not create the table
			 */
			if(!tableExists(indexParams, connection)) {
				// Table does not exist, so create it
				String createTableSQL = "CREATE TABLE IF NOT EXISTS `" + indexParams + "` " +
						"(Var_Name VARCHAR(100) PRIMARY KEY," +
						" Var_Value VARCHAR(50))";
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
		HashMap<String, String> SP500Vars = new HashMap<String, String>();
		SP500Vars.put("fileName", "ResultsSP500.txt");
		SP500Vars.put("index", "^GSPC");
		SP500Vars.put("startDate", "1980-01-01");
		SP500Vars.put("endDate", "2009-12-31");
		SP500Vars.put("dDayParam", "10");
		SP500Vars.put("churnVolRange", "0.03");
		SP500Vars.put("churnPriceRange", "0.02");
		SP500Vars.put("chrunPriceCloseHigherOn", "true");
		SP500Vars.put("churnAVG50On", "true");
		SP500Vars.put("churnPriceTrend350n", "false");
		SP500Vars.put("churnPriceTrend35", "0.007");
		SP500Vars.put("volVolatilityOn", "false");
		SP500Vars.put("volumeMult", "1.1");
		SP500Vars.put("volMultTop", "1.1");
		SP500Vars.put("volMultBot", "1.1");
		SP500Vars.put("priceVolatilityOn", "true");
		SP500Vars.put("priceMult", "1.013");
		SP500Vars.put("priceMultTop", "1.014");
		SP500Vars.put("priceMultBot", "1.012");
		SP500Vars.put("rDaysMin", "4");
		SP500Vars.put("rDaysMax", "18");
		SP500Vars.put("pivotTrend35On", "false");
		SP500Vars.put("pivotTrend35", "-0.003");
		SP500Vars.put("rallyVolAVG50On", "false");
		SP500Vars.put("rallyPriceHighOn", "true");
		
		//Get a set of the entries
		Set keys = SP500Vars.keySet();
		
		//Get an iterator
		Iterator itr = keys.iterator();
		
		//Add each entry to the DB
		while(itr.hasNext()) {
			String key = (String)itr.next();
			String value = (String)SP500Vars.get(key);
			addVarPairRecord(connection, indexParams, key, value);
		}
		
		
	}
	private static void addVarPairRecord(Connection connection, String indexParams, String key, String value) {
		String insertQuery = "INSERT ON DUPLICATE KEY UPDATE INTO `" + indexParams + "` "
				+ "(?,?)";
		PreparedStatement ps=null;
		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, key);
			ps.setString(2, value);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException sqlEx) { } // ignore

				ps = null;
			}
		}
	}
}
