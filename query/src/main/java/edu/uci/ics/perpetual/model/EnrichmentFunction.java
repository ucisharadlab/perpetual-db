package edu.uci.ics.perpetual.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class EnrichmentFunction {
	
	private int functionId;	
	private String functionName;
	private String dataType;
	private String functionPath;
	private double cost;
	private double quality;
	
	private List<Entry<String,String>> parameterNamesAndTypes;  // Name of the parameters
	private List<Object> parameters;
	
	public EnrichmentFunction()
	{
		//initializing lists
		parameterNamesAndTypes = new ArrayList<Entry<String,String>>();
		parameters = new ArrayList<Object>();
	}
	
	public int getFunctionId() {
		return functionId;
	}
	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getFunctionPath() {
		return functionPath;
	}
	public void setFunctionPath(String functionPath) {
		this.functionPath = functionPath;
	}
	public List<Entry<String, String>> getParameterNamesAndTypes() {
		return parameterNamesAndTypes;
	}
	public void setParameterNamesAndTypes(List<Entry<String, String>> parameterNamesAndTypes) {
		this.parameterNamesAndTypes = parameterNamesAndTypes;
	}
	// add, get and remove for parameters
	public void addParameter(Object o)
	{
		parameters.add(o);
	}
	public void addParameter(Object o, int index)
	{
		parameters.add(index, o);
	}
	public Object getParameter(int index)
	{
		return parameters.get(index);
	}
	public Object removeParameter(int index)
	{
		return parameters.remove(index);
	}
	
	// add, get and remove for parameters
	public void addParameterNamesAndTypes(Entry<String,String> e)
	{
		parameterNamesAndTypes.add(e);
	}
	public void addParameterNamesAndTypes(Entry<String,String> e, int index)
	{
		parameterNamesAndTypes.add(index, e);
	}
	public Entry<String,String> getParameterNamesAndTypes(int index)
	{
		return parameterNamesAndTypes.get(index);
	}
	public Entry<String,String> removeParameterNamesAndTypes(int index)
	{
		return parameterNamesAndTypes.remove(index);
	}
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
	
	
	
	
	
	
}
