package com.domain;

public class Relationship {

	private String id;
	private String name;
	private String displayName;
	private Entity srcEntity;
	private Attribute srcAttribute;
	private Entity dstEntity;
	private Attribute dstAttribute;
	private Attribute dstTargetAttribute;
	
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
	public Entity getSrcEntity() {
		return srcEntity;
	}
	public void setSrcEntity(Entity srcEntity) {
		this.srcEntity = srcEntity;
	}
	public Attribute getSrcAttribute() {
		return srcAttribute;
	}
	public void setSrcAttribute(Attribute srcAttribute) {
		this.srcAttribute = srcAttribute;
	}
	public Entity getDstEntity() {
		return dstEntity;
	}
	public void setDstEntity(Entity dstEntity) {
		this.dstEntity = dstEntity;
	}
	public Attribute getDstAttribute() {
		return dstAttribute;
	}
	public void setDstAttribute(Attribute dstAttribute) {
		this.dstAttribute = dstAttribute;
	}
	public Attribute getDstTargetAttribute() {
		return dstTargetAttribute;
	}
	public void setDstTargetAttribute(Attribute dstTargetAttribute) {
		this.dstTargetAttribute = dstTargetAttribute;
	}
}
