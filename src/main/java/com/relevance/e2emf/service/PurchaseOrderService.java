package com.relevance.e2emf.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.relevance.e2emf.commons.E2emfAppUtil;
import com.relevance.e2emf.commons.E2emfConstants;
import com.relevance.e2emf.commons.JSONUtil;
import com.relevance.e2emf.commons.Log;
import com.relevance.e2emf.dao.E2emfDao;
import com.relevance.e2emf.domain.E2emfBusinessObject;
import com.relevance.e2emf.domain.PurchaseOrder;
import com.relevance.e2emf.view.E2emfView;
import com.relevance.e2emf.view.FlowData;
import com.relevance.e2emf.view.Link;
import com.relevance.e2emf.view.Node;
import com.relevance.e2emf.view.purchase.PurchaseOrderView;
/**
 * @author emanuel
 * E2emf PurchasOrder Service 
 *
 */
public class PurchaseOrderService implements Service {
	
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
	
	public PurchaseOrderService(){
		appUtil = new E2emfAppUtil();
	}



	@Override
	public String getJsonObject() {
		String purchaseOrderJson = null;
		
		Log.Info("Fetching PurchaseOrder DAO..");
		E2emfDao purchaseOrderDao = E2emfServiceLocator.getServiceDaoInstance("purchase");
		PurchaseOrder purchaseOrder = (PurchaseOrder) purchaseOrderDao.getBusinessObject();	
		Log.Info("Fetched  PurchaseOrder Object ");
		//Implementation 1
		List<FlowData> purchaseOrderFlowList = purchaseOrder.getPurchaseOrderflowDataList();
		PurchaseOrderService service = new PurchaseOrderService();
		service.derivePurchaseOrderFlow(purchaseOrderFlowList);
		Log.Info("Getting  getPurchaseOrderSankeyJson ..");
		purchaseOrderJson = service.getPurchaseOrderSankeyJson();
	
		
	/*	
		
		
		
		
		
		E2emfDao purchaseOrderDao = E2emfServiceLocator.getServiceDaoInstance("purchase");
		PurchaseOrder purchaseOrder = (PurchaseOrder) purchaseOrderDao.getBusinessObject();	
		
		//Implementation 1
		List<FlowData> purchaseOrderFlowList = purchaseOrder.getPurchaseOrderflowDataList();
		
		derivePurchaseOrderFlow(purchaseOrderFlowList);
		purchaseOrderJson = getPurchaseOrderSankeyJson();
*/		
			
		//Log.Info("Fetched Po FlowDataList " + purchaseOrderFlowList);
		
	//	derivePOFlow(purchaseOrderFlowList);
		//purchaseOrderJson = toD3SankeyJSON();
		
		//Implementation 2		
		/*PurchaseOrderView purchaseOrderView = createPurchaseOrderView(purchaseOrder);		
		String purchaseOrderJson = generateJsonString(purchaseOrderView);
		*/
		return purchaseOrderJson;
	}

	/**
	 * @return 
	 * 
	 */
	private String getPurchaseOrderSankeyJson() {
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
					
			/*log.info("Converted JSON sent");
			log.info(sbNode.toString());*/
			
		}catch(Exception e){
			Log.Error("Exception in getPurchaseOrderSankeyJson " + e.getMessage() );
		}
		
		return sbNode.toString();
		
	}



	/**
	 * @param purchaseOrderFlowList
	 */
	private void derivePurchaseOrderFlow(List<FlowData> purchaseOrderFlowList) {
		try{
			this.links = new ArrayList<Link>();
			for(FlowData flowData : purchaseOrderFlowList)
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
		}catch(Exception e){
			Log.Error("Exception Deriving PurchaseOrder Flow..." + e.getMessage());
		}
		
	}



	/**
	 * 
	 */
	private String toD3SankeyJSON() {
		StringBuilder sbNode = new StringBuilder(); 
		
		ArrayList<Node> listNodes = new ArrayList<Node>();
		Map<String, ArrayList<Node>> nodeMap = this.getDistinctNodes();
		
		try{
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
					sbNode.append(appUtil.getKeyValue("name", node.getCode()));
					sbNode.append(",");
					sbNode.append(appUtil.getKeyValue("color", node.getColor()));
					sbNode.append(",");
					sbNode.append(appUtil.getKeyValue("stack", ""+node.getStack()));
					sbNode.append(",");
					sbNode.append(appUtil.getKeyValue("text", ""+node.getName()));
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
				sbNode.append(appUtil.getKeyValue("source", link.getStartNode().getCode()));
				sbNode.append(",");
				sbNode.append(appUtil.getKeyValue("target", link.getEndNode().getCode()));
				sbNode.append(",");
				sbNode.append(appUtil.getKeyValue("color", link.getColor()));
				sbNode.append(",");
				sbNode.append(appUtil.getKeyValue("value", ""+link.getValue()));
				sbNode.append(",");
				sbNode.append(appUtil.getKeyValue("sourcename", link.getStartNode().getName()));
				sbNode.append(",");
				sbNode.append(appUtil.getKeyValue("targetname", link.getEndNode().getName()));
				sbNode.append(",");
				if (link.getSubType() != null)
					sbNode.append(appUtil.getKeyValue("text", "" + link.getSubType() + " - " + link.getType()));
				else if(link.getType() != null)
					sbNode.append(appUtil.getKeyValue("text", "" + link.getType()));
				
				sbNode.append("}\n");
				
				
			}
			sbNode.append("],");
			sbNode.append("\n\"count\":\n");
			sbNode.append("{");
			sbNode.append("\"Sub Contracting\":");
			sbNode.append("{");
			sbNode.append(appUtil.getKeyValue("count", String.valueOf(subContractingCount)));
			sbNode.append(",");		
			sbNode.append(appUtil.getKeyValue("color", subContractingColor));
			sbNode.append("},");
			
			sbNode.append("\"External Processing\":");
			sbNode.append("{");
			sbNode.append(appUtil.getKeyValue("count", String.valueOf(externalProcessingCount)));
			sbNode.append(",");		
			sbNode.append(appUtil.getKeyValue("color", externalProcessColor));
			sbNode.append("},");
			
			sbNode.append("\"Normal Purchasing\":");
			sbNode.append("{");
			sbNode.append(appUtil.getKeyValue("count", String.valueOf(othersCount)));
			sbNode.append(",");		
			sbNode.append(appUtil.getKeyValue("color", normalPurchasingColor));
			sbNode.append("},");
			
			sbNode.append("\"Drop Ship\":");
			sbNode.append("{");
			sbNode.append(appUtil.getKeyValue("count", String.valueOf(dropShipCount)));
			sbNode.append(",");		
			sbNode.append(appUtil.getKeyValue("color", dropShipColor));
			sbNode.append("},");
			
			sbNode.append("\"Direct Sales\":");
			sbNode.append("{");
			sbNode.append(appUtil.getKeyValue("count", String.valueOf(directSalesCount)));
			sbNode.append(",");		
			sbNode.append(appUtil.getKeyValue("color", directSalesColor));
			sbNode.append("},");
			
			
			
			sbNode.append("\"Consigment\":");
			sbNode.append("{");
			sbNode.append(appUtil.getKeyValue("count", String.valueOf(consigmentCount)));
			sbNode.append(",");		
			sbNode.append(appUtil.getKeyValue("color", consigmentColor));
			sbNode.append("},");
			
			sbNode.append("\"STO\":");
			sbNode.append("{");
			sbNode.append(appUtil.getKeyValue("count", String.valueOf(stoCount)));
			sbNode.append(",");		
			sbNode.append(appUtil.getKeyValue("color", stoColor));
			sbNode.append("},");
			
			sbNode.append("\"Intercompany DropShip\":");
			sbNode.append("{");
			sbNode.append(appUtil.getKeyValue("count", String.valueOf(intercompanyDropShipCount)));
			sbNode.append(",");		
			sbNode.append(appUtil.getKeyValue("color", interCompDropShipColor));
			sbNode.append("}");
					
			sbNode.append("}\n");
			
			sbNode.append("}");
	
		}catch(Exception e){
			Log.Error("Exception converting FlowDataList to Json " + e.getMessage());
			Log.Error(e);
		}
		
				
		System.out.println("PurchaseOrder converted Json : \n " + sbNode.toString());
	
		
		return sbNode.toString();
		
	}

	/**
	 * @return
	 */
	private Map<String, ArrayList<Node>> getDistinctNodes() {
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

	/**
	 * @param nodeMap
	 * @param startNode
	 */
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

	@Override
	public String getJsonObject(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public E2emfBusinessObject getBusinessObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public E2emfBusinessObject getBusinessObject(Object[] obj) {
		// TODO Auto-generated method stub
		return null;
	}

	public E2emfView getViewObject() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public E2emfView getViewObject(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public E2emfView getViewObject(E2emfBusinessObject material) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public String generateJsonString(Object... obj) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private PurchaseOrderView createPurchaseOrderView(
			PurchaseOrder purchaseOrder) {		
		
		PurchaseOrderView purchaseOrderView = new PurchaseOrderView();
		
		
		List<FlowData> purchaseOrderFlowList = purchaseOrder.getPurchaseOrderflowDataList();
		List<Node> nodes = new ArrayList<Node>();
		List<Link> links = new ArrayList<Link>();
				
		for(FlowData flowData: purchaseOrderFlowList){
			
			String fromCode = flowData.getFromCode();
			String fromName = flowData.getFromName();
			String fromText = flowData.getFromText();
			String fromType = flowData.getFromType();	
			String toCode = flowData.getToCode();
			String toName = flowData.getToName();
			String toText = flowData.getToText();
			String toType = flowData.getToType();
			String linkType = flowData.getLinkType();
			String linkSubType = flowData.getLinkSubType();
			double linkValue = flowData.getLinkValue();
			
			Node source = new Node();		
			String stackId = "0";
			String nodeColor = "0";
			
			source.setName(fromName);
			source.setText(fromText);		
			
			if(fromType != null && fromType.trim().length() > 0){
				stackId = getNodeStackId(fromType);
				source.setStack(Integer.parseInt(stackId));
				nodeColor = getNodeColor(fromType);
				source.setColor(nodeColor);			
			}
			
			nodes.add(source);
			
			Node target = new Node();
			stackId = "0";
			nodeColor = "0";
			
			target.setName(toName);
			target.setText(toText);
			
			if(toType != null && toType.trim().length() > 0){
				stackId = getNodeStackId(toType);
				target.setStack(Integer.parseInt(stackId));
				nodeColor = getNodeColor(toType);
				source.setColor(nodeColor);	
			}
			
			nodes.add(target);
			
			Link link = new Link();
			
			String linkColor = "0";
			//link.setName(fromNode.getName() + " To " + toNode.getName());			
			link.setName(E2emfConstants.COMPANYCODE_PLANT_LINKTYPE);
			
			link.setSource(source.getName());
			link.setTarget(target.getName());		
			
			if(linkType != null && linkType.trim().length() > 0){
				linkColor = getLinkColor(linkType);
				link.setColor(linkColor);
			}
						
			link.setValue(linkValue);			
			link.setSourcename(source.getName());
			link.setTargetname(target.getName());
			link.setText(source.getText());
			links.add(link);	
			
		}
		
		purchaseOrderView.setNodes(nodes);
		purchaseOrderView.setLinks(links);
		/*System.out.println("No of Nodes : \n" + nodes.size());
		System.out.println("No of Links : \n" + links.size());
*/
		//generateJsonString(purchaseOrderView);
		
		return purchaseOrderView;
	}
	
	
	
	private String generateJsonString(PurchaseOrderView poview) {
		Gson gson = new Gson();
		String purchaseOrderJsonStr = gson.toJson(poview);
		
		return purchaseOrderJsonStr;
	}

	
	private String getLinkColor(String linkType) {
		String linkColor = "#FDE560";
		return linkColor;
	}

	
	private String getNodeColor(String type) {

		//String NodeColor 
		if (E2emfConstants.FROM_COMPANYCODE.equalsIgnoreCase(type))
			return "#1a6f8b";
		else if (E2emfConstants.FROM_PLANT.equalsIgnoreCase(type))
			return "#0f691b";
		else if (E2emfConstants.FROM_PURCHASEORG.equalsIgnoreCase(type))
			return "#4b4545";
		else if (E2emfConstants.FROM_DOCTYPE.equalsIgnoreCase(type))
			return "#f03434";
		else if (E2emfConstants.FROM_PAYTERM.equalsIgnoreCase(type))
			return "#9d591b";		
		else
			return "#000"; 
	}

	private String getNodeStackId(String type) {
		if (E2emfConstants.FROM_COMPANYCODE.equalsIgnoreCase(type))
		{
			return "1";
		}
		else if (E2emfConstants.FROM_PLANT.equalsIgnoreCase(type))
		{
			return "2";
		}
		else if (E2emfConstants.FROM_PURCHASEORG.equalsIgnoreCase(type))
		{
			return "3";
		}
		else if (E2emfConstants.FROM_DOCTYPE.equalsIgnoreCase(type))
		{
			return "4";
		}
		else if (E2emfConstants.FROM_PAYTERM.equalsIgnoreCase(type))
		{
			return "5";
		}	
		
		return "0";
	}

	
	
	public void derivePOFlow(List<FlowData> flowDataList)
	{
	
		try{
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
			
		}catch(Exception e){
			Log.Error("Exception in DerivePOFLow creating Links and Nodes... " + e);
		}
					
			
			
	}

	/**
	 * @param link
	 */
	private void setPOLinkColor(Link link) {
		link.setColor("#FDE560");
		
	}

	/**
	 * @param startNode
	 */
	private void setPOFlowNodeColor(Node node) {
		try{
			//String NodeColor 
			if (E2emfConstants.FROM_COMPANYCODE.equalsIgnoreCase(node.getType()))
				node.setColor("#1a6f8b");
			else if (E2emfConstants.FROM_PLANT.equalsIgnoreCase(node.getType()))
				node.setColor("#0f691b");
			else if (E2emfConstants.FROM_PURCHASEORG.equalsIgnoreCase(node.getType()))
				node.setColor("#4b4545");
			else if (E2emfConstants.FROM_DOCTYPE.equalsIgnoreCase(node.getType()))
				node.setColor("#f03434");
			else if (E2emfConstants.FROM_PAYTERM.equalsIgnoreCase(node.getType()))
				node.setColor("#9d591b");		
			else
				node.setColor("#000");	
		}catch(Exception  e){
			Log.Error("Exception setting PO FLow Node Color... " + e);
		}
		
		
	}

	/**
	 * @param startNode
	 */
	private void setPOStackValue(Node node) {
		try{
			if (E2emfConstants.FROM_COMPANYCODE.equalsIgnoreCase(node.getType()))
			{
				node.setStack(1);
			}
			else if (E2emfConstants.FROM_PLANT.equalsIgnoreCase(node.getType()))
			{
				node.setStack(2);
			}
			else if (E2emfConstants.FROM_PURCHASEORG.equalsIgnoreCase(node.getType()))
			{
				node.setStack(3);
			}
			else if (E2emfConstants.FROM_DOCTYPE.equalsIgnoreCase(node.getType()))
			{
				node.setStack(4);
			}
			else if (E2emfConstants.FROM_PAYTERM.equalsIgnoreCase(node.getType()))
			{
				node.setStack(5);
			}	
		}catch(Exception e){
			Log.Error("Exception setting PO Stack Value ... " + e);
		}
		
		
		
	}
	
/*	public static void main(String args[]){
		String purchaseOrderJson = null;
		E2emfDao purchaseOrderDao = E2emfServiceLocator.getServiceDaoInstance("purchase");
		PurchaseOrder purchaseOrder = (PurchaseOrder) purchaseOrderDao.getBusinessObject();	
		
		//Implementation 1
		List<FlowData> purchaseOrderFlowList = purchaseOrder.getPurchaseOrderflowDataList();
		PurchaseOrderService service = new PurchaseOrderService();
		service.derivePurchaseOrderFlow(purchaseOrderFlowList);
		purchaseOrderJson = service.getPurchaseOrderSankeyJson();
		Log.Info("PurchaseORder String \n" + purchaseOrderJson);;
		
		
	}*/

}
