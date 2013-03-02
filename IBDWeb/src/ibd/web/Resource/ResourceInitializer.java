package ibd.web.Resource;

import ibd.web.classes.VarDow;
import ibd.web.classes.VarNasdaq;
import ibd.web.classes.VarSP500;
import ibd.web.controller.IndexController;
import ibd.web.threads.ApplicationThread;

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

	public void initMethod() {
		System.out.println(message);
			ApplicationThread.startThread();
	}
}
