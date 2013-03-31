package ibd.web.IBD50;
import ibd.web.Resource.Communication;
import ibd.web.Resource.LoadProperties;
import ibd.web.classes.MarketDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ParseExcel {
	public static void main(String args) throws IOException {//String fileName = "C://Data50.xls";
		//String[] filesNames = getAllFiles("D:\\Important Work\\Aaron\\marketPredictorOpenShift\\Data50\\");
		Boolean dataFlag = false;  
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
				createTable = "CREATE TABLE `^"+tableName+"` (rank INTEGER, companyName VARCHAR(100), symbol VARCHAR(10), smartSelectCompositeRating VARCHAR(20), epsRating VARCHAR(20), rsRating VARCHAR(20), indGroupRelativeStrength VARCHAR(10), smrRating VARCHAR(10), accDis VARCHAR(10), weekHigh52 VARCHAR(10), closingPrice VARCHAR(10), dollarChange VARCHAR(10), volChange VARCHAR(10), volume VARCHAR(50), pe VARCHAR(10), sponRating VARCHAR(10), divYield VARCHAR(10), offHigh VARCHAR(10), annualEpsEstChange VARCHAR(10), lastQtrEpsChange VARCHAR(10),nextQtrEpsChange VARCHAR(10), lastQtrSalesChange VARCHAR(10), roe VARCHAR(10), pretaxMargin VARCHAR(10), managementOwns VARCHAR(10), qtrEpsCountGreaterThan15 VARCHAR(10), description VARCHAR(1000), footNote VARCHAR(10), dataAsOf VARCHAR(100), indexAsOf VARCHAR(100), PRIMARY KEY (rank, dataAsOf, indexAsOf))";
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
					  tableName = array[2]+"-"+array[1]+"-"+array[0];
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
						  queryy = "INSERT INTO `^"+tableName+"` VALUES ("; 
					  }
					  if(counter < 28){
						  if(counter == 27){
								
							  queryy += valuesss+"','"+dataOf+"','"+indexOf+"');";
							  queries.add(queryy);
						  }else{
							  if(counter == 0){
								  queryy += Integer.parseInt(valuesss)+",'"; 
							  }else{
								  queryy += valuesss+"','";
							  }
							  //System.out.println("Counter: "+line);
						  }
						  counter++;
					  }else{
						  counter = 0;
						  //queryCounter++;
						  queryy = "INSERT INTO `^"+tableName+"` VALUES (";
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
			  System.out.println(createTable);
			  createTable = createTable.replaceAll("temporary", tableName);
			  ibd.web.Constants.Constants.logger.info("Inside ParseExcel: Create Table Query: "+createTable);
			  stmt.executeUpdate(createTable);
			  ibd.web.Constants.Constants.logger.info("Inside ParseExcel: Following Batch of Queries are Executing");
			  for(int i=0;i<queries.size();i++){
				  check = i;
				  String query = queries.get(i);
				  query = query.replaceAll("temporary",tableName);
				  ibd.web.Constants.Constants.logger.info(query);
				  stmt.executeUpdate(query);
			  }
			  ibd.web.Constants.Constants.logger.info("Inside ParseExcel: Batch has been Ended");
			  tableName = "temporary";
			  stmt.close();
			  con.close();
		  }catch(Exception e){
			  System.out.println(queries.get(check));
			  ibd.web.Constants.Constants.logger.info("Inside ParseExcel: EXCEPTION: "+queries.get(check));
			  //System.exit(-1);
			  //e.printStackTrace();
		  }finally{
			  if(stmt!=null){
				  try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
			  if(con!=null){
				  try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
		  }

			//}
		  
		  try{
		    	//ibd.web.Constants.Constants.logger.info(LoadProperties.hostName+" "+LoadProperties.fromEmail+" "+LoadProperties.passKey+" "+LoadProperties.toEmail1+" "+LoadProperties.toEmail2+" "+LoadProperties.serverPath+"IBDinfo.log");
		    	Communication obj = new Communication();
		    	ibd.web.Constants.Constants.logger.info("INSIDE ParseExcel.java: Execute Downloading of File and Parsing/saving in database");
		    	obj.communicate(LoadProperties.hostName, LoadProperties.fromEmail, LoadProperties.passKey, LoadProperties.toEmail1, LoadProperties.toEmail2 , LoadProperties.serverPath+"IBDinfo.log");
		    }catch(Exception e){
		    	ibd.web.Constants.Constants.logger.info("EXCEPTION IN SENDING EMAIL");
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
