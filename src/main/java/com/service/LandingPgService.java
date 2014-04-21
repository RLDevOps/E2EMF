package com.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.noggit.JSONUtil;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

import com.dao.MaterialFlowDAO;
import com.dao.PurchaseOrderDAO;
import com.data.FlowData;
import com.model.Link;
import com.model.MaterialFlow;
import com.model.Node;
import com.util.Configurator;
import com.util.SearchUtil;

@Path("/searchLndData")
@Consumes(MediaType.APPLICATION_JSON)
public class LandingPgService {
	
	@Context
	ServletContext context;
	
	private static Logger log = LogManager.getLogger(LandingPgService.class);
	private Configurator props = null;
	private String JDBC_DRIVER = null;
	private String DB_URL = null;
	

	public LandingPgService(@Context ServletContext value) throws IOException{
		this.context = value;
		props = Configurator.getInstance();
		JDBC_DRIVER = props.getConnectionDriver();
		DB_URL = props.getConnectionURL();		
	}
	
	  public String searchLandingPgData() {	   
		 
	   Connection conn = null;
	   Statement stmt1 = null;
	   Statement stmt2 = null;
	   Statement stmt3 = null;
	   Statement stmt4 = null;
	   String doc = "";
	   String jsonResult1 ="";
	   String jsonResult2 ="";
	   String jsonResult3 ="";
	   String jsonResult4 ="";
	   final String JDBC_DRIVER = props.getConnectionDriver();
	   final String DB_URL = props.getConnectionURL();
	   
	   //final String JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";
	   //final String DB_URL = "jdbc:hive2://RLD3HADOO04.RLLABINDIA.COM:21050/;auth=noSasl";
	   
	   final String USER = "";
	   final String PASS = "";
	   
		  try {
			  Class.forName(JDBC_DRIVER);

			  conn = DriverManager.getConnection(DB_URL,USER,PASS);
		      stmt1 = conn.createStatement();
		      stmt2 = conn.createStatement();
		      stmt3 = conn.createStatement();
		      stmt4 = conn.createStatement();
		      String sqlquery1,sqlquery2, sqlquery3, sqlquery4 ;
		      sqlquery1 = "select werks, sum(netvalue),ekorggrp from jandj.poproc22 group by werks, ekorggrp";
		      sqlquery2 = "select ekorggrp, sum(netvalue),bsart from jandj.poproc22 group by ekorggrp, bsart";
		      sqlquery3 = "select  bsart, sum(netvalue),zterm from jandj.poproc22 group by bsart,zterm";
		      sqlquery4 = "select bukrs, sum(netvalue),werks from jandj.poproc22 group by bukrs, werks";
		      ResultSet rs1 = stmt1.executeQuery(sqlquery1);
		      ResultSet rs2 = stmt2.executeQuery(sqlquery2);
		      ResultSet rs3 = stmt3.executeQuery(sqlquery3);
		      ResultSet rs4 = stmt4.executeQuery(sqlquery4);
		      SearchUtil util = new SearchUtil();
		      jsonResult1 = util.getJSONFromResultSetWithCol(rs4, "grpBypd");
		      jsonResult2 = util.getJSONFromResultSetWithCol(rs1, "grpBydp");
		      jsonResult3 = util.getJSONFromResultSetWithCol(rs2, "grpBypp");
		      jsonResult4 = util.getJSONFromResultSetWithCol(rs3, "grpBypc");
		      
		  } catch (ClassNotFoundException e) {
				e.printStackTrace();
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
			  Result = Result + "," + resultSubs3;
		  }
		  if(jsonResult4.length()>0){
			  resultSubs4 =  jsonResult4.substring(1, jsonResult4.length()-1);
			  Result = Result + "," + resultSubs4;
		  }
		
		  return "\"dataList\": [" + Result + "] }";
	}

@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/getNodeData/")
public String getNodeData() {	 
	   
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
	   //final String JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";  
	   //final String DB_URL = "jdbc:hive2://RLD3HADOO03.RLLABINDIA.COM:21050/;auth=noSasl";
	   final String USER = "";
	   final String PASS = "";
	   
		  try {
			  Class.forName(JDBC_DRIVER);

			  conn = DriverManager.getConnection(DB_URL,USER,PASS);
		      stmt1 = conn.createStatement();
		      stmt2 = conn.createStatement();
		      stmt3 = conn.createStatement();
		      stmt4 = conn.createStatement();
		      stmt5 = conn.createStatement();
		      String sqlquery1,sqlquery2, sqlquery3, sqlquery4, sqlquery5 ;
		      sqlquery1 = "select distinct ekorggrp from jandj.poproc22 order by ekorggrp limit 100";
		      sqlquery2 = "select distinct bsart from jandj.poproc22 order by bsart limit 100";
		      sqlquery3 = "select distinct zterm from jandj.poproc22 order by zterm limit 100";
		      sqlquery4 = "select distinct werks from jandj.poproc22 order by werks limit 100";
		      sqlquery5 = "select distinct bukrs from jandj.poproc22 order by bukrs limit 100";
		      ResultSet rs1 = stmt1.executeQuery(sqlquery1);
		      ResultSet rs2 = stmt2.executeQuery(sqlquery2);
		      ResultSet rs3 = stmt3.executeQuery(sqlquery3);
		      ResultSet rs4 = stmt4.executeQuery(sqlquery4);
		      ResultSet rs5 = stmt5.executeQuery(sqlquery5);
		      SearchUtil util = new SearchUtil();
		      jsonResult1 = util.getJSONFromResultSet(rs5, "\"0\"");
		      jsonResult2 = util.getJSONFromResultSet(rs4, "\"1\"");
		      jsonResult3 = util.getJSONFromResultSet(rs1, "\"2\"");
		      jsonResult4 = util.getJSONFromResultSet(rs2, "\"3\"");
		      jsonResult5 = util.getJSONFromResultSet(rs3, "\"4\"");
		      
		      
		  } catch (ClassNotFoundException e) {
				e.printStackTrace();
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
		   
		  
		  String finalNode = "{ \"stackList\": {" + Result + "},";
		  String finalData = searchLandingPgData();
		  //System.out.println(finalNode + finalData);
		  return finalNode + finalData;
}


@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/searchSummaryDataNode/{varX}")
public String searchLevelTwoDataNode(@PathParam("varX") String varX) {	

	Connection conn = null;
	Statement stmt1 = null;

	String jsonResult1 ="";

	//final String JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";  
	//final String DB_URL = "jdbc:hive2://RLD3HADOO03.RLLABINDIA.COM:21050/;auth=noSasl";
	final String USER = "";
	final String PASS = "";

	try {
		Class.forName(JDBC_DRIVER);

		conn = DriverManager.getConnection(DB_URL,USER,PASS);
		stmt1 = conn.createStatement();
		String sqlquery1;
		SearchUtil util = new SearchUtil();

		if(!varX.contains(",")) {
			String lookUp = util.getLookUpNode(varX);

			String[] nodeStr = lookUp.split("\\s");
						
			String nodeName = nodeStr[0];
			String nodeVal = nodeStr[1];
			String finalNodeVal= null;
			if(nodeVal.startsWith("CC")){
				 finalNodeVal = nodeVal.substring(3);
			} else {
				 finalNodeVal = nodeVal;
			}
           
			//sqlquery1 = "select porg, pgr,  purchdoc, vendor, doctype, payt, plnt, cocd, netvalue, crcy, quantity, oun from poproc23 where <node> = " + varX;
			sqlquery1 = "select ebeln, ekorg, ekgrp, lifnr, bsart, zterm, werks, bukrs, netwr, wkurs, menge, meins from jandj.poproc26 where "+nodeName+"='"+finalNodeVal+"' limit 100";
			System.out.println("sqlquery1 within If:" +sqlquery1);
		} else {

			String lookUp = util.getLookUpNodeConnector(varX);

			String[] nodeStr = lookUp.split(",");

			String firstSet = nodeStr[0];
			String secondSet = nodeStr[1];
			String[] splitStrFirstSet = firstSet.split("\\s");
			String firstNodeName = splitStrFirstSet[0];
			String firstNodeValue = splitStrFirstSet[1];
			String finalNodeVal = null;
			if(firstNodeValue.startsWith("CC")){
				 finalNodeVal = firstNodeValue.substring(3);
			} else {
				 finalNodeVal = firstNodeValue;
			}

			String[] splitStrSecondSet = secondSet.split("\\s");
			String secondNodeName = splitStrSecondSet[0];
			String secondNodeValue = splitStrSecondSet[1];
            
			//sqlquery1 = "select purchdoc, porg, pgr, vendor, doctype, payt, plnt, cocd, netvalue, crcy, quantity, oun from poproc26 where cocd = 3280 and plnt= 4000;
			sqlquery1 = "select ebeln, ekorg, ekgrp, lifnr, bsart, zterm, werks, bukrs, netwr, wkurs, menge, meins from jandj.poproc26 where "+firstNodeName+" ='"+finalNodeVal+"' and "+secondNodeName+"='"+secondNodeValue+"' limit 100";
			
			System.out.println("sqlquery1 within else:" +sqlquery1);

		}
		
		ResultSet rs1 = stmt1.executeQuery(sqlquery1);	     
		jsonResult1 = util.convert(rs1);  		

	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	finally {
		try {
			stmt1.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	System.out.println(jsonResult1);
	return "{ \"MainKey\" : "+jsonResult1+"}";
}



@GET
@Produces(MediaType.APPLICATION_JSON)
@Path("/searchDetailedData/{varX}")
public String searchSummaryData(@PathParam("varX") String varX) {
    
   Connection conn = null;
   Statement stmt1 = null;
   Statement stmt2 = null;
   
   String jsonResult1 ="";
   String jsonResult2 ="";
   
   //final String JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";  
   //final String DB_URL = "jdbc:hive2://RLD3HADOO03.RLLABINDIA.COM:21050/;auth=noSasl";
   final String USER = "";
   final String PASS = "";
   String xml = "";
   
	  try {
		  Class.forName(JDBC_DRIVER);

		  conn = DriverManager.getConnection(DB_URL,USER,PASS);
	      stmt1 = conn.createStatement();
	      stmt2 = conn.createStatement();
	      String sqlquery1,sqlquery2;
	      sqlquery1 = "select * from ekkotabHB where purchdoc='"+varX+"'";
	      sqlquery2 = "select * from ekpotabHB where purchdoc='"+varX+"'";
	      
	      ResultSet rs1 = stmt1.executeQuery(sqlquery1);
	      ResultSet rs2 = stmt2.executeQuery(sqlquery2);
	      
	      SearchUtil util = new SearchUtil();
	      jsonResult1 = util.convert(rs1);
	      jsonResult2 = util.convert(rs2);
	      
	      //Json to XML conversion 	      
	      //JSONObject json = new JSONObject("{ \"HeaderKey\" : "+jsonResult1+", \"DetailKey\" : "+jsonResult2+"}");
	      //xml = XML.toString(json);  	
	           
	      
	  } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	  finally {
	   try {
		   stmt1.close();
		   conn.close();
	   } catch (SQLException e) {
	       e.printStackTrace();
	   }
      }
	  return " { \"HeaderKey\" : "+jsonResult1+", \"DetailKey\" : "+jsonResult2+"}";
	  //return "<?xml version='1.0' encoding='UTF-8'?>"+xml;
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
	   //final String JDBC_DRIVER = "org.apache.hive.jdbc.HiveDriver";  
	   //final String DB_URL = "jdbc:hive2://RLD3HADOO03.RLLABINDIA.COM:21050/;auth=noSasl";
	   final String USER = "";
	   final String PASS = "";
	   
		  try {
			  Class.forName(JDBC_DRIVER);

			  conn = DriverManager.getConnection(DB_URL,USER,PASS);
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
		      SearchUtil util = new SearchUtil();
		      jsonResult1 = util.getJSONFromResultSet(rs5, "\"cocd\"");
		      jsonResult2 = util.getJSONFromResultSet(rs4, "\"plnt\"");
		      jsonResult3 = util.getJSONFromResultSet(rs1, "\"porg\"");
		      jsonResult4 = util.getJSONFromResultSet(rs2, "\"doctype\"");
		      jsonResult5 = util.getJSONFromResultSet(rs3, "\"payt\"");
		      
		      
		  } catch (ClassNotFoundException e) {
				e.printStackTrace();
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


@POST
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Path("/getE2EMF/")
	   public String getE2EMFMap(@FormParam("supplier") String supplier, @FormParam("customer") String customer, @FormParam("meterialid") String meterialid, @FormParam("tier1") String tier1, @FormParam("tier2") String tier2, @FormParam("type") String type) 
	{
	   List<FlowData> flowDataList;	  	
		
	  try
	{
		 MaterialFlowDAO mfDao = new MaterialFlowDAO();
		  
		List<String> vendorList = new ArrayList<String>();
		List<String> plantList = new ArrayList<String>();
		List<String> customerList = new ArrayList<String>();
		List<String> materialList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();
		
		
		if (meterialid != null && !"null".equalsIgnoreCase(meterialid) && !"".equalsIgnoreCase(meterialid))
		{	
			for(String mId : meterialid.split(","))
				materialList.add(mId); 
		}
		
		if (type != null && !"null".equalsIgnoreCase(type) && !"".equalsIgnoreCase(type))
		{	
			for(String typeName : type.split(","))
				typeList.add(typeName); 
		}
		
		if (supplier != null && !"null".equalsIgnoreCase(supplier) && !"".equalsIgnoreCase(supplier))
		{	for(String vId : supplier.split(","))
				vendorList.add(vId); 
		}
		
		if (customer != null && !"null".equalsIgnoreCase(customer) && !"".equalsIgnoreCase(customer))
		{	
			for(String cId : customer.split(","))
				customerList.add(cId);
		}
		
		if (tier1 != null && !"null".equalsIgnoreCase(tier1) && !"".equalsIgnoreCase(tier1))
		{	
			for(String pId : tier1.split(","))
				plantList.add(pId);
		}			
				
		//Only during default load get the top vendors and customers
		if (materialList.size() == 0 && plantList.size() == 0 && typeList.size() == 0 && customerList.size()==0 && vendorList.size() == 0)
		{
			//System.out.println("Get top vendors");
			//log.info("Get top vendors");
			//vendorList.addAll(mfDao.getVendorsList());
			
			//log.info("Get top customers");
			//customerList.addAll(mfDao.getCustomersList()); 
			
			flowDataList = (List<FlowData>) this.context.getAttribute("e2emfsankeyflowdata");
		} else{

			//System.out.println("Getting material flow data list...");
			flowDataList = mfDao.retrieveFlowDataList(vendorList, materialList, plantList, customerList, typeList);
		}	
		//System.out.println("Retrived material flow data list");
		log.info("Getting material flow data list...");
		log.info("Retrived material flow data list");
		MaterialFlow mf = new MaterialFlow();
		mf.deriveMaterialFlow(flowDataList);
		
		return mf.toD3SankeyJSON();
		
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
		
	}

	//Purchase order D3Sankey Implementation
	@GET
	@Produces(MediaType.APPLICATION_JSON)	
	@Path("/getE2emfpo/")	
	public String getE2EMFPoMap(){
		
		log.info("PurchaseOrder Service called");		
		String purchaseOrderJsonStr = null;
		List<FlowData> purchaseOrderflowDataList = null;
		MaterialFlowDAO mfDao = null;
		
		PurchaseOrderDAO purchaseOrderDao = new PurchaseOrderDAO();
		MaterialFlow mf = null;
		log.info("Fetching the DataList for Purchase Order" );
		List<String> companyCodeList = purchaseOrderDao.getCompanyCodeList();
		List<String> plantList = purchaseOrderDao.getPlantList();
		List<String> purchaseGroupList = purchaseOrderDao.getPurchaseGroupList();
		List<String> docTypeList = purchaseOrderDao.getDocTypeList();
		List<String> payTermList = purchaseOrderDao.getPayTermList();
				
		log.info("Fetching the FlowData List for Purchase Order...");
		purchaseOrderflowDataList = purchaseOrderDao.getPurchaseOrderFlowDataList(companyCodeList, plantList, purchaseGroupList, docTypeList, payTermList);
		
		try {
			mf = new MaterialFlow();
			log.info("Deriving the view for Purchase Order ...");
			mf.derivePOFlow(purchaseOrderflowDataList);
			log.info("Formatting PurchaseOrder Object to Json ...");
			purchaseOrderJsonStr =  mf.toD3SankeyJSON();
			log.debug("Formatted PurchaseOrder Json : \n" + purchaseOrderJsonStr);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return purchaseOrderJsonStr;
	}
	
	

}


