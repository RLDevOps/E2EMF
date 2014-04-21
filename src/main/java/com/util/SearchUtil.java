package com.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.simple.JSONObject;

public class SearchUtil {
	
public synchronized String getJSONFromResultSetWithCol(ResultSet rs,String keyName) {
    
	Map json = new HashMap(); 
    List list = new ArrayList();
    
    if(rs!=null)
    {
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            while(rs.next())
            {
                //Map<String,Object> columnMap = new HashMap<String, Object>();
            	List dataList = new ArrayList<Object>();
            	String nodeList = "";
            	int columnIndex;
                for(columnIndex=1;columnIndex<=metaData.getColumnCount();columnIndex++)
                {
                    String dataVal ="" ;
                    //Based on conv with Dinil - Impala will send None or "" only for non values/null values
                    String colValue = "";
                    if (rs.getString(metaData.getColumnName(columnIndex)) != null){
                    	colValue = rs.getString(metaData.getColumnName(columnIndex));
                    }
                    if(colValue !=null || colValue.equalsIgnoreCase("None") || colValue.equals(" ")) {
                        //columnMap.put(metaData.getColumnLabel(columnIndex),     rs.getString(metaData.getColumnName(columnIndex)));
                    	if(columnIndex == 2){
                    		dataVal = rs.getString(metaData.getColumnName(columnIndex));
                    	}else if(columnIndex == 1 && metaData.getColumnName(columnIndex).equalsIgnoreCase("bukrs")){
                    		dataVal = "\"" + "C-"+rs.getString(metaData.getColumnName(columnIndex)) + "\"";
                    	}
                    	 else {                    		
                    		dataVal = "\"" + rs.getString(metaData.getColumnName(columnIndex)) + "\"";                    		
                    	}
                    	dataList.add(dataVal);
                    	//nodeList.add(rs.getString(metaData.getColumnName(columnIndex)));
                    	//nodeList = rs.getString(metaData.getColumnName(columnIndex));
					} 
//						else {
//	                        //columnMap.put(metaData.getColumnLabel(columnIndex), "");
//	                    	dataList.add(dataVal);
//	                    	nodeList ="";
//	                }
                //list.add(columnMap);
                //list.add(nodeList);
				//list.add(dataList);
                
            }
                list.add(dataList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //json.put(keyName, list);
     }
     return list.toString();
    //return json.toString().replace("=", ":");
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
            e.printStackTrace();
        }
        json.put(keyName, list);
     }
    // return JSONValue.toJSONString(json);
     return json.toString().replace("=", ":");
    //System.out.println("The value is "+ json.toString().replace("=", ":"));
    //return json.toString();
	}

public static String convert( ResultSet rs )
	    throws SQLException
	  {
	
	    JSONArray json = new JSONArray();
	    ResultSetMetaData rsmd = rs.getMetaData();
	   
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
      // System.out.println(json.toString());
	    return json.toString();
	  }

public static String getLookUpNode(String varx){
	
		
	String selectedNode = null;
	String[] splitStr = varx.split("\\s");
	if(splitStr[0].equalsIgnoreCase("0")) {
	  selectedNode = "bukrs";		
	} else if(splitStr[0].equalsIgnoreCase("1")){
	  selectedNode = "werks";	
	} else if(splitStr[0].equalsIgnoreCase("2")){
		selectedNode = "ekorggrp";	
	} else if(splitStr[0].equalsIgnoreCase("3")){
		selectedNode = "bsart";
	} else if(splitStr[0].equalsIgnoreCase("4")){
		selectedNode = "zterm";
	} 
	
	return selectedNode+" "+splitStr[1];
	
}

public static String getLookUpNodeConnector(String varx){

	String selectedNode = null;
	String selectedNode1 = null;

	String[] splitStr = varx.split(",");
	String firstSet = splitStr[0];
	String secondSet = splitStr[1];

	String[] splitStrFirstSet = firstSet.split("\\s");	

	if(splitStrFirstSet[0].equalsIgnoreCase("0")) {
		selectedNode = "bukrs";		
	} else if(splitStrFirstSet[0].equalsIgnoreCase("1")){
		selectedNode = "werks";	
	} else if(splitStrFirstSet[0].equalsIgnoreCase("2")){
		selectedNode = "ekorggrp";	
	} else if(splitStrFirstSet[0].equalsIgnoreCase("3")){
		selectedNode = "bsart";
	} else if(splitStrFirstSet[0].equalsIgnoreCase("4")){
		selectedNode = "zterm";
	}

	String[] splitStrSecondSet = secondSet.split("\\s");

	if(splitStrSecondSet[0].equalsIgnoreCase("0")) {
		selectedNode1 = "bukrs";		
	} else if(splitStrSecondSet[0].equalsIgnoreCase("1")){
		selectedNode1 = "werks";	
	} else if(splitStrSecondSet[0].equalsIgnoreCase("2")){
		selectedNode1 = "ekorggrp";	
	} else if(splitStrSecondSet[0].equalsIgnoreCase("3")){
		selectedNode1 = "bsart";
	} else if(splitStrSecondSet[0].equalsIgnoreCase("4")){
		selectedNode1 = "zterm";
	}

	return selectedNode+" "+splitStrFirstSet[1]+","+selectedNode1+" "+splitStrSecondSet[1];

}

 
//preparing data into CSV format
public synchronized String getCSVFromResultSet(ResultSet rs,String profileType) {
	
	String dropShipTitle = "";
	String dropShipHead1="";
	String dropShipData="";
	int count=0; 
	if(profileType.equalsIgnoreCase("poprofile")){
	    dropShipTitle="po_count,companycode,purchaseorg,purchasegroup,plantgroup,orderquantity,netprice,grossordervalue,source,lastchangedon,material_type,vendor_type,po_type";
	} else if(profileType.equalsIgnoreCase("soprofile")){
		dropShipTitle="so_count,companycode,salesorg,salesgroup,division,netvalue,salesdoctype,plantgroup,materialgroup,lastchangedon,material_type,so_type";	
	}
		
	
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
					if(colValue !=null || colValue.equalsIgnoreCase("None") || colValue.equals(" ")) {  
						colValue = colValue + rs.getString(metaData.getColumnName(columnIndex))+",";
					}
				}
				colValue = colValue.substring(0, colValue.length() - 1);
				dropShipData =colValue +"\n"+ dropShipData;
					}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
		
	return dropShipTitle+"\n"+ dropShipData;
	
}




}
