package edu.uci.ics.perpetual.model;

import java.util.ArrayList;
import java.util.List;

public class BlockPath implements Comparable<BlockPath>{
	private BlockState blockState;
	private double cost;
	private double quality;
	private double benefit;
	//private int blockSize;
	private List<EnrichmentFunctionInfo> enrichmentFunctionList;
	
	
	

	
	public List<EnrichmentFunctionInfo> getEnrichmentFunctionList() {
		return enrichmentFunctionList;
	}


	public void setEnrichmentFunctionList(List<EnrichmentFunctionInfo> enrichmentFunctionList) {
		this.enrichmentFunctionList = enrichmentFunctionList;
	}


	public BlockPath()
	{
		//initializing list
		enrichmentFunctionList = new ArrayList<EnrichmentFunctionInfo>();
		
		
	}
	

	public double getBenefit() {
		return benefit;
	}

	public void setBenefit(double benefit) {
		this.benefit = benefit;
	}
	/*
	public int getBlocksize() {
		return blockSize;
	}

	public void setBlocksize(int blockSize) {
		this.blockSize = blockSize;
	}*/
	
	
	
	public BlockState getBlockState() {
		return blockState;
	}
	public void setBlockState(BlockState blockState) {
		this.blockState = blockState;
	}
	public double getCost() {
		return cost;
	}
	public void calculateCost() {
		cost = 0;
		// sum up all the cost of the enrichment funcitons.
		for(EnrichmentFunctionInfo f: enrichmentFunctionList)
			cost += f.getCost();
		int numUnresolved = blockState.getNumMayBe();
		cost = numUnresolved;
		//cost = cost * numUnresolved;
		//cost = cost * blockState.getBlocksize();
	}
	
	public void calculateBenefit() {
		cost = 0.0;
		quality = 0.0;
		// sum up all the cost of the enrichment funcitons.
		
		for(EnrichmentFunctionInfo f: enrichmentFunctionList) {
			quality += (f.getQuality() * blockState.getBlocksize());
			cost += (f.getCost() * blockState.getBlocksize());
		}
			
		benefit = quality/cost;	
	}
	
	// add, get and remove for enrichmentFunctionList
	public void addEnrichmentFunction(EnrichmentFunctionInfo f)
	{
		enrichmentFunctionList.add(f);
	}
	public void addEnrichmentFunction(EnrichmentFunctionInfo f, int index)
	{
		enrichmentFunctionList.add(index, f);
	}
	public EnrichmentFunctionInfo getFunction(int index)
	{
		return enrichmentFunctionList.get(index);
	}
	public int getEnrichmentFunctionInfoSize()
	{
		return enrichmentFunctionList.size();
	}
	public EnrichmentFunctionInfo removeFunction(int index)
	{
		return enrichmentFunctionList.remove(index);
	}
	
	@Override
	/*
	public int compareTo(BlockPath o) {
		// TODO Auto-generated method stub
		if(this.getCost() < o.getCost())
			return -1;
		if(this.getCost() > o.getCost())
			return 1;
		return 0;
	}*/
	// Based on number of unresolved objects. Higher cost means better. 
	public int compareTo(BlockPath o) {
		// TODO Auto-generated method stub
		if(this.getCost() < o.getCost())
			return 1;
		if(this.getCost() > o.getCost())
			return -1;
		return 0;
	}
	
	/*
	@Override
	public int compareTo(BlockPath o) {
		// TODO Auto-generated method stub
		if(this.getBenefit() < o.getBenefit())
			return -1;
		if(this.getBenefit() > o.getBenefit())
			return 1;
		return 0;
	}*/
}
