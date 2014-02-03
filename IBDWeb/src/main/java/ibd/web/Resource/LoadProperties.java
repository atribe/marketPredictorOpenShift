package ibd.web.Resource;

import java.io.IOException;
import java.util.Properties;

public final class LoadProperties {
	public static String hostName="",fromEmail="",passKey="",toEmail1="",toEmail2="";
	public static String serverPath="", environment = "", fileName = "";
	// Constructor
	public LoadProperties(){
		// initialize the properties file.
		init();
	}

	// init() function

	private void init(){
		ibd.web.Constants.Constants.logger.info("Loading Properties from the file.");
		Properties file = new Properties();
		//file.setProperty("pathOfFile", ReportGeneration.pathOfFile);
		try {
			//file.store(new FileOutputStream("report.properties"), null);
			file.load(ibd.web.Resource.ResourceInitializer.class.getClassLoader().getResourceAsStream("resources.properties"));	
			//file.load(getClass().getResourceAsStream("report.properties"));
			//properties.put("path", file.getProperty("pathOfFile"));

			//file = null;
			//file = new Properties();
			// Load all the properties at once in the map for later use.
			/*File f = new File("resources.properties");
			if(!f.exists()){
				ibd.web.Constants.Constants.logger.info("Can not find properties File.");
				ibd.web.Constants.Constants.logger.info("Exiting Now.");
			}
			file.load(new FileInputStream("/resources/resources.properties"));*/
			hostName = file.getProperty("host");
			ibd.web.Constants.Constants.logger.info("Loaded Host Name: "+hostName);
			fromEmail = file.getProperty("fromEmail");
			ibd.web.Constants.Constants.logger.info("Loaded from Email: "+fromEmail);
			toEmail1 = file.getProperty("toEmail1");
			ibd.web.Constants.Constants.logger.info("Loaded to Email1: "+toEmail1);
			toEmail2 = file.getProperty("toEmail2");
			ibd.web.Constants.Constants.logger.info("Loaded to Email2: "+toEmail2);
			passKey = file.getProperty("emailPassword");
			ibd.web.Constants.Constants.logger.info("Loaded Password: "+"manisasocialanimal");
			serverPath = file.getProperty("serverPath");
			ibd.web.Constants.Constants.logger.info("Loaded Server Path.");
			environment = file.getProperty("environment");
			ibd.web.Constants.Constants.logger.info("Properties Loaded.");
			file = null;
		} catch (IOException e) {
			ibd.web.Constants.Constants.logger.info(e.toString());
		} 
	}

}

