package com.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;



public class Configurator {
	
	private Properties properties;
	private static Configurator configurator = null;

	public static Configurator getInstance() throws IOException{
		if(configurator == null){
			System.out.println("Creatin new instance of configurator.");
			configurator = new Configurator();
		}
		return configurator;
	}

	public Configurator() throws IOException{
		properties = new Properties();
		properties.load(Configurator.class.getClassLoader().getResourceAsStream("b2b.properties"));
	}


	public String getConnectionURL(){
		return getProptiesValue("CONNECTION_URL_HIVE");
	}

	public String getConnectionDriver(){
		return getProptiesValue("CONNECTION_DRIVER_HIVE");
	}
	
	public String getSolrServerURL(){
		return getProptiesValue("SOLR_SERVER_URL");
	}
	
	public String getSolrServerInstance(){
		return getProptiesValue("SOLR_SERVER_INSTANCE_NAME");
	}
	
	// PO Header related - EKKO
	
	public String getPOHeaderPurchDocField(){
		return getProptiesValue("POH1");
	}
	
	public String getPOHeaderVendorField(){
		return getProptiesValue("POH2");
	}
	
	
	// PO Line related - EKPO
	
	public String getPOPurchDocField(){
		return getProptiesValue("POD1");
	}
	
	/*public String getPOItemField(){
		return getProptiesValue("POItem");
	}*/
	
	public String getPOMaterialNumField(){
		return getProptiesValue("POD3");
	}
	
	public String getPOMaterialField(){
		return getProptiesValue("POD2");
	}
	
	/*public String getPOQtyField(){
		return getProptiesValue("POQty");
		
	}
	
	public String getPONetPriceField(){
		return getProptiesValue("PONetPrice");
	}*/
	
	
	// SO related
	
	public String getSOSalesDocField(){
		return getProptiesValue("SOSalesDoc");
	}
	
	public String getSOItemField(){
		return getProptiesValue("SOItem");
	}
	
	public String getSOMaterialNumField(){
		return getProptiesValue("SOMaterialNum");
	}
	
	public String getSOMaterialField(){
		return getProptiesValue("SOMaterial");
	}
	
	public String getSONetValueField(){
		return getProptiesValue("SONetValue");
	}
	
	public String getSOOrderQtyField(){
		return getProptiesValue("SOOrderQty");
	}
	
	
	// PREQ EBKN related fields
	
	public String getPRFirstField(){
		return getProptiesValue("PR1");
	}
	
	public String getPRSecondField(){
		return getProptiesValue("PR2");
	}
	
	public String getPRThirdField(){
		return getProptiesValue("PR3");
	}
	
	// PREQ EBAN related fields
	
		public String getPREBANFirstField(){
			return getProptiesValue("EBANPR1");
		}
		
		public String getPREBANSecondField(){
			return getProptiesValue("EBANPR2");
		}
		
		public String getPREBANThirdField(){
			return getProptiesValue("EBANPR3");
		}
		
		public String getPREBANFourthField(){
			return getProptiesValue("EBANPR4");
		}
		
	
	
	// KNA1 related fields
	
	public String getKNAFirstField(){
		return getProptiesValue("KNACust1");
	}
		
	public String getKNASecondField(){
		return getProptiesValue("KNACust2");
	}
		
	public String getKNAThirdField(){
		return getProptiesValue("KNACust3");
	}
		
	public String getKNAFourthField(){
		return getProptiesValue("KNACust4");
	}
		
	public String getKNAFifthField(){
		return getProptiesValue("KNACust5");
	}
		
	
	// KNB1 related fields
	
	public String getKNBFirstField(){
		return getProptiesValue("KNBCust1");
	}
			
	public String getKNBSecondField(){
		return getProptiesValue("KNBCust2");
	}
	
	// LFA1 related fields
	
	public String getLFAFirstField(){
		return getProptiesValue("LFA1");
	}
				
	public String getLFASecondField(){
		return getProptiesValue("LFA2");
	}

	public String getLFAThirdField(){
		return getProptiesValue("LFA3");
	}
				
	public String getLFAFourthField(){
		return getProptiesValue("LFA4");
	}
	
	public String getLFAFifthField(){
		return getProptiesValue("LFA5");
	}
	
	
	// MAKT related fields
	
	public String getMAKTFirstField(){
		return getProptiesValue("MAKT1");
	}
				
	public String getMAKTSecondField(){
		return getProptiesValue("MAKT2");
	}
	
	// MARA related fields
	
	public String getMARAFirstField(){
		return getProptiesValue("MARA1");
	}
					
	public String getMARASecondField(){
		return getProptiesValue("MARA2");
	}
	
	public String getMARAThirdField(){
		return getProptiesValue("MARA3");
	}
	
	// MARC related fields
	
	public String getMARCFirstField(){
		return getProptiesValue("MARC1");
	}
						
	public String getMARCSecondField(){
		return getProptiesValue("MARC2");
	}
		
		
	
	private String getProptiesValue(String key){
		return properties.getProperty(key);
	}
	
	
	public List<String> getPropertiesKey(){
		List<String> keysString = null;
		
		java.util.Set<Object> keys = properties.keySet();
		for(Object k:keys){            
			String key = (String)k;
            System.out.println(key+":"+getProptiesValue(key));
            //keysString.add(key+":"+getProptiesValue(key));            
        }
		 return keysString;		
	}

}
