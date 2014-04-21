package com.transform;

import java.util.ArrayList;

import com.data.EntityData;
import com.domain.Attribute;

public class AttributeTransform {
	
	public String getAttributeJSON(String entityId, boolean encloseOuterBrace)
	{
		EntityData entityData = new EntityData();
		try
		{
			ArrayList<Attribute> attributeList = entityData.getAttributes(entityId);
			StringBuffer json = new StringBuffer();
			if (encloseOuterBrace)
			{
				json.append("[");
			}
			boolean isFirstRecord = true;
			for (Attribute attribute : attributeList)
			{
				String title = null;
				if (isFirstRecord)
				{
					isFirstRecord = false;
				}
				else
				{
					json.append(",");
				}
				//"[{\"title\":\"item1\"},{\"title\":\"item2\"}]";
				json.append("{");
				title = attribute.getDisplayName();
				json.append("\"title\":\"" +  title + "\"");
				json.append(",");
				json.append("\"isLazy\":" +  "true");
				json.append(",");
				json.append("\"isFolder\":" +  "false");
				json.append(",");
				json.append("\"key\":\"" +  entityId + "\"");
				json.append(",");
				json.append("\"subKey\":\"" +  attribute.getId() + "\"");
				json.append("}");
			}
			if (encloseOuterBrace)
			{
				json.append("]");
			}
			return json.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
