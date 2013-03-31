package ibd.web.Resource;

import ibd.web.IBD50.IBD50WeeklyJob;
import ibd.web.threads.ApplicationThread;

import java.util.Calendar;
import java.util.Timer;

public class ResourceInitializer {
	/**
	 * 
	 */
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void initMethod(){
			new LoadProperties();
			ibd.web.Constants.Constants.logger.info("Inside Resource Initializer: Putting Weekly Timer for IBD50");
			Timer timer  = new Timer();
		    Calendar date = Calendar.getInstance();
		    date.set(
		      Calendar.DAY_OF_WEEK,
		      Calendar.SUNDAY
		    );
		    date.set(Calendar.HOUR, 6);
		    date.set(Calendar.MINUTE, 0);
		    date.set(Calendar.SECOND, 0);
		    date.set(Calendar.MILLISECOND, 0);
		    timer.schedule(
		      new IBD50WeeklyJob(),
		      date.getTime(),
		      1000 * 60 * 60 * 24 * 7
		    );
			ApplicationThread.startThread();
	}
}
