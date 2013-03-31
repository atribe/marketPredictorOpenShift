package ibd.web.Constants;

import ibd.web.classes.Output;

import java.util.Date;

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
}
