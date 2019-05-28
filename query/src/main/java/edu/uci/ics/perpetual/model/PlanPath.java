package edu.uci.ics.perpetual.model;

import java.util.List;

public class PlanPath {
	private int objectId;
	
	private double cost;
	private List<EnrichmentFunction> enrichmentFunctionList;
	
	
	public int getObjectId() {
		return objectId;
	}
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}
	public double getCost() {
		return cost;
	}
	public void calculateCost() {
		// sum up all the cost of the enrichment funcitons.
		
	}
	public List<EnrichmentFunction> getEnrichmentFunctionList() {
		return enrichmentFunctionList;
	}
	public void setEnrichmentFunctionList(List<EnrichmentFunction> enrichmentFunctionList) {
		this.enrichmentFunctionList = enrichmentFunctionList;
	}
	
	
}
