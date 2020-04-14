package edu.uci.ics.perpetual.types;

import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class DataSourceType {
    private String name;

    // should I expend this to a hashmap ?
    private List<String> paramList;

    private RawType returnType;

    private HashMap<String, String> sourceFunctions;

    public DataSourceType(String name, List<String> paramList, RawType returnType) {
        this.name = name;
        this.paramList = paramList;
        this.returnType = returnType;
        this.sourceFunctions = new HashMap<>();
    }

    public DataSourceType(String name, List<String> paramList, RawType returnType, HashMap<String, String> sourceFunctions) {
        this.name = name;
        this.paramList = paramList;
        this.returnType = returnType;
        if (sourceFunctions == null) {
            sourceFunctions = new HashMap<>();
        }
        this.sourceFunctions = sourceFunctions;
    }

    // region check param scheme for new source
    public boolean checkParams(Collection<String> sourceParams) {
        // the following is a stream operation that checks
        // there should be an one-to-one mapping between 'keys of Attribute' and 'this.params'.
        long nonExistCount = sourceParams.stream()
                .map(key -> paramList.contains(key))
                .filter(r -> !r)
                .count();
        return nonExistCount == 0;
    }
    // endregion

    // region Acquisition Function
    public boolean hasAcquisitionFunction(String funcName) {
        return sourceFunctions.containsKey(funcName.toUpperCase());
    }

    public void addAcquisitionFunction(String funcName, String funcPath) {
        // duplication is not a use case, but
        // if there is duplicate, just replace the old value
        sourceFunctions.put(funcName.toUpperCase(), funcPath);
    }

    public String getAcquisitionFunctionPath(String funcName) {
        return sourceFunctions.get(funcName.toUpperCase());
    }
    // endregion

    // region Getter
    public List<String> getParamList() {
        return paramList;
    }

    public HashMap<String, String> getSourceFunctions() {
        return sourceFunctions;
    }

    public String getName() {
        return name;
    }

    public RawType getReturnType() {
        return returnType;
    }

    // endregion

    public String toString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(name).append(": ");
        for (String param : paramList) {
            sb.append(String.format("%s, ", param));
        }
        sb.append("Acquisition Functions, ").append(sourceFunctions);
        return sb.toString();

    }

    public JsonObject toJson() {
        JsonObject sb = new JsonObject();

        sb.addProperty("Name", name);
        sb.addProperty("ReturnType", returnType.getName());
        sb.addProperty("ReturnType", returnType.getName());

        StringBuilder builder = new StringBuilder();
        for (String param : paramList) {
            builder.append(String.format("%s, ", param));
        }
        sb.addProperty("Params", builder.toString());

        builder = new StringBuilder();
        for (String function : sourceFunctions.keySet()) {
            builder.append(String.format("%s, ", function));
        }
        sb.addProperty("Acquisition Functions", builder.toString());
        return sb;
    }

}