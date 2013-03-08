package ibd.web.classes;

/**
 * Encapsulates the price and volume arrays for each stock into one object
 * @author Shakeel Shahzad
 * @description This class is actually responsible for communicating with database
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import ibd.web.classes.MarketDB;

public class HelloWorld {

	/**
	 * 
	 * @param args These arguments are not being used for now but will be used if necessary
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
	int status = 0;
	String[] list = {"`^SML`", "`^MID`", "`^GSPC`", "`^DJI`", "`^IXIC`"};

	for (int i = 0; i < list.length; i++) {
	    query = "create table" + list[i] + " (Date DATE, Open FLOAT(20), High FLOAT(20), Low FLOAT(20), Close FLOAT(20), Volume BIGINT(50), primary key(Date));";
	    statement = connection.createStatement();
	    status = statement.executeUpdate(query);
	}
	statement.close();
	System.out.println(status);
	ibd.web.Resource.ResourceInitializer.logger.info("Status in HelloWorld.java: "+status);
    }
}