package ibd.web.model;

import java.sql.Connection;
import java.sql.SQLException;

import ibd.web.DBManagers.MarketIndexDB;

public class MarketIndicesModel {
	
	//							  Dow	 Nasdaq	 S&P500
	static String[] indexList = {/*"^DJI",*/"^IXIC","^GSPC","^SML","^MID"};

	public static void main() {
		/*
		 * Dabase initialization Section
		 */
		//connect to the db
		Connection connection = MarketIndexDB.getConnection();
		
		//Initialize the price/volume databases for each index 
		MarketIndexDB.priceVolumeDBInitialization(connection, indexList);
				
		
		MarketIndexDB.indexModelParametersInitialization(connection, indexList);		
		//Loop for Model parameters for each index
			//if table !exists
				//create it
	
			//if tables are empty
				//populate it
					//initial values would be the best values based on last optimization
		//End Model Parameter Loop
		
		//Market Index Models
			/*
			 * Models runs are not looped because you may want to run or optimize them one at a time
			 * I'll figure out this code after I figure out the above
			 */
			//Run model for Dow
			//Run model for SP500
			//Run model for Nasdaq
		
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
