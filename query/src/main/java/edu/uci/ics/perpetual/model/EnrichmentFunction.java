package edu.uci.ics.perpetual.model;

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
	public List<Object> getParameters() {
		return parameters;
	}
	public void setParameters(List<Object> parameters) {
		this.parameters = parameters;
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
