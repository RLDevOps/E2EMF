package com.data;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.domain.Attribute;
import com.domain.Entity;
import com.domain.Relationship;
 
 
public class EntityData {
 
	Connection connection = null;
	
	public ArrayList<Relationship> getRelationships(String entityId, String attributeId) throws SQLException 
	{
		ArrayList<Relationship> relationshipList = new ArrayList<Relationship>();
		Statement stmt = null;
		String query = getRelationshipQuery(entityId, attributeId);

			    try {
			        stmt = getConnection().createStatement();
			        ResultSet rs = stmt.executeQuery(query);
			        while (rs.next()) 
			        {
			        	Relationship relationship = new Relationship();
			        	relationship.setId(rs.getString("id"));
			        	relationship.setName(rs.getString("rel_name"));
			        	relationship.setDisplayName(rs.getString("rel_disp_name"));
			        	
			        	String srcEntityId = rs.getString("src_entity_id");
			        	if (srcEntityId != null)
			        	{
			        		Entity entity = new Entity();
			        		entity.setId(srcEntityId);
			        		entity.setName(rs.getString("se_name"));
			        		entity.setDisplayName(rs.getString("se_disp_name"));
			        		entity.setPkColName(rs.getString("se_pk_col_name"));
			        		relationship.setSrcEntity(entity);
			        	}
			        	
			        	String dstEntityId = rs.getString("dst_entity_id");
			        	if (dstEntityId != null)
			        	{
			        		Entity entity = new Entity();
			        		entity.setId(dstEntityId);
			        		entity.setName(rs.getString("de_name"));
			        		entity.setDisplayName(rs.getString("de_disp_name"));
			        		entity.setPkColName(rs.getString("de_pk_col_name"));
			        		relationship.setDstEntity(entity);
			        	}
			        	
			        	String srcAttributeId = rs.getString("src_attribute_id");
			        	if (srcAttributeId != null)
			        	{
			        		Attribute attribute = new Attribute();
			        		attribute.setId(srcAttributeId);
			        		attribute.setName(rs.getString("sa_name"));
			        		attribute.setDisplayName(rs.getString("sa_disp_name"));
			        		attribute.setAggregrate(rs.getInt("sa_is_aggr") > 0);
			        		attribute.setAggregrate(rs.getInt("sa_is_disp") > 0);
			        		relationship.setSrcAttribute(attribute);
			        	}
			        	
			        	String dstAttributeId = rs.getString("dst_attribute_id");
			        	if (dstAttributeId != null)
			        	{
			        		Attribute attribute = new Attribute();
			        		attribute.setId(dstAttributeId);
			        		attribute.setName(rs.getString("da_name"));
			        		attribute.setDisplayName(rs.getString("da_disp_name"));
			        		attribute.setAggregrate(rs.getInt("da_is_aggr") > 0);
			        		attribute.setAggregrate(rs.getInt("da_is_disp") > 0);
			        		relationship.setDstAttribute(attribute);
			        	}
			        	
			        	String dstTgtAttributeId = rs.getString("dst_tgt_attribute_id");
			        	if (dstTgtAttributeId != null)
			        	{
			        		Attribute attribute = new Attribute();
			        		attribute.setId(dstTgtAttributeId);
			        		attribute.setName(rs.getString("ta_name"));
			        		attribute.setDisplayName(rs.getString("ta_disp_name"));
			        		attribute.setAggregrate(rs.getInt("ta_is_aggr") > 0);
			        		attribute.setAggregrate(rs.getInt("ta_is_disp") > 0);
			        		relationship.setDstTargetAttribute(attribute);
			        	}
			        	
			        	relationshipList.add(relationship);
			          
			        }
			    } catch (SQLException e ) {
			        e.printStackTrace();
			    } finally {
			        if (stmt != null) { stmt.close(); }
			        closeConnection();
			    }
			return relationshipList;
	}
	
	public ArrayList<Attribute> getAttributes(String entityId) throws SQLException 
	{
		ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
		Statement stmt = null;
		String query = getAttributeQuery(entityId);

			    try {
			        stmt = getConnection().createStatement();
			        ResultSet rs = stmt.executeQuery(query);
			        while (rs.next()) 
			        {
			        	Attribute attribute = new Attribute();
			        	
		        		attribute.setId(rs.getString("id"));
		        		attribute.setEntityId(rs.getString("entity_id"));
		        		attribute.setName(rs.getString("name"));
		        		attribute.setDisplayName(rs.getString("display_name"));
		        		attribute.setAggregrate(rs.getInt("is_aggr") > 0);
		        		attribute.setAggregrate(rs.getInt("is_disp") > 0);
		        		        	
			        	
		        		attributeList.add(attribute);
			          
			        }
			    } catch (SQLException e ) {
			        e.printStackTrace();
			    } finally {
			        if (stmt != null) { stmt.close(); }
			        closeConnection();
			    }
			return attributeList;
	}
	private Connection getConnection()
	{
 
		if (connection != null) {
			return connection;
		}
		System.out.println("-------- Oracle JDBC Connection Testing ------");
 
		try {
 
			Class.forName("oracle.jdbc.driver.OracleDriver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return null;
 
		}
 
		System.out.println("Oracle JDBC Driver Registered!");
 
		
 
		try {
 
			connection = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:xe", "ERUSER",
					"password");
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return null;
 
		}
 
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}
		
		return connection;
	}
 
	private void closeConnection() throws SQLException
	{
		if (connection != null) {
			connection.close();
		} 
	}
	private String getRelationshipQuery(String entityId, String attributeId)
	{
		StringBuffer query = new StringBuffer();
		
		query.append(" select ");
		query.append(" r.id,");
		query.append(" r.name as rel_name,");
		query.append(" r.disp_name as rel_disp_name, ");
		query.append(" r.src_entity_id,");
		query.append(" r.src_attribute_id,");
		query.append(" r.dst_entity_id,");
		query.append(" r.dst_attribute_id,");
		query.append(" r.dst_tgt_attribute_id,");
		query.append(" se.name as se_name,");
		query.append(" se.display_name as se_disp_name,");
		query.append(" se.pk_col_name as se_pk_col_name,");
		query.append(" de.name as de_name,");
		query.append(" de.display_name as de_disp_name,");
		query.append(" de.pk_col_name as de_pk_col_name,");
		query.append(" sa.display_name as sa_disp_name,");
		query.append(" sa.name as sa_name,");
		query.append(" sa.is_aggr as sa_is_aggr,");
		query.append(" sa.is_disp as sa_is_disp,");
		query.append(" da.display_name as da_disp_name,");
		query.append(" da.name as da_name,");
		query.append(" da.is_aggr as da_is_aggr,");
		query.append(" da.is_disp as da_is_disp,");
		query.append(" ta.display_name as ta_disp_name,");
		query.append(" ta.name as ta_name,");
		query.append(" ta.is_aggr as ta_is_aggr,");
		query.append(" ta.is_disp as ta_is_disp");
		query.append(" from relationship r");
		query.append(" left join entity se");
		query.append(" on r.src_entity_id = se.id");
		query.append(" left join attribute sa");
		query.append(" on r.src_attribute_id = sa.id");
		query.append(" left join entity de");
		query.append(" on r.dst_entity_id = de.id");
		query.append(" left join attribute da");
		query.append(" on r.dst_attribute_id = da.id");
		query.append(" left join attribute ta");
		query.append(" on r.dst_tgt_attribute_id = ta.id ");
		query.append(" where r.src_entity_id = '" + entityId + "'");
		
		if (attributeId != null && attributeId.length() > 0)
		{
			query.append(" and r.src_attribute_id = '" + attributeId + "'");
		}
		else
		{
			query.append(" and r.src_attribute_id is null");
		}

		return query.toString();
	}
	
	private String getAttributeQuery(String entityId)
	{
		StringBuffer query = new StringBuffer();
		
		query.append(" select ");
		query.append(" id,");
		query.append(" entity_id,");
		query.append(" name,");
		query.append(" display_name, ");
		query.append(" is_aggr,");
		query.append(" is_disp");
		query.append(" from attribute ");
		query.append(" where entity_id = '" + entityId + "'");

		return query.toString();
	}

}
