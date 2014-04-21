package com.relevance.e2emf.rest;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.commons.Log;

/**
 * @author emanuel
 * E2emf Profile Resource 
 *
 */
@Path("/profiling")
public class ProfileResource {
	
	
	@Context
	ServletContext context;	
	
	
	
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/dropship/")
	public String getDropShipData() {		
		String poProfilData = null;		
		Log.Info("Fetching ProfileData for DropShip from the Context ");
		
		poProfilData = this.context.getAttribute("poprofiledata").toString();
		Log.Info("ProfileData fetched from Context " + poProfilData);
		
		if(poProfilData != null){
			Log.Info("ProfileData fetched from Context " + poProfilData);
		
			
		}else{
			System.out.println("Profile Data NOT AVAILABLE on the context...");
		}			
		
		Log.Info("Returning ProfileData fetched from Context " + poProfilData);
		
		return poProfilData;
		
		
	}
}
