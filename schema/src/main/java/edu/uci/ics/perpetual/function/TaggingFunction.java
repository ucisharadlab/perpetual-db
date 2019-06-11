package edu.uci.ics.perpetual.function;

import edu.uci.ics.perpetual.schema.Tag;
import edu.uci.ics.perpetual.table.Parameters;
import edu.uci.ics.perpetual.types.RawType;

import java.util.List;

public class TaggingFunction {

    private String functionName;

    private String rawType;

    private List<Tag> parameters;

    private String generatedTag;

    private int cost;

    public TaggingFunction(String functionName, List<Tag> parameters, String rawType, int cost) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.rawType = rawType;
        this.cost = cost;
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
