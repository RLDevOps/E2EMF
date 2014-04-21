package com.relevance.e2emf.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.relevance.e2emf.commons.Log; 

import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.domain.Master;
import com.relevance.e2emf.domain.MasterPojo;
import com.relevance.e2emf.service.E2emfServiceLocator;
import com.relevance.e2emf.view.FlowData;
/**
 * @author emanuel
 * E2emf Master Data Access Object  
 *
 */
public class MasterDao implements E2emfDao{
	
	 E2emfDbUtil dbUtil = null;
	 static Connection con = null;
	 static Statement stmt = null;
	    
	    public MasterDao(){
	    	 dbUtil = new E2emfDbUtil();
	       	 dbUtil.initialize();
	       	 con = dbUtil.getHiveConnection();
	      
	    }

	@Override
	public E2emfBusinessObject getBusinessObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E2emfBusinessObject getBusinessObject(Object... obj) {
		Log.Error("Fetching Master Details from Db");
		
		String param = null;
		String searchParam = null;
		String masterSearchQuery = null;
		
		ResultSet rs = null;
		List<MasterPojo>  masterPojoList = new ArrayList<MasterPojo>();
		Master master = new Master();
		
		
		if(obj.length >1){			
			param = (String) obj[0];
			searchParam = (String) obj[1];
			Log.Info("Getting MASTER details for param " + param + " and search Str : " +searchParam);
		}else{
			Log.Info("No params specified for Master Search ");
		}
		
		if( param != null && searchParam != null){
				masterSearchQuery = dbUtil.buildMasterSearchQuery(param, searchParam);
				
				try{
					if (con == null)
						con = dbUtil.getHiveConnection();
					
					if(con != null){
						
						if(stmt == null)
							stmt = con.createStatement();
						
						rs = stmt.executeQuery(masterSearchQuery); 
				        
					   	if(rs != null) {        	
				        	while(rs.next()){
				        		MasterPojo masterPojo = new MasterPojo();
				        		masterPojo.setName(rs.getString("name"));
				        		masterPojo.setId(rs.getString("id"));
				        		
				        		masterPojoList.add(masterPojo);
				        	} 
				        	
				        	master.setMasterDataList(masterPojoList);
				        } 
				        else{
				        	Log.Info("ResultSet Empty for executed for query : \n " + masterSearchQuery);
				        } 
						
					}
					
				}catch(SQLException sql){
					Log.Error("Exception Fetching Master Details " + sql.getStackTrace());
					sql.getStackTrace();
				}catch(Exception e){
					Log.Error("Exception Fetching Master Details " + e.getStackTrace());
					e.printStackTrace();
				}
					
		}else{
			Log.Info("Criteria not set to fetch Master Details..");
		}
		return master;
		
		}

	@Override
	public List<FlowData> getFlowDataList(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String executeQuery(String masterSearchQuery) {
		// TODO Auto-generated method stub
		return null;
	}

}
