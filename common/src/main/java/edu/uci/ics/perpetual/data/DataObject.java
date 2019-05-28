package edu.uci.ics.perpetual.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.uci.ics.perpetual.types.DataObjectType;

public class DataObject {
    private static JsonParser parser = new JsonParser();
    private JsonObject object;
    private DataObjectType type;

    public DataObject(String data, DataObjectType type){
        this.object = parser.parse(data).getAsJsonObject();
        this.type = type;
    }

    public JsonObject getObject() {
        return object;
    }

    public DataObjectType getType() {
        return type;
    }
}