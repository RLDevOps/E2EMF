package com.relevance.e2emf.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.relevance.e2emf.commons.E2emfAppUtil;
import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.commons.Log;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;





import org.apache.log4j.Logger;
	
/**
 * @author emanuel
 * E2emf RequestProcessor 
 *
 */
public class RequestProcessor implements ContainerRequestFilter{
	
	
	
	@Context
    UriInfo uriInfo;

	@Override
	public ContainerRequest filter(ContainerRequest request) {
		String requestUrl = request.getAbsolutePath().toString();
	//	Log.Info("Recieved " + request + " for processing");
		System.out.println("Recieved Request " + request.getBaseUri() + " " + request.getAbsolutePath() + "for processing");
		Log.Info("Base Url :" + request.getBaseUri() + " Absolute Path : " + request.getAbsolutePath());
		System.out.println(" requested URl : " + requestUrl);
		/*UriBuilder ub = uriInfo.getAbsolutePathBuilder();
		URI requestUri = null;
		
		if (requestUrl.contains("onload")) {			
			requestUri = ub.path("processInitialLoad").build();
		} else if (requestUrl.contains("search")) {
			System.out.println("search service Called");
			//requestUri = ub.path("search").build();
		} else if (requestUrl.contains("profile")) {
			System.out.println("profile service Called");
			//requestUri = ub.path("profile").build();
		} else if (requestUrl.contains("master")) {
			System.out.println("master service Called");
		   //requestUri = ub.path("master").build();
		} else{
			System.out.println("No Specific request recieved, hence fowarding to startupService");
			requestUri = ub.path("onload/processInitialLoad").build();
		}
		
		requestUri = ub.path("onload/processInitialLoad").build();
		request.setUris(request.getBaseUri(), requestUri);
		
		Log.Debug("Forwarding the request to container with URL: " + request.getAbsolutePath());
		
*/		
		return request;	
	}

}
