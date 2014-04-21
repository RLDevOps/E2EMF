package com.relevance.e2emf.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.commons.Log;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.domain.Profile;
import com.relevance.e2emf.view.FlowData;
/**
 * @author emanuel
 * E2emf Profile Data Access Object  
 *
 */
public class ProfileDao implements E2emfDao {
	
	static String profileQuery = "select ebeln_count as po_count,bukrs as CompanyCode,ekorg as PurchaseOrg,ekgrp as PurchaseGroup,werks as PlantGroup,menge as OrderQuantity,netpr as NetPrice,brtwr as GrossOrderValue,source,aedat as LastChangedOn,material_type,vendor_type, case when po_type = 'OTHERS' then 'NORMAL PURCHASING' ELSE po_type end as po_type from JANDJ.e2e_dn_poprofile_red_view";
	
	E2emfDbUtil dbUtil = null;
    static Connection con = null;
    static Statement stmt = null;
    
    public ProfileDao(){    	
   	 
     dbUtil = new E2emfDbUtil();
     dbUtil.initialize();
     initializeProfile();
	 con = dbUtil.getHiveConnection();
     
   }

	private void initializeProfile() {
		String profileData = null;
		try{
			profileData = executeQuery(profileQuery);
			Profile.profileData = profileData;
		}catch(Exception e){
			Log.Error("Exception executing profileQuery to fetch profile Info" );
			e.printStackTrace();	
		}
	}

	@Override
	public E2emfBusinessObject getBusinessObject() {
		String profileInfo = null;
		Profile profile = new Profile();
		try{			
			profileInfo = executeQuery(profileQuery);
			profile.setProfileInfo(profileInfo);
			
		}catch(Exception e){
			
			e.printStackTrace();	
		}
		
		return profile;
	}

	@Override
	public E2emfBusinessObject getBusinessObject(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlowData> getFlowDataList(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String executeQuery(String query) {
		String profileCsv = null;
		try{
			
			if(con == null)
			  con = dbUtil.getHiveConnection ();		
			
			if(con != null){
				
				if(stmt == null)
				   stmt = con.createStatement();
			
				ResultSet rs = stmt.executeQuery(query);
				
			    if(rs != null ){			    	
			    	System.out.println("Fetcing csv format of the Profile ");
			    	profileCsv = dbUtil.getCSVFromResultSet(rs);			    	
			     }			    	
			   }
			}catch(SQLException sql){
				Log.Error("SQLException executing Query " + sql.getStackTrace());
				sql.getStackTrace();
			}catch(Exception e){
				Log.Error("Exception executing Query " + e.getMessage());
				e.printStackTrace();
			}
		return profileCsv;
	}

}
