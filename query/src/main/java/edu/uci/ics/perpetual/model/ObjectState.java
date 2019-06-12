package edu.uci.ics.perpetual.model;

import edu.uci.ics.perpetual.data.DataObject;

import java.util.ArrayList;
import java.util.List;

public final class ObjectState{
	private DataObject object;
	private List<Integer> functionBitmap;
	private List<String> functionResultList;  // For deterministic functions
	private List<Double> functionProbResultList; // For probabilistic functions
	
	
	public List<Integer> getFunctionBitmap() {
		return functionBitmap;
	}

	public void setFunctionBitmap(List<Integer> functionBitmap) {
		this.functionBitmap = functionBitmap;
	}

	public List<String> getFunctionResultList() {
		return functionResultList;
	}

	public void setFunctionResultList(List<String> functionResultList) {
		this.functionResultList = functionResultList;
	}

	public List<Double> getFunctionProbResultList() {
		return functionProbResultList;
	}

	public void setFunctionProbResultList(List<Double> functionProbResultList) {
		this.functionProbResultList = functionProbResultList;
	}

	public ObjectState(){
		//initializing lists
		functionBitmap = new ArrayList<Integer>();
		functionResultList = new ArrayList<String>();
		functionProbResultList = new ArrayList<Double>();
	}
	
	public DataObject getObject() {
		return object;
	}
	public void setObject(DataObject object) {
		this.object = object;
	}
	
	// add, get and remove for functionBitmap
	public void addFunctionBit(int n)
	{
		functionBitmap.add(n);
	}
	public void addFunctionBit(int n, int index)
	{
		functionBitmap.add(index, n);
	}
	public int getFunctionBit(int index)
	{
		return functionBitmap.get(index);
	}
	public int removeFunctionBit(int index)
	{
		return functionBitmap.remove(index);
	}
	
	// add, get and remove for functionResultList
	public void addFunctionResult(String s)
	{
		functionResultList.add(s);
	}
	public void addFunctionResult(String s, int index)
	{
		functionResultList.add(index, s);
	}
	public String getFunctionResult(int index)
	{
		return functionResultList.get(index);
	}
	public String removeFunctionResult(int index)
	{
		return functionResultList.remove(index);
	}
	
	// add, get and remove for functionProbResultList
	public void addFunctionProbResult(double d)
	{
		functionProbResultList.add(d);
	}
	public void addFunctionProbResult(double d, int index)
	{
		functionProbResultList.add(index, d);
	}
	public double getFunctionProbResult(int index)
	{
		return functionProbResultList.get(index);
	}
	public double removeFunctionProbResult(int index)
	{
		return functionProbResultList.remove(index);
	}
}