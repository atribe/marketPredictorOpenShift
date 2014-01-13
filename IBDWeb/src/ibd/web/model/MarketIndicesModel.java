package ibd.web.model;

import ibd.web.DBManagers.GenericDBSuperclass;
import ibd.web.DBManagers.MarketIndexDB;
import ibd.web.DBManagers.MarketIndexParametersDB;
import ibd.web.analyzer.IndexAnalyzer;

import java.sql.Connection;

/**
 * This class is the highest level class that deals with all things market index.
 * Initializes and updates databases
 * Runs the model to determine buy and sell dates
 * @author Allan
 *
 */
public class MarketIndicesModel {

	//							  		  Nasdaq  S&P500
	private static String[] indexList = {"^IXIC","^GSPC","^SML","^MID"};
	private static String[] indexParametersDBNameList;

	public static void main() {

		//Get a database connection
		Connection connection = GenericDBSuperclass.getConnection();

		/*
		 * Dabase initialization Section
		 */
		//Initialize the price/volume databases for each index 
		MarketIndexDB.priceVolumeDBInitialization(connection, indexList);

		/*
		 * setting the indexParameterList to be equal to the indexList+"_var"
		 * This means adding future indices only requires adding them to one list
		 * as the indexParameterList is autogenerated from indexList
		 */
		setIndexParameterList();

		//Initialize the model variables
		MarketIndexParametersDB.indexModelParametersInitialization(connection, indexParametersDBNameList);		


		//Market Index Models
		/*
		 * Models runs are not looped because you may want to run or optimize them one at a time
		 * I'll figure out this code after I figure out the above
		 */
		IndexAnalyzer.runIndexAnalysis(connection, "^IXIC", "^IXICvars");
		//Run model for Nasdaq
		//Run model for SP500
	}

	/**
	 * This method allows for the dynamic creation of the indexParameterList
	 * The indexParameterList is simply the indexList with 'var' appended to the end
	 */
	private static void setIndexParameterList() {
		indexParametersDBNameList = new String[indexList.length];

		for(int i = 0;i<indexList.length;i++) {
			indexParametersDBNameList[i]=indexList[i] +"vars";
		}
	}

	private static String[] getIndexList() {
		return indexList;
	}

	private static String[] getIndexParameterList() {
		return indexParametersDBNameList;
	}
}
