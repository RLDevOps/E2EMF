package com.domain;

public class Attribute {
	
	private String id;
	private String name;
	private String displayName;
	private String entityId;
	private boolean isAggregrate;
	private boolean isDisplayOnNode;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getEntityId() {
		return entityId;
	}
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	public boolean isAggregrate() {
		return isAggregrate;
	}
	public void setAggregrate(boolean isAggregrate) {
		this.isAggregrate = isAggregrate;
	}
	public boolean isDisplayOnNode() {
		return isDisplayOnNode;
	}
	public void setDisplayOnNode(boolean isDisplayOnNode) {
		this.isDisplayOnNode = isDisplayOnNode;
	}

	
}
