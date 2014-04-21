package com.relevance.e2emf.service;

import com.relevance.e2emf.dao.E2emfDao;
import com.relevance.e2emf.dao.MasterDao;
import com.relevance.e2emf.dao.MaterialDao;

import com.relevance.e2emf.dao.ProfileDao;
import com.relevance.e2emf.dao.PurchaseOrderDao;
import com.relevance.e2emf.dao.PurchaseOrderDao;
import com.relevance.e2emf.dao.SearchDao;

/**
 * @author emanuel
 * E2emf Service Locator 
 *
 */
public class E2emfServiceLocator {
	
	public static Service getServiceInstance(String serviceName) {
		Service service = null;
		
		
		
		if(serviceName.equalsIgnoreCase("master")){
			service = new MasterService();
		}
		else if(serviceName.equalsIgnoreCase("search")){
			service = new SearchService();
			
		}else if(serviceName.equalsIgnoreCase("material")){
		
		service = new MaterialService();
		}else if(serviceName.equalsIgnoreCase("purchase")){
		
		service = new PurchaseOrderService();
		}
		else if(serviceName.equalsIgnoreCase("profile")){
			service = new ProfileService();
		}
		
		return service;
		
	}

	public static E2emfDao getServiceDaoInstance(String serviceName) {
		E2emfDao daoService = null;
		if(serviceName.equalsIgnoreCase("master")){
			daoService = new MasterDao();
		}
		else if(serviceName.equalsIgnoreCase("search")){
			daoService = new SearchDao();
			
		}else if(serviceName.equalsIgnoreCase("material")){		
			daoService = new MaterialDao();
			
		}else if(serviceName.equalsIgnoreCase("purchase")){		
			daoService = new PurchaseOrderDao();
			
		    
		}
		else if(serviceName.equalsIgnoreCase("profile")){
			daoService = new ProfileDao();
		}
		return daoService;
	}

}
