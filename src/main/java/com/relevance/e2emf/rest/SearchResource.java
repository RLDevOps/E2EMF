package com.relevance.e2emf.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.commons.Log;
import com.relevance.e2emf.service.E2emfServiceLocator;
import com.relevance.e2emf.service.Service;

/**
 * @author emanuel
 * E2emf Search Resource 
 *
 */

@Path("/search")
@Consumes("application/javascript")
public class SearchResource {
	
	
	@GET
	  @Produces(MediaType.APPLICATION_JSON)
	  @Path("/searchservice/{varX}")
	  public String searchData(@PathParam("varX") String varX){
		
		String searchResults = null;
		Log.Info("SearchService Invoked with param " +varX);		
		try{
			Service service = E2emfServiceLocator.getServiceInstance("search");			
			searchResults = (String) service.getJsonObject(varX,"searchService");
			Log.Info("Search Json fetched is \n " + searchResults);
		
		}catch(Exception e){
			Log.Error("Exception in searchService " + e.getStackTrace());
				e.printStackTrace();
				
			}
		Log.Info("Search Json fetched is \n " + searchResults);
		return searchResults;
	}
	
	
	
}
