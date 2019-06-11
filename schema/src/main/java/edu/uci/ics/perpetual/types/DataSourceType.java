package edu.uci.ics.perpetual.types;

import edu.uci.ics.perpetual.table.Attribute;
import edu.uci.ics.perpetual.table.Parameters;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

public class DataSourceType {
    private String name;

    // should I expend this to a hashmap ?
    private Parameters parameters;

    private HashMap<Integer, Pair<String, List<Attribute>>> sources;

    private HashMap<String, String> sourceFunctions;

    public DataSourceType(String name, List<String> params) {
        this.name = name;
        this.parameters = new Parameters(params);
        this.sources = new HashMap<>();
        this.sourceFunctions = new HashMap<>();
    }

    // region check param scheme for new source
    public boolean checkParams(List<Attribute> sourceParams) {
        // the following is a stream operation that checks
        // there is a one-to-one mapping between 'keys of Attribute' and 'this.params'.
        long nonExistCount = sourceParams.stream()
                .map(Attribute::getKey)
                .map(key -> parameters.hasParameter(key))
                .filter(r -> !r)
                .count();
        return nonExistCount == 0;
    }
    // endregion

    // region existence checking
    public boolean hasSource(int sourceId) {
        return sources.containsKey(sourceId);
    }

    public boolean hasSourceFunctions(String funcName) {
        return sourceFunctions.containsKey(funcName);
    }
    // endregion

    // region add source and acquisition function
    public void addSource(int id, String name, List<Attribute> sourceParams) {
        sources.put(id, new Pair<>(name, sourceParams));
    }

    public void addAcquisitionFunction(String funcName, String funcPath) {
        // if there is duplicate, just replace the old value
        sourceFunctions.put(funcName, funcPath);
    }
    // endregion

    // region getter
    public String getSourceName(int sourceId) {
        return sources.get(sourceId).getKey();
    }

    public String getName() {
        return name;
    }

    public Parameters getParameters() {
        return parameters;
    }
    // endregion

    public String toString() {
        StringBuilder sb = new StringBuilder();
//        sb.append(name).append(": ");
        for (String param: parameters.getParamList()) {
            sb.append(String.format("%s, ", param));
        }
        return sb.toString();

    }
}
