package com.relevance.e2emf.domain;

import java.util.ArrayList;
import java.util.List;
/**
 * @author emanuel
 * E2emf Master Business Object  
 *
 */
public class Master extends E2emfBusinessObject {

	List<MasterPojo> masterPojoList = new ArrayList<MasterPojo>();

	public List<MasterPojo> getMasterDataList() {
		return masterPojoList;
	}

	public void setMasterDataList(List<MasterPojo> masterPojoList) {
		masterPojoList = masterPojoList;
	}

}
