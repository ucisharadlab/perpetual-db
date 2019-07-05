package edu.uci.ics.perpetual.function;

import edu.uci.ics.perpetual.schema.Tag;


import java.util.List;

public class TaggingFunction {

    private String functionName;

    private String rawType;

    private List<Tag> parameters;

    private String generatedTag;

    private int cost;
    
    private double quality;

    public TaggingFunction(String functionName, List<Tag> parameters, String rawType, int cost) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.rawType = rawType;
        this.cost = cost;
    }
    
    public TaggingFunction(String functionName, List<Tag> parameters, String rawType, int cost, double quality) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.rawType = rawType;
        this.cost = cost;
        this.quality = quality;
    }

    public String getRawType() {
		return rawType;
	}

	public void setRawType(String rawType) {
		this.rawType = rawType;
	}

	public String getGeneratedTag() {
		return generatedTag;
	}

	public void setGeneratedTag(String generatedTag) {
		this.generatedTag = generatedTag;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public double getQuality() {
		return quality;
	}

	public void setQuality(double quality) {
		this.quality = quality;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public void setParameters(List<Tag> parameters) {
		this.parameters = parameters;
	}

	public String getFunctionName() {
        return functionName;
    }

    public List<Tag> getParameters() {
        return parameters;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(functionName).append(": ");

        sb.append(rawType).append(": ").append(generatedTag).append(": ").append(cost).append(": ");
        if (parameters!=null) {
            for (Tag param : parameters) {
                sb.append(String.format("%s, ", param));
            }
        }
        return sb.toString();

    }
}
