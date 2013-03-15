package ibd.web.Resource;

import ibd.web.threads.ApplicationThread;

import org.apache.log4j.Logger;

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
			ApplicationThread.startThread();
	}
}