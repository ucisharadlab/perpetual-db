package edu.uci.ics.perpetual.types;

import java.util.List;

public class TaggingFunction {

    private String functionName;

    private String sourceType;

    private String returnTag;

    private List<String> paramList;

    private int cost;

    public TaggingFunction(String functionName, String sourceType, List<String> paramList, String returnTag, int cost) {
        this.functionName = functionName;
        this.sourceType = sourceType;
        this.paramList = paramList;
        this.returnTag = returnTag;
        this.cost = cost;
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
}
