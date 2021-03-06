package ibd.web.classes;

import ibd.web.Resource.LoadProperties;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MarketDB {

	/**
	 * @description Static method for creation of database connection
	 * @return A database Connection
	 */
	public static Connection getConnection() {
		Connection connection = null;
		String host, port, dbURL, username, password;
		try {
			//Loading the JDBC MySQL drivers that are used by java.sql.Connection
			Class.forName("com.mysql.jdbc.Driver");

			// ************For Open Shift Account************	  
			if(LoadProperties.environment.trim().equalsIgnoreCase(/*"production"*/"notproduction")){
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
			//System.out.println("Connection established");
			ibd.web.Constants.Constants.logger.info("Connection Established in MarketDB.java with teedixindices");
		} catch (ClassNotFoundException e) {
			ibd.web.Constants.Constants.logger.info("Database Driver not found in MarketDB.java with teedixindices "+e);
		} catch (SQLException e) {
			ibd.web.Constants.Constants.logger.info("Exception loading Database Driver in MarketDB.java with teedixindices "+e);
			//System.out.println("Error loading database driver: " + e.getMessage());
		}
		return connection;
	}


	public static Connection getConnectionIBD50() {
		Connection connection = null;
		String host, port, dbURL, username, password;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// ************For Open Shift Account************	    

			if(LoadProperties.environment.trim().equalsIgnoreCase("production")){
				host = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
				port = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
				dbURL = "jdbc:mysql://"+host+":"+port+"/teedixibd50";
				username = "adminQRungBu";
				password = "BdaTdanJuw9n";
			}else{


				// ************For Local Account************

				host = "localhost";
				port="3306";
				dbURL = "jdbc:mysql://"+host+":"+port+"/moneytreeibd50";
				username = "root";
				password = "";
			}


			connection = DriverManager.getConnection(dbURL, username, password);
			//System.out.println("Connection established");
			ibd.web.Constants.Constants.logger.info("Connection Established in MarketDB.java with teedixibd50");
		} catch (ClassNotFoundException e) {
			ibd.web.Constants.Constants.logger.info("Database Driver not found in MarketDB.java with teedixibd50"+e);
		} catch (SQLException e) {
			ibd.web.Constants.Constants.logger.info("Exception loading Database Driver in MarketDB.java with teedixibd50"+e);
			//System.out.println("Error loading database driver: " + e.getMessage());
		}
		return connection;
	}

	public static Connection getConnectionIBD50PricesVolumes() {
		Connection connection = null;
		String host, port, dbURL, username, password;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// ************For Open Shift Account************	    

			if(LoadProperties.environment.trim().equalsIgnoreCase("production")){
				host = System.getenv("OPENSHIFT_MYSQL_DB_HOST");
				port = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
				dbURL = "jdbc:mysql://"+host+":"+port+"/teedixibd50pricesvolumes";
				username = "adminQRungBu";
				password = "BdaTdanJuw9n";
			}else{


				// ************For Local Account************

				host = "localhost";
				port="3306";
				dbURL = "jdbc:mysql://"+host+":"+port+"/moneytreeibd50pricesvolumes";
				username = "root";
				password = "";
			}


			connection = DriverManager.getConnection(dbURL, username, password);
			//System.out.println("Connection established");
			ibd.web.Constants.Constants.logger.info("Connection Established in MarketDB.java with teedixibd50pricesvolumes");
		} catch (ClassNotFoundException e) {
			ibd.web.Constants.Constants.logger.info("Database Driver not found in MarketDB.java with teedixibd50pricesvolumes"+e);
		} catch (SQLException e) {
			ibd.web.Constants.Constants.logger.info("Exception loading Database Driver in MarketDB.java with teedixibd50pricesvolumes"+e);
			//System.out.println("Error loading database driver: " + e.getMessage());
		}
		return connection;
	}

	/**
	 *
	 * @param connection
	 * @param sym
	 * @param date 
	 * @return boolean true if match is found else false. Means if there is data for this <code>date</code>
	 * @throws SQLException
	 */
	public static synchronized boolean isMatch(Connection connection, String sym, Date date) throws SQLException {

		String query = "SELECT * FROM `" + sym.toLowerCase() + "` WHERE date='" + date + "'";
		Statement statement = connection.createStatement();
		ResultSet results = statement.executeQuery(query);
		boolean datePresent = results.next();
		results.close();
		statement.close();
		return datePresent;
	}

	/**
	 *
	 * @param connection
	 * @param sym
	 * @param startDate
	 * @param endDate
	 * @return Object of Class <code>Data</code>
	 * @throws SQLException
	 */
	public static synchronized Data getRecord(Connection connection, String sym, Date startDate, Date endDate) throws SQLException {

		String query;
		query = "SELECT * FROM `" + sym.toLowerCase() + "` WHERE date>='" + startDate + "' AND date<='" + endDate + "' ORDER BY date DESC";
		//System.out.println(query);
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		ibd.web.Constants.Constants.logger.info("executeQuery successful for "+sym.toLowerCase()+ " where startDate="+startDate+" and endDate="+endDate);
		//System.out.println("executeQuery successful for "+sym.toLowerCase()+ " where startDate="+startDate+" and endDate="+endDate);

		ArrayList<Date> dates1 = new ArrayList<Date>();
		ArrayList<Float> opens1 = new ArrayList<Float>();
		ArrayList<Float> highs1 = new ArrayList<Float>();
		ArrayList<Float> lows1 = new ArrayList<Float>();
		ArrayList<Float> closes1 = new ArrayList<Float>();
		ArrayList<Long> volumes1 = new ArrayList<Long>();

		rs.first();

		while (rs.isAfterLast() == false) {
			dates1.add(rs.getDate(1));
			opens1.add(rs.getFloat(2));
			highs1.add(rs.getFloat(3));
			lows1.add(rs.getFloat(4));
			closes1.add(rs.getFloat(5));
			volumes1.add(rs.getLong(6));
			rs.next();
		}

		return new Data(dates1, opens1, highs1, lows1, closes1, volumes1);
	}

	/**
	 *
	 * @param connection
	 * @param sym
	 * @param date
	 * @param open
	 * @param high
	 * @param low
	 * @param close
	 * @param volume
	 * @return Integer if record is added 1 else 0 or any negative value (TO-DO)
	 * @throws SQLException
	 */
	public static synchronized int addRecord(Connection connection, String sym, Date[] date, float[] open,
			float[] high, float[] low, float[] close, long[] volume) throws SQLException {

		//System.out.println(date[0]);
		Statement statement = null;
		int status = 0;
		String query = "Insert into `" + sym.toLowerCase() + "` (Date,Open,High,Low,Close,Volume) Values";
		for (int n = 0; n < date.length ; n++) {
			query = query + "('" + date[n] + "','" + open[n] + "','" + high[n] + "','" + low[n] + "','" + close[n] + "','" + volume[n] + "')";
			if (n<date.length-1){
				query=query+",";
			}
			//	    }else if (n==date.length-1){
			//		query=query+";";
			//	    }
		}
		statement = connection.createStatement();
		status = statement.executeUpdate(query);


		statement.close();
		return status;
	}

	/**
	 *
	 * @param connection
	 * @param priceTable
	 * @param volumeTable
	 * @param symbol
	 * @return Integer if record is deleted. (TO-DO)
	 * @throws SQLException
	 */
	public static synchronized int deleteRecord(Connection connection,
			String priceTable, String volumeTable, String symbol) throws SQLException {
		String query =
				"DELETE FROM " + priceTable.toLowerCase() +
				" WHERE Symbol = '" + symbol + "'";
		Statement statement = connection.createStatement();
		int status = statement.executeUpdate(query);
		query =
				"DELETE FROM " + volumeTable.toLowerCase() +
				" WHERE Symbol = '" + symbol + "'";
		statement = connection.createStatement();
		status = statement.executeUpdate(query);
		statement.close();
		return status;
	}

	/**
	 *
	 * @param connection
	 * @param priceTable
	 * @param volumeTable
	 * @return Integer if all records are deleted (TO-DO)
	 * @throws SQLException
	 */
	public static synchronized int deleteAll(Connection connection, String priceTable, String volumeTable) throws SQLException {
		String query = null;
		Statement statement = null;
		int status = 0;

		query = "DELETE FROM " + priceTable.toLowerCase() + " WHERE ranking >= 0";
		statement = connection.createStatement();
		status = statement.executeUpdate(query);
		query = "DELETE FROM " + volumeTable.toLowerCase() + " WHERE ranking >= 0";
		statement = connection.createStatement();
		status = statement.executeUpdate(query);

		statement.close();
		return status;
	}
}
