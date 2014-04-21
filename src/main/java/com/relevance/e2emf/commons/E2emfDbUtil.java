package com.relevance.e2emf.commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONArray;


/**
 * @author emanuel
 * E2emf Database Utiity Class  
 *
 */
public class E2emfDbUtil {
	
	public static String hiveURL = null;
	public static String hivedriverURL = null;
	public static String hiveUser = null;
	public static String hivePwd = null;

	private static Connection hiveConn = null;
	private static E2emfAppUtil appUtil = null;
	
	public static boolean isDbUtilInitialized = false;
	
	public E2emfDbUtil(){
		Log.Info("initializing AppUtils and DbUtils...");
		appUtil = new E2emfAppUtil();
		
		if(!isDbUtilInitialized)
			initialize();
		
	}
	
	//SearchQueries
	String firstLevelNodeSelectQuery = "select ebeln, ekorg, ekgrp, lifnr, bsart, zterm, werks, bukrs, netwr, wkurs, menge, meins from jandj.poproc26 ";
	//String 	secondLevelNodeSelectQuery = "select ebeln, ekorg, ekgrp, lifnr, bsart, zterm, werks, bukrs, netwr, wkurs, menge, meins from jandj.poproc26 ";
	
	String ekkoPurchaseDocSelectQuery = "select * from ekkotabHB ";
    String ekpoPurchaseDocSelectQuery = "select * from ekpotabHB ";


	public void initialize() {
		hivedriverURL = appUtil.getAppProperty(E2emfConstants.hivedriverUrl);
		hiveURL = appUtil.getAppProperty(E2emfConstants.hiveUrl);		
		hiveUser =  appUtil.getAppProperty(E2emfConstants.hiveUser);
		hivePwd =  appUtil.getAppProperty(E2emfConstants.hivePwd);		
		hiveConn = getHiveConnection();
		isDbUtilInitialized = true;
		Log.Info(" DriverURL: " + hivedriverURL + "\n hiveURL: " + hiveURL + "\n hiveUser: " + hiveUser + "\n hivePwd: " + hivePwd + "\n hiveConnection : " + hiveConn);
	}
	
	
	public Connection getHiveConnection() {
		try {
			Class.forName(hivedriverURL);
			
				if(hiveConn == null){
					hiveConn = DriverManager.getConnection(hiveURL, "", "");
					Log.Info("Established Hive Connection : " + hiveConn);
					return hiveConn;
				}
				else{
					return hiveConn;
				}
				
			
		}catch (SQLException e) {
			Log.Error("SQL Exception trying to get connection " + e.getStackTrace());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			Log.Error("ClassNotFoundException trying to get connection " + e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {			
			Log.Error("Exception trying to get connection " + e.getMessage());
			e.printStackTrace();
		}
		
		return hiveConn;
	}
	
	
	
	public String getCommaSeperatedInClauseValues(List<String> valueList) {
		StringBuilder inValues = new StringBuilder();

		if (valueList.size() > 0) {
			for (int i = 0; i < valueList.size(); i++) {
				inValues.append("'").append(valueList.get(i)).append("'");
				if (i < valueList.size() - 1) {
					inValues.append(", ");
				}
			}

		}
		return inValues.toString();
	}
	
	
	public String buildFirstLevelNodeQuery(String nodeName, String finalNodeVal){
		StringBuilder where = new StringBuilder(" where ");		
		
		where.append(nodeName).append(" = '" ).append(finalNodeVal).append("' limit 100");
		
		StringBuilder firstLeveNodeQuery = new StringBuilder(firstLevelNodeSelectQuery).append(where);
		
		return firstLeveNodeQuery.toString();
		
	}

	
	public String buildSecondLevelNodeQuery(String firstNodeName,
			String finalNodeVal, String secondNodeName, String secondNodeValue) {
		StringBuilder where = new StringBuilder(" where ");		
		
		where.append(firstNodeName).append(" = '" ).append(finalNodeVal).append("' and ");
		where.append(secondNodeName).append(" = '").append(secondNodeValue).append("' limit 100");
		
		StringBuilder secondLevelNodeQuery = new StringBuilder(firstLevelNodeSelectQuery).append(where);
		return secondLevelNodeQuery.toString();
		
	}

	//to be implemented for E2emf secondLevelData Populatation fro Canada flow
	public Map<String, String> buildSearchSummaryE2emfQuery(String purchaseDoc) {
		Map<String, String> searchSummaryQueryMap = new HashMap<String, String>();
		
		
		return searchSummaryQueryMap;
		
	}

	public Map<String, String> buildSearchSummaryQuery(String purchaseDoc) {
		
		Map<String, String> searchSummaryQueryMap = new HashMap<String, String>();
		
		StringBuilder where = new StringBuilder(" where ");		
		
		where.append("purchdoc = '").append(purchaseDoc).append("'");
		
		StringBuilder ekkoSearchQuery = new StringBuilder(ekkoPurchaseDocSelectQuery).append(where);
		StringBuilder ekpoSearchQuery = new StringBuilder(ekpoPurchaseDocSelectQuery).append(where);
		
		
		searchSummaryQueryMap.put(
				E2emfConstants.SEARCH_SUMMARY_EKKO_QUERY,
				ekkoSearchQuery.toString());
		searchSummaryQueryMap.put(
				E2emfConstants.SEARCH_SUMMARY_EKPO_QUERY,
				ekpoSearchQuery.toString());
		
		return searchSummaryQueryMap;
		
	}

	
public String buildMasterSearchQuery(String param, String searchStr) {
		
		String masterSearchQuery = null;
		 if (param.equals("V"))
		  {
			  //query1 = "select lifnr as id, NAME1 as name from jandj.e2e_lfa1";
			  //select lifnr as id, NAME1 as name from jandj.e2e_lfa1 where NAME1 like 'AIG %'	
			  //query1 = "select lifnr as id, NAME1 as name from jandj.e2e_lfa1 where lifnr in ('0002000352','0002000145','0002000350','0002000351','0002000462','0002000066') and NAME1 like '"+ searchStr +"%'";		//limit 100	  
			  //query1 = "select lifnr as id, NAME1 as name from jandj.e2e_lfa1 where NAME1 RLIKE'("+ searchStr +"|lcase("+searchStr +")).*' or lifnr like '"+ searchStr +"%' limit 30";		//limit 100
			 masterSearchQuery = "select lifnr as id, NAME1 as name from jandj.e2e_lfa1 where NAME1 like '%"+ searchStr +"%' or NAME1 like lcase('%"+ searchStr +"%') or NAME1 like ucase('%"+ searchStr +"%') or lifnr like '%"+ searchStr +"%' limit 30";		//limit 100
		  }
		  else if(param.equals("C"))
		  {
			  //query1 = "select kunnr as id,NAME1 as name from jandj.KNA1";
			  masterSearchQuery = "select kunnr as id, NAME1 as name from jandj.E2e_kna1 where NAME1 like '%"+ searchStr +"%' or NAME1 like lcase('%"+ searchStr +"%') or NAME1 like ucase('%"+ searchStr +"%') or kunnr like '%"+ searchStr +"%'  limit 30";//limit 100
		  }
		  else if(param.equals("M"))
		  {
			  //query1 = "select matnr as id, maktx as name from jandj.makt";
			  masterSearchQuery = "select matnr as id, maktx as name from jandj.E2e_makt where maktx like '%"+ searchStr +"%' or maktx like lcase('%"+ searchStr +"%') or maktx like ucase('%"+ searchStr +"%') or matnr like '%"+ searchStr +"%' limit 30";
		  }
		  else if(param.equals("P"))
		  {
			  masterSearchQuery = "select werks as id, name1 as name from JandJ.e2e_T001W where name1 like '%"+ searchStr +"%' or name1 like lcase('%"+ searchStr +"%') or name1 like ucase('%"+ searchStr +"%') or werks like '%"+ searchStr +"%' limit 30";
		  }
		 
		 Log.Info("MasterSearch Query built is \n" + masterSearchQuery);
		 
		return masterSearchQuery;
	}


	public String convert(ResultSet rs) throws SQLException {
		JSONArray json = new JSONArray();
	    ResultSetMetaData rsmd = rs.getMetaData();
	        
	      try{
	   
	    	  while(rs.next()) {
	    	      int numColumns = rsmd.getColumnCount();
	    	      
	    	      
	    	      JSONObject obj = new JSONObject();

	    	      for (int i=1; i<numColumns+1; i++) {
	    	        String column_name = rsmd.getColumnName(i);

	    	       if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
	    	         obj.put(column_name, rs.getFloat(column_name));
	    	        }
	    	        else {
	    	         obj.put(column_name, rs.getString(column_name));
	    	        }
	    	        
	    	      }

	    	      json.put(obj);
	    	    }
	    	  
	      }catch(Exception e){
	    	  e.printStackTrace();
	      }	      
  
	    return json.toString();
		
	}
	
	
	    //preparing data into CSV format
		public synchronized String getCSVFromResultSet(ResultSet rs) {
			
			String dropShipTitle="po_count,companycode,purchaseorg,purchasegroup,plantgroup,orderquantity,netprice,grossordervalue,source,lastchangedon,material_type,vendor_type,po_type";
			String dropShipHead1="";
			String dropShipData="";
			int count=0; 
			if(rs!=null)
			{
				try {
					ResultSetMetaData metaData = rs.getMetaData();
					while(rs.next())
					{                
						String nodeString = "";
						int columnIndex;

						String colValue = "";
						for(columnIndex = 1;columnIndex <= metaData.getColumnCount();columnIndex++)
						{
							// Preparing Header with "," seperated values.
						/*	if(count <= columnIndex)
							{   
								if(count == metaData.getColumnCount()-1)
								{	count=count+1;
								dropShipHead= dropShipHead + metaData.getColumnName(columnIndex);
								}
								else if(count < columnIndex){
									count=count+1;
									dropShipHead= dropShipHead + metaData.getColumnName(columnIndex)+",";
								}
							} */
							// Binding data with "," seperated values.
							if(colValue != null || colValue.equalsIgnoreCase("None") || colValue.equals(" ")) {  
								colValue = colValue + rs.getString(metaData.getColumnName(columnIndex))+",";
							}
						}
						colValue = colValue.substring(0, colValue.length() - 1);
						dropShipData =colValue +"\n"+ dropShipData;
							}
				} catch (SQLException e) {
					Log.Error("SQLException in creating DropShipData " + e.getStackTrace());
					e.printStackTrace();
				}
				
			}
				
			return dropShipTitle+"\n"+ dropShipData;
			
		}

		
		
		public synchronized String getJSONFromResultSet(ResultSet rs,String keyName) {

		    Map json = new Hashtable<String, List>(); 
		    List list = new ArrayList<String>();
		    int i =0;
		    
		    if(rs!=null)
		    {
		        try {
		            ResultSetMetaData metaData = rs.getMetaData();
		            while(rs.next())
		            {
		                
		            	String nodeString = "";
		            	int columnIndex;
		                for(columnIndex=1;columnIndex<=metaData.getColumnCount();columnIndex++)
		                {
		                	//Based on conv with Dinil - Impala will send None or "" only for non values/null values
		                	String colValue = "";
		                	if (rs.getString(metaData.getColumnName(columnIndex)) != null){
		                		colValue = rs.getString(metaData.getColumnName(columnIndex));
		                	}
		                    if(colValue !=null || colValue.equalsIgnoreCase("None") || colValue.equals(" ")) {                    	
		                    
		                    	if(keyName.equalsIgnoreCase("\"0\"")){
		                    		nodeString = "\"" + "C-"+rs.getString(metaData.getColumnName(columnIndex)) + "\"";
		                    	} else {
		                    		nodeString = "\"" + rs.getString(metaData.getColumnName(columnIndex)) + "\"";
		                    	}                                       	

		                    	list.add(nodeString);
		                    }
		                }                
		            }
		        } catch (SQLException e) {
		        	Log.Error("SQLException in creating Json from ResultSet " + e.getStackTrace());
		            e.printStackTrace();
		        }
		        json.put(keyName, list);
		     }
		    // return JSONValue.toJSONString(json);
		     return json.toString().replace("=", ":");
		    //System.out.println("The value is "+ json.toString().replace("=", ":"));
		    //return json.toString();
			}	
		
		
	public void releaseConnection(Connection con) {
		//Add to the Pool of Connection
		
	}


	/**
	 * @param customerIdList
	 * @return
	 */
	public String getCSV(List<String> inputList) {
		StringBuilder retValue = new StringBuilder("");
		boolean isFirstRec = true;
		for (String Id : inputList)
		{
			if (isFirstRec)
				isFirstRec = false;
			else
				retValue.append(",");
			retValue.append("'" + Id + "'");
		}
		
		return retValue.toString();
	}


}
