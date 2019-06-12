package edu.uci.ics.perpetual.types;

import edu.uci.ics.perpetual.data.DataObject;
import edu.uci.ics.perpetual.functions.TaggingFunction;

import java.util.List;

public class DataObjectType {

    private String name;

    public List<String> getAvailableTags() {
        return null;
    }

    public List<TaggingFunction> getTFByDO() {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
