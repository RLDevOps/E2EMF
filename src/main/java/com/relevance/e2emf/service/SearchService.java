package com.relevance.e2emf.service;

import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;

import com.relevance.e2emf.commons.E2emfAppUtil;
import com.relevance.e2emf.commons.E2emfConstants;
import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.dao.E2emfDao;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.view.E2emfView;
import com.relevance.e2emf.commons.Log;
//import com.relevance.e2emf.view.search.SearchView;

import org.apache.noggit.JSONUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
/**
 * @author emanuel
 * E2emf Search Service 
 *
 */
public class SearchService implements Service {

	E2emfAppUtil appUtil;
	E2emfDbUtil dbUtil;
	
	static String solarUrl = "SOLR_SERVER_URL";
	static String solarInstanceName = "SOLR_SERVER_INSTANCE_NAME";
	
	static String solarServerInsanceUrl = null;

	public SearchService() {

		appUtil = new E2emfAppUtil();
		dbUtil = new E2emfDbUtil();
	}

	@Override
	public String getJsonObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJsonObject(Object... obj) {
		String searchResultJson = null;
		CommonsHttpSolrServer solarServer = null;
		QueryResponse queryResponse = null;
		SolrDocumentList solarDocumentList = null;
		
		E2emfDao searchDao = E2emfServiceLocator.getServiceDaoInstance("search");
		String searchCriteria = null;
		if(obj != null && obj.length >1){
			searchCriteria = (String) obj[1];
			Log.Info("Searching initiated for " + searchCriteria);
		}else {
			searchCriteria = (String) obj[0];
			Log.Info("Searching initiated for " + searchCriteria);
		}
		   
		
		
		if (searchCriteria != null && searchCriteria.equalsIgnoreCase("searchSummary")) {
			
			
			Map<String, String> searchSummaryQueryMap = dbUtil
					.buildSearchSummaryQuery((String) obj[0]);

			String searchEkkoQuery = searchSummaryQueryMap
					.get(E2emfConstants.SEARCH_SUMMARY_EKKO_QUERY);
			String searchEkpoQuery = searchSummaryQueryMap
					.get(E2emfConstants.SEARCH_SUMMARY_EKPO_QUERY);

			String ekkoSearchJson = searchDao.executeQuery(searchEkkoQuery);
			String ekpoSearchJson = searchDao.executeQuery(searchEkpoQuery);

			searchResultJson = " { \"HeaderKey\" : " + ekkoSearchJson
					+ ", \"DetailKey\" : " + ekpoSearchJson + "}";

			
		} 
		else if(searchCriteria != null && searchCriteria.equalsIgnoreCase("searchService")) {
			
			 String solarReturnValue = null;
			 String lookUpStr = null;
			 
			 solarServer = getSolarServerConnection();
			 String facetStr = searchParamProcess((String) obj[0]);
			 
			  String[] facetStrSplit = facetStr.split(":");
			  String queryParamVal = facetStrSplit[0];
			  String queryFilterVal = facetStrSplit[1];			  
			   
			  ModifiableSolrParams parameters = new ModifiableSolrParams();	 
			  parameters.set("q", queryParamVal);
			  parameters.set("defType", "edismax");
			  parameters.set("wt", "xml");
			  parameters.set("start", "0");
			  parameters.set("rows", "200");
			  parameters.set("qf", "allcanadadataci priorityci^2 keywordsci");
			  parameters.set("fq", "facet:"+queryFilterVal);	 

			  try {
				  if(solarServer != null){
					  queryResponse = solarServer.query(parameters);  
					  Log.Info("Recieved response from solar : " + queryResponse );
				  }else{
					  Log.Error("Unable to connect to solarServer...");
				  }
				  			

				  solarDocumentList = queryResponse.getResults();
				  long resultCount = solarDocumentList.getNumFound();
				  solarReturnValue = JSONUtil.toJSON(solarDocumentList);		 

				  JSONObject json;
				  
				  json = new JSONObject("{ \"SearchResult\":"+solarReturnValue+"}");
			 
				  solarReturnValue = XML.toString(json);
				  
				  lookUpStr = getSearchLookUp(queryFilterVal);	  

				  solarReturnValue = "<?xml version='1.0' encoding='UTF-8'?><SearchResults><count>"+resultCount+"</count><facet>"+queryFilterVal+"</facet>"+lookUpStr+solarReturnValue +"</SearchResults>";
				  Log.Info("Search Results : \n " + solarReturnValue);
			  }catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			  } catch (SolrServerException e) {
				  e.printStackTrace();
			  }

			  if( null != solarDocumentList ){		 
				  return solarReturnValue;
			  } else {
				  Log.Info("Search Results : Nothing returned from Solr ");
				  return "Nothing returned from Solr";
			  }

			
			
			//return searchResultJson;
			
		}else {
			
			String nodeLevelQuery = getNodeLevelQuery((String) obj[0]);
			String nodeLevelJson = searchDao.executeQuery(nodeLevelQuery);
			searchResultJson = "{ \"MainKey\" : " + nodeLevelJson + "}";
			
			//return searchResultJson;
		}
		
		Log.Info("Search Results : \n" + searchResultJson);
		
		return searchResultJson;

	}
	

	private String getSearchLookUp(String queryFilterVal) {
		  String lookUpStr=null;
		  String col1, col2, col3, col4, col5, col6 = null;
		  String[] col1Str, col2Str, col3Str, col4Str, col5Str, col6Str;
		  
	      if(queryFilterVal.equalsIgnoreCase("EKKO")){
	    	  
	    	  col1 = appUtil.getAppProperty(E2emfConstants.POH1);
	    	  col1Str = parsePropertiesKeyValue(col1);	
		  
	    	  col2 = appUtil.getAppProperty(E2emfConstants.POH2);
	    	  col2Str = parsePropertiesKeyValue(col2);		    	  
			  		  
			  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2></Header> ";
	      }
	      
	      if(queryFilterVal.equalsIgnoreCase("EKPO")){
	    	  
			  col1 = appUtil.getAppProperty(E2emfConstants.POD1);
			  col1Str = parsePropertiesKeyValue(col1);
			  
			  /*String pReq2 = appUtil.getAppProperty(getPOItemField();
			  String[] col2Str = parsePropertiesKeyValue(pReq2);*/
			  
			  col2 = appUtil.getAppProperty(E2emfConstants.POD2);
			  col2Str = parsePropertiesKeyValue(col2);
			  
			  col3 = appUtil.getAppProperty(E2emfConstants.POD3);
			  col3Str = parsePropertiesKeyValue(col3);
			  
			 /* String pReq5 = appUtil.getAppProperty(getPOQtyField();
			  String[] col5Str = parsePropertiesKeyValue(pReq5);*/
			  
			  /*String pReq6 = appUtil.getAppProperty(getPONetPriceField();
			  String[] col6Str = parsePropertiesKeyValue(pReq6);*/
			  
			 // lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3><col4 FieldMap='"+col4Str[0]+"'>"+col4Str[1]+"</col4><col5 FieldMap='"+col5Str[0]+"'>"+col5Str[1]+"</col5><col6 FieldMap='"+col6Str[0]+"'>"+col6Str[1]+"</col6></Header> "; 
			  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3></Header> ";
			  
		  } if(queryFilterVal.equalsIgnoreCase("VBAPO")){
			  
			  lookUpStr = "<Header><col1 FieldMap='"+appUtil.getAppProperty(E2emfConstants.SOSalesDoc)+"'>Sales Doc.#</col1><col2 FieldMap='"+appUtil.getAppProperty(E2emfConstants.SOItem)+"'>Item#</col2><col3 FieldMap='"+appUtil.getAppProperty(E2emfConstants.SOMaterialNum)+"'>Material #</col3><col4 FieldMap='"+appUtil.getAppProperty(E2emfConstants.SOMaterial)+"'>Material</col4><col5 FieldMap='"+appUtil.getAppProperty(E2emfConstants.SONetValue)+"'>Net Value</col5><col6 FieldMap='"+appUtil.getAppProperty(E2emfConstants.SOOrderQty)+"'>Order Qty</col6></Header> ";
			  
		  } if(queryFilterVal.equalsIgnoreCase("EBKN")){
			  
			   col1 = appUtil.getAppProperty(E2emfConstants.PR1);
			   col1Str = parsePropertiesKeyValue(col1);
			  
			   col2 = appUtil.getAppProperty(E2emfConstants.PR2);
			   col2Str = parsePropertiesKeyValue(col2);
			  
			   col3 = appUtil.getAppProperty(E2emfConstants.PR3);
			   col3Str = parsePropertiesKeyValue(col3);			  
			  
			  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3></Header> ";			  
		  }	
		  
		   if(queryFilterVal.equalsIgnoreCase("EBAN")){
			  
			   col1 = appUtil.getAppProperty(E2emfConstants.EBANPR1);
			   col1Str = parsePropertiesKeyValue(col1);
			  
			   col2 = appUtil.getAppProperty(E2emfConstants.EBANPR2);
			   col2Str = parsePropertiesKeyValue(col2);
			  
			   col3 = appUtil.getAppProperty(E2emfConstants.EBANPR3);
			   col3Str = parsePropertiesKeyValue(col3);	
			   
			   col4 = appUtil.getAppProperty(E2emfConstants.EBANPR4);
			   col4Str = parsePropertiesKeyValue(col4);	
			  
			  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3><col4 FieldMap='"+col4Str[0]+"'>"+col4Str[1]+"</col4></Header> ";			  
		  }		
		   if(queryFilterVal.equalsIgnoreCase("KNA1")){
				  
			   col1 = appUtil.getAppProperty(E2emfConstants.KNACust1);
			   col1Str = parsePropertiesKeyValue(col1);
			  
			   col2 = appUtil.getAppProperty(E2emfConstants.KNACust2);
			   col2Str = parsePropertiesKeyValue(col2);
			  
			   col3 = appUtil.getAppProperty(E2emfConstants.KNACust3);
			   col3Str = parsePropertiesKeyValue(col3);	
			   
			   col4 = appUtil.getAppProperty(E2emfConstants.KNACust4);
			   col4Str = parsePropertiesKeyValue(col4);	
			   
			   col5 = appUtil.getAppProperty(E2emfConstants.KNACust5);
			   col5Str = parsePropertiesKeyValue(col5);	
			  
			  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3><col4 FieldMap='"+col4Str[0]+"'>"+col4Str[1]+"</col4><col5 FieldMap='"+col5Str[0]+"'>"+col5Str[1]+"</col5></Header> ";			  
		  }	
		   if(queryFilterVal.equalsIgnoreCase("KNB1")){
				  
			   col1 = appUtil.getAppProperty(E2emfConstants.KNBCust1);
			   col1Str = parsePropertiesKeyValue(col1);
			  
			   col2 = appUtil.getAppProperty(E2emfConstants.KNBCust2);
			   col2Str = parsePropertiesKeyValue(col2);		  
			  
			  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2></Header> ";			  
		  }	
		   
		   if(queryFilterVal.equalsIgnoreCase("LFA1")){
				  
			   col1 = appUtil.getAppProperty(E2emfConstants.LFA1);
			   col1Str = parsePropertiesKeyValue(col1);
			  
			   col2 = appUtil.getAppProperty(E2emfConstants.LFA2);
			   col2Str = parsePropertiesKeyValue(col2);		
			   
			   col3 = appUtil.getAppProperty(E2emfConstants.LFA3);
			   col3Str = parsePropertiesKeyValue(col3);
			  
			   col4 = appUtil.getAppProperty(E2emfConstants.LFA4);
			   col4Str = parsePropertiesKeyValue(col4);
			   
			   col5 = appUtil.getAppProperty(E2emfConstants.LFA5);
			   col5Str = parsePropertiesKeyValue(col5);
			  
			  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3><col4 FieldMap='"+col4Str[0]+"'>"+col4Str[1]+"</col4><col5 FieldMap='"+col5Str[0]+"'>"+col5Str[1]+"</col5></Header> ";			  
		  }
		   if(queryFilterVal.equalsIgnoreCase("MAKT")){
				  
			   col1 = appUtil.getAppProperty(E2emfConstants.MAKT1);
			   col1Str = parsePropertiesKeyValue(col1);
			  
			   col2 = appUtil.getAppProperty(E2emfConstants.MAKT2);
			   col2Str = parsePropertiesKeyValue(col2);		  
			  
			  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2></Header> ";			  
		  }	
		   
		   if(queryFilterVal.equalsIgnoreCase("MARA")){
				  
			   col1 = appUtil.getAppProperty(E2emfConstants.MARA1);
			   col1Str = parsePropertiesKeyValue(col1);
			  
			   col2 = appUtil.getAppProperty(E2emfConstants.MARA2);
			   col2Str = parsePropertiesKeyValue(col2);	
			   
			   col3 = appUtil.getAppProperty(E2emfConstants.MARA3);
			   col3Str = parsePropertiesKeyValue(col3);
			  
			  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3></Header> ";			  
		  }	
		   
		   if(queryFilterVal.equalsIgnoreCase("MARC")){
				  
			   col1 = appUtil.getAppProperty(E2emfConstants.MARC1);
			   col1Str = parsePropertiesKeyValue(col1);
			  
			   col2 = appUtil.getAppProperty(E2emfConstants.MARC2);
			   col2Str = parsePropertiesKeyValue(col2);			   		   
			  
			  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2></Header> ";			  
		  }	
		  
		  return lookUpStr;
	}

	private String[] parsePropertiesKeyValue(String value) {
		 String[] pReqSplitStr = value.split(",");	  
		  return pReqSplitStr;
	}

	private String searchParamProcess(String searchParam) {
		
		 String finalStr = "";
		  //String searchParam1 = "key:a b c,facet:POH";

		  String[] strSplit = searchParam.split(",");

		  String firstSet = strSplit[0];
		  String secondSet = strSplit[1];	 

		  String[] firstSetSplit = firstSet.split(":");
		  
		  String[] secondSetSplit = secondSet.split(":");	 
		  
		  String facet = secondSetSplit[1];

		  String[] keyStr = firstSetSplit[1].split("\\s");

		  for(int i=0;i<keyStr.length; i++){		  
			  if(i != keyStr.length-1)
				  finalStr = finalStr +"*"+keyStr[i]+"* OR ";
			  else
				  finalStr = finalStr +"*"+keyStr[i]+"*";
		  }
		    //System.out.println("finalStr:"+finalStr);	    
		    return finalStr+":"+facet;
	}

	private CommonsHttpSolrServer getSolarServerConnection() {
		CommonsHttpSolrServer serverCon = null;
		
		if(solarServerInsanceUrl == null){
			solarUrl = appUtil.getAppProperty(E2emfConstants.solarUrl);
			solarInstanceName = appUtil.getAppProperty(E2emfConstants.solarInstanceName);
			solarServerInsanceUrl = solarUrl+solarInstanceName+"/";				
		}
			
		try {		  
			serverCon = new CommonsHttpSolrServer(solarServerInsanceUrl);
		  } catch(Exception e) {
			  System.out.println("Exception connecting to SolarServer...");
			  e.printStackTrace();
		  }
		
		return serverCon;
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
		// TODO Auto-generated method stub
		return null;
	}

	private String getNodeLevelQuery(String varX) {

		String nodeLevelQuery = null;

		if (!varX.contains(",")) {

			String lookupNode = appUtil.getLookUpNode(varX);

			String[] nodeStr = lookupNode.split("\\s");

			String nodeName = nodeStr[0];
			String nodeVal = nodeStr[1];
			String finalNodeVal = null;
			if (nodeVal.startsWith("CC")) {
				finalNodeVal = nodeVal.substring(3);
			} else {
				finalNodeVal = nodeVal;
			}

			nodeLevelQuery = dbUtil.buildFirstLevelNodeQuery(nodeName,
					finalNodeVal);

		} else {
			String lookupNode = appUtil.getLookUpNodeConnector(varX);

			String[] nodeStr = lookupNode.split(",");

			String firstSet = nodeStr[0];
			String secondSet = nodeStr[1];
			String[] splitStrFirstSet = firstSet.split("\\s");
			String firstNodeName = splitStrFirstSet[0];
			String firstNodeValue = splitStrFirstSet[1];
			String finalNodeVal = null;
			if (firstNodeValue.startsWith("CC")) {
				finalNodeVal = firstNodeValue.substring(3);
			} else {
				finalNodeVal = firstNodeValue;
			}

			String[] splitStrSecondSet = secondSet.split("\\s");
			String secondNodeName = splitStrSecondSet[0];
			String secondNodeValue = splitStrSecondSet[1];

			nodeLevelQuery = dbUtil.buildSecondLevelNodeQuery(firstNodeName,
					finalNodeVal, secondNodeName, secondNodeValue);

		}
		Log.Info("Node Level Query : \n" + nodeLevelQuery);

		return nodeLevelQuery;

	}

}
