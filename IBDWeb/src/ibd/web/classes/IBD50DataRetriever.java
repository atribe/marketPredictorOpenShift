package ibd.web.classes;

import ibd.web.beans.Data50;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IBD50DataRetriever {
	
	public List<Data50> getData50(){
		List<Data50> ibd50List = new ArrayList<Data50>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			String tableName = getTableName();
			String query = "SELECT * FROM `^"+tableName.toLowerCase()+"` ORDER BY rank ASC";
			ibd.web.Constants.Constants.logger.info("In IBD50DataRetriever: "+query);
			connection = MarketDB.getConnectionIBD50();
			preparedStatement = connection.prepareStatement(query);
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
				obj.setExchangeToTrade(resultSet.getString(31));
				obj.setIndexMembership(resultSet.getString(32));
				obj.setMarketCap(resultSet.getString(33));
				obj.setEarningAnnouncement(resultSet.getString(34));
				ibd50List.add(obj);
			}
		}catch(Exception e){
			//e.printStackTrace();
		}finally{
			try{
				connection.close();
				preparedStatement.close();
			}catch(Exception ex){
				//ex.printStackTrace();
			}
		}
		return ibd50List;
	}
	
	public List<Data50> getData50(String selectedDate){
		List<Data50> ibd50List = new ArrayList<Data50>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{
			String tableName = selectedDate;
			String query = "SELECT * FROM `^"+tableName.toLowerCase()+"` ORDER BY rank ASC";
			ibd.web.Constants.Constants.logger.info("In IBD50DataRetriever: "+query);
			connection = MarketDB.getConnectionIBD50();
			preparedStatement = connection.prepareStatement(query);
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
				obj.setExchangeToTrade(resultSet.getString(31));
				obj.setIndexMembership(resultSet.getString(32));
				obj.setMarketCap(resultSet.getString(33));
				obj.setEarningAnnouncement(resultSet.getString(34));
				ibd50List.add(obj);
			}
		}catch(Exception e){
			//e.printStackTrace();
		}finally{
			try{
				connection.close();
				preparedStatement.close();
			}catch(Exception ex){
				//ex.printStackTrace();
			}
		}
		return ibd50List;
	}
	
	
	public List<String> getAllTables(){
		ibd.web.Constants.Constants.logger.info("Inside IBD50DataRetriever: Getting All Tables Names");
		Connection c = MarketDB.getConnectionIBD50();
		List<String> tableNames = new ArrayList<String>();
		DatabaseMetaData md = null;
		try {
			md = c.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	    ResultSet rs = null;
		try {
			rs = md.getTables(null, null, "%", null);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	    try {
			while (rs.next()) {
			  //System.out.println(rs.getString(3));
			  String name = rs.getString(3);
			  name = name.substring(1);
			  tableNames.add(name);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}finally{
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			if(c!=null)
				try {
					c.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
		}
	    return tableNames;
	}
	
	public String getTableName(){
		ibd.web.Constants.Constants.logger.info("Inside IBD50DataRetriever: Getting the Newest Table Name");
		List<String> tableNames = getAllTables();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date largestDate = null;
		try{
		largestDate = df.parse(tableNames.get(0));
		}catch(Exception e){
			//e.printStackTrace();
		}
		for(int i=0;i<tableNames.size();i++){
			try {
				Date date1 = df.parse(tableNames.get(i));
				if(largestDate.before(date1)){
					largestDate = date1;
				}
			} catch (ParseException e) {
				//e.printStackTrace();
			}
		}
		String [] arr = (largestDate.toString()).split(" ");
		String month = Integer.toString(returnMonth(arr[1]));
		if(month.trim().length()==1){
			month = "0"+month;
		}
		if(arr[2].trim().length()==1){
			arr[2] = "0"+arr[2];
		}
		ibd.web.Constants.Constants.logger.info("Inside IBD50Retriever: Newest Table is: "+arr[5]+"-"+month+"-"+arr[2]);
	    return (arr[5]+"-"+month+"-"+arr[2]);
	}
	
	
	public Integer getLargestDate(List<String> allDates){
		ibd.web.Constants.Constants.logger.info("Inside IBD50DataRetriever: Getting the LargestDate");
		List<String> tableNames = allDates;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		int index = 0;
		Date largestDate = null;
		try{
		largestDate = df.parse(tableNames.get(0));
		}catch(Exception e){
			//e.printStackTrace();
		}
		for(int i=0;i<tableNames.size();i++){
			try {
				Date date1 = df.parse(tableNames.get(i));
				if(largestDate.before(date1)){
					largestDate = date1;
					index = i;
				}
			} catch (ParseException e) {
				//e.printStackTrace();
			}
		}
	    return (index);
	}
	

	
	public List<String> getDataFrom(String tableName){
		ibd.web.Constants.Constants.logger.info("Inside IBD50Retriever: Getting Data from: "+tableName);
		List<String> data = new ArrayList<String>();
		Connection con = null;
		  Statement stmt = null;
		  ResultSet rs = null;
		  try{
			  con = MarketDB.getConnectionIBD50();
			  stmt = con.createStatement();
			  String query = "SELECT symbol FROM `^"+tableName.toLowerCase()+"`";
			  rs = stmt.executeQuery(query);
			  while(rs.next()){
				  data.add(rs.getString(1));
			  }
		  }catch(Exception e){
			  //e.printStackTrace();
			  ibd.web.Constants.Constants.logger.info("Exception Inside IBD50Retriever: "+e.toString());
		  }finally{
			  try{
				  stmt.close();
				  con.close();
				  rs.close();
			  }catch(Exception e){
				  //e.printStackTrace();
			  }
		  }
		return data;
	}
	
	
	private Integer returnMonth(String month){
		if(month.contains("Jan")){
			return 1;
		}else if(month.contains("Feb")){
			return 2;
		}else if(month.contains("Mar")){
			return 3;
		}else if(month.contains("Apr")){
			return 4;
		}else if(month.contains("May")){
			return 5;
		}else if(month.contains("Jun")){
			return 6;
		}else if(month.contains("Jul")){
			return 7;
		}else if(month.contains("Aug")){
			return 8;
		}else if(month.contains("Sep")){
			return 9;
		}else if(month.contains("Oct")){
			return 10;
		}else if(month.contains("Nov")){
			return 11;
		}else{
			return 12;
		}
	}
}
