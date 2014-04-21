package com.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.util.Configurator;
import com.util.SearchUtil;

public class POProfilingDAO {
	
	private static Logger log = LogManager.getLogger(POProfilingDAO.class);
	private Configurator props = null;
	private String JDBC_DRIVER = null;
	private String DB_URL = null;
	Connection conn = null;

	public POProfilingDAO() throws IOException{
		props = Configurator.getInstance();
		JDBC_DRIVER = props.getConnectionDriver();
		DB_URL = props.getConnectionURL();		
	}
	
	public String retrievePOProfileData(){
		
		Statement stmt1 = null;
		String sqlquery1 = null;
		String data = "";
		SearchUtil util = new SearchUtil();
		
		try {

			Class.forName(JDBC_DRIVER);
			//conn = DriverManager.getConnection("jdbc:hive2://192.168.101.56:21050/;auth=noSasl", "", "");
			conn = DriverManager.getConnection(DB_URL, "", "");
			stmt1 = conn.createStatement();
			//sqlquery1 = "select po_count,bukrs as CompanyCode,ekorg as PurchaseOrg,ekgrp as PurchaseGroup,matnr as MaterialId,werks as PlantGroup,matkl as MaterialGroup,menge as OrderQuantity,netpr as NetPrice,brtwr as GrossOrderValue,lifnr as vendor,source,aedat as LastChangedOn,material_type,vendor_type,po_type from JANDJ.E2E_AG_PO_PROF";
			 sqlquery1 = "select ebeln_count as po_count,bukrs as CompanyCode,ekorg as PurchaseOrg,ekgrp as PurchaseGroup,werks as PlantGroup,menge as OrderQuantity,netpr as NetPrice,brtwr as GrossOrderValue,source,aedat as LastChangedOn,material_type,vendor_type, case when po_type = 'OTHERS' then 'NORMAL PURCHASING' ELSE po_type end as po_type from JANDJ.e2e_dn_poprofile_red_view";
			ResultSet rs1 = stmt1.executeQuery(sqlquery1);
			data = util.getCSVFromResultSet(rs1,"poprofile");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				stmt1.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}				
		
		return data;
	}
	
	public String retrieveSOProfileData(){
		
		Statement stmt1 = null;
		String sqlquery1 = null;
		String data = "";
		SearchUtil util = new SearchUtil();
		
		try {

			Class.forName(JDBC_DRIVER);
			//conn = DriverManager.getConnection("jdbc:hive2://192.168.101.56:21050/;auth=noSasl", "", "");
			conn = DriverManager.getConnection(DB_URL, "", "");
			stmt1 = conn.createStatement();
			//sqlquery1 = "select po_count,bukrs as CompanyCode,ekorg as PurchaseOrg,ekgrp as PurchaseGroup,matnr as MaterialId,werks as PlantGroup,matkl as MaterialGroup,menge as OrderQuantity,netpr as NetPrice,brtwr as GrossOrderValue,lifnr as vendor,source,aedat as LastChangedOn,material_type,vendor_type,po_type from JANDJ.E2E_AG_PO_PROF";
			 sqlquery1 = "select vbeln_count as so_count,bukrs as CompanyCode,vkorg as SalesOrg,vkgrp as SalesGroup,spart as Division,vbap_netwr as netvalue,auart as SalesDocType,werks as PlantGroup,matkl as MaterialGroup,aedat as LastChangedOn,mat_type as MaterialType,so_type from JANDJ.e2e_ag_soprofnewred";
			ResultSet rs1 = stmt1.executeQuery(sqlquery1);
			data = util.getCSVFromResultSet(rs1,"soprofile");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				stmt1.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}				
		
		return data;
	}

}
