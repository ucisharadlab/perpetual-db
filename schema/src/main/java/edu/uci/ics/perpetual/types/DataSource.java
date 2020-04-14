package edu.uci.ics.perpetual.types;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class DataSource {
    // unique identifier
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

    // region Getter
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
    // endregion

    // region Setter
    // TODO I don't believe I need this.
    public void setSourceType(DataSourceType sourceType) {
        this.sourceType = sourceType;
    }
    // endregion

    public String toString() {

        return String.format("Data Source Type = %s, Function Path = %s, Function Params = %s", sourceType.getName(),
                functionPath, functionParams);

    }

    public JsonObject toJson() {
        JsonObject sb = new JsonObject();

        sb.addProperty("Id", id);
        sb.addProperty("Description", sourceDescription);
        sb.addProperty("DataSourceType", sourceType.getName());

        sb.addProperty("FunctionPath", functionPath);

        sb.addProperty("Parameters", functionParams.toString());
        return sb;
    }
}
