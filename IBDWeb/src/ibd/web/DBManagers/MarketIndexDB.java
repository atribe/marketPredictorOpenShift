package ibd.web.DBManagers;

import ibd.web.classes.Data;
import ibd.web.classes.MarketRetriever;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.joda.time.LocalDate;

public class MarketIndexDB extends GenericDBSuperclass {

	public static synchronized void priceVolumeDBInitialization(Connection connection, String[] indexList) {		
		System.out.println("");
		System.out.println("--------------------------------------------------------------------");
		System.out.println("Starting Market Index Database Initialization");

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
			System.out.println("     -Checking if table " + index + " exists.");
			if(!tableExists(index, connection)) {
				// Table does not exist, so create it
				String createTableSQL = "CREATE TABLE IF NOT EXISTS `" + index + "` (" +
						" id INT not NULL AUTO_INCREMENT," +
						" Date DATE not NULL," +
						" Open FLOAT(20)," +
						" High FLOAT(20)," +
						" Low FLOAT(20)," +
						" Close FLOAT(20)," +
						" Volume BIGINT(50)," +
						" PRIMARY KEY (id))";
				createTable(createTableSQL, connection, index);
			}

			/*
			 * Checking to see if the tables are empty
			 * If they are populate them from Yahoo
			 * If not, check if they are up to date
			 * 		If not, update them
			 */
			System.out.println("     -Checking if table " + index + " is empty.");
			if(tableEmpty(index, connection)){
				//if table is empty
				//populate it
				populateFreshDB(connection, index);
			}

			System.out.println("     -Checking to see if table " + index +" is up to date.");
			int indexDaysBehind = getIndexDaysBehind(connection, index);
			if(indexDaysBehind>0)
			{
				updateIndexDB(connection, index, indexDaysBehind);
			}
		}
		System.out.println("--------------------------------------------------------------------");
	}

	public static void addRecordsFromData(Connection connection, String index, Data priceVolumeData) {
		//This query ignores duplicate dates
		String insertQuery = "INSERT IGNORE INTO `" + index + "` "
				+ "(Date,Open,High,Low,Close,Volume) VALUES"
				+ "(?,?,?,?,?,?)";
		PreparedStatement ps=null;
		int batchSize = 200;
		try {
			ps = connection.prepareStatement(insertQuery);

			for (int i = 0; i < priceVolumeData.getRowCount() ; i++) {
				ps.setDate(1, priceVolumeData.dateData[i]);
				ps.setFloat(2,  priceVolumeData.priceDataOpen[i]);
				ps.setFloat(3,  priceVolumeData.priceDataHigh[i]);
				ps.setFloat(4,  priceVolumeData.priceDataLow[i]);
				ps.setFloat(5,  priceVolumeData.priceDataClose[i]);
				ps.setFloat(6,  priceVolumeData.volumeData[i]);
				ps.addBatch();
				if (i % batchSize == 0) //if i/batch size remainder == 0 execute batch
				{
					ps.executeBatch();
				}
			}
			//Execute the last batch, in case the last value of i isn't a multiple of batchSize
			ps.executeBatch();
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
	}

	private static int getIndexDaysBehind(Connection connection, String index) {

		//initializing variables
		//java.sql.Date newestDateInDB=null;
		LocalDate newestDate=null;

		String getNewestDateInDBQuery = "SELECT Date FROM `" + index + "` "
				+ "ORDER BY Date "
				+ "DESC LIMIT 1";
		PreparedStatement ps=null;
		ResultSet rs = null;

		try {
			// Querying the database for the newest date
			ps = connection.prepareStatement(getNewestDateInDBQuery);
			rs = ps.executeQuery();

			if (!rs.next() ) {
				System.out.println("no data");
				//java.util.Calendar cal = java.util.Calendar.getInstance(); 
				//newestDateInDB = new Date(cal.getTimeInMillis());
				newestDate = new LocalDate();
			} else {
				//newestDateInDB = rs.getDate("Date");
				newestDate = LocalDate.fromDateFields(rs.getDate("Date"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println(e);
		} catch(NullPointerException e) {
			// probably don't bother doing clean up
		} finally {
			//This if statement doesn't make much sense
			try {
				if(rs!=null) {
					rs.close();
				}
				if(ps!=null) {
					ps.close();
				}
			} catch (SQLException sqlEx) { } // ignore
		}
		//calls the getNumberOfDaysFromNow method from market retriever and immediately returns
		//how many behind the database is from the current date
		//System.out.println("          The newest date in the database is " + newestDateInDB.toString() + ".");
		System.out.println("          The newest date in the database is " + newestDate.toString() + ".");

		int DBDaysTilNow = MarketRetriever.getNumberOfDaysFromNow(newestDate);
		//System.out.println("          Which is " + DBDaysTilNow + " days out of date.");
		System.out.println("          Which is " + DBDaysTilNow + " days out of date.");
		return DBDaysTilNow;//DBDaysTilNow;
	}

	public static void populateFreshDB(Connection connection, String index) {
		//Container to hold the downloaded data
		Data priceVolumeData = null;

		//This date represents the beginning of time as far as any of the indexes go
		LocalDate beginningDate = new LocalDate("1920-01-01");

		//calculates the number of days from today back to beginning date
		int numDays = MarketRetriever.getNumberOfDaysFromNow(beginningDate);

		//Creates a yahoo URL given the index symbol from now back a given number of days
		String URL = MarketRetriever.getYahooURL(index, numDays);

		try {
			priceVolumeData = MarketRetriever.dataParser(URL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// extract price and volume data for URL, # of yahoo days
		addRecordsFromData(connection, index, priceVolumeData);
	}

	public static void updateIndexDB(Connection connection, String index,int indexDaysBehind) {
		//Container to hold the downloaded data
		Data priceVolumeData = null;

		//Creates a yahoo URL given the index symbol from now back a given number of days
		String URL = MarketRetriever.getYahooURL(index, indexDaysBehind);

		try {
			priceVolumeData = MarketRetriever.dataParser(URL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// extract price and volume data for URL, # of yahoo days
		addRecordsFromData(connection, index, priceVolumeData);
	}

	/**
	 * @param connection
	 * @param tableName
	 * @param Date
	 * @return
	 */
	public static int getIdByDate(Connection connection, String tableName, LocalDate Date){
		int value = 0;
		String query = "SELECT id FROM `" + tableName + "`"
				+ " WHERE Date=?";

		try {
			PreparedStatement selectStatement = connection.prepareStatement(query);
			selectStatement.setString(1, Date.toString());
			ResultSet rs = selectStatement.executeQuery();
			if(rs.next()) {
				value = rs.getInt("id");
			} else {
				System.out.println("     The date of " + Date.toString() + " not found in the database.");
				System.out.println("          Let me check the preceeding couple of days in case you chose a weekend or holiday.");
				for(int i = 1;i<7;i++)
				{
					selectStatement.setString(1, Date.minusDays(i).toString());
					rs = selectStatement.executeQuery();
					if(rs.next())
					{
						value = rs.getInt("id");
						System.out.println("          Looks like I found one...and you got all worried for nothing.");
						break;
					}
					else if(i==6)
					{
						System.out.println("I didn't find an earlier date, so I'll just choose the first date in the data set");
						value=1;
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("There was an error in the getIdByDate method. And that error is: ");
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return value;
	}
}