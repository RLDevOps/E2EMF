package com.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.data.FlowData;
import com.startup.E2EMFDataLoaderThread;
import com.util.E2EMFConstants;
import com.util.JSONUtil;

public class MaterialFlow extends E2EMFConstants
{

public MaterialFlow(){
	System.out.println("Testing");
}	
	private static Logger log = LogManager.getLogger(MaterialFlow.class);
	private ArrayList<Link> links;
	private int subContractingCount = 0;
	private int dropShipCount = 0;
	private int othersCount = 0;
	private int directSalesCount = 0;
	private int externalProcessingCount = 0;
	private int consigmentCount = 0;
	private int stoCount = 0;
	private int intercompanyDropShipCount = 0;
	private int;
	
	private String subContractingColor = null;
	private String normalPurchasingColor = null;
	private String dropShipColor = null;
	private String directSalesColor = null;
	private String externalProcessColor = null;
	private String consigmentColor = null;
	private String stoColor = null;
	private String interCompDropShipColor =  null;
	
	public void deriveMaterialFlow(List<FlowData> flowDataList)
	{
		
		
		this.links = new ArrayList<Link>();
		for(FlowData flowData : flowDataList)
		{
			Link link = new Link();
			Node startNode = new Node();
			startNode.setCode(flowData.getFromCode());
			startNode.setName(flowData.getFromName());
			/*if (startNode.getName() != null && startNode.getName().length() > 10)
			{
				startNode.setName(startNode.getCode() + "" + startNode.getName());//.substring(0, 9));
			}
			else
			{
				startNode.setName(startNode.getCode() + "" + startNode.getName());
			}*/
			
			
			startNode.setText(flowData.getFromText());
			startNode.setType(flowData.getFromType());			
			setStackValue(startNode);			
			setNodeColor(startNode);			
			
			Node endNode = new Node();
			endNode.setCode(flowData.getToCode());
			endNode.setName(flowData.getToName());
			/*if (endNode.getName() != null && endNode.getName().length() > 10)
			{
				endNode.setName(endNode.getCode() + "-" + endNode.getName().substring(0, 9));
			}
			else
			{
				endNode.setName(endNode.getCode() + "-" + endNode.getName());
			}*/
			endNode.setText(flowData.getToText());
			endNode.setType(flowData.getToType());
			setStackValue(endNode);
			setNodeColor(endNode);
			
					
			link.setName(startNode.getName() + " To " + endNode.getName());
			link.setValue(flowData.getLinkValue());
			link.setStartNode(startNode);
			link.setEndNode(endNode);
			link.setType(flowData.getLinkType());
			link.setSubType(flowData.getLinkSubType());
			setLinkColor(link);
			
			this.links.add(link);
			
		}		
	}
	
	public void derivePOFlow(List<FlowData> flowDataList)
	{
				
		this.links = new ArrayList<Link>();
		for(FlowData flowData : flowDataList)
		{
			Link link = new Link();
			Node startNode = new Node();
			startNode.setCode(flowData.getFromCode());
			startNode.setName(flowData.getFromName());			
			startNode.setText(flowData.getFromText());
			startNode.setType(flowData.getFromType());	
			
			setPOStackValue(startNode);			
			setPOFlowNodeColor(startNode);			
			
			Node endNode = new Node();
			endNode.setCode(flowData.getToCode());
			endNode.setName(flowData.getToName());			
			endNode.setText(flowData.getToText());
			endNode.setType(flowData.getToType());
			setPOStackValue(endNode);	
			setPOFlowNodeColor(endNode);
			
					
			link.setName(startNode.getName() + " To " + endNode.getName());
			link.setValue(flowData.getLinkValue());
			link.setStartNode(startNode);
			link.setEndNode(endNode);
			link.setType(flowData.getLinkType());
			link.setSubType(flowData.getLinkSubType());
			setPOLinkColor(link);
			
			this.links.add(link);
			
		}		
	}
	
	
	public String toD3SankeyJSON()
	{
		StringBuilder sbNode = new StringBuilder(); 
				
		ArrayList<Node> listNodes = new ArrayList<Node>();
		Map<String, ArrayList<Node>> nodeMap = this.getDistinctNodes();
		
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
				sbNode.append(JSONUtil.getKeyValue("name", node.getCode()));
				sbNode.append(",");
				sbNode.append(JSONUtil.getKeyValue("color", node.getColor()));
				sbNode.append(",");
				sbNode.append(JSONUtil.getKeyValue("stack", ""+node.getStack()));
				sbNode.append(",");
				sbNode.append(JSONUtil.getKeyValue("text", ""+node.getName()));
				sbNode.append("}\n");
			}			
		}
		sbNode.append("],");
		
		//2.For links iterate this.links
		sbNode.append("\n\"links\":[\n");
		firstRec = true;
		for(int x=0; x<this.links.size(); x++)
		{
			if (firstRec)
				firstRec = false;
			else
				sbNode.append(",");
			
			Link link = this.links.get(x);
			sbNode.append("{");
			sbNode.append(JSONUtil.getKeyValue("source", link.getStartNode().getCode()));
			sbNode.append(",");
			sbNode.append(JSONUtil.getKeyValue("target", link.getEndNode().getCode()));
			sbNode.append(",");
			sbNode.append(JSONUtil.getKeyValue("color", link.getColor()));
			sbNode.append(",");
			sbNode.append(JSONUtil.getKeyValue("value", ""+link.getValue()));
			sbNode.append(",");
			sbNode.append(JSONUtil.getKeyValue("sourcename", link.getStartNode().getName()));
			sbNode.append(",");
			sbNode.append(JSONUtil.getKeyValue("targetname", link.getEndNode().getName()));
			sbNode.append(",");
			if (link.getSubType() != null)
				sbNode.append(JSONUtil.getKeyValue("text", "" + link.getSubType() + " - " + link.getType()));
			else
				sbNode.append(JSONUtil.getKeyValue("text", "" + link.getType()));
			sbNode.append("}\n");
		}
		
		
		sbNode.append("],");
		sbNode.append("\n\"count\":\n");
		sbNode.append("{");
		sbNode.append("\"Sub Contracting\":");
		sbNode.append("{");
		sbNode.append(JSONUtil.getKeyValue("count", String.valueOf(subContractingCount)));
		sbNode.append(",");		
		sbNode.append(JSONUtil.getKeyValue("color", subContractingColor));
		sbNode.append("},");
		
		sbNode.append("\"External Processing\":");
		sbNode.append("{");
		sbNode.append(JSONUtil.getKeyValue("count", String.valueOf(externalProcessingCount)));
		sbNode.append(",");		
		sbNode.append(JSONUtil.getKeyValue("color", externalProcessColor));
		sbNode.append("},");
		
		sbNode.append("\"Normal Purchasing\":");
		sbNode.append("{");
		sbNode.append(JSONUtil.getKeyValue("count", String.valueOf(othersCount)));
		sbNode.append(",");		
		sbNode.append(JSONUtil.getKeyValue("color", normalPurchasingColor));
		sbNode.append("},");
		
		sbNode.append("\"Drop Ship\":");
		sbNode.append("{");
		sbNode.append(JSONUtil.getKeyValue("count", String.valueOf(dropShipCount)));
		sbNode.append(",");		
		sbNode.append(JSONUtil.getKeyValue("color", dropShipColor));
		sbNode.append("},");
		
		sbNode.append("\"Direct Sales\":");
		sbNode.append("{");
		sbNode.append(JSONUtil.getKeyValue("count", String.valueOf(directSalesCount)));
		sbNode.append(",");		
		sbNode.append(JSONUtil.getKeyValue("color", directSalesColor));
		sbNode.append("},");
		
		
		
		sbNode.append("\"Consigment\":");
		sbNode.append("{");
		sbNode.append(JSONUtil.getKeyValue("count", String.valueOf(consigmentCount)));
		sbNode.append(",");		
		sbNode.append(JSONUtil.getKeyValue("color", consigmentColor));
		sbNode.append("},");
		
		sbNode.append("\"STO\":");
		sbNode.append("{");
		sbNode.append(JSONUtil.getKeyValue("count", String.valueOf(stoCount)));
		sbNode.append(",");		
		sbNode.append(JSONUtil.getKeyValue("color", stoColor));
		sbNode.append("},");
		
		sbNode.append("\"Intercompany DropShip\":");
		sbNode.append("{");
		sbNode.append(JSONUtil.getKeyValue("count", String.valueOf(intercompanyDropShipCount)));
		sbNode.append(",");		
		sbNode.append(JSONUtil.getKeyValue("color", interCompDropShipColor));
		sbNode.append("}");
				
		sbNode.append("}\n");
		
		sbNode.append("}");
				
		log.info("Converted JSON sent");
		log.info(sbNode.toString());
		return sbNode.toString();
		
	}
	
	private void setStackValue(Node node)
	{
		if (EXTERNAL_VENDOR.equalsIgnoreCase(node.getType()) || INTERNAL_VENDOR.equalsIgnoreCase(node.getType()))
		{
			node.setStack(1);
		}
		else if (MF_PLANT.equalsIgnoreCase(node.getType()))
		{
			node.setStack(2);
		}
		else if (DC_PLANT.equalsIgnoreCase(node.getType()))
		{
			node.setStack(3);
		}
		else if (CUSTOMER.equalsIgnoreCase(node.getType()))
		{
			node.setStack(4);
		}
	}
	
	private void setPOStackValue(Node node)
	{
		if (FROM_COMPANYCODE.equalsIgnoreCase(node.getType()))
		{
			node.setStack(1);
		}
		else if (FROM_PLANT.equalsIgnoreCase(node.getType()))
		{
			node.setStack(2);
		}
		else if (FROM_PURCHASEORG.equalsIgnoreCase(node.getType()))
		{
			node.setStack(3);
		}
		else if (FROM_DOCTYPE.equalsIgnoreCase(node.getType()))
		{
			node.setStack(4);
		}
		else if (FROM_PAYTERM.equalsIgnoreCase(node.getType()))
		{
			node.setStack(5);
		}
	}
	
	private void setLinkColor(Link link)
	{
		//String NodeColor 
		if (SUB_CONTRACTING.equalsIgnoreCase(link.getType())){
			link.setColor("#bfea55");
			subContractingColor = "#bfea55";
			subContractingCount++;
		} else{
			subContractingColor = "#bfea55";
		}
		 if (DROP_SHIP.equalsIgnoreCase(link.getType())){
			link.setColor("#A52A2A");
			dropShipColor = "#A52A2A";
			dropShipCount++;
		} else{
			dropShipColor = "#A52A2A";
		}
		 
		 if (EXTERNAL_PROCESSING.equalsIgnoreCase(link.getType())){
			link.setColor("#4b4545");
			externalProcessColor = "#4b4545";
			externalProcessingCount++;
		} else{
			externalProcessColor = "#4b4545";
		}
		 
		 if (NORMAL_PURCHASING.equalsIgnoreCase(link.getType())){
			link.setColor("#71afdb");
			normalPurchasingColor = "#71afdb";
			othersCount++;
		} else{
			normalPurchasingColor = "#71afdb";
		}
		 
		 if (CONSIGMENT.equalsIgnoreCase(link.getType())){
			link.setColor("#fc96ee");
			consigmentColor = "#fc96ee";
			consigmentCount++;
		} else{
			consigmentColor = "#fc96ee";
		}
		 
		 if (DIRECT_SALES.equalsIgnoreCase(link.getType())){
			link.setColor("#fde560");
			directSalesColor = "#fde560";
			directSalesCount++;
		} else{
			directSalesColor = "#fde560";
		}
		 
		if (STO.equalsIgnoreCase(link.getType())){
			link.setColor("#0f691b");
			stoColor = "#0f691b";
			stoCount++;
		} else{
			stoColor = "#0f691b";
		}
		
		 if (INTERCOMPANY_DROPSHIP.equalsIgnoreCase(link.getType())){
			link.setColor("#8B4513");
			interCompDropShipColor = "#8B4513";
			intercompanyDropShipCount++;
		} else{
			interCompDropShipColor = "#8B4513"; 
		}
		
	}
	
	
	private void setPOLinkColor(Link link)
	{
		link.setColor("#FDE560");	
	}
	
	private void setNodeColor(Node node)
	{
		//String NodeColor 
		if (EXTERNAL_VENDOR.equalsIgnoreCase(node.getType()))
			node.setColor("#4c1362");
		else if (INTERNAL_VENDOR.equalsIgnoreCase(node.getType()))
			node.setColor("#eee");
		else if (MF_PLANT.equalsIgnoreCase(node.getType()))
			node.setColor("#1a6f8b");
		else if (DC_PLANT.equalsIgnoreCase(node.getType()))
			node.setColor("#1e2548");
		else if (CUSTOMER.equalsIgnoreCase(node.getType()))
			node.setColor("#9d591b");		
		else
			node.setColor("#000"); 
	}
	
	private void setPOFlowNodeColor(Node node)
	{
		//String NodeColor 
		if (FROM_COMPANYCODE.equalsIgnoreCase(node.getType()))
			node.setColor("#1a6f8b");
		else if (FROM_PLANT.equalsIgnoreCase(node.getType()))
			node.setColor("#0f691b");
		else if (FROM_PURCHASEORG.equalsIgnoreCase(node.getType()))
			node.setColor("#4b4545");
		else if (FROM_DOCTYPE.equalsIgnoreCase(node.getType()))
			node.setColor("#f03434");
		else if (FROM_PAYTERM.equalsIgnoreCase(node.getType()))
			node.setColor("#9d591b");		
		else
			node.setColor("#000"); 
	}
	
	private Map<String, ArrayList<Node>> getDistinctNodes()
	{
		HashMap<String, ArrayList<Node>> nodeMap = new HashMap<String, ArrayList<Node>>();
		
		if (this.links != null)
		{
			for (Link link : this.links)
			{
				if (link.getStartNode() != null && link.getStartNode().getCode() != null)
				{
					addtoHashMap(nodeMap, link.getStartNode());
				}
				if (link.getEndNode() != null && link.getEndNode().getCode() != null)
				{
					addtoHashMap(nodeMap, link.getEndNode());
				}
			}
		}
		return nodeMap;
	}
	
	private void addtoHashMap(HashMap<String, ArrayList<Node>> nodeMap, Node node)
	{
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
	
}
