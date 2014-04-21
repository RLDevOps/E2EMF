package com.transform;

import java.util.ArrayList;

import com.data.EntityData;
import com.domain.Relationship;

public class RelationshipTransform {
	
	public String getRelationshipJSON(String entityId, String attributeId)
	{
		EntityData entityData = new EntityData();
		try
		{
			ArrayList<Relationship> relationshipList = entityData.getRelationships(entityId, attributeId);
			StringBuffer json = new StringBuffer();
			json.append("[");
			String attributeJSON = null;
			if (entityId != null && (attributeId == null || (attributeId != null && attributeId.length() == 0)))
			{
				AttributeTransform attributeTransform = new AttributeTransform();
				attributeJSON = attributeTransform.getAttributeJSON(entityId, false);
				json.append(attributeJSON);
				
			}
			
			boolean isFirstRecord = true;
			for (Relationship relationship : relationshipList)
			{
				String dstEntityId = null;
				String dstAttributeId = null;
				String title = null;
				boolean isFolder = true;
				
				if (isFirstRecord)
				{
					isFirstRecord = false;
					if (attributeJSON != null && attributeJSON.length() > 0)
					{
						json.append(",");
					}
				}
				else
				{
					json.append(",");
				}
				//"[{\"title\":\"item1\"},{\"title\":\"item2\"}]";
				json.append("{");
				
				if (relationship.getDstEntity() != null && relationship.getDstAttribute() != null && relationship.getDstTargetAttribute() != null)
				{
					title = relationship.getDstEntity().getDisplayName() + "." + relationship.getDstAttribute().getDisplayName()
																		+  " -> " + relationship.getDstEntity().getDisplayName()
																		+ "." + relationship.getDstTargetAttribute().getDisplayName();
					dstAttributeId = relationship.getDstTargetAttribute().getId();
					isFolder = false;
				}
				else if (relationship.getDstEntity() != null)
				{
					title = relationship.getDstEntity().getDisplayName();
					dstAttributeId = "";
				}
				else
				{
					title = relationship.getDisplayName();
					dstAttributeId = "";
				}
				json.append("\"title\":\"" +  title + "\"");
				json.append(",");
				json.append("\"isLazy\":" +  "true");
				json.append(",");
				json.append("\"isFolder\":" +  (isFolder?"true":"false"));
				json.append(",");
				
				if (relationship.getDstEntity() != null)
				{
					dstEntityId = relationship.getDstEntity().getId();
				}
				else
				{
					dstEntityId = "";
				}
				json.append("\"key\":\"" +  dstEntityId + "\"");
				json.append(",");
				json.append("\"subKey\":\"" +  dstAttributeId + "\"");
				json.append("}");
			}
			json.append("]");
			
			return json.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
