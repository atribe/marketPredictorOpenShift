package ibd.web.DBManagers;

import ibd.web.Resource.LoadProperties;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is the Parent class to all database classes
 * @author Allan
 *
 */
public class GenericDBSuperclass {

	
	/**
	 * Establishes a connection to the database
	 * @return connection
	 */
	public static Connection getConnection() {
		Connection connection = null;
		String host, port, dbURL, username, password, DBName;
		try {
			//Loading the JDBC MySQL drivers that are used by java.sql.Connection
			Class.forName("com.mysql.jdbc.Driver");

			// ************For Open Shift Account************	  
			if(LoadProperties.environment.trim().equalsIgnoreCase("production")){
				DBName = "teedixindices";
				host = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
				port = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
				dbURL = "jdbc:mysql://"+host+":"+port+"/" + DBName;
				username = "adminQRungBu";
				password = "BdaTdanJuw9n";
			}else{
				// ************For Local Account************
				DBName = "moneytreeindices";
				host = "localhost";
				port="3306";
				dbURL = "jdbc:mysql://"+host+":"+port+"/" + DBName;
				username = "root";
				password = "";
			}

			connection = DriverManager.getConnection(dbURL, username, password);

			System.out.println("     Connection established to " + DBName);
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

	protected static boolean tableExists(String tableName, Connection connection){
		boolean tableExists = false;

		DatabaseMetaData metadata = null;
		ResultSet tables = null;

		try {
			metadata = connection.getMetaData();
			tables = metadata.getTables(null, null, tableName, null);

			if (tables.next()) {
				// Table exists
				System.out.println(
						"          " + tables.getString("TABLE_TYPE") 
						+ " " + tables.getString("TABLE_NAME")
						+ " already exists.");
				tableExists = true;
			}
			else {
				tableExists = false;
				System.out.println("          Table " + tableName + "does not yet exist, let me try and create that for you.");
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

	protected static synchronized boolean createTable(String createTableSQL, Connection connection, String tableName){
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
		{
			System.out.println("          Table '" + tableName + "' wasn't created");
			return true;
		}
		else
		{
			System.out.println("          Table '" + tableName + "' has been created");
			return false;
		}
	}

	protected static boolean tableEmpty(String tableName, Connection connection){
		boolean empty = true;
		Statement queryStatement = null;
		ResultSet rs = null;
		
		int i = 0;

		try {
			queryStatement = connection.createStatement();
			rs = queryStatement.executeQuery("SELECT * FROM `" + tableName + "`");
			while (rs.next())
			{
				empty = false;
				i++;
				if(i>2)
					break;
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
		if(empty)
			System.out.println("          Table '" + tableName +"' is empty.");
		else
			System.out.println("          Table '" + tableName +"' has some stuff in it.");
		return empty;
	}

}
