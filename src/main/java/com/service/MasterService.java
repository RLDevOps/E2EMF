package com.service;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.dao.MasterDataDAO;
import com.dao.MaterialFlowDAO;
import com.data.FlowData;
import com.data.MasterData;
import com.model.MaterialFlow;
import com.util.JSONUtil;
import com.util.SearchUtil;

/**
 * @author Sai kumar --Service for to display Master Data 
 * 
 */
@Path("/MasterData")
@Consumes(MediaType.APPLICATION_JSON)
public class MasterService {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/searchMasterData")
		public List<MasterData>  getMasterData(@QueryParam("type") String varX,@QueryParam("q") String searchParam) 
		{
		String param=varX;
		String searchStr=searchParam;
		String jsonArr="";
		String str="";
		//List<MasterData> masterDataList11 = new ArrayList<MasterData>();
		ArrayList<MasterData> masterDataList = new ArrayList<MasterData>();
		StringBuilder sbNode = new StringBuilder();
			try
			{
			MasterDataDAO maDao = new MasterDataDAO();
			masterDataList = maDao.retrieveMasterData(param,searchStr);
			
			//masterDataList11=masterDataList;
			
		/*	
			for(MasterData mlist: masterDataList){
				sbNode.append("{");
				sbNode.append(JSONUtil.getKeyValue("id", mlist.getId()));
				sbNode.append(",");
				sbNode.append(JSONUtil.getKeyValue("Name", mlist.getName()));
				sbNode.append("}");
				sbNode.append(",");
			}
			
			
			
			str = sbNode.toString();
			str = str.substring(0,str.length()-1);
			str = str +"]";
			
			*/
			//sbNode.substring(0, sbNode.length()-1);
			//sbNode.append("]");
			
		/*	for (int i=0;i< MasterDataList.size();i++)
			{
				String	MasterDataList21 = MasterDataList.get(i).getName().toString();
				sbNode.append(JSONUtil.getKeyValue("name", MasterDataList.get(i).getName().toString()));
			}*/
			
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return masterDataList;
			
		}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/searchMasterDatanew")
		public String  getMasterDatanew(@QueryParam("type") String varX,@QueryParam("q") String searchParam) 
		{
		String param=varX;
		String searchStr=searchParam;
		String jsonArr="";
		String str="";
		//str = str +"{[";
		//List<MasterData> masterDataList11 = new ArrayList<MasterData>();
		ArrayList<MasterData> masterDataList = new ArrayList<MasterData>();
		StringBuilder sbNode = new StringBuilder();
		sbNode.append("[");
			try
			{
			MasterDataDAO maDao = new MasterDataDAO();
			masterDataList = maDao.retrieveMasterData(param,searchStr);
			
			//masterDataList11=masterDataList;
			
			if(masterDataList.size()!=0)
			{
				for(MasterData mlist: masterDataList){
					sbNode.append("{");
					sbNode.append(JSONUtil.getKeyValue("id", mlist.getId()));
					sbNode.append(",");
					sbNode.append(JSONUtil.getKeyValue("name", mlist.getName()));
					sbNode.append("}");
					sbNode.append(",");
				}	
				str = sbNode.toString();
				str = str.substring(0,str.length()-1);
			}
			else
			{
				str = str + "[";
			}
			
			
			
			
			str = str +"]";
			
			
			//sbNode.substring(0, sbNode.length()-1);
			//sbNode.append("]");
			
		/*	for (int i=0;i< MasterDataList.size();i++)
			{
				String	MasterDataList21 = MasterDataList.get(i).getName().toString();
				sbNode.append(JSONUtil.getKeyValue("name", MasterDataList.get(i).getName().toString()));
			}*/
			
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return str;
			
		}

}
