package edu.uci.ics.perpetual.model;

import edu.uci.ics.perpetual.enrichment.EnrichmentFunction;

public class EnrichmentFunctionInfo implements Comparable< EnrichmentFunctionInfo >{
	private int id;
	private EnrichmentFunction function;
	private double cost;
	private double quality;
	
	public double getQuality() {
		return quality;
	}
	public void setQuality(double quality) {
		this.quality = quality;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public EnrichmentFunction getFunction() {
		return function;
	}
	public void setFunction(EnrichmentFunction function) {
		this.function = function;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public EnrichmentFunctionInfo(int id, EnrichmentFunction function, double cost, double quality) {
		super();
		this.id = id;
		this.function = function;
		this.cost = cost;
		this.quality = quality;
	}
	@Override
	public int compareTo(EnrichmentFunctionInfo o) {
		// TODO Auto-generated method stub
		if(this.cost < o.cost)
			return -1;
		if(this.cost > o.cost)
			return 1;
		return 0;
	}
}
