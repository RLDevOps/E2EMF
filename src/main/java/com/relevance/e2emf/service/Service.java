package com.relevance.e2emf.service;

import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.view.E2emfView;
/**
 * @author emanuel
 * E2emf Service Interface 
 *
 */
public interface Service {
	
	public String getJsonObject();
	
	public Object getJsonObject(Object... obj);
	
	public E2emfBusinessObject getBusinessObject();

	public E2emfBusinessObject getBusinessObject(Object[] obj);

	public E2emfView getViewObject();
	
	public E2emfView getViewObject(Object... obj);
	
	public E2emfView getViewObject(E2emfBusinessObject businessObj);			
	
	public String generateJsonString(Object... obj);
	
}
