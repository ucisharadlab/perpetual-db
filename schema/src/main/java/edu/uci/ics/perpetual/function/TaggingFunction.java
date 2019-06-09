package edu.uci.ics.perpetual.function;

import edu.uci.ics.perpetual.table.Parameters;
import edu.uci.ics.perpetual.types.RawType;

public class TaggingFunction {

    private String functionName;

    private RawType rawType;

    private Parameters parameters;

    public TaggingFunction(String functionName, Parameters parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
    }

    public String getFunctionName() {
        return functionName;
    }

    public Parameters getParameters() {
        return parameters;
    }
}
