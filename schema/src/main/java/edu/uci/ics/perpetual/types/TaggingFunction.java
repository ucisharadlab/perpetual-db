package edu.uci.ics.perpetual.types;

import java.util.List;

public class TaggingFunction {

    private String functionName;

    private String sourceType;

    private String returnTag;

    private List<String> paramList;

    private String path;

    private int cost;
    
    private double quality;

    public TaggingFunction(String functionName, String sourceType, List<String> paramList, String returnTag, int cost) {
        this.functionName = functionName;
        this.sourceType = sourceType;
        this.paramList = paramList;
        this.returnTag = returnTag;
        this.cost = cost;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getReturnTag() {
        return returnTag;
    }

    public List<String> getParamList() {
        return paramList;
    }

    public int getCost() {
        return cost;
    }

    public double getQuality() {
		return quality;
	}

	public void setQuality(double quality) {
		this.quality = quality;
	}

	@Override
    public String toString() {
        return String.format("Raw Type=%s, Tag=%s, Dependencies=%s, Cost=%s, Quality=%s, Path=%s",
                sourceType, returnTag, paramList, cost, quality,path);
    }
}
