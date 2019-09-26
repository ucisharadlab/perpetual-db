package edu.uci.ics.perpetual.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.uci.ics.perpetual.types.DataObjectType;
import edu.uci.ics.perpetual.types.IType;

public class DataObject {
    private static JsonParser parser = new JsonParser();
    private JsonObject object;
    private DataObjectType type;

    public DataObject(String data, DataObjectType type){
        this.object = parser.parse(data).getAsJsonObject();
        this.type = type;
    }

    public DataObject(JsonObject data, DataObjectType type){
        this.object = data;
        this.type = type;
    }

    public JsonObject getObject() {
        return object;
    }

    public DataObjectType getType() {
        return type;
    }

    public JsonElement getTimeStamp(){
        return object.get("timestamp");
    }

    @Override
    public String toString() {
        return object.toString();
    }
}