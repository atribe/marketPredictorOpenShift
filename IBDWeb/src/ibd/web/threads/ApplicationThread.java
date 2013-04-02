package ibd.web.threads;

import ibd.web.IBD50.IBD50DailyJob;
import ibd.web.IBD50.IBD50WeeklyJob;
import ibd.web.Resource.LoadProperties;
import ibd.web.Resource.Communication;
import ibd.web.classes.VarDow;
import ibd.web.classes.VarNasdaq;
import ibd.web.classes.VarSP500;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationThread implements Runnable {

    public static boolean _continueRunning = true;
    private static boolean _running = false;
    private static boolean _debug = true;
    public static Thread _thread = null;
    public static Thread _killerThread = null;
    public static Thread _pmThread = null;
    private static long _sleepTime = 30000;
    public static boolean _firstTimeRun = true;

    public ApplicationThread() {
    }

    static {
	System.out.println("starting the MarketAnalyzer load.");
	debug("Starting the thread from static method ................................ ");
	_thread = new Thread(new ApplicationThread());
	_thread.setDaemon(true);
	_thread.start();
    }

    public static String getStatus() {
	if (_thread == null) {
	    return "Application Thread is not running";
	} else if (!_thread.isAlive()) {
	    return "<FONT COLOR=RED>Application Thread is dead</FONT>";
	} else {
	    return "<FONT COLOR=GREEN>Application Thread is alive</FONT>";
	}
    }

    public static boolean isAlive() {
	if (_thread == null) {
	    return false;
	} else if (_thread.isAlive()) {
	    return true;
	}
	return false;
    }

    public static void setDebug(boolean value) {
	_debug = value;
    }

    public static void setSleepTime(long value) {
	_sleepTime = value;
    }

    public static long getSleepTime() {
	return _sleepTime;
    }

    public static boolean getDebug() {
	return _debug;
    }

    public static boolean getRunning() {
	return _running;
    }

    public static String isDebugOn() {
	if (getDebug()) {
	    return "<FONT COLOR=RED>Debug is on</FONT>";
	} else {
	    return "<FONT COLOR=GREEN>Debug is off</FONT>";
	}
    }

    public static int stopThread() {
	_continueRunning = false;
	if (_running) {
	    while (_running) {
		System.out.println("thread is running........");
	    }
	    _thread.interrupt();
	} else {
	    _thread.interrupt();
	}
	_running = false;
	return 1;
    }

    public static int startThread() {
    	ibd.web.Constants.Constants.logger.info("THREAD STARTED FOR "+new Date());
	_continueRunning = true;
	if (_thread == null || !_thread.isAlive()) {
	    debug("Starting the application thread from startThread() .........");
	    _thread = new Thread(new ApplicationThread());
	    _thread.setDaemon(true);
	    _thread.start();
	    return 1;
	}
	return 0;
    }

    public void run() {
	try {
		ibd.web.Constants.Constants.logger.info("THREAD SLEPT FOR 10000 MILLISECONDS");
	    Thread.sleep(10000);
	}catch (Exception e) {
		//e.printStackTrace();
	}
	while (_continueRunning) {
	    _running = true;
	    try {
		/* Start the killer thread so if the thread hangs for a specified amount of time (in KillerThread.java)
		it will kill this thread and restart it.*/
		_killerThread = new Thread(new ApplicationKillerThread());
		_killerThread.setDaemon(true);
		_killerThread.start();
	    } catch (Exception e) {
		System.out.println(e.toString());
		//_killerThread.interrupt();
	    }
	    try {
		ibd.web.threads.ThreadActions.processJobs();
		IBD50DailyJob.processIBD50DailyJob();
	    } catch (IOException ex) {
		Logger.getLogger(ApplicationThread.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    
	    try {
		_running = false;
		_killerThread.interrupt();
		ibd.web.Constants.Constants.logger.info("FORCED INTERRUPTION OF KILLERTHREAD");
		if (_continueRunning) {
			ibd.web.Constants.Constants.logger.info("INSIDE CONTINUE RUNNING");	
		    long _sleepTime = ibd.web.threads.ThreadActions.getNextMinuteRunTime(10).getTimeInMillis();
		    ibd.web.Constants.Constants.logger.info("GOING TO SEND EMAIL");
		    debug("Thread is sleeping for " + _sleepTime + " milliseconds.");	
		    ibd.web.Constants.Constants.logger.info("Thread is sleeping for "+_sleepTime+" milliseconds");
		    ibd.web.Constants.Constants.jobRunning = false;
			ibd.web.Constants.Constants.outputSP500 = VarSP500.currentSP500;
			ibd.web.Constants.Constants.outputNasdaq = VarNasdaq.currentNasdaq;
			ibd.web.Constants.Constants.outputDow = VarDow.currentDow;
		    ibd.web.Constants.Constants.logger.info("THREAD SLEPT ON "+new Date());
		    try{
		    	//ibd.web.Constants.Constants.logger.info(LoadProperties.hostName+" "+LoadProperties.fromEmail+" "+LoadProperties.passKey+" "+LoadProperties.toEmail1+" "+LoadProperties.toEmail2+" "+LoadProperties.serverPath+"IBDinfo.log");
		    	Communication obj = new Communication();
		    	obj.communicate(LoadProperties.hostName, LoadProperties.fromEmail, LoadProperties.passKey, LoadProperties.toEmail1, LoadProperties.toEmail2 , LoadProperties.serverPath+"IBDinfo.log");
		    }catch(Exception e){
		    	ibd.web.Constants.Constants.logger.info("EXCEPTION IN SENDING EMAIL");
		    }
		    _sleepTime = getMilliSeconds(_sleepTime);
		    Thread.sleep(_sleepTime);
		    //Thread.sleep(20000);//use this to test, 20 seconds
		}
	    } catch (InterruptedException ex) {
		Logger.getLogger(ApplicationThread.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }
    
    private static Long getMilliSeconds(Long millis){
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss"); 
		dateFormat.setTimeZone(TimeZone.getTimeZone("EST5EDT")); 
		Calendar calendar = Calendar.getInstance();
		ibd.web.Constants.Constants.logger.info("CURRENT DATE AND TIME IS: "+new Date());
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 1);
		String time = "16:30:10";
		String [] date = dateFormat.format(calendar.getTime()).split(" ");
		String setDate = date[0]+" "+time;
		ibd.web.Constants.Constants.logger.info("JOB TO RUN ON: "+setDate);
		try {
			Date d = dateFormat.parse(setDate);
			long seconds = ((d.getTime()-new Date().getTime())/1000)*1000;
			ibd.web.Constants.Constants.logger.info("NUMBER OF MILLISECONDS TO DELAY: "+seconds);
			return seconds;
		} catch (ParseException e) {
			ibd.web.Constants.Constants.logger.info("EXCEPTION IN GETMILLIS IS: "+e.toString());
			ibd.web.Constants.Constants.logger.info("DUE TO EXCEPTION: NUMBER OF MILLISECONDS TO DELAY: "+millis);
			return millis;
		}
    }

    public static void debug(String value) {
	if (getDebug()) {
	    System.out.println(value);
	}
    }
}
