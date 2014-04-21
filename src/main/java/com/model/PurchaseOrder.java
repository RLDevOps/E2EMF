package com.model;

import java.util.ArrayList;
import java.util.List;

public class PurchaseOrder {
	List<String> companyCodeList = new ArrayList<String>();
	List<String> plantList = new ArrayList<String>();
	List<String> purchaseGroupList = new ArrayList<String>();
	List<String> docTypeList = new ArrayList<String>();	
	List<String> payTermList = new ArrayList<String>();
	/**
	 * @return the companyCodeList
	 */
	public List<String> getCompanyCodeList() {
		return companyCodeList;
	}
	/**
	 * @param companyCodeList the companyCodeList to set
	 */
	public void setCompanyCodeList(List<String> companyCodeList) {
		this.companyCodeList = companyCodeList;
	}
	/**
	 * @return the plantList
	 */
	public List<String> getPlantList() {
		return plantList;
	}
	/**
	 * @param plantList the plantList to set
	 */
	public void setPlantList(List<String> plantList) {
		this.plantList = plantList;
	}
	/**
	 * @return the purchaseOrderList
	 */
	public List<String> getPurchaseGroupList() {
		return purchaseGroupList;
	}
	/**
	 * @param purchaseOrderList the purchaseOrderList to set
	 */
	public void setPurchaseGroupList(List<String> purchaseOrderList) {
		this.purchaseGroupList = purchaseOrderList;
	}
	/**
	 * @return the docTypeList
	 */
	public List<String> getDocTypeList() {
		return docTypeList;
	}
	/**
	 * @param docTypeList the docTypeList to set
	 */
	public void setDocTypeList(List<String> docTypeList) {
		this.docTypeList = docTypeList;
	}
	/**
	 * @return the payTermList
	 */
	public List<String> getPayTermList() {
		return payTermList;
	}
	/**
	 * @param payTermList the payTermList to set
	 */
	public void setPayTermList(List<String> payTermList) {
		this.payTermList = payTermList;
	}
	
}