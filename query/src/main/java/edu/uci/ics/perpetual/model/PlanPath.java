package edu.uci.ics.perpetual.model;

import java.util.ArrayList;
import java.util.List;

public class PlanPath {
	private int objectId;
	
	private double cost;
	private List<EnrichmentFunction> enrichmentFunctionList;
	
	public PlanPath()
	{
		//initializing list
		enrichmentFunctionList = new ArrayList<EnrichmentFunction>();
	}
	
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
		cost = 0;
		// sum up all the cost of the enrichment funcitons.
		for(EnrichmentFunction f: enrichmentFunctionList)
			cost += f.getCost();
	}
	
	// add, get and remove for enrichmentFunctionList
	public void addEnrichmentFunction(EnrichmentFunction f)
	{
		enrichmentFunctionList.add(f);
	}
	public void addEnrichmentFunction(EnrichmentFunction f, int index)
	{
		enrichmentFunctionList.add(index, f);
	}
	public EnrichmentFunction getFunction(int index)
	{
		return enrichmentFunctionList.get(index);
	}
	public EnrichmentFunction removeFunction(int index)
	{
		return enrichmentFunctionList.remove(index);
	}
}
