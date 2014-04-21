package com.relevance.e2emf.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.relevance.e2emf.commons.Log;
import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.view.FlowData;
/**
 * @author emanuel
 * E2emf Search Data Access Object  
 *
 */
public class SearchDao implements E2emfDao {
	
	E2emfDbUtil dbUtil = null;
    static Connection con = null;
    static Statement stmt = null;
    
    
    public SearchDao(){
    	
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
	
		return null;
	}

	@Override
	public List<FlowData> getFlowDataList(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String executeQuery(String query) {
		String nodeLevelJson = "";
		try{
			if(con == null)
				con = dbUtil.getHiveConnection ();		
			
			if(con != null){
				
				if(stmt == null)
				   stmt = con.createStatement();
				
			    ResultSet rs = stmt.executeQuery(query);
			    
			    if(rs != null ){
			    	
			    	nodeLevelJson = dbUtil.convert(rs);
			    }
			    	
			    }
			}catch(SQLException sql){
				Log.Error("SQLException executing Query " + sql.getStackTrace());
				sql.getStackTrace();
			}catch(Exception e){
				Log.Error("Exception in executing Query " + e.getMessage());
				e.printStackTrace();
			}
					
		
		return nodeLevelJson;
	}

	
}
