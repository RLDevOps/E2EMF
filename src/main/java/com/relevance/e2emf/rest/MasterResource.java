package com.relevance.e2emf.rest;


import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.commons.Log;
import com.relevance.e2emf.service.E2emfServiceLocator;
import com.relevance.e2emf.service.Service;


/**
 * @author emanuel
 * E2emf Master Resource 
 *
 */
@Path("/MasterData")
@Consumes(MediaType.APPLICATION_JSON)
public class MasterResource {
	
	
	/*static{
		Log.initialize();
		E2emfDbUtil dbUtil = new E2emfDbUtil();
		//E2emfAppUtil appUtil = new E2emfAppUtil();
		
	}*/
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/searchMasterDatanew")
	public String getMasterData(@QueryParam("type") String varX,
			@QueryParam("q") String searchParam) {
		String masterDataJson = null;
		Log.Info("getMasterDatanew() called with param " + varX + " searchParam " + searchParam);
		try{
			
			Service service = E2emfServiceLocator.getServiceInstance("master");
			masterDataJson = (String) service.getJsonObject(varX, searchParam);
			
			Log.Info("MasterDataJson got from getJsonObject " + masterDataJson);
				
		}catch(Exception e){
			Log.Error("Exception getMasterData() of the MasterData with queryParams " + e.getStackTrace());	
		}
	
		return masterDataJson;
	}

}
