package com.dao;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.data.MasterData;
import com.util.Configurator;

public class MasterDataDAO {
	private Configurator props = null;
	private String JDBC_DRIVER = null;
	private String DB_URL = null;
	public MasterDataDAO() throws IOException{
		props = Configurator.getInstance();
		JDBC_DRIVER = props.getConnectionDriver();
		DB_URL = props.getConnectionURL(); 		
	}
	public ArrayList<MasterData> retrieveMasterData(String param,String searchStr) throws SQLException {
		  Connection conn = null;
		  Statement stmt1 = null;
		  ArrayList<MasterData> MasterDataList = new ArrayList<MasterData>();
		  try {
			  Class.forName(JDBC_DRIVER);
			  conn = DriverManager.getConnection(DB_URL,"","");
			  stmt1 = conn.createStatement();
		    } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  String query1="";
		  if (param.equals("V"))
		  {
			  //query1 = "select lifnr as id, NAME1 as name from jandj.e2e_lfa1";
			  //select lifnr as id, NAME1 as name from jandj.e2e_lfa1 where NAME1 like 'AIG %'	
			  //query1 = "select lifnr as id, NAME1 as name from jandj.e2e_lfa1 where lifnr in ('0002000352','0002000145','0002000350','0002000351','0002000462','0002000066') and NAME1 like '"+ searchStr +"%'";		//limit 100	  
			  //query1 = "select lifnr as id, NAME1 as name from jandj.e2e_lfa1 where NAME1 RLIKE'("+ searchStr +"|lcase("+searchStr +")).*' or lifnr like '"+ searchStr +"%' limit 30";		//limit 100
			  query1 = "select lifnr as id, NAME1 as name from jandj.e2e_lfa1 where NAME1 like '%"+ searchStr +"%' or NAME1 like lcase('%"+ searchStr +"%') or NAME1 like ucase('%"+ searchStr +"%') or lifnr like '%"+ searchStr +"%' limit 30";		//limit 100
		  }
		  else if(param.equals("C"))
		  {
			  //query1 = "select kunnr as id,NAME1 as name from jandj.KNA1";
			  query1 = "select kunnr as id, NAME1 as name from jandj.E2e_kna1 where NAME1 like '%"+ searchStr +"%' or NAME1 like lcase('%"+ searchStr +"%') or NAME1 like ucase('%"+ searchStr +"%') or kunnr like '%"+ searchStr +"%'  limit 30";//limit 100
		  }
		  else if(param.equals("M"))
		  {
			  //query1 = "select matnr as id, maktx as name from jandj.makt";
			  query1 = "select matnr as id, maktx as name from jandj.E2e_makt where maktx like '%"+ searchStr +"%' or maktx like lcase('%"+ searchStr +"%') or maktx like ucase('%"+ searchStr +"%') or matnr like '%"+ searchStr +"%' limit 30";
		  }
		  else if(param.equals("P"))
		  {
			  query1 = "select werks as id, name1 as name from JandJ.e2e_T001W where name1 like '%"+ searchStr +"%' or name1 like lcase('%"+ searchStr +"%') or name1 like ucase('%"+ searchStr +"%') or werks like '%"+ searchStr +"%' limit 30";
		  }
		  
	        ResultSet rs1 = stmt1.executeQuery(query1); 
	        if(rs1 != null) {        	
	        	while(rs1.next()){
	        		MasterData masterdata=new MasterData();
	        		masterdata.setName(rs1.getString("name"));
	        		masterdata.setId(rs1.getString("id"));
	        		MasterDataList.add(masterdata);
	        	}        	
	        } 
	        else{
	        	System.out.println("Resultset is null");
	        } 
	     /*   if(MasterDataList.size()==0){
	        	MasterData masterdata1=new MasterData();
	        	MasterDataList.add(masterdata1);
	        }*/
		   return MasterDataList; 
		  
	   }
}
