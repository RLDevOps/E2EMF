
package com.relevance.e2emf.view.purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.relevance.e2emf.view.E2emfView;
import com.relevance.e2emf.view.Link;
import com.relevance.e2emf.view.Node;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * @author emanuel
 * E2emf PurchaseOrder View Object  
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    
    "nodes",
    "links"
})
public class PurchaseOrderView implements E2emfView{

   
	/**
     * 
     */
    @JsonProperty("nodes")
    private List<Node> nodes = new ArrayList<Node>();
    
    /**
     * 
     */
    @JsonProperty("links")
    private List<Link> links = new ArrayList<Link>();
    
    //private Map<String, Object> additionalProperties = new HashMap<String, Object>(); 


    /**
     * 
     */
    @JsonProperty("links")
    public List<Link> getLinks() {
        return links;
    }

    /**
     * 
     */
    @JsonProperty("links")
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * 
     */
    @JsonProperty("nodes")
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * 
     */
    @JsonProperty("nodes")
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
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

   /* @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }*/

}
