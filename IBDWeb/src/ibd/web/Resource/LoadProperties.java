package ibd.web.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class LoadProperties {
	public static String hostName="",fromEmail="",passKey="",toEmail="";
	// Constructor
	public LoadProperties(){
		// initialize the properties file.
		init();
	}
	
	// init() function
	
	private void init(){
		ibd.web.Resource.ResourceInitializer.logger.info("Loading Properties from the file.");
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
				ibd.web.Resource.ResourceInitializer.logger.info("Can not find properties File.");
				ibd.web.Resource.ResourceInitializer.logger.info("Exiting Now.");
			}
			file.load(new FileInputStream("/resources/resources.properties"));*/
			hostName = file.getProperty("host");
			ibd.web.Resource.ResourceInitializer.logger.info("Loaded Host Name: "+hostName);
			fromEmail = file.getProperty("fromEmail");
			ibd.web.Resource.ResourceInitializer.logger.info("Loaded from Email: "+fromEmail);
			toEmail = file.getProperty("toEmail");
			ibd.web.Resource.ResourceInitializer.logger.info("Loaded to Email: "+toEmail);
			passKey = file.getProperty("emailPassword");
			ibd.web.Resource.ResourceInitializer.logger.info("Loaded Password: "+"manisasocialanimal");
			ibd.web.Resource.ResourceInitializer.logger.info("Properties Loaded.");
			file = null;
		} catch (IOException e) {
			ibd.web.Resource.ResourceInitializer.logger.info(e.toString());
		} 
	}
	
}

