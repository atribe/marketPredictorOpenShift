package ibd.web.IBD50;
import ibd.web.Resource.Communication;
import ibd.web.Resource.LoadProperties;
import ibd.web.classes.MarketDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ParseExcel {
	public static void main(String args) throws IOException {//String fileName = "C://Data50.xls";
		//String[] filesNames = getAllFiles("D:\\Important Work\\Aaron\\marketPredictorOpenShift\\Data50\\");
		Boolean dataFlag = false;  
		String symbol = "";
		  /* @author Shakeel Shahzad
		   * @description New table in database has been created for storing IBD50's data.
		   *  
		   * String createTable = "CREATE TABLE `^data50` (rank VARCHAR(10), companyName VARCHAR(100), symbol VARCHAR(10), smartSelectCompositeRating VARCHAR(20), epsRating VARCHAR(20), rsRating VARCHAR(20), indGroupRelativeStrength VARCHAR(10), smrRating VARCHAR(10), accDis VARCHAR(10), weekHigh52 VARCHAR(10), closingPrice VARCHAR(10), dollarChange VARCHAR(10), volChange VARCHAR(10), volume VARCHAR(50), pe VARCHAR(10), sponRating VARCHAR(10), divYield VARCHAR(10), offHigh VARCHAR(10), annualEpsEstChange VARCHAR(10), lastQtrEpsChange VARCHAR(10),nextQtrEpsChange VARCHAR(10), lastQtrSalesChange VARCHAR(10), roe VARCHAR(10), pretaxMargin VARCHAR(10), managementOwns VARCHAR(10), qtrEpsCountGreaterThan15 VARCHAR(10), description VARCHAR(1000), footNote VARCHAR(10), dataAsOf VARCHAR(100), indexAsOf VARCHAR(100), PRIMARY KEY (rank, dataAsOf, indexAsOf));";
		   * ALTER TABLE `^data50` MODIFY rank INTEGER;
		  		
		  
		  System.out.println(createTable);*/
		String[] array = null;
			int x = 0;
			//String date = new Date().toString();
			//array = date.split(" ");
			String tableName = "temporary";
			String createTable = "";
		  
			//while(x<filesNames.length){
				createTable = "CREATE TABLE `^"+tableName+"` (rank INTEGER, companyName VARCHAR(100), symbol VARCHAR(10), smartSelectCompositeRating VARCHAR(20), epsRating VARCHAR(20), rsRating VARCHAR(20), indGroupRelativeStrength VARCHAR(10), smrRating VARCHAR(10), accDis VARCHAR(10), weekHigh52 VARCHAR(10), closingPrice VARCHAR(10), dollarChange VARCHAR(10), volChange VARCHAR(10), volume VARCHAR(50), pe VARCHAR(10), sponRating VARCHAR(10), divYield VARCHAR(10), offHigh VARCHAR(10), annualEpsEstChange VARCHAR(10), lastQtrEpsChange VARCHAR(10),nextQtrEpsChange VARCHAR(10), lastQtrSalesChange VARCHAR(10), roe VARCHAR(10), pretaxMargin VARCHAR(10), managementOwns VARCHAR(10), qtrEpsCountGreaterThan15 VARCHAR(10), description VARCHAR(1000), footNote VARCHAR(10), dataAsOf VARCHAR(100), indexAsOf VARCHAR(100), exchangeToTrade VARCHAR(100), indexMembership VARCHAR(100), marketCap VARCHAR(100), earningAnnouncement VARCHAR(50), PRIMARY KEY (rank, dataAsOf, indexAsOf))";
				//System.out.println("File: "+(x+1)+" out of "+filesNames.length);
				
		  BufferedReader br = new BufferedReader(new FileReader(args));
		  String line;
		  //int queryCounter = 0;
		  Boolean flag = false;
		  int counter = 0;
		  List<String> queries = new ArrayList<String>();
		  String queryy = "";
		  String dataOf = "";
		  String indexOf = "";
		  //String[] queries = new String[50];
		  ibd.web.Constants.Constants.teedixIbd50PricesVolumes = null;
		  ibd.web.Constants.Constants.teedixIbd50PricesVolumes = new ArrayList<String>();
		  while ((line = br.readLine()) != null) {
		     // process the line.
			  String valuesss = line.trim();
			  valuesss = valuesss.replaceAll("'", "");
			  if(valuesss.contains("Data as of")){
				  int index = valuesss.indexOf("Data as of");
				  String value = valuesss.substring(index);
				  dataOf = value.substring(10,21);
				  index = value.indexOf("Index as of");
				  indexOf = value.substring(index+12);
				  dataOf = dataOf.trim();
				  indexOf = indexOf.trim();
				  dataFlag = true;
				  if(dataOf.contains(";")){
					  dataOf = dataOf.substring(0,dataOf.length()-1);
				  }
				  if(dataFlag){
					  array = dataOf.split("/");
					  if(array[0].trim().length()==1){
						  array[0]="0"+array[0];
					  }
					  if(array[1].trim().length()==1){
						  array[1]="0"+array[1];
					  }
					  tableName = array[2]+"-"+array[0]+"-"+array[1];
					  System.out.println(tableName);//System.exit(-1);
					  dataFlag = false;
				  }
				  if(indexOf.contains(";")){
						indexOf = indexOf.substring(0,indexOf.length()-1);
				  }
			  }
			  if(valuesss.equals("<td style=\"width:48px;\">")){
				  flag = true;
			  }
			  if(flag){
				  if(!valuesss.contains("<")){
					  if(counter == 0){
						  queryy = "INSERT INTO `^"+tableName.toLowerCase()+"` VALUES ("; 
					  }
					  if(counter < 28){
						  if(counter == 27){								
							  queryy += valuesss+"','"+dataOf+"','"+indexOf+"');";
							  String exchange = new ParseURL().exchangeToTrade(symbol);
							  boolean flags = false;
							  if(null == exchange || exchange.trim().equalsIgnoreCase("") || exchange.trim().equalsIgnoreCase(" ") || "null".equalsIgnoreCase(exchange)){
								  exchange = ",'-'";
								  flags = true;
							  }
							  if(!flags)
								  queryy += ",'"+exchange+"'";
							  else
								  queryy += exchange;
							  
							  flags = false;
							  List<String> indexMembership = new ParseURL().indexMembership(symbol);
							  if(null == indexMembership || indexMembership.size()<=0){
								  queryy += ",'-'";
							  }else{
								  int count = 0;
								  for(String value : indexMembership){
									  queryy += ",";
									  if(0 == count)
										  queryy += "'";
									  queryy += value;
									  count++;
								  }
								  queryy += "'";
							  }
							  String market = ""+new ParseURL().marketCap(symbol);
							  if(null == market || market.trim().equalsIgnoreCase("") || market.trim().equalsIgnoreCase(" ") || "null".equalsIgnoreCase(market)){
								  market = ",'-'";
								  flags = true;
							  }
							  if(!flags)
								  queryy += ",'"+market+"'";
							  else
								  queryy += market;
							  List<String> earning = new ParseURL().earningAnnouncement(symbol);
							  if(null == earning || earning.size()<=0){
								  queryy += ",'-'";
							  }else{
								  int count = 0;
								  if("No Upcoming Events".equalsIgnoreCase(earning.get(0))){
									  queryy += ",'-'";
								  }else{
									  for(String value : earning){
										  queryy += ",";
										  if(0 == count)
											  queryy += "'";
										  queryy += value;
										  count++;
									  }
								  queryy += "'";
								  }
							  }
							  queryy += ");";
							  //System.out.println(queryy);
							  queries.add(queryy);
						  }else{
							  if(counter == 0){
								  queryy += Integer.parseInt(valuesss)+",'"; 
							  }else{
								  if(2 == counter){
									  symbol = valuesss;
									  ibd.web.Constants.Constants.teedixIbd50PricesVolumes.add(valuesss);
								  }
								  queryy += valuesss+"','";
							  }
							  //System.out.println("Counter: "+line);
						  }
						  counter++;
					  }else{
						  counter = 0;
						  //queryCounter++;
						  queryy = "INSERT INTO `^"+tableName.toLowerCase()+"` VALUES (";
						  queryy += Integer.parseInt(valuesss)+",'";
						  counter++;
						  //System.out.println("Nex Counter"+line);
					  }
				  }
			  }
		  }
		  br.close();
		  x++;
		  // Database Enteries.
		  Connection con = null;
		  Statement stmt = null;
		  int check = 0;
		  try{
			  con = MarketDB.getConnectionIBD50();
			  stmt = con.createStatement();
			  //System.out.println(createTable);
			  createTable = createTable.replaceAll("temporary", tableName.toLowerCase());
			  ibd.web.Constants.Constants.logger.info("Inside ParseExcel: Create Table Query: "+createTable);
			  stmt.executeUpdate(createTable);
			  ibd.web.Constants.Constants.logger.info("Inside ParseExcel: Following Batch of Queries are Executing");
			  for(int i=0;i<queries.size();i++){
				  check = i;
				  String query = queries.get(i);
				  query = query.replaceAll("temporary",tableName.toLowerCase());
				  ibd.web.Constants.Constants.logger.info(query);
				  stmt.executeUpdate(query);
			  }
			  ibd.web.Constants.Constants.logger.info("Inside ParseExcel: Batch has been Ended");
			  tableName = "temporary";
			  stmt.close();
			  con.close();
		  }catch(Exception e){
			  System.out.println("EXCEPTION: "+queries.get(check));
			  ibd.web.Constants.Constants.logger.info("Inside ParseExcel: EXCEPTION: "+queries.get(check));
			  //System.exit(-1);
			  //e.printStackTrace();
		  }finally{
			  if(stmt!=null){
				  try {
					stmt.close();
				} catch (SQLException e) {
					//e.printStackTrace();
				}
			  }
			  if(con!=null){
				  try {
					con.close();
				} catch (SQLException e) {
					//e.printStackTrace();
				}
			  }
		  }

			//}
		  if(LoadProperties.environment.trim().equalsIgnoreCase("production")){
		  
			  try{
			    	//ibd.web.Constants.Constants.logger.info(LoadProperties.hostName+" "+LoadProperties.fromEmail+" "+LoadProperties.passKey+" "+LoadProperties.toEmail1+" "+LoadProperties.toEmail2+" "+LoadProperties.serverPath+"IBDinfo.log");
				  	ibd.web.Constants.Constants.isParseExcel = true;
				  	Communication obj = new Communication();
			    	ibd.web.Constants.Constants.logger.info("INSIDE ParseExcel.java: Execute Downloading of File and Parsing/saving in database");
			    	obj.communicate(LoadProperties.hostName, LoadProperties.fromEmail, LoadProperties.passKey, LoadProperties.toEmail1, LoadProperties.toEmail2 , LoadProperties.serverPath+"IBDinfo.log");
			    	ibd.web.Constants.Constants.isParseExcel = false;
			  }catch(Exception e){
			    	ibd.web.Constants.Constants.logger.info("EXCEPTION IN SENDING EMAIL");
			    }
		  }
		  
		  /**
		   * @author Shakeel Shahzad
		   * @description Now create Tables for teedixibd50pricesvolumes Database, but before creation, 
		   * drop all the already existing Tables.
		   */
		  
		  dropTeedixIBD50PricesVolumesTables();
		  createTeedixIBD50PricesVolumesTables();
		  
		 }
	
	/**
	 * @author Shakeel Shahzad
	 * @description This function deletes all of the existing tables in teedixibd50pricesvolumes Database.
	 */
	public static void dropTeedixIBD50PricesVolumesTables(){
		ibd.web.Constants.Constants.logger.info("Inside ParseExcel: Starting to drop all the Tables.");
		  List<String> tableNames = getAllTableNames();
		  Connection con = null;
		  Statement stmt = null;
		  try{
			  con = MarketDB.getConnectionIBD50PricesVolumes();
			  stmt = con.createStatement();
			  String query = "";
			  int counter = 0;
			  while(counter < tableNames.size()){
				  query = "DROP TABLE `^"+(tableNames.get(counter)).toLowerCase()+"`";
				  stmt.executeUpdate(query);
				  counter++;
			  }
		  }catch(Exception e){
			  //e.printStackTrace();
			  ibd.web.Constants.Constants.logger.info("Exception Inside ParseExcel:"+e.toString());
		  }finally{
			  try{
				  stmt.close();
				  con.close();
			  }catch(Exception e){
				  //e.printStackTrace();
			  }
		  }
	}
	
	/**
	 * @author Shakeel Shahzad
	 * @description This function returns List of all the existing tables in teedixibd50pricesvolumes Database.
	 * @return List<String> of all the tables Names in teedixibd50pricesvolumes Database.
	 */
	private static List<String> getAllTableNames(){
		List<String> tableNames = new ArrayList<String>();
		Connection con = MarketDB.getConnectionIBD50PricesVolumes();
		DatabaseMetaData md = null;
		try {
			md = con.getMetaData();
		} catch (SQLException e) {
			//e.printStackTrace();
		}
	    ResultSet rs = null;
		try {
			rs = md.getTables(null, null, "%", null);
		} catch (SQLException e) {
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
			//e.printStackTrace();
		}finally{
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					//e.printStackTrace();
				}
			if(con!=null)
				try {
					con.close();
				} catch (SQLException e) {
					//e.printStackTrace();
				}
		}
		return tableNames;
	}
	
	/**
	 * @author Shakeel Shahzad
	 * @description This function creates the tables against each stock in teedixibd50pricesvolumes Database.
	 */
	public static void createTeedixIBD50PricesVolumesTables(){
		  Connection con = null;
		  Statement stmt = null;
		  String query = "";
		  try{
			  ibd.web.Constants.Constants.logger.info("Inside ParseExcel: Creating Tables");
			  con = MarketDB.getConnectionIBD50PricesVolumes();
			  stmt = con.createStatement();
			  int counter = 0;
			  while(counter < ibd.web.Constants.Constants.teedixIbd50PricesVolumes.size()){
				  query = "CREATE TABLE `^"+(ibd.web.Constants.Constants.teedixIbd50PricesVolumes.get(counter)).toLowerCase()+"` (Date VARCHAR(100), Open float, High float, Low float, Close float, Volume BIGINT, PRIMARY KEY (Date))";
				  stmt.executeUpdate(query);
				  counter++;
			  }
		  }catch(Exception e){
			  //e.printStackTrace();
			  ibd.web.Constants.Constants.logger.info("Exception Inside ParseExcel: Query "+query);
		  }finally{
			  try{
				  stmt.close();
				  con.close();
			  }catch(Exception e){
				  //e.printStackTrace();
			  }
		  }
	}
	
	
	public static String[] getAllFiles(String path){
		// Directory path here
		 
		  String [] files;
		  File folder = new File(path);
		  File[] listOfFiles = folder.listFiles(); 
		  files = new String[listOfFiles.length];
		  for (int i = 0; i < listOfFiles.length; i++) 
		  {
		 
		   if (listOfFiles[i].isFile()) 
		   {
		   files[i] = listOfFiles[i].getName();
		   //System.out.println(files[i]);
		      }
		  }
		  
		  return files;
	}
	
	private static Integer returnMonth(String month){
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
