package com.relevance.e2emf.listener;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;



import com.relevance.e2emf.commons.Log;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.domain.Material;
import com.relevance.e2emf.domain.Profile;
import com.relevance.e2emf.service.E2emfServiceLocator;
import com.relevance.e2emf.service.Service;
import com.relevance.e2emf.view.*;
/**
 * @author emanuel
 * E2emf Server Context Event Listener 
 *
 */
public class E2emfContextListener implements ServletContextListener {
	
	ServletContext context;
	
	
	/* @author emanuel 
	 * Setting profile and Material details to context
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {
		
		if(!Log.isLoggerInitialized)
		    Log.initialize();
	
		context = contextEvent.getServletContext();
		Log.Info("contextInitialized, fetching profile and material details to be made available to app ");
		System.out.println("**contextInitialized() " + context);
		
		
		try{
			if(context != null){
				
				Service profileService = E2emfServiceLocator.getServiceInstance("profile");		
				
				Profile profile = (Profile) profileService.getBusinessObject();	
				String profileData = profile.getProfileInfo();
			
				Log.Info("Fetched ProfileDat on listener: " + profileData);	
				context.setAttribute("poprofiledata", profileData);
					
				Service materialService = E2emfServiceLocator.getServiceInstance("material");			
				Material material = (Material) materialService.getBusinessObject();
				
				List<FlowData> materialflowList = material.getMaterialflowList();	
				
				Log.Info("Fetched MaterialFlowList on listener: " + materialflowList);			
				context.setAttribute("e2emfsankeyflowdata", materialflowList);		
				
				Log.Info("Added Profile Data and MaterialFlowList to the context ");
				
				
		 } else {
			 System.out.println("This should never be executed...");
			 
		
		 }	
		}catch(Exception e){
			Log.Error(e);
			Log.Error("Exception, fetching profile and material details to be made available to app ");
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent contextEvent) {
			
		context = contextEvent.getServletContext();
		System.out.println("**contextDestroyed() " + context);
		Log.Info("contextDestroyed() " + context);	
		
		if(context != null){
			//context.removeAttribute("poprofiledata");
			//context.removeAttribute("e2emfsankeyflowdata");
		}		
		
	}

}
