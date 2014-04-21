package com.relevance.e2emf.commons;
/**
 * @author emanuel
 * E2emf Application constants  
 *
 */
public class E2emfConstants {
	
	   //DB Constants
		public static String hiveUrl = "CONNECTION_URL_HIVE";
		public static String hivedriverUrl = "CONNECTION_DRIVER_HIVE";
		public static String hiveUser = "HIVE_USER_NAME";
		public static String hivePwd =  "HIVE_USER_PWD";
		
		public static String solarUrl = "SOLR_SERVER_URL";
		public static String solarInstanceName = "SOLR_SERVER_INSTANCE_NAME";
		
		//SolarSearch Constants PurchaseOrder
		public static String POH1 = "POH1";
		public static String POH2 = "POH2";
		public static String POD1 = "POD1";
		public static String POD2 = "POD2";
		public static String POD3 = "POD3";
		
		public static String PONetPrice = "PONetPrice";
		public static String POQty = "POQty";
		public static String POItem = "POItem";
		
		
		//SolarSearch Constants SalesOrder
		public static String SOSalesDoc = "SOSalesDoc";
		public static String SOItem = "SOItem";
		
		public static String SOMaterialNum = "SOMaterialNum";
		public static String SOMaterial = "SOMaterial";
		public static String SONetValue = "SONetValue";
		public static String SOOrderQty = "SOOrderQty";
		
		
		//SolarSearch Constants PREQ EBKN related fields
		public static String PR1 = "PR1";
		public static String PR2 = "PR2";
		public static String PR3 = "PR3";
		
		
		//SolarSearch Constants PREQ EBAN related fields
		public static String EBANPR1 = "EBANPR1";
		public static String EBANPR2 = "EBANPR2";
		public static String EBANPR3 = "EBANPR3";
		public static String EBANPR4 = "EBANPR4";
		
		//SolarSearch Constants KNA1 related fields
		public static String KNACust1 = "KNACust1";
		public static String KNACust2 = "KNACust2";		
		public static String KNACust3 = "KNACust3";		
		public static String KNACust4 = "KNACust4";
		public static String KNACust5 = "KNACust5";
		
		//SolarSearch Constants KNB1 related fields
		public static String KNBCust1 = "KNBCust1";
		public static String KNBCust2 = "KNBCust2";
		
		//SolarSearch Constants LFA1 related fields
		public static String LFA1 = "LFA1";
		public static String LFA2 = "LFA2";
		public static String LFA3 = "LFA3";		
		public static String LFA4 = "LFA4";
		public static String LFA5 = "LFA5";
		
		//SolarSearch Constants MAKT related fields		
		public static String MAKT1 = "MAKT1";
		public static String MAKT2 = "MAKT2";
		
		//SolarSearch Constants MARA related fields
		public static String MARA1 = "MARA1";
		public static String MARA2 = "MARA2";
		public static String MARA3 = "MARA3";
		
		//SolarSearch Constants MARC related fields
		public static String MARC1 = "MARC1";
		public static String MARC2 = "MARC2";
		
		
		//Search Summary
		public static final String SEARCH_SUMMARY_EKKO_QUERY = "SEARCH_EKKO";
		public static final String SEARCH_SUMMARY_EKPO_QUERY = "SEARCH_EKKO";
		
		
		public static final String SUB_CONTRACTING = "SUB CONTRACTING";
		public static final String DROP_SHIP = "DROP SHIP";
		public static final String EXTERNAL_PROCESSING = "EXTERNAL PROCESSING";	
		public static final String NORMAL_PURCHASING = "NORMAL PURCHASING";
		public static final String CONSIGMENT= "CONSIGMENT";
		public static final String DIRECT_SALES= "DIRECT SALES";
		public static final String STO= "STO";
		public static final String INTERCOMPANY_DROPSHIP= "INTERCOMPANY DROPSHIP";
		
		public static final String EXTERNAL_VENDOR = "EXTERNAL VENDOR";
		public static final String INTERNAL_VENDOR = "J&J AFFILIATE";
		public static final String MF_PLANT = "MF_PLANT";
		public static final String DC_PLANT = "DC_PLANT";
		public static final String CUSTOMER = "CUSTOMER";
		
		//PurchaseOrder Constants
		public static final String COMPANY_CODE = "COMPANY_CODE";
		public static final String PLANT = "PLANT";
		public static final String PURCHASE_ORG = "MF_PLANT";
		public static final String DOC_TYPE = "DOC_TYPE";
		public static final String PAY_TERM = "PAY_TERM";
		
		public static final String COMPANYCODE_LIST = "COMPANYCODE_LIST";
		public static final String PLANT_LIST = "PLANT_LIST";
		public static final String PURCHASEORG_LIST = "PURCHASEORG_LIST";
		public static final String DOCTYPE_LIST = "DOCTYPE_LIST";
		public static final String PAYTERM_LIST = "PAYTERM_LIST";
		
		public static final String COMPANYCODE_PLANT_QUERY = "COMPANYCODE_PLANT";
		public static final String PLANT_PURCHASEORG_QUERY = "PLANT_PURCHASEORG";
		public static final String PURCHASEORG_DOCTYPE_QUERY = "PURCHASEORG_DOCTYPE";
		public static final String DOCTYPE_PAYTERM_QUERY = "DOCTYPE_PAYTERM";
		
		public static final String COMPANYCODE_PLANT_FLOWLIST = "COMPANYCODE_PLANT";
		public static final String PLANT_PURCHASEORG_FLOWLIST = "PLANT_PURCHASEORG";
		public static final String PURCHASEORG_DOCTYPE_FLOWLIST = "PURCHASEORG_DOCTYPE";
		public static final String DOCTYPE_PAYTERM_FLOWLIST = "DOCTYPE_PAYTERM";
		
		public static final String FROM_COMPANYCODE = "COMPANYCODE";
		public static final String FROM_PLANT = "PLANT";
		public static final String FROM_PURCHASEORG = "PURCHASEORG";
		public static final String FROM_DOCTYPE = "DOCTYPE";
		public static final String FROM_PAYTERM = "PAYTERM";
		
		
		public static final String TO_PLANT = "PLANT";
		public static final String TO_PURCHASEORG = "PURCHASEORG";
		public static final String TO_DOCTYPE = "DOCTYPE";
		public static final String TO_PAYTERM = "PAYTERM";
		
		
		public static final String COMPANYCODE_PLANT_LINKTYPE = "COMPANYCODE_TO_PLANT";
		public static final String PLANT_PURCHASEORG_LINKTYPE = "PLANT_TO_PURCHASEORG";
		public static final String PURCHASEORG_DOCTYPE_LINKTYPE = "PURCHASEORG_TO_DOCTYPE";
		public static final String DOCTYPE_PAYTERM_LINKTYPE = "DOCTYPE_TO_PAYTERM";
		
		
		
}
