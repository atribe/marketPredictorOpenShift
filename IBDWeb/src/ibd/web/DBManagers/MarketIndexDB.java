package ibd.web.DBManagers;

import ibd.web.Resource.LoadProperties;

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
		} catch (SQLException se) { //Handle errors for JDBC
			System.out.println("Exception loading Database Driver in MarketDB.java with teedixindices "+se);
			System.out.println("Did you forget to turn on Apache and MySQLL again?");
			ibd.web.Constants.Constants.logger.info("Exception loading Database Driver in MarketDB.java with teedixindices "+se);
		}

		return connection;
	}

	public static synchronized void priceVolumeDBInitialization(Connection connection, String[] indexList) {
		
		//Loop for each Price Volume DBs for each index
		for(String index:indexList) {

			//if table !exists
			DatabaseMetaData metadata = null;
			ResultSet tables = null;
			try {
				metadata = connection.getMetaData();
				tables = metadata.getTables(null, null, index, null);

				if (tables.next()) {
					// Table exists
					System.out.println(
					        "   "+tables.getString("TABLE_CAT") 
					       + ", "+tables.getString("TABLE_SCHEM")
					       + ", "+tables.getString("TABLE_NAME")
					       + ", "+tables.getString("TABLE_TYPE")
					       + ", "+tables.getString("REMARKS")
					       + ", already exists."); 
				}
				else {
					// Table does not exist, so create it
					String createTableStatement = "CREATE TABLE IF NOT EXISTS '" + index + "' (" +
							" Date DATE not NULL," +
							" Open FLOAT(20)," +
							" High FLOAT(20)," +
							" Low FLOAT(20)," +
							" Close FLOAT(20)," +
							" Volume BIGINT(50)," +
							" primary key (Date))";
					Statement statement = connection.createStatement();
					int status = statement.executeUpdate(createTableStatement);
				}

			} catch (SQLException e) { //Handle errors for JDBC
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e);
			}

			//if tables are empty
			//populate it
			//else if
			//if tables !(up to date)
			//update it
			//End Price Volume DB Loop
		}
		//needs to be made real, set so the method would not give me an error

	}
}
