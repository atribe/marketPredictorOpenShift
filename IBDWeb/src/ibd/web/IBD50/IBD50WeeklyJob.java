package ibd.web.IBD50;

import ibd.web.Resource.LoadProperties;

import java.io.IOException;
import java.util.TimerTask;

public class IBD50WeeklyJob extends TimerTask{

	@Override
	public void run() {
		DownloadAuthenticatedFile.main(LoadProperties.serverPath);
		try {
			ParseExcel.main(LoadProperties.serverPath+ibd.web.Constants.Constants.fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
