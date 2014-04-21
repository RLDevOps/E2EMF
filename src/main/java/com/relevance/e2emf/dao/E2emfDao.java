package com.relevance.e2emf.dao;

import java.util.List;

import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.domain.PurchaseOrder;
import com.relevance.e2emf.view.FlowData;
/**
 * @author emanuel
 * E2emf Data Access Interface  
 *
 */
public interface E2emfDao {
	
	
	public String executeQuery(String nodeLevelQuery);
	
	public List<FlowData> getFlowDataList(Object... obj);

	public E2emfBusinessObject getBusinessObject();
	
	public E2emfBusinessObject getBusinessObject(Object... obj);
	
	

}
