package com.startup;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;









import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.dao.MaterialFlowDAO;
import com.data.FlowData;
import com.util.SearchUtil;

public class E2EMFSankeyDataLoaderThread implements Runnable {
	
	private static Logger log = LogManager.getLogger(E2EMFSankeyDataLoaderThread.class);
	private ServletContext context = null;
		
	
	public E2EMFSankeyDataLoaderThread(ServletContext context) {
		this.context = context;
		
	}
	
	@Override
	public void run() {
		//while(true) {
			try {
				loadSankeyData();				
			} catch(IOException e){
				log.error(e.toString());
			} catch(Exception e){
				log.error(e.toString());
			}			
		//}
	}
		
	@SuppressWarnings({ "unused", "unused" })
	private void loadSankeyData() throws IOException {		
		
		List<FlowData> flowDataList = null;	
		
		List<String> vendorList = new ArrayList<String>();
		List<String> customerList = new ArrayList<String>();
		List<String> materialList = new ArrayList<String>();
		List<String> typeList = new ArrayList<String>();
		List<String> plantList = new ArrayList<String>();
		
		MaterialFlowDAO mfDao = new MaterialFlowDAO(); 
		
		//log.info("Get top vendors");
		//vendorList.addAll(mfDao.getVendorsList());
		
			
		//log.info("Get top customers");
		//customerList.addAll(mfDao.getCustomersList());
		
		try {
			flowDataList = mfDao.retrieveFlowDataListDefaultView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.setAttribute("e2emfsankeyflowdata", flowDataList);		
		//log.info(context.getAttribute("poprofiledata").toString());
	}
	
	
}
