package ibd.web.classes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MarketDBSetup {

	/**
	 * 
	 * @description Connects to a database and then creates 5 tables, one for each market index
	 * @param args
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, URISyntaxException, SQLException, ClassNotFoundException {

		Connection connection = MarketDB.getConnection();

		Statement statement = null;
		String query;
		String preparedSQL;
		int status = 0;
		String[] list = {"`^SML`","`^MID`","`^GSPC`","`^DJI`","`^IXIC`"};

		//preparedSQL = "create table ? (Date DATE, Open FLOAT(20), High FLOAT(20), Low FLOAT(20), Close FLOAT(20), Volume BIGINT(50), primary key(Date));";
		//PreparedStatement ps = connection.prepareStatement(preparedSQL);

		for(int i=0;i<list.length;i++){
			query = "create table"+list[i]+" (Date DATE, Open FLOAT(20), High FLOAT(20), Low FLOAT(20), Close FLOAT(20), Volume BIGINT(50), primary key(Date));";
			//ps.setString(1,  list[i]);
			//ps.executeQuery();
			statement = connection.createStatement();
			status = statement.executeUpdate(query);
		}
		statement.close();
		ibd.web.Constants.Constants.logger.info("Status in MarketDB Setup.java: "+status);
	}
}