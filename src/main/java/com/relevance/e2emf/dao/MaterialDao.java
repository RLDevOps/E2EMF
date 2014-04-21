package com.relevance.e2emf.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.relevance.e2emf.commons.E2emfConstants;
import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.commons.Log;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.domain.Material;
import com.relevance.e2emf.view.FlowData;

import javax.ws.rs.core.Context;

public class MaterialDao implements E2emfDao{
	
    E2emfDbUtil dbUtil = null;
    static Connection con = null;
    static Statement stmt = null;
    
   
    
    public MaterialDao(){
    	 dbUtil = new E2emfDbUtil();
       	 dbUtil.initialize();
 		con = dbUtil.getHiveConnection();
      
    }
	

	@Override
	public E2emfBusinessObject getBusinessObject() {
		Log.Info("getBusinessObject() invoked on MaterialDao for initial load..." );
		Material material = new Material();
		List<FlowData> materialFlowList = null;	
		
		try{
			materialFlowList = retrieveFlowDataListDefaultView();		
			material.setMaterialflowList(materialFlowList);	
			
		}catch(Exception e){
			Log.Error("Exception fetching Material flow Datal list for initial Load " + e.getMessage());
		}
		
		return material;
	}

	/**
	 * @return
	 */
	private List<FlowData> retrieveFlowDataListDefaultView() {
		List<FlowData> flowDataList = new ArrayList<FlowData>();
		try{
			
			List<FlowData> flowDataListVendortoPlant = new ArrayList<FlowData>();
			List<FlowData> flowDataListPlanttoCustomer = new ArrayList<FlowData>();
			List<FlowData> flowDataListPlanttoCustomerInterCompany = new ArrayList<FlowData>();
			List<FlowData> flowDataListPlanttoPlant = new ArrayList<FlowData>();
			
			flowDataListVendortoPlant = getDefaultVendorsToPlantList();
			Log.Info("VendorsToPlantList fetched from Db");
			flowDataListPlanttoCustomer = getDefaultPlantToCustomersList();
			Log.Info("PlantToCustormerList fetched from Db");
			flowDataListPlanttoCustomerInterCompany = getDefaultIntercompanyDropShipList();
			Log.Info("PlantToCustormerInterCompanyList fetched from Db with size " + flowDataListPlanttoCustomerInterCompany.size());
			flowDataListPlanttoPlant = getDefaultPlantToPlant();
			Log.Info("PlantToPlantList fetched from Db with size " + flowDataListPlanttoPlant.size());
			
			flowDataList.addAll(flowDataListVendortoPlant);
			flowDataList.addAll(flowDataListPlanttoCustomer);
			flowDataList.addAll(flowDataListPlanttoCustomerInterCompany);
			flowDataList.addAll(flowDataListPlanttoPlant);
			
			Log.Info("Material accumulated Flow DataList formed with size " + flowDataList.size());
			
		}catch(Exception e){
			Log.Error("Exception in retrieveFlowDataListDefaultView " + e.getMessage());
			e.printStackTrace();
		}
		return flowDataList;
	}


	/**
	 * @return
	 */
	private List<FlowData> getDefaultPlantToPlant() {
		List<FlowData> flowDataList =  new ArrayList<FlowData>();		
		ResultSet rs2;
		String query = "select werks as plant, wname1 as plantname, concat('IP-',receiving_plant_id) as receivingplantid, receiving_plant_name as receivingplantname, so_type, mat_type, sum(sum_netwr) as netvalue from JANDJ.e2e_ag_sto_all where (1=1)  group by werks, receiving_plant_id, receiving_plant_name, wname1, so_type, mat_type having sum(sum_netwr) <> 0.0 order by netvalue desc limit 10";
		
		try{
			if(con == null)
				con = dbUtil.getHiveConnection ();		
			
				if(stmt == null)			
					stmt = con.createStatement();
			
			
			rs2 = stmt.executeQuery(query);
				
		if(rs2 != null) {     
			
			while(rs2.next()){

				FlowData flowData = new FlowData();

				flowData.setFromCode(rs2.getString("plant"));
				flowData.setFromName(rs2.getString("plantname"));
				flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				flowData.setFromType(E2emfConstants.MF_PLANT);

				flowData.setToCode(rs2.getString("receivingplantid"));
				flowData.setToName(rs2.getString("receivingplantname"));
				flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				//if(rs1.getString("receivingplantid").equals("2300")){
					flowData.setToType(E2emfConstants.DC_PLANT);
				//} else {
				//	flowData.setToType(MF_PLANT);
				//}

				flowData.setLinkType(rs2.getString("so_type"));
				flowData.setLinkValue(rs2.getDouble("netvalue"));
				flowData.setLinkSubType(rs2.getString("mat_type"));
				
				//System.out.println(flowData.getToCode()+":"+flowData.getToName()+":"+flowData.getToText()+":"+flowData.getToType());

				flowDataList.add(flowData);
			}        	
		} else{
			System.out.println("Resultset is null");
		}	
		}catch(SQLException sql){
			Log.Error("SQLException in getDefaultPlantToPlant " + sql.getStackTrace());
			sql.getStackTrace();
			
		}catch(Exception e){
			Log.Error("Exception in getDefaultPlantToPlant " + e.getMessage());
			e.getStackTrace();
		}
		// TODO Auto-generated method stub
		return flowDataList;
	}


	/**
	 * @return
	 */
	private List<FlowData> getDefaultIntercompanyDropShipList() {
		Log.Info("Fetching DefaultIntercompanyDropShipList...");
		List<FlowData> flowDataList =  new ArrayList<FlowData>();				
		ResultSet rs2;
		String query = "select concat('CC-', bukrs_vf) as salesorgcc,kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) and so_type='INTERCOMPANY DROPSHIP' group by bukrs_vf,kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 5";
		try{
			if(con == null)
				con = dbUtil.getHiveConnection ();		
			
				if(stmt == null)			
					stmt = con.createStatement();
				
				rs2 = stmt.executeQuery(query);
				
			while(rs2.next()){
				FlowData flowData = new FlowData();

				  flowData.setFromCode(rs2.getString("plant"));
				  flowData.setFromName(rs2.getString("plantname"));
				  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				  flowData.setFromType(E2emfConstants.MF_PLANT);

				  
				  flowData.setToCode(rs2.getString("salesorgcc"));
				  flowData.setToName(rs2.getString("salesorgcc"));
				  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				  flowData.setToType(E2emfConstants.DC_PLANT);

				  flowData.setLinkType(rs2.getString("so_type"));
				  flowData.setLinkSubType(rs2.getString("mat_type"));
				  flowData.setLinkValue(rs2.getDouble("netvalue")*10);//Added factor since not all SO data is loaded

				  flowDataList.add(flowData);
				  
				  
				  //Second Set
				  flowData = new FlowData();

				  flowData.setFromCode(rs2.getString("salesorgcc"));
				  flowData.setFromName(rs2.getString("salesorgcc"));
				  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				  flowData.setFromType(E2emfConstants.DC_PLANT);

				  
				  flowData.setToCode(rs2.getString("customerid"));
				  flowData.setToName(rs2.getString("customername"));
				  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				  flowData.setToType(E2emfConstants.CUSTOMER);

				  flowData.setLinkType(rs2.getString("so_type"));
				  flowData.setLinkSubType(rs2.getString("mat_type"));
				  flowData.setLinkValue(rs2.getDouble("netvalue")*10);//Added factor since not all SO data is loaded
				  
				  flowDataList.add(flowData);
				 
			}
			
		}catch(SQLException sql){
			Log.Error("SQLException in getDefaultIntercompanyDropShipList " + sql.getStackTrace());
			sql.getStackTrace();
		}	
		catch(Exception e){		
			Log.Error("Exception in getDefaultIntercompanyDropShipList " + e.getMessage());
			e.printStackTrace();
		}
		
		 Log.Info("Retrieved DefaultIntercompanyDropShipFlowDataList from Db with size " + flowDataList.size());
		
		return flowDataList;
	}


	/**
	 * @return
	 */
	private List<FlowData> getDefaultPlantToCustomersList() {
		List<FlowData> flowDataList =  new ArrayList<FlowData>();
		ResultSet rs2;
		String query = "select kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) and so_type='DIRECT SALES' group by kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 5 ";
		 StringBuilder completeQuery = new StringBuilder(query);		 
		 completeQuery.append("union all select kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) and so_type='CONSIGNMENT' group by kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 5 ");
		 
		try{
			if(con == null)
				con = dbUtil.getHiveConnection ();		
			
				if(stmt == null)			
					stmt = con.createStatement();
		
			rs2 = stmt.executeQuery(completeQuery.toString());		
			while(rs2.next()){
				 FlowData flowData = new FlowData();

				  flowData.setFromCode(rs2.getString("plant"));
				  flowData.setFromName(rs2.getString("plantname"));
				  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				  flowData.setFromType(E2emfConstants.MF_PLANT);

				  flowData.setToCode(rs2.getString("customerid"));
				  flowData.setToName(rs2.getString("customername"));
				  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				  flowData.setToType(E2emfConstants.CUSTOMER);

				  flowData.setLinkType(rs2.getString("so_type"));
				  flowData.setLinkSubType(rs2.getString("mat_type"));
				  flowData.setLinkValue(rs2.getDouble("netvalue")*10);//Added factor since not all SO data is loaded

				  flowDataList.add(flowData);
			}
		}catch(Exception e){
			Log.Error("Exception in getDefaultPlantToCustomersList " + e.getMessage() );
			e.getStackTrace();
		}
		
		return flowDataList;
	}


	/**
	 * @return
	 */
	private List<FlowData> getDefaultVendorsToPlantList() {
		List<FlowData> flowDataList =  new ArrayList<FlowData>();	
		ResultSet rs2 = null;
		
		 String query = "select lifnr as vendorid, lname1 as vendorname, werks as plant, wname1 as plantname, po_type, sum(netwr) as netvalue, vendor_type, material_type from JANDJ.E2E_DN_AG_PROF where (1=1) and po_type = 'NORMAL PURCHASING' group by lifnr,lname1,werks,wname1,PO_type, vendor_type,material_type order by sum(netwr) desc limit 5 ";
		 StringBuilder completeQuery = new StringBuilder(query);
		 completeQuery.append("union all select lifnr as vendorid, lname1 as vendorname, werks as plant, wname1 as plantname, po_type, sum(netwr) as netvalue, vendor_type, material_type from JANDJ.E2E_DN_AG_PROF where (1=1) and po_type = 'DROP SHIP' group by lifnr,lname1,werks,wname1,PO_type, vendor_type,material_type order by sum(netwr) desc limit 5 ");
		 completeQuery.append("union all select lifnr as vendorid, lname1 as vendorname, werks as plant, wname1 as plantname, po_type, sum(netwr) as netvalue, vendor_type, material_type from JANDJ.E2E_DN_AG_PROF where (1=1) and po_type = 'EXTERNAL PROCESSING' group by lifnr,lname1,werks,wname1,PO_type, vendor_type,material_type order by sum(netwr) desc limit 5 ");
		 completeQuery.append("union all select lifnr as vendorid, lname1 as vendorname, werks as plant, wname1 as plantname, po_type, sum(netwr) as netvalue, vendor_type, material_type from JANDJ.E2E_DN_AG_PROF where (1=1) and po_type = 'SUB CONTRACTING' group by lifnr,lname1,werks,wname1,PO_type, vendor_type,material_type order by sum(netwr) desc limit 5");
		
		 
		try{
			if(con == null)
				con = dbUtil.getHiveConnection ();		
			
				if(stmt == null)			
					stmt = con.createStatement();
			
			rs2 = stmt.executeQuery(completeQuery.toString());	
			
			while(rs2.next()){
				//vendorList.add(rs2.getString("vendorid"));
				
				FlowData flowData = new FlowData();

				flowData.setFromCode(rs2.getString("vendorid"));
				flowData.setFromName(rs2.getString("vendorname"));
				flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				// make changes to identify the External vendors v/s J&J affiliate vendors
				//flowData.setFromType("VENDOR");
				if(rs2.getString("vendor_type").equalsIgnoreCase(E2emfConstants.EXTERNAL_VENDOR)){
					flowData.setFromType(E2emfConstants.EXTERNAL_VENDOR);
				} else if(rs2.getString("vendor_type").equalsIgnoreCase(E2emfConstants.INTERNAL_VENDOR)){
					flowData.setFromType(E2emfConstants.INTERNAL_VENDOR);
				}

				flowData.setToCode(rs2.getString("plant"));
				flowData.setToName(rs2.getString("plantname"));
				flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				flowData.setToType(E2emfConstants.MF_PLANT);

				flowData.setLinkType(rs2.getString("po_type"));
				flowData.setLinkSubType(rs2.getString("material_type"));
				flowData.setLinkValue(rs2.getDouble("netvalue"));

				flowDataList.add(flowData);				
			}
			
		
		}catch(SQLException e){
			Log.Error("Exception in getDefaultVendorsToPlantList " + e.getStackTrace());
			e.printStackTrace();
		}catch(Exception e){
			Log.Error("Exception in getDefaultVendorsToPlantList " + e.getMessage());
			e.printStackTrace();
		}
		return flowDataList;
	}


	@Override
	public E2emfBusinessObject getBusinessObject(Object... obj) {	
		
		Log.Info("***Fetching Material Details based on the input.... ");
		
		Material material = new Material();
		List<FlowData> materialflowList = null;
		
		List<String> vendorList = new ArrayList<String>();
		List<String> plantList = new ArrayList<String>();
		List<String> customerList = new ArrayList<String>();
		List<String> materialList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();
		
		String supplier = null;
		String customer = null;
		String meterialId = null;
		String tier1 = null;
		String tier2 = null;		
		String type = null;
		Log.Info("Fetching Data List based on the Inputs \n  Supplier : " + obj[0] + "\n Customer : " + obj[1] + "\n meterialId " + obj[2] + "\n Tier1 : " + obj[3] + "\n type : " + obj[5]);
		
		try{
			if(obj[0] != null )
			    supplier = (String) obj[0];
			if(obj[1] != null )
				customer = (String) obj[1];
			if(obj[2] != null )
				meterialId = (String) obj[2];
			if(obj[3] != null )
			    tier1 = (String) obj[3];
			if(obj[4] != null )
				tier2 = (String) obj[4];
			if(obj[5] != null )
		 	    type = (String) obj[5];
			
			Log.Info("Fetching Data List based on the Inputs \n  Supplier : " + supplier + "\n Customer : " + customer + "\n meterialId " + meterialId + "\n Tier1 : " + tier1 + "\n type : " + type);
			
			
			if (meterialId != null && !"null".equalsIgnoreCase(meterialId) && !"".equalsIgnoreCase(meterialId))
			{	
				for(String mId : meterialId.split(","))
					materialList.add(mId);
				
				Log.Info("Material List created based on Input has size " + materialList.size());
			}
			
			if (type != null && !"null".equalsIgnoreCase(type) && !"".equalsIgnoreCase(type))
			{	
				for(String typeName : type.split(","))
					typeList.add(typeName); 
				
				Log.Info("TypeList List created based on Input has size " + typeList.size());
			}
			
			if (supplier != null && !"null".equalsIgnoreCase(supplier) && !"".equalsIgnoreCase(supplier))
			{	for(String vId : supplier.split(","))
					vendorList.add(vId); 
				
				Log.Info("vendorList List created based on Input has size " + vendorList.size());
			}
			
			if (customer != null && !"null".equalsIgnoreCase(customer) && !"".equalsIgnoreCase(customer))
			{	
				for(String cId : customer.split(","))
					customerList.add(cId);
				
				Log.Info("customerList List created based on Input has size " + customerList.size());
			}
			
			if (tier1 != null && !"null".equalsIgnoreCase(tier1) && !"".equalsIgnoreCase(tier1))
			{	
				for(String pId : tier1.split(","))
					plantList.add(pId);
				
				Log.Info("plantList List created based on Input has size " + plantList.size());
			}			
					
			materialflowList = retrieveMaterialFlowList(vendorList, materialList, plantList, customerList, typeList);			
			Log.Info("Material Flow List loaded from db, based on the input recieved has size ..." + materialflowList.size());
			
			material.setMaterialflowList(materialflowList);			
			Log.Info("Material Object populated with material flow list " );
			
		}catch(Exception e){
			Log.Error("Exception in getBusinessObject() " + e.getMessage());
			Log.getStackTrace(e);
			
		}
		
		return material;
	}

	/**
	 * @param vendorList
	 * @param materialList
	 * @param plantList
	 * @param customerList
	 * @param typeList
	 * @return
	 */
	public List<FlowData> retrieveMaterialFlowList(List<String> vendorList,
			List<String> materialList, List<String> plantList,
			List<String> customerList, List<String> typeList) {

		List<FlowData> materialflowDataList = new ArrayList<FlowData>();
		List<FlowData> flowDataListVendortoPlant = new ArrayList<FlowData>();
		List<FlowData> flowDataListPlanttoCustomer = new ArrayList<FlowData>();
		List<FlowData> flowDataListPlanttoPlant = new ArrayList<FlowData>();
		List<FlowData> flowDataListPlanttoCustomerIntercompany = new ArrayList<FlowData>();

		try {		  
			

			flowDataListVendortoPlant = retrieveVendorToPlantList(vendorList, materialList,plantList,typeList);	
			Log.Info("flowDataListVendortoPlant retrieved from db has size " + flowDataListVendortoPlant.size());

			flowDataListPlanttoPlant = retrievePlantToPlantList(plantList, materialList, customerList, typeList);	
			Log.Info("flowDataListPlanttoPlant retrieved from db has size " + flowDataListPlanttoPlant.size());
			
			flowDataListPlanttoCustomer = retrievePlantToCustomerList(plantList, materialList, customerList, typeList);
			Log.Info("flowDataListPlanttoCustomer retrieved from db has size " + flowDataListPlanttoCustomer.size());
			
			flowDataListPlanttoCustomerIntercompany = retrievePlantToCustomerListForIntercompanyDropship(plantList, materialList, customerList, typeList);
			Log.Info("flowDataListPlanttoCustomerIntercompany retrieved from db has size " + flowDataListPlanttoCustomerIntercompany.size());
			//flowDataListDcPlanttoCustomer = dcPlantToCustomer(plantIdList, stmt1);	

			materialflowDataList.addAll(flowDataListVendortoPlant);
			materialflowDataList.addAll(flowDataListPlanttoPlant);
			materialflowDataList.addAll(flowDataListPlanttoCustomer);
			materialflowDataList.addAll(flowDataListPlanttoCustomerIntercompany);

			Log.Info("materialflowDataList retrieved from db has size " + materialflowDataList.size());
		} catch (Exception e) {
			
			Log.Error("Excepton fetching Materialflow list based on the input params " + e.getStackTrace());
			
			e.printStackTrace();
		}finally{
			dbUtil.releaseConnection(con);
		}
		
		return materialflowDataList;
		
	}


	
	private List<FlowData> retrievePlantToCustomerListForIntercompanyDropship(
			List<String> plantIdList, List<String> materialIdList,
			List<String> customerIdList, List<String> typeList) {
		
		List<FlowData> plantCustomerFlowList = new ArrayList<FlowData>();
		
		try{
			
			if(con == null)
				con = dbUtil.getHiveConnection ();		
			
				if(stmt == null)			
					stmt = con.createStatement();
				
				
				StringBuilder query = new StringBuilder();
					
				query.append("select concat('CC-', bukrs_vf) as salesorgcc, kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) ");
					
				if (customerIdList.size() > 0)
					query.append("and kunnr in (" + dbUtil.getCSV(customerIdList) + ") ");
					
				if (materialIdList.size() > 0)
					query.append(" and vbap_matnr in (" +  dbUtil.getCSV(materialIdList) + ")");
				
				if (plantIdList.size() > 0)
					query.append("and werks in (" + dbUtil.getCSV(plantIdList) + ") ");
					
				if (typeList.size() > 0)
					query.append(" and so_type in (" +  dbUtil.getCSV(typeList) + ")");
				
				query.append(" and so_type = '" +  E2emfConstants.INTERCOMPANY_DROPSHIP + "'");
						 
				query.append(" group by bukrs_vf, kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 30");
				  
				//System.out.println("Fetchinging plant to customer...");
				//log.info("Fetchinging plant to customer for intercompany dropship...");
				//System.out.println(query.toString());
			//	log.info(query.toString());
				
				Log.Info("Executing Query  : " + query.toString());
				
				ResultSet rs1 = stmt.executeQuery(query.toString()); 

				//System.out.println("Fetched plant to customer");
				//log.info("Fetched plant to customer for intercompany dropship...");
				
				if(rs1 != null) {        	
					  while(rs1.next()){

				          FlowData flowData = new FlowData();

						  flowData.setFromCode(rs1.getString("plant"));
						  flowData.setFromName(rs1.getString("plantname"));
						  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
						  flowData.setFromType(E2emfConstants.MF_PLANT);

						  
						  flowData.setToCode(rs1.getString("salesorgcc"));
						  flowData.setToName(rs1.getString("salesorgcc"));
						  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
						  flowData.setToType(E2emfConstants.DC_PLANT);

						  flowData.setLinkType(rs1.getString("so_type"));
						  flowData.setLinkSubType(rs1.getString("mat_type"));
						  flowData.setLinkValue(rs1.getDouble("netvalue")*10);//Added factor since not all SO data is loaded

						  plantCustomerFlowList.add(flowData);
						  
						  
						  //Second Set
						  flowData = new FlowData();

						  flowData.setFromCode(rs1.getString("salesorgcc"));
						  flowData.setFromName(rs1.getString("salesorgcc"));
						  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
						  flowData.setFromType(E2emfConstants.DC_PLANT);

						  
						  flowData.setToCode(rs1.getString("customerid"));
						  flowData.setToName(rs1.getString("customername"));
						  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
						  flowData.setToType(E2emfConstants.CUSTOMER);

						  flowData.setLinkType(rs1.getString("so_type"));
						  flowData.setLinkSubType(rs1.getString("mat_type"));
						  flowData.setLinkValue(rs1.getDouble("netvalue")*10);//Added factor since not all SO data is loaded
						  
						  plantCustomerFlowList.add(flowData);
						  
				   }        	
				  } else{
					  System.out.println("Resultset is null");
				  }

				
			
			
		}catch(SQLException sql) {
			sql.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return plantCustomerFlowList;
	}


	private List<FlowData> retrievePlantToCustomerList(
			List<String> plantIdList, List<String> materialIdList,
			List<String> customerIdList, List<String> typeList) {
		
		List<FlowData> plantCustomerFlowList = new ArrayList<FlowData>();
		
		try{
			
			if(con == null)
			   con = dbUtil.getHiveConnection ();		
			
			if(stmt == null)			
				stmt = con.createStatement();
			
				StringBuilder query = new StringBuilder();
					
				query.append("select kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) ");
					
				if (customerIdList.size() > 0)
					query.append("and kunnr in (" + dbUtil.getCSV(customerIdList) + ") ");
					
				if (materialIdList.size() > 0)
					query.append(" and vbap_matnr in (" +  dbUtil.getCSV(materialIdList) + ")");
				
				if (plantIdList.size() > 0)
					query.append("and werks in (" + dbUtil.getCSV(plantIdList) + ") ");
					
				if (typeList.size() > 0)
					query.append(" and so_type in (" +  dbUtil.getCSV(typeList) + ")");
				
				query.append(" and so_type <> '" +  E2emfConstants.INTERCOMPANY_DROPSHIP + "'");
						 
				query.append(" group by kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 20");
				  
				//System.out.println("Fetchinging plant to customer...");
				//log.info("Fetchinging plant to customer...");
				//System.out.println(query.toString());
				//log.info(query.toString());
				Log.Info("Executing Query 3 : " + query.toString());
				ResultSet rs1 = stmt.executeQuery(query.toString()); 

				//System.out.println("Fetched plant to customer");
			//	log.info("Fetched plant to customer");
				
				if(rs1 != null) {        	
					  while(rs1.next()){

				          FlowData flowData = new FlowData();

						  flowData.setFromCode(rs1.getString("plant"));
						  flowData.setFromName(rs1.getString("plantname"));
						  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
						  flowData.setFromType(E2emfConstants.MF_PLANT);

						  flowData.setToCode(rs1.getString("customerid"));
						  flowData.setToName(rs1.getString("customername"));
						  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
						  flowData.setToType(E2emfConstants.CUSTOMER);

						  flowData.setLinkType(rs1.getString("so_type"));
						  flowData.setLinkSubType(rs1.getString("mat_type"));
						  flowData.setLinkValue(rs1.getDouble("netvalue")*10);//Added factor since not all SO data is loaded

						  plantCustomerFlowList.add(flowData);
				   }        	
				  } else{
					  System.out.println("Resultset is null");
				  }						
				
			
			
		}catch(SQLException sql) {
			sql.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return plantCustomerFlowList;
	}


	private List<FlowData> retrievePlantToPlantList(List<String> plantIdList,
			List<String> materialIdList, List<String> customerIdList,
			List<String> typeList) {
		
		List<FlowData> plantPlantFlowList = new ArrayList<FlowData>();
		
		try{
					
			if(con == null)
				con = dbUtil.getHiveConnection ();
			
				if(stmt == null)			
					stmt = con.createStatement();
			
			    StringBuilder query = new StringBuilder();
				
				query.append("select werks as plant, wname1 as plantname, concat('IP-',receiving_plant_id) as receivingplantid, receiving_plant_name as receivingplantname, so_type, mat_type, sum(sum_netwr) as netvalue from JANDJ.e2e_ag_sto_all where (1=1) ");
				
				/*if (customerIdList.size() > 0)
					query.append("and kunnr in (" + DBUtil.getCSV(customerIdList) + ") ");*/
					
				if (materialIdList.size() > 0)
					query.append(" and vbap_matnr in (" +  dbUtil.getCSV(materialIdList) + ")");
				
				if (plantIdList.size() > 0)
					query.append("and werks in (" + dbUtil.getCSV(plantIdList) + ") ");
					
				if (typeList.size() > 0)
					query.append(" and so_type in (" +  dbUtil.getCSV(typeList) + ")");
						 
				query.append(" group by werks, receiving_plant_id, receiving_plant_name, wname1, so_type, mat_type having sum(sum_netwr) <> 0.0");

			//	log.info("Fetchinging plant to plant...");
				
				//log.info(query.toString());
				Log.Info("Executing Query 2 : " + query.toString());
				
				ResultSet rs1 = stmt.executeQuery(query.toString()); 

				if(rs1 != null) {        	
					while(rs1.next()){

						FlowData flowData = new FlowData();

						flowData.setFromCode(rs1.getString("plant"));
						flowData.setFromName(rs1.getString("plantname"));
						flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
						flowData.setFromType(E2emfConstants.MF_PLANT);

						flowData.setToCode(rs1.getString("receivingplantid"));
						flowData.setToName(rs1.getString("receivingplantname"));
						flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
						//if(rs1.getString("receivingplantid").equals("2300")){
							flowData.setToType(E2emfConstants.DC_PLANT);
						//} else {
						//	flowData.setToType(MF_PLANT);
						//}

						flowData.setLinkType(rs1.getString("so_type"));
						flowData.setLinkValue(rs1.getDouble("netvalue"));
						flowData.setLinkSubType(rs1.getString("mat_type"));
						
						//System.out.println(flowData.getToCode()+":"+flowData.getToName()+":"+flowData.getToText()+":"+flowData.getToType());

						plantPlantFlowList.add(flowData);
					}        	
				} else{
					System.out.println("Resultset is null");
				}

				/*FlowData flowData = new FlowData();

				flowData.setFromCode("4000");
				flowData.setFromName("4000");
				flowData.setFromText("4000");
				flowData.setFromType("MF_PLANT");

				flowData.setToCode("DCPlant");
				flowData.setToName("DCPlant");
				flowData.setToText("DCPlant");
				flowData.setToType("DC_PLANT");

				flowData.setLinkType("OTHERS");
				flowData.setLinkValue(20000000d);
					

				flowDataList.add(flowData);	*/	
				
				/*flowData = new FlowData();

				flowData.setFromCode("6000");
				flowData.setFromName("6000");
				flowData.setFromText("6000");
				flowData.setFromType("MF_PLANT");

				flowData.setToCode("DCPlant");
				flowData.setToName("DCPlant");
				flowData.setToText("DCPlant");
				flowData.setToType("DC_PLANT");

				flowData.setLinkType("OTHERS");
				flowData.setLinkValue(20000000d);
				
				flowDataList.add(flowData);*/

			
			
		}catch(SQLException sql) {
			sql.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return plantPlantFlowList;
	}


	private List<FlowData> retrieveVendorToPlantList(List<String> vendorIdList,
			List<String> materialIdList, List<String> plantIdList,
			List<String> typeList) {
		List<FlowData> flowDataList = new ArrayList<FlowData>();
		
		
	try{
		if(con != null)
				con = dbUtil.getHiveConnection ();		
		
			if(stmt == null)			
				stmt = con.createStatement();
			
			Statement stmt1 = stmt;
			
			StringBuilder query = new StringBuilder();
			
			query.append("select lifnr as vendorid, lname1 as vendorname, werks as plant, wname1 as plantname, po_type, sum(netwr) as netvalue, vendor_type, material_type from JANDJ.E2E_DN_AG_PROF where (1=1) ");
				
			if (vendorIdList.size() > 0)
				query.append("and lifnr in (" + dbUtil.getCSV(vendorIdList) + ") ");
				
			if (materialIdList.size() > 0)
				query.append(" and matnr in (" +  dbUtil.getCSV(materialIdList) + ")");
			
			if (plantIdList.size() > 0)
				query.append("and werks in (" + dbUtil.getCSV(plantIdList) + ") ");
				
			if (typeList.size() > 0)
				query.append(" and po_type in (" +  dbUtil.getCSV(typeList) + ")");
				
				
			query.append(" group by lifnr,lname1,werks,wname1,PO_type, vendor_type,material_type order by sum(netwr) desc limit 20");			
			
			
			//System.out.println("Fetchinging vendor to plant...");
		//	log.info("Fetchinging vendor to plant...");
			
			//System.out.println(query.toString());
			//log.info(query.toString());
			
			Log.Info("Executing Query 1 : " + query.toString());
			ResultSet rs1 = stmt1.executeQuery(query.toString()); 

			//System.out.println("Fetched vendor to plant");
		//	log.info("Fetched vendor to plant");

			if(rs1 != null) {        	
				while(rs1.next()){

					FlowData flowData = new FlowData();

					flowData.setFromCode(rs1.getString("vendorid"));
					flowData.setFromName(rs1.getString("vendorname"));
					flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
					// make changes to identify the External vendors v/s J&J affiliate vendors
					//flowData.setFromType("VENDOR");
					if(rs1.getString("vendor_type").equalsIgnoreCase(E2emfConstants.EXTERNAL_VENDOR)){
						flowData.setFromType(E2emfConstants.EXTERNAL_VENDOR);
					} else if(rs1.getString("vendor_type").equalsIgnoreCase(E2emfConstants.INTERNAL_VENDOR)){
						flowData.setFromType(E2emfConstants.INTERNAL_VENDOR);
					}

					flowData.setToCode(rs1.getString("plant"));
					flowData.setToName(rs1.getString("plantname"));
					flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
					flowData.setToType(E2emfConstants.MF_PLANT);

					flowData.setLinkType(rs1.getString("po_type"));
					flowData.setLinkSubType(rs1.getString("material_type"));
					flowData.setLinkValue(rs1.getDouble("netvalue"));

					flowDataList.add(flowData);
				}        	
			} else{
				System.out.println("Resultset is null");
			}
			
		
			
	}catch(SQLException sql) {
		sql.printStackTrace();
	}catch(Exception e){
		e.printStackTrace();
	}
		
		return flowDataList;
	}


	@Override
	public String executeQuery(String nodeLevelQuery) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see com.relevance.e2emf.dao.E2emfDao#getFlowDataList(java.lang.Object[])
	 */
	@Override
	public List<FlowData> getFlowDataList(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
