package com.relevance.e2emf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.relevance.e2emf.commons.E2emfAppUtil;
import com.relevance.e2emf.commons.E2emfConstants;
import com.relevance.e2emf.commons.JSONUtil;
import com.relevance.e2emf.commons.Log;
import com.relevance.e2emf.dao.E2emfDao;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.domain.Material;
import com.relevance.e2emf.view.E2emfView;
import com.relevance.e2emf.view.FlowData;
import com.relevance.e2emf.view.Link;
import com.relevance.e2emf.view.Node;
import com.relevance.e2emf.view.material.MaterialView;

public class MaterialService implements Service {
	
	private ArrayList<Link> links;
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
	
	
	private E2emfAppUtil appUtil = null;
	
	public MaterialService(){
		appUtil = new E2emfAppUtil();
	}
	

	@Override
	public String getJsonObject() {
		
		return null;
	}

	@Override
	public String getJsonObject(Object... obj) {
		String materialViewJson = null;
		List<FlowData> materialflowList = null;
		 Log.Info("getJsonObject on Material Service called with input of size... " + obj.length);
		 String supplier = null;
		 String customer = null;
		 String meterialId = null;
		 String tier1 = null;
		 String tier2 = null;
		 String type = null;
		 
		
		if(obj[0] != null )
		    supplier = (String) obj[0];
		if(obj[1] != null )
			customer = (String) obj[1];
		if(obj[2] != null )
			meterialId = (String) obj[2];
		if(obj[3] != null )
		    tier1 = (String) obj[3];
		if(obj[4] != null )
			tier2 = (String) obj[4];
		if(obj[5] != null )
	 	    type = (String) obj[5];
		
		 Log.Info("**Fetching Data List based on the Inputs \n  Supplier : " + supplier + "\n Customer : " + customer + "\n meterialId " + meterialId + "\n Tier1 : " + tier1 + "\n type : " + type);
		
		
		try{
			E2emfDao materialDao = E2emfServiceLocator.getServiceDaoInstance("material");				
			Material material = (Material) materialDao.getBusinessObject(obj);	
			materialflowList = material.getMaterialflowList();
			deriveMaterialNodesLinks(materialflowList);
			materialViewJson = getMaterialFlowD3SankeyJson();
			
			Log.Info("MaterialJason got is " + materialViewJson);
				
		}catch(Exception e){
			Log.Error("Error fetching Material Business Object");	
			Log.Error(e);
			e.printStackTrace();
		}
		
		return materialViewJson;
		
	}
	
	
	
	/* Called from the Context Listener on load
	 * @see com.relevance.e2emf.service.Service#getBusinessObject()
	 */
	@Override
	public E2emfBusinessObject getBusinessObject() {
		Log.Info("getBusinessObject invoked from the Context event Listener for pre population of Material Details...");
		Material material = null;
		try{			
			E2emfDao materialDao = E2emfServiceLocator.getServiceDaoInstance("material");			
			material  = (Material) materialDao.getBusinessObject();				
				
		}catch(Exception e){
			Log.Error("Exception in getBusinessObject of the Materal Service " + e.getMessage());
			e.printStackTrace();
		}
		
		return material;
	}
	
	
	/* (non-Javadoc)
	 * @see com.relevance.e2emf.service.Service#generateJsonString(java.lang.Object[])
	 */
	@Override
	public String generateJsonString(Object... obj) {
		
	Log.Info("Generating Json for Material Flow ");		
		
		MaterialView materialView = new MaterialView();
		List<FlowData> materialflowList = null;
		String materialflowJson = null;		
		try{
			E2emfDao materialDao = E2emfServiceLocator.getServiceDaoInstance("material");
			materialDao.getFlowDataList(obj);
			Material material = (Material) obj[0];	
			materialflowList = material.getMaterialflowList();
			deriveMaterialNodesLinks(materialflowList);		
			materialflowJson = 	getMaterialFlowD3SankeyJson();			
			
		}catch(Exception e){
			Log.Error("Exception generating Json from Material Details " + e.getMessage());
			e.printStackTrace();
		}
				
		return materialflowJson;
	}

	
	/**
	 * @return
	 */
	private String getMaterialFlowD3SankeyJson() {
		StringBuilder sbNode = new StringBuilder(); 
		
		try{
			
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
								
		}catch(Exception e) {
			Log.Error("Exception getting Material Flow Sankey Json "  +e.getMessage());
			e.printStackTrace();
		}
		
		return sbNode.toString();
	}


	/**
	 * @return
	 */
	private Map<String, ArrayList<Node>> getDistinctNodes() {
		HashMap<String, ArrayList<Node>> nodeMap = new HashMap<String, ArrayList<Node>>();
		
		try{
			
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
			
		}catch(Exception e){
			Log.Error("Exception getting Distinct Nodes in generating Sankey Json" + e.getMessage());
		}
		return nodeMap;
	}


	/**
	 * @param nodeMap
	 * @param startNode
	 */
	private void addtoHashMap(HashMap<String, ArrayList<Node>> nodeMap,
			Node node) {
		try{
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
		
		}catch(Exception e){
		 Log.Error("Exception Adding distinct Nodes to HashMap " + e.getMessage());
		 e.printStackTrace();
		}
		
	}




	@Override
	public E2emfView getViewObject(E2emfBusinessObject material) {
		
		
		return null;
	}

	

	@Override
	public E2emfBusinessObject getBusinessObject(Object... obj) {

		return null;
	}
	
	@Override
	public E2emfView getViewObject(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}


	

	
	@Override
	public E2emfView getViewObject() {
		
		return null;
	}
	
	
	

	/**
	 * @param flowDataList
	 */
	private void deriveMaterialNodesLinks(List<FlowData> flowDataList) {
		try{
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
		}catch(Exception e){
			Log.Error("Exception Deriving Material Flow Notes and Links " + e.getMessage());
		}
	}

	
	
	
	/**
	 * @param link
	 */
	private void setLinkColor(Link link) {
		
		try{
			//String NodeColor 
			if (E2emfConstants.SUB_CONTRACTING.equalsIgnoreCase(link.getType())){
				link.setColor("#bfea55");
				subContractingColor = "#bfea55";
				subContractingCount++;
			} else{
				subContractingColor = "#bfea55";
			}
			 if (E2emfConstants.DROP_SHIP.equalsIgnoreCase(link.getType())){
				link.setColor("#A52A2A");
				dropShipColor = "#A52A2A";
				dropShipCount++;
			} else{
				dropShipColor = "#A52A2A";
			}
			 
			 if (E2emfConstants.EXTERNAL_PROCESSING.equalsIgnoreCase(link.getType())){
				link.setColor("#4b4545");
				externalProcessColor = "#4b4545";
				externalProcessingCount++;
			} else{
				externalProcessColor = "#4b4545";
			}
			 
			 if (E2emfConstants.NORMAL_PURCHASING.equalsIgnoreCase(link.getType())){
				link.setColor("#71afdb");
				normalPurchasingColor = "#71afdb";
				othersCount++;
			} else{
				normalPurchasingColor = "#71afdb";
			}
			 
			 if (E2emfConstants.CONSIGMENT.equalsIgnoreCase(link.getType())){
				link.setColor("#fc96ee");
				consigmentColor = "#fc96ee";
				consigmentCount++;
			} else{
				consigmentColor = "#fc96ee";
			}
			 
			 if (E2emfConstants.DIRECT_SALES.equalsIgnoreCase(link.getType())){
				link.setColor("#fde560");
				directSalesColor = "#fde560";
				directSalesCount++;
			} else{
				directSalesColor = "#fde560";
			}
			 
			if (E2emfConstants.STO.equalsIgnoreCase(link.getType())){
				link.setColor("#0f691b");
				stoColor = "#0f691b";
				stoCount++;
			} else{
				stoColor = "#0f691b";
			}
			
			 if (E2emfConstants.INTERCOMPANY_DROPSHIP.equalsIgnoreCase(link.getType())){
				link.setColor("#8B4513");
				interCompDropShipColor = "#8B4513";
				intercompanyDropShipCount++;
			} else{
				interCompDropShipColor = "#8B4513"; 
			}

		}catch(Exception e) {
			
		}
		
	}


	private void setNodeColor(Node node) {
		
			try{
				//String NodeColor 
				if (E2emfConstants.EXTERNAL_VENDOR.equalsIgnoreCase(node.getType()))
					node.setColor("#4c1362");
				else if (E2emfConstants.INTERNAL_VENDOR.equalsIgnoreCase(node.getType()))
					node.setColor("#eee");
				else if (E2emfConstants.MF_PLANT.equalsIgnoreCase(node.getType()))
					node.setColor("#1a6f8b");
				else if (E2emfConstants.DC_PLANT.equalsIgnoreCase(node.getType()))
					node.setColor("#1e2548");
				else if (E2emfConstants.CUSTOMER.equalsIgnoreCase(node.getType()))
					node.setColor("#9d591b");		
				else
					node.setColor("#000"); 
					
			}catch(Exception e){
				Log.Error("Exception setting Node Color for Material Flow " + e);
			}
		
		
	}

	private void setStackValue(Node node)
	{
		try{
			if (E2emfConstants.EXTERNAL_VENDOR.equalsIgnoreCase(node.getType()) || E2emfConstants.INTERNAL_VENDOR.equalsIgnoreCase(node.getType()))
			{
				node.setStack(1);
			}
			else if (E2emfConstants.MF_PLANT.equalsIgnoreCase(node.getType()))
			{
				node.setStack(2);
			}
			else if (E2emfConstants.DC_PLANT.equalsIgnoreCase(node.getType()))
			{
				node.setStack(3);
			}
			else if (E2emfConstants.CUSTOMER.equalsIgnoreCase(node.getType()))
			{
				node.setStack(4);
			}	
		}catch(Exception e){
			Log.Error("Exception in setStackValue " + e.getMessage() );
			e.printStackTrace();
		}
		
	}
	
	
/*	public static void main(String args[]){
		String materialFlowJson = null;
		
		MaterialService materialService = new MaterialService();
		E2emfDao materialDao = E2emfServiceLocator.getServiceDaoInstance("material");	
		Material material = (Material) materialDao.getBusinessObject();
		List<FlowData> flowDataList = material.getMaterialflowList();
		materialService.deriveMaterialNodesLinks(flowDataList);
		materialFlowJson = materialService.getMaterialFlowD3SankeyJson();
		
		Log.Info("**materalFlowJson : \n " + materialFlowJson); 
		
	}
*/


}
