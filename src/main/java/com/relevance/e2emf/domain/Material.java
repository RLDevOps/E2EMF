package com.relevance.e2emf.domain;

import java.util.ArrayList;
import java.util.List;

import com.relevance.e2emf.view.FlowData;
/**
 * @author emanuel
 * E2emf Material Business Object  
 *
 */
public class Material extends E2emfBusinessObject {
	
	List<String> vendorList = null; //new ArrayList<String>();
	List<String> plantList = null; //new ArrayList<String>();
	List<String> customerList = null; //new ArrayList<String>();
	List<String> materialList = null; //new ArrayList<String>();
	List<String> typeList =  null; //new ArrayList<String>();
	
	List<FlowData> vendorPlantFlowList = null; //new ArrayList<FlowData>();
	List<FlowData> plantPlantFlowList = null; //new ArrayList<FlowData>();
	List<FlowData> plantCustomerFlowList = null; //new ArrayList<FlowData>();
	List<FlowData> plantCustomerInterCompanyFlowList  = null; //new ArrayList<FlowData>();
	
	List<FlowData> materialflowList = new ArrayList<FlowData>();

	public List<String> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<String> vendorList) {
		this.vendorList = vendorList;
	}

	public List<String> getPlantList() {
		return plantList;
	}

	public void setPlantList(List<String> plantList) {
		this.plantList = plantList;
	}

	public List<String> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(List<String> customerList) {
		this.customerList = customerList;
	}

	public List<String> getMaterialList() {
		return materialList;
	}

	public void setMaterialList(List<String> materialList) {
		this.materialList = materialList;
	}

	public List<String> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<String> typeList) {
		this.typeList = typeList;
	}

	public List<FlowData> getVendorPlantFlowList() {
		return vendorPlantFlowList;
	}

	public void setVendorPlantFlowList(List<FlowData> vendorPlantFlowList) {
		this.vendorPlantFlowList = vendorPlantFlowList;
	}

	public List<FlowData> getPlantPlantFlowList() {
		return plantPlantFlowList;
	}

	public void setPlantPlantFlowList(List<FlowData> plantPlantFlowList) {
		this.plantPlantFlowList = plantPlantFlowList;
	}

	public List<FlowData> getPlantCustomerFlowList() {
		return plantCustomerFlowList;
	}

	public void setPlantCustomerFlowList(List<FlowData> plantCustomerFlowList) {
		this.plantCustomerFlowList = plantCustomerFlowList;
	}

	public List<FlowData> getPlantCustomerInterCompanyFlowList() {
		return plantCustomerInterCompanyFlowList;
	}

	public void setPlantCustomerInterCompanyFlowList(
			List<FlowData> plantCustomerInterCompanyFlowList) {
		this.plantCustomerInterCompanyFlowList = plantCustomerInterCompanyFlowList;
	}

	public List<FlowData> getMaterialflowList() {
		return materialflowList;
	}

	public void setMaterialflowList(List<FlowData> materialflowList) {
		this.materialflowList = materialflowList;
	}	
	

}
