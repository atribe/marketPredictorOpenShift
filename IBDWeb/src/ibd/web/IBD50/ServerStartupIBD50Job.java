package ibd.web.IBD50;

import ibd.web.classes.IBD50DataRetriever;

public class ServerStartupIBD50Job {
	public static void serverStartupIBD50Job(){
		ParseExcel.dropTeedixIBD50PricesVolumesTables();
		IBD50DataRetriever object = new IBD50DataRetriever();
		String tableName = object.getTableName();
		ibd.web.Constants.Constants.teedixIbd50PricesVolumes = null;
		ibd.web.Constants.Constants.teedixIbd50PricesVolumes = object.getDataFrom(tableName);
		ParseExcel.createTeedixIBD50PricesVolumesTables();
	}
}
