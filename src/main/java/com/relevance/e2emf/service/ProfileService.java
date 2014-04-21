package com.relevance.e2emf.service;

import com.relevance.e2emf.commons.E2emfAppUtil;
import com.relevance.e2emf.dao.E2emfDao;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.domain.Profile;
import com.relevance.e2emf.domain.PurchaseOrder;
import com.relevance.e2emf.view.E2emfView;
import com.relevance.e2emf.commons.Log;
/**
 * @author emanuel
 * E2emf Profile Service 
 *
 */
public class ProfileService implements Service {
	
	public ProfileService(){
		
	}

	
	@Override
	public String getJsonObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJsonObject(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public E2emfView getViewObject(E2emfBusinessObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateJsonString(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E2emfBusinessObject getBusinessObject() {
		Profile profile = null;
		Log.Info("getBusinessObject called on Profile");
		try{
			E2emfDao profileDao = E2emfServiceLocator.getServiceDaoInstance("profile");		
			profile = (Profile) profileDao.getBusinessObject();				
			Log.Info("Fetched Profile object from DAO " + profile);
				
		}catch(Exception e){
			Log.Error("Exception fetting Profile Object " + e.getStackTrace());
		}
		
		
		return profile;
	}

	@Override
	public E2emfBusinessObject getBusinessObject(Object[] obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E2emfView getViewObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E2emfView getViewObject(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
