package edu.uci.ics.perpetual.types;

import java.util.HashMap;

public class DataSource {
    // unique identifier of DataSource, always in increasing order
    private int id;

    private String sourceDescription;

    private DataSourceType sourceType;

    private String functionPath;

    private HashMap<String, String> functionParams;

    public DataSource(int id, String sourceDescription, DataSourceType sourceType, String functionPath, HashMap<String, String> functionParams) {
        this.id = id;
        this.sourceType = sourceType;
        this.sourceDescription = sourceDescription;
        this.functionPath = functionPath;
        this.functionParams = functionParams;
    }

    public int getId() {
        return id;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public String getFunctionPath() {
        return functionPath;
    }

    public DataSourceType getSourceType() {
        return sourceType;
    }

    public HashMap<String, String> getFunctionParams() {
        return functionParams;
    }

    /**
     * This seems unnecessary, but fine.
     */
    public void setSourceType(DataSourceType sourceType) {
        this.sourceType = sourceType;
    }
}
