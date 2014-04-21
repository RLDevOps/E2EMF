package com.relevance.e2emf.service;

import java.util.List;

import com.relevance.e2emf.commons.E2emfAppUtil;
import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.dao.E2emfDao;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.domain.Master;
import com.relevance.e2emf.domain.MasterPojo;
import com.relevance.e2emf.domain.PurchaseOrder;
import com.relevance.e2emf.view.E2emfView;
import com.relevance.e2emf.commons.Log;
public class MasterService implements Service {
	E2emfAppUtil appUtil;
	
	public MasterService() {
		appUtil = new E2emfAppUtil();		
	}

	@Override
	public String getJsonObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJsonObject(Object... obj) {
		Log.Info("getJsonObject called to fetch Master Json Obj...");
		String masterSearchJson = null;
		try{

			E2emfDao masterDao = E2emfServiceLocator.getServiceDaoInstance("master");
			E2emfBusinessObject master = masterDao.getBusinessObject(obj);
			masterSearchJson = generateJsonString(master);
			
			Log.Info("MasterSearchJson generated is : " + masterSearchJson);

		}catch(Exception e){
			Log.Error("getJsonObject called to fetch Master Json Obj..." + e.getStackTrace());
			e.printStackTrace(System.out);
		}


		return masterSearchJson;
	}

	@Override
	public E2emfBusinessObject getBusinessObject() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public E2emfView getViewObject(E2emfBusinessObject material) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateJsonString(Object... obj) {
		StringBuilder sbNode = new StringBuilder();
		String masterDataJson = "";
		
		
		List<MasterPojo> masterPojoList = null;
		if(obj != null && obj[0] != null){
			Master master = (Master) obj[0];
			masterPojoList = master.getMasterDataList();
	
		}else {
			Log.Error("Obj is null to creat json ..");
			return "";
		}
		
	

		try {
			sbNode.append("[");

			if (masterPojoList.size() != 0) {
				for (MasterPojo masterPojo : masterPojoList) {
					sbNode.append("{");
					sbNode.append(appUtil.getKeyValue("id", masterPojo.getId()));
					sbNode.append(",");
					sbNode.append(appUtil.getKeyValue("name",
							masterPojo.getName()));
					sbNode.append("}");
					sbNode.append(",");
				}
				masterDataJson = sbNode.toString();
				masterDataJson = masterDataJson.substring(0,
						masterDataJson.length() - 1);
			} else {
				masterDataJson = masterDataJson + "[";
			}

			masterDataJson = masterDataJson + "]";

		} catch (Exception e) {
			Log.Error("Exception creating JsonObject for Master..." + e.getStackTrace());
			e.printStackTrace();
		}

		return masterDataJson;
	}

	

}
