package com.startup;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

 
public class E2EMFListener implements ServletContextListener {
	
	private static Logger log = LogManager.getLogger(E2EMFListener.class);
	
	ServletContext context;		
	private static Thread e2emfDataLoader;
	private static Thread e2emfCanadaSankeyDataLoader;
	private static Thread soProfilingDataLoader;
	

	
	public void contextInitialized(ServletContextEvent contextEvent) {
		log.info("Context Created");
		context = contextEvent.getServletContext();
		if(context!=null){
			e2emfDataLoader = new Thread(new E2EMFDataLoaderThread(context));
			e2emfCanadaSankeyDataLoader = new Thread(new E2EMFSankeyDataLoaderThread(context));
			soProfilingDataLoader = new Thread(new E2EMFSOProfileDataLoaderThread(context));
			e2emfDataLoader.start();
			e2emfCanadaSankeyDataLoader.start();
			soProfilingDataLoader.start();
			
	 } else {
		 log.info("context is null");
	 }
	}
	public void contextDestroyed(ServletContextEvent contextEvent) {
		context = contextEvent.getServletContext();
		//directoryPoller.stop();
		e2emfDataLoader.stop();
		e2emfCanadaSankeyDataLoader.stop();		
		soProfilingDataLoader.start();
		log.info("Context Destroyed");
	}
}