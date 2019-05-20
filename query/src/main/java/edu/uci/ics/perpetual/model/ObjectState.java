package edu.uci.ics.perpetual.model;

import java.util.List;

public final class ObjectState{
	private int objectId;
	private List<Integer> functionBitmap;
	private List<String> functionResultList;  // For deterministic functions
	private List<Double> functionProbResultList; // For probabilistic functions
	
	
	public ObjectState(){
			
	}
	
	public int objectId() {
		return objectId;
	}
	
	public List<Integer> getFunctionBitmap() {
		return this.functionBitmap;
	}
	public void setFunctionBitmap(List<Integer> functionBitmap) {
		this.functionBitmap = functionBitmap;
	}
	
	public List<String> getFunctionResultList() {
		return this.functionResultList;
	}
	public void setFunctionResultList(List<String> functionResultList) {
		this.functionResultList = functionResultList;
	}
	
	public List<Double> getFunctionProbResultList() {
		return this.functionProbResultList;
	}
	public void setFunctionProbResultList(List<Double> functionProbResultList) {
		this.functionProbResultList = functionProbResultList;
	}
	

	
}