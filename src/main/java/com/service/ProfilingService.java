/**
 * 
 */
package com.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dao.POProfilingDAO;

/**
 * @author Sai kumar --Service for to display Profiling page 
 * 
 */
@Path("/profiling")
public class ProfilingService {
	
	@Context
	ServletContext context;
	
	private static Logger log = LogManager.getLogger(ProfilingService.class);	

	public ProfilingService(@Context ServletContext value) throws IOException{
		this.context = value;						
	}	
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/dropship/")
	public String getDropShipData() {
		
		String data = this.context.getAttribute("poprofiledata").toString();
		return data;

	}
	
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/soprofile/")
	public String getSoProfileData() throws IOException {
		
		log.info("service for soprofile data is called ");
		String data = this.context.getAttribute("soprofiledata").toString();
		return data;
	
	}
	
}
