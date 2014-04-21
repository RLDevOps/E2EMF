
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
 * E2emf Node View Object 
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "color",
    "stack",
    "text",
    "fromType",
    "type",
    "code"
})
public class Node {

    /**
     * 
     */
    @JsonProperty("name")
    private String name;
    /**
     * 
     */
    @JsonProperty("color")
    private String color;
    /**
     * 
     */
    @JsonProperty("stack")
    private Object stack;
    /**
     * 
     */
    @JsonProperty("text")
    private String text;
    /**
     * 
     */
    @JsonProperty("text")
    private String fromType;
    
    /**
     * 
     */
    @JsonProperty("text")
    private String type;
    
    /**
     * 
     */
    @JsonProperty("code")
    private String code;

    /**
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
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
    @JsonProperty("stack")
    public Object getStack() {
        return stack;
    }

    /**
     * 
     */
    @JsonProperty("stack")
    public void setStack(Object stack) {
        this.stack = stack;
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

   	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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
