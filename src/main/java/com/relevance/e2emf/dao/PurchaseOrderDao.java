/**
 * 
 */
package com.relevance.e2emf.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.relevance.e2emf.commons.E2emfConstants;
import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.commons.Log;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.domain.PurchaseOrder;
import com.relevance.e2emf.view.FlowData;

/**
 * @author emanuel
 *PurchaseORder Dao
 */


public class PurchaseOrderDao implements E2emfDao{
	
//	private static Logger log = LogManager.getLogger(PurchaseOrderDAO.class);
	
	

	String distinctCompanyCodeQuery = "select distinct concat('CC-',bukrs) as companycode from JANDJ.poproc22 order by concat('CC-',bukrs)  limit 100";

	String distinctPlantQuery = "select distinct werks from JANDJ.poproc22 where werks <> '' order by werks limit 100";
	String distinctPOGroupQuery = "select distinct ekorggrp from JANDJ.poproc22 where ekorggrp in ('1000-001','1000-031','7690-400','3350-300','1000-010','1000-012','1000-13','2000-001','4000-001','4000-208','4000-205')";
	String distinctDocTypeQuery = "select distinct bsart from JANDJ.poproc22 order by bsart limit 100";
	String distinctPayTermQuery = "select distinct zterm from JANDJ.poproc22 where zterm <> '' order by zterm limit 100";

	/*String companyPlantLinkQuery = "select bukrs, sum(netwr) as netwr, werks from JANDJ.poproc22 group by bukrs, werks";
	String plantPoGroupLinkQuery = "select werks, sum(netvalue) as netwr, ekorggrp from JANDJ.poproc22 group by werks, ekorggrp";
	String poGroupDocTypeLinkQuery = "select ekorggrp, sum(netvalue) as netwr, bsart from JANDJ.poproc22 group by ekorggrp, bsart";
	String docTypePayTermLinkQuery = "select  bsart, sum(netvalue) as netwr, zterm from JANDJ.poproc22 group by bsart,zterm";*/

	E2emfDbUtil dbUtil = null;
	static Connection con = null;
	static Statement stmt = null;

	//Logger log = Logger.getLogger("e2emf");

	public PurchaseOrderDao() {
		dbUtil = new E2emfDbUtil();
		dbUtil.initialize();
		con = dbUtil.getHiveConnection();

	}

	/*private Connection getConnection() {
		try {
			dbUtil = new E2emfDbUtil();
			dbUtil.initialize();
			con = dbUtil.getHiveConnection();
		}  catch (Exception e) {
			Log.Error("SQLException in getConnection" + e);	
			e.printStackTrace();
		}

		return conn;

	}*/

	public List<String> getCompanyCodeList() {
		List<String> companyCodeList = null;
		/*conn = getConnection();
		Statement stmt;*/
		try {
			if(con == null)
				   con = dbUtil.getHiveConnection();

				if (stmt == null)
						stmt = con.createStatement();
			
			ResultSet rs = stmt.executeQuery(distinctCompanyCodeQuery);
			companyCodeList = processResultSet(rs, E2emfConstants.COMPANYCODE_LIST);
		} catch (SQLException e) {
			Log.Error("SQLException in getCompanyCodeList" + e);	
			e.printStackTrace();
		}

		return companyCodeList;
	}

	public List<String> getPlantList() {
		List<String> plantList = null;
		
		try {
			if(con == null)
				   con = dbUtil.getHiveConnection();

				if (stmt == null)
						stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(distinctPlantQuery);
			plantList = processResultSet(rs, E2emfConstants.PLANT_LIST);
		} catch (SQLException e) {
			Log.Error("SQLException in getPlantList" + e);	
			e.printStackTrace();
		}

		return plantList;
	}

	public List<String> getPurchaseGroupList() {
		List<String> purchaseGroupList = null;
		
		try {
			if(con == null)
				   con = dbUtil.getHiveConnection();

				if (stmt == null)
						stmt = con.createStatement();
				
			ResultSet rs = stmt.executeQuery(distinctPOGroupQuery);
			purchaseGroupList = processResultSet(rs, E2emfConstants.PURCHASEORG_LIST);
		} catch (SQLException e) {
			Log.Error("SQLException in getPurchaseGroupList" + e);	
			e.printStackTrace();
		}

		return purchaseGroupList;
	}

	public List<String> getDocTypeList() {
		List<String> docTypeList = null;
		
		try {
			if(con == null)
				   con = dbUtil.getHiveConnection();

				if (stmt == null)
						stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(distinctDocTypeQuery);
			docTypeList = processResultSet(rs, E2emfConstants.DOCTYPE_LIST);
		} catch (SQLException e) {
			Log.Error("SQLException in getDocTypeList" + e);	
			e.printStackTrace();
		}

		return docTypeList;
	}

	public List<String> getPayTermList() {
		List<String> payTermList = null;
		
		try {
			if(con == null)
				   con = dbUtil.getHiveConnection();

				if (stmt == null)
						stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(distinctPayTermQuery);
			payTermList = processResultSet(rs, E2emfConstants.PAYTERM_LIST);
		} catch (SQLException e) {
			Log.Error("SQLException in getPayTermList" + e);	
			e.printStackTrace();
		}

		return payTermList;
	}

	private List<String> processResultSet(ResultSet rs, String descriminator) {
		
		Log.Info("Processing ResultSet for " + descriminator);

		List<String> resultList = new ArrayList<String>();

		if (rs != null) {

			try {
				if (descriminator.equalsIgnoreCase(E2emfConstants.COMPANYCODE_LIST)) {

					if (rs != null) {

						while (rs.next()) {
							String valueHolder = rs.getString("companycode");
							String[] splitString = valueHolder.split("-");							
							resultList.add(splitString[1]);

						}

					}

				} else if (descriminator.equalsIgnoreCase(E2emfConstants.PLANT_LIST)) {

					if (rs != null) {

						while (rs.next()) {
							String valueHolder = rs.getString("werks");
							resultList.add(valueHolder);

						}

					}

				} else if (descriminator.equalsIgnoreCase(E2emfConstants.PURCHASEORG_LIST)) {

					if (rs != null) {

						while (rs.next()) {
							String valueHolder = rs.getString("ekorggrp");
							resultList.add(valueHolder);

						}

					}

				} else if (descriminator.equalsIgnoreCase(E2emfConstants.DOCTYPE_LIST)) {

					if (rs != null) {

						while (rs.next()) {
							String valueHolder = rs.getString("bsart");
							resultList.add(valueHolder);

						}

					}

				} else if (descriminator.equalsIgnoreCase(E2emfConstants.PAYTERM_LIST)) {

					if (rs != null) {

						while (rs.next()) {
							String valueHolder = rs.getString("zterm");
							resultList.add(valueHolder);
						}
					}

				}

			} catch (Exception e) {
				Log.Error("Exception in processResultSet() " + e);
				e.printStackTrace();
			}
		}
		return resultList;
	}

	public List<FlowData> getPurchaseOrderFlowDataList(
			List<String> companyCodeList, List<String> plantList,
			List<String> purchaseGroupList, List<String> docTypeList,
			List<String> payTermList) {

		List<FlowData> companyPlantFlowList = null;
		List<FlowData> plantPurchaseFlowList = null;
		List<FlowData> purchaseDocumentFlowList = null;
		List<FlowData> documentPayTermsFlowList = null;
		List<FlowData> purchaseOrderFlowList = null;

		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setCompanyCodeList(companyCodeList);
		purchaseOrder.setPlantList(plantList);
		purchaseOrder.setPurchaseGroupList(purchaseGroupList);
		purchaseOrder.setDocTypeList(docTypeList);
		purchaseOrder.setPayTermList(payTermList);

		try {
			// companyPlantFlowView
			Map<String, String> companyCodePlantQueryMap = buildPurchaseOrderQueryString(
					purchaseOrder, E2emfConstants.COMPANYCODE_PLANT_QUERY);
			companyPlantFlowList = deriveCompanyPlantFlowList(companyCodePlantQueryMap
					.get(E2emfConstants.COMPANYCODE_PLANT_FLOWLIST));
		

			// plantPurchaseFlowView
			Map<String, String> plantPurchaseQueryMap = buildPurchaseOrderQueryString(
					purchaseOrder, E2emfConstants.PLANT_PURCHASEORG_QUERY);
			plantPurchaseFlowList = derivePlantPurchaseFlowList(plantPurchaseQueryMap
					.get(E2emfConstants.PLANT_PURCHASEORG_FLOWLIST));
		
			// purchaseDocTypeFlowView
			Map<String, String> purchaseDocTypeQueryMap = buildPurchaseOrderQueryString(
					purchaseOrder, E2emfConstants.PURCHASEORG_DOCTYPE_QUERY);
			purchaseDocumentFlowList = derivePurchaseDocumentFlowList(purchaseDocTypeQueryMap
					.get(E2emfConstants.PURCHASEORG_DOCTYPE_FLOWLIST));
		
			// docTypePayTermFlowView
			Map<String, String> documentPayTermQueryMap = buildPurchaseOrderQueryString(
					purchaseOrder, E2emfConstants.DOCTYPE_PAYTERM_QUERY);
			documentPayTermsFlowList = deriveDocumentPayTermsFlowList(documentPayTermQueryMap
					.get(E2emfConstants.DOCTYPE_PAYTERM_FLOWLIST));
		
			// Purchase Order Accumulated Flow View
			purchaseOrderFlowList = getPurchaseOrderFlowList(
					companyPlantFlowList, plantPurchaseFlowList,
					purchaseDocumentFlowList, documentPayTermsFlowList);

		} catch (Exception e) {
			Log.Error("Exception in getPurchaseOrderFlowDataList() " + e);
			e.printStackTrace();
		}

		return purchaseOrderFlowList;
	}

	private List<FlowData> getPurchaseOrderFlowList(
			List<FlowData> companyPlantFlowList,
			List<FlowData> plantPurchaseFlowList,
			List<FlowData> purchaseDocumentFlowList,
			List<FlowData> documentPayTermsFlowList) {

		List<FlowData> purchaseOrderFlowList = new ArrayList<FlowData>();

		purchaseOrderFlowList.addAll(companyPlantFlowList);
		purchaseOrderFlowList.addAll(plantPurchaseFlowList);
		purchaseOrderFlowList.addAll(purchaseDocumentFlowList);
		purchaseOrderFlowList.addAll(documentPayTermsFlowList);

		return purchaseOrderFlowList;
	}

	private List<FlowData> deriveDocumentPayTermsFlowList(
			String docTypePayTermQuery) {
		Log.Info("creating flowlist for Query: \n" + docTypePayTermQuery);
		List<FlowData> docTypePayTermFlowList = new ArrayList<FlowData>();
		ResultSet rs = null;
		FlowData flowData = null;

		try {
			if(con == null)
				   con = dbUtil.getHiveConnection();

				if (stmt == null)
						stmt = con.createStatement();

			rs = stmt.executeQuery(docTypePayTermQuery);

			if (rs != null) {

				while (rs.next()) {
					flowData = new FlowData();

					String bsart = rs.getString("bsart");
					String payTerm = rs.getString("zterm");
					String netValue = rs.getString("netvalue");

					//flowData.setFromType(E2EMFConstants.FROM_DOCTYPE);
					flowData.setFromType(E2emfConstants.FROM_DOCTYPE);
					flowData.setFromCode(bsart);
					flowData.setFromName(bsart);
					flowData.setFromText(bsart);

					
					//flowData.setToType(E2EMFConstants.TO_PAYTERM);
					flowData.setToType(E2emfConstants.FROM_PAYTERM);
					flowData.setToCode(payTerm);
					flowData.setToName(payTerm);
					flowData.setToText(payTerm);
					
					flowData.setLinkType(E2emfConstants.DOCTYPE_PAYTERM_LINKTYPE);				
					flowData.setLinkValue(Double.parseDouble(netValue));

					docTypePayTermFlowList.add(flowData);

				}
			}

		} catch (Exception e) {
			Log.Error("Exception in deriveDocumentPayTermsFlowList() " + e);
			e.printStackTrace();
		}

		return docTypePayTermFlowList;
	}

	private List<FlowData> derivePurchaseDocumentFlowList(
			String purchaseDocTypeQuery) {
		Log.Info("creating flowlist for Query: \n" + purchaseDocTypeQuery);
		List<FlowData> purchaseDocTypeFlowList = new ArrayList<FlowData>();
		ResultSet rs = null;
		FlowData flowData = null;

		try {
			if(con == null)
				   con = dbUtil.getHiveConnection();

				if (stmt == null)
						stmt = con.createStatement();
		
				rs = stmt.executeQuery(purchaseDocTypeQuery);

			if (rs != null) {

				while (rs.next()) {
					flowData = new FlowData();

					String purchaseGroup = rs.getString("ekorggrp");
					String docType = rs.getString("bsart");
					String netValue = rs.getString("netvalue");

					//flowData.setFromType(E2EMFConstants.FROM_PURCHASEORG);
					flowData.setFromType(E2emfConstants.FROM_PURCHASEORG);
					flowData.setFromCode(purchaseGroup);
					flowData.setFromName(purchaseGroup);
					flowData.setFromText(purchaseGroup);

					//flowData.setToType(E2EMFConstants.TO_DOCTYPE);
					flowData.setToType(E2emfConstants.FROM_DOCTYPE);
					flowData.setToCode(docType);
					flowData.setToName(docType);
					flowData.setToText(docType);
					
					flowData.setLinkType(E2emfConstants.PURCHASEORG_DOCTYPE_LINKTYPE);					
					flowData.setLinkValue(Double.parseDouble(netValue));
					
					purchaseDocTypeFlowList.add(flowData);

				}
			}

		} catch (Exception e) {
			Log.Error("Exception in derivePurchaseDocumentFlowList " + e);
			e.printStackTrace();
		}

		return purchaseDocTypeFlowList;
	}

	private List<FlowData> derivePlantPurchaseFlowList(String plantPurchaseQuery) {
		Log.Info("creating flowlist for Query: \n" + plantPurchaseQuery);
		List<FlowData> plantPurchaseFlowList = new ArrayList<FlowData>();
		ResultSet rs = null;
		FlowData flowData = null;

		try {
			if(con == null)
				   con = dbUtil.getHiveConnection();

				if (stmt == null)
						stmt = con.createStatement();

			rs = stmt.executeQuery(plantPurchaseQuery);

			if (rs != null) {

				while (rs.next()) {
					flowData = new FlowData();

					String plant = rs.getString("werks");
					String purchaseGroup = rs.getString("ekorggrp");
					String netValue = rs.getString("netvalue");

					//flowData.setFromType(E2EMFConstants.FROM_PLANT);
					flowData.setFromType(E2emfConstants.FROM_PLANT);
					flowData.setFromCode(plant);
					flowData.setFromName(plant);
					flowData.setFromText(plant);
					
					
					//flowData.setToType(E2EMFConstants.TO_PURCHASEORG);
					flowData.setToType(E2emfConstants.FROM_PURCHASEORG);
					flowData.setToCode(purchaseGroup);
					flowData.setToName(purchaseGroup);
					flowData.setToText(purchaseGroup);
					
					flowData.setLinkType(E2emfConstants.PLANT_PURCHASEORG_LINKTYPE);					
					flowData.setLinkValue(Double.parseDouble(netValue));

					plantPurchaseFlowList.add(flowData);

				}
			}

		} catch (Exception e) {
			Log.Error("Exception in derivePlantPurchaseFlowLis() " + e);
			e.printStackTrace();
		}

		return plantPurchaseFlowList;
	}

	private List<FlowData> deriveCompanyPlantFlowList(
			String companyCodePlantQuery) {
		Log.Info("companyCodePlantQuery:" +companyCodePlantQuery);
		
		List<FlowData> companyPlanflowList = new ArrayList<FlowData>();
		ResultSet rs = null;
		FlowData flowData = null;
		try {

			if(con == null)
				   con = dbUtil.getHiveConnection();

				if (stmt == null)
						stmt = con.createStatement();

			rs = stmt.executeQuery(companyCodePlantQuery);

			if (rs != null) {

				while (rs.next()) {
					flowData = new FlowData();

					String companyCode = rs.getString("companycode");
					String plant = rs.getString("werks");
					String netValue = rs.getString("netvalue");

					flowData.setFromCode(companyCode);
					flowData.setFromName(companyCode);
					flowData.setFromText(companyCode);
					flowData.setFromType(E2emfConstants.FROM_COMPANYCODE); 

					flowData.setToCode(plant);
					flowData.setToName(plant);
					flowData.setToText(plant);
					flowData.setToType(E2emfConstants.FROM_PLANT);

					flowData.setLinkType(E2emfConstants.COMPANYCODE_PLANT_LINKTYPE);					
					flowData.setLinkValue(Double.parseDouble(netValue));

					companyPlanflowList.add(flowData);

				}
			}

		} catch (Exception e) {
			Log.Error("Exception in deriveCompanyPlantFlowList()" + e);
			e.printStackTrace();
		}

		return companyPlanflowList;
	}

	private Map<String, String> buildPurchaseOrderQueryString(
			PurchaseOrder purchaseOrder, String criteria) {

		Log.Info("Building PurchaseOrderQuery for " + criteria);
		StringBuilder whereQuery = null;
		StringBuilder groupByQuery = null;

		Map<String, String> purchaseOrderQueryMap = new HashMap<String, String>();

		List<String> companyCodeList = purchaseOrder.getCompanyCodeList();
		List<String> plantList = purchaseOrder.getPlantList();
		List<String> purchaseOrderList = purchaseOrder.getPurchaseGroupList();
		List<String> docTypeList = purchaseOrder.getDocTypeList();
		List<String> payTermList = purchaseOrder.getPayTermList();

		String companyCodeInList = getCommaSeperatedInClauseValues(companyCodeList);
		String plantInList = getCommaSeperatedInClauseValues(plantList);
		String purchaseOrderInList = getCommaSeperatedInClauseValues(purchaseOrderList);
		String docTypeInList = getCommaSeperatedInClauseValues(docTypeList);
		String payTermInList = getCommaSeperatedInClauseValues(payTermList);
		try{

		if (criteria.equalsIgnoreCase(E2emfConstants.COMPANYCODE_PLANT_QUERY)) {
			
			StringBuilder companyPlantAggregatedQuery = new StringBuilder(
					"select concat('CC-',bukrs) as companycode, sum(netvalue) as netvalue, werks from JANDJ.poproc22 ");

			whereQuery = new StringBuilder(" where ");
			groupByQuery = new StringBuilder(" group by ");

			if (companyCodeList.size() > 0) {

				whereQuery.append(" bukrs in(").append(companyCodeInList)
						.append(")");
				groupByQuery.append(" bukrs ");
			}

			if (plantList.size() > 0) {

				whereQuery.append(" and werks in(").append(plantInList)
						.append(")");
				if (groupByQuery.toString().contains("bukrs"))
					groupByQuery.append(", werks ");
				else
					groupByQuery.append(" werks ");
			}

			companyPlantAggregatedQuery.append(whereQuery.toString()).append(
					groupByQuery.toString());

			purchaseOrderQueryMap.put(E2emfConstants.COMPANYCODE_PLANT_QUERY,
					companyPlantAggregatedQuery.toString());

		}

		// plantPurchase Query String
		if (criteria.equalsIgnoreCase(E2emfConstants.PLANT_PURCHASEORG_QUERY)) {

			StringBuilder plantPurchaseGroupSelectQuery = new StringBuilder(
					"select werks, sum(netvalue) as netvalue, ekorggrp from JANDJ.poproc22 ");
			whereQuery = new StringBuilder(" where ");
			groupByQuery = new StringBuilder(" group by ");

			if (plantList.size() > 0) {
				whereQuery.append(" werks in(").append(plantInList).append(")");
				groupByQuery.append(" werks ");
			}

			if (purchaseOrderList.size() > 0) {
				whereQuery.append(" and ekorggrp in(")
						.append(purchaseOrderInList).append(")");

				if (groupByQuery.toString().contains("werks"))
					groupByQuery.append(", ekorggrp ");
				else
					groupByQuery.append(" ekorggrp ");
			}

			plantPurchaseGroupSelectQuery.append(whereQuery.toString()).append(
					groupByQuery.toString());
			purchaseOrderQueryMap.put(E2emfConstants.PLANT_PURCHASEORG_QUERY,
					plantPurchaseGroupSelectQuery.toString());

		}

		// purchaseDoc Query String
		if (criteria.equalsIgnoreCase(E2emfConstants.PURCHASEORG_DOCTYPE_QUERY)) {

			StringBuilder purchaseDocTypeSelectQuery = new StringBuilder(
					"select ekorggrp, sum(netvalue) as netvalue, bsart from JANDJ.poproc22 ");
			whereQuery = new StringBuilder(" where ");
			groupByQuery = new StringBuilder(" group by ");

			if (purchaseOrderList.size() > 0) {
				whereQuery.append(" ekorggrp in(").append(purchaseOrderInList)
						.append(")");
				groupByQuery.append(" ekorggrp ");
			}

			if (docTypeList.size() > 0) {
				whereQuery.append(" and bsart in(").append(docTypeInList)
						.append(")");

				if (groupByQuery.toString().contains("ekorggrp"))
					groupByQuery.append(", bsart ");
				else
					groupByQuery.append(" bsart ");
			}

			purchaseDocTypeSelectQuery.append(whereQuery.toString()).append(
					groupByQuery.toString());
			purchaseOrderQueryMap.put(E2emfConstants.PURCHASEORG_DOCTYPE_QUERY,
					purchaseDocTypeSelectQuery.toString());

		}

		// docTypePayTerm Query String
		if (criteria.equalsIgnoreCase(E2emfConstants.DOCTYPE_PAYTERM_QUERY)) {

			StringBuilder docTypePayTrimSelectQuery = new StringBuilder(
					"select  bsart, sum(netvalue) as netvalue, zterm from JANDJ.poproc22 ");
			whereQuery = new StringBuilder(" where ");
			groupByQuery = new StringBuilder(" group by ");

			if (docTypeList.size() > 0) {
				whereQuery.append(" bsart in(").append(docTypeInList)
						.append(")");
				groupByQuery.append(" bsart ");
			}

			if (payTermList.size() > 0) {
				whereQuery.append(" and zterm in(").append(payTermInList)
						.append(")");

				if (groupByQuery.toString().contains("bsart"))
					groupByQuery.append(", zterm ");
				else
					groupByQuery.append(" zterm ");
			}

			docTypePayTrimSelectQuery.append(whereQuery.toString()).append(
					groupByQuery.toString());
			purchaseOrderQueryMap.put(E2emfConstants.DOCTYPE_PAYTERM_QUERY,
					docTypePayTrimSelectQuery.toString());

		}
		
		}catch(Exception e){
			Log.Error("Exception in deriveCompanyPlantFlowList() " + e);
		}

		return purchaseOrderQueryMap;
	}

	private String getCommaSeperatedInClauseValues(List<String> valueList) {
		StringBuilder inValues = new StringBuilder();

		if (valueList.size() > 0) {
			for (int i = 0; i < valueList.size(); i++) {
				inValues.append("'").append(valueList.get(i)).append("'");
				if (i < valueList.size() - 1) {
					inValues.append(", ");
				}
			}

		}
		
		Log.Info("Created inClause values: \n" + inValues);
		
		return inValues.toString();
	}

	/* (non-Javadoc)
	 * @see com.relevance.e2emf.dao.E2emfDao#executeQuery(java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see com.relevance.e2emf.dao.E2emfDao#getBusinessObject()
	 */
	@Override
	public E2emfBusinessObject getBusinessObject() {
		
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		List<FlowData> purchaseOrderflowDataList = null;
		
		try{
			List<String> companyCodeList = getCompanyCodeList();
			List<String> plantList = getPlantList();
			List<String> purchaseGroupList = getPurchaseGroupList();
			List<String> docTypeList = getDocTypeList();
			List<String> payTermList = getPayTermList();
			
			
			purchaseOrder.setCompanyCodeList(companyCodeList);
			purchaseOrder.setPlantList(plantList);
			purchaseOrder.setPurchaseGroupList(purchaseGroupList);
			purchaseOrder.setDocTypeList(docTypeList);
			purchaseOrder.setPayTermList(payTermList);
			
			purchaseOrderflowDataList = getPurchaseOrderFlowDataList(companyCodeList, plantList, purchaseGroupList, docTypeList, payTermList);
			purchaseOrder.setPurchaseOrderflowDataList(purchaseOrderflowDataList);
			
			
			
		}catch(Exception e){
			Log.Error("Exception fetching PurchaseOrder Object....");
			Log.Error(e);
		}
		
		
		
		return purchaseOrder;
	}

	/* (non-Javadoc)
	 * @see com.relevance.e2emf.dao.E2emfDao#getBusinessObject(java.lang.Object[])
	 */
	@Override
	public E2emfBusinessObject getBusinessObject(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}
 

}
