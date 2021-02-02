package edu.uci.ics.perpetual.types;

import java.util.HashMap;
import java.util.List;

public class Table {

    private String name;

    private HashMap<String, String> attributeDataTypes;
    private List<String> attributeObservables;

    public Table(String name, HashMap<String, String> attributeDataTypes, List<String> attributeObservables) {
        this.name = name;
        this.attributeDataTypes = attributeDataTypes;
        this.attributeObservables = attributeObservables;
    }

    // region getter and setter
    public String getName() {
        return name;
    }

    public HashMap<String, String> getAttributeDataTypes() {
        return attributeDataTypes;
    }

    public List<String> getAttributeObservables() {
        return attributeObservables;
    }

    public void setAttributeObservables(List<String> attributeObservables) {
        this.attributeObservables = attributeObservables;
    }

    // endregion

}
