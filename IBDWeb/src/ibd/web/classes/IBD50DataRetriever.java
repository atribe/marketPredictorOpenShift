package ibd.web.classes;

import ibd.web.beans.Data50;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IBD50DataRetriever {
	
	public List<Data50> getData50(String from, String to){
		List<Data50> ibd50List = new ArrayList<Data50>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			String tableName = getTableName();
			String query = "SELECT * FROM `^"+tableName+"` WHERE dataAsOf >= ? AND dataAsOf < ? ORDER BY rank ASC";
			ibd.web.Constants.Constants.logger.info("In IBD50DataRetriever: "+query);
			connection = MarketDB.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, from);
			preparedStatement.setString(2, to);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				Data50 obj = new Data50();
				obj.setRank(Integer.toString(resultSet.getInt(1)));
				obj.setCompanyName(resultSet.getString(2));
				obj.setSymbol(resultSet.getString(3));
				obj.setSmartSelectCompositeRating(resultSet.getString(4));
				obj.setEpsRating(resultSet.getString(5));
				obj.setRsRating(resultSet.getString(6));
				obj.setIndGroupRelativeStrength(resultSet.getString(7));
				obj.setSmrRating(resultSet.getString(8));
				obj.setAccDis(resultSet.getString(9));
				obj.setWeekHigh52(resultSet.getString(10));
				obj.setClosingPrice(resultSet.getString(11));
				obj.setDollarChange(resultSet.getString(12));
				obj.setVolChange(resultSet.getString(13));
				obj.setVolume(resultSet.getString(14));
				obj.setPe(resultSet.getString(15));
				obj.setSponReading(resultSet.getString(16));
				obj.setDivYield(resultSet.getString(17));
				obj.setOffHigh(resultSet.getString(18));
				obj.setAnnualEpsEstChange(resultSet.getString(19));
				obj.setLastQtrEpsChange(resultSet.getString(20));
				obj.setNextQtrEpsChange(resultSet.getString(21));
				obj.setLastQtrSalesChange(resultSet.getString(22));
				obj.setRoe(resultSet.getString(23));
				obj.setPretaxmargin(resultSet.getString(24));
				obj.setManagementOwns(resultSet.getString(25));
				obj.setQtrEpsCountGreaterThan15(resultSet.getString(26));
				obj.setDescription(resultSet.getString(27));
				obj.setFootNote(resultSet.getString(28));
				obj.setDataAsOf(resultSet.getString(29));
				obj.setIndexAsOf(resultSet.getString(30));
				ibd50List.add(obj);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				connection.close();
				preparedStatement.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return ibd50List;
	}
	
	private String getTableName(){
		Connection c = MarketDB.getConnectionIBD50();
		List<String> tableNames = new ArrayList<String>();
		DatabaseMetaData md = null;
		try {
			md = c.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    ResultSet rs = null;
		try {
			rs = md.getTables(null, null, "%", null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			while (rs.next()) {
			  System.out.println(rs.getString(3));
			  String name = rs.getString(3);
			  name = name.substring(1);
			  tableNames.add(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(rs!=null)
				rs.close();
			if(c!=null)
				c.close();
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-dd-MM");
		Date largestDate = null;
		try{
		largestDate = df.parse(tableNames.get(0));
		}catch(Exception e){
			e.printStackTrace();
		}
		for(int i=0;i<tableNames.size();i++){
			try {
				Date date1 = df.parse(tableNames.get(i));
				if(largestDate.before(date1)){
					largestDate = date1;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		System.out.println(largestDate);
	    
	}
}
