
package com.relevance.e2emf.view;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author emanuel
 * E2emf Link View Object 
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"name",
    "source",
    "target",
    "color",
    "value",
    "sourcename",
    "targetname",
    "text",
    "sourceNode",
    "targetNode",
    "linkType",
    "linkSubType"
})
public class Link {
	
	  /**
     * 
     */
    @JsonProperty("name")
    private String name;

    /**
     * 
     */
    @JsonProperty("source")
    private String source;
    /**
     * 
     */
    @JsonProperty("target")
    private String target;
    /**
     * 
     */
    @JsonProperty("color")
    private String color;
    /**
     * 
     */
    @JsonProperty("value")
    private Object value;
    /**
     * 
     */
    @JsonProperty("sourcename")
    private String sourcename;
    /**
     * 
     */
    @JsonProperty("targetname")
    private String targetname;
    /**
     * 
     */
    @JsonProperty("text")
    private String text;
    
    /**
     * 
     */
    @JsonProperty("startNode")
    private Node startNode;
     
      /**
       * 
       */
      @JsonProperty("endNode")
      private Node endNode; 
    
    /**
     * 
     */
    @JsonProperty("sourceNode")  
    private Node sourceNode;
    
    /**
     * 
     */
    @JsonProperty("targetNode")  
    private Node targetNode;
    
    /**
     * 
     */
    @JsonProperty("type")  
    private String type;   
    
    /**
     * 
     */
    @JsonProperty("subType")  
    private String subType;
   
    /**
     * 
     */
    @JsonProperty("linkType")  
    private String linkType;
   
    /**
     * 
     */
    @JsonProperty("linkSubType")  
    private String linkSubType;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
     * 
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     * 
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 
     */
    @JsonProperty("target")
    public String getTarget() {
        return target;
    }

    /**
     * 
     */
    @JsonProperty("target")
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * 
     */
    @JsonProperty("color")
    public String getColor() {
        return color;
    }

    /**
     * 
     */
    @JsonProperty("color")
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * 
     */
    @JsonProperty("value")
    public Object getValue() {
        return value;
    }

    /**
     * 
     */
    @JsonProperty("value")
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * 
     */
    @JsonProperty("sourcename")
    public String getSourcename() {
        return sourcename;
    }

    /**
     * 
     */
    @JsonProperty("sourcename")
    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    /**
     * 
     */
    @JsonProperty("targetname")
    public String getTargetname() {
        return targetname;
    }

    /**
     * 
     */
    @JsonProperty("targetname")
    public void setTargetname(String targetname) {
        this.targetname = targetname;
    }

    /**
     * 
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     * 
     */
    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	public Node getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(Node targetNode) {
		this.targetNode = targetNode;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getType() {
		return type;
	}

	
	public void setType(String type) {
		this.type = type;
		
	}
	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getLinkSubType() {
		return linkSubType;
	}

	public void setLinkSubType(String linkSubType) {
		this.linkSubType = linkSubType;
	}

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

}
