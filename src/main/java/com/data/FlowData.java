package com.data;

public class FlowData {
	private String fromCode;
	private String fromName;
	private String fromText;
	private String fromType;
	
	
	private String toCode;
	private String toName;
	private String toText;
	private String toType;
	
	
	private String linkType;
	private String linkSubType;
	private double linkValue;

		
	
	public String getLinkSubType() {
		return linkSubType;
	}

	public void setLinkSubType(String linkSubType) {
		this.linkSubType = linkSubType;
	}

	public String getToType() {
		return toType;
	}

	public void setToType(String toType) {
		this.toType = toType;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getFromCode() {
		return fromCode;
	}

	public void setFromCode(String fromCode) {
		this.fromCode = fromCode;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromText() {
		return fromText;
	}

	public void setFromText(String fromText) {
		this.fromText = fromText;
	}

	public String getToCode() {
		return toCode;
	}

	public void setToCode(String toCode) {
		this.toCode = toCode;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getToText() {
		return toText;
	}

	public void setToText(String toText) {
		this.toText = toText;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public double getLinkValue() {
		return linkValue;
	}

	public void setLinkValue(double linkValue) {
		this.linkValue = linkValue;
	}
	
}
