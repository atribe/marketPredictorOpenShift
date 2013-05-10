package ibd.web.Resource;

import ibd.web.IBD50.IBD50WeeklyJob;
import ibd.web.IBD50.ServerStartupIBD50Job;
import ibd.web.threads.ApplicationThread;

import java.util.Calendar;
import java.util.Timer;

public class ResourceInitializer {
	/**
	 * 
	 */
	private String message;
	private static boolean _isThisRunning = false;
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void initMethod(){
		if(!_isThisRunning){
			_isThisRunning = true;
			new LoadProperties();
			ibd.web.Constants.Constants.logger.info("Inside Resource Initializer: Putting Weekly Timer for IBD50");
			/*Timer timer  = new Timer();
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
		    );*/
		}
		    /**
		     * @author Shakeel Shahzad
		     * @description This is important to execute this on server start-up so when our application
		     * is deployed, it could first delete all the tables and create new ones so that Daily Job couldn't be interrupted
		     * because of no databases in Database (As weekly job drops and creates new Tables in Database)
		     */
		    /*ibd.web.Constants.Constants.logger.info("Inside Resource Initializer: Going to start ServerStartupIBD50Job");
		    ServerStartupIBD50Job.serverStartupIBD50Job();
		    ibd.web.Constants.Constants.logger.info("Inside Resource Initializer: Ended ServerStartupIBD50Job");*/
		if(!ApplicationThread._isRunning){
		    	ApplicationThread.startThread();
		}
	}
}
