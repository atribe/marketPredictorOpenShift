package ibd.web.IBD50;
import ibd.web.classes.MarketDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ParseExcel {
	public static void main(String args) throws IOException {

		  //String fileName = "C://Data50.xls";
		//String[] filesNames = getAllFiles("D:\\Important Work\\Aaron\\marketPredictorOpenShift\\Data50\\");
		  
		  /* @author Shakeel Shahzad
		   * @description New table in database has been created for storing IBD50's data.
		   *  
		   * String createTable = "CREATE TABLE `^data50` (rank VARCHAR(10), companyName VARCHAR(100), symbol VARCHAR(10), smartSelectCompositeRating VARCHAR(20), epsRating VARCHAR(20), rsRating VARCHAR(20), indGroupRelativeStrength VARCHAR(10), smrRating VARCHAR(10), accDis VARCHAR(10), weekHigh52 VARCHAR(10), closingPrice VARCHAR(10), dollarChange VARCHAR(10), volChange VARCHAR(10), volume VARCHAR(50), pe VARCHAR(10), sponRating VARCHAR(10), divYield VARCHAR(10), offHigh VARCHAR(10), annualEpsEstChange VARCHAR(10), lastQtrEpsChange VARCHAR(10),nextQtrEpsChange VARCHAR(10), lastQtrSalesChange VARCHAR(10), roe VARCHAR(10), pretaxMargin VARCHAR(10), managementOwns VARCHAR(10), qtrEpsCountGreaterThan15 VARCHAR(10), description VARCHAR(1000), footNote VARCHAR(10), dataAsOf VARCHAR(100), indexAsOf VARCHAR(100), PRIMARY KEY (rank, dataAsOf, indexAsOf));";
		   * ALTER TABLE `^data50` MODIFY rank INTEGER;
		  		
		  
		  System.out.println(createTable);*/
		String[] array = null;
			//int x = 0;
			//while(x<filesNames.length){
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
				  if(dataOf.contains(";")){
					  dataOf = dataOf.substring(0,dataOf.length()-1);
					  array = dataOf.split("/");
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
						  queryy = "INSERT INTO `^data50` VALUES ("; 
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
						  queryy = "INSERT INTO `^data50` VALUES (";
						  queryy += Integer.parseInt(valuesss)+",'";
						  counter++;
						  //System.out.println("Nex Counter"+line);
					  }
				  }
			  }
		  }
		  br.close();

			String createTable = "CREATE TABLE `^"+array[2]+"-"+array[1]+"-"+array[0]+"` (rank INTEGER, companyName VARCHAR(100), symbol VARCHAR(10), smartSelectCompositeRating VARCHAR(20), epsRating VARCHAR(20), rsRating VARCHAR(20), indGroupRelativeStrength VARCHAR(10), smrRating VARCHAR(10), accDis VARCHAR(10), weekHigh52 VARCHAR(10), closingPrice VARCHAR(10), dollarChange VARCHAR(10), volChange VARCHAR(10), volume VARCHAR(50), pe VARCHAR(10), sponRating VARCHAR(10), divYield VARCHAR(10), offHigh VARCHAR(10), annualEpsEstChange VARCHAR(10), lastQtrEpsChange VARCHAR(10),nextQtrEpsChange VARCHAR(10), lastQtrSalesChange VARCHAR(10), roe VARCHAR(10), pretaxMargin VARCHAR(10), managementOwns VARCHAR(10), qtrEpsCountGreaterThan15 VARCHAR(10), description VARCHAR(1000), footNote VARCHAR(10), dataAsOf VARCHAR(100), indexAsOf VARCHAR(100), PRIMARY KEY (rank, dataAsOf, indexAsOf))";
		  //x++;
		  // Database Enteries.
		  Connection con = null;
		  Statement stmt = null;
		  int check = 0;
		  try{
			  con = MarketDB.getConnectionIBD50();
			  stmt = con.createStatement();
			  stmt.executeUpdate(createTable);
			  for(int i=0;i<queries.size();i++){
				  check = i;
				  stmt.executeUpdate(queries.get(i));
			  }
			  stmt.close();
			  con.close();
		  }catch(Exception e){
			  System.out.println(queries.get(check));
			  System.exit(-1);
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
}
