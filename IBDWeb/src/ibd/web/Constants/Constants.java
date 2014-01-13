package ibd.web.Constants;

import ibd.web.classes.Output;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class Constants {
	public static Output outputSP500;
	public static Output outputNasdaq;
	public static Output outputDow;
	public static Boolean jobRunning = false;
	public static Date jobSchedule = null;
	public static Logger logger = Logger.getLogger(ibd.web.Constants.Constants.class);
	public static String fileName = "";
	public static Boolean isParseExcel = false;
	/**
	 * @author Shakeel Shahzad
	 * @description Will save weekly stocks symbols and create the tables in teedixibd50pricesvolumes database.
	 */
	public static List<String> teedixIbd50PricesVolumes = null;
}
