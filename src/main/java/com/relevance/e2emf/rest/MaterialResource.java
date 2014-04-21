/**
 * 
 */
package com.relevance.e2emf.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.relevance.e2emf.commons.Log;
import com.relevance.e2emf.service.E2emfServiceLocator;
import com.relevance.e2emf.service.Service;

/**
 * @author emanuel
 *
 */

@Path("/material")
public class MaterialResource {

	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCanadaLevel2MaterialView/{varX}")
	public String getCanadaLevel2MaterialView(@PathParam("varX") String varX) {
		String canadaLevel2MaterialViewJson = null;
		Log.Info("getCanadaLevel2MaterialView/{varX} on overView service called");
		
		if(!varX.contains(",")) {
			Service service = E2emfServiceLocator.getServiceInstance("search");			
			canadaLevel2MaterialViewJson = (String) service.getJsonObject(varX);	
			
		}
		
		return canadaLevel2MaterialViewJson;
	}

}
