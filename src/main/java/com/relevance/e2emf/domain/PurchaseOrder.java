package com.relevance.e2emf.domain;

import java.util.ArrayList;
import java.util.List;

import com.relevance.e2emf.view.FlowData;
/**
 * @author emanuel
 * E2emf PurchaseOrder Business Object  
 *
 */
public class PurchaseOrder extends E2emfBusinessObject {
	
	List<String> companyCodeList = new ArrayList<String>();
	List<String> plantList = new ArrayList<String>();
	List<String> purchaseOrderList = new ArrayList<String>();
	List<String> purchaseGroupList = new ArrayList<String>();
	List<String> docTypeList = new ArrayList<String>();	
	List<String> payTermList = new ArrayList<String>();
	
	List<FlowData> companyPlantFlowList = null;
	List<FlowData> plantPurchaseFlowList = null;
	List<FlowData> purchaseDocumentFlowList = null;
	List<FlowData> documentPayTermsFlowList = null;
	
	List<FlowData> purchaseOrderflowDataList = null;
	
	public List<String> getCompanyCodeList() {
		return companyCodeList;
	}
	public void setCompanyCodeList(List<String> companyCodeList) {
		this.companyCodeList = companyCodeList;
	}
	public List<String> getPlantList() {
		return plantList;
	}
	public void setPlantList(List<String> plantList) {
		this.plantList = plantList;
	}
	public List<String> getPurchaseOrderList() {
		return purchaseOrderList;
	}
	public void setPurchaseOrderList(List<String> purchaseOrderList) {
		this.purchaseOrderList = purchaseOrderList;
	}

	/**
	 * @return the purchaseGroupList
	 */
	public List<String> getPurchaseGroupList() {
		return purchaseGroupList;
	}
	/**
	 * @param purchaseGroupList the purchaseGroupList to set
	 */
	public void setPurchaseGroupList(List<String> purchaseGroupList) {
		this.purchaseGroupList = purchaseGroupList;
	}
	public List<String> getDocTypeList() {
		return docTypeList;
	}
	public void setDocTypeList(List<String> docTypeList) {
		this.docTypeList = docTypeList;
	}
	public List<String> getPayTermList() {
		return payTermList;
	}
	public void setPayTermList(List<String> payTermList) {
		this.payTermList = payTermList;
	}
	public List<FlowData> getCompanyPlantFlowList() {
		return companyPlantFlowList;
	}
	public void setCompanyPlantFlowList(List<FlowData> companyPlantFlowList) {
		this.companyPlantFlowList = companyPlantFlowList;
	}
	public List<FlowData> getPlantPurchaseFlowList() {
		return plantPurchaseFlowList;
	}
	public void setPlantPurchaseFlowList(List<FlowData> plantPurchaseFlowList) {
		this.plantPurchaseFlowList = plantPurchaseFlowList;
	}
	public List<FlowData> getPurchaseDocumentFlowList() {
		return purchaseDocumentFlowList;
	}
	public void setPurchaseDocumentFlowList(List<FlowData> purchaseDocumentFlowList) {
		this.purchaseDocumentFlowList = purchaseDocumentFlowList;
	}
	public List<FlowData> getDocumentPayTermsFlowList() {
		return documentPayTermsFlowList;
	}
	public void setDocumentPayTermsFlowList(List<FlowData> documentPayTermsFlowList) {
		this.documentPayTermsFlowList = documentPayTermsFlowList;
	}
	public List<FlowData> getPurchaseOrderflowDataList() {
		return purchaseOrderflowDataList;
	}
	public void setPurchaseOrderflowDataList(
			List<FlowData> purchaseOrderflowDataList) {
		this.purchaseOrderflowDataList = purchaseOrderflowDataList;
	}
	
}
