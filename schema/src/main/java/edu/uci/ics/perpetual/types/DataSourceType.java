package edu.uci.ics.perpetual.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class DataSourceType {
    private String name;

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

    public boolean checkParams(Collection<String> sourceParams) {
        // the following is a stream operation that checks
        // there should be an one-to-one mapping between 'keys of Attribute' and 'this.params'.
        long nonExistCount = sourceParams.stream()
                .map(key -> paramList.contains(key))
                .filter(r -> !r)
                .count();
        return nonExistCount == 0;
    }

    public boolean hasAcquisitionFunction(String funcName) {
        return sourceFunctions.containsKey(funcName.toUpperCase());
    }

    public void addAcquisitionFunction(String funcName, String funcPath) {
        // duplication function is not a use case, but
        // if there is duplicate, just replace the old value
        sourceFunctions.put(funcName.toUpperCase(), funcPath);
    }

    public String getAcquisitionFunctionPath(String funcName) {
        return sourceFunctions.get(funcName.toUpperCase());
    }

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
}
