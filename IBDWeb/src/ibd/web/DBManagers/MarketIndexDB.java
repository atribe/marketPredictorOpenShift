package ibd.web.DBManagers;

import ibd.web.Resource.LoadProperties;
import ibd.web.classes.Data;
import ibd.web.classes.MarketRetriever;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MarketIndexDB {

	public static Connection getConnection() {
		Connection connection = null;
		String host, port, dbURL, username, password;
		try {
			//Loading the JDBC MySQL drivers that are used by java.sql.Connection
			Class.forName("com.mysql.jdbc.Driver");

			// ************For Open Shift Account************	  
			if(LoadProperties.environment.trim().equalsIgnoreCase("production")){
				host = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
				port = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
				dbURL = "jdbc:mysql://"+host+":"+port+"/teedixindices";
				username = "adminQRungBu";
				password = "BdaTdanJuw9n";
			}else{
				// ************For Local Account************	
				host = "localhost";
				port="3306";
				dbURL = "jdbc:mysql://"+host+":"+port+"/moneytreeindices";
				username = "root";
				password = "";
			}

			connection = DriverManager.getConnection(dbURL, username, password);

			System.out.println("Connection established");
			ibd.web.Constants.Constants.logger.info("Connection Established in MarketDB.java with teedixindices");
		} catch (ClassNotFoundException e) { //Handle errors for Class.forName
			System.out.println("Database Driver not found in MarketDB.java with teedixindices "+e);
			ibd.web.Constants.Constants.logger.info("Database Driver not found in MarketDB.java with teedixindices "+e);
		} catch (SQLException ex){
			// handle any errors
			System.out.println("Exception loading Database Driver in MarketDB.java with teedixindices");
			System.out.println("Did you forget to turn on Apache and MySQLL again? From Exception:");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			ibd.web.Constants.Constants.logger.info("Exception loading Database Driver in MarketDB.java with teedixindices "+ex);
		}
		return connection;
	}

	public static synchronized void priceVolumeDBInitialization(Connection connection, String[] indexList) {
		//Loop for each Price Volume DBs for each index
		for(String index:indexList) {
			/*
			 * Checking to see if a table with the index name exists
			 * If it does, print to the command prompt
			 * if not create the table
			 */
			if(!tableExists(index, connection)) {
				// Table does not exist, so create it
				String createTableSQL = "CREATE TABLE IF NOT EXISTS `" + index + "` (" +
						" Date DATE not NULL," +
						" Open FLOAT(20)," +
						" High FLOAT(20)," +
						" Low FLOAT(20)," +
						" Close FLOAT(20)," +
						" Volume BIGINT(50)," +
						" primary key (Date))";
				createTable(createTableSQL, connection);
			}

			/*
			 * Checking to see if the tables are empty
			 * If they are populate them from Yahoo
			 * If not, check if they are up to date
			 * 		If not, update them
			 */
			if(tableEmpty(index, connection)){
				//if table is empty
				//populate it
				MarketRetriever.populateFreshDB(connection, index);
			}



			//else if
			//if tables !(up to date)
			//update it
			//End Price Volume DB Loop
		}
		//needs to be made real, set so the method would not give me an error

	}
	private static boolean tableExists(String tableName, Connection connection){
		boolean tableExists = false;

		DatabaseMetaData metadata = null;
		ResultSet tables = null;

		try {
			metadata = connection.getMetaData();
			tables = metadata.getTables(null, null, tableName, null);

			if (tables.next()) {
				// Table exists
				System.out.println(
						"   "+tables.getString("TABLE_CAT") 
						+ ", "+tables.getString("TABLE_SCHEM")
						+ ", "+tables.getString("TABLE_NAME")
						+ ", "+tables.getString("TABLE_TYPE")
						+ ", "+tables.getString("REMARKS")
						+ ", already exists.");
				tableExists = true;
			}
			else {
				tableExists = false;
			}
		} catch (SQLException ex){
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed
			if (tables != null) {
				try {
					tables.close();
				} catch (SQLException sqlEx) { } // ignore

				tables = null;
			}
		}

		return tableExists;
	}
	private static synchronized boolean createTable(String createTableSQL, Connection connection){
		int status=0;
		Statement createStatement = null;
		try {
			createStatement = connection.createStatement();
			status = createStatement.executeUpdate(createTableSQL);
		} catch (SQLException ex){
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed

			if (createStatement != null) {
				try {
					createStatement.close();
				} catch (SQLException sqlEx) { } // ignore

				createStatement = null;
			}
		}
		if (status>0)
			return true;
		else
			return false;
	}
	private static boolean tableEmpty(String tableName, Connection connection){
		boolean empty = true;
		Statement queryStatement = null;
		ResultSet rs = null;

		try {
			queryStatement = connection.createStatement();
			rs = queryStatement.executeQuery("SELECT * FROM `" + tableName + "`");
			while (rs.next())
			{
				empty = false;
			}
		} catch (SQLException ex){
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		finally {
			// it is a good idea to release
			// resources in a finally{} block
			// in reverse-order of their creation
			// if they are no-longer needed
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException sqlEx) { } // ignore

				rs = null;
			}
			if (queryStatement != null) {
				try {
					queryStatement.close();
				} catch (SQLException sqlEx) { } // ignore

				queryStatement = null;
			}
		}
		return empty;
	}

	public static void addRecord(Connection connection, String index, Data priceVolumeData) {
		String insertQuery = "INSERT INTO `" + index + "` "
							+ "(Date,Open,High,Low,Close,Volume) VALUES"
							+ "(?,?,?,?,?,?)";
		PreparedStatement ps=null;
		try {
			ps = connection.prepareStatement(insertQuery);
			
			for (int i = 0; i < priceVolumeData.getRowCount() ; i++) {
				ps.setDate(1, priceVolumeData.dateData[i]);
				ps.setFloat(2,  priceVolumeData.priceDataOpen[i]);
				ps.setFloat(3,  priceVolumeData.priceDataHigh[i]);
				ps.setFloat(4,  priceVolumeData.priceDataLow[i]);
				ps.setFloat(5,  priceVolumeData.priceDataClose[i]);
				ps.setFloat(6,  priceVolumeData.volumeData[i]);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println(e);
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException sqlEx) { } // ignore

				ps = null;
			}
		}
		
		

		int abc = 55;
	}
}
