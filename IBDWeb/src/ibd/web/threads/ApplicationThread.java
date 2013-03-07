package ibd.web.threads;

import ibd.web.Resource.LoadProperties;
import ibd.web.Resource.SendEmail;
import ibd.web.classes.VarDow;
import ibd.web.classes.VarNasdaq;
import ibd.web.classes.VarSP500;

import java.io.IOException;
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
    	ibd.web.Resource.ResourceInitializer.logger.info("THREAD STARTED");
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
		ibd.web.Resource.ResourceInitializer.logger.info("THREAD SLEPT FOR 10000 MILLISECONDS");
	    Thread.sleep(10000);
	}catch (Exception e) {
		e.printStackTrace();
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
	    } catch (IOException ex) {
		Logger.getLogger(ApplicationThread.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    
	    try {
		_running = false;
		_killerThread.interrupt();
		ibd.web.Resource.ResourceInitializer.logger.info("FORCED INTERRUPTION OF KILLERTHREAD");
		if (_continueRunning) {
			ibd.web.Resource.ResourceInitializer.logger.info("INSIDE CONTINUE RUNNING");	
		    long _sleepTime = ibd.web.threads.ThreadActions.getNextMinuteRunTime(10).getTimeInMillis();
		    ibd.web.Resource.ResourceInitializer.logger.info("GOING TO SEND EMAIL");
		    debug("Thread is sleeping for " + _sleepTime + " milliseconds.");	
		    ibd.web.Resource.ResourceInitializer.logger.info("Thread is sleeping for "+_sleepTime+" milliseconds");
		    ibd.web.Constants.Constants.jobRunning = false;
			ibd.web.Constants.Constants.outputSP500 = VarSP500.currentSP500;
			ibd.web.Constants.Constants.outputNasdaq = VarNasdaq.currentNasdaq;
			ibd.web.Constants.Constants.outputDow = VarDow.currentDow;
		    try{
		    	ibd.web.Resource.ResourceInitializer.logger.info(LoadProperties.hostName+" "+LoadProperties.fromEmail+" "+LoadProperties.passKey+" "+LoadProperties.toEmail);
			new ibd.web.Resource.SendEmail().sendEmail(LoadProperties.hostName, LoadProperties.fromEmail, LoadProperties.passKey, LoadProperties.toEmail , "/var/lib/openshift/5138e23f5004466868000261/app-root/runtime/repo/IBDinfo.log");
		    }catch(Exception e){
		    	ibd.web.Resource.ResourceInitializer.logger.info("EXCEPTION IN SENDING EMAIL");
		    }
		    ibd.web.Resource.ResourceInitializer.logger.info("THREAD SLEPT ENDEDDDDDDDDDDDDDDDDDDD");
		    Thread.sleep(_sleepTime);
		    //Thread.sleep(20000);//use this to test, 20 seconds
		}
	    } catch (InterruptedException ex) {
		Logger.getLogger(ApplicationThread.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
    }

    public static void debug(String value) {
	if (getDebug()) {
	    System.out.println(value);
	}
    }
}
