package com.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.data.FlowData;
import com.service.LandingPgService;
import com.util.Configurator;
import com.util.DBUtil;
import com.util.E2EMFConstants;

public class MaterialFlowDAO extends E2EMFConstants {

	private static Logger log = LogManager.getLogger(MaterialFlowDAO.class);
	private Configurator props = null;
	private String JDBC_DRIVER = null;
	private String DB_URL = null;
	Connection conn = null;

	public MaterialFlowDAO() throws IOException{
		props = Configurator.getInstance();
		JDBC_DRIVER = props.getConnectionDriver();
		DB_URL = props.getConnectionURL();		
	} 

	public List<FlowData> retrieveFlowDataList(List<String> vendorIdList, List<String> materialIdList, List<String> plantIdList,List<String> customerIdList, List<String> typeList) throws SQLException {

		Statement stmt1 = null;
		List<FlowData> flowDataList = new ArrayList<FlowData>();
		List<FlowData> flowDataListVendortoPlant = new ArrayList<FlowData>();
		List<FlowData> flowDataListPlanttoCustomer = new ArrayList<FlowData>();
		List<FlowData> flowDataListPlanttoPlant = new ArrayList<FlowData>();
		List<FlowData> flowDataListPlanttoCustomerIntercompany = new ArrayList<FlowData>();

		try {		  
			conn = getConnection();
			stmt1 = conn.createStatement();

			flowDataListVendortoPlant = retrieveVendorToPlantList(vendorIdList, materialIdList,plantIdList,typeList, stmt1);	

			flowDataListPlanttoPlant = retrievePlantToPlantList(plantIdList, materialIdList, customerIdList, typeList, stmt1);	

			flowDataListPlanttoCustomer = retrievePlantToCustomerList(plantIdList, materialIdList, customerIdList, typeList, stmt1);
			
			flowDataListPlanttoCustomerIntercompany = retrievePlantToCustomerListForIntercompanyDropship(plantIdList, materialIdList, customerIdList, typeList, stmt1);

			//flowDataListDcPlanttoCustomer = dcPlantToCustomer(plantIdList, stmt1);	

			flowDataList.addAll(flowDataListVendortoPlant);
			flowDataList.addAll(flowDataListPlanttoPlant);
			flowDataList.addAll(flowDataListPlanttoCustomer);
			flowDataList.addAll(flowDataListPlanttoCustomerIntercompany);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			releaseConncection(conn);
		}
		return flowDataList;

	}

	private List<FlowData> retrieveVendorToPlantList(List<String> vendorIdList,  List<String> materialIdList, List<String> plantIdList, List<String> typeList, Statement stmt1) throws SQLException 
	{

		List<FlowData> flowDataList = new ArrayList<FlowData>();
		StringBuilder query = new StringBuilder();
			
		query.append("select lifnr as vendorid, lname1 as vendorname, werks as plant, wname1 as plantname, po_type, sum(netwr) as netvalue, vendor_type, material_type from JANDJ.E2E_DN_AG_PROF where (1=1) ");
			
		if (vendorIdList.size() > 0)
			query.append("and lifnr in (" + DBUtil.getCSV(vendorIdList) + ") ");
			
		if (materialIdList.size() > 0)
			query.append(" and matnr in (" +  DBUtil.getCSV(materialIdList) + ")");
		
		if (plantIdList.size() > 0)
			query.append("and werks in (" + DBUtil.getCSV(plantIdList) + ") ");
			
		if (typeList.size() > 0)
			query.append(" and po_type in (" +  DBUtil.getCSV(typeList) + ")");
			
			
		query.append(" group by lifnr,lname1,werks,wname1,PO_type, vendor_type,material_type order by sum(netwr) desc limit 20");			
		
		
		//System.out.println("Fetchinging vendor to plant...");
		log.info("Fetchinging vendor to plant...");
		
		//System.out.println(query.toString());
		log.info(query.toString());
		ResultSet rs1 = stmt1.executeQuery(query.toString()); 

		//System.out.println("Fetched vendor to plant");
		log.info("Fetched vendor to plant");

		if(rs1 != null) {        	
			while(rs1.next()){

				FlowData flowData = new FlowData();

				flowData.setFromCode(rs1.getString("vendorid"));
				flowData.setFromName(rs1.getString("vendorname"));
				flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				// make changes to identify the External vendors v/s J&J affiliate vendors
				//flowData.setFromType("VENDOR");
				if(rs1.getString("vendor_type").equalsIgnoreCase(EXTERNAL_VENDOR)){
					flowData.setFromType(EXTERNAL_VENDOR);
				} else if(rs1.getString("vendor_type").equalsIgnoreCase(INTERNAL_VENDOR)){
					flowData.setFromType(INTERNAL_VENDOR);
				}

				flowData.setToCode(rs1.getString("plant"));
				flowData.setToName(rs1.getString("plantname"));
				flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				flowData.setToType(MF_PLANT);

				flowData.setLinkType(rs1.getString("po_type"));
				flowData.setLinkSubType(rs1.getString("material_type"));
				flowData.setLinkValue(rs1.getDouble("netvalue"));

				flowDataList.add(flowData);
			}        	
		} else{
			System.out.println("Resultset is null");
		}

		return flowDataList;
	}


	

	
	private List<FlowData> retrievePlantToCustomerList(List<String> plantIdList, List<String> materialIdList, List<String> customerIdList, List<String> typeList, Statement stmt1) throws SQLException 
	{

		List<FlowData> flowDataList = new ArrayList<FlowData>();
		StringBuilder query = new StringBuilder();
			
		query.append("select kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) ");
			
		if (customerIdList.size() > 0)
			query.append("and kunnr in (" + DBUtil.getCSV(customerIdList) + ") ");
			
		if (materialIdList.size() > 0)
			query.append(" and vbap_matnr in (" +  DBUtil.getCSV(materialIdList) + ")");
		
		if (plantIdList.size() > 0)
			query.append("and werks in (" + DBUtil.getCSV(plantIdList) + ") ");
			
		if (typeList.size() > 0)
			query.append(" and so_type in (" +  DBUtil.getCSV(typeList) + ")");
		
		query.append(" and so_type <> '" +  E2EMFConstants.INTERCOMPANY_DROPSHIP + "'");
				 
		query.append(" group by kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 20");
		  
		//System.out.println("Fetchinging plant to customer...");
		log.info("Fetchinging plant to customer...");
		//System.out.println(query.toString());
		log.info(query.toString());
		ResultSet rs1 = stmt1.executeQuery(query.toString()); 

		//System.out.println("Fetched plant to customer");
		log.info("Fetched plant to customer");
		
		if(rs1 != null) {        	
			  while(rs1.next()){

		          FlowData flowData = new FlowData();

				  flowData.setFromCode(rs1.getString("plant"));
				  flowData.setFromName(rs1.getString("plantname"));
				  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				  flowData.setFromType(MF_PLANT);

				  flowData.setToCode(rs1.getString("customerid"));
				  flowData.setToName(rs1.getString("customername"));
				  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				  flowData.setToType(CUSTOMER);

				  flowData.setLinkType(rs1.getString("so_type"));
				  flowData.setLinkSubType(rs1.getString("mat_type"));
				  flowData.setLinkValue(rs1.getDouble("netvalue")*10);//Added factor since not all SO data is loaded

				  flowDataList.add(flowData);
		   }        	
		  } else{
			  System.out.println("Resultset is null");
		  }

		return flowDataList;
	}
	
	
	private List<FlowData> retrievePlantToCustomerListForIntercompanyDropship(List<String> plantIdList, List<String> materialIdList, List<String> customerIdList, List<String> typeList, Statement stmt1) throws SQLException 
	{

		List<FlowData> flowDataList = new ArrayList<FlowData>();
		StringBuilder query = new StringBuilder();
			
		query.append("select concat('CC-', bukrs_vf) as salesorgcc, kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) ");
			
		if (customerIdList.size() > 0)
			query.append("and kunnr in (" + DBUtil.getCSV(customerIdList) + ") ");
			
		if (materialIdList.size() > 0)
			query.append(" and vbap_matnr in (" +  DBUtil.getCSV(materialIdList) + ")");
		
		if (plantIdList.size() > 0)
			query.append("and werks in (" + DBUtil.getCSV(plantIdList) + ") ");
			
		if (typeList.size() > 0)
			query.append(" and so_type in (" +  DBUtil.getCSV(typeList) + ")");
		
		query.append(" and so_type = '" +  E2EMFConstants.INTERCOMPANY_DROPSHIP + "'");
				 
		query.append(" group by bukrs_vf, kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 30");
		  
		//System.out.println("Fetchinging plant to customer...");
		log.info("Fetchinging plant to customer for intercompany dropship...");
		//System.out.println(query.toString());
		log.info(query.toString());
		ResultSet rs1 = stmt1.executeQuery(query.toString()); 

		//System.out.println("Fetched plant to customer");
		log.info("Fetched plant to customer for intercompany dropship...");
		
		if(rs1 != null) {        	
			  while(rs1.next()){

		          FlowData flowData = new FlowData();

				  flowData.setFromCode(rs1.getString("plant"));
				  flowData.setFromName(rs1.getString("plantname"));
				  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				  flowData.setFromType(MF_PLANT);

				  
				  flowData.setToCode(rs1.getString("salesorgcc"));
				  flowData.setToName(rs1.getString("salesorgcc"));
				  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				  flowData.setToType(DC_PLANT);

				  flowData.setLinkType(rs1.getString("so_type"));
				  flowData.setLinkSubType(rs1.getString("mat_type"));
				  flowData.setLinkValue(rs1.getDouble("netvalue")*10);//Added factor since not all SO data is loaded

				  flowDataList.add(flowData);
				  
				  
				  //Second Set
				  flowData = new FlowData();

				  flowData.setFromCode(rs1.getString("salesorgcc"));
				  flowData.setFromName(rs1.getString("salesorgcc"));
				  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				  flowData.setFromType(DC_PLANT);

				  
				  flowData.setToCode(rs1.getString("customerid"));
				  flowData.setToName(rs1.getString("customername"));
				  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				  flowData.setToType(CUSTOMER);

				  flowData.setLinkType(rs1.getString("so_type"));
				  flowData.setLinkSubType(rs1.getString("mat_type"));
				  flowData.setLinkValue(rs1.getDouble("netvalue")*10);//Added factor since not all SO data is loaded
				  
				  flowDataList.add(flowData);
				  
		   }        	
		  } else{
			  System.out.println("Resultset is null");
		  }

		return flowDataList;
	}
	
	private List<FlowData> retrievePlantToPlantList(List<String> plantIdList, List<String> materialIdList, List<String> customerIdList, List<String> typeList, Statement stmt1) throws SQLException 
	{

		List<FlowData> flowDataList = new ArrayList<FlowData>();
		StringBuilder query = new StringBuilder();
		
		query.append("select werks as plant, wname1 as plantname, concat('IP-',receiving_plant_id) as receivingplantid, receiving_plant_name as receivingplantname, so_type, mat_type, sum(sum_netwr) as netvalue from JANDJ.e2e_ag_sto_all where (1=1) ");
		
		/*if (customerIdList.size() > 0)
			query.append("and kunnr in (" + DBUtil.getCSV(customerIdList) + ") ");*/
			
		if (materialIdList.size() > 0)
			query.append(" and vbap_matnr in (" +  DBUtil.getCSV(materialIdList) + ")");
		
		if (plantIdList.size() > 0)
			query.append("and werks in (" + DBUtil.getCSV(plantIdList) + ") ");
			
		if (typeList.size() > 0)
			query.append(" and so_type in (" +  DBUtil.getCSV(typeList) + ")");
				 
		query.append(" group by werks, receiving_plant_id, receiving_plant_name, wname1, so_type, mat_type having sum(sum_netwr) <> 0.0 ");

		log.info("Fetchinging plant to plant...");
		
		log.info(query.toString());
		
		ResultSet rs1 = stmt1.executeQuery(query.toString()); 

		if(rs1 != null) {        	
			while(rs1.next()){

				FlowData flowData = new FlowData();

				flowData.setFromCode(rs1.getString("plant"));
				flowData.setFromName(rs1.getString("plantname"));
				flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				flowData.setFromType(MF_PLANT);

				flowData.setToCode(rs1.getString("receivingplantid"));
				flowData.setToName(rs1.getString("receivingplantname"));
				flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				//if(rs1.getString("receivingplantid").equals("2300")){
					flowData.setToType(DC_PLANT);
				//} else {
				//	flowData.setToType(MF_PLANT);
				//}

				flowData.setLinkType(rs1.getString("so_type"));
				flowData.setLinkValue(rs1.getDouble("netvalue"));
				flowData.setLinkSubType(rs1.getString("mat_type"));
				
				//System.out.println(flowData.getToCode()+":"+flowData.getToName()+":"+flowData.getToText()+":"+flowData.getToType());

				flowDataList.add(flowData);
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

		return flowDataList;
	}


	//private List<FlowData> dcPlantToCustomer(List<String> plantIdList, Statement stmt1) throws SQLException 
		//{

			//List<FlowData> flowDataList = new ArrayList<FlowData>();
			/*	  StringBuilder plantIds = new StringBuilder();
			  if (plantIdList != null)
			  {
				  boolean isFirstRec = true;
				  for (String pId : plantIdList)
				  {
					  if (isFirstRec)
						  isFirstRec = false;
					  else
						  plantIds.append(",");
					  plantIds.append("'" + pId + "'");
				  }
			  }

			  String query1 = "select lifnr as VendorId, werks as Plant, PO_type, sum(menge) as NetValue from JANDJ.E2E_AG_PO_PROF where lifnr in"
					  + " (" + plantIds + ") "
					  + " group by lifnr , werks, PO_type";
			  ResultSet rs1 = stmt1.executeQuery(query1); 


			  if(rs1 != null) {        	
				  while(rs1.next()){*/

			/*FlowData flowData = new FlowData();

					  flowData.setFromCode(rs1.getString("vendorid"));
					  flowData.setFromName(rs1.getString("vendorid"));
					  flowData.setFromText(rs1.getString("vendorid"));
					  flowData.setFromType("VENDOR");

					  flowData.setToCode(rs1.getString("plant"));
					  flowData.setToName(rs1.getString("plant"));
					  flowData.setToText(rs1.getString("plant"));
					  flowData.setToType("MF_PLANT");

					  flowData.setLinkType(rs1.getString("po_type"));
					  flowData.setLinkValue(rs1.getDouble("netvalue"));

					  flowDataList.add(flowData);*/
			/*  }        	
			  } else{
				  System.out.println("Resultset is null");
			  }*/

			/*FlowData flowData = new FlowData();

			flowData.setFromCode("DCPLANT");
			flowData.setFromName("DCPLANT");
			flowData.setFromText("DCPLANT");
			flowData.setFromType("DC_PLANT");

			flowData.setToCode("customer");
			flowData.setToName("customer");
			flowData.setToText("customer");
			flowData.setToType("CUSTOMER");

			flowData.setLinkType("INTERCOMPANY/DROPSHIP");
			flowData.setLinkValue(30000000d);

			flowDataList.add(flowData);		

			return flowDataList;
		}*/
	
	
	public List<FlowData> retrieveFlowDataListDefaultView(){
		
		List<FlowData> flowDataList = new ArrayList<FlowData>();
		List<FlowData> flowDataListVendortoPlant = new ArrayList<FlowData>();
		List<FlowData> flowDataListPlanttoCustomer = new ArrayList<FlowData>();
		List<FlowData> flowDataListPlanttoCustomerInterCompany = new ArrayList<FlowData>();
		List<FlowData> flowDataListPlanttoPlant = new ArrayList<FlowData>();
		
		flowDataListVendortoPlant = getDefaultVendorsToPlantList();
		flowDataListPlanttoCustomer = getDefaultPlantToCustomersList();
		flowDataListPlanttoCustomerInterCompany = getDefaultIntercompanyDropShipList();
		flowDataListPlanttoPlant = getDefaultPlantToPlant();
		
		flowDataList.addAll(flowDataListVendortoPlant);
		flowDataList.addAll(flowDataListPlanttoCustomer);
		flowDataList.addAll(flowDataListPlanttoCustomerInterCompany);
		flowDataList.addAll(flowDataListPlanttoPlant);
		return flowDataList;
		
	}
	
	public List<FlowData> getDefaultVendorsToPlantList()
	{

		Statement stmt2 = null;
		
		ResultSet rs2;
		
		List<FlowData> flowDataList =  new ArrayList<FlowData>();		

		//String query = "select sum(netwr), lifnr from JANDJ.E2E_DN_AG_PROF where vendor_type='"+EXTERNAL_VENDOR+"'"+" group by lifnr order by sum(netwr) desc limit 5";
		//String query1 = "select sum(netwr), lifnr from JANDJ.E2E_DN_AG_PROF where vendor_type='"+INTERNAL_VENDOR+"'"+" group by lifnr order by sum(netwr) desc limit 5";
		 String query = "select lifnr as vendorid, lname1 as vendorname, werks as plant, wname1 as plantname, po_type, sum(netwr) as netvalue, vendor_type, material_type from JANDJ.E2E_DN_AG_PROF where (1=1) and po_type = 'NORMAL PURCHASING' group by lifnr,lname1,werks,wname1,PO_type, vendor_type,material_type order by sum(netwr) desc limit 5 ";
		 StringBuilder completeQuery = new StringBuilder(query);
		 completeQuery.append("union all select lifnr as vendorid, lname1 as vendorname, werks as plant, wname1 as plantname, po_type, sum(netwr) as netvalue, vendor_type, material_type from JANDJ.E2E_DN_AG_PROF where (1=1) and po_type = 'DROP SHIP' group by lifnr,lname1,werks,wname1,PO_type, vendor_type,material_type order by sum(netwr) desc limit 5 ");
		 completeQuery.append("union all select lifnr as vendorid, lname1 as vendorname, werks as plant, wname1 as plantname, po_type, sum(netwr) as netvalue, vendor_type, material_type from JANDJ.E2E_DN_AG_PROF where (1=1) and po_type = 'EXTERNAL PROCESSING' group by lifnr,lname1,werks,wname1,PO_type, vendor_type,material_type order by sum(netwr) desc limit 5 ");
		 completeQuery.append("union all select lifnr as vendorid, lname1 as vendorname, werks as plant, wname1 as plantname, po_type, sum(netwr) as netvalue, vendor_type, material_type from JANDJ.E2E_DN_AG_PROF where (1=1) and po_type = 'SUB CONTRACTING' group by lifnr,lname1,werks,wname1,PO_type, vendor_type,material_type order by sum(netwr) desc limit 5");
		 

		conn = getConnection();
		try {
			stmt2 = conn.createStatement();
			rs2 = stmt2.executeQuery(completeQuery.toString());		
			while(rs2.next()){
				//vendorList.add(rs2.getString("vendorid"));
				
				FlowData flowData = new FlowData();

				flowData.setFromCode(rs2.getString("vendorid"));
				flowData.setFromName(rs2.getString("vendorname"));
				flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				// make changes to identify the External vendors v/s J&J affiliate vendors
				//flowData.setFromType("VENDOR");
				if(rs2.getString("vendor_type").equalsIgnoreCase(EXTERNAL_VENDOR)){
					flowData.setFromType(EXTERNAL_VENDOR);
				} else if(rs2.getString("vendor_type").equalsIgnoreCase(INTERNAL_VENDOR)){
					flowData.setFromType(INTERNAL_VENDOR);
				}

				flowData.setToCode(rs2.getString("plant"));
				flowData.setToName(rs2.getString("plantname"));
				flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				flowData.setToType(MF_PLANT);

				flowData.setLinkType(rs2.getString("po_type"));
				flowData.setLinkSubType(rs2.getString("material_type"));
				flowData.setLinkValue(rs2.getDouble("netvalue"));

				flowDataList.add(flowData);				
			}
			
						
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			releaseConncection(conn);
		}
		return flowDataList;

	}
	
	public List<FlowData> getDefaultPlantToCustomersList()
	{

		List<FlowData> flowDataList =  new ArrayList<FlowData>();
		Statement stmt2 = null;		
		ResultSet rs2;
		
		//List<String> customerList =  new ArrayList<String>();
		//String query = "select kunnr, sum(sum_netwr) as netwr from JANDJ.e2e_ag_soprof_names group by kunnr order by 2 desc limit 10";
		
		 String query = "select kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) and so_type='DIRECT SALES' group by kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 5 ";
		 StringBuilder completeQuery = new StringBuilder(query);
		 //completeQuery.append("union all select kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) and so_type='INTERCOMPANY DROPSHIP' group by kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 5 ");
		 completeQuery.append("union all select kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) and so_type='CONSIGNMENT' group by kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 5 ");
		 
		
		conn = getConnection();
		try {
			stmt2 = conn.createStatement();
			rs2 = stmt2.executeQuery(completeQuery.toString());		
			while(rs2.next()){
				 FlowData flowData = new FlowData();

				  flowData.setFromCode(rs2.getString("plant"));
				  flowData.setFromName(rs2.getString("plantname"));
				  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				  flowData.setFromType(MF_PLANT);

				  flowData.setToCode(rs2.getString("customerid"));
				  flowData.setToName(rs2.getString("customername"));
				  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				  flowData.setToType(CUSTOMER);

				  flowData.setLinkType(rs2.getString("so_type"));
				  flowData.setLinkSubType(rs2.getString("mat_type"));
				  flowData.setLinkValue(rs2.getDouble("netvalue")*10);//Added factor since not all SO data is loaded

				  flowDataList.add(flowData);
			}				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			releaseConncection(conn);
		}
		return flowDataList;

	}
	
	public List<FlowData> getDefaultIntercompanyDropShipList(){
		
		List<FlowData> flowDataList =  new ArrayList<FlowData>();
		Statement stmt2 = null;		
		ResultSet rs2;
		
		//List<String> customerList =  new ArrayList<String>();
		//String query = "select kunnr, sum(sum_netwr) as netwr from JANDJ.e2e_ag_soprof_names group by kunnr order by 2 desc limit 10";
		
		 //String query = "select 'dummy' as salesorgcc,kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) and so_type='DIRECT SALES' group by kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 5 ";
		 //StringBuilder completeQuery = new StringBuilder(query);
		 String query = "select concat('CC-', bukrs_vf) as salesorgcc,kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) and so_type='INTERCOMPANY DROPSHIP' group by bukrs_vf,kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 5";
		 //completeQuery.append("union all select concat('CC-', bukrs_vf) as salesorgcc,kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) and so_type='INTERCOMPANY DROPSHIP' group by bukrs_vf,kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 5 ");
		 //completeQuery.append("union all select 'dummy' as salesorgcc,kunnr as customerid, sum(sum_netwr) as netvalue, werks as plant, wname1 as plantname, so_type, mat_type, kname1 as customername from JANDJ.e2e_ag_soprof_names where (1=1) and so_type='CONSIGNMENT' group by kunnr, werks, wname1, so_type, mat_type, kname1 order by sum(sum_netwr) desc limit 5 ");
		 
		
		conn = getConnection();
		try {
			stmt2 = conn.createStatement();
			rs2 = stmt2.executeQuery(query);		
			while(rs2.next()){
				FlowData flowData = new FlowData();

				  flowData.setFromCode(rs2.getString("plant"));
				  flowData.setFromName(rs2.getString("plantname"));
				  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				  flowData.setFromType(MF_PLANT);

				  
				  flowData.setToCode(rs2.getString("salesorgcc"));
				  flowData.setToName(rs2.getString("salesorgcc"));
				  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				  flowData.setToType(DC_PLANT);

				  flowData.setLinkType(rs2.getString("so_type"));
				  flowData.setLinkSubType(rs2.getString("mat_type"));
				  flowData.setLinkValue(rs2.getDouble("netvalue")*10);//Added factor since not all SO data is loaded

				  flowDataList.add(flowData);
				  
				  
				  //Second Set
				  flowData = new FlowData();

				  flowData.setFromCode(rs2.getString("salesorgcc"));
				  flowData.setFromName(rs2.getString("salesorgcc"));
				  flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				  flowData.setFromType(DC_PLANT);

				  
				  flowData.setToCode(rs2.getString("customerid"));
				  flowData.setToName(rs2.getString("customername"));
				  flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				  flowData.setToType(CUSTOMER);

				  flowData.setLinkType(rs2.getString("so_type"));
				  flowData.setLinkSubType(rs2.getString("mat_type"));
				  flowData.setLinkValue(rs2.getDouble("netvalue")*10);//Added factor since not all SO data is loaded
				  
				  flowDataList.add(flowData);

			}				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			releaseConncection(conn);
		}
		return flowDataList;
		
	}
	
	public List<FlowData> getDefaultPlantToPlant(){
		
		List<FlowData> flowDataList =  new ArrayList<FlowData>();
		Statement stmt2 = null;		
		ResultSet rs2;
		
		String query = "select werks as plant, wname1 as plantname, concat('IP-',receiving_plant_id) as receivingplantid, receiving_plant_name as receivingplantname, so_type, mat_type, sum(sum_netwr) as netvalue from JANDJ.e2e_ag_sto_all where (1=1)  group by werks, receiving_plant_id, receiving_plant_name, wname1, so_type, mat_type having sum(sum_netwr) <> 0.0 order by netvalue desc limit 10";
		conn = getConnection();
		try {
			stmt2 = conn.createStatement();
			rs2 = stmt2.executeQuery(query);
				
		if(rs2 != null) {        	
			while(rs2.next()){

				FlowData flowData = new FlowData();

				flowData.setFromCode(rs2.getString("plant"));
				flowData.setFromName(rs2.getString("plantname"));
				flowData.setFromText(flowData.getFromCode() + " - " + flowData.getFromName());
				flowData.setFromType(MF_PLANT);

				flowData.setToCode(rs2.getString("receivingplantid"));
				flowData.setToName(rs2.getString("receivingplantname"));
				flowData.setToText(flowData.getToCode() + " - " + flowData.getToName());
				//if(rs1.getString("receivingplantid").equals("2300")){
					flowData.setToType(DC_PLANT);
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			releaseConncection(conn);
		}
		return flowDataList;		
		
	}


	private Connection getConnection(){	  
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL,"","");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(SQLException se){
			se.printStackTrace();
		}	  

		return conn;

	}

	private void releaseConncection(Connection conn){

		if(conn!=null){
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
