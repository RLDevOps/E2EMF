package com.relevance.e2emf.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.relevance.e2emf.view.Link;
import com.relevance.e2emf.view.Node;

/**
 * @author emanuel
 * E2emf Application Utility Class  
 *
 */
public class E2emfAppUtil {
	
	public static final String propFile = "b2b.properties";
	public static Properties appProps = new Properties();
	
	public static String solarURL = null;
	public static String solarInstanceName = null;	
	
	
	//private ArrayList<Link> links;
	private int subContractingCount = 0;
	private int dropShipCount = 0;
	private int othersCount = 0;
	private int directSalesCount = 0;
	private int externalProcessingCount = 0;
	private int consigmentCount = 0;
	private int stoCount = 0;
	private int intercompanyDropShipCount = 0;
	
	private String subContractingColor = null;
	private String normalPurchasingColor = null;
	private String dropShipColor = null;
	private String directSalesColor = null;
	private String externalProcessColor = null;
	private String consigmentColor = null;
	private String stoColor = null;
	private String interCompDropShipColor =  null;	
	
	private static boolean isAppUtilInitialized = false;
	

	
	public E2emfAppUtil(){
		if(!Log.isLoggerInitialized)
		    Log.initialize();
		if(!isAppUtilInitialized)
			initialize();	
	}
	
	private void initialize() {
		Log.Info("Initializing Application Properties");
		try{
			appProps.load(E2emfAppUtil.class.getClassLoader().getResourceAsStream(propFile));
		//	appProps.load(this.getClass().getClassLoader().getSystemResourceAsStream(propFile));			
			Log.Info("Loaded AppProperties Successfully... ");
			solarURL = appProps.getProperty(E2emfConstants.solarUrl);
			solarInstanceName = appProps.getProperty(E2emfConstants.solarInstanceName);
			isAppUtilInitialized = true;
			Log.Info("Loaded AppProperties sucessfully... ");
			
		}catch(Exception e){
			Log.Error("Error Initializing App Properties...");
			Log.Error(e);
			//e.printStackTrace();
		
		
		Log.Error("Exception Loading App Properties" + e);	
		}	
	}
	
	public String getAppProperty(String key){		
		return appProps.getProperty(key,"");	
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

	
	
	public String getLookUpNodeConnector(String varX) {
		String selectedNode = null;
		String selectedNode1 = null;

		String[] splitStr = varX.split(",");
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
	
	
	public String toD3SankeyJSON(ArrayList<Link> links)
	{
		StringBuilder sbNode = new StringBuilder(); 
				
		ArrayList<Node> listNodes = new ArrayList<Node>();
		Map<String, ArrayList<Node>> nodeMap = getDistinctNodes(links);
		
		Log.Info("Nodes got to D2SakeyJson creator are : " + nodeMap.size());
		
		//1.For distinct nodes iterate nodeMap
		
		sbNode.append("{\"nodes\":[\n");
		boolean firstRec = true;
		for (Map.Entry<String, ArrayList<Node>> entry : nodeMap.entrySet()) 
		{
			//System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
			listNodes= entry.getValue();
			if (listNodes.size() > 0)
			{
				if (firstRec)
					firstRec = false;
				else
					sbNode.append(",");
				
				Node node = listNodes.get(0);
				
				sbNode.append("{");
				sbNode.append(getKeyValue("name", node.getCode()));
				sbNode.append(",");
				sbNode.append(getKeyValue("color", node.getColor()));
				sbNode.append(",");
				sbNode.append(getKeyValue("stack", ""+node.getStack()));
				sbNode.append(",");
				sbNode.append(getKeyValue("text", ""+node.getName()));
				sbNode.append("}\n");
			}			
		}
		sbNode.append("],");
		
		//2.For links iterate this.links
		sbNode.append("\n\"links\":[\n");
		firstRec = true;
		for(int x=0; x<links.size(); x++)
		{
			if (firstRec)
				firstRec = false;
			else
				sbNode.append(",");
			
			Link link = links.get(x);
			sbNode.append("{");
			sbNode.append(getKeyValue("source", link.getSourceNode().getCode()));
			sbNode.append(",");
			sbNode.append(getKeyValue("target", link.getTargetNode().getCode()));
			sbNode.append(",");
			sbNode.append(getKeyValue("color", link.getColor()));
			sbNode.append(",");
			sbNode.append(getKeyValue("value", ""+link.getValue()));
			sbNode.append(",");
			sbNode.append(getKeyValue("sourcename", link.getSourceNode().getName()));
			sbNode.append(",");
			sbNode.append(getKeyValue("targetname", link.getTargetNode().getName()));
			sbNode.append(",");
			if (link.getLinkSubType() != null)
				sbNode.append(getKeyValue("text", "" + link.getLinkSubType() + " - " + link.getLinkType()));
			else
				sbNode.append(getKeyValue("text", "" + link.getLinkType()));
			sbNode.append("}\n");
		}
		
		
		sbNode.append("],");
		sbNode.append("\n\"count\":\n");
		sbNode.append("{");
		sbNode.append("\"Sub Contracting\":");
		sbNode.append("{");
		sbNode.append(getKeyValue("count", String.valueOf(subContractingCount)));
		sbNode.append(",");		
		sbNode.append(getKeyValue("color", subContractingColor));
		sbNode.append("},");
		
		sbNode.append("\"External Processing\":");
		sbNode.append("{");
		sbNode.append(getKeyValue("count", String.valueOf(externalProcessingCount)));
		sbNode.append(",");		
		sbNode.append(getKeyValue("color", externalProcessColor));
		sbNode.append("},");
		
		sbNode.append("\"Normal Purchasing\":");
		sbNode.append("{");
		sbNode.append(getKeyValue("count", String.valueOf(othersCount)));
		sbNode.append(",");		
		sbNode.append(getKeyValue("color", normalPurchasingColor));
		sbNode.append("},");
		
		sbNode.append("\"Drop Ship\":");
		sbNode.append("{");
		sbNode.append(getKeyValue("count", String.valueOf(dropShipCount)));
		sbNode.append(",");		
		sbNode.append(getKeyValue("color", dropShipColor));
		sbNode.append("},");
		
		sbNode.append("\"Direct Sales\":");
		sbNode.append("{");
		sbNode.append(getKeyValue("count", String.valueOf(directSalesCount)));
		sbNode.append(",");		
		sbNode.append(getKeyValue("color", directSalesColor));
		sbNode.append("},");
		
		
		
		sbNode.append("\"Consigment\":");
		sbNode.append("{");
		sbNode.append(getKeyValue("count", String.valueOf(consigmentCount)));
		sbNode.append(",");		
		sbNode.append(getKeyValue("color", consigmentColor));
		sbNode.append("},");
		
		sbNode.append("\"STO\":");
		sbNode.append("{");
		sbNode.append(getKeyValue("count", String.valueOf(stoCount)));
		sbNode.append(",");		
		sbNode.append(getKeyValue("color", stoColor));
		sbNode.append("},");
		
		sbNode.append("\"Intercompany DropShip\":");
		sbNode.append("{");
		sbNode.append(getKeyValue("count", String.valueOf(intercompanyDropShipCount)));
		sbNode.append(",");		
		sbNode.append(getKeyValue("color", interCompDropShipColor));
		sbNode.append("}");
				
		sbNode.append("}\n");
		
		sbNode.append("}");		
		
		return sbNode.toString();
		
	}
	
	private Map<String, ArrayList<Node>> getDistinctNodes(ArrayList<Link> links) {
				
	     HashMap<String, ArrayList<Node>> nodeMap = new HashMap<String, ArrayList<Node>>();
			
			if (links != null)
			{
				for (Link link : links)
				{
					if (link.getSourceNode() != null && link.getSourceNode().getCode() != null)
					{
						addtoHashMap(nodeMap, link.getSourceNode());
					}
					if (link.getTargetNode() != null && link.getTargetNode().getCode() != null)
					{
						addtoHashMap(nodeMap, link.getTargetNode());
					}
				}
			}
			
			
			return nodeMap;
		}

	
	private void addtoHashMap(HashMap<String, ArrayList<Node>> nodeMap,
			Node node) {
		if (node != null && node.getCode() != null)
		{  
			if (nodeMap.containsKey(node.getCode()))
			{
			   nodeMap.get(node.getCode()).add(node);		
			
			}
			else
			{
				ArrayList<Node> nodeList = new ArrayList<Node>();			
				nodeList.add(node);				
				nodeMap.put(node.getCode(), nodeList);
			}
		}
		
	}

	
	public String getKeyValue(String key, String value)
	{
		String Q = "\"";
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(Q);
		sb.append(key);
		sb.append(Q);
		sb.append(":");
		sb.append(Q);
		sb.append(value);
		sb.append(Q);
		
		return sb.toString();
	}
	


	/*//moved from configurator class to here
	public List<String> getPropertiesKey(){
		List<String> keysString = null;
		
		java.util.Set<Object> keys = appProps.keySet();
		for(Object k:keys){            
			String key = (String)k;
            System.out.println(key+":"+getProptiesValue(key));
            //keysString.add(key+":"+getProptiesValue(key));            
        }
		 return keysString;		
	}*/
}
