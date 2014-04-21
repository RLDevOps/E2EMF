package com.service;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;






/* imports for solr */
import org.apache.solr.client.solrj.*;
import org.apache.solr.common.*;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.client.solrj.response.*;
import org.apache.solr.client.solrj.impl.*;
import org.apache.noggit.JSONUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.util.Configurator;

@Path("/search")
@Consumes("application/javascript")
public class SearchService {
	
	private Configurator props = null;
	private String solrServerURL = null;
	private String solrServerInstance = null;
	
	public SearchService() throws IOException{
		props = Configurator.getInstance();
		solrServerURL = props.getSolrServerURL();
		solrServerInstance = props.getSolrServerInstance();	
	}

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/searchservice/{varX}")
  public String searchData(@PathParam("varX") String varX) throws JSONException {
	
	  CommonsHttpSolrServer server = null;
	  SolrDocumentList sdl = null;
	  String returnValue = null;
	  String lookUpStr = null;
	  
	  try {		  
		  server = new CommonsHttpSolrServer(solrServerURL+solrServerInstance+"/");
	  } catch(Exception e) {
		  e.printStackTrace();
	  }

	  String facetStr = searchParamProcess(varX);
	  
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
		  QueryResponse qr = server.query(parameters);			

		  sdl = qr.getResults();
		  long resultCount = sdl.getNumFound();
		  returnValue= JSONUtil.toJSON(sdl);		 

		  JSONObject json = new JSONObject("{ \"SearchResult\":"+returnValue+"}");
		  returnValue = XML.toString(json);
		  
		  lookUpStr = getSearchLookUp(queryFilterVal);	  

		  returnValue = "<?xml version='1.0' encoding='UTF-8'?><SearchResults><count>"+resultCount+"</count><facet>"+queryFilterVal+"</facet>"+lookUpStr+returnValue +"</SearchResults>";
		  
	  } catch (SolrServerException e) {
		  e.printStackTrace();
	  }

	  if( null != sdl ){		 
		  return returnValue;
	  } else {
		  return "Nothing returned from Solr";
	  }
  }
  
  private String searchParamProcess(String searchParam){

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
  
  
  private String getSearchLookUp(String queryFilterVal) {
	  
	  String lookUpStr=null;
	  String col1, col2, col3, col4, col5, col6 = null;
	  String[] col1Str, col2Str, col3Str, col4Str, col5Str, col6Str;
	  
      if(queryFilterVal.equalsIgnoreCase("EKKO")){
		  
		  col1 = props.getPOHeaderPurchDocField();
		  col1Str = parsePropertiesKeyValue(col1);		  
		  		  
		  col2 = props.getPOHeaderVendorField();
		  col2Str = parsePropertiesKeyValue(col2);
		  		  
		  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2></Header> ";		  
	  }
	  
	  if(queryFilterVal.equalsIgnoreCase("EKPO")){
		  
		  col1 = props.getPOPurchDocField();
		  col1Str = parsePropertiesKeyValue(col1);
		  
		  /*String pReq2 = props.getPOItemField();
		  String[] col2Str = parsePropertiesKeyValue(pReq2);*/
		  
		  col2 = props.getPOMaterialField();
		  col2Str = parsePropertiesKeyValue(col2);
		  
		  col3 = props.getPOMaterialNumField();
		  col3Str = parsePropertiesKeyValue(col3);
		  
		 /* String pReq5 = props.getPOQtyField();
		  String[] col5Str = parsePropertiesKeyValue(pReq5);*/
		  
		  /*String pReq6 = props.getPONetPriceField();
		  String[] col6Str = parsePropertiesKeyValue(pReq6);*/
		  
		 // lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3><col4 FieldMap='"+col4Str[0]+"'>"+col4Str[1]+"</col4><col5 FieldMap='"+col5Str[0]+"'>"+col5Str[1]+"</col5><col6 FieldMap='"+col6Str[0]+"'>"+col6Str[1]+"</col6></Header> "; 
		  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3></Header> ";
		  
	  } if(queryFilterVal.equalsIgnoreCase("VBAPO")){
		  
		  lookUpStr = "<Header><col1 FieldMap='"+props.getSOSalesDocField()+"'>Sales Doc.#</col1><col2 FieldMap='"+props.getSOItemField()+"'>Item#</col2><col3 FieldMap='"+props.getSOMaterialNumField()+"'>Material #</col3><col4 FieldMap='"+props.getSOMaterialField()+"'>Material</col4><col5 FieldMap='"+props.getSONetValueField()+"'>Net Value</col5><col6 FieldMap='"+props.getSOOrderQtyField()+"'>Order Qty</col6></Header> ";
		  
	  } if(queryFilterVal.equalsIgnoreCase("EBKN")){
		  
		   col1 = props.getPRFirstField();
		   col1Str = parsePropertiesKeyValue(col1);
		  
		   col2 = props.getPRSecondField();
		   col2Str = parsePropertiesKeyValue(col2);
		  
		   col3 = props.getPRThirdField();
		   col3Str = parsePropertiesKeyValue(col3);			  
		  
		  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3></Header> ";			  
	  }	
	  
	   if(queryFilterVal.equalsIgnoreCase("EBAN")){
		  
		   col1 = props.getPREBANFirstField();
		   col1Str = parsePropertiesKeyValue(col1);
		  
		   col2 = props.getPREBANSecondField();
		   col2Str = parsePropertiesKeyValue(col2);
		  
		   col3 = props.getPREBANThirdField();
		   col3Str = parsePropertiesKeyValue(col3);	
		   
		   col4 = props.getPREBANFourthField();
		   col4Str = parsePropertiesKeyValue(col4);	
		  
		  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3><col4 FieldMap='"+col4Str[0]+"'>"+col4Str[1]+"</col4></Header> ";			  
	  }		
	   if(queryFilterVal.equalsIgnoreCase("KNA1")){
			  
		   col1 = props.getKNAFirstField();
		   col1Str = parsePropertiesKeyValue(col1);
		  
		   col2 = props.getKNASecondField();
		   col2Str = parsePropertiesKeyValue(col2);
		  
		   col3 = props.getKNAThirdField();
		   col3Str = parsePropertiesKeyValue(col3);	
		   
		   col4 = props.getKNAFourthField();
		   col4Str = parsePropertiesKeyValue(col4);	
		   
		   col5 = props.getKNAFifthField();
		   col5Str = parsePropertiesKeyValue(col5);	
		  
		  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3><col4 FieldMap='"+col4Str[0]+"'>"+col4Str[1]+"</col4><col5 FieldMap='"+col5Str[0]+"'>"+col5Str[1]+"</col5></Header> ";			  
	  }	
	   if(queryFilterVal.equalsIgnoreCase("KNB1")){
			  
		   col1 = props.getKNBFirstField();
		   col1Str = parsePropertiesKeyValue(col1);
		  
		   col2 = props.getKNBSecondField();
		   col2Str = parsePropertiesKeyValue(col2);		  
		  
		  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2></Header> ";			  
	  }	
	   
	   if(queryFilterVal.equalsIgnoreCase("LFA1")){
			  
		   col1 = props.getLFAFirstField();
		   col1Str = parsePropertiesKeyValue(col1);
		  
		   col2 = props.getLFASecondField();
		   col2Str = parsePropertiesKeyValue(col2);		
		   
		   col3 = props.getLFAThirdField();
		   col3Str = parsePropertiesKeyValue(col3);
		  
		   col4 = props.getLFAFourthField();
		   col4Str = parsePropertiesKeyValue(col4);
		   
		   col5 = props.getLFAFifthField();
		   col5Str = parsePropertiesKeyValue(col5);
		  
		  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3><col4 FieldMap='"+col4Str[0]+"'>"+col4Str[1]+"</col4><col5 FieldMap='"+col5Str[0]+"'>"+col5Str[1]+"</col5></Header> ";			  
	  }
	   if(queryFilterVal.equalsIgnoreCase("MAKT")){
			  
		   col1 = props.getMAKTFirstField();
		   col1Str = parsePropertiesKeyValue(col1);
		  
		   col2 = props.getMAKTSecondField();
		   col2Str = parsePropertiesKeyValue(col2);		  
		  
		  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2></Header> ";			  
	  }	
	   
	   if(queryFilterVal.equalsIgnoreCase("MARA")){
			  
		   col1 = props.getMARAFirstField();
		   col1Str = parsePropertiesKeyValue(col1);
		  
		   col2 = props.getMARASecondField();
		   col2Str = parsePropertiesKeyValue(col2);	
		   
		   col3 = props.getMARAThirdField();
		   col3Str = parsePropertiesKeyValue(col3);
		  
		  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2><col3 FieldMap='"+col3Str[0]+"'>"+col3Str[1]+"</col3></Header> ";			  
	  }	
	   
	   if(queryFilterVal.equalsIgnoreCase("MARC")){
			  
		   col1 = props.getMARCFirstField();
		   col1Str = parsePropertiesKeyValue(col1);
		  
		   col2 = props.getMARCSecondField();
		   col2Str = parsePropertiesKeyValue(col2);			   		   
		  
		  lookUpStr = "<Header><col1 FieldMap='"+col1Str[0]+"'>"+col1Str[1]+"</col1><col2 FieldMap='"+col2Str[0]+"'>"+col2Str[1]+"</col2></Header> ";			  
	  }	
	  
	  return lookUpStr;

  }
  
  
  private String[] parsePropertiesKeyValue(String value){
	  
	  String[] pReqSplitStr = value.split(",");	  
	  return pReqSplitStr;
	  
  }
   
  
} 