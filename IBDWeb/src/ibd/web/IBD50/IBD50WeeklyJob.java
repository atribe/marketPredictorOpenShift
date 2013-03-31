package ibd.web.IBD50;

import ibd.web.Resource.LoadProperties;

import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;

public class IBD50WeeklyJob extends TimerTask{

	@Override
	public void run() {
		ibd.web.Constants.Constants.logger.info("Inside IBD50WeeklyJob: Starting downloading file from Server on: "+new Date().toString());
		DownloadAuthenticatedFile.main(LoadProperties.serverPath);
		
		try {
			ibd.web.Constants.Constants.logger.info("Inside IBD50WeeklyJob: Parsing the downloaded Excel File named as: "+LoadProperties.serverPath+ibd.web.Constants.Constants.fileName);
			ParseExcel.main(LoadProperties.serverPath+ibd.web.Constants.Constants.fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
