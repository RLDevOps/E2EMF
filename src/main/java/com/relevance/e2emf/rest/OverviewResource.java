package com.relevance.e2emf.rest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.relevance.e2emf.commons.E2emfAppUtil;
import com.relevance.e2emf.commons.E2emfDbUtil;
import com.relevance.e2emf.commons.Log;
import com.relevance.e2emf.dao.MaterialDao;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.domain.Material;
import com.relevance.e2emf.service.E2emfServiceLocator;
import com.relevance.e2emf.service.Service;
import com.relevance.e2emf.view.E2emfView;
import com.relevance.e2emf.view.FlowData;


/**
 * @author emanuel
 * E2emf overviewService 
 *
 */


@Path("/searchLndData")
@Consumes(MediaType.APPLICATION_JSON)
public class OverviewResource {

	@Context
	ServletContext context;
	static boolean isFirstTimeLoad = true;
	
	
	

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/getE2EMF/")
	public String getE2emfMaterial(@FormParam("supplier") String supplier,
			@FormParam("customer") String customer,
			@FormParam("meterialid") String meterialId,
			@FormParam("tier1") String tier1, @FormParam("tier2") String tier2,
			@FormParam("type") String type) {
	
		Log.Info("getE2emfMaterial() on overView service called... ");		
		String materialJsonStr = null;
		List<FlowData> materialFlowList = null;
		
		MaterialDao mDao= new MaterialDao();
		
		Log.Info("Fetching Data List based on the Inputs \n  Supplier : " + supplier + "\n Customer : " + customer + "\n meterialId " + meterialId + "\n Tier1 : " + tier1 + "\n type : " + type);
		

		Service service = E2emfServiceLocator.getServiceInstance("material");		
		materialFlowList = (List<FlowData>) this.context.getAttribute("e2emfsankeyflowdata");
		
		Log.Info("Material FlowList Retrieved from Context on initial Load " + materialFlowList);
		if (materialFlowList != null && isFirstTimeLoad) {			
			Log.Info("MaterialFlow list retrieved from context : \n" + materialFlowList);
			
			Material material = new Material();
			material.setMaterialflowList(materialFlowList);			
			
			materialJsonStr = service.generateJsonString(material);
			Log.Info("Material Json retrieved on load " + materialJsonStr);
			
			isFirstTimeLoad = false;

		} else {			
			
			Log.Info("Getting Material Json for Subsequent calls...");
			materialJsonStr = (String) service.getJsonObject(supplier,customer, meterialId, tier1, tier2, type);			
			Log.Info("Material Json formed based on parameters : \n" + materialJsonStr);
		}
		
		Log.Info("Material Json formed based on parameters : \n" + materialJsonStr);

		return materialJsonStr;
		

	}

	

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getE2emfpo/")
	public String getE2emfPurchaseOrder() {
		
		Log.Info("getE2emfPurchaseOrder() on overView service called...");
		
		String purchaseOrderJsonStr = null;
	
		try{
		Service service = E2emfServiceLocator.getServiceInstance("purchase");
		purchaseOrderJsonStr = service.getJsonObject();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Log.Info("Purchase Order Json : \n" + purchaseOrderJsonStr );
		
		return purchaseOrderJsonStr;
	}



	
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/searchSummaryDataNode/{varX}")
	public String searchLevel2DataNode(@PathParam("varX") String varX) {
		
		Log.Info("searchSummaryDataNode/{varX} on overView service called");

		String searchLevel2NodeData = "";

		if(!varX.contains(",")) {
			Service service = E2emfServiceLocator.getServiceInstance("search");			
			searchLevel2NodeData = (String) service.getJsonObject(varX);	
			
		}
		Log.Info("searchLevel2NodeData Json : \n" + searchLevel2NodeData);
		return searchLevel2NodeData;
	}
	
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/searchDetailedData/{varX}")
	public String searchSummaryData(@PathParam("varX") String varX) {
		
		Log.Info("searchDetailedData/{varX} on overView service called");
		
		String searchSummaryJson = "";

		if(!varX.contains(",")) {			
			Service service = E2emfServiceLocator.getServiceInstance("search");			
			searchSummaryJson = (String) service.getJsonObject(varX , "searchSummary");
		}
		
		Log.Info("SearchSummary Json : \n" + searchSummaryJson);
		
		return searchSummaryJson;
		
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getD3JSON/")
	public String getD3SankeyJson() {
		String d3Json = null;

		//d3Json = "{\"phonetype\":\"N95\",\"cat\":\"WP\"}";

		d3Json = "{\"nodes\":["
				+"{\"name\":\"Ext V 1\",\"color\":\"#FFE4C4\",\"stack\":1},"
				+"{\"name\":\"Ext V 2\",\"color\":\"#FFE4C4\",\"stack\":1},"
				+"{\"name\":\"Aff V 1\",\"color\":\"#0000FF\",\"stack\":1},"
				+"{\"name\":\"Aff V 2\",\"color\":\"#0000FF\",\"stack\":1},"
				+"{\"name\":\"Manuf Plant 1 (w. Company Code)\",\"color\":\"#8A2BE2\",\"stack\":2},"
				+"{\"name\":\"Manuf  Plant 2 (w. company code)\",\"color\":\"#8A2BE2\",\"stack\":2},"
				+"{\"name\":\"Company Code\",\"color\":\"#8A2BE2\",\"stack\":2},"
				+"{\"name\":\"DC Plant 1 (w. CC)\",\"color\":\"#8A2BE2\",\"stack\":3},"
				+"{\"name\":\"DC Plant 2 (w. CC)\",\"color\":\"#8A2BE2\",\"stack\":3},"
				+"{\"name\":\"DC Plant 3 (w. CC)\",\"color\":\"#8A2BE2\",\"stack\":3},"
				+"{\"name\":\"Distributor 1\",\"color\":\"#DEB887\",\"stack\":4},"
				+"{\"name\":\"Distributor 2\",\"color\":\"#DEB887\",\"stack\":4},"
				+"{\"name\":\"Customer 1\",\"color\":\"#FF7F50\",\"stack\":4},"
				+"{\"name\":\"Customer 2\",\"color\":\"#FF7F50\",\"stack\":4},"
				+"{\"name\":\"Customer 3\",\"color\":\"#FF7F50\",\"stack\":4}"
			+"],"
			+"\"links\":["
				+"{\"source\":\"Ext V 1\",\"target\":\"Manuf Plant 1 (w. Company Code)\",\"value\":124,\"color\":\"#FFE4C4\"},"
				+"{\"source\":\"Ext V 2\",\"target\":\"Customer 2\",\"value\":30,\"color\":\"#0000FF\"},"
				+"{\"source\":\"Aff V 1\",\"target\":\"Manuf Plant 1 (w. Company Code)\",\"value\":26,\"color\":\"#8A2BE2\"},"
				+"{\"source\":\"Aff V 1\",\"target\":\"Manuf  Plant 2 (w. company code)\",\"value\":180,\"color\":\"#A52A2A\"},"
				+"{\"source\":\"Aff V 2\",\"target\":\"Manuf  Plant 2 (w. company code)\",\"value\":81,\"color\":\"#DEB887\"},"
				+"{\"source\":\"Aff V 2\",\"target\":\"Customer 3\",\"value\":35,\"color\":\"#5F9EA0\"},"
				+"{\"source\":\"Manuf Plant 1 (w. Company Code)\",\"target\":\"DC Plant 2 (w. CC)\",\"value\":35,\"color\":\"#7FFF00\"},"
				+"{\"source\":\"Manuf Plant 1 (w. Company Code)\",\"target\":\"DC Plant 3 (w. CC)\",\"value\":11,\"color\":\"#D2691E\"},"
				+"{\"source\":\"Manuf  Plant 2 (w. company code)\",\"target\":\"Customer 1\",\"value\":63,\"color\":\"#FF7F50\"},"
				+"{\"source\":\"DC Plant 1 (w. CC)\",\"target\":\"Distributor 1\",\"value\":75,\"color\":\"#6495ED\"},"
				+"{\"source\":\"DC Plant 2 (w. CC)\",\"target\":\"Distributor 2\",\"value\":10,\"color\":\"#DC143C\"},"
				+"{\"source\":\"DC Plant 3 (w. CC)\",\"target\":\"Customer 2\",\"value\":22,\"color\":\"#00008B\"}"
			+"]}";


		 return d3Json;
	  }

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getNodeDataDropDown/")
	public String getNodeDataForDropDown() {

		   Connection conn = null;
		   Statement stmt1 = null;
		   Statement stmt2 = null;
		   Statement stmt3 = null;
		   Statement stmt4 = null;
		   Statement stmt5 = null;
		   String doc = "";
		   String jsonResult1 ="";
		   String jsonResult2 ="";
		   String jsonResult3 ="";
		   String jsonResult4 ="";
		   String jsonResult5 ="";
		   
		   E2emfDbUtil dbUtil = new E2emfDbUtil();
		   E2emfAppUtil appUtil = new E2emfAppUtil();
		  
		   
		   //final String JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";
		   //final String DB_URL = "jdbc:hive2://RLD3HADOO03.RLLABINDIA.COM:21050/;auth=noSasl";
		   final String USER = "";
		   final String PASS = "";

			  try {
				  //Class.forName(JDBC_DRIVER);

				//  conn = DriverManager.getConnection(DB_URL,USER,PASS);
				  
				  conn = dbUtil.getHiveConnection();
				  
			      stmt1 = conn.createStatement();
			      stmt2 = conn.createStatement();
			      stmt3 = conn.createStatement();
			      stmt4 = conn.createStatement();
			      stmt5 = conn.createStatement();
			      String sqlquery1,sqlquery2, sqlquery3, sqlquery4, sqlquery5 ;
			      sqlquery1 = "select distinct porgpgr from poproc22 order by porgpgr limit 100";
			      sqlquery2 = "select distinct doctype from poproc22 order by doctype limit 100";
			      sqlquery3 = "select distinct payt from poproc22 order by payt limit 100";
			      sqlquery4 = "select distinct plnt from poproc22 order by plnt limit 100";
			      sqlquery5 = "select distinct cocd from poproc22 order by cocd limit 100";
			      ResultSet rs1 = stmt1.executeQuery(sqlquery1);
			      ResultSet rs2 = stmt2.executeQuery(sqlquery2);
			      ResultSet rs3 = stmt3.executeQuery(sqlquery3);
			      ResultSet rs4 = stmt4.executeQuery(sqlquery4);
			      ResultSet rs5 = stmt5.executeQuery(sqlquery5);
			     // SearchUtil util = new SearchUtil();
			      jsonResult1 = dbUtil.getJSONFromResultSet(rs5, "\"cocd\"");
			      jsonResult2 = dbUtil.getJSONFromResultSet(rs4, "\"plnt\"");
			      jsonResult3 = dbUtil.getJSONFromResultSet(rs1, "\"porg\"");
			      jsonResult4 = dbUtil.getJSONFromResultSet(rs2, "\"doctype\"");
			      jsonResult5 = dbUtil.getJSONFromResultSet(rs3, "\"payt\"");


			  } catch (SQLException e) {
					e.printStackTrace();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			  finally {
			   try {
				   stmt1.close();
				   stmt2.close();
				   stmt3.close();
				   stmt4.close();
				   conn.close();
			   } catch (SQLException e) {
			       e.printStackTrace();
			   }
		      }

			  String resultSubs1 = "";
			  String resultSubs2 = "";
			  String resultSubs3 = "";
			  String resultSubs4 = "";
			  String resultSubs5 = "";
			  String Result = "";

			  if(jsonResult1.length()>0){
				  resultSubs1 =  jsonResult1.substring(1, jsonResult1.length()-1);
				  Result = resultSubs1;
			  }
			  if(jsonResult2.length()>0){
				  resultSubs2 =  jsonResult2.substring(1, jsonResult2.length()-1);
				  Result = Result + "," + resultSubs2;
			  }
			  if(jsonResult3.length()>0){
				  resultSubs3 =  jsonResult3.substring(1, jsonResult3.length()-1);
				  Result = Result + "," +  resultSubs3;
			  }
			  if(jsonResult4.length()>0){
				  resultSubs4 =  jsonResult4.substring(1, jsonResult4.length()-1);
				  Result = Result + "," + resultSubs4;
			  }
			  if(jsonResult5.length()>0){
				  resultSubs5 =  jsonResult5.substring(1, jsonResult5.length()-1);
				  Result = Result + "," + resultSubs5;
			  }


			  String finalNode = "{ \"stackList\": {" + Result + "}}";
			  return finalNode;
	 }

}
